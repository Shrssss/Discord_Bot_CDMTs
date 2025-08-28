package Codemates.Discord.Bot;

import java.awt.Color;
import java.text.MessageFormat;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

class CircleInfo {
	private String roomid="未定";
	private boolean roomop=false;
	
	public void setRoomID(String roomid) {
		this.roomid=roomid;
	}
	public String getRoomID() {
		return roomid;
	}
	public void setRoomOC(boolean roomoc) {
		this.roomop=roomoc;
	}
	public boolean getRoomOC() {
		return roomop;
	}
	
	public void printRoomInfo(TextChannel channel) {
		EmbedBuilder eb=new EmbedBuilder();
		String status=this.getRoomOC()?"開いています" : "閉まっています";
			eb.addField("部屋情報",MessageFormat.format("\nステータス: {0}\n活動場所: {1}",status,this.getRoomID()),false);
		    eb.setColor(Color.BLUE);
		    channel.sendMessageEmbeds(eb.build()).queue();
    }
}