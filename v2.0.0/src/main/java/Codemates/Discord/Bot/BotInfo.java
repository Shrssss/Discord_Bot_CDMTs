package Codemates.Discord.Bot;

import java.awt.Color;
import java.text.MessageFormat;
import java.util.Map;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

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
	
	public static final String UPDATE_INFO="このバージョンはオープンテストです。正式リリースでは貸出物の管理を行えるようにする予定です。";
	public static final String EASY_UPDATE_INFO="/announceコマンドの追加"
													+ "\nコマンド名の変更(/room-status-update)"
													+ "\n/roomコマンドで部屋を指定できるように変更";
	
	
	
	public void printBotInfo(SlashCommandInteractionEvent event) {
	    EmbedBuilder eb=new EmbedBuilder();

	    String description="このBOTはCODEMATES-DiscordServerの利便性向上を目的としています。";
	    String versioninfo=MessageFormat.format(
	        "\nVersion: {0}\nDeveloper: {1}\nLastUpdate: {2}",
	        this.getVersion(),
	        this.getDeveloper(),
	        this.getUpdate()
	    );
	    eb.addField(description,versioninfo,false);
	    eb.setColor(Color.BLUE);

	    event.replyEmbeds(eb.build()).setEphemeral(true).queue();
	}
	
	public void printUpdateInfo(SlashCommandInteractionEvent event,String ezupinfo) {
		EmbedBuilder eb =new EmbedBuilder();

		String description=MessageFormat.format("[{0}]\n {1}",this.getVersion(),ezupinfo);
					eb.addField("更新内容",description,false);
					eb.setColor(Color.BLUE);
		event.replyEmbeds(eb.build()).setEphemeral(true).queue();
	}
	
    public void printHelp(SlashCommandInteractionEvent event,Map<String,String> cmdinfo) {
        EmbedBuilder eb=new EmbedBuilder();
            eb.setTitle("コマンド一覧");
            for (Map.Entry<String,String> ent:cmdinfo.entrySet()) {
                String command="/"+ent.getKey();
                String description=cmdinfo.getOrDefault(ent.getKey(), "説明がありません。");
                eb.addField(command,description,false);
            }
            eb.setColor(Color.BLUE);
        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }
    
    public void printAnnounce(SlashCommandInteractionEvent event,TextChannel channel) {
        EmbedBuilder eb=new EmbedBuilder();
        	eb.setTitle(MessageFormat.format("[{0}]",this.getVersion()));

        	eb.addField("更新内容",BotInfo.UPDATE_INFO,false);
        	eb.addField("詳細",BotInfo.EASY_UPDATE_INFO,false);
        	eb.addField("","(Botの操作に不具合がある場合は、Discordを再起動してください。)",false);

        	eb.setColor(Color.BLUE);
        	channel.sendMessageEmbeds(eb.build()).queue();
    }
}
