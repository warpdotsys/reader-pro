package com.htmake.reader.config;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/* JADX INFO: compiled from: AppConfig.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/config/AppConfig.class */
@ConfigurationProperties(prefix = "reader.app")
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b \n\u0002\u0010\u000e\n\u0002\b8\b\u0017\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\bR\u001a\u0010\u0012\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0006\"\u0004\b\u0014\u0010\bR\u001a\u0010\u0015\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0006\"\u0004\b\u0017\u0010\bR\u001a\u0010\u0018\u001a\u00020\nX\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\f\"\u0004\b\u001a\u0010\u000eR\u001a\u0010\u001b\u001a\u00020\nX\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\f\"\u0004\b\u001d\u0010\u000eR\u001a\u0010\u001e\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0006\"\u0004\b \u0010\bR\u001a\u0010!\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0006\"\u0004\b#\u0010\bR\u001a\u0010$\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0006\"\u0004\b&\u0010\bR\u001a\u0010'\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0006\"\u0004\b)\u0010\bR\u001a\u0010*\u001a\u00020+X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010-\"\u0004\b.\u0010/R\u001a\u00100\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\u0006\"\u0004\b2\u0010\bR\u001a\u00103\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b4\u0010\u0006\"\u0004\b5\u0010\bR\u001a\u00106\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b7\u0010\u0006\"\u0004\b8\u0010\bR\u001a\u00109\u001a\u00020+X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b:\u0010-\"\u0004\b;\u0010/R\u001a\u0010<\u001a\u00020\nX\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b=\u0010\f\"\u0004\b>\u0010\u000eR\u001a\u0010?\u001a\u00020+X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b@\u0010-\"\u0004\bA\u0010/R\u001a\u0010B\u001a\u00020+X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bC\u0010-\"\u0004\bD\u0010/R\u001a\u0010E\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bF\u0010\u0006\"\u0004\bG\u0010\bR\u001a\u0010H\u001a\u00020\nX\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bI\u0010\f\"\u0004\bJ\u0010\u000eR\u001a\u0010K\u001a\u00020+X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bL\u0010-\"\u0004\bM\u0010/R\u001a\u0010N\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bO\u0010\u0006\"\u0004\bP\u0010\bR\u001a\u0010Q\u001a\u00020+X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bR\u0010-\"\u0004\bS\u0010/R\u001a\u0010T\u001a\u00020\nX\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bU\u0010\f\"\u0004\bV\u0010\u000eR\u001a\u0010W\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bX\u0010\u0006\"\u0004\bY\u0010\bR\u001a\u0010Z\u001a\u00020\nX\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b[\u0010\f\"\u0004\b\\\u0010\u000eR\u001a\u0010]\u001a\u00020\nX\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b^\u0010\f\"\u0004\b_\u0010\u000eR\u001a\u0010`\u001a\u00020+X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\ba\u0010-\"\u0004\bb\u0010/¨\u0006c"}, d2 = {"Lcom/htmake/reader/config/AppConfig;", PackageDocumentBase.PREFIX_OPF, "()V", "autoBackupUserData", PackageDocumentBase.PREFIX_OPF, "getAutoBackupUserData", "()Z", "setAutoBackupUserData", "(Z)V", "autoClearInactiveUser", PackageDocumentBase.PREFIX_OPF, "getAutoClearInactiveUser", "()I", "setAutoClearInactiveUser", "(I)V", "cacheChapterContent", "getCacheChapterContent", "setCacheChapterContent", "debug", "getDebug", "setDebug", "debugLog", "getDebugLog", "setDebugLog", "defaultUserBookLimit", "getDefaultUserBookLimit", "setDefaultUserBookLimit", "defaultUserBookSourceLimit", "getDefaultUserBookSourceLimit", "setDefaultUserBookSourceLimit", "defaultUserEnableBookSource", "getDefaultUserEnableBookSource", "setDefaultUserEnableBookSource", "defaultUserEnableLocalStore", "getDefaultUserEnableLocalStore", "setDefaultUserEnableLocalStore", "defaultUserEnableRssSource", "getDefaultUserEnableRssSource", "setDefaultUserEnableRssSource", "defaultUserEnableWebdav", "getDefaultUserEnableWebdav", "setDefaultUserEnableWebdav", "exportCharset", PackageDocumentBase.PREFIX_OPF, "getExportCharset", "()Ljava/lang/String;", "setExportCharset", "(Ljava/lang/String;)V", "exportNoChapterName", "getExportNoChapterName", "setExportNoChapterName", "exportPictureFile", "getExportPictureFile", "setExportPictureFile", "exportUseReplace", "getExportUseReplace", "setExportUseReplace", "inviteCode", "getInviteCode", "setInviteCode", "minUserPasswordLength", "getMinUserPasswordLength", "setMinUserPasswordLength", "mongoDbName", "getMongoDbName", "setMongoDbName", "mongoUri", "getMongoUri", "setMongoUri", "packaged", "getPackaged", "setPackaged", "remoteBookSourceUpdateInterval", "getRemoteBookSourceUpdateInterval", "setRemoteBookSourceUpdateInterval", "remoteWebviewApi", "getRemoteWebviewApi", "setRemoteWebviewApi", "secure", "getSecure", "setSecure", "secureKey", "getSecureKey", "setSecureKey", "shelfUpdateInteval", "getShelfUpdateInteval", "setShelfUpdateInteval", "showUI", "getShowUI", "setShowUI", "userBookLimit", "getUserBookLimit", "setUserBookLimit", "userLimit", "getUserLimit", "setUserLimit", "workDir", "getWorkDir", "setWorkDir", "reader-pro"})
@Component
public class AppConfig {
    private boolean showUI;
    private boolean debug;
    private boolean packaged;
    private boolean secure;
    private boolean cacheChapterContent;
    private boolean debugLog;
    private int autoClearInactiveUser;
    private boolean exportUseReplace;
    private boolean exportNoChapterName;
    private boolean exportPictureFile;
    private boolean defaultUserEnableWebdav;
    private boolean defaultUserEnableLocalStore;
    private boolean autoBackupUserData;

