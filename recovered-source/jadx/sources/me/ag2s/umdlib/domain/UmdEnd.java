package me.ag2s.umdlib.domain;

import java.io.IOException;
import me.ag2s.umdlib.tool.WrapOutputStream;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/umdlib/domain/UmdEnd.class */
public class UmdEnd {
    public void buildEnd(WrapOutputStream wos) throws IOException {
        wos.writeBytes(35, 12, 0, 1, 9);
        wos.writeInt(wos.getWritten() + 4);
    }
}
