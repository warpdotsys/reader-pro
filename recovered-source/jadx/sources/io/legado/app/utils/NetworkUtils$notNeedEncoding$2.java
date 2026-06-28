package io.legado.app.utils;

import java.util.BitSet;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: NetworkUtils.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/NetworkUtils$notNeedEncoding$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Ljava/util/BitSet;"})
final class NetworkUtils$notNeedEncoding$2 extends Lambda implements Function0<BitSet> {
    public static final NetworkUtils$notNeedEncoding$2 INSTANCE = new NetworkUtils$notNeedEncoding$2();

    NetworkUtils$notNeedEncoding$2() {
        super(0);
    }

    @NotNull
    /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
    public final BitSet m300invoke() {
        int i;
        int i2;
        int i3;
        BitSet bitSet = new BitSet(256);
        int i4 = 97;
        do {
            i = i4;
            i4++;
            bitSet.set(i);
        } while (i != 122);
        int i5 = 65;
        do {
            i2 = i5;
            i5++;
            bitSet.set(i2);
        } while (i2 != 90);
        int i6 = 48;
        do {
            i3 = i6;
            i6++;
            bitSet.set(i3);
        } while (i3 != 57);
        int i7 = 0;
        int length = "+-_.$:()!*@&#,[]".length();
        while (i7 < length) {
            char cCharAt = "+-_.$:()!*@&#,[]".charAt(i7);
            i7++;
            bitSet.set(cCharAt);
        }
        return bitSet;
    }
}
