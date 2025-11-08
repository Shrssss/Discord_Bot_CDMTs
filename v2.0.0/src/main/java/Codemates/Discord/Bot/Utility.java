package Codemates.Discord.Bot;

import java.util.Calendar;
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
	        	circleInfo.setRoomOp(false);
	        	circleInfo.setRoomId("");
	        }
        },0,1,TimeUnit.HOURS); //(処理内容,遅延,実行間隔,時間単位)
    }
    
    public static boolean hasRoleById(Member mem,String roleid) {
    	return	mem.getRoles().stream()
                 .anyMatch(role -> role.getId().equals(roleid));
    }
}
