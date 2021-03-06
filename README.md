## Disaster kitの説明

__機能の確認のためここに実装した機能とかの説明を追加してほしい。__

## とりあえず実装した機能
- __オフラインでのマップ表示機能（noderu）__
- __地域別のマップダウンロード機能（noderu）__
- __簡単な画面遷移（noderu）__
- __BlueToothによるP2P通信、端末間のチャット機能(haruki)__
- __WiFiDirectによるP2P通信(haruki)__

## 機能説明
- **マップ**：外部ストレージに保存されている拡張子.mapのファイルを呼び出している。mapsforgeというものを使って実装した。位置情報に関しては、通信がない場合と想定しているのでGPSのみを使っているので取得に少し時間がかかり、室内では取得しづらい。一度取得できたら、前回の位置を残しているのでもし取得できなかったら前回の位置を表示磨呂になっている。マップに表示しているピンなどは.png形式の画像を使っている。

- **ダウンロード**：リスト表示で地域名とダウンロードボタンを作った。ダウンロードは外部サーバから.mapファイルを外部ストレージにダウンロードする。すでに外部ストレージにファイルがある場合、もう一度ダウンロードし直すかのアラートダイアログを出す。マップデータを自分たちのサーバに置いて、そこからダウンロードさせたいので"japan"以外はまだダウンロードできません。

- **画面遷移**：最初に表示される画面はトーク画面にしたいので、とりあえずはBluetooth接続とチャットの画面を出力している。"トーク"にBluetooth接続とチャット、"友達追加"にQRコード出力、"マップ"にマップ出力、"マップダウンロード"にマップのダウンロード画面をそれぞれ遷移するようになっている。画面遷移は右上のメニューバーからできるようにしている。マップに関しては画面の上にマップを出力しているみたいで画面上のメニューバーなどが表示されない。

- **BlueTooth**：この機能では、ソケット通信を利用する。まず、端末のBluetoothがonになっていなければonに促す通知をする。onにした後、「CONNECT」ボタンをおすことで、ペアリングされた端末、または周辺のBluetoothを搭載している端末を探し、見つかった端末情報の名前がリストとして表示される。ユーザーはこの表示されたリスト中の端末を一つ選択することで、その端末とのP2P接続が可能になる。接続したら、文字入力場所に送りたいテキストを入力し、「SEND」ボタンでそのテキストデータを相手の端末に送る。相手の端末は、送られてきたテキストデータを画面に表示する。また、どちらかの端末がbluetooth接続を切断した場合はP2P通信は切断される。周辺端末を探す機能にまだバグがあると思われる。
 
- **WiFiDirect**：「相手を探す」ボタンで、周辺のWiFiをonにしている端末を探し、「接続」ボタンで見つけた相手の端末とP2P接続をする。この接続先はランダムである。P2P接続を終了するには、「終了」ボタンを押す、あるいはホームボタンを押すことで接続が切断される使用になっている。
