package Codemates.Discord.TestBot;

//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.security.auth.login.LoginException;
import java.awt.Color;
import java.text.MessageFormat;
import java.util.Calendar;

class CircleInfo {
	private String roomid="未定義";
	private boolean roomoc=false;
	
	public void setRoomID(String roomid) {
		this.roomid=roomid;
	}
	public String getRoomID() {
		return roomid;
	}
	public void setRoomOC(boolean roomoc) {
		this.roomoc=roomoc;
	}
	public boolean getRoomOC() {
		return roomoc;
	}
	public void printRoomInfo(TextChannel channel) {
		EmbedBuilder eb=new EmbedBuilder();
		String status=this.getRoomOC()?"開いています" : "閉まっています";
			eb.addField("部屋情報",MessageFormat.format("\nステータス: {0}\n活動場所: {1}", status, this.getRoomID()), false);
			//eb.setFooter("サークル活動部屋空き状況");
		    eb.setColor(Color.BLUE);
		    channel.sendMessageEmbeds(eb.build()).queue();
		}
}

class BotInfo {
	private String version;
	private String developer;
	private String update;
	
	public void setVersion(String version) {
		this.version=version;
	}
	public String getVersion() {
		return version;
	}
	public void setDeveloper(String developer) {
		this.developer=developer;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setUpdate(String update) {
		this.update=update;
	}
	public String getUpdate() {
		return update;
	}
	public void printBotInfo(SlashCommandInteractionEvent event) {
	    EmbedBuilder eb = new EmbedBuilder();

	    String description = "このBOTはCodeMates-DiscordServerの利便性向上を目的としています。";
	    String versionInfo = MessageFormat.format(
	        "\nVersion: {0}\nDeveloper: {1}\nLastUpdate: {2}",
	        this.getVersion(),
	        this.getDeveloper(),
	        this.getUpdate()
	    );
	    eb.addField(description, versionInfo, false);
	    //eb.setFooter("BOTの説明");
	    eb.setColor(Color.BLUE);

	    event.replyEmbeds(eb.build()).setEphemeral(true).queue();
	}
}

public class CodeMatesBot extends ListenerAdapter {
	private static JDA jda = null;
	private static final String BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");; //環境変数
	private static final BotInfo botinfo = new BotInfo();
	private static final CircleInfo circleinfo=new CircleInfo();

	public static void main(String[] args) throws LoginException{
		//CircleInfo
		circleinfo.setRoomID("エッグドーム5階　研修室1,2");
		
		//BotInfo
		botinfo.setVersion("Closed-Test");
		botinfo.setDeveloper("RyosukeNagashima");
		botinfo.setUpdate("16/06/25 DD/MM/YY");

		//Channel,RoleID
		String BotChannelID="1382708384221888562";//Bot用TextChannel
		String ExecuteRoleID="1382708559120306238";//幹部ロール
		
		
		jda = JDABuilder.createDefault(BOT_TOKEN)
                .setRawEventsEnabled(true)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new CodeMatesBot())
                .setActivity(Activity.playing("なにか"))
                .build();
		jda.updateCommands().queue();
	//("CommandName","CommandDiscription")
		jda.upsertCommand("info","BOTの説明を表示。").queue();
		//jda.upsertCommand("room-status","活動部屋の場所と開き状況確認。").queue(); // !!!!OUTDATED!!!!
		jda.upsertCommand("room-oc-update","活動部屋の空き状況更新。!幹部のみ実行可能").queue();
		//jda.upsertCommand("room-id-update","活動部屋の場所を更新。 !幹部のみ実行可能").queue(); //要相談
		
		/*	方針
		 * 1.サークル部屋の鍵空き情報。（スラッシュコマンドによるトグル式） done
		 * 2.名簿とDiscord内ニックネームの照合。将来的:ニックネームの編集（Excel名簿参照方式）
		 * 3.ゲームサーバーの空き情報
		 */
	}
	
//	//forMassage
//	@Override
//    public void onMessageReceived(MessageReceivedEvent event) {
//        if (event.getAuthor().isBot()) return;//IgnoreBotMessage
//        
//        String inmsg = event.getMessage().getContentRaw();//ReceiveMessage
//        String outmsg=switch (inmsg) {
//        case "動作確認" -> "動作しています。";
//        default -> "";
//        };
//        
//         if(!outmsg.isEmpty()) event.getChannel().sendMessage(outmsg).queue();
//    }
	
	//forSlashCommand
	 @Override
	    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	  String cmd=event.getName();//ReceiveCommand
	  String outmsg="";

	        switch(cmd) {
//	            //room-statusコマンド !!!!OUTDATED!!!!
//	            case "room-status"->{
//	            	 String oc = circleinfo.getRoomOC() ? "解錠" : "施錠";
//	            	    outmsg = MessageFormat.format("活動場所は{0}です。現在{1}されています。", circleinfo.getRoomID(), oc);
//	            	    event.reply(outmsg).setEphemeral(true).queue();
//	            }
	            //room-status-updateコマンド　ロールによる実行制限
	            case "room-oc-update"-> {
	            	if(hasRoleById(event,ExecuteRoleID)){
	            		outmsg="部屋の開き状況を選択してください。";
	            		event.reply(outmsg).setEphemeral(true).addActionRow(
	            				Button.primary("unlock", "解錠"),
	            				Button.danger("lock","施錠")
	            		).queue();
	            	}else {event.reply("実行する権限がありません。").setEphemeral(true).queue();}
	            }
	            //infoコマンド
	            case "info" -> {
	            	botinfo.printBotInfo(event);
	            }
	            default -> event.reply("不明なコマンドです。\n").setEphemeral(true).queue();
	        }
	    }

	    //forButton
	    @Override
	    public void onButtonInteraction(ButtonInteractionEvent event) {
	    	TextChannel channel = jda.getTextChannelById(BotChannelID);
	        if (channel == null) return;
	        
	        switch (event.getComponentId()) {
            	case "unlock" -> {
            		circleinfo.setRoomOC(true);
            		event.reply("「解錠」に更新しました。").setEphemeral(true).queue();
            		circleinfo.printRoomInfo(channel);
            	}
            	case "lock" -> {
            		circleinfo.setRoomOC(false);
            		event.reply("「施錠」に更新しました。今日もお疲れ様でした。").setEphemeral(true).queue();
            		circleinfo.printRoomInfo(channel);
            	}
	        }
	    }
	    
	    private boolean hasRoleById(SlashCommandInteractionEvent event, String roleId) {
	    	return event.getMember() != null &&
	        event.getMember().getRoles().stream().anyMatch(role -> role.getId().equals(roleId));
	    }
	    //日付が変わった場合、施錠状態にする
	    public static void dailyReset() {
	        Calendar calendar = Calendar.getInstance();
	        if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
	                circleinfo.setRoomOC(false);
	        }
	    }
}
