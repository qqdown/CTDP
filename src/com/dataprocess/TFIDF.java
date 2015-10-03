package com.dataprocess;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	
	public void Calculate()
	{
		Set<TWord> words = TCalculate(fileList);
		List<TWord> wL = new ArrayList<TWord>(words);
		for(int i=0; i<fileList.size(); i++)
		{
			TFile f = fileList.get(i);
			List<SparseMatrixElement> smeList = f.smeList;
			smeList.clear();
			for(int j=0; j<words.size(); j++)
			{
				double dfitf = f.getTFIDF(wL.get(j).content);
				if(dfitf >= 0.00001)
				{
					smeList.add(new SparseMatrixElement(i,j,dfitf));
				}
			}
		}
	}
	
	public static Set<TWord> TCalculate(List<TFile> fileList)
	{
		List<HashMap<TWord, Integer>> TCMapList = new ArrayList<HashMap<TWord, Integer>>();
		HashMap<TWord, List<Integer>> TDMap = new HashMap<TWord, List<Integer>>(); 
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
					TCMap.put(w, 1);
					
				//记录IDF中每个Term出现在文件中的个数
				if(TDMap.containsKey(w))
				{
					List<Integer> list = TDMap.get(w);
					if(!list.contains(i))
					{
						list.add(i);
						TDMap.put(w, list);
					}
						
				}
				else
				{
					List<Integer> list = new ArrayList<Integer>();
					list.add(i);
					TDMap.put(w, list);
				}
			}
			
			TCMapList.add(TCMap);
		}

		for(int i=0; i<fileList.size(); i++)
		{
			TFile file = fileList.get(i);
			int wordsSize = file.getWordsSize();
			HashMap<TWord, Integer> TCMap = TCMapList.get(i);

			for(TWord word : file.getWords())
			{
				Integer tc = TCMap.get(word);
				double TD = tc*1.0/wordsSize;
				double IDF = Math.log10(fileList.size()*1.0/TDMap.get(word).size());//计算IDF
				word.TFIDF = TD*IDF;
			}
		}
		
		return TDMap.keySet();
	}
}

