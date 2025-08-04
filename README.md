# ReadMe...
## このレポジトリについて
これはCODEMATESのDiscordサーバーの利便性を向上させるために作られたDiscord Botです。<br>
使用言語はJava17であり、外部ライブラリを使用するためにMaven上で作成しています。<br>
##バージョンについて
このBotのバージョンはv0.0.0aのようなピリオド区切りの3つの整数と小文字アルファベットで表現されます。<br>
それぞれ頭から順に、v(メジャーアップデート).(マイナーアップデート).(パッチ)+(Botのリリース状態) を指しています。<br>
Botのリリース状態を表すアルファベットは、開発者がちゃんと公開用のファイルを設定できているかを判別するためのものです。<br>
種類はこのようなものがあります。<br>

| アルファベット  | 説明 |
| ------------- | ------------- |
| o (open)  | リリース版ファイル。  |
| ot (open-test)  | オープンテスト版ファイル。  |
| ct (closed-test) | クローズドテスト版ファイル。  |

<br>
## スラッシュコマンド一覧

| コマンド名  | 説明 |
| ------------- | ------------- |
| info  | BOTの説明を表示。  |
| help  | コマンド一覧の表示。  |
| updateinfo  | 直近のアップデート内容の表示。  |
| room-status-update  | 活動部屋の空き状況を更新。  |

# CodeMatesBot.java内のコード説明
  ## Main class　（114-225行）<br>
  ```

  public class CodeMatesBot extends ListenerAdapter {
    //コード...
  }
  
  ```
### 主な変数とインスタンス
| 変数、インスタンス名  | 説明 |
| ------------- | ------------- |
| BOT_TOKEN  | [変数String] 　ボットトークン。  |
| jda  | [インスタンス] 　JDA(Java Discord API)。  |
| botinfo  | [インスタンス] 　BotInfoクラスをインスタンス化。 |
| circleinfo  | [インスタンス] 　CircleInfoクラスをインスタンス化。 |
| cmdname  | [インスタンス]　ArrayList。スラッシュコマンドの名前を格納する。　  |
| cmdinfo  |[インスタンス]　 HashMap。cmdnameをkeyとし、コマンドの説明文を格納する。  |

  このjavaファイルの本体の部分です。Discordのチャットを読み取る必要があるため、MainクラスはJDAライブラリのListenerAdapterというクラスをスーパークラスとして持ちます。<br>
  <br>
  Mainクラス内の以下のコード群は、Botを動かす上で特に重要なクラス変数を定義するものです。（115-120行）<br>
  ```
  private static JDA jda = null;

    //JDAインスタンスを初期化。JDAインスタンスについては後述。
  ```
  ```
  private static final String BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");

    //Botトークンの定義。これがないとBotが動かない。今回は動かすコンピュータの環境変数を読み取る方式に。
  ```
  ```
  private static final BotInfo botinfo = new BotInfo();
  private static final CircleInfo circleinfo=new CircleInfo();

    //BotInfoと、CircleInfoというクラスをインスタンス化。
  ```
  ```
  private static final ArrayList<String> cmdname=new ArrayList<>();
  private static final Map<String,String> cmdinfo=new HashMap<>();

    //Botのスラッシュコマンドの名前(cmdname)と説明文(cmdinfo)を格納するArrayListとHashMapを定義。
  ```
　ここで、スラッシュコマンドの情報を定義するためにArrayListとHashMapの二つを使用していますが、これはMapのKeyをArrayListの要素と紐づけ、順序をつけるためです。(HashMapは順序を保持しないため)<br>
 <br>
  Mainクラス内には現時点(08/07/2025)で、`main` `onSlashCommandInteraction` `onButtonInteraction` `dailyReset`の四つのメソッドが格納されています。<br>
<br>
## mainメソッド (114-151行)
```
public static void main(String[] args) throws LoginException{
  //コード...
}
```
  `main`内には、Botを動かす上で必ず必要なものを定義（123-149行）したり、ほかの(スケジュール化された)メソッドを実行したりしています。<br>
  ###### (`main`は基本的にコードを書くところではないのでメソッド呼び出しや変数定義ぐらいでしか使いません。)<br>
  <br>
  123-130行では、CircleInfoとBotInfoクラス内で定義されている変数を再度定義しています。<br>
  その下の`//CommandArray`と書かれたところでは、先ほどのArrayListとMapに名前と説明文を入れています。<br>
  また、
  
  ```
    jda = JDABuilder.createDefault(BOT_TOKEN)
                .setRawEventsEnabled(true) //すべてのイベント(ボタンの押下、スラッシュコマンド等)を処理できるように。
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new CodeMatesBot())
                .setActivity(Activity.playing("String")) //プロフィールに「Stringをプレイ中」と表示させる。
                .build(); 
  ```

  はJDAインスタンスを定義しています。これはBotの設定を定義するもので、これがなければBotが動かない重要なものです。<br>
  <br>
  それより下では、
  
  ```
  jda.updateCommands().queue();

    //ここでJDAインスタンス内のスラッシュコマンドをアップデート。
  ```
  ```
  for(int i=0;i<cmdinfo.size();i++) {

      //ここでは定義されたArrayListとMapを用いてコマンドを定義。

  }
  ```
  ```
  dailyReset();

      //dailyResetメソッドを呼び出し。

  ```

