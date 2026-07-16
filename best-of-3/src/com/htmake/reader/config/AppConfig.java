//
// Decompiled by Procyon v0.6.0
//

package com.htmake.reader.config;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "reader.app")
@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b \n\u0002\u0010\u000e\n\u0002\b8\b\u0017\u0018\u00002\u00020\u0001B\u0005?\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\bR\u001a\u0010\u0012\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0006\"\u0004\b\u0014\u0010\bR\u001a\u0010\u0015\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0006\"\u0004\b\u0017\u0010\bR\u001a\u0010\u0018\u001a\u00020\nX\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\f\"\u0004\b\u001a\u0010\u000eR\u001a\u0010\u001b\u001a\u00020\nX\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\f\"\u0004\b\u001d\u0010\u000eR\u001a\u0010\u001e\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0006\"\u0004\b \u0010\bR\u001a\u0010!\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0006\"\u0004\b#\u0010\bR\u001a\u0010$\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0006\"\u0004\b&\u0010\bR\u001a\u0010'\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0006\"\u0004\b)\u0010\bR\u001a\u0010*\u001a\u00020+X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b,\u0010-\"\u0004\b.\u0010/R\u001a\u00100\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\u0006\"\u0004\b2\u0010\bR\u001a\u00103\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b4\u0010\u0006\"\u0004\b5\u0010\bR\u001a\u00106\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b7\u0010\u0006\"\u0004\b8\u0010\bR\u001a\u00109\u001a\u00020+X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b:\u0010-\"\u0004\b;\u0010/R\u001a\u0010<\u001a\u00020\nX\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b=\u0010\f\"\u0004\b>\u0010\u000eR\u001a\u0010?\u001a\u00020+X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b@\u0010-\"\u0004\bA\u0010/R\u001a\u0010B\u001a\u00020+X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bC\u0010-\"\u0004\bD\u0010/R\u001a\u0010E\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bF\u0010\u0006\"\u0004\bG\u0010\bR\u001a\u0010H\u001a\u00020\nX\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bI\u0010\f\"\u0004\bJ\u0010\u000eR\u001a\u0010K\u001a\u00020+X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bL\u0010-\"\u0004\bM\u0010/R\u001a\u0010N\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bO\u0010\u0006\"\u0004\bP\u0010\bR\u001a\u0010Q\u001a\u00020+X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bR\u0010-\"\u0004\bS\u0010/R\u001a\u0010T\u001a\u00020\nX\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bU\u0010\f\"\u0004\bV\u0010\u000eR\u001a\u0010W\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bX\u0010\u0006\"\u0004\bY\u0010\bR\u001a\u0010Z\u001a\u00020\nX\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b[\u0010\f\"\u0004\b\\\u0010\u000eR\u001a\u0010]\u001a\u00020\nX\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b^\u0010\f\"\u0004\b_\u0010\u000eR\u001a\u0010`\u001a\u00020+X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\ba\u0010-\"\u0004\bb\u0010/��\u0006c" }, d2 = { "Lcom/htmake/reader/config/AppConfig;", "", "()V", "autoBackupUserData", "", "getAutoBackupUserData", "()Z", "setAutoBackupUserData", "(Z)V", "autoClearInactiveUser", "", "getAutoClearInactiveUser", "()I", "setAutoClearInactiveUser", "(I)V", "cacheChapterContent", "getCacheChapterContent", "setCacheChapterContent", "debug", "getDebug", "setDebug", "debugLog", "getDebugLog", "setDebugLog", "defaultUserBookLimit", "getDefaultUserBookLimit", "setDefaultUserBookLimit", "defaultUserBookSourceLimit", "getDefaultUserBookSourceLimit", "setDefaultUserBookSourceLimit", "defaultUserEnableBookSource", "getDefaultUserEnableBookSource", "setDefaultUserEnableBookSource", "defaultUserEnableLocalStore", "getDefaultUserEnableLocalStore", "setDefaultUserEnableLocalStore", "defaultUserEnableRssSource", "getDefaultUserEnableRssSource", "setDefaultUserEnableRssSource", "defaultUserEnableWebdav", "getDefaultUserEnableWebdav", "setDefaultUserEnableWebdav", "exportCharset", "", "getExportCharset", "()Ljava/lang/String;", "setExportCharset", "(Ljava/lang/String;)V", "exportNoChapterName", "getExportNoChapterName", "setExportNoChapterName", "exportPictureFile", "getExportPictureFile", "setExportPictureFile", "exportUseReplace", "getExportUseReplace", "setExportUseReplace", "inviteCode", "getInviteCode", "setInviteCode", "minUserPasswordLength", "getMinUserPasswordLength", "setMinUserPasswordLength", "mongoDbName", "getMongoDbName", "setMongoDbName", "mongoUri", "getMongoUri", "setMongoUri", "packaged", "getPackaged", "setPackaged", "remoteBookSourceUpdateInterval", "getRemoteBookSourceUpdateInterval", "setRemoteBookSourceUpdateInterval", "remoteWebviewApi", "getRemoteWebviewApi", "setRemoteWebviewApi", "secure", "getSecure", "setSecure", "secureKey", "getSecureKey", "setSecureKey", "shelfUpdateInteval", "getShelfUpdateInteval", "setShelfUpdateInteval", "showUI", "getShowUI", "setShowUI", "userBookLimit", "getUserBookLimit", "setUserBookLimit", "userLimit", "getUserLimit", "setUserLimit", "workDir", "getWorkDir", "setWorkDir", "reader-pro" })
public class AppConfig
{
    @NotNull
    private String workDir;
    private boolean showUI;
    private boolean debug;
    private boolean packaged;
    private boolean secure;
    @NotNull
    private String inviteCode;
    @NotNull
    private String secureKey;
    private boolean cacheChapterContent;
    private int userLimit;
    private int userBookLimit;
    private boolean debugLog;
    private int autoClearInactiveUser;
    private boolean exportUseReplace;
    @NotNull
    private String exportCharset;
    private boolean exportNoChapterName;
    private boolean exportPictureFile;
    @NotNull
    private String mongoUri;
    @NotNull
    private String mongoDbName;
    private int shelfUpdateInteval;
    @NotNull
    private String remoteWebviewApi;
    private boolean defaultUserEnableWebdav;
    private boolean defaultUserEnableLocalStore;
    private boolean defaultUserEnableBookSource;
    private boolean defaultUserEnableRssSource;
    private int defaultUserBookSourceLimit;
    private int defaultUserBookLimit;
    private boolean autoBackupUserData;
    private int minUserPasswordLength;
    private int remoteBookSourceUpdateInterval;

