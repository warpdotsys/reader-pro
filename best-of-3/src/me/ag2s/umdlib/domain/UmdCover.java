//
// Decompiled by Procyon v0.6.0
//

package me.ag2s.umdlib.domain;

import me.ag2s.umdlib.tool.WrapOutputStream;
import java.io.IOException;
import me.ag2s.umdlib.tool.UmdUtils;
import java.io.File;

public class UmdCover
{
    private static int DEFAULT_COVER_WIDTH;
    private static int DEFAULT_COVER_HEIGHT;
    private byte[] coverData;

    public UmdCover() {
    }

    public UmdCover(final byte[] coverData) {
        this.coverData = coverData;
    }

    public void load(final File f) throws IOException {
        this.coverData = UmdUtils.readFile(f);
    }

    public void load(final String fileName) throws IOException {
        this.load(new File(fileName));
    }

    public void initDefaultCover(final String title) throws IOException {
    }

    public void buildCover(final WrapOutputStream wos) throws IOException {
        if (this.coverData == null || this.coverData.length == 0) {
            return;
        }
        wos.writeBytes(35, 130, 0, 1, 10, 1);
        final byte[] rb = UmdUtils.genRandomBytes(4);
        wos.writeBytes(rb);
        wos.write(36);
        wos.writeBytes(rb);
        wos.writeInt(this.coverData.length + 9);
        wos.write(this.coverData);
    }

    public byte[] getCoverData() {
        return this.coverData;
    }

    public void setCoverData(final byte[] coverData) {
        this.coverData = coverData;
    }

    static {
        UmdCover.DEFAULT_COVER_WIDTH = 120;
        UmdCover.DEFAULT_COVER_HEIGHT = 160;
    }
}
