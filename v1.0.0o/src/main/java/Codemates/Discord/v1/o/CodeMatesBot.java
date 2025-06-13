package Codemates.Discord.v1.o;

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
	    eb.setColor(Color.BLUE);

	    event.replyEmbeds(eb.build()).setEphemeral(true).queue();
	}
}

public class CodeMatesBot extends ListenerAdapter {
	private static JDA jda = null;
	private static final String BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");;
	private static final BotInfo botinfo = new BotInfo();
	private static final CircleInfo circleinfo=new CircleInfo();

	public static void main(String[] args) throws LoginException{
		//CircleInfo
		circleinfo.setRoomID("エッグドーム5階　研修室1,2");
		
		//BotInfo
		botinfo.setVersion("v1.0.0o");
		botinfo.setDeveloper("RyosukeNagashima");
		botinfo.setUpdate("12/06/25 DD/MM/YY");
		
		
		jda = JDABuilder.createDefault(BOT_TOKEN)
                .setRawEventsEnabled(true)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new CodeMatesBot())
                .setActivity(Activity.playing("オープンテスト段階"))
                .build();
		jda.updateCommands().queue();
	//("CommandName","CommandDiscription")
		jda.upsertCommand("info","BOTの説明").queue();
		jda.upsertCommand("room-status-update","活動部屋の空き状況更新。!幹部のみ実行可能").queue();
	}

	//forSlashCommand
	 @Override
	    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	  String cmd=event.getName();//ReceiveCommand
	  String outmsg="";

	        switch(cmd) {
	            //room-status-updateコマンド　ロールによる実行制限
	            case "room-status-update"-> {
	            	if(hasRoleById(event,"任意のロールID")){
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
	    	TextChannel channel = jda.getTextChannelById("任意のチャンネルID");
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
