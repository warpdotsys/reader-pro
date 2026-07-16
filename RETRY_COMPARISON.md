# Decompiler retry comparison (imperfect set only)

Files compared: 43

## Winners

- **vf**: 31
- **cfr**: 6
- **pr**: 6

## Per file

| File | Winner | CFR penalty | VF penalty | Procyon penalty | CFR hard/stub | VF hard/stub | PR hard/stub |
|---|---|---:|---:|---:|---|---|---|
| `com/htmake/reader/api/YueduApi` | vf | 2620 | 0 | 600 | 0/0 | 0/0 | 0/0 |
| `com/htmake/reader/api/controller/BaseController` | cfr | 1520 | 4930 | 1700 | 0/0 | 4/0 | 0/0 |
| `com/htmake/reader/api/controller/BookController` | vf | 36480 | 22270 | 23840 | 0/0 | 18/0 | 0/0 |
| `com/htmake/reader/api/controller/BookGroupController` | vf | 340 | 0 | 280 | 0/0 | 0/0 | 0/0 |
| `com/htmake/reader/api/controller/BookSourceController` | vf | 4680 | 0 | 3670 | 0/0 | 0/0 | 0/0 |
| `com/htmake/reader/api/controller/CURD` | vf | 1700 | 0 | 1000 | 0/0 | 0/0 | 0/0 |
| `com/htmake/reader/api/controller/FileController` | vf | 4200 | 0 | 3100 | 0/0 | 0/0 | 0/0 |
| `com/htmake/reader/api/controller/LicenseController` | vf | 780 | 0 | 200 | 0/0 | 0/0 | 0/0 |
| `com/htmake/reader/api/controller/RssSourceController` | vf | 2760 | 0 | 2200 | 0/0 | 0/0 | 0/0 |
| `com/htmake/reader/api/controller/UserController` | vf | 7750 | 2450 | 4300 | 0/0 | 2/0 | 0/0 |
| `com/htmake/reader/api/controller/WebdavController` | vf | 1190 | 0 | 1200 | 0/0 | 0/0 | 0/0 |
| `com/htmake/reader/utils/ExtKt` | vf | 220 | 0 | 380 | 0/0 | 0/0 | 0/0 |
| `com/htmake/reader/utils/RemoteWebview` | vf | 340 | 0 | 200 | 0/0 | 0/0 | 0/0 |
| `com/htmake/reader/utils/UserMutex` | vf | 340 | 0 | 20 | 0/0 | 0/0 | 0/0 |
| `com/htmake/reader/utils/VertExtKt` | pr | 20 | 0 | 0 | 0/0 | 0/0 | 0/0 |
| `com/htmake/reader/verticle/RestVerticle` | vf | 710 | 0 | 400 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/adapters/DefaultAdpater` | vf | 220 | 0 | 200 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/constant/BookType` | pr | 20 | 0 | 0 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/data/entities/Book` | cfr | 40 | 0 | 0 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/data/entities/BookSource` | cfr | 20 | 0 | 0 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/data/entities/HttpTTS` | cfr | 20 | 0 | 0 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/data/entities/RssSource` | cfr | 20 | 0 | 200 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/help/BookHelp` | cfr | 810 | 2440 | 820 | 0/0 | 2/0 | 0/0 |
| `io/legado/app/help/JsExtensions` | vf | 1140 | 0 | 40 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/help/SourceAnalyzer` | vf | 240 | 0 | 600 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/help/coroutine/Coroutine` | pr | 7620 | 2400 | 0 | 6/2 | 2/0 | 0/0 |
| `io/legado/app/help/http/ByteConverter` | pr | 20 | 0 | 0 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/help/http/EncodeConverter` | vf | 20 | 0 | 0 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/help/http/OkHttpUtilsKt` | vf | 1600 | 0 | 1400 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/help/http/SSLHelper` | pr | 20 | 0 | 0 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/model/Debugger` | vf | 1870 | 0 | 2000 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/model/analyzeRule/AnalyzeRule` | vf | 790 | 0 | 220 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/model/analyzeRule/AnalyzeUrl` | vf | 1620 | 0 | 2620 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/model/localBook/EpubFile` | vf | 440 | 0 | 200 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/model/rss/Rss` | vf | 680 | 0 | 400 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/model/webBook/BookChapterList` | vf | 7600 | 0 | 380 | 6/2 | 0/0 | 0/0 |
| `io/legado/app/model/webBook/BookContent` | vf | 1260 | 0 | 210 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/model/webBook/BookInfo` | vf | 220 | 0 | 200 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/model/webBook/BookList` | vf | 1400 | 0 | 390 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/model/webBook/WebBook` | vf | 3510 | 0 | 2560 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/utils/ACache` | pr | 120 | 0 | 0 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/utils/EncoderUtils` | vf | 220 | 0 | 0 | 0/0 | 0/0 | 0/0 |
| `io/legado/app/utils/FileUtils` | vf | 20 | 0 | 60 | 0/0 | 0/0 | 0/0 |