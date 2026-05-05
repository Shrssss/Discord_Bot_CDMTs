package Codemates.Discord.Bot.info;

import java.awt.Color;
import java.text.MessageFormat;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class CircleInfo {
	private String roomid="";
	private boolean roomop=false;
	
	public void setRoomId(String roomid) {
		this.roomid=roomid;
	}
	public String getRoomId() {
		return roomid;
	}
	public void setRoomOp(boolean roomop) {
		this.roomop=roomop;
	}
	public boolean getRoomOp() {
		return roomop;
	}
	
	public void printRoomInfo(TextChannel channel) {
		EmbedBuilder eb=new EmbedBuilder();
		String status=this.getRoomOp()?"開いています" : "閉まっています";
			eb.addField("部屋情報",MessageFormat.format("\nステータス: {0}\n活動場所: {1}",status,this.getRoomId()),false);
		    eb.setColor(Color.BLUE);
		    channel.sendMessageEmbeds(eb.build()).queue();
    }
	
	public void printRoomCommand(SlashCommandInteractionEvent event) {
		if(roomid.isBlank()) {
			
	    	event.reply("開ける部屋を選択して下さい。").setEphemeral(true).addActionRow(
					Button.primary("seminar1-2", "研修室1,2"),
					Button.primary("seminar3","研修室3"),
					Button.primary("conference3","会議室3"),
					Button.primary("otherRoom","その他")
			).queue();
	    	
		}else {
			
	    	event.reply("部屋を変更しますか？").setEphemeral(true).addActionRow(
					Button.primary("yes", "はい"),
					Button.danger("no","いいえ")
			).queue();
	    	
		}
	}
	
	public void printRoomOpenClose(ButtonInteractionEvent event) {
    	event.reply("空き状況を選択して下さい。現在の部屋名は、"+getRoomId()+"です。").setEphemeral(true).addActionRow(
				Button.primary("unlock", "解錠"),
				Button.danger("lock","施錠")
		).queue();
	}
	
}