    public AppConfig() {
        this.workDir = "";
        this.inviteCode = "";
        this.secureKey = "";
        this.userLimit = 15;
        this.userBookLimit = 200;
        this.exportCharset = "UTF-8";
        this.mongoUri = "";
        this.mongoDbName = "reader";
        this.shelfUpdateInteval = 10;
        this.remoteWebviewApi = "";
        this.defaultUserEnableBookSource = true;
        this.defaultUserEnableRssSource = true;
        this.defaultUserBookSourceLimit = 200;
        this.defaultUserBookLimit = 200;
        this.minUserPasswordLength = 8;
        this.remoteBookSourceUpdateInterval = 720;
    }

    @NotNull
    public String getWorkDir() {
        return this.workDir;
    }

    public void setWorkDir(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.workDir = <set-?>;
    }

    public boolean getShowUI() {
        return this.showUI;
    }

    public void setShowUI(final boolean <set-?>) {
        this.showUI = <set-?>;
    }

    public boolean getDebug() {
        return this.debug;
    }

    public void setDebug(final boolean <set-?>) {
        this.debug = <set-?>;
    }

    public boolean getPackaged() {
        return this.packaged;
    }

    public void setPackaged(final boolean <set-?>) {
        this.packaged = <set-?>;
    }

    public boolean getSecure() {
        return this.secure;
    }

    public void setSecure(final boolean <set-?>) {
        this.secure = <set-?>;
    }

    @NotNull
    public String getInviteCode() {
        return this.inviteCode;
    }

    public void setInviteCode(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.inviteCode = <set-?>;
    }

    @NotNull
    public String getSecureKey() {
        return this.secureKey;
    }

    public void setSecureKey(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.secureKey = <set-?>;
    }

    public boolean getCacheChapterContent() {
        return this.cacheChapterContent;
    }

    public void setCacheChapterContent(final boolean <set-?>) {
        this.cacheChapterContent = <set-?>;
    }

    public int getUserLimit() {
        return this.userLimit;
    }

