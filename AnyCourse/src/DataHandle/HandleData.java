package DataHandle;

import java.io.BufferedReader;
import java.io.FileReader;
//把影片網址切出課程ID，拼接成圖片網址
public class HandleData {

	public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:/Users/Chen/Desktop/專題爬蟲/專題爬蟲單元影片/all.csv"));
            reader.readLine();
            String line = null;
            String last;
            while((line=reader.readLine())!=null){
                String item[] = line.split(",");
                last = item[4];
                if(last.split("/")[2].equals("www.youtube.com")) {
                	System.out.println("https://i.ytimg.com/vi/" + last.split("/")[4] + "/hqdefault.jpg");
                }
                else if(last.split("/")[2].equals("140.112.161.119")) {
                	System.out.println("http://ocw.aca.ntu.edu.tw/ntu-ocw/files/110px/" + last.split("/")[4] + ".jpg");
                }
                else {
                	System.out.println(last);
                }
            }
            
        }catch(ArrayIndexOutOfBoundsException e) {
        	e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
