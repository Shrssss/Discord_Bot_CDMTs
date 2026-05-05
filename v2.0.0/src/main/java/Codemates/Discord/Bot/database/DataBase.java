package Codemates.Discord.Bot.database;

public class DataBase {
	
	//テーブル設計
	//
	//	Item（備品）
	//	└─ itemId(Key) :integer
	//	   ├─ itemName :String
	//	   ├─ quantity　:integer(if item is uncountable: null) /nullAble
	//	   ├─ isRentable :boolean
	//	   ├─ storageLocation :String
	//	   └─ usageHistory(array:Histories/contains rent) /nullAble
	//	      └─ rent :object
	//
	//	rent（貸し出し・使用記録）
	//	└─ rentId(key) :String
	//	   ├─ studentId :String
	//	   └─ rentDate :LocalDateTime
	//
	//	User（ユーザー）
	//	└─ studentId(Key) :String
	//	   ├─ userName :String
	//	   ├─ userEmail :String
	//	   ├─ userDiscordId :String
	//	   └─ isCircleMember :boolean
	//
	
	public void connect() {
		try {
			//DB接続の処理
		}catch(Exception e) {
			
		}
	}
	
}
