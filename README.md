導入の際の注意：BOTを動かす際はBOTトークンを入力する必要があります。
  private static final String BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");;  内の System.getenv("DISCORD_BOT_TOKEN"); をString型のBOTトークンに書き換えてください。
※自宅サーバーで動かす場合はBOTトークンを環境変数とすることで、書き換えずにBOTを動かすことができます。

　すべてのスラッシュコマンドを使用するためには、テキストチャンネルIDまたはロールIDを特定の場所に書きこむ必要があります。
