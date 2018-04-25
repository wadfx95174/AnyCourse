package Keyword;

import anycourse.KeyWordAnalyzer;

public class KeywordImporter
{	
	public static void main(String []args)
	{
		KeyWordAnalyzer keyWordAnalyzer = new KeyWordAnalyzer();
// 取得課程關鍵字，參數 n 個關鍵字
		keyWordAnalyzer.getCourseKeyWord(5);
		
		
// 取得單元關鍵字，參數 n 個關鍵字
//		keyWordAnalyzer.getUnitKeyWord(5);
	}
}
