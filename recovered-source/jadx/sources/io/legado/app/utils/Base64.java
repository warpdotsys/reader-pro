package io.legado.app.utils;

import java.io.UnsupportedEncodingException;

/* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/Base64.class */
public class Base64 {
    public static final int DEFAULT = 0;
    public static final int NO_PADDING = 1;
    public static final int NO_WRAP = 2;
    public static final int CRLF = 4;
    public static final int URL_SAFE = 8;
    public static final int NO_CLOSE = 16;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Base64.class.desiredAssertionStatus();
    }

    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/Base64$Coder.class */
    static abstract class Coder {
        public byte[] output;
        public int op;

        public abstract boolean process(byte[] input, int offset, int len, boolean finish);

        public abstract int maxOutputSize(int len);

        Coder() {
        }
    }

    public static byte[] decode(String str, int flags) {
        return decode(str.getBytes(), flags);
    }

    public static byte[] decode(byte[] input, int flags) {
        return decode(input, 0, input.length, flags);
    }

    public static byte[] decode(byte[] input, int offset, int len, int flags) {
        Decoder decoder = new Decoder(flags, new byte[(len * 3) / 4]);
        if (!decoder.process(input, offset, len, true)) {
            throw new IllegalArgumentException("bad base-64");
        }
        if (decoder.op == decoder.output.length) {
            return decoder.output;
        }
        byte[] temp = new byte[decoder.op];
        System.arraycopy(decoder.output, 0, temp, 0, decoder.op);
        return temp;
    }

    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/Base64$Decoder.class */
    static class Decoder extends Coder {
        private static final int SKIP = -1;
        private int state;
        private int value;
        private final int[] alphabet;
        private static final int EQUALS = -2;
        private static final int[] DECODE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, EQUALS, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int[] DECODE_WEBSAFE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, EQUALS, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

        public Decoder(int flags, byte[] output) {
            this.output = output;
            this.alphabet = (flags & 8) == 0 ? DECODE : DECODE_WEBSAFE;
            this.state = 0;
            this.value = 0;
        }

        @Override // io.legado.app.utils.Base64.Coder
        public int maxOutputSize(int len) {
            return ((len * 3) / 4) + 10;
        }

        /* JADX WARN: Removed duplicated region for block: B:69:0x020d  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x0221  */
        /* JADX WARN: Removed duplicated region for block: B:83:0x0208 A[SYNTHETIC] */
        @Override // io.legado.app.utils.Base64.Coder
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean process(byte[] input, int offset, int len, boolean finish) {
            if (this.state == 6) {
                return false;
            }
            int p = offset;
            int len2 = len + offset;
            int state = this.state;
            int value = this.value;
            int op = 0;
            byte[] output = this.output;
            int[] alphabet = this.alphabet;
            while (p < len2) {
                if (state == 0) {
                    while (p + 4 <= len2) {
                        int i = (alphabet[input[p] & 255] << 18) | (alphabet[input[p + 1] & 255] << 12) | (alphabet[input[p + 2] & 255] << 6) | alphabet[input[p + 3] & 255];
                        value = i;
                        if (i >= 0) {
                            output[op + 2] = (byte) value;
                            output[op + 1] = (byte) (value >> 8);
                            output[op] = (byte) (value >> 16);
                            op += 3;
                            p += 4;
                        } else if (p >= len2) {
                            if (finish) {
                                this.state = state;
                                this.value = value;
                                this.op = op;
                                return true;
                            }
                            switch (state) {
                                case 1:
                                    this.state = 6;
                                    return false;
                                case 2:
                                    int i2 = op;
                                    op++;
                                    output[i2] = (byte) (value >> 4);
                                    break;
                                case 3:
                                    int i3 = op;
                                    int op2 = op + 1;
                                    output[i3] = (byte) (value >> 10);
                                    op = op2 + 1;
                                    output[op2] = (byte) (value >> 2);
                                    break;
                                case 4:
                                    this.state = 6;
                                    return false;
                            }
                            this.state = state;
                            this.op = op;
                            return true;
                        }
                    }
                    if (p >= len2) {
                    }
                }
                int i4 = p;
                p++;
                int d = alphabet[input[i4] & 255];
                switch (state) {
                    case 0:
                        if (d >= 0) {
                            value = d;
                            state++;
                        } else {
                            if (d != -1) {
                                this.state = 6;
                                return false;
                            }
                        }
                        break;
                    case 1:
                        if (d >= 0) {
                            value = (value << 6) | d;
                            state++;
                        } else {
                            if (d != -1) {
                                this.state = 6;
                                return false;
                            }
                        }
                        break;
                    case 2:
                        if (d >= 0) {
                            value = (value << 6) | d;
                            state++;
                        } else if (d == EQUALS) {
                            int i5 = op;
                            op++;
                            output[i5] = (byte) (value >> 4);
                            state = 4;
                        } else {
                            if (d != -1) {
                                this.state = 6;
                                return false;
                            }
                        }
                        break;
                    case 3:
                        if (d >= 0) {
                            value = (value << 6) | d;
                            output[op + 2] = (byte) value;
                            output[op + 1] = (byte) (value >> 8);
                            output[op] = (byte) (value >> 16);
                            op += 3;
                            state = 0;
                        } else if (d == EQUALS) {
                            output[op + 1] = (byte) (value >> 2);
                            output[op] = (byte) (value >> 10);
                            op += 2;
                            state = 5;
                        } else {
                            if (d != -1) {
                                this.state = 6;
                                return false;
                            }
                        }
                        break;
                    case 4:
                        if (d == EQUALS) {
                            state++;
                        } else {
                            if (d != -1) {
                                this.state = 6;
                                return false;
                            }
                        }
                        break;
                    case 5:
                        if (d != -1) {
                            this.state = 6;
                            return false;
                        }
                        break;
                }
            }
            if (finish) {
            }
        }
    }

    public static String encodeToString(byte[] input, int flags) {
        try {
            return new String(encode(input, flags), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static String encodeToString(byte[] input, int offset, int len, int flags) {
        try {
            return new String(encode(input, offset, len, flags), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static byte[] encode(byte[] input, int flags) {
        return encode(input, 0, input.length, flags);
    }

    public static byte[] encode(byte[] input, int offset, int len, int flags) {
        Encoder encoder = new Encoder(flags, null);
        int output_len = (len / 3) * 4;
        if (encoder.do_padding) {
            if (len % 3 > 0) {
                output_len += 4;
            }
        } else {
            switch (len % 3) {
                case 1:
                    output_len += 2;
                    break;
                case 2:
                    output_len += 3;
                    break;
            }
        }
        if (encoder.do_newline && len > 0) {
            output_len += (((len - 1) / 57) + 1) * (encoder.do_cr ? 2 : 1);
        }
        encoder.output = new byte[output_len];
        encoder.process(input, offset, len, true);
        if ($assertionsDisabled || encoder.op == output_len) {
            return encoder.output;
        }
        throw new AssertionError();
    }

    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/Base64$Encoder.class */
    static class Encoder extends Coder {
        public static final int LINE_GROUPS = 19;
        private static final byte[] ENCODE;
        private static final byte[] ENCODE_WEBSAFE;
        private final byte[] tail;
        int tailLen;
        private int count;
        public final boolean do_padding;
        public final boolean do_newline;
        public final boolean do_cr;
        private final byte[] alphabet;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Base64.class.desiredAssertionStatus();
            ENCODE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
            ENCODE_WEBSAFE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
        }

        public Encoder(int flags, byte[] output) {
            this.output = output;
            this.do_padding = (flags & 1) == 0;
            this.do_newline = (flags & 2) == 0;
            this.do_cr = (flags & 4) != 0;
            this.alphabet = (flags & 8) == 0 ? ENCODE : ENCODE_WEBSAFE;
            this.tail = new byte[2];
            this.tailLen = 0;
            this.count = this.do_newline ? 19 : -1;
        }

        @Override // io.legado.app.utils.Base64.Coder
        public int maxOutputSize(int len) {
            return ((len * 8) / 5) + 10;
        }

        @Override // io.legado.app.utils.Base64.Coder
        public boolean process(byte[] input, int offset, int len, boolean finish) {
            byte b;
            byte b2;
            byte b3;
            byte[] alphabet = this.alphabet;
            byte[] output = this.output;
            int op = 0;
            int count = this.count;
            int p = offset;
            int len2 = len + offset;
            int v = -1;
            switch (this.tailLen) {
                case 1:
                    if (p + 2 <= len2) {
                        int p2 = p + 1;
                        int i = ((this.tail[0] & 255) << 16) | ((input[p] & 255) << 8);
                        p = p2 + 1;
                        v = i | (input[p2] & 255);
                        this.tailLen = 0;
                    }
                    break;
                case 2:
                    if (p + 1 <= len2) {
                        p++;
                        v = ((this.tail[0] & 255) << 16) | ((this.tail[1] & 255) << 8) | (input[p] & 255);
                        this.tailLen = 0;
                    }
                    break;
            }
            if (v != -1) {
                int op2 = 0 + 1;
                output[0] = alphabet[(v >> 18) & 63];
                int op3 = op2 + 1;
                output[op2] = alphabet[(v >> 12) & 63];
                int op4 = op3 + 1;
                output[op3] = alphabet[(v >> 6) & 63];
                op = op4 + 1;
                output[op4] = alphabet[v & 63];
                count--;
                if (count == 0) {
                    if (this.do_cr) {
                        op++;
                        output[op] = 13;
                    }
                    int i2 = op;
                    op++;
                    output[i2] = 10;
                    count = 19;
                }
            }
            while (p + 3 <= len2) {
                int v2 = ((input[p] & 255) << 16) | ((input[p + 1] & 255) << 8) | (input[p + 2] & 255);
                output[op] = alphabet[(v2 >> 18) & 63];
                output[op + 1] = alphabet[(v2 >> 12) & 63];
                output[op + 2] = alphabet[(v2 >> 6) & 63];
                output[op + 3] = alphabet[v2 & 63];
                p += 3;
                op += 4;
                count--;
                if (count == 0) {
                    if (this.do_cr) {
                        op++;
                        output[op] = 13;
                    }
                    int i3 = op;
                    op++;
                    output[i3] = 10;
                    count = 19;
                }
            }
            if (finish) {
                if (p - this.tailLen == len2 - 1) {
                    int t = 0;
                    if (this.tailLen > 0) {
                        t = 0 + 1;
                        b3 = this.tail[0];
                    } else {
                        int i4 = p;
                        p++;
                        b3 = input[i4];
                    }
                    int v3 = (b3 & 255) << 4;
                    this.tailLen -= t;
                    int i5 = op;
                    int op5 = op + 1;
                    output[i5] = alphabet[(v3 >> 6) & 63];
                    op = op5 + 1;
                    output[op5] = alphabet[v3 & 63];
                    if (this.do_padding) {
                        int op6 = op + 1;
                        output[op] = 61;
                        op = op6 + 1;
                        output[op6] = 61;
                    }
                    if (this.do_newline) {
                        if (this.do_cr) {
                            int i6 = op;
                            op++;
                            output[i6] = 13;
                        }
                        int i7 = op;
                        op++;
                        output[i7] = 10;
                    }
                } else if (p - this.tailLen == len2 - 2) {
                    int t2 = 0;
                    if (this.tailLen > 1) {
                        t2 = 0 + 1;
                        b = this.tail[0];
                    } else {
                        int i8 = p;
                        p++;
                        b = input[i8];
                    }
                    int i9 = (b & 255) << 10;
                    if (this.tailLen > 0) {
                        int i10 = t2;
                        t2++;
                        b2 = this.tail[i10];
                    } else {
                        int i11 = p;
                        p++;
                        b2 = input[i11];
                    }
                    int v4 = i9 | ((b2 & 255) << 2);
                    this.tailLen -= t2;
                    int i12 = op;
                    int op7 = op + 1;
                    output[i12] = alphabet[(v4 >> 12) & 63];
                    int op8 = op7 + 1;
                    output[op7] = alphabet[(v4 >> 6) & 63];
                    op = op8 + 1;
                    output[op8] = alphabet[v4 & 63];
                    if (this.do_padding) {
                        op++;
                        output[op] = 61;
                    }
                    if (this.do_newline) {
                        if (this.do_cr) {
                            int i13 = op;
                            op++;
                            output[i13] = 13;
                        }
                        int i14 = op;
                        op++;
                        output[i14] = 10;
                    }
                } else if (this.do_newline && op > 0 && count != 19) {
                    if (this.do_cr) {
                        int i15 = op;
                        op++;
                        output[i15] = 13;
                    }
                    int i16 = op;
                    op++;
                    output[i16] = 10;
                }
                if (!$assertionsDisabled && this.tailLen != 0) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && p != len2) {
                    throw new AssertionError();
                }
            } else if (p == len2 - 1) {
                byte[] bArr = this.tail;
                int i17 = this.tailLen;
                this.tailLen = i17 + 1;
                bArr[i17] = input[p];
            } else if (p == len2 - 2) {
                byte[] bArr2 = this.tail;
                int i18 = this.tailLen;
                this.tailLen = i18 + 1;
                bArr2[i18] = input[p];
                byte[] bArr3 = this.tail;
                int i19 = this.tailLen;
                this.tailLen = i19 + 1;
                bArr3[i19] = input[p + 1];
            }
            this.op = op;
            this.count = count;
            return true;
        }
    }

    private Base64() {
    }
}
