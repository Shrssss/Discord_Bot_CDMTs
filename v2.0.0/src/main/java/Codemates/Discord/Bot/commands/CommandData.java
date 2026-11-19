package Codemates.Discord.Bot.commands;

public enum CommandData {
	
        BOT_INFO("botinfo","BOTの説明を表示。"),
        HELP("help","コマンド一覧の表示。"),
        UPDATE_INFO("updateinfo","直近のアップデート内容の表示。"),
        ROOM("room","活動部屋の空き状況を更新。"),
        ANNOUNCE("announce", "開発者用");

        private final String name;
        private final String description;

        CommandData(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

	}