　を行っています。<br>
 <br>
 見たらわかると思いますが、try-catchで例外処理をしています。これはBotが何らかの要因で起動失敗した際に開発側で分かりやすくするためです。

 ## onSlashCommandInteractionメソッド (154-187行)
  ```
   @Override
	    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
          //コード...
      }
  ```
<br>
このメソッドではスラッシュコマンドを制御しています。<br>
<br>引数`event`としてチャット内で発生したスラッシュコマンドイベントを受け取り、String型の`cmd`に代入しています。<br>
ついでにボットが送信するメッセージを代入する`outmsg`も定義してます。

```
	  String cmd=event.getName();//ReceiveCommand
	  String outmsg="";
```
これプラスswitch文を使って、コマンドを制御します。
```
switch(cmd) {
	            case "コマンド名"-> {
                  //実行する内容
              }
}
```
基本的に、caseの中にメソッドを入れて実行みたいな形がシンプルでいいです。<br>
定義されたコマンド以外を実行した場合は`default`が実行されますが、正直意味ないです。
```
default -> event.reply("不明なコマンドです。\n").setEphemeral(true).queue();
```
## onButtonInteractionメソッド (193-214行)
```
public void onButtonInteraction(ButtonInteractionEvent event) {
      //コード...
}
```
このメソッドではボタンを押した際に発生するイベントを引数として受け取っています。<br>
また、このメソッドでは別チャットチャンネルで返信をするという処理をするので、チャンネルIDを設定しています。今回の場合はCodeMatesサーバーのbotチャットです。
```
TextChannel channel = jda.getTextChannelById("1384067026871390208");
```
ここも基本は`onSlashCommandInteraction`と一緒です。ですが、いっぱい分岐先を追加するわけではないので、生のコードを置いています。
```
//実例

switch (event.getComponentId()) {
            	case "unlock" -> {
                    if(circleinfo.getRoomOC()==false){
                		circleinfo.setRoomOP(true);
                		event.reply("「解錠」に更新しました。").setEphemeral(true).queue();
                		circleinfo.printRoomInfo(channel);
                    }else event.reply("既に解錠されています。").setEphemeral(true).queue();
            	}
```
## dailyResetメソッド (216-225行)
```
public static void dailyReset() {
        //コード...
}
```
このメソッドは、日本時間の21時に変数`roomop`の値を`false`に上書きするメソッドです。<br>
毎時間、時間を確認し、UTC12時になった場合`false`に変更するようにしています。<br>
また、毎時間繰り返し処理を行う必要があるため、`scheduler`というものを使っています。<br>
<br>

 ## CircleInfo class　（28-51行）<br>
 ```
 class CircleInfo {
  //コード...
 }
 ```
### 主な変数とメソッド
| 変数、メソッド名  | 説明 |
| ------------- | ------------- |
| roomid  | [変数String]  　部屋の場所。（別館65階404号室など...）  |
| roomop  | [変数boolean]  　部屋の開き、締まり。trueは開き、falseは締まり。  |
| setRoomID,setRoomOP  | [メソッド]  　変数のセッター。 |
| getRoomID,getRoomOP  | [メソッド]  　変数のゲッター。 |
| printRoomInfo  | [メソッド]  　チャンネルIDを引数とし、そのチャンネルに部屋の開き状況を送信する。　|

 このクラスは、サークルの基本情報に関するクラスです。変数に関するゲッターとセッター、サークル活動部屋の情報を返す`printRoomInfo`メソッドが含まれます。
 ```
  public void printRoomInfo(TextChannel channel) {
		//コード...
 }
 ```
  ## Botnfo class　（53-111行）<br>
  ```
  class BotInfo {
      //コード...
  }
  ```
### 主な変数とメソッド
| 変数、メソッド名  | 説明 |
| ------------- | ------------- |
| version  | [変数String]  　ボットのバージョン。  |
| developer  | [変数String]  　デベロッパの名前。  |
| update  | [変数String]  　アップデート日。 |
| setVersion,setDeveloper,setUpdate  | [メソッド]  　変数のセッター。 |
| getVersion,getDeveloper,getUpdate  | [メソッド]  　変数のゲッター。 |
| printBotInfo  | [メソッド]  　スラッシュコマンドのイベントを引数として、ボットの基本情報をリプライする。　|
| printUpdateInfo  | [メソッド]  　スラッシュコマンドのイベントを引数として、ボットのアップデート内容をリプライする。　|
| printHelp  | [メソッド]  　スラッシュコマンドのイベント、`cmdname`、`cmdinfo`を引数として、コマンド一覧をリプライする。　|

  ここには、変数のほかに`printBotInfo`、`printUpdateInfo`、`printHelp`の三つのメソッドがあります。<br>
  それぞれ、「ボットの基本情報」、「アップデート内容」、「コマンド一覧」を返します。
  ```
  public void printBotInfo(SlashCommandInteractionEvent event) {
      //コード...
  }
  
  ```
  ```
  public void printUpdateInfo(SlashCommandInteractionEvent event) {
    //コード...
  }
  ```
  ```
  public void printHelp(SlashCommandInteractionEvent event,ArrayList<String> cmdname,Map<String,String> cmdinfo) {
   //コード...
  }
  ```
