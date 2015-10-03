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
		if(this == obj)                                      //�ȼ���Ƿ����Է��ԣ���Ƚ�other�Ƿ�Ϊ�ա�����Ч�ʸ�
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
