package com.dataprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;


public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String folderPath = "";
		boolean forceOverwrite = false;
		for(String arg : args)
		{
			if(arg.startsWith("-"))
			{
				for(int i=1; i<arg.length(); i++)
				{
					if(arg.charAt(i) == 'h')
					{
						OutputHelp();
					}
					else if(arg.charAt(i) == 'f')
					{
						forceOverwrite = true;
					}
				}
			}
			else
				folderPath = arg;
		}
		if(folderPath=="")
		{
			return;
		}
			
		File folder = new File(folderPath);
		
		if(!folder.exists())
		{
			System.out.println("数据文件夹: " + folder.getAbsolutePath() + " 不存在");
			System.out.println("已退出");
			return;
		}
		else
		{
			File[] classfiles = folder.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					if(pathname.getName().endsWith(".txt"))
						return true;
					return false;
				}
			});

			List<TFile> tfileList = new ArrayList<TFile>();
			HashMap<String, List<TFile>> filePathMap = new HashMap<String, List<TFile>>();
			for(File file : classfiles)//对每个文件进行分词
			{			
				FileInputStream fis = new FileInputStream(file);   
				InputStreamReader isr = new InputStreamReader(fis, "UTF-8");  
				BufferedReader br = new BufferedReader(isr);  
				String s;  
	            while ((s = br.readLine()) != null) {
	            	TFile tfile = new TFile(file.getName());
	            	
	            	StringReader sr = new StringReader(s);
	        		IKSegmenter ik = new IKSegmenter(sr, true);
	        		Lexeme lex = null;
	        		while((lex=ik.next()) != null)//获取分词
	        		{
	        			TWord tword = new TWord(lex.getLexemeText());
	        			tfile.addWord(tword);
	        		} 	
	        		tfileList.add(tfile);
	        		if(!filePathMap.containsKey(tfile.getFilePath()))
	        			filePathMap.put(tfile.getFilePath(), new ArrayList<TFile>());

	        		filePathMap.get(tfile.getFilePath()).add(tfile);
	            }  
	            
	            br.close();
			}
			System.out.println(tfileList.size());
			long startMili=System.currentTimeMillis();// 当前时间对应的毫秒数
			TFIDF tfidf = new TFIDF(tfileList);
			tfidf.calculate();//计算tfidf

			Iterator<Entry<String, List<TFile>>> iter = filePathMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, List<TFile>> entry = (Map.Entry<String, List<TFile>>) iter.next();
				String path = entry.getKey();
				List<TFile> files = entry.getValue();
				File file = new File("output\\" + path);
				if(!file.getParentFile().exists())
					file.getParentFile().mkdirs();
				if (!file.exists()) 
				    file.createNewFile();
				else
				{
					if(!forceOverwrite)
					{
						System.out.println(file.getAbsolutePath() + "已经存在，无法写入！\n如果需要覆盖这个文件，请使用-f命令！");
						System.out.println("已退出");
						return;
					}
				}
				
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				for(TFile tf : files)
				{	
					for(SparseMatrixElement e : tf.smeList)
					{
						bw.write(e.toString() + " ");
					}
					bw.newLine();

				}
				bw.close();
				
			}
			
			long endMili=System.currentTimeMillis();
			System.out.println("共花时间" + (endMili-startMili)/1000.0 + "秒");
			System.out.println("完成！");
		}
	}

	public static void OutputHelp()
	{
		String helpStr = "CTDP.jar [-f] [-h] [数据文件夹]\n"
				+ "\t输出目录为output！\n"
				+ "\t-f 输出将覆盖原有文件\n"
				+ "\t-h 输出帮助";
		System.out.println(helpStr);
	}
}
