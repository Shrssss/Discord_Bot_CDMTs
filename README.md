導入の際の注意：
  BOTを動かす際はBOTトークンを入力する必要があります。
  private static final String BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");;  内の System.getenv("DISCORD_BOT_TOKEN"); をString型のBOTトークンに書き換えてください。
（※自宅サーバーで動かす場合はBOTトークンを環境変数とすることで、書き換えずにBOTを動かすことができます。）
  すべてのスラッシュコマンドを使用するためには、テキストチャンネルIDまたはロールIDを特定の場所に書きこむ必要があります。

TestBotファイル：動作テスト用のファイル。(BOTトークンと各種IDを変更しなかった場合、TEST BOTという別のBOTが動きます)

(バージョン名)ファイル：リリース版ファイル。V1.0.1やV1.1.0などのマイナーアップデートはそのままコミットしてください。
