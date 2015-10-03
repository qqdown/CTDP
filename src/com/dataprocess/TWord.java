package com.dataprocess;

import java.util.ArrayList;

public class TWord 
{
	public String content;
	public double TFIDF;
	
	public TWord(String content)
	{
		this.content = content;
		TFIDF = 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)                                      //先检查是否其自反性，后比较other是否为空。这样效率高
			return true;
		if(obj == null)         
			return false;
		if(!(obj instanceof TWord))
			return false;
			  
		final TWord tw = (TWord)obj;
			  
		return content.equals(tw.content);
	}
	
	@Override
	public int hashCode() {
		 int result = content.hashCode();
		 return result;
	}
}
