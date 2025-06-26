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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

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
			eb.addField("部屋情報",MessageFormat.format("\nステータス: {0}\n活動場所: {1}",status,this.getRoomID()),false);
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
	    String versioninfo = MessageFormat.format(
	        "\nVersion: {0}\nDeveloper: {1}\nLastUpdate: {2}",
	        this.getVersion(),
	        this.getDeveloper(),
	        this.getUpdate()
	    );
	    eb.addField(description, versioninfo, false);
	    eb.setColor(Color.BLUE);

	    event.replyEmbeds(eb.build()).setEphemeral(true).queue();
	}
	public void printUpdateInfo(SlashCommandInteractionEvent event) {
		EmbedBuilder eb =new EmbedBuilder();

		String description=MessageFormat.format("[{0}]\n dailyResetメソッドをUTC基準に変更。",this.getVersion());
					eb.addField("更新内容",description,false);
					eb.setColor(Color.BLUE);
		event.replyEmbeds(eb.build()).setEphemeral(true).queue();
	}
    public void printHelp(SlashCommandInteractionEvent event,ArrayList<String> cmdname,Map<String,String> cmdinfo) {
        EmbedBuilder eb=new EmbedBuilder();
            eb.setTitle("コマンド一覧");
            for (String name : cmdname) {
                String command="/"+name;
                String description=cmdinfo.getOrDefault(name, "説明がありません。");
                eb.addField(command,description,false);
            }
            eb.setColor(Color.BLUE);
        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }
    
}


public class CodeMatesBot extends ListenerAdapter {
	private static JDA jda = null;
	private static final String BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");
	private static final BotInfo botinfo = new BotInfo();
	private static final CircleInfo circleinfo=new CircleInfo();
	private static final ArrayList<String> cmdname=new ArrayList<>();
	private static final Map<String,String> cmdinfo=new HashMap<>();

	public static void main(String[] args) throws LoginException{
		//CircleInfo
		circleinfo.setRoomID("エッグドーム5階　研修室1,2");
		
		//BotInfo
		botinfo.setVersion("v1.2.1o");
		botinfo.setDeveloper("RyosukeNagashima");
		botinfo.setUpdate("26/06/25 DD/MM/YY");
		
        //CommandArray
        cmdname.add("info");cmdname.add("help");cmdname.add("updateinfo");cmdname.add("room-status-update");
        
        cmdinfo.put(cmdname.get(0),"BOTの説明を表示。");cmdinfo.put(cmdname.get(1),"コマンド一覧の表示。");
        cmdinfo.put(cmdname.get(2),"直近のアップデート内容の表示。");cmdinfo.put(cmdname.get(3),"活動部屋の空き状況を更新。");
        
        
		jda = JDABuilder.createDefault(BOT_TOKEN)
                .setRawEventsEnabled(true)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new CodeMatesBot())
                .setActivity(Activity.playing("オープンテスト段階"))
                .build();
		jda.updateCommands().queue();
    
        for(int i=0;i<cmdinfo.size();i++) {
            jda.upsertCommand(cmdname.get(i),cmdinfo.get(cmdname.get(i))).queue();
        }
        
        dailyReset(); //毎時、時間を参照
	}

	//forSlashCommand
	 @Override
	    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	  String cmd=event.getName();//ReceiveCommand
	  String outmsg="";



            //hasRoleでUpdateAnnounce作りたい

            
	        switch(cmd) {
	            //room-status-updateコマンド
	            case "room-status-update"-> {
	            		outmsg="部屋の開き状況を選択してください。";
    	            	event.reply(outmsg).setEphemeral(true).addActionRow(
	            				Button.primary("unlock", "解錠"),
	            				Button.danger("lock","施錠")
	            		).queue();
	            }
	            //infoコマンド
	            case "info" -> {
	            	botinfo.printBotInfo(event);
	            }
				 //updatelogコマンド
				 case "updateinfo" -> {
					botinfo.printUpdateInfo(event);
				 }
                //helpコマンド
                case "help" -> {
                    botinfo.printHelp(event,cmdname,cmdinfo);
                }
	            default -> event.reply("不明なコマンドです。\n").setEphemeral(true).queue();
	        }
	    }

        //スパム対策のタイムアウト実装したいよね
        
	    //forButton
	    @Override
	    public void onButtonInteraction(ButtonInteractionEvent event) {
	    	TextChannel channel = jda.getTextChannelById("1384067026871390208");
	    	//TextChannel channel = jda.getTextChannelById("1382708384221888562"); //THIS ID IS FOR TEST SERVER
	        if (channel == null) return;
	        
	        switch (event.getComponentId()) {
            	case "unlock" -> {
                    if(circleinfo.getRoomOC()==false){
                		circleinfo.setRoomOC(true);
                		event.reply("「解錠」に更新しました。").setEphemeral(true).queue();
                		circleinfo.printRoomInfo(channel);
                    }else event.reply("既に解錠されています。").setEphemeral(true).queue();
            	}
            	case "lock" -> {
                    if(circleinfo.getRoomOC()==true){
                		circleinfo.setRoomOC(false);
                		event.reply("「施錠」に更新しました。今日もお疲れ様でした。").setEphemeral(true).queue();
                		circleinfo.printRoomInfo(channel);
                    }else event.reply("既に施錠されています。").setEphemeral(true).queue();
            	}
	        }
	    }
	    //UTC12時(21時)になった場合、施錠状態にする
	    public static void dailyReset() {
	    	ScheduledExecutorService scheduler=Executors.newScheduledThreadPool(1); //1本のスレッド
	        scheduler.scheduleAtFixedRate(() -> {
	        	Calendar calendar = Calendar.getInstance();
		        if (calendar.get(Calendar.HOUR_OF_DAY)==12) {
		                circleinfo.setRoomOC(false);
		                }
	        },0,1,TimeUnit.HOURS); //(処理内容,遅延,実行間隔,時間単位)
	    }
	}