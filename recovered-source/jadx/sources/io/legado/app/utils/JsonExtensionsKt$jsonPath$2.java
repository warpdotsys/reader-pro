package io.legado.app.utils;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* JADX INFO: compiled from: JsonExtensions.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/JsonExtensionsKt$jsonPath$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001H\n"}, d2 = {"<anonymous>", "Lcom/jayway/jsonpath/ParseContext;", "kotlin.jvm.PlatformType"})
final class JsonExtensionsKt$jsonPath$2 extends Lambda implements Function0<ParseContext> {
    public static final JsonExtensionsKt$jsonPath$2 INSTANCE = new JsonExtensionsKt$jsonPath$2();

    JsonExtensionsKt$jsonPath$2() {
        super(0);
    }

    /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
    public final ParseContext m295invoke() {
        return JsonPath.using(Configuration.builder().options(new Option[]{Option.SUPPRESS_EXCEPTIONS}).build());
    }
}
