package Codemates.Discord.Bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import java.util.Map;

import Codemates.Discord.Bot.commands.CommandData;
import Codemates.Discord.Bot.info.BotInfo;
import Codemates.Discord.Bot.info.CircleInfo;
import Codemates.Discord.Bot.util.Utility;



public class Main extends ListenerAdapter {
	private static JDA jda = null;
	private static final String BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN"); //本番環境
	public static final String CHANNEL_ID ="1384067026871390208"; //本番環境
//	private static final String BOT_TOKEN=""; //テスト環境
//	public static final String CHANNEL_ID="1382708384221888562"; //テスト環境
	
	private static final BotInfo BOT_INFO = new BotInfo();
	private static final CircleInfo CIRCLE_INFO=new CircleInfo();

	public static void main(String[] args) {
		try {
			
			//BotInfo
			BOT_INFO.setVersion("v2.0.0ot");
			BOT_INFO.setDeveloper("R.N.");
			BOT_INFO.setUpdate("02/12/25 DD/MM/YY");
	        
			
			
			
			jda = JDABuilder.createDefault(BOT_TOKEN)
	                .setRawEventsEnabled(true)
	                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
	                .addEventListeners(new Main())
	                .setActivity(Activity.playing("open test"))
	                .build();
			
			jda.updateCommands().queue();
			
			
			//DetaBase.connect();
			
			
	    
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
	  
	        switch(cmd) {
	            //roomコマンド
	            case "room"-> {
	            	CIRCLE_INFO.printRoomCommand(event);
	            }
	            //botinfoコマンド
	            case"botinfo"->{
	            	BOT_INFO.printBotInfo(event);
	            }
				 //updateinfoコマンド
				 case"updateinfo"->{
					BOT_INFO.printUpdateInfo(event,BotInfo.EASY_UPDATE_INFO);
				 }
                //helpコマンド
                case"help"->{
                    BOT_INFO.printHelp(event,CommandData.CMD_INFO);
                }
                //announceコマンド
                case"announce"->{
                	//1247737193774977094 <- 本番role
                	//1382708559120306238 <- テスト
                	if(Utility.hasRoleById(event.getMember(),"1247737193774977094")==true) {
                    	BOT_INFO.printAnnounce(event,channel);
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
	        if (channel==null) return;
	        
	        switch (event.getComponentId()) {
	        
            	case "unlock"->{
                    if(CIRCLE_INFO.getRoomOp()==false){
                		CIRCLE_INFO.setRoomOp(true);
                		event.reply("「解錠」に更新しました。").setEphemeral(true).queue();
                		CIRCLE_INFO.printRoomInfo(channel);
                    }else event.reply("既に解錠されています。").setEphemeral(true).queue();
            	}
            	
            	case "lock"->{
                    if(CIRCLE_INFO.getRoomOp()==true){
                		CIRCLE_INFO.setRoomOp(false);
                		event.reply("「施錠」に更新しました。今日もお疲れ様でした。").setEphemeral(true).queue();
                		CIRCLE_INFO.printRoomInfo(channel);
                    }else event.reply("既に施錠されています。").setEphemeral(true).queue();
            	}
            	
            	case "seminar1-2"->{
            		CIRCLE_INFO.setRoomId("エッグドーム5階 研修室1,2");
            		CIRCLE_INFO.printRoomOpenClose(event);
            	}
            	
            	case "seminar3"->{
            		CIRCLE_INFO.setRoomId("エッグドーム5階 研修室3");
            		CIRCLE_INFO.printRoomOpenClose(event);
            	}

            	case "conference3"->{
            		CIRCLE_INFO.setRoomId("エッグドーム5階 会議室3");
            		CIRCLE_INFO.printRoomOpenClose(event);
            	}
            	
            	case "otherRoom"->{
            		CIRCLE_INFO.setRoomId("その他");
            		CIRCLE_INFO.printRoomOpenClose(event);
            	}
            	
            	case "yes"->{
            		CIRCLE_INFO.setRoomId("");
            		event.reply("部屋名が初期化されました。再度/roomを実行してください。").setEphemeral(true).queue();
            	}
            	
            	case "no"->{
            		CIRCLE_INFO.printRoomOpenClose(event);
            	}
	        }
	    }
	}