    @NotNull
    private String workDir = PackageDocumentBase.PREFIX_OPF;

    @NotNull
    private String inviteCode = PackageDocumentBase.PREFIX_OPF;

    @NotNull
    private String secureKey = PackageDocumentBase.PREFIX_OPF;
    private int userLimit = 15;
    private int userBookLimit = 200;

    @NotNull
    private String exportCharset = Constants.CHARACTER_ENCODING;

    @NotNull
    private String mongoUri = PackageDocumentBase.PREFIX_OPF;

    @NotNull
    private String mongoDbName = "reader";
    private int shelfUpdateInteval = 10;

    @NotNull
    private String remoteWebviewApi = PackageDocumentBase.PREFIX_OPF;
    private boolean defaultUserEnableBookSource = true;
    private boolean defaultUserEnableRssSource = true;
    private int defaultUserBookSourceLimit = 200;
    private int defaultUserBookLimit = 200;
    private int minUserPasswordLength = 8;
    private int remoteBookSourceUpdateInterval = 720;

    @NotNull
    public String getWorkDir() {
        return this.workDir;
    }

    public void setWorkDir(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.workDir = str;
    }

    public boolean getShowUI() {
        return this.showUI;
    }

    public void setShowUI(boolean z) {
        this.showUI = z;
    }

    public boolean getDebug() {
        return this.debug;
    }

    public void setDebug(boolean z) {
        this.debug = z;
    }

    public boolean getPackaged() {
        return this.packaged;
    }

    public void setPackaged(boolean z) {
        this.packaged = z;
    }

    public boolean getSecure() {
        return this.secure;
    }

    public void setSecure(boolean z) {
        this.secure = z;
    }

    @NotNull
    public String getInviteCode() {
        return this.inviteCode;
    }

    public void setInviteCode(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.inviteCode = str;
    }

    @NotNull
    public String getSecureKey() {
        return this.secureKey;
    }

    public void setSecureKey(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.secureKey = str;
    }

    public boolean getCacheChapterContent() {
        return this.cacheChapterContent;
    }

