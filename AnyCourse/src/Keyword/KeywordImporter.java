package Keyword;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import anycourse.KeyWordAnalyzer;

public class KeywordImporter
{	
	private enum importMethod {COURSE, UNIT};

	// 取得課程關鍵字，參數 n 個關鍵字
	public static void importKeywords(importMethod method)
	{
		KeyWordAnalyzer keyWordAnalyzer = new KeyWordAnalyzer();
		KeywordManager manager = new KeywordManager();
		ArrayList<List<Entry<String, Double>>> keywordList;
		switch(method)
		{
		case COURSE:
			keyWordAnalyzer.importCourse(manager.getCourse());
			keywordList = keyWordAnalyzer.getKeyWordsWithTFIDF(5);
			for (int i = 1; i <= keywordList.size(); i++)
			{
				manager.insertCourseKeyword(keywordList.get(i-1), i);
			}
			break;
		case UNIT:
			keyWordAnalyzer.importUnit(manager.getUnit());
			keywordList = keyWordAnalyzer.getKeyWordsWithTFIDF(5);
			for (int i = 1; i <= keywordList.size(); i++)
			{
				manager.insertUnitKeyword(keywordList.get(i-1), i);
			}
			break;
		}
	}
	
	public static void main(String []args)
	{
		importKeywords(importMethod.UNIT);
	}
}
