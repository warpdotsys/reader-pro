# business/ â€” ä¸šåŠ¡åŒ–é‡å†™æºç 

ä» `reader-pro-3.2.14.jar` é€†å‘åçš„ **å¯è¯»ä¸šåŠ¡å±‚ Kotlin**ï¼ˆçº¦ 43 æ–‡ä»¶ / 3000+ è¡Œï¼‰ã€‚

## ä¸¤æ£µæ ‘æ€ä¹ˆç”¨

| è·¯å¾„ | å†…å®¹ |
|------|------|
| `best-of-3/src/` | åç¼–è¯‘æ‹©ä¼˜ + ç¡¬ä¼¤ä¿®è¡¥ï¼ˆè´´è¿‘å­—èŠ‚ç ï¼Œå«åç¨‹çŠ¶æ€æœºï¼‰ |
| **`best-of-3/business/`** | **ä¸šåŠ¡åŒ–é‡å†™**ï¼šsuspend é¡ºåºé€»è¾‘ã€ä¸­æ–‡é”™è¯¯æ–‡æ¡ˆã€è·¯ç”±ä¸é¢†åŸŸæ¸…æ™° |

**è¯»ä¸šåŠ¡ / åšå¯¹æ¥ï¼šä¼˜å…ˆ business/**  
**å¯¹æŸä¸€æ¡æŒ‡ä»¤æˆ–è¾¹ç•Œï¼šå›çœ‹ src/ æˆ– full-cfr/**

## æ¨¡å—æ¸…å•

### æœåŠ¡ç«¯
- `YueduApi` â€” è·¯ç”±è¡¨ï¼ˆæ ¸å¿ƒ `/reader3/*` å·²æŒ‚ï¼›å®Œæ•´ 133 æ¡è§ä»“åº“æ ¹ `API_ROUTES.md`ï¼‰
- `RestVerticle` â€” HTTP / Session / Body / CORS
- `ReturnData`ã€`BaseController`ï¼ˆé‰´æƒã€ç”¨æˆ·ç©ºé—´ã€limitConcurrentï¼‰
- `AppConfig`ã€`User` / `License` å®ä½“
- `ExtKt` / `UserMutex` / `VertExtKt` / `SpringContextUtils`

### æ§åˆ¶å™¨ï¼ˆä¸šåŠ¡æ–¹æ³•å·²é¡ºåºåŒ–ï¼‰
| æ§åˆ¶å™¨ | èŒè´£ |
|--------|------|
| **BookController** | ä¹¦æ¶ã€æœç´¢ã€è¯¦æƒ…ã€ç›®å½•ã€æ­£æ–‡ã€è¿›åº¦ã€ç¼“å­˜ã€å°é¢ã€WebDAV åŒæ­¥ã€TTS |
| **UserController** | ç™»å½•æ³¨å†Œã€ç™»å‡ºã€ç”¨æˆ· CRUDã€é…ç½® |
| **BookSourceController** | ä¹¦æº CRUDã€é»˜è®¤ä¹¦æºã€è¿œç¨‹å¯¼å…¥å ä½ |
| **LicenseController** | æˆæƒå¯¼å…¥/æ ¡éªŒ/æ¿€æ´»ï¼ˆåŠ å¯†ç»†èŠ‚è§ jarï¼‰ |
| **FileController** | æœ¬åœ°æ–‡ä»¶æµè§ˆ/ä¸Šä¼ /ä¸‹è½½ |
| **WebdavController** | å¤‡ä»½åˆ° WebDAV |
| **RssSourceController** | RSS æº |
| **BookGroup / Bookmark / ReplaceRule** | åˆ†ç»„ã€ä¹¦ç­¾ã€æ›¿æ¢è§„åˆ™ |

### é˜…è¯»å¼•æ“
- `WebBook` é—¨é¢
- `BookList` / `BookInfo` / `BookChapterList` / `BookContent`
- `AnalyzeRule` / `AnalyzeUrl`ï¼ˆAPI é¢ï¼›åº•å±‚ CSS/XPath/JS å®Œæ•´å®ç°ä»åœ¨ `src/` åç¼–è¯‘å¤§æ–‡ä»¶ï¼‰
- `BookHelp.saveImage`ã€`LocalBook` å ä½

## ç”Ÿæˆè„šæœ¬

```
_business_rewrite_core.py
_business_rewrite_more.py
_business_book_controller.py
_business_utils_entities.py
```

## è¯´æ˜ä¸è¾¹ç•Œ

1. **ä¸æ˜¯**å¯ç›´æ¥ç¼–è¯‘çš„å®Œæ•´å·¥ç¨‹ï¼ˆä¾èµ–ç‰ˆæœ¬ã€éƒ¨åˆ†æ–¹æ³•ä½“ä¸ºè¯­ä¹‰è¿˜åŸï¼‰ã€‚
2. **BookController** ä» ~1 ä¸‡è¡ŒçŠ¶æ€æœºå‹æˆçº¦ 500 è¡Œä¸šåŠ¡é€»è¾‘ï¼›å†·é—¨åˆ†æ”¯è§ `src/`ã€‚
3. **AnalyzeRule å†…æ ¸**ï¼ˆJSoup/XPath/JSON/Rhinoï¼‰å»ºè®®ç»§ç»­è¯» `src/io/legado/app/model/analyzeRule/*`ã€‚
4. **License åŠ ç­¾éªŒç­¾** ä»…ç»“æ„çº§ï¼›å…·ä½“ RSA/hutool è°ƒç”¨åœ¨ jar ä¸ `src` çš„ LicenseControllerã€‚

## å»ºè®®é˜…è¯»é¡ºåº

1. `YueduApi.kt`ï¼ˆè·¯ç”±ï¼‰  
2. `BaseController.kt` + `UserController.kt`  
3. `BookController.kt`  
4. `BookSourceController.kt`  
5. `webBook/*` + `BookChapterList` / `BookContent`  
6. å¯¹ç…§ `../src/` åŒåç±»è¡¥ç»†èŠ‚  
'''


## Phase 2 å¢é‡

- **YueduApi**ï¼šæŒ‰ `API_ROUTES.md` æŒ‚è½½ **133 è·¯ç”±**ï¼ˆSSE/æ–‡ä»¶æµå•ç‹¬å¤„ç†ï¼‰
- **BookControllerExtras**ï¼šexplore/multi/SSE/cache/export/mongo/tts ç­‰
- **HttpTTSController**ã€Rss æ–‡ç« æ¥å£
- **AnalyzeRule**ï¼šMode åˆ†å‘ï¼ˆJs/Regex/Json/XPath/Defaultï¼‰+ JSoup å®ç°
- **JsExtensions**ï¼šajax/connect/base64/md5/aes/file ç­‰ä¹¦æº JS API
- **LicenseController + EncoderUtils**ï¼šRSA å¯†é’¥ã€åˆ†æ®µåŠ è§£å¯†ã€activate è½ç›˜


## Phase 3 å¢é‡

- **AnalyzeByXPath**ï¼šseimicrawler JXDocument + `&&/||/%%` è§„åˆ™æ‹†åˆ†ï¼ˆRuleAnalyzerï¼‰
- **SourceAnalyzer**ï¼šlegado/æ—§ç‰ˆä¹¦æº JSON â†’ BookSource è§„èŒƒåŒ–ï¼›`BookSource.fromJson` æ¥å…¥
- **searchBookMulti / SSE**ï¼šå¤šä¹¦æºåç¨‹å¹¶å‘æœç´¢ä¸äº‹ä»¶æµ
- **LocalBook + TextFile**ï¼šæœ¬åœ°ä¹¦åˆ†å‘ï¼›TXT ç›®å½•æ­£åˆ™åˆ‡åˆ†ä¸æŒ‰ offset å–æ­£æ–‡
- **MongoBackup**ï¼šæŒ‰ç”¨æˆ·å¤‡ä»½/æ¢å¤ JSON æ–‡æ¡£åˆ° MongoDB


## Phase 4 å¢é‡

- **EpubFile**ï¼šepublib è¯» spine/TOC + ç« èŠ‚ HTMLï¼ˆJsoup å» script/styleï¼‰
- **CbzFile / PdfFile / UmdFile**ï¼šåˆ†é¡µ/æ¡ç›®ç›®å½•ä¸æ­£æ–‡
- **AnalyzeByJSoup**ï¼š`&&` / `||` / `%%` ä¸å±æ€§ `@text/@html/@href`
- **searchBookMulti**ï¼šé»˜è®¤å¹¶å‘ **36**ï¼ŒSSE **24**ï¼Œå•æºè¶…æ—¶ **15s**
- **DefaultData + txtTocRule.json**ï¼šå†…ç½®ç›®å½•æ­£åˆ™åº“ï¼›TextFile è‡ªåŠ¨é€‰æœ€ä¼˜è§„åˆ™


## Phase 5 å¢é‡

- **EpubFile**ï¼š`getChapterListBySpinAndToc` / `getChapterListByTocAndSpin` æ ‡é¢˜åˆå¹¶ï¼›é»˜è®¤ç›®å½•ç”¨ spine é¡ºåº + TOC æ ‡é¢˜
- **LocalMedia**ï¼šCBZ æ¡ç›®å›¾ / PDF é¡µæ¸²æŸ“ JPG / EPUB å°é¢ä¸å†…åµŒå›¾å­—èŠ‚æµ
- **SourceAnalyzer.toNewRule**ï¼šå®Œæ•´ `-`/`+`ã€`#`â†’`##`ã€`|`â†’`||`ã€`&`â†’`&&` æ—§è§„åˆ™è¿ç§»
- **HtmlFormatter.formatKeepImg**
- **LocalBookApi**ï¼šæœ¬åœ°ä¹¦å¯¼å…¥é¢„è§ˆ + ç« èŠ‚å›¾/å°é¢ HTTP æµå¼è¾“å‡º


## Phase 6 å¢é‡

- **WebdavController**ï¼šPROPFIND/MKCOL/PUT/GET/DELETE/MOVE/COPY/LOCK/UNLOCK + zip å¤‡ä»½
- **Debugger + bookSourceDebugSSE**ï¼šæœç´¢â†’è¯¦æƒ…â†’ç›®å½•â†’æ­£æ–‡ é€æ­¥ SSE æ—¥å¿—
- **ContentProcessor**ï¼š`replaceRule.json` åº”ç”¨åˆ° `getBookContent`
- **INDEX.md**ï¼šbusiness â†” src ç±»åå¯¹ç…§ç´¢å¼•


## Phase 7 å¢é‡

- **WebdavPaths + MOVE/COPY**ï¼šDestination æŒ‰ URL.path è§£æå¹¶å»æ‰ `/reader3/webdav`ï¼ŒOverwrite è¯­ä¹‰å¯¹é½ jarï¼ˆç¼ºçœ 412ï¼‰
- **Debugger**ï¼š`http(s)` è¯¦æƒ… / `::` å‘ç° / `++` ç›®å½• / `--` æ­£æ–‡ / é»˜è®¤æœç´¢
- **ContentProcessor**ï¼štimeoutï¼ˆé»˜è®¤ 3sï¼‰ã€bookName è¿‡æ»¤ã€title/content ä½œç”¨åŸŸ
- **BookExport**ï¼š`exportToTxt` / `exportToEpub`ï¼ˆå…¨æ–‡æ‹‰å– + æ›¿æ¢è§„åˆ™ï¼‰


## Phase 8 å¢é‡

- **exportBook** æ¥çº¿ `BookExport`ï¼ˆtxt/epubï¼‰
- **cacheBookSSE**ï¼šå¹¶å‘é»˜è®¤ 24ï¼Œè¿›åº¦ SSEï¼ˆsuccess/failed/cached/totalï¼‰
- **saveBookContent / deleteBookCache / getShelfBookWithCacheInfo**
- **getChapterListByRule**ï¼šæœ¬åœ° txt/epub/pdf æŒ‰è§„åˆ™é‡åˆ‡ç›®å½•
- **searchBookContent**ï¼šç« èŠ‚å†…å…³é”®å­—æ£€ç´¢ï¼ˆåˆ†é¡µ lastIndex/sizeï¼‰
- **saveBookGroupId / addBookGroupMulti / removeBookGroupMulti / saveBookConfig**
- **setBookSource / searchBookSourceSSE / getInvalidBookSources**
- **Rss** å¼•æ“ + **RssSourceController**ï¼šè§„åˆ™è§£æ / é»˜è®¤ RSSÂ·Atom XML
- **Book**ï¼š`group`ã€`pdfImageWidth` å­—æ®µ


## Phase 9 å¢é‡

- **TTS**ï¼š`textToSpeech` / `ttsByEdge`ï¼ˆTTSService åå°„ï¼‰/ `ttsByApi`ï¼ˆHttpTTSï¼‰/ `ttsByTextToSpeechCn`
- **EdgeTts**ï¼šSSML æ„é€  + jar `TTSService.sendText` å¯¹æ¥
- **saveFromRemoteSource**ï¼šOkHttp æ‹‰å–è¿œç¨‹ä¹¦æº JSON â†’ `saveBookSources`
- **SearchResult** + `searchChapter` / `searchPosition` / `getResultAndQueryIndex`
- **syncFromWebdav**ï¼šæ¢å¤ books ç›®å½• + bookProgress åˆå¹¶
- **saveToWebdav / createUserBackup / getLastBackFileFromWebdav**
- **backupToWebdav** æ‰“åŒ… books é•œåƒ
- **HttpTTS.name** + æŒ‰ name åˆ é™¤


## Phase 10 å¢é‡

- **UserConfig**ï¼šä¸»é¢˜/å­—ä½“/ç¿»é¡µ/TTS/å¹¶å‘ç­‰é”®ï¼›`saveUserConfig` å†™ `@updateTime`ï¼Œæ”¯æŒ `merge=true`
- **getUserConfig**ï¼šæ— æ–‡ä»¶æ—¶ã€Œæ²¡æœ‰å¤‡ä»½æ–‡ä»¶ã€+ defaults
- **uploadFile / deleteFile**ï¼šç”¨æˆ· assets ä¸Šä¼ ä¸å®‰å…¨åˆ é™¤
- **invalidBookSource ç¼“å­˜**ï¼š`addInvalidBookSource` TTL 600sï¼›ç›®å½•å¤±è´¥å†™å…¥ï¼›å¤šæºæœç´¢è·³è¿‡/æ ‡è®°
- **getInvalidBookSources**ï¼šæŒ‰ ACache hash è¯»å–
- **AnalyzeRule**ï¼šallInOne `:`ã€`<js>`/`@js` æ‹†åˆ†ã€`##`/`###` æ›¿æ¢ã€`@put`ã€`put/get`ã€evalJS å…¨ç»‘å®š
- **CookieStore / CacheManager**ï¼šJS `cookie` / `cache` ç»‘å®š
- **ACache**ï¼šè¿‡æœŸå¤´ + `getByHashCode`


## Phase 11 å¢é‡

- **CookieStore**ï¼šç£ç›˜ ACache æŒ‰ subdomain æŒä¹…åŒ–ï¼›`replaceCookie` / Set-Cookie åˆå¹¶
- **BaseSource**ï¼šloginUrl / loginCheckJs / loginHeader / getHeaderMap(withLogin)
- **BookSource**ï¼šexploreUrlã€enabledExploreã€login* å­—æ®µï¼›`setUserNameSpace`
- **AnalyzeUrl**ï¼šè¯·æ±‚å¸¦ Cookieï¼›å“åº”å†™ Cookieï¼›loginCheckJs é’©å­
- **BookList.explore**ï¼šruleExplore å­—æ®µå›è½ ruleSearchï¼›allInOne åˆ—è¡¨ï¼›`parseExploreUrl`
- **exploreBook API**ï¼šæ—  url æ—¶è¿”å›å‘ç°åˆ†ç±»ï¼›æ”¯æŒ sortUrl
- **ReplaceRuleController**ï¼šæŒ‰ name å¢æ”¹åˆ ï¼›normalize scope/timeout
- **ContentProcessor**ï¼šscope=`all`ï¼›bookName æ”¯æŒ `regex:` ä¸ `/pat/`
- **API_INDEX.md**ï¼šä¸šåŠ¡ä¸€é¡µçº¸å¯¹ç…§

## Phase 12 å¢é‡ï¼ˆä¸ src/main å¯¹é½ï¼‰

- **YueduApi**ï¼šæŒ‰ API_ROUTES.md æŒ‚æ»¡ **133** è·¯ç”±
- **BookMore**ï¼šcache SSE/å¯¼å‡º/TTS/æ­£æ–‡æ£€ç´¢/åˆ†ç»„/å°é¢/æœ¬åœ°é¢„è§ˆ
- **MongoBackup** / ControllerMoreï¼šä¹¦æºæ‰¹é‡ã€æ–‡ä»¶ä¸Šä¼ ã€ç”¨æˆ·èµ„äº§
- **å‰ç«¯**ï¼šsrc/main/resources/web + simple-web ä» jar èµ„æºå›å¡«

## Phase 13 æ·±åº¦

- Xsoup XPath + &&/||/%%
- EpubFile OPF spine/NCX/nav
- EdgeTts + SmokeTest


## Phase 14

- umdlib åªè¯»è§£æ
- SourceLogin + loginBookSource API
- Docker æ„å»º

## Phase 15ï¼ˆloginUi + GHCRï¼‰

- **SourceLogin**ï¼š`loginUi` è§£æï¼ˆJSON æ•°ç»„ / CSV / rowsï¼‰ã€`loginInfo` AES è½ç›˜ã€`loginWithForm`
- **API**ï¼š`GET|POST /reader3/getLoginUi`ã€`POST /reader3/loginBookSource`ã€`POST /reader3/logoutBookSource`
- **AnalyzeRule.evalJS**ï¼šç»‘å®š `loginInfo` + è¡¨å•å­—æ®µé¡¶å±‚åï¼ˆ`username`/`password`â€¦ï¼‰
- **JsExtensions**ï¼š`putLoginHeader` / `getLoginInfo` / `putLoginInfo`
- **BaseSource**ï¼š`getLoginInfo` / `putLoginInfo` / `removeLoginInfo` / `login()`
- **Docker workflow**ï¼špush åˆ° `ghcr.io/${{ github.repository }}`ï¼ˆbranch / semver / latestï¼‰
- **SmokeTest**ï¼šloginUi è§£æã€form loginã€UMD header æ ¡éªŒã€EPUB spine/TOC


## Phase 16£¨UMD Ğ´¶Ë + ÆõÔ¼²âÊÔ + loginUi µ÷ÊÔÒ³£©

- **umdlib Ğ´Â·¾¶**£ºWrapOutputStream¡¢UmdBook.buildUmd¡¢ÕÂ½Ú zlib chunk¡¢·âÃæ/½áÊø¶Î
- **StreamReader** ¶ÔÆë jar£ºEOF ·µ»Ø 0£¬½áÊø section Ñ­»·
- **UmdUtils**£ºstringToUnicodeBytes / compress / genRandomBytes
- **SmokeTest**£ºUMD write¡úread »Æ½ğÍù·µ + UmdFile ¼¯³É£»eader3-routes.txt Â·ÓÉÆõÔ¼
- **bookSourceDebug**£ºÏÔÊ¾ loginUrl/loginUi£»±íµ¥¼ÓÔØ/µÇÂ¼/ÍË³ö API

## Phase 17£¨WebDAV Éî»¯ + Ìæ»»¹æÔò + ÎÄ±¾Ä¿Â¼ + ÆõÔ¼£©

- **WebDAV**£ºHEAD/LOCK/UNLOCK¡¢PROPFIND displayname/getlastmodified¡¢Â·¾¶´©Ô½·À»¤
- **restoreFromWebdav / listWebdavBackups** API
- **ContentProcessor**£ºregex:/pat/ ÊéÃû¹ıÂË¡¢applyRules Ö±µ÷
- **HtmlFormatter.formatKeepImg** ±£Áô img
- **DefaultData** ¼ÓÔØ classpath txtTocRule.json
- **SmokeTest**£ºÌæ»»¹æÔò¡¢TXT ·ÖÕÂ¡¢Cookie/ACache¡¢TTS SSML¡¢WebDAV Â·¾¶

## Phase 18£¨PDF/CBZ/LocalMedia + AnalyzeUrl Ñ¡Ïî£©

- **PdfFile**£ºPDFTextStripper °´Ò³È¡ÎÄ¡¢ÔªÊı¾İ¡¢DPI/¿í¶ÈäÖÈ¾ JPEG ·âÃæ
- **CbzFile**£º×ÔÈ»ÅÅĞò¡¢ComicInfo.xml¡¢getImage¡¢·âÃæ
- **LocalMedia**£ºÍ³Ò» PDF/CBZ Ò³Í¼
- **AnalyzeUrl**£º,{method,body,headers,charset} Ñ¡Ïî¡¢±íµ¥ POST¡¢baseUrl¡¢Cookie¡¢loginCheckJs¡¢retry
- **SmokeTest**£ºAnalyzeUrl ½âÎö¡¢CBZ Íù·µ¡¢PDF ÎÄ±¾+äÖÈ¾

## Phase 19£¨RSS Êµ×° + ±¾µØÒ³Í¼£©

- **RssSourceController**£ºCRUD °´ sourceUrl ºÏ²¢/É¾³ı£»getRssArticles/getRssContent ½ÓÏß Rss ÒıÇæ
- **Rss**£ºRSS2/Atom¡¢enclosure/media¡¢sortUrl ·ÖÀà¡¢parseArticlesFromBody ÀëÏß
- **getLocalBookImage**£ºPDF/CBZ Ò³Í¼Á÷£»cover Ö§³Ö bookUrl¡úLocalMedia
- **getRssSorts** API

## Phase 20£¨Debugger + ¼øÈ¨¼ÓÑÎ£©

- **Debugger**£ºËÑË÷/·¢ÏÖ/ÏêÇé/Ä¿Â¼/ÕıÎÄÁ´Â·£»:: ++ -- ¼üÎ»£»Ê±¼ä´ÁÈÕÖ¾
- **ÃÜÂë**£ºmd5(md5(pw+salt)+salt) + salt ´æÅÌ£»addUser/resetPassword ¶ÔÆë jar
- **checkAuth**£ºaccessToken ²éÑ¯/Í·/Bearer£»session
- **getInvalidBookSources**£º¶Á invalid »º´æÄ¿Â¼

## Phase 21£¨License RSA + WebDAV Basic£©

- **EncoderUtils**£ºRSA ·Ö¶Î¼Ó½âÃÜ£¨¹«Ë½Ô¿£©¡¢genRsaPair
- **LicenseController**£ºgenerateKeys/import/activate/isLicenseValid/isHostValid/decrypt¡¢¹ıÆÚÓë host Ğ£Ñé
- **WebDAV Basic**£ºÓÃ»§+¼ÓÑÎÃÜÂëĞ£Ñé£»accessToken£»°´ÓÃ»§ webdav home

## Phase 22£¨Mongo ÎÄ¼ş»ØÂä + ÓÊÏäÑéÖ¤Âë + Ô¶³Ì¼¤»î£©

- **MongoBackup**£ºMongo ÓÅÏÈ£¬Ê§°Ü/Î´ÅäÖÃÊ±ÎÄ¼ş»ØÂä£»list/delete/backupAll
- **API**£ºlistMongoBackups / deleteMongoBackup / backupAllToMongodb
- **EmailCodeStore**£º6 Î»ÑéÖ¤Âë TTL£»sendCodeToEmail / activate Ğ£Ñé
- **RemoteLicenseClient**£º¿ÉÑ¡ POST µ½ r.htmake.com
- **clearInactiveUsers**£º°´ last_login ÇåÀí

## Phase 23£¨¶¨Ê±ÈÎÎñ + SMTP£©

- **ReaderJobs**£º@Scheduled autoBackup / clearInactive / shelf tick
- **SmtpMailer**£º¿ÉÅäÖÃ SMTP ·¢ÑéÖ¤Âë£»Î´ÅäÖÃÊ±½µ¼¶·µ»Ø code
- **AppConfig/yml**£ºsmtp*¡¢autoBackup*¡¢autoClear*
- **getSystemInfo**£ºjobs ×´Ì¬¡¢smtpConfigured

## Phase 24£¨Êé¼Ü¶¨Ê±Ë¢ĞÂ + OpenAPI£©

- **ShelfRefresh**£º°´ÓÃ»§Êé¼ÜË¢ĞÂ latestChapter/totalChapterNum
- **ReaderJobs.shelfUpdateTick**£º°´ shelfUpdateInteval Ö´ĞĞ
- **OpenAPI**£º/reader3/openapi.json ¡¤ /reader3/apiDocs ¡¤ /reader3/apiRoutes.md
- **refreshShelfBooks** ÊÖ¶¯´¥·¢µ±Ç°ÓÃ»§

## Phase 25£¨Docker / WebUI ¶ÔÆëÔ´ jar£©

- Web SPA ¹ÒÔÚÕ¾µã¸ù /*£¨Ïà¶Ô css/js£©£¬²»ÔÙÖ»¿¿ /web ÖØĞ´
- application.yml / AppConfig ¶ÔÆëÔ´ jar ×Ö¶Î£¨userLimit=15¡¢proxy¡¢defaultUser*£©
- Docker: workDir=/data ¾í¡¢TZ¡¢entrypoint¡¢healthcheck¡¢compose Óë GHCR
- banner + logback-spring ´ÓÔ´ jar »ØÌî
