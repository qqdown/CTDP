package com.dataprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TFIDF
{
	List<TFile> fileList;
	
	public TFIDF()
	{
		fileList = new ArrayList<TFile>();
	}
	
	public TFIDF(List<TFile> fileList)
	{
		this.fileList = fileList;
	}
	
	public void addFile(String fileContent)
	{
		TFile file = new TFile(fileContent);
		fileList.add(file);		
	}
	
	public void calculate()
	{
		Set<TWord> words = TCalculate(fileList);
		List<TWord> wL = new ArrayList<TWord>(words);
		//以稀疏矩阵的形式输出
		for(int i=0; i<fileList.size(); i++)
		{
			TFile f = fileList.get(i);
			List<SparseMatrixElement> smeList = f.smeList;
			smeList.clear();
			for(int j=0; j<words.size(); j++)
			{
				double dfitf = f.getTFIDF(wL.get(j).content);
				if(dfitf >= 0.00001)//认为小于0.00001为0
				{
					smeList.add(new SparseMatrixElement(i,j,dfitf));
				}
			}
		}
	}
	
	//计算出每个单词的TF-IDF
	public static Set<TWord> TCalculate(List<TFile> fileList)
	{
		List<HashMap<TWord, Integer>> TCMapList = new ArrayList<HashMap<TWord, Integer>>();
		HashMap<TWord, List<Integer>> IDFMap = new HashMap<TWord, List<Integer>>(); 
		for(int i=0; i<fileList.size(); i++)
		{
			TFile tfile = fileList.get(i);
			//获取TD中的TC
			HashMap<TWord, Integer> TCMap = new HashMap<TWord, Integer>();
			
			for(TWord w : tfile.getWords())
			{
				if(TCMap.containsKey(w))
				{
					Integer count = TCMap.get(w);
					TCMap.put(w, count+1);
				}
				else
				{
					TCMap.put(w, 1);
					//同一个词可能在一个文件中出现多次，但是在计算idf时，同一个文件不能重复计算多次
					//记录IDF中每个Term出现在文件中的个数
					if(IDFMap.containsKey(w))
					{
						List<Integer> list = IDFMap.get(w);
						if(!list.contains(i))
						{
							list.add(i);
							IDFMap.put(w, list);
						}
							
					}
					else
					{
						List<Integer> list = new ArrayList<Integer>();
						list.add(i);
						IDFMap.put(w, list);
					}
				}
			}	
			TCMapList.add(TCMap);
		}

		for(int i=0; i<fileList.size(); i++)
		{
			TFile file = fileList.get(i);
			HashMap<TWord, Integer> TCMap = TCMapList.get(i);

			for(TWord word : file.getWords())
			{
				Integer tc = TCMap.get(word);
				double TF = tc*1.0;
				double IDF = Math.log10(fileList.size()*1.0/IDFMap.get(word).size());//计算IDF
				word.TFIDF = TF*IDF;
			}
		}
		
		return IDFMap.keySet();
	}
}

