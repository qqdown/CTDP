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
		//��ϡ��������ʽ���
		for(int i=0; i<fileList.size(); i++)
		{
			TFile f = fileList.get(i);
			List<SparseMatrixElement> smeList = f.smeList;
			smeList.clear();
			for(int j=0; j<words.size(); j++)
			{
				double dfitf = f.getTFIDF(wL.get(j).content);
				if(dfitf >= 0.00001)//��ΪС��0.00001Ϊ0
				{
					smeList.add(new SparseMatrixElement(i,j,dfitf));
				}
			}
		}
	}
	
	//�����ÿ�����ʵ�TF-IDF
	public static Set<TWord> TCalculate(List<TFile> fileList)
	{
		List<HashMap<TWord, Integer>> TCMapList = new ArrayList<HashMap<TWord, Integer>>();
		HashMap<TWord, List<Integer>> IDFMap = new HashMap<TWord, List<Integer>>(); 
		for(int i=0; i<fileList.size(); i++)
		{
			TFile tfile = fileList.get(i);
			//��ȡTD�е�TC
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
					//ͬһ���ʿ�����һ���ļ��г��ֶ�Σ������ڼ���idfʱ��ͬһ���ļ������ظ�������
					//��¼IDF��ÿ��Term�������ļ��еĸ���
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
				double IDF = Math.log10(fileList.size()*1.0/IDFMap.get(word).size());//����IDF
				word.TFIDF = TF*IDF;
			}
		}
		
		return IDFMap.keySet();
	}
}

