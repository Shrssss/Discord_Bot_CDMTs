package Codemates.Discord.Bot.commands;

import Codemates.Discord.Bot.Main;
import Codemates.Discord.Bot.info.BotInfo;
import Codemates.Discord.Bot.info.CircleInfo;
import Codemates.Discord.Bot.util.Utility;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommand extends ListenerAdapter {
    private static final JDA jda = Main.jda();
    public static final String CHANNEL_ID ="1384067026871390208"; //本番環境

    private static final BotInfo BOT_INFO = new BotInfo();
	private static final CircleInfo CIRCLE_INFO=new CircleInfo();

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
					BOT_INFO.printHelp(event);
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
}
