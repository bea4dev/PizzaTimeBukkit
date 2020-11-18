# PizzaTimeBukkit

LunaChatを入れたBukkit / Spigot / Paperサーバー間でBungeeCordに頼らずに、チャット共有や個人チャットを実現するプラグイン

## Requirement

* LunaChat
* ProtocolLib

## How to use

* サーバーのpluginsフォルダにPizzaTimeBukkit.jarを入れる
* サーバーを起動する
* PizzaTimeBukkitフォルダとその中にconfig.ymlが生成されているので任意で書き換える
```yaml
#LunaChatの/tellコマンドを乗っ取るかどうかの設定
hijack-tell-commands: true

#チャット情報受け取り用のサーバーポート
receive-port: 28000

#チャット情報送信先のサーバーとポート
servers:
  server1:
    host: localhost
    port: 28001
  server2:
    host: localhost
    port: 28002
```
* /pt reloadで設定を再ロードする

## Command
```
/pt reload
```
設定ファイルをリロードします
※receive-port の変更は再起動するか/reloadコマンドを使うまで適応されません

## Permission
```
pt.admin
```
/pt コマンドを使うために必要

## License



## Download

