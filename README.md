## Disaster kitの説明

__機能の確認のためここに実装した機能とかの説明を追加してほしい。__

## とりあえず実装した機能
- __オフラインでのマップ表示機能（萩原）__
- __地域別のマップダウンロード機能（萩原）__
- __簡単な画面遷移（萩原）__

## 機能説明
- マップ：外部ストレージに保存されている拡張子.mapのファイルを呼び出している。mapsforgeというものを使って実装した。位置情報に関しては、通信がない場合と想定しているのでGPSのみを使っているので取得に少し時間がかかり、室内では取得しづらい。一度取得できたら、前回の位置を残しているのでもし取得できなかったら前回の位置を表示磨呂になっている。マップに表示しているピンなどは.png形式の画像を使っている。

- ダウンロード：リスト表示で地域名とダウンロードボタンを作った。ダウンロードは外部サーバから.mapファイルを外部ストレージにダウンロードする。すでに外部ストレージにファイルがある場合、もう一度ダウンロードし直すかのアラートダイアログを出す。ミスが見つかって、表示している地域の名前が重複している。すぐ直します。

- 画面遷移：最初に表示される画面はトーク画面にしたいので、今は仮にHallo Worldを表示している。画面遷移は右上のメニューバーからできるようにしている。メニューバー内の”トーク””友達追加”はまだ実装していないのでマップが表示されているようのなっている。マップに関しては画面の上にマップを出力しているみたいで画面上のメニューバーなどが表示されない。


