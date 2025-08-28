package Codemates.Discord.Bot;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandData {
	public static final Map<String,String> CMD_INFO=createCommandInfo();
	
	private static Map<String,String> createCommandInfo() {
		Map<String,String> map=new LinkedHashMap<>();
        map.put("info","BOTの説明を表示。");
        map.put("help","コマンド一覧の表示。");
        map.put("updateinfo","直近のアップデート内容の表示。");
        map.put("room-status-update","活動部屋の空き状況を更新。");
        map.put("update-announce", "開発者用");
        
        
        return map;
	}
}
