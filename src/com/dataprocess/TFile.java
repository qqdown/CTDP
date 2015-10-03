package com.dataprocess;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TFile 
{
	private String filePath;
	private List<TWord> wordList;
	private HashMap<String, TWord> wordMap;
	
	public List<SparseMatrixElement> smeList = new ArrayList<SparseMatrixElement>();
	public TFile(String filePath)
	{
		this.filePath = filePath;
		wordList = new ArrayList<TWord>();
		wordMap = new HashMap<String, TWord>();
	}
	
	public void addWord(TWord word)
	{
		wordList.add(word);
		wordMap.put(word.content, word);
	}
	
	public List<TWord> getWords()
	{
		return wordList;
	}
	
	public int getWordsSize()
	{
		return wordList.size();
	}
	
	public TWord getWord(int i)
	{
		return wordList.get(i);
	}
	
	public String getFilePath()
	{
		return filePath;
	}
	
	public double getTFIDF(String word)
	{
		TWord tword = wordMap.get(word);
		if(tword == null)
			return 0;
		return tword.TFIDF;
	}
}
