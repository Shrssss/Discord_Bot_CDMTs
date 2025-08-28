package Codemates.Discord.Bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.Member;
//import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
//import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

//import javax.security.auth.login.LoginException;

//import java.awt.Color;

//import java.text.MessageFormat;

import java.time.Duration;

//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import java.util.Calendar;
import java.util.Map;
//import java.util.LinkedHashMap;
//import java.util.ArrayList;



public class Main extends ListenerAdapter {
	private static JDA jda = null;
	private static final String BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");
	private static final BotInfo BOT_INFO = new BotInfo();
	private static final CircleInfo CIRCLE_INFO=new CircleInfo();
	
	//public static final String CHANNEL_ID ="1384067026871390208";
	public static final String CHANNEL_ID ="1382708384221888562"; //THIS ID IS FOR TEST SERVER
	

	public static void main(String[] args) {
		try {
			//CircleInfo
			CIRCLE_INFO.setRoomID("エッグドーム5階　研修室1,2");
			
			//BotInfo
			BOT_INFO.setVersion("v2.0.0ct");
			BOT_INFO.setDeveloper("R.N.");
			BOT_INFO.setUpdate("04/08/25 DD/MM/YY");
			
	        
	        
			jda = JDABuilder.createDefault(BOT_TOKEN)
	                .setRawEventsEnabled(true)
	                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
	                .addEventListeners(new Main())
	                .setActivity(Activity.playing("closed test"))
	                .build();
			jda.updateCommands().queue();
	    
	        for(Map.Entry<String,String> ent:CommandData.CMD_INFO.entrySet()) {
	            jda.upsertCommand(ent.getKey(),ent.getValue()).queue();
	        }
	        
	        Utility.dailyReset(CIRCLE_INFO); //毎時、時間を参照
	        
		}catch(RuntimeException e) {
			System.err.println("起動失敗：" + e.getMessage());
	        e.printStackTrace();
		}
		
	}

	//forSlashCommand
	 @Override
	    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		 TextChannel channel=jda.getTextChannelById(CHANNEL_ID);
		 String cmd=event.getName();//ReceiveCommand
		 String outmsg="";
	  
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
	            case"info"->{
	            	BOT_INFO.printBotInfo(event);
	            }
				 //updatelogコマンド
				 case"updateinfo"->{
					BOT_INFO.printUpdateInfo(event,BotInfo.EASY_UPDATE_INFO);
				 }
                //helpコマンド
                case"help"->{
                    BOT_INFO.printHelp(event,CommandData.CMD_INFO);
                }
                //update-announceコマンド
                case"update-announce"->{
                	//1247737958560174130 <- 本番
                	if(Utility.hasRoleById(event.getMember(),"1382708559120306238")==true) {
                    	BOT_INFO.printUpdateAnnounce(event,channel);
                    	event.reply("送信完了。").setEphemeral(true).queue();
                	} else event.reply("実行権限を持っていません。").setEphemeral(true).queue();
                }
	            default -> event.reply("不明なコマンドです。\n").setEphemeral(true).queue();
	        }
	    }

	    //forButton
	    @Override
	    public void onButtonInteraction(ButtonInteractionEvent event) {
	    	TextChannel channel=jda.getTextChannelById(CHANNEL_ID);
	        if (channel == null) return;
	        
	        switch (event.getComponentId()) {
            	case "unlock" -> {
                    if(CIRCLE_INFO.getRoomOC()==false){
                		CIRCLE_INFO.setRoomOC(true);
                		event.reply("「解錠」に更新しました。").setEphemeral(true).queue();
                		CIRCLE_INFO.printRoomInfo(channel);
                    }else event.reply("既に解錠されています。").setEphemeral(true).queue();
            	}
            	case "lock" -> {
                    if(CIRCLE_INFO.getRoomOC()==true){
                		CIRCLE_INFO.setRoomOC(false);
                		event.reply("「施錠」に更新しました。今日もお疲れ様でした。").setEphemeral(true).queue();
                		CIRCLE_INFO.printRoomInfo(channel);
                    }else event.reply("既に施錠されています。").setEphemeral(true).queue();
            	}
	        }
	    }
	    
	    //memberをduration中、reasonでタイムアウト　<- 協議
	    public void timeoutUser(Member mem,Duration dur,String rea) {
	    	mem.timeoutFor(dur)
	    	   .reason(rea)
	    	   .queue(
	    			success->System.out.println(mem.getEffectiveName()+"をタイムアウト"),
	    			error->System.err.println("タイムアウト失敗："+error.getMessage())
	    		);
	    }
	}
