//
// Decompiled by Procyon v0.6.0
//

package me.ag2s.umdlib.domain;

import java.io.IOException;
import me.ag2s.umdlib.tool.WrapOutputStream;

public class UmdEnd
{
    public void buildEnd(final WrapOutputStream wos) throws IOException {
        wos.writeBytes(35, 12, 0, 1, 9);
        wos.writeInt(wos.getWritten() + 4);
    }
}