    public void setCacheChapterContent(boolean z) {
        this.cacheChapterContent = z;
    }

    public int getUserLimit() {
        return this.userLimit;
    }

    public void setUserLimit(int i) {
        this.userLimit = i;
    }

    public int getUserBookLimit() {
        return this.userBookLimit;
    }

    public void setUserBookLimit(int i) {
        this.userBookLimit = i;
    }

    public boolean getDebugLog() {
        return this.debugLog;
    }

    public void setDebugLog(boolean z) {
        this.debugLog = z;
    }

    public int getAutoClearInactiveUser() {
        return this.autoClearInactiveUser;
    }

    public void setAutoClearInactiveUser(int i) {
        this.autoClearInactiveUser = i;
    }

    public boolean getExportUseReplace() {
        return this.exportUseReplace;
    }

    public void setExportUseReplace(boolean z) {
        this.exportUseReplace = z;
    }

    @NotNull
    public String getExportCharset() {
        return this.exportCharset;
    }

    public void setExportCharset(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.exportCharset = str;
    }

    public boolean getExportNoChapterName() {
        return this.exportNoChapterName;
    }

    public void setExportNoChapterName(boolean z) {
        this.exportNoChapterName = z;
    }

    public boolean getExportPictureFile() {
        return this.exportPictureFile;
    }

    public void setExportPictureFile(boolean z) {
        this.exportPictureFile = z;
    }

    @NotNull
    public String getMongoUri() {
        return this.mongoUri;
    }

    public void setMongoUri(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.mongoUri = str;
    }

    @NotNull
    public String getMongoDbName() {
        return this.mongoDbName;
    }

    public void setMongoDbName(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.mongoDbName = str;
    }

    public int getShelfUpdateInteval() {
        return this.shelfUpdateInteval;
    }

    public void setShelfUpdateInteval(int i) {
        this.shelfUpdateInteval = i;
    }

    @NotNull
    public String getRemoteWebviewApi() {
        return this.remoteWebviewApi;
    }

    public void setRemoteWebviewApi(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.remoteWebviewApi = str;
    }

    public boolean getDefaultUserEnableWebdav() {
        return this.defaultUserEnableWebdav;
    }

    public void setDefaultUserEnableWebdav(boolean z) {
        this.defaultUserEnableWebdav = z;
    }

    public boolean getDefaultUserEnableLocalStore() {
        return this.defaultUserEnableLocalStore;
    }

    public void setDefaultUserEnableLocalStore(boolean z) {
        this.defaultUserEnableLocalStore = z;
    }

    public boolean getDefaultUserEnableBookSource() {
        return this.defaultUserEnableBookSource;
    }

    public void setDefaultUserEnableBookSource(boolean z) {
        this.defaultUserEnableBookSource = z;
    }

    public boolean getDefaultUserEnableRssSource() {
        return this.defaultUserEnableRssSource;
    }

    public void setDefaultUserEnableRssSource(boolean z) {
        this.defaultUserEnableRssSource = z;
    }

    public int getDefaultUserBookSourceLimit() {
        return this.defaultUserBookSourceLimit;
    }

    public void setDefaultUserBookSourceLimit(int i) {
        this.defaultUserBookSourceLimit = i;
    }

    public int getDefaultUserBookLimit() {
        return this.defaultUserBookLimit;
    }

    public void setDefaultUserBookLimit(int i) {
        this.defaultUserBookLimit = i;
    }

    public boolean getAutoBackupUserData() {
        return this.autoBackupUserData;
    }

    public void setAutoBackupUserData(boolean z) {
        this.autoBackupUserData = z;
    }

    public int getMinUserPasswordLength() {
        return this.minUserPasswordLength;
    }

    public void setMinUserPasswordLength(int i) {
        this.minUserPasswordLength = i;
    }

    public int getRemoteBookSourceUpdateInterval() {
        return this.remoteBookSourceUpdateInterval;
    }

    public void setRemoteBookSourceUpdateInterval(int i) {
        this.remoteBookSourceUpdateInterval = i;
    }
}
