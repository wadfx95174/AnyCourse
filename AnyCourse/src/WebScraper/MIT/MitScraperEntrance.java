package WebScraper.MIT;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import RecommenderSystem.RecommendationResult;

public class MitScraperEntrance {
	public static void main(String[] args) throws IOException, SQLException {
		try {
			List<RecommendedItem> test = RecommendationResult.designatedItemRecommendedResult(1,284,20);
			for(RecommendedItem r:test) {
				System.out.println(r.getItemID());
			}
			RecommendationResult.Evaluate();
			RecommendationResult.EvaluateBooleanPref();
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
	}
}
