package Codemates.Discord.Bot.commands;

import Codemates.Discord.Bot.Main;
import Codemates.Discord.Bot.info.CircleInfo;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Button extends ListenerAdapter {
    private static final JDA jda = Main.jda();
    public static final String CHANNEL_ID = Main.CHANNEL_ID;

	private static final CircleInfo CIRCLE_INFO=new CircleInfo();
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
