package Codemates.Discord.Bot;

import java.io.BufferedReader;
//import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.entities.Member;

public class Utility {
	
	
    //UTC12時(21時)になった場合、施錠状態にする
    public static void dailyReset(CircleInfo circleInfo) {
    	ScheduledExecutorService scheduler=Executors.newScheduledThreadPool(1); //1本のスレッド
        scheduler.scheduleAtFixedRate(() -> {
        	Calendar calendar = Calendar.getInstance();
	        if (calendar.get(Calendar.HOUR_OF_DAY)==12) {
	        	circleInfo.setRoomOC(false);
	        }
        },0,1,TimeUnit.HOURS); //(処理内容,遅延,実行間隔,時間単位)
    }
    
    public static boolean hasRoleById(Member mem,String roleid) {
    	return	mem.getRoles().stream()
                 .anyMatch(role -> role.getId().equals(roleid));
    }
    
    public static ArrayList<String> readCSV(String csvfile) {
    	String line;
    	ArrayList<String> lines=new ArrayList<>();
    	
    	try(BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(csvfile),StandardCharsets.UTF_8))){
    		while((line=br.readLine())!=null) {
    			lines.add(line);
    		}
    	}catch(FileNotFoundException e) {
    		System.err.println("指定ファイルが見つかりません:"+csvfile);
    		e.printStackTrace();
    	}catch (IOException e) {
    		System.err.println("ファイル読み込み中にエラーが発生しました:"+e.getMessage());
    		e.printStackTrace();
    	}
    	
    	return lines;
    }
    
    public static String[] getLine(int linenumber,ArrayList<String> lines) {
    	if(linenumber<0 || linenumber>=lines.size()) {
    		throw new IllegalArgumentException("行番号が範囲外です: "+linenumber);
    	}
    	
    	return lines.get(linenumber).split(",");
    }
}
