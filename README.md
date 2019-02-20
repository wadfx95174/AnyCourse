# 整合台灣開放課程之行動學習應用系統 - AnyCourse #

## 專案說明 ##

本作品整合台灣開放課程之行動學習應用系統「AnyCourse」的目標是要讓高中生、大學生或社會人士，透過本系統整合之開放式課程(Open Course)，搭配多種方便友善的功能，讓使用者更有效率地管理及學習。

本團隊發現，現有課程影片資源往往散佈在各個不同網站中，目前並沒有一個便利的開放課程資源之彙整、搜尋與管理系統，所以使用者不僅需要花許多時間去搜尋開放課程，也無法有效地管理自己有興趣的學習資源。

本團隊還發現，即使部分開放課程系統提供特定之課程管理與瀏覽功能，但目前並無系統提供群組學習之功能，使用者往往需要獨自一人埋頭苦讀，當有問題時無人可討論，導致學習效果不佳。(現有之開放課程APP及網頁分析調查報告：https://drive.google.com/open?id=1tQ5cQSeoAH-LFDkPuzatinyoIbtESS8r)

綜合上述兩點，本團隊針對以上的缺點加以改進，基於上述議題，本研究計畫提出一個整合台灣開放式課程之行動學習應用系統，稱為AnyCourse，將依循軟體工程流程方法，規劃建置一個友善的開放式課程整合平台，此系統將蒐集台灣各大學及國外知名大學開放式課程，讓自學者可以利用搜尋工具及推薦系統快速地獲取需要的課程資源，並可利用課程計畫行事曆規劃學習進度，亦可使用筆記、重點標籤、交流區及討論區功能，搭配群組功能與其他使用者互動來達到教學相長的效果，進一步提升學習效率。

## 基本說明 ##

- 使用MySQL/Java/JavaScript/html/CSS

## 需自行設定項目 ##

- 1.AnyCourse\AnyCourse\src中各資料夾之xxxManager.java的存取資料庫路徑、登入帳密

更改此行code

con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=Big5"
					, "root", "peter")
- 2.AnyCourse\AnyCourse\WebContent\AnyCourse\dist\js中的account.js檔中的ajaxURL

更改此行code

var ajaxURL="http://anycourse.cs.ntou.edu.tw:7603/";					

## 安裝說明 ##

- 1.將AnyCourse\AnyCourse\database中的anycourse.sql匯入phpMyAdmin
- 2.將AnyCourse\AnyCourse\src中各資料夾之xxxManager.java檔中的存取資料庫路徑設定好
- 3.將AnyCourse\AnyCourse\WebContent\AnyCourse\dist\js中的account.js檔中的ajaxURL設定好
- 4.將專案部屬至Server或tomcat即可使用