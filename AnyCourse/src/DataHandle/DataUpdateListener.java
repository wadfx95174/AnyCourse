package DataHandle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;

import org.apache.mahout.cf.taste.common.TasteException;

import RecommenderSystem.RecommendationResult;
public class DataUpdateListener {
	Timer timer;

	public void contextInitialized(ServletContextEvent event) {
//		TimerTask task = new TimerTask() {
//			@Override
//			public void run() {
//				try {
//					RecommendationResult.itemEuclideanNoPref();
//				} catch (TasteException | IOException | SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		};
//		timer = new Timer();
//		//12小時跑一次
//		timer.schedule(task,0, 12*60*60*1000);
	}
	
	public void contextDestroyed(ServletContextEvent event) {
//		timer.cancel();
	}
}
