# reader-pro 3.2.14 API Routes

Extracted from YueduApi.initRouter, total unique endpoints: 133


## Book

- `GET    /reader3/getSystemInfo`
- `GET    /reader3/getBookshelf`
- `GET    /reader3/getShelfBook`
- `POST   /reader3/saveBook`
- `POST   /reader3/deleteBook`
- `POST   /reader3/deleteBooks`
- `POST   /reader3/exploreBook`
- `GET    /reader3/exploreBook`
- `GET    /reader3/searchBook`
- `POST   /reader3/searchBook`
- `GET    /reader3/searchBookMulti`
- `POST   /reader3/searchBookMulti`
- `GET    /reader3/searchBookMultiSSE`
- `GET    /reader3/getBookInfo`
- `POST   /reader3/getBookInfo`
- `GET    /reader3/getChapterList`
- `POST   /reader3/getChapterList`
- `GET    /reader3/getBookContent`
- `POST   /reader3/getBookContent`
- `POST   /reader3/saveBookContent`
- `POST   /reader3/saveBookProgress`
- `GET    /reader3/cover`
- `POST   /reader3/importBookPreview`
- `POST   /reader3/refreshLocalBook`
- `GET    /reader3/getTxtTocRules`
- `POST   /reader3/getChapterListByRule`
- `GET    /reader3/cacheBookSSE`
- `POST   /reader3/cacheBookOnServer`
- `GET    /reader3/getShelfBookWithCacheInfo`
- `POST   /reader3/deleteBookCache`
- `POST   /reader3/exportBook`
- `GET    /reader3/exportBook`
- `GET    /reader3/searchBookContent`
- `POST   /reader3/searchBookContent`
- `POST   /reader3/book/saveBookConfig`
- `GET    /reader3/file/list`
- `GET    /reader3/file/get`
- `POST   /reader3/file/save`
- `POST   /reader3/file/mkdir`
- `GET    /reader3/file/download`
- `POST   /reader3/file/delete`
- `POST   /reader3/file/deleteMulti`
- `POST   /reader3/file/importPreview`
- `POST   /reader3/file/restore`
- `GET    /reader3/file/parse`
- `POST   /reader3/file/parse`

## BookGroup

- `POST   /reader3/saveBookGroupId`
- `POST   /reader3/addBookGroupMulti`
- `POST   /reader3/removeBookGroupMulti`
- `GET    /reader3/getBookGroups`
- `POST   /reader3/saveBookGroup`
- `POST   /reader3/deleteBookGroup`
- `POST   /reader3/saveBookGroupOrder`

## BookSource

- `POST   /reader3/saveBookSource`
- `POST   /reader3/saveBookSources`
- `GET    /reader3/getBookSource`
- `POST   /reader3/getBookSource`
- `GET    /reader3/getBookSources`
- `POST   /reader3/getBookSources`
- `POST   /reader3/deleteAllBookSources`
- `POST   /reader3/deleteBookSource`
- `POST   /reader3/deleteBookSources`
- `POST   /reader3/readSourceFile`
- `POST   /reader3/saveFromRemoteSource`
- `POST   /reader3/setAsDefaultBookSources`
- `POST   /reader3/deleteUserBookSource`
- `POST   /reader3/deleteBookSourcesFile`
- `POST   /reader3/getInvalidBookSources`
- `GET    /reader3/searchBookSource`
- `POST   /reader3/searchBookSource`
- `GET    /reader3/getAvailableBookSource`
- `POST   /reader3/getAvailableBookSource`
- `GET    /reader3/searchBookSourceSSE`
- `GET    /reader3/setBookSource`
- `POST   /reader3/setBookSource`
- `GET    /reader3/bookSourceDebugSSE`

## Bookmark

- `GET    /reader3/getBookmarks`
- `POST   /reader3/saveBookmark`
- `POST   /reader3/saveBookmarks`
- `POST   /reader3/deleteBookmark`
- `POST   /reader3/deleteBookmarks`

## File

- `POST   /reader3/uploadFile`
- `POST   /reader3/deleteFile`
- `GET    /reader3/user/downloadBackupFile`
- `POST   /reader3/file/upload`

## License

- `GET    /reader3/getLicense`
- `POST   /reader3/importLicense`
- `GET    /reader3/generateKeys`
- `POST   /reader3/generateKeys`
- `GET    /reader3/generateLicense`
- `POST   /reader3/generateLicense`
- `GET    /reader3/isHostValid`
- `POST   /reader3/isHostValid`
- `POST   /reader3/activateLicense`
- `GET    /reader3/isLicenseValid`
- `POST   /reader3/isLicenseValid`
- `POST   /reader3/decryptLicense`
- `POST   /reader3/sendCodeToEmail`
- `POST   /reader3/supplyLicense`

## MongoBackup

- `POST   /reader3/backupToMongodb`
- `POST   /reader3/restoreFromMongodb`

## RSS

- `GET    /reader3/getRssSources`
- `POST   /reader3/saveRssSource`
- `POST   /reader3/saveRssSources`
- `POST   /reader3/deleteRssSource`
- `GET    /reader3/getRssArticles`
- `POST   /reader3/getRssArticles`
- `GET    /reader3/getRssContent`
- `POST   /reader3/getRssContent`

## ReplaceRule

- `GET    /reader3/getReplaceRules`
- `POST   /reader3/saveReplaceRule`
- `POST   /reader3/saveReplaceRules`
- `POST   /reader3/deleteReplaceRule`
- `POST   /reader3/deleteReplaceRules`

## TTS

- `GET    /reader3/book/tts`
- `POST   /reader3/book/tts`
- `GET    /reader3/httpTTS/list`
- `POST   /reader3/httpTTS/save`
- `POST   /reader3/httpTTS/saveMulti`
- `POST   /reader3/httpTTS/delete`
- `POST   /reader3/httpTTS/deleteMulti`

## User

- `POST   /reader3/login`
- `POST   /reader3/logout`
- `GET    /reader3/getUserInfo`
- `POST   /reader3/saveUserConfig`
- `GET    /reader3/getUserConfig`
- `GET    /reader3/getUserList`
- `POST   /reader3/deleteUsers`
- `POST   /reader3/clearInactiveUsers`
- `POST   /reader3/addUser`
- `POST   /reader3/resetPassword`
- `POST   /reader3/updateUser`

## WebDAV

- `POST   /reader3/backupToWebdav`

## Static routes

- `ROUTE  /*`
- `ROUTE  /assets/*`
- `ROUTE  /book-assets/*`
- `ROUTE  /book-assets/*`
- `ROUTE  /epub/*`
- `ROUTE  /epub/*`
- `ROUTE  /simple-web`
- `ROUTE  /simple-web/*`
- `ROUTE  /simple-web/*`