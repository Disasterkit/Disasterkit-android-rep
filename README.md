## Disaster kitの説明

__ここに実装した機能とかを追加してほしい。__

## とりあえず実装した機能
	-__・オフラインでのマップ表示機能（萩原）__
	-__・地域別のマップダウンロード機能（萩原）__
	-__・簡単な画面遷移（萩原）__

#### 機能説明
	-マップ：外部ストレージに保存されている拡張子.mapのファイルを呼び出している。mapsforgeというものを使って実装した。

	-ダウンロード：リスト表示で地域名とダウンロードボタンを作った。ダウンロードは外部サーバから.mapファイルを外部ストレージにダウンロードする。すでに外部ストレージにファイルがある場合、もう一度ダウンロードし直すかのアラートダイアログを出す。

	-画面遷移：最初に表示される画面はトーク画面にしたいので、今は仮にHallo Worldを表示している。画面遷移は右上のメニューバーからできるようにしている。メニューバー内の”トーク””友達追加”はまだ実装していないのでマップが表示されているようのなっている。マップに関しては画面の上にマップを出力しているみたいでう画面上のメニューバーなどが表示されない。