    public void setUserLimit(final int <set-?>) {
        this.userLimit = <set-?>;
    }

    public int getUserBookLimit() {
        return this.userBookLimit;
    }

    public void setUserBookLimit(final int <set-?>) {
        this.userBookLimit = <set-?>;
    }

    public boolean getDebugLog() {
        return this.debugLog;
    }

    public void setDebugLog(final boolean <set-?>) {
        this.debugLog = <set-?>;
    }

    public int getAutoClearInactiveUser() {
        return this.autoClearInactiveUser;
    }

    public void setAutoClearInactiveUser(final int <set-?>) {
        this.autoClearInactiveUser = <set-?>;
    }

    public boolean getExportUseReplace() {
        return this.exportUseReplace;
    }

    public void setExportUseReplace(final boolean <set-?>) {
        this.exportUseReplace = <set-?>;
    }

    @NotNull
    public String getExportCharset() {
        return this.exportCharset;
    }

    public void setExportCharset(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.exportCharset = <set-?>;
    }

    public boolean getExportNoChapterName() {
        return this.exportNoChapterName;
    }

    public void setExportNoChapterName(final boolean <set-?>) {
        this.exportNoChapterName = <set-?>;
    }

    public boolean getExportPictureFile() {
        return this.exportPictureFile;
    }

    public void setExportPictureFile(final boolean <set-?>) {
        this.exportPictureFile = <set-?>;
    }

    @NotNull
    public String getMongoUri() {
        return this.mongoUri;
    }

    public void setMongoUri(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.mongoUri = <set-?>;
    }

    @NotNull
    public String getMongoDbName() {
        return this.mongoDbName;
    }

    public void setMongoDbName(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.mongoDbName = <set-?>;
    }

    public int getShelfUpdateInteval() {
        return this.shelfUpdateInteval;
    }

    public void setShelfUpdateInteval(final int <set-?>) {
        this.shelfUpdateInteval = <set-?>;
    }

    @NotNull
    public String getRemoteWebviewApi() {
        return this.remoteWebviewApi;
    }

    public void setRemoteWebviewApi(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.remoteWebviewApi = <set-?>;
    }

    public boolean getDefaultUserEnableWebdav() {
        return this.defaultUserEnableWebdav;
    }

    public void setDefaultUserEnableWebdav(final boolean <set-?>) {
        this.defaultUserEnableWebdav = <set-?>;
    }

    public boolean getDefaultUserEnableLocalStore() {
        return this.defaultUserEnableLocalStore;
    }

    public void setDefaultUserEnableLocalStore(final boolean <set-?>) {
        this.defaultUserEnableLocalStore = <set-?>;
    }

    public boolean getDefaultUserEnableBookSource() {
        return this.defaultUserEnableBookSource;
    }

    public void setDefaultUserEnableBookSource(final boolean <set-?>) {
        this.defaultUserEnableBookSource = <set-?>;
    }

    public boolean getDefaultUserEnableRssSource() {
        return this.defaultUserEnableRssSource;
    }

    public void setDefaultUserEnableRssSource(final boolean <set-?>) {
        this.defaultUserEnableRssSource = <set-?>;
    }

    public int getDefaultUserBookSourceLimit() {
        return this.defaultUserBookSourceLimit;
    }

    public void setDefaultUserBookSourceLimit(final int <set-?>) {
        this.defaultUserBookSourceLimit = <set-?>;
    }

    public int getDefaultUserBookLimit() {
        return this.defaultUserBookLimit;
    }

    public void setDefaultUserBookLimit(final int <set-?>) {
        this.defaultUserBookLimit = <set-?>;
    }

    public boolean getAutoBackupUserData() {
        return this.autoBackupUserData;
    }

    public void setAutoBackupUserData(final boolean <set-?>) {
        this.autoBackupUserData = <set-?>;
    }

    public int getMinUserPasswordLength() {
        return this.minUserPasswordLength;
    }

    public void setMinUserPasswordLength(final int <set-?>) {
        this.minUserPasswordLength = <set-?>;
    }

    public int getRemoteBookSourceUpdateInterval() {
        return this.remoteBookSourceUpdateInterval;
    }

    public void setRemoteBookSourceUpdateInterval(final int <set-?>) {
        this.remoteBookSourceUpdateInterval = <set-?>;
    }
}
