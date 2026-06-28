package io.legado.app.model.analyzeRule;

import io.legado.app.help.http.OkHttpUtilsKt;
import io.legado.app.help.http.RequestMethod;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: AnalyzeUrl.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/AnalyzeUrl$getResponseAwait$response$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lokhttp3/Request$Builder;"})
final class AnalyzeUrl$getResponseAwait$response$1 extends Lambda implements Function1<Request.Builder, Unit> {
    final /* synthetic */ AnalyzeUrl this$0;

    /* JADX INFO: compiled from: AnalyzeUrl.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/AnalyzeUrl$getResponseAwait$response$1$WhenMappings.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[RequestMethod.values().length];
            iArr[RequestMethod.POST.ordinal()] = 1;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    AnalyzeUrl$getResponseAwait$response$1(AnalyzeUrl this$0) {
        super(1);
        this.this$0 = this$0;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
        invoke((Request.Builder) p1);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull Request.Builder $this$newCallResponse) {
        Intrinsics.checkNotNullParameter($this$newCallResponse, "$this$newCallResponse");
        OkHttpUtilsKt.addHeaders($this$newCallResponse, this.this$0.getHeaderMap());
        if (WhenMappings.$EnumSwitchMapping$0[this.this$0.method.ordinal()] == 1) {
            $this$newCallResponse.url(this.this$0.urlNoQuery);
            String contentType = this.this$0.getHeaderMap().get(NCXDocumentV3.XHTMLAttributeValues.Content_Type);
            String body = this.this$0.getBody();
            if (!(!this.this$0.fieldMap.isEmpty())) {
                String str = body;
                if (!(str == null || StringsKt.isBlank(str))) {
                    String str2 = contentType;
                    if (!(str2 == null || StringsKt.isBlank(str2))) {
                        RequestBody requestBody = RequestBody.Companion.create(body, MediaType.Companion.get(contentType));
                        $this$newCallResponse.post(requestBody);
                        return;
                    } else {
                        OkHttpUtilsKt.postJson($this$newCallResponse, body);
                        return;
                    }
                }
            }
            OkHttpUtilsKt.postForm($this$newCallResponse, this.this$0.fieldMap, true);
            return;
        }
        OkHttpUtilsKt.get($this$newCallResponse, this.this$0.urlNoQuery, this.this$0.fieldMap, true);
    }
}
