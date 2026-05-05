package Codemates.Discord.Bot.dto;

import java.time.*;

public class rentData {
	
	//仕様、貸出を行った記録データ成形
	//
	//	rent（貸し出し記録）
	//	└─ rentId(key) :String
	//	   ├─ studentId :String
	//	   └─ rentDate :LocalDateTime
	
	/** 貸し出し・使用記録番号(randomUUIDを使用) */
	private String rentid;
	/** 学籍番号（外部） */
	private String studentId;
	/** 貸し出し・使用日時 */
	private LocalDateTime rentDate;
	
}
