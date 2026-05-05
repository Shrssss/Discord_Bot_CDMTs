package Codemates.Discord.Bot.dto;

public class StudentData {
	
	//学生(サークルメンバー)データ成形
	//
	//	Student（サークルメンバー）
	//	└─ studentId(Key) :String
	//	   ├─ userName :String
	//	   ├─ userEmail :String
	//	   ├─ userDiscordId :String
	//	   └─ isCircleMember :boolean
	
	/** 学籍番号 */
	private String studentId;
	/** 名前 */
	private String userName;
	/** 個人Email(学校アドレスを使用しないこと) */
	private String userEmail;
	/** DiscordアカウントのユーザーID */
	private String userDiscordId;
	/** サークルメンバーかどうか（退部、卒業の場合これをfalseにすること） */
	private Boolean isCircleMember;
	
}
