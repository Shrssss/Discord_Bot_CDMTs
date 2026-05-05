package Codemates.Discord.Bot.dto;

import java.util.*;

public class ItemData {
	
	//備品に関するデータ成形
	//
	//	Item（備品）
	//	└─ itemId(Key) :integer
	//	   ├─ itemName :String
	//	   ├─ quantity　:integer(if item is uncountable: null) /nullAble
	//	   ├─ isRentable :boolean
	//	   ├─ storageLocation :String
	//	   └─ usageHistory(array:Histories/contains rent) /nullAble
	//	      └─ rent :object
	
	
	/** 備品番号 */
	private Integer itemid;
	/** 備品名 */
	private String itemName;
	/** 備品在庫（不可算ならばnull） */
	private Optional<Integer> quantity;
	/** 貸し出し可否 */
	private Boolean isRentable;
	/** 保管場所 */
	private String storageLocation;
	/** 貸し出し・使用履歴 */
	private List<rentData> usageHistory;
	
}
