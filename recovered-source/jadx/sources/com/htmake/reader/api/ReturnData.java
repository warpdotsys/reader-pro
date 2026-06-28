package com.htmake.reader.api;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: ReturnData.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/ReturnData.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u000e\u001a\u00020\u00002\u0006\u0010\u0004\u001a\u00020\u00012\b\b\u0002\u0010\u000f\u001a\u00020\u0007J\u000e\u0010\u0010\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\u0007R\"\u0010\u0004\u001a\u0004\u0018\u00010\u00012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0001@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001e\u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0007@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001e\u0010\f\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0011"}, d2 = {"Lcom/htmake/reader/api/ReturnData;", PackageDocumentBase.PREFIX_OPF, "()V", "<set-?>", "data", "getData", "()Ljava/lang/Object;", PackageDocumentBase.PREFIX_OPF, "errorMsg", "getErrorMsg", "()Ljava/lang/String;", PackageDocumentBase.PREFIX_OPF, "isSuccess", "()Z", "setData", "msg", "setErrorMsg", "reader-pro"})
public final class ReturnData {
    private boolean isSuccess;

    @NotNull
    private String errorMsg = "未知错误,请联系开发者!";

    @Nullable
    private Object data;

    public final boolean isSuccess() {
        return this.isSuccess;
    }

    @NotNull
    public final String getErrorMsg() {
        return this.errorMsg;
    }

    @Nullable
    public final Object getData() {
        return this.data;
    }

    @NotNull
    public final ReturnData setErrorMsg(@NotNull String errorMsg) {
        Intrinsics.checkNotNullParameter(errorMsg, "errorMsg");
        this.isSuccess = false;
        this.errorMsg = errorMsg;
        return this;
    }

    public static /* synthetic */ ReturnData setData$default(ReturnData returnData, Object obj, String str, int i, Object obj2) {
        if ((i & 2) != 0) {
            str = PackageDocumentBase.PREFIX_OPF;
        }
        return returnData.setData(obj, str);
    }

    @NotNull
    public final ReturnData setData(@NotNull Object data, @NotNull String msg) {
        Intrinsics.checkNotNullParameter(data, "data");
        Intrinsics.checkNotNullParameter(msg, "msg");
        this.isSuccess = true;
        this.errorMsg = msg;
        this.data = data;
        return this;
    }
}
