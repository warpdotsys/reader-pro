package io.legado.app.help;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

class BytesEncodingDetect extends Encoding {
   int[][] GBFreq;
   int[][] GBKFreq;
   int[][] Big5Freq;
   int[][] Big5PFreq;
   int[][] EUC_TWFreq;
   int[][] KRFreq;
   int[][] JPFreq;
   public boolean debug = false;

   public BytesEncodingDetect() {
      this.GBFreq = new int[94][94];
      this.GBKFreq = new int[126][191];
      this.Big5Freq = new int[94][158];
      this.Big5PFreq = new int[126][191];
      this.EUC_TWFreq = new int[94][94];
      this.KRFreq = new int[94][94];
      this.JPFreq = new int[94][94];
      this.initialize_frequencies();
   }

   public int detectEncoding(URL testurl) {
      byte[] rawtext = new byte[10000];
      int bytesread = 0;
      int byteoffset = 0;
      int guess = OTHER;

      try {
         InputStream chinesestream = testurl.openStream();

         while ((bytesread = chinesestream.read(rawtext, byteoffset, rawtext.length - byteoffset)) > 0) {
            byteoffset += bytesread;
         }

         chinesestream.close();
         guess = this.detectEncoding(rawtext);
      } catch (Exception var8) {
         System.err.println("Error loading or using URL " + var8.toString());
         guess = -1;
      }

      return guess;
   }

   public int detectEncoding(File testfile) {
      byte[] rawtext = getFileBytes(testfile);
      return this.detectEncoding(rawtext);
   }

   public static byte[] getFileBytes(File testfile) {
      byte[] rawtext = new byte[2000];

      try {
         FileInputStream chinesefile = new FileInputStream(testfile);
         chinesefile.read(rawtext);
         chinesefile.close();
      } catch (Exception var4) {
         System.err.println("Error: " + var4);
      }

      return rawtext;
   }

   public int detectEncoding(byte[] rawtext) {
      int maxscore = 0;
      int encoding_guess = OTHER;
      int[] scores = new int[TOTALTYPES];
      scores[GB2312] = this.gb2312_probability(rawtext);
      scores[GBK] = this.gbk_probability(rawtext);
      scores[GB18030] = this.gb18030_probability(rawtext);
      scores[HZ] = this.hz_probability(rawtext);
      scores[BIG5] = this.big5_probability(rawtext);
      scores[CNS11643] = this.euc_tw_probability(rawtext);
      scores[ISO2022CN] = this.iso_2022_cn_probability(rawtext);
      scores[UTF8] = this.utf8_probability(rawtext);
      scores[UNICODE] = this.utf16_probability(rawtext);
      scores[EUC_KR] = this.euc_kr_probability(rawtext);
      scores[CP949] = this.cp949_probability(rawtext);
      scores[JOHAB] = 0;
      scores[ISO2022KR] = this.iso_2022_kr_probability(rawtext);
      scores[ASCII] = this.ascii_probability(rawtext);
      scores[SJIS] = this.sjis_probability(rawtext);
      scores[EUC_JP] = this.euc_jp_probability(rawtext);
      scores[ISO2022JP] = this.iso_2022_jp_probability(rawtext);
      scores[UNICODET] = 0;
      scores[UNICODES] = 0;
      scores[ISO2022CN_GB] = 0;
      scores[ISO2022CN_CNS] = 0;
      scores[OTHER] = 0;

      for (int index = 0; index < TOTALTYPES; index++) {
         if (this.debug) {
            System.err.println("Encoding " + nicename[index] + " score " + scores[index]);
         }

         if (scores[index] > maxscore) {
            encoding_guess = index;
            maxscore = scores[index];
         }
      }

      if (maxscore <= 50) {
         encoding_guess = OTHER;
      }

      return encoding_guess;
   }

   int gb2312_probability(byte[] rawtext) {
      int rawtextlen = 0;
      int dbchars = 1;
      int gbchars = 1;
      long gbfreq = 0L;
      long totalfreq = 1L;
      float rangeval = 0.0F;
      float freqval = 0.0F;
      rawtextlen = rawtext.length;

      for (int i = 0; i < rawtextlen - 1; i++) {
         if (rawtext[i] < 0) {
            dbchars++;
            if (-95 <= rawtext[i] && rawtext[i] <= -9 && -95 <= rawtext[i + 1] && rawtext[i + 1] <= -2) {
               gbchars++;
               totalfreq += 500L;
               int row = rawtext[i] + 256 - 161;
               int column = rawtext[i + 1] + 256 - 161;
               if (this.GBFreq[row][column] != 0) {
                  gbfreq += this.GBFreq[row][column];
               } else if (15 <= row && row < 55) {
                  gbfreq += 200L;
               }
            }

            i++;
         }
      }

      rangeval = 50.0F * ((float)gbchars / dbchars);
      freqval = 50.0F * ((float)gbfreq / (float)totalfreq);
      return (int)(rangeval + freqval);
   }

   int gbk_probability(byte[] rawtext) {
      int rawtextlen = 0;
      int dbchars = 1;
      int gbchars = 1;
      long gbfreq = 0L;
      long totalfreq = 1L;
      float rangeval = 0.0F;
      float freqval = 0.0F;
      rawtextlen = rawtext.length;

      for (int i = 0; i < rawtextlen - 1; i++) {
         if (rawtext[i] < 0) {
            dbchars++;
            if (-95 <= rawtext[i] && rawtext[i] <= -9 && -95 <= rawtext[i + 1] && rawtext[i + 1] <= -2) {
               gbchars++;
               totalfreq += 500L;
               int row = rawtext[i] + 256 - 161;
               int column = rawtext[i + 1] + 256 - 161;
               if (this.GBFreq[row][column] != 0) {
                  gbfreq += this.GBFreq[row][column];
               } else if (15 <= row && row < 55) {
                  gbfreq += 200L;
               }
            } else if (-127 <= rawtext[i]
               && rawtext[i] <= -2
               && (-128 <= rawtext[i + 1] && rawtext[i + 1] <= -2 || 64 <= rawtext[i + 1] && rawtext[i + 1] <= 126)) {
               gbchars++;
               totalfreq += 500L;
               int row = rawtext[i] + 256 - 129;
               int column;
               if (64 <= rawtext[i + 1] && rawtext[i + 1] <= 126) {
                  column = rawtext[i + 1] - 64;
               } else {
                  column = rawtext[i + 1] + 256 - 64;
               }

               if (this.GBKFreq[row][column] != 0) {
                  gbfreq += this.GBKFreq[row][column];
               }
            }

            i++;
         }
      }

      rangeval = 50.0F * ((float)gbchars / dbchars);
      freqval = 50.0F * ((float)gbfreq / (float)totalfreq);
      return (int)(rangeval + freqval) - 1;
   }

   int gb18030_probability(byte[] rawtext) {
      int rawtextlen = 0;
      int dbchars = 1;
      int gbchars = 1;
      long gbfreq = 0L;
      long totalfreq = 1L;
      float rangeval = 0.0F;
      float freqval = 0.0F;
      rawtextlen = rawtext.length;

      for (int i = 0; i < rawtextlen - 1; i++) {
         if (rawtext[i] < 0) {
            dbchars++;
            if (-95 <= rawtext[i] && rawtext[i] <= -9 && i + 1 < rawtextlen && -95 <= rawtext[i + 1] && rawtext[i + 1] <= -2) {
               gbchars++;
               totalfreq += 500L;
               int row = rawtext[i] + 256 - 161;
               int column = rawtext[i + 1] + 256 - 161;
               if (this.GBFreq[row][column] != 0) {
                  gbfreq += this.GBFreq[row][column];
               } else if (15 <= row && row < 55) {
                  gbfreq += 200L;
               }
            } else if (-127 <= rawtext[i]
               && rawtext[i] <= -2
               && i + 1 < rawtextlen
               && (-128 <= rawtext[i + 1] && rawtext[i + 1] <= -2 || 64 <= rawtext[i + 1] && rawtext[i + 1] <= 126)) {
               gbchars++;
               totalfreq += 500L;
               int row = rawtext[i] + 256 - 129;
               int column;
               if (64 <= rawtext[i + 1] && rawtext[i + 1] <= 126) {
                  column = rawtext[i + 1] - 64;
               } else {
                  column = rawtext[i + 1] + 256 - 64;
               }

               if (this.GBKFreq[row][column] != 0) {
                  gbfreq += this.GBKFreq[row][column];
               }
            } else if (-127 <= rawtext[i]
               && rawtext[i] <= -2
               && i + 3 < rawtextlen
               && 48 <= rawtext[i + 1]
               && rawtext[i + 1] <= 57
               && -127 <= rawtext[i + 2]
               && rawtext[i + 2] <= -2
               && 48 <= rawtext[i + 3]
               && rawtext[i + 3] <= 57) {
               gbchars++;
            }

            i++;
         }
      }

      rangeval = 50.0F * ((float)gbchars / dbchars);
      freqval = 50.0F * ((float)gbfreq / (float)totalfreq);
      return (int)(rangeval + freqval) - 1;
   }

   int hz_probability(byte[] rawtext) {
      int hzchars = 0;
      int dbchars = 1;
      long hzfreq = 0L;
      long totalfreq = 1L;
      float rangeval = 0.0F;
      float freqval = 0.0F;
      int hzstart = 0;
      int hzend = 0;
      int rawtextlen = rawtext.length;

      for (int i = 0; i < rawtextlen; i++) {
         if (rawtext[i] == 126) {
            if (rawtext[i + 1] != 123) {
               if (rawtext[i + 1] == 125) {
                  hzend++;
                  i++;
               } else if (rawtext[i + 1] == 126) {
                  i++;
               }
            } else {
               hzstart++;

               for (i += 2; i < rawtextlen - 1 && rawtext[i] != 10 && rawtext[i] != 13; i += 2) {
                  if (rawtext[i] == 126 && rawtext[i + 1] == 125) {
                     hzend++;
                     i++;
                     break;
                  }

                  if (33 <= rawtext[i] && rawtext[i] <= 119 && 33 <= rawtext[i + 1] && rawtext[i + 1] <= 119) {
                     hzchars += 2;
                     int row = rawtext[i] - 33;
                     int column = rawtext[i + 1] - 33;
                     totalfreq += 500L;
                     if (this.GBFreq[row][column] != 0) {
                        hzfreq += this.GBFreq[row][column];
                     } else if (15 <= row && row < 55) {
                        hzfreq += 200L;
                     }
                  } else if (161 <= rawtext[i] && rawtext[i] <= 247 && 161 <= rawtext[i + 1] && rawtext[i + 1] <= 247) {
                     hzchars += 2;
                     int row = rawtext[i] + 256 - 161;
                     int column = rawtext[i + 1] + 256 - 161;
                     totalfreq += 500L;
                     if (this.GBFreq[row][column] != 0) {
                        hzfreq += this.GBFreq[row][column];
                     } else if (15 <= row && row < 55) {
                        hzfreq += 200L;
                     }
                  }

                  dbchars += 2;
               }
            }
         }
      }

      if (hzstart > 4) {
         rangeval = 50.0F;
      } else if (hzstart > 1) {
         rangeval = 41.0F;
      } else if (hzstart > 0) {
         rangeval = 39.0F;
      } else {
         rangeval = 0.0F;
      }

      freqval = 50.0F * ((float)hzfreq / (float)totalfreq);
      return (int)(rangeval + freqval);
   }

   int big5_probability(byte[] rawtext) {
      int rawtextlen = 0;
      int dbchars = 1;
      int bfchars = 1;
      float rangeval = 0.0F;
      float freqval = 0.0F;
      long bffreq = 0L;
      long totalfreq = 1L;
      rawtextlen = rawtext.length;

      for (int i = 0; i < rawtextlen - 1; i++) {
         if (rawtext[i] < 0) {
            dbchars++;
            if (-95 <= rawtext[i] && rawtext[i] <= -7 && (64 <= rawtext[i + 1] && rawtext[i + 1] <= 126 || -95 <= rawtext[i + 1] && rawtext[i + 1] <= -2)) {
               bfchars++;
               totalfreq += 500L;
               int row = rawtext[i] + 256 - 161;
               int column;
               if (64 <= rawtext[i + 1] && rawtext[i + 1] <= 126) {
                  column = rawtext[i + 1] - 64;
               } else {
                  column = rawtext[i + 1] + 256 - 97;
               }

               if (this.Big5Freq[row][column] != 0) {
                  bffreq += this.Big5Freq[row][column];
               } else if (3 <= row && row <= 37) {
                  bffreq += 200L;
               }
            }

            i++;
         }
      }

      rangeval = 50.0F * ((float)bfchars / dbchars);
      freqval = 50.0F * ((float)bffreq / (float)totalfreq);
      return (int)(rangeval + freqval);
   }

   int big5plus_probability(byte[] rawtext) {
      int rawtextlen = 0;
      int dbchars = 1;
      int bfchars = 1;
      long bffreq = 0L;
      long totalfreq = 1L;
      float rangeval = 0.0F;
      float freqval = 0.0F;
      rawtextlen = rawtext.length;

      for (int i = 0; i < rawtextlen - 1; i++) {
         if (rawtext[i] < 128) {
            dbchars++;
            if (161 > rawtext[i] || rawtext[i] > 249 || (64 > rawtext[i + 1] || rawtext[i + 1] > 126) && (161 > rawtext[i + 1] || rawtext[i + 1] > 254)) {
               if (129 <= rawtext[i] && rawtext[i] <= 254 && (64 <= rawtext[i + 1] && rawtext[i + 1] <= 126 || 128 <= rawtext[i + 1] && rawtext[i + 1] <= 254)) {
                  bfchars++;
                  totalfreq += 500L;
                  int row = rawtext[i] - 129;
                  int column;
                  if (64 <= rawtext[i + 1] && rawtext[i + 1] <= 126) {
                     column = rawtext[i + 1] - 64;
                  } else {
                     column = rawtext[i + 1] - 64;
                  }

                  if (this.Big5PFreq[row][column] != 0) {
                     bffreq += this.Big5PFreq[row][column];
                  }
               }
            } else {
               bfchars++;
               totalfreq += 500L;
               int rowx = rawtext[i] - 161;
               int columnx;
               if (64 <= rawtext[i + 1] && rawtext[i + 1] <= 126) {
                  columnx = rawtext[i + 1] - 64;
               } else {
                  columnx = rawtext[i + 1] - 97;
               }

               if (this.Big5Freq[rowx][columnx] != 0) {
                  bffreq += this.Big5Freq[rowx][columnx];
               } else if (3 <= rowx && rowx < 37) {
                  bffreq += 200L;
               }
            }

            i++;
         }
      }

      rangeval = 50.0F * ((float)bfchars / dbchars);
      freqval = 50.0F * ((float)bffreq / (float)totalfreq);
      return (int)(rangeval + freqval) - 1;
   }

   int euc_tw_probability(byte[] rawtext) {
      int rawtextlen = 0;
      int dbchars = 1;
      int cnschars = 1;
      long cnsfreq = 0L;
      long totalfreq = 1L;
      float rangeval = 0.0F;
      float freqval = 0.0F;
      rawtextlen = rawtext.length;

      for (int i = 0; i < rawtextlen - 1; i++) {
         if (rawtext[i] < 0) {
            dbchars++;
            if (i + 3 < rawtextlen
               && -114 == rawtext[i]
               && -95 <= rawtext[i + 1]
               && rawtext[i + 1] <= -80
               && -95 <= rawtext[i + 2]
               && rawtext[i + 2] <= -2
               && -95 <= rawtext[i + 3]
               && rawtext[i + 3] <= -2) {
               cnschars++;
               i += 3;
            } else if (-95 <= rawtext[i] && rawtext[i] <= -2 && -95 <= rawtext[i + 1] && rawtext[i + 1] <= -2) {
               cnschars++;
               totalfreq += 500L;
               int row = rawtext[i] + 256 - 161;
               int column = rawtext[i + 1] + 256 - 161;
               if (this.EUC_TWFreq[row][column] != 0) {
                  cnsfreq += this.EUC_TWFreq[row][column];
               } else if (35 <= row && row <= 92) {
                  cnsfreq += 150L;
               }

               i++;
            }
         }
      }

      rangeval = 50.0F * ((float)cnschars / dbchars);
      freqval = 50.0F * ((float)cnsfreq / (float)totalfreq);
      return (int)(rangeval + freqval);
   }

   int iso_2022_cn_probability(byte[] rawtext) {
      int rawtextlen = 0;
      int dbchars = 1;
      int isochars = 1;
      long isofreq = 0L;
      long totalfreq = 1L;
      float rangeval = 0.0F;
      float freqval = 0.0F;
      rawtextlen = rawtext.length;

      for (int i = 0; i < rawtextlen - 1; i++) {
         if (rawtext[i] == 27 && i + 3 < rawtextlen) {
            if (rawtext[i + 1] == 36 && rawtext[i + 2] == 41 && rawtext[i + 3] == 65) {
               for (i += 4; rawtext[i] != 27; i++) {
                  dbchars++;
                  if (33 <= rawtext[i] && rawtext[i] <= 119 && 33 <= rawtext[i + 1] && rawtext[i + 1] <= 119) {
                     isochars++;
                     int row = rawtext[i] - 33;
                     int column = rawtext[i + 1] - 33;
                     totalfreq += 500L;
                     if (this.GBFreq[row][column] != 0) {
                        isofreq += this.GBFreq[row][column];
                     } else if (15 <= row && row < 55) {
                        isofreq += 200L;
                     }

                     i++;
                  }
               }
            } else if (i + 3 < rawtextlen && rawtext[i + 1] == 36 && rawtext[i + 2] == 41 && rawtext[i + 3] == 71) {
               for (i += 4; rawtext[i] != 27; i++) {
                  dbchars++;
                  if (33 <= rawtext[i] && rawtext[i] <= 126 && 33 <= rawtext[i + 1] && rawtext[i + 1] <= 126) {
                     isochars++;
                     totalfreq += 500L;
                     int row = rawtext[i] - 33;
                     int column = rawtext[i + 1] - 33;
                     if (this.EUC_TWFreq[row][column] != 0) {
                        isofreq += this.EUC_TWFreq[row][column];
                     } else if (35 <= row && row <= 92) {
                        isofreq += 150L;
                     }

                     i++;
                  }
               }
            }

            if (rawtext[i] == 27 && i + 2 < rawtextlen && rawtext[i + 1] == 40 && rawtext[i + 2] == 66) {
               i += 2;
            }
         }
      }

      rangeval = 50.0F * ((float)isochars / dbchars);
      freqval = 50.0F * ((float)isofreq / (float)totalfreq);
      return (int)(rangeval + freqval);
   }

   int utf8_probability(byte[] rawtext) {
      int score = 0;
      int rawtextlen = 0;
      int goodbytes = 0;
      int asciibytes = 0;
      rawtextlen = rawtext.length;

      for (int i = 0; i < rawtextlen; i++) {
         if ((rawtext[i] & 127) == rawtext[i]) {
            asciibytes++;
         } else if (-64 <= rawtext[i] && rawtext[i] <= -33 && i + 1 < rawtextlen && -128 <= rawtext[i + 1] && rawtext[i + 1] <= -65) {
            goodbytes += 2;
            i++;
         } else if (-32 <= rawtext[i]
            && rawtext[i] <= -17
            && i + 2 < rawtextlen
            && -128 <= rawtext[i + 1]
            && rawtext[i + 1] <= -65
            && -128 <= rawtext[i + 2]
            && rawtext[i + 2] <= -65) {
            goodbytes += 3;
            i += 2;
         }
      }

      if (asciibytes == rawtextlen) {
         return 0;
      } else {
         score = (int)(100.0F * ((float)goodbytes / (rawtextlen - asciibytes)));
         if (score > 98) {
            return score;
         } else {
            return score > 95 && goodbytes > 30 ? score : 0;
         }
      }
   }

   int utf16_probability(byte[] rawtext) {
      return (rawtext.length <= 1 || -2 != rawtext[0] || -1 != rawtext[1]) && (-1 != rawtext[0] || -2 != rawtext[1]) ? 0 : 100;
   }

   int ascii_probability(byte[] rawtext) {
      int score = 75;
      int rawtextlen = rawtext.length;

      for (int i = 0; i < rawtextlen; i++) {
         if (rawtext[i] < 0) {
            score -= 5;
         } else if (rawtext[i] == 27) {
            score -= 5;
         }

         if (score <= 0) {
            return 0;
         }
      }

      return score;
   }

   int euc_kr_probability(byte[] rawtext) {
      int rawtextlen = 0;
      int dbchars = 1;
      int krchars = 1;
      long krfreq = 0L;
      long totalfreq = 1L;
      float rangeval = 0.0F;
      float freqval = 0.0F;
      rawtextlen = rawtext.length;

      for (int i = 0; i < rawtextlen - 1; i++) {
         if (rawtext[i] < 0) {
            dbchars++;
            if (-95 <= rawtext[i] && rawtext[i] <= -2 && -95 <= rawtext[i + 1] && rawtext[i + 1] <= -2) {
               krchars++;
               totalfreq += 500L;
               int row = rawtext[i] + 256 - 161;
               int column = rawtext[i + 1] + 256 - 161;
               if (this.KRFreq[row][column] != 0) {
                  krfreq += this.KRFreq[row][column];
               } else if (15 <= row && row < 55) {
                  krfreq += 0L;
               }
            }

            i++;
         }
      }

      rangeval = 50.0F * ((float)krchars / dbchars);
      freqval = 50.0F * ((float)krfreq / (float)totalfreq);
      return (int)(rangeval + freqval);
   }

   int cp949_probability(byte[] rawtext) {
      int rawtextlen = 0;
      int dbchars = 1;
      int krchars = 1;
      long krfreq = 0L;
      long totalfreq = 1L;
      float rangeval = 0.0F;
      float freqval = 0.0F;
      rawtextlen = rawtext.length;

      for (int i = 0; i < rawtextlen - 1; i++) {
         if (rawtext[i] < 0) {
            dbchars++;
            if (-127 <= rawtext[i]
               && rawtext[i] <= -2
               && (
                  65 <= rawtext[i + 1] && rawtext[i + 1] <= 90
                     || 97 <= rawtext[i + 1] && rawtext[i + 1] <= 122
                     || -127 <= rawtext[i + 1] && rawtext[i + 1] <= -2
               )) {
               krchars++;
               totalfreq += 500L;
               if (-95 <= rawtext[i] && rawtext[i] <= -2 && -95 <= rawtext[i + 1] && rawtext[i + 1] <= -2) {
                  int row = rawtext[i] + 256 - 161;
                  int column = rawtext[i + 1] + 256 - 161;
                  if (this.KRFreq[row][column] != 0) {
                     krfreq += this.KRFreq[row][column];
                  }
               }
            }

            i++;
         }
      }

      rangeval = 50.0F * ((float)krchars / dbchars);
      freqval = 50.0F * ((float)krfreq / (float)totalfreq);
      return (int)(rangeval + freqval);
   }

   int iso_2022_kr_probability(byte[] rawtext) {
      for (int i = 0; i < rawtext.length; i++) {
         if (i + 3 < rawtext.length && rawtext[i] == 27 && (char)rawtext[i + 1] == '$' && (char)rawtext[i + 2] == ')' && (char)rawtext[i + 3] == 'C') {
            return 100;
         }
      }

      return 0;
   }

   int euc_jp_probability(byte[] rawtext) {
      int rawtextlen = 0;
      int dbchars = 1;
      int jpchars = 1;
      long jpfreq = 0L;
      long totalfreq = 1L;
      float rangeval = 0.0F;
      float freqval = 0.0F;
      rawtextlen = rawtext.length;

      for (int i = 0; i < rawtextlen - 1; i++) {
         if (rawtext[i] < 0) {
            dbchars++;
            if (-95 <= rawtext[i] && rawtext[i] <= -2 && -95 <= rawtext[i + 1] && rawtext[i + 1] <= -2) {
               jpchars++;
               totalfreq += 500L;
               int row = rawtext[i] + 256 - 161;
               int column = rawtext[i + 1] + 256 - 161;
               if (this.JPFreq[row][column] != 0) {
                  jpfreq += this.JPFreq[row][column];
               } else if (15 <= row && row < 55) {
                  jpfreq += 0L;
               }
            }

            i++;
         }
      }

      rangeval = 50.0F * ((float)jpchars / dbchars);
      freqval = 50.0F * ((float)jpfreq / (float)totalfreq);
      return (int)(rangeval + freqval);
   }

   int iso_2022_jp_probability(byte[] rawtext) {
      for (int i = 0; i < rawtext.length; i++) {
         if (i + 2 < rawtext.length && rawtext[i] == 27 && (char)rawtext[i + 1] == '$' && (char)rawtext[i + 2] == 'B') {
            return 100;
         }
      }

      return 0;
   }

   int sjis_probability(byte[] rawtext) {
      int rawtextlen = 0;
      int dbchars = 1;
      int jpchars = 1;
      long jpfreq = 0L;
      long totalfreq = 1L;
      float rangeval = 0.0F;
      float freqval = 0.0F;
      rawtextlen = rawtext.length;

      for (int i = 0; i < rawtextlen - 1; i++) {
         if (rawtext[i] < 0) {
            dbchars++;
            if (i + 1 < rawtext.length
               && (-127 <= rawtext[i] && rawtext[i] <= -97 || -32 <= rawtext[i] && rawtext[i] <= -17)
               && (64 <= rawtext[i + 1] && rawtext[i + 1] <= 126 || -128 <= rawtext[i + 1] && rawtext[i + 1] <= -4)) {
               jpchars++;
               totalfreq += 500L;
               int row = rawtext[i] + 256;
               int column = rawtext[i + 1] + 256;
               int adjust;
               if (column < 159) {
                  adjust = 1;
                  if (column > 127) {
                     column -= 32;
                  } else {
                     column -= 25;
                  }
               } else {
                  adjust = 0;
                  column -= 126;
               }

               if (row < 160) {
                  row = (row - 112 << 1) - adjust;
               } else {
                  row = (row - 176 << 1) - adjust;
               }

               row -= 32;
               int var23 = 32;
               if (row < this.JPFreq.length && var23 < this.JPFreq[row].length && this.JPFreq[row][var23] != 0) {
                  jpfreq += this.JPFreq[row][var23];
               }

               i++;
            } else if (-95 <= rawtext[i] && rawtext[i] <= -33) {
            }
         }
      }

      rangeval = 50.0F * ((float)jpchars / dbchars);
      freqval = 50.0F * ((float)jpfreq / (float)totalfreq);
      return (int)(rangeval + freqval) - 1;
   }

   void initialize_frequencies() {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.OutOfMemoryError: Java heap space
      //   at org.jetbrains.java.decompiler.util.collections.FastSparseSetFactory$FastSparseSet.getCopy(FastSparseSetFactory.java:95)
      //   at org.jetbrains.java.decompiler.util.collections.SFormsFastMapDirect.getCopy(SFormsFastMapDirect.java:67)
      //   at org.jetbrains.java.decompiler.modules.decompiler.sforms.SSAUConstructorSparseEx.updateLiveMap(SSAUConstructorSparseEx.java:269)
      //   at org.jetbrains.java.decompiler.modules.decompiler.sforms.SSAUConstructorSparseEx.varReadSingleVersion(SSAUConstructorSparseEx.java:110)
      //   at org.jetbrains.java.decompiler.modules.decompiler.sforms.SFormsConstructor.varRead(SFormsConstructor.java:167)
      //   at org.jetbrains.java.decompiler.modules.decompiler.exps.VarExprent.processSforms(VarExprent.java:516)
      //   at org.jetbrains.java.decompiler.modules.decompiler.exps.FieldExprent.processSforms(FieldExprent.java:302)
      //   at org.jetbrains.java.decompiler.modules.decompiler.exps.AssignmentExprent.processSforms(AssignmentExprent.java:305)
      //   at org.jetbrains.java.decompiler.modules.decompiler.sforms.SFormsConstructor.ssaStatements(SFormsConstructor.java:126)
      //   at org.jetbrains.java.decompiler.modules.decompiler.sforms.SSAUConstructorSparseEx.splitVariables(SSAUConstructorSparseEx.java:45)
      //   at org.jetbrains.java.decompiler.modules.decompiler.StackVarsProcessor.simplifyStackVars(StackVarsProcessor.java:65)
      //   at org.jetbrains.java.decompiler.modules.decompiler.StackVarsProcessor.simplifyStackVars(StackVarsProcessor.java:40)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:231)
      //
      // Bytecode:
      // 0000: bipush 93
      // 0002: istore 1
      // 0003: iload 1
      // 0004: iflt 0023
      // 0007: bipush 93
      // 0009: istore 2
      // 000a: iload 2
      // 000b: iflt 001d
      // 000e: aload 0
      // 000f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0012: iload 1
      // 0013: aaload
      // 0014: iload 2
      // 0015: bipush 0
      // 0016: iastore
      // 0017: iinc 2 -1
      // 001a: goto 000a
      // 001d: iinc 1 -1
      // 0020: goto 0003
      // 0023: bipush 125
      // 0025: istore 1
      // 0026: iload 1
      // 0027: iflt 0047
      // 002a: sipush 190
      // 002d: istore 2
      // 002e: iload 2
      // 002f: iflt 0041
      // 0032: aload 0
      // 0033: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 0036: iload 1
      // 0037: aaload
      // 0038: iload 2
      // 0039: bipush 0
      // 003a: iastore
      // 003b: iinc 2 -1
      // 003e: goto 002e
      // 0041: iinc 1 -1
      // 0044: goto 0026
      // 0047: bipush 93
      // 0049: istore 1
      // 004a: iload 1
      // 004b: iflt 006b
      // 004e: sipush 157
      // 0051: istore 2
      // 0052: iload 2
      // 0053: iflt 0065
      // 0056: aload 0
      // 0057: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 005a: iload 1
      // 005b: aaload
      // 005c: iload 2
      // 005d: bipush 0
      // 005e: iastore
      // 005f: iinc 2 -1
      // 0062: goto 0052
      // 0065: iinc 1 -1
      // 0068: goto 004a
      // 006b: bipush 125
      // 006d: istore 1
      // 006e: iload 1
      // 006f: iflt 008f
      // 0072: sipush 190
      // 0075: istore 2
      // 0076: iload 2
      // 0077: iflt 0089
      // 007a: aload 0
      // 007b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 007e: iload 1
      // 007f: aaload
      // 0080: iload 2
      // 0081: bipush 0
      // 0082: iastore
      // 0083: iinc 2 -1
      // 0086: goto 0076
      // 0089: iinc 1 -1
      // 008c: goto 006e
      // 008f: bipush 93
      // 0091: istore 1
      // 0092: iload 1
      // 0093: iflt 00b2
      // 0096: bipush 93
      // 0098: istore 2
      // 0099: iload 2
      // 009a: iflt 00ac
      // 009d: aload 0
      // 009e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 00a1: iload 1
      // 00a2: aaload
      // 00a3: iload 2
      // 00a4: bipush 0
      // 00a5: iastore
      // 00a6: iinc 2 -1
      // 00a9: goto 0099
      // 00ac: iinc 1 -1
      // 00af: goto 0092
      // 00b2: bipush 93
      // 00b4: istore 1
      // 00b5: iload 1
      // 00b6: iflt 00d5
      // 00b9: bipush 93
      // 00bb: istore 2
      // 00bc: iload 2
      // 00bd: iflt 00cf
      // 00c0: aload 0
      // 00c1: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 00c4: iload 1
      // 00c5: aaload
      // 00c6: iload 2
      // 00c7: bipush 0
      // 00c8: iastore
      // 00c9: iinc 2 -1
      // 00cc: goto 00bc
      // 00cf: iinc 1 -1
      // 00d2: goto 00b5
      // 00d5: aload 0
      // 00d6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 00d9: bipush 20
      // 00db: aaload
      // 00dc: bipush 35
      // 00de: sipush 599
      // 00e1: iastore
      // 00e2: aload 0
      // 00e3: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 00e6: bipush 49
      // 00e8: aaload
      // 00e9: bipush 26
      // 00eb: sipush 598
      // 00ee: iastore
      // 00ef: aload 0
      // 00f0: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 00f3: bipush 41
      // 00f5: aaload
      // 00f6: bipush 38
      // 00f8: sipush 597
      // 00fb: iastore
      // 00fc: aload 0
      // 00fd: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0100: bipush 17
      // 0102: aaload
      // 0103: bipush 26
      // 0105: sipush 596
      // 0108: iastore
      // 0109: aload 0
      // 010a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 010d: bipush 32
      // 010f: aaload
      // 0110: bipush 42
      // 0112: sipush 595
      // 0115: iastore
      // 0116: aload 0
      // 0117: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 011a: bipush 39
      // 011c: aaload
      // 011d: bipush 42
      // 011f: sipush 594
      // 0122: iastore
      // 0123: aload 0
      // 0124: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0127: bipush 45
      // 0129: aaload
      // 012a: bipush 49
      // 012c: sipush 593
      // 012f: iastore
      // 0130: aload 0
      // 0131: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0134: bipush 51
      // 0136: aaload
      // 0137: bipush 57
      // 0139: sipush 592
      // 013c: iastore
      // 013d: aload 0
      // 013e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0141: bipush 50
      // 0143: aaload
      // 0144: bipush 47
      // 0146: sipush 591
      // 0149: iastore
      // 014a: aload 0
      // 014b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 014e: bipush 42
      // 0150: aaload
      // 0151: bipush 90
      // 0153: sipush 590
      // 0156: iastore
      // 0157: aload 0
      // 0158: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 015b: bipush 52
      // 015d: aaload
      // 015e: bipush 65
      // 0160: sipush 589
      // 0163: iastore
      // 0164: aload 0
      // 0165: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0168: bipush 53
      // 016a: aaload
      // 016b: bipush 47
      // 016d: sipush 588
      // 0170: iastore
      // 0171: aload 0
      // 0172: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0175: bipush 19
      // 0177: aaload
      // 0178: bipush 82
      // 017a: sipush 587
      // 017d: iastore
      // 017e: aload 0
      // 017f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0182: bipush 31
      // 0184: aaload
      // 0185: bipush 19
      // 0187: sipush 586
      // 018a: iastore
      // 018b: aload 0
      // 018c: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 018f: bipush 40
      // 0191: aaload
      // 0192: bipush 46
      // 0194: sipush 585
      // 0197: iastore
      // 0198: aload 0
      // 0199: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 019c: bipush 24
      // 019e: aaload
      // 019f: bipush 89
      // 01a1: sipush 584
      // 01a4: iastore
      // 01a5: aload 0
      // 01a6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 01a9: bipush 23
      // 01ab: aaload
      // 01ac: bipush 85
      // 01ae: sipush 583
      // 01b1: iastore
      // 01b2: aload 0
      // 01b3: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 01b6: bipush 20
      // 01b8: aaload
      // 01b9: bipush 28
      // 01bb: sipush 582
      // 01be: iastore
      // 01bf: aload 0
      // 01c0: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 01c3: bipush 42
      // 01c5: aaload
      // 01c6: bipush 20
      // 01c8: sipush 581
      // 01cb: iastore
      // 01cc: aload 0
      // 01cd: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 01d0: bipush 34
      // 01d2: aaload
      // 01d3: bipush 38
      // 01d5: sipush 580
      // 01d8: iastore
      // 01d9: aload 0
      // 01da: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 01dd: bipush 45
      // 01df: aaload
      // 01e0: bipush 9
      // 01e2: sipush 579
      // 01e5: iastore
      // 01e6: aload 0
      // 01e7: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 01ea: bipush 54
      // 01ec: aaload
      // 01ed: bipush 50
      // 01ef: sipush 578
      // 01f2: iastore
      // 01f3: aload 0
      // 01f4: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 01f7: bipush 25
      // 01f9: aaload
      // 01fa: bipush 44
      // 01fc: sipush 577
      // 01ff: iastore
      // 0200: aload 0
      // 0201: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0204: bipush 35
      // 0206: aaload
      // 0207: bipush 66
      // 0209: sipush 576
      // 020c: iastore
      // 020d: aload 0
      // 020e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0211: bipush 20
      // 0213: aaload
      // 0214: bipush 55
      // 0216: sipush 575
      // 0219: iastore
      // 021a: aload 0
      // 021b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 021e: bipush 18
      // 0220: aaload
      // 0221: bipush 85
      // 0223: sipush 574
      // 0226: iastore
      // 0227: aload 0
      // 0228: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 022b: bipush 20
      // 022d: aaload
      // 022e: bipush 31
      // 0230: sipush 573
      // 0233: iastore
      // 0234: aload 0
      // 0235: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0238: bipush 49
      // 023a: aaload
      // 023b: bipush 17
      // 023d: sipush 572
      // 0240: iastore
      // 0241: aload 0
      // 0242: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0245: bipush 41
      // 0247: aaload
      // 0248: bipush 16
      // 024a: sipush 571
      // 024d: iastore
      // 024e: aload 0
      // 024f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0252: bipush 35
      // 0254: aaload
      // 0255: bipush 73
      // 0257: sipush 570
      // 025a: iastore
      // 025b: aload 0
      // 025c: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 025f: bipush 20
      // 0261: aaload
      // 0262: bipush 34
      // 0264: sipush 569
      // 0267: iastore
      // 0268: aload 0
      // 0269: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 026c: bipush 29
      // 026e: aaload
      // 026f: bipush 44
      // 0271: sipush 568
      // 0274: iastore
      // 0275: aload 0
      // 0276: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0279: bipush 35
      // 027b: aaload
      // 027c: bipush 38
      // 027e: sipush 567
      // 0281: iastore
      // 0282: aload 0
      // 0283: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0286: bipush 49
      // 0288: aaload
      // 0289: bipush 9
      // 028b: sipush 566
      // 028e: iastore
      // 028f: aload 0
      // 0290: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0293: bipush 46
      // 0295: aaload
      // 0296: bipush 33
      // 0298: sipush 565
      // 029b: iastore
      // 029c: aload 0
      // 029d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 02a0: bipush 49
      // 02a2: aaload
      // 02a3: bipush 51
      // 02a5: sipush 564
      // 02a8: iastore
      // 02a9: aload 0
      // 02aa: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 02ad: bipush 40
      // 02af: aaload
      // 02b0: bipush 89
      // 02b2: sipush 563
      // 02b5: iastore
      // 02b6: aload 0
      // 02b7: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 02ba: bipush 26
      // 02bc: aaload
      // 02bd: bipush 64
      // 02bf: sipush 562
      // 02c2: iastore
      // 02c3: aload 0
      // 02c4: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 02c7: bipush 54
      // 02c9: aaload
      // 02ca: bipush 51
      // 02cc: sipush 561
      // 02cf: iastore
      // 02d0: aload 0
      // 02d1: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 02d4: bipush 54
      // 02d6: aaload
      // 02d7: bipush 36
      // 02d9: sipush 560
      // 02dc: iastore
      // 02dd: aload 0
      // 02de: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 02e1: bipush 39
      // 02e3: aaload
      // 02e4: bipush 4
      // 02e5: sipush 559
      // 02e8: iastore
      // 02e9: aload 0
      // 02ea: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 02ed: bipush 53
      // 02ef: aaload
      // 02f0: bipush 13
      // 02f2: sipush 558
      // 02f5: iastore
      // 02f6: aload 0
      // 02f7: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 02fa: bipush 24
      // 02fc: aaload
      // 02fd: bipush 92
      // 02ff: sipush 557
      // 0302: iastore
      // 0303: aload 0
      // 0304: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0307: bipush 27
      // 0309: aaload
      // 030a: bipush 49
      // 030c: sipush 556
      // 030f: iastore
      // 0310: aload 0
      // 0311: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0314: bipush 48
      // 0316: aaload
      // 0317: bipush 6
      // 0319: sipush 555
      // 031c: iastore
      // 031d: aload 0
      // 031e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0321: bipush 21
      // 0323: aaload
      // 0324: bipush 51
      // 0326: sipush 554
      // 0329: iastore
      // 032a: aload 0
      // 032b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 032e: bipush 30
      // 0330: aaload
      // 0331: bipush 40
      // 0333: sipush 553
      // 0336: iastore
      // 0337: aload 0
      // 0338: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 033b: bipush 42
      // 033d: aaload
      // 033e: bipush 92
      // 0340: sipush 552
      // 0343: iastore
      // 0344: aload 0
      // 0345: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0348: bipush 31
      // 034a: aaload
      // 034b: bipush 78
      // 034d: sipush 551
      // 0350: iastore
      // 0351: aload 0
      // 0352: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0355: bipush 25
      // 0357: aaload
      // 0358: bipush 82
      // 035a: sipush 550
      // 035d: iastore
      // 035e: aload 0
      // 035f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0362: bipush 47
      // 0364: aaload
      // 0365: bipush 0
      // 0366: sipush 549
      // 0369: iastore
      // 036a: aload 0
      // 036b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 036e: bipush 34
      // 0370: aaload
      // 0371: bipush 19
      // 0373: sipush 548
      // 0376: iastore
      // 0377: aload 0
      // 0378: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 037b: bipush 47
      // 037d: aaload
      // 037e: bipush 35
      // 0380: sipush 547
      // 0383: iastore
      // 0384: aload 0
      // 0385: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0388: bipush 21
      // 038a: aaload
      // 038b: bipush 63
      // 038d: sipush 546
      // 0390: iastore
      // 0391: aload 0
      // 0392: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0395: bipush 43
      // 0397: aaload
      // 0398: bipush 75
      // 039a: sipush 545
      // 039d: iastore
      // 039e: aload 0
      // 039f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 03a2: bipush 21
      // 03a4: aaload
      // 03a5: bipush 87
      // 03a7: sipush 544
      // 03aa: iastore
      // 03ab: aload 0
      // 03ac: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 03af: bipush 35
      // 03b1: aaload
      // 03b2: bipush 59
      // 03b4: sipush 543
      // 03b7: iastore
      // 03b8: aload 0
      // 03b9: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 03bc: bipush 25
      // 03be: aaload
      // 03bf: bipush 34
      // 03c1: sipush 542
      // 03c4: iastore
      // 03c5: aload 0
      // 03c6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 03c9: bipush 21
      // 03cb: aaload
      // 03cc: bipush 27
      // 03ce: sipush 541
      // 03d1: iastore
      // 03d2: aload 0
      // 03d3: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 03d6: bipush 39
      // 03d8: aaload
      // 03d9: bipush 26
      // 03db: sipush 540
      // 03de: iastore
      // 03df: aload 0
      // 03e0: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 03e3: bipush 34
      // 03e5: aaload
      // 03e6: bipush 26
      // 03e8: sipush 539
      // 03eb: iastore
      // 03ec: aload 0
      // 03ed: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 03f0: bipush 39
      // 03f2: aaload
      // 03f3: bipush 52
      // 03f5: sipush 538
      // 03f8: iastore
      // 03f9: aload 0
      // 03fa: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 03fd: bipush 50
      // 03ff: aaload
      // 0400: bipush 57
      // 0402: sipush 537
      // 0405: iastore
      // 0406: aload 0
      // 0407: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 040a: bipush 37
      // 040c: aaload
      // 040d: bipush 79
      // 040f: sipush 536
      // 0412: iastore
      // 0413: aload 0
      // 0414: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0417: bipush 26
      // 0419: aaload
      // 041a: bipush 24
      // 041c: sipush 535
      // 041f: iastore
      // 0420: aload 0
      // 0421: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0424: bipush 22
      // 0426: aaload
      // 0427: bipush 1
      // 0428: sipush 534
      // 042b: iastore
      // 042c: aload 0
      // 042d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0430: bipush 18
      // 0432: aaload
      // 0433: bipush 40
      // 0435: sipush 533
      // 0438: iastore
      // 0439: aload 0
      // 043a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 043d: bipush 41
      // 043f: aaload
      // 0440: bipush 33
      // 0442: sipush 532
      // 0445: iastore
      // 0446: aload 0
      // 0447: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 044a: bipush 53
      // 044c: aaload
      // 044d: bipush 26
      // 044f: sipush 531
      // 0452: iastore
      // 0453: aload 0
      // 0454: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0457: bipush 54
      // 0459: aaload
      // 045a: bipush 86
      // 045c: sipush 530
      // 045f: iastore
      // 0460: aload 0
      // 0461: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0464: bipush 20
      // 0466: aaload
      // 0467: bipush 16
      // 0469: sipush 529
      // 046c: iastore
      // 046d: aload 0
      // 046e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0471: bipush 46
      // 0473: aaload
      // 0474: bipush 74
      // 0476: sipush 528
      // 0479: iastore
      // 047a: aload 0
      // 047b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 047e: bipush 30
      // 0480: aaload
      // 0481: bipush 19
      // 0483: sipush 527
      // 0486: iastore
      // 0487: aload 0
      // 0488: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 048b: bipush 45
      // 048d: aaload
      // 048e: bipush 35
      // 0490: sipush 526
      // 0493: iastore
      // 0494: aload 0
      // 0495: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0498: bipush 45
      // 049a: aaload
      // 049b: bipush 61
      // 049d: sipush 525
      // 04a0: iastore
      // 04a1: aload 0
      // 04a2: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 04a5: bipush 30
      // 04a7: aaload
      // 04a8: bipush 9
      // 04aa: sipush 524
      // 04ad: iastore
      // 04ae: aload 0
      // 04af: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 04b2: bipush 41
      // 04b4: aaload
      // 04b5: bipush 53
      // 04b7: sipush 523
      // 04ba: iastore
      // 04bb: aload 0
      // 04bc: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 04bf: bipush 41
      // 04c1: aaload
      // 04c2: bipush 13
      // 04c4: sipush 522
      // 04c7: iastore
      // 04c8: aload 0
      // 04c9: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 04cc: bipush 50
      // 04ce: aaload
      // 04cf: bipush 34
      // 04d1: sipush 521
      // 04d4: iastore
      // 04d5: aload 0
      // 04d6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 04d9: bipush 53
      // 04db: aaload
      // 04dc: bipush 86
      // 04de: sipush 520
      // 04e1: iastore
      // 04e2: aload 0
      // 04e3: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 04e6: bipush 47
      // 04e8: aaload
      // 04e9: bipush 47
      // 04eb: sipush 519
      // 04ee: iastore
      // 04ef: aload 0
      // 04f0: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 04f3: bipush 22
      // 04f5: aaload
      // 04f6: bipush 28
      // 04f8: sipush 518
      // 04fb: iastore
      // 04fc: aload 0
      // 04fd: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0500: bipush 50
      // 0502: aaload
      // 0503: bipush 53
      // 0505: sipush 517
      // 0508: iastore
      // 0509: aload 0
      // 050a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 050d: bipush 39
      // 050f: aaload
      // 0510: bipush 70
      // 0512: sipush 516
      // 0515: iastore
      // 0516: aload 0
      // 0517: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 051a: bipush 38
      // 051c: aaload
      // 051d: bipush 15
      // 051f: sipush 515
      // 0522: iastore
      // 0523: aload 0
      // 0524: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0527: bipush 42
      // 0529: aaload
      // 052a: bipush 88
      // 052c: sipush 514
      // 052f: iastore
      // 0530: aload 0
      // 0531: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0534: bipush 16
      // 0536: aaload
      // 0537: bipush 29
      // 0539: sipush 513
      // 053c: iastore
      // 053d: aload 0
      // 053e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0541: bipush 27
      // 0543: aaload
      // 0544: bipush 90
      // 0546: sipush 512
      // 0549: iastore
      // 054a: aload 0
      // 054b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 054e: bipush 29
      // 0550: aaload
      // 0551: bipush 12
      // 0553: sipush 511
      // 0556: iastore
      // 0557: aload 0
      // 0558: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 055b: bipush 44
      // 055d: aaload
      // 055e: bipush 22
      // 0560: sipush 510
      // 0563: iastore
      // 0564: aload 0
      // 0565: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0568: bipush 34
      // 056a: aaload
      // 056b: bipush 69
      // 056d: sipush 509
      // 0570: iastore
      // 0571: aload 0
      // 0572: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0575: bipush 24
      // 0577: aaload
      // 0578: bipush 10
      // 057a: sipush 508
      // 057d: iastore
      // 057e: aload 0
      // 057f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0582: bipush 44
      // 0584: aaload
      // 0585: bipush 11
      // 0587: sipush 507
      // 058a: iastore
      // 058b: aload 0
      // 058c: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 058f: bipush 39
      // 0591: aaload
      // 0592: bipush 92
      // 0594: sipush 506
      // 0597: iastore
      // 0598: aload 0
      // 0599: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 059c: bipush 49
      // 059e: aaload
      // 059f: bipush 48
      // 05a1: sipush 505
      // 05a4: iastore
      // 05a5: aload 0
      // 05a6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 05a9: bipush 31
      // 05ab: aaload
      // 05ac: bipush 46
      // 05ae: sipush 504
      // 05b1: iastore
      // 05b2: aload 0
      // 05b3: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 05b6: bipush 19
      // 05b8: aaload
      // 05b9: bipush 50
      // 05bb: sipush 503
      // 05be: iastore
      // 05bf: aload 0
      // 05c0: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 05c3: bipush 21
      // 05c5: aaload
      // 05c6: bipush 14
      // 05c8: sipush 502
      // 05cb: iastore
      // 05cc: aload 0
      // 05cd: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 05d0: bipush 32
      // 05d2: aaload
      // 05d3: bipush 28
      // 05d5: sipush 501
      // 05d8: iastore
      // 05d9: aload 0
      // 05da: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 05dd: bipush 18
      // 05df: aaload
      // 05e0: bipush 3
      // 05e1: sipush 500
      // 05e4: iastore
      // 05e5: aload 0
      // 05e6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 05e9: bipush 53
      // 05eb: aaload
      // 05ec: bipush 9
      // 05ee: sipush 499
      // 05f1: iastore
      // 05f2: aload 0
      // 05f3: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 05f6: bipush 34
      // 05f8: aaload
      // 05f9: bipush 80
      // 05fb: sipush 498
      // 05fe: iastore
      // 05ff: aload 0
      // 0600: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0603: bipush 48
      // 0605: aaload
      // 0606: bipush 88
      // 0608: sipush 497
      // 060b: iastore
      // 060c: aload 0
      // 060d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0610: bipush 46
      // 0612: aaload
      // 0613: bipush 53
      // 0615: sipush 496
      // 0618: iastore
      // 0619: aload 0
      // 061a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 061d: bipush 22
      // 061f: aaload
      // 0620: bipush 53
      // 0622: sipush 495
      // 0625: iastore
      // 0626: aload 0
      // 0627: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 062a: bipush 28
      // 062c: aaload
      // 062d: bipush 10
      // 062f: sipush 494
      // 0632: iastore
      // 0633: aload 0
      // 0634: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0637: bipush 44
      // 0639: aaload
      // 063a: bipush 65
      // 063c: sipush 493
      // 063f: iastore
      // 0640: aload 0
      // 0641: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0644: bipush 20
      // 0646: aaload
      // 0647: bipush 10
      // 0649: sipush 492
      // 064c: iastore
      // 064d: aload 0
      // 064e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0651: bipush 40
      // 0653: aaload
      // 0654: bipush 76
      // 0656: sipush 491
      // 0659: iastore
      // 065a: aload 0
      // 065b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 065e: bipush 47
      // 0660: aaload
      // 0661: bipush 8
      // 0663: sipush 490
      // 0666: iastore
      // 0667: aload 0
      // 0668: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 066b: bipush 50
      // 066d: aaload
      // 066e: bipush 74
      // 0670: sipush 489
      // 0673: iastore
      // 0674: aload 0
      // 0675: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0678: bipush 23
      // 067a: aaload
      // 067b: bipush 62
      // 067d: sipush 488
      // 0680: iastore
      // 0681: aload 0
      // 0682: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0685: bipush 49
      // 0687: aaload
      // 0688: bipush 65
      // 068a: sipush 487
      // 068d: iastore
      // 068e: aload 0
      // 068f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0692: bipush 28
      // 0694: aaload
      // 0695: bipush 87
      // 0697: sipush 486
      // 069a: iastore
      // 069b: aload 0
      // 069c: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 069f: bipush 15
      // 06a1: aaload
      // 06a2: bipush 48
      // 06a4: sipush 485
      // 06a7: iastore
      // 06a8: aload 0
      // 06a9: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 06ac: bipush 22
      // 06ae: aaload
      // 06af: bipush 7
      // 06b1: sipush 484
      // 06b4: iastore
      // 06b5: aload 0
      // 06b6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 06b9: bipush 19
      // 06bb: aaload
      // 06bc: bipush 42
      // 06be: sipush 483
      // 06c1: iastore
      // 06c2: aload 0
      // 06c3: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 06c6: bipush 41
      // 06c8: aaload
      // 06c9: bipush 20
      // 06cb: sipush 482
      // 06ce: iastore
      // 06cf: aload 0
      // 06d0: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 06d3: bipush 26
      // 06d5: aaload
      // 06d6: bipush 55
      // 06d8: sipush 481
      // 06db: iastore
      // 06dc: aload 0
      // 06dd: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 06e0: bipush 21
      // 06e2: aaload
      // 06e3: bipush 93
      // 06e5: sipush 480
      // 06e8: iastore
      // 06e9: aload 0
      // 06ea: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 06ed: bipush 31
      // 06ef: aaload
      // 06f0: bipush 76
      // 06f2: sipush 479
      // 06f5: iastore
      // 06f6: aload 0
      // 06f7: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 06fa: bipush 34
      // 06fc: aaload
      // 06fd: bipush 31
      // 06ff: sipush 478
      // 0702: iastore
      // 0703: aload 0
      // 0704: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0707: bipush 20
      // 0709: aaload
      // 070a: bipush 66
      // 070c: sipush 477
      // 070f: iastore
      // 0710: aload 0
      // 0711: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0714: bipush 51
      // 0716: aaload
      // 0717: bipush 33
      // 0719: sipush 476
      // 071c: iastore
      // 071d: aload 0
      // 071e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0721: bipush 34
      // 0723: aaload
      // 0724: bipush 86
      // 0726: sipush 475
      // 0729: iastore
      // 072a: aload 0
      // 072b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 072e: bipush 37
      // 0730: aaload
      // 0731: bipush 67
      // 0733: sipush 474
      // 0736: iastore
      // 0737: aload 0
      // 0738: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 073b: bipush 53
      // 073d: aaload
      // 073e: bipush 53
      // 0740: sipush 473
      // 0743: iastore
      // 0744: aload 0
      // 0745: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0748: bipush 40
      // 074a: aaload
      // 074b: bipush 88
      // 074d: sipush 472
      // 0750: iastore
      // 0751: aload 0
      // 0752: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0755: bipush 39
      // 0757: aaload
      // 0758: bipush 10
      // 075a: sipush 471
      // 075d: iastore
      // 075e: aload 0
      // 075f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0762: bipush 24
      // 0764: aaload
      // 0765: bipush 3
      // 0766: sipush 470
      // 0769: iastore
      // 076a: aload 0
      // 076b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 076e: bipush 27
      // 0770: aaload
      // 0771: bipush 25
      // 0773: sipush 469
      // 0776: iastore
      // 0777: aload 0
      // 0778: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 077b: bipush 26
      // 077d: aaload
      // 077e: bipush 15
      // 0780: sipush 468
      // 0783: iastore
      // 0784: aload 0
      // 0785: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0788: bipush 21
      // 078a: aaload
      // 078b: bipush 88
      // 078d: sipush 467
      // 0790: iastore
      // 0791: aload 0
      // 0792: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0795: bipush 52
      // 0797: aaload
      // 0798: bipush 62
      // 079a: sipush 466
      // 079d: iastore
      // 079e: aload 0
      // 079f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 07a2: bipush 46
      // 07a4: aaload
      // 07a5: bipush 81
      // 07a7: sipush 465
      // 07aa: iastore
      // 07ab: aload 0
      // 07ac: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 07af: bipush 38
      // 07b1: aaload
      // 07b2: bipush 72
      // 07b4: sipush 464
      // 07b7: iastore
      // 07b8: aload 0
      // 07b9: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 07bc: bipush 17
      // 07be: aaload
      // 07bf: bipush 30
      // 07c1: sipush 463
      // 07c4: iastore
      // 07c5: aload 0
      // 07c6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 07c9: bipush 52
      // 07cb: aaload
      // 07cc: bipush 92
      // 07ce: sipush 462
      // 07d1: iastore
      // 07d2: aload 0
      // 07d3: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 07d6: bipush 34
      // 07d8: aaload
      // 07d9: bipush 90
      // 07db: sipush 461
      // 07de: iastore
      // 07df: aload 0
      // 07e0: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 07e3: bipush 21
      // 07e5: aaload
      // 07e6: bipush 7
      // 07e8: sipush 460
      // 07eb: iastore
      // 07ec: aload 0
      // 07ed: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 07f0: bipush 36
      // 07f2: aaload
      // 07f3: bipush 13
      // 07f5: sipush 459
      // 07f8: iastore
      // 07f9: aload 0
      // 07fa: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 07fd: bipush 45
      // 07ff: aaload
      // 0800: bipush 41
      // 0802: sipush 458
      // 0805: iastore
      // 0806: aload 0
      // 0807: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 080a: bipush 32
      // 080c: aaload
      // 080d: bipush 5
      // 080e: sipush 457
      // 0811: iastore
      // 0812: aload 0
      // 0813: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0816: bipush 26
      // 0818: aaload
      // 0819: bipush 89
      // 081b: sipush 456
      // 081e: iastore
      // 081f: aload 0
      // 0820: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0823: bipush 23
      // 0825: aaload
      // 0826: bipush 87
      // 0828: sipush 455
      // 082b: iastore
      // 082c: aload 0
      // 082d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0830: bipush 20
      // 0832: aaload
      // 0833: bipush 39
      // 0835: sipush 454
      // 0838: iastore
      // 0839: aload 0
      // 083a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 083d: bipush 27
      // 083f: aaload
      // 0840: bipush 23
      // 0842: sipush 453
      // 0845: iastore
      // 0846: aload 0
      // 0847: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 084a: bipush 25
      // 084c: aaload
      // 084d: bipush 59
      // 084f: sipush 452
      // 0852: iastore
      // 0853: aload 0
      // 0854: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0857: bipush 49
      // 0859: aaload
      // 085a: bipush 20
      // 085c: sipush 451
      // 085f: iastore
      // 0860: aload 0
      // 0861: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0864: bipush 54
      // 0866: aaload
      // 0867: bipush 77
      // 0869: sipush 450
      // 086c: iastore
      // 086d: aload 0
      // 086e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0871: bipush 27
      // 0873: aaload
      // 0874: bipush 67
      // 0876: sipush 449
      // 0879: iastore
      // 087a: aload 0
      // 087b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 087e: bipush 47
      // 0880: aaload
      // 0881: bipush 33
      // 0883: sipush 448
      // 0886: iastore
      // 0887: aload 0
      // 0888: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 088b: bipush 41
      // 088d: aaload
      // 088e: bipush 17
      // 0890: sipush 447
      // 0893: iastore
      // 0894: aload 0
      // 0895: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0898: bipush 19
      // 089a: aaload
      // 089b: bipush 81
      // 089d: sipush 446
      // 08a0: iastore
      // 08a1: aload 0
      // 08a2: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 08a5: bipush 16
      // 08a7: aaload
      // 08a8: bipush 66
      // 08aa: sipush 445
      // 08ad: iastore
      // 08ae: aload 0
      // 08af: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 08b2: bipush 45
      // 08b4: aaload
      // 08b5: bipush 26
      // 08b7: sipush 444
      // 08ba: iastore
      // 08bb: aload 0
      // 08bc: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 08bf: bipush 49
      // 08c1: aaload
      // 08c2: bipush 81
      // 08c4: sipush 443
      // 08c7: iastore
      // 08c8: aload 0
      // 08c9: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 08cc: bipush 53
      // 08ce: aaload
      // 08cf: bipush 55
      // 08d1: sipush 442
      // 08d4: iastore
      // 08d5: aload 0
      // 08d6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 08d9: bipush 16
      // 08db: aaload
      // 08dc: bipush 26
      // 08de: sipush 441
      // 08e1: iastore
      // 08e2: aload 0
      // 08e3: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 08e6: bipush 54
      // 08e8: aaload
      // 08e9: bipush 62
      // 08eb: sipush 440
      // 08ee: iastore
      // 08ef: aload 0
      // 08f0: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 08f3: bipush 20
      // 08f5: aaload
      // 08f6: bipush 70
      // 08f8: sipush 439
      // 08fb: iastore
      // 08fc: aload 0
      // 08fd: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0900: bipush 42
      // 0902: aaload
      // 0903: bipush 35
      // 0905: sipush 438
      // 0908: iastore
      // 0909: aload 0
      // 090a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 090d: bipush 20
      // 090f: aaload
      // 0910: bipush 57
      // 0912: sipush 437
      // 0915: iastore
      // 0916: aload 0
      // 0917: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 091a: bipush 34
      // 091c: aaload
      // 091d: bipush 36
      // 091f: sipush 436
      // 0922: iastore
      // 0923: aload 0
      // 0924: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0927: bipush 46
      // 0929: aaload
      // 092a: bipush 63
      // 092c: sipush 435
      // 092f: iastore
      // 0930: aload 0
      // 0931: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0934: bipush 19
      // 0936: aaload
      // 0937: bipush 45
      // 0939: sipush 434
      // 093c: iastore
      // 093d: aload 0
      // 093e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0941: bipush 21
      // 0943: aaload
      // 0944: bipush 10
      // 0946: sipush 433
      // 0949: iastore
      // 094a: aload 0
      // 094b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 094e: bipush 52
      // 0950: aaload
      // 0951: bipush 93
      // 0953: sipush 432
      // 0956: iastore
      // 0957: aload 0
      // 0958: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 095b: bipush 25
      // 095d: aaload
      // 095e: bipush 2
      // 095f: sipush 431
      // 0962: iastore
      // 0963: aload 0
      // 0964: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0967: bipush 30
      // 0969: aaload
      // 096a: bipush 57
      // 096c: sipush 430
      // 096f: iastore
      // 0970: aload 0
      // 0971: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0974: bipush 41
      // 0976: aaload
      // 0977: bipush 24
      // 0979: sipush 429
      // 097c: iastore
      // 097d: aload 0
      // 097e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0981: bipush 28
      // 0983: aaload
      // 0984: bipush 43
      // 0986: sipush 428
      // 0989: iastore
      // 098a: aload 0
      // 098b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 098e: bipush 45
      // 0990: aaload
      // 0991: bipush 86
      // 0993: sipush 427
      // 0996: iastore
      // 0997: aload 0
      // 0998: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 099b: bipush 51
      // 099d: aaload
      // 099e: bipush 56
      // 09a0: sipush 426
      // 09a3: iastore
      // 09a4: aload 0
      // 09a5: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 09a8: bipush 37
      // 09aa: aaload
      // 09ab: bipush 28
      // 09ad: sipush 425
      // 09b0: iastore
      // 09b1: aload 0
      // 09b2: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 09b5: bipush 52
      // 09b7: aaload
      // 09b8: bipush 69
      // 09ba: sipush 424
      // 09bd: iastore
      // 09be: aload 0
      // 09bf: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 09c2: bipush 43
      // 09c4: aaload
      // 09c5: bipush 92
      // 09c7: sipush 423
      // 09ca: iastore
      // 09cb: aload 0
      // 09cc: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 09cf: bipush 41
      // 09d1: aaload
      // 09d2: bipush 31
      // 09d4: sipush 422
      // 09d7: iastore
      // 09d8: aload 0
      // 09d9: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 09dc: bipush 37
      // 09de: aaload
      // 09df: bipush 87
      // 09e1: sipush 421
      // 09e4: iastore
      // 09e5: aload 0
      // 09e6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 09e9: bipush 47
      // 09eb: aaload
      // 09ec: bipush 36
      // 09ee: sipush 420
      // 09f1: iastore
      // 09f2: aload 0
      // 09f3: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 09f6: bipush 16
      // 09f8: aaload
      // 09f9: bipush 16
      // 09fb: sipush 419
      // 09fe: iastore
      // 09ff: aload 0
      // 0a00: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0a03: bipush 40
      // 0a05: aaload
      // 0a06: bipush 56
      // 0a08: sipush 418
      // 0a0b: iastore
      // 0a0c: aload 0
      // 0a0d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0a10: bipush 24
      // 0a12: aaload
      // 0a13: bipush 55
      // 0a15: sipush 417
      // 0a18: iastore
      // 0a19: aload 0
      // 0a1a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0a1d: bipush 17
      // 0a1f: aaload
      // 0a20: bipush 1
      // 0a21: sipush 416
      // 0a24: iastore
      // 0a25: aload 0
      // 0a26: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0a29: bipush 35
      // 0a2b: aaload
      // 0a2c: bipush 57
      // 0a2e: sipush 415
      // 0a31: iastore
      // 0a32: aload 0
      // 0a33: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0a36: bipush 27
      // 0a38: aaload
      // 0a39: bipush 50
      // 0a3b: sipush 414
      // 0a3e: iastore
      // 0a3f: aload 0
      // 0a40: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0a43: bipush 26
      // 0a45: aaload
      // 0a46: bipush 14
      // 0a48: sipush 413
      // 0a4b: iastore
      // 0a4c: aload 0
      // 0a4d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0a50: bipush 50
      // 0a52: aaload
      // 0a53: bipush 40
      // 0a55: sipush 412
      // 0a58: iastore
      // 0a59: aload 0
      // 0a5a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0a5d: bipush 39
      // 0a5f: aaload
      // 0a60: bipush 19
      // 0a62: sipush 411
      // 0a65: iastore
      // 0a66: aload 0
      // 0a67: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0a6a: bipush 19
      // 0a6c: aaload
      // 0a6d: bipush 89
      // 0a6f: sipush 410
      // 0a72: iastore
      // 0a73: aload 0
      // 0a74: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0a77: bipush 29
      // 0a79: aaload
      // 0a7a: bipush 91
      // 0a7c: sipush 409
      // 0a7f: iastore
      // 0a80: aload 0
      // 0a81: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0a84: bipush 17
      // 0a86: aaload
      // 0a87: bipush 89
      // 0a89: sipush 408
      // 0a8c: iastore
      // 0a8d: aload 0
      // 0a8e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0a91: bipush 39
      // 0a93: aaload
      // 0a94: bipush 74
      // 0a96: sipush 407
      // 0a99: iastore
      // 0a9a: aload 0
      // 0a9b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0a9e: bipush 46
      // 0aa0: aaload
      // 0aa1: bipush 39
      // 0aa3: sipush 406
      // 0aa6: iastore
      // 0aa7: aload 0
      // 0aa8: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0aab: bipush 40
      // 0aad: aaload
      // 0aae: bipush 28
      // 0ab0: sipush 405
      // 0ab3: iastore
      // 0ab4: aload 0
      // 0ab5: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0ab8: bipush 45
      // 0aba: aaload
      // 0abb: bipush 68
      // 0abd: sipush 404
      // 0ac0: iastore
      // 0ac1: aload 0
      // 0ac2: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0ac5: bipush 43
      // 0ac7: aaload
      // 0ac8: bipush 10
      // 0aca: sipush 403
      // 0acd: iastore
      // 0ace: aload 0
      // 0acf: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0ad2: bipush 42
      // 0ad4: aaload
      // 0ad5: bipush 13
      // 0ad7: sipush 402
      // 0ada: iastore
      // 0adb: aload 0
      // 0adc: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0adf: bipush 44
      // 0ae1: aaload
      // 0ae2: bipush 81
      // 0ae4: sipush 401
      // 0ae7: iastore
      // 0ae8: aload 0
      // 0ae9: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0aec: bipush 41
      // 0aee: aaload
      // 0aef: bipush 47
      // 0af1: sipush 400
      // 0af4: iastore
      // 0af5: aload 0
      // 0af6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0af9: bipush 48
      // 0afb: aaload
      // 0afc: bipush 58
      // 0afe: sipush 399
      // 0b01: iastore
      // 0b02: aload 0
      // 0b03: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0b06: bipush 43
      // 0b08: aaload
      // 0b09: bipush 68
      // 0b0b: sipush 398
      // 0b0e: iastore
      // 0b0f: aload 0
      // 0b10: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0b13: bipush 16
      // 0b15: aaload
      // 0b16: bipush 79
      // 0b18: sipush 397
      // 0b1b: iastore
      // 0b1c: aload 0
      // 0b1d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0b20: bipush 19
      // 0b22: aaload
      // 0b23: bipush 5
      // 0b24: sipush 396
      // 0b27: iastore
      // 0b28: aload 0
      // 0b29: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0b2c: bipush 54
      // 0b2e: aaload
      // 0b2f: bipush 59
      // 0b31: sipush 395
      // 0b34: iastore
      // 0b35: aload 0
      // 0b36: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0b39: bipush 17
      // 0b3b: aaload
      // 0b3c: bipush 36
      // 0b3e: sipush 394
      // 0b41: iastore
      // 0b42: aload 0
      // 0b43: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0b46: bipush 18
      // 0b48: aaload
      // 0b49: bipush 0
      // 0b4a: sipush 393
      // 0b4d: iastore
      // 0b4e: aload 0
      // 0b4f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0b52: bipush 41
      // 0b54: aaload
      // 0b55: bipush 5
      // 0b56: sipush 392
      // 0b59: iastore
      // 0b5a: aload 0
      // 0b5b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0b5e: bipush 41
      // 0b60: aaload
      // 0b61: bipush 72
      // 0b63: sipush 391
      // 0b66: iastore
      // 0b67: aload 0
      // 0b68: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0b6b: bipush 16
      // 0b6d: aaload
      // 0b6e: bipush 39
      // 0b70: sipush 390
      // 0b73: iastore
      // 0b74: aload 0
      // 0b75: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0b78: bipush 54
      // 0b7a: aaload
      // 0b7b: bipush 0
      // 0b7c: sipush 389
      // 0b7f: iastore
      // 0b80: aload 0
      // 0b81: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0b84: bipush 51
      // 0b86: aaload
      // 0b87: bipush 16
      // 0b89: sipush 388
      // 0b8c: iastore
      // 0b8d: aload 0
      // 0b8e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0b91: bipush 29
      // 0b93: aaload
      // 0b94: bipush 36
      // 0b96: sipush 387
      // 0b99: iastore
      // 0b9a: aload 0
      // 0b9b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0b9e: bipush 47
      // 0ba0: aaload
      // 0ba1: bipush 5
      // 0ba2: sipush 386
      // 0ba5: iastore
      // 0ba6: aload 0
      // 0ba7: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0baa: bipush 47
      // 0bac: aaload
      // 0bad: bipush 51
      // 0baf: sipush 385
      // 0bb2: iastore
      // 0bb3: aload 0
      // 0bb4: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0bb7: bipush 44
      // 0bb9: aaload
      // 0bba: bipush 7
      // 0bbc: sipush 384
      // 0bbf: iastore
      // 0bc0: aload 0
      // 0bc1: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0bc4: bipush 35
      // 0bc6: aaload
      // 0bc7: bipush 30
      // 0bc9: sipush 383
      // 0bcc: iastore
      // 0bcd: aload 0
      // 0bce: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0bd1: bipush 26
      // 0bd3: aaload
      // 0bd4: bipush 9
      // 0bd6: sipush 382
      // 0bd9: iastore
      // 0bda: aload 0
      // 0bdb: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0bde: bipush 16
      // 0be0: aaload
      // 0be1: bipush 7
      // 0be3: sipush 381
      // 0be6: iastore
      // 0be7: aload 0
      // 0be8: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0beb: bipush 32
      // 0bed: aaload
      // 0bee: bipush 1
      // 0bef: sipush 380
      // 0bf2: iastore
      // 0bf3: aload 0
      // 0bf4: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0bf7: bipush 33
      // 0bf9: aaload
      // 0bfa: bipush 76
      // 0bfc: sipush 379
      // 0bff: iastore
      // 0c00: aload 0
      // 0c01: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0c04: bipush 34
      // 0c06: aaload
      // 0c07: bipush 91
      // 0c09: sipush 378
      // 0c0c: iastore
      // 0c0d: aload 0
      // 0c0e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0c11: bipush 52
      // 0c13: aaload
      // 0c14: bipush 36
      // 0c16: sipush 377
      // 0c19: iastore
      // 0c1a: aload 0
      // 0c1b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0c1e: bipush 26
      // 0c20: aaload
      // 0c21: bipush 77
      // 0c23: sipush 376
      // 0c26: iastore
      // 0c27: aload 0
      // 0c28: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0c2b: bipush 35
      // 0c2d: aaload
      // 0c2e: bipush 48
      // 0c30: sipush 375
      // 0c33: iastore
      // 0c34: aload 0
      // 0c35: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0c38: bipush 40
      // 0c3a: aaload
      // 0c3b: bipush 80
      // 0c3d: sipush 374
      // 0c40: iastore
      // 0c41: aload 0
      // 0c42: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0c45: bipush 41
      // 0c47: aaload
      // 0c48: bipush 92
      // 0c4a: sipush 373
      // 0c4d: iastore
      // 0c4e: aload 0
      // 0c4f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0c52: bipush 27
      // 0c54: aaload
      // 0c55: bipush 93
      // 0c57: sipush 372
      // 0c5a: iastore
      // 0c5b: aload 0
      // 0c5c: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0c5f: bipush 15
      // 0c61: aaload
      // 0c62: bipush 17
      // 0c64: sipush 371
      // 0c67: iastore
      // 0c68: aload 0
      // 0c69: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0c6c: bipush 16
      // 0c6e: aaload
      // 0c6f: bipush 76
      // 0c71: sipush 370
      // 0c74: iastore
      // 0c75: aload 0
      // 0c76: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0c79: bipush 51
      // 0c7b: aaload
      // 0c7c: bipush 12
      // 0c7e: sipush 369
      // 0c81: iastore
      // 0c82: aload 0
      // 0c83: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0c86: bipush 18
      // 0c88: aaload
      // 0c89: bipush 20
      // 0c8b: sipush 368
      // 0c8e: iastore
      // 0c8f: aload 0
      // 0c90: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0c93: bipush 15
      // 0c95: aaload
      // 0c96: bipush 54
      // 0c98: sipush 367
      // 0c9b: iastore
      // 0c9c: aload 0
      // 0c9d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0ca0: bipush 50
      // 0ca2: aaload
      // 0ca3: bipush 5
      // 0ca4: sipush 366
      // 0ca7: iastore
      // 0ca8: aload 0
      // 0ca9: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0cac: bipush 33
      // 0cae: aaload
      // 0caf: bipush 22
      // 0cb1: sipush 365
      // 0cb4: iastore
      // 0cb5: aload 0
      // 0cb6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0cb9: bipush 37
      // 0cbb: aaload
      // 0cbc: bipush 57
      // 0cbe: sipush 364
      // 0cc1: iastore
      // 0cc2: aload 0
      // 0cc3: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0cc6: bipush 28
      // 0cc8: aaload
      // 0cc9: bipush 47
      // 0ccb: sipush 363
      // 0cce: iastore
      // 0ccf: aload 0
      // 0cd0: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0cd3: bipush 42
      // 0cd5: aaload
      // 0cd6: bipush 31
      // 0cd8: sipush 362
      // 0cdb: iastore
      // 0cdc: aload 0
      // 0cdd: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0ce0: bipush 18
      // 0ce2: aaload
      // 0ce3: bipush 2
      // 0ce4: sipush 361
      // 0ce7: iastore
      // 0ce8: aload 0
      // 0ce9: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0cec: bipush 43
      // 0cee: aaload
      // 0cef: bipush 64
      // 0cf1: sipush 360
      // 0cf4: iastore
      // 0cf5: aload 0
      // 0cf6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0cf9: bipush 23
      // 0cfb: aaload
      // 0cfc: bipush 47
      // 0cfe: sipush 359
      // 0d01: iastore
      // 0d02: aload 0
      // 0d03: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0d06: bipush 28
      // 0d08: aaload
      // 0d09: bipush 79
      // 0d0b: sipush 358
      // 0d0e: iastore
      // 0d0f: aload 0
      // 0d10: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0d13: bipush 25
      // 0d15: aaload
      // 0d16: bipush 45
      // 0d18: sipush 357
      // 0d1b: iastore
      // 0d1c: aload 0
      // 0d1d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0d20: bipush 23
      // 0d22: aaload
      // 0d23: bipush 91
      // 0d25: sipush 356
      // 0d28: iastore
      // 0d29: aload 0
      // 0d2a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0d2d: bipush 22
      // 0d2f: aaload
      // 0d30: bipush 19
      // 0d32: sipush 355
      // 0d35: iastore
      // 0d36: aload 0
      // 0d37: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0d3a: bipush 25
      // 0d3c: aaload
      // 0d3d: bipush 46
      // 0d3f: sipush 354
      // 0d42: iastore
      // 0d43: aload 0
      // 0d44: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0d47: bipush 22
      // 0d49: aaload
      // 0d4a: bipush 36
      // 0d4c: sipush 353
      // 0d4f: iastore
      // 0d50: aload 0
      // 0d51: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0d54: bipush 54
      // 0d56: aaload
      // 0d57: bipush 85
      // 0d59: sipush 352
      // 0d5c: iastore
      // 0d5d: aload 0
      // 0d5e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0d61: bipush 46
      // 0d63: aaload
      // 0d64: bipush 20
      // 0d66: sipush 351
      // 0d69: iastore
      // 0d6a: aload 0
      // 0d6b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0d6e: bipush 27
      // 0d70: aaload
      // 0d71: bipush 37
      // 0d73: sipush 350
      // 0d76: iastore
      // 0d77: aload 0
      // 0d78: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0d7b: bipush 26
      // 0d7d: aaload
      // 0d7e: bipush 81
      // 0d80: sipush 349
      // 0d83: iastore
      // 0d84: aload 0
      // 0d85: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0d88: bipush 42
      // 0d8a: aaload
      // 0d8b: bipush 29
      // 0d8d: sipush 348
      // 0d90: iastore
      // 0d91: aload 0
      // 0d92: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0d95: bipush 31
      // 0d97: aaload
      // 0d98: bipush 90
      // 0d9a: sipush 347
      // 0d9d: iastore
      // 0d9e: aload 0
      // 0d9f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0da2: bipush 41
      // 0da4: aaload
      // 0da5: bipush 59
      // 0da7: sipush 346
      // 0daa: iastore
      // 0dab: aload 0
      // 0dac: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0daf: bipush 24
      // 0db1: aaload
      // 0db2: bipush 65
      // 0db4: sipush 345
      // 0db7: iastore
      // 0db8: aload 0
      // 0db9: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0dbc: bipush 44
      // 0dbe: aaload
      // 0dbf: bipush 84
      // 0dc1: sipush 344
      // 0dc4: iastore
      // 0dc5: aload 0
      // 0dc6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0dc9: bipush 24
      // 0dcb: aaload
      // 0dcc: bipush 90
      // 0dce: sipush 343
      // 0dd1: iastore
      // 0dd2: aload 0
      // 0dd3: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0dd6: bipush 38
      // 0dd8: aaload
      // 0dd9: bipush 54
      // 0ddb: sipush 342
      // 0dde: iastore
      // 0ddf: aload 0
      // 0de0: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0de3: bipush 28
      // 0de5: aaload
      // 0de6: bipush 70
      // 0de8: sipush 341
      // 0deb: iastore
      // 0dec: aload 0
      // 0ded: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0df0: bipush 27
      // 0df2: aaload
      // 0df3: bipush 15
      // 0df5: sipush 340
      // 0df8: iastore
      // 0df9: aload 0
      // 0dfa: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0dfd: bipush 28
      // 0dff: aaload
      // 0e00: bipush 80
      // 0e02: sipush 339
      // 0e05: iastore
      // 0e06: aload 0
      // 0e07: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0e0a: bipush 29
      // 0e0c: aaload
      // 0e0d: bipush 8
      // 0e0f: sipush 338
      // 0e12: iastore
      // 0e13: aload 0
      // 0e14: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0e17: bipush 45
      // 0e19: aaload
      // 0e1a: bipush 80
      // 0e1c: sipush 337
      // 0e1f: iastore
      // 0e20: aload 0
      // 0e21: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0e24: bipush 53
      // 0e26: aaload
      // 0e27: bipush 37
      // 0e29: sipush 336
      // 0e2c: iastore
      // 0e2d: aload 0
      // 0e2e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0e31: bipush 28
      // 0e33: aaload
      // 0e34: bipush 65
      // 0e36: sipush 335
      // 0e39: iastore
      // 0e3a: aload 0
      // 0e3b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0e3e: bipush 23
      // 0e40: aaload
      // 0e41: bipush 86
      // 0e43: sipush 334
      // 0e46: iastore
      // 0e47: aload 0
      // 0e48: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0e4b: bipush 39
      // 0e4d: aaload
      // 0e4e: bipush 45
      // 0e50: sipush 333
      // 0e53: iastore
      // 0e54: aload 0
      // 0e55: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0e58: bipush 53
      // 0e5a: aaload
      // 0e5b: bipush 32
      // 0e5d: sipush 332
      // 0e60: iastore
      // 0e61: aload 0
      // 0e62: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0e65: bipush 38
      // 0e67: aaload
      // 0e68: bipush 68
      // 0e6a: sipush 331
      // 0e6d: iastore
      // 0e6e: aload 0
      // 0e6f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0e72: bipush 45
      // 0e74: aaload
      // 0e75: bipush 78
      // 0e77: sipush 330
      // 0e7a: iastore
      // 0e7b: aload 0
      // 0e7c: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0e7f: bipush 43
      // 0e81: aaload
      // 0e82: bipush 7
      // 0e84: sipush 329
      // 0e87: iastore
      // 0e88: aload 0
      // 0e89: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0e8c: bipush 46
      // 0e8e: aaload
      // 0e8f: bipush 82
      // 0e91: sipush 328
      // 0e94: iastore
      // 0e95: aload 0
      // 0e96: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0e99: bipush 27
      // 0e9b: aaload
      // 0e9c: bipush 38
      // 0e9e: sipush 327
      // 0ea1: iastore
      // 0ea2: aload 0
      // 0ea3: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0ea6: bipush 16
      // 0ea8: aaload
      // 0ea9: bipush 62
      // 0eab: sipush 326
      // 0eae: iastore
      // 0eaf: aload 0
      // 0eb0: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0eb3: bipush 24
      // 0eb5: aaload
      // 0eb6: bipush 17
      // 0eb8: sipush 325
      // 0ebb: iastore
      // 0ebc: aload 0
      // 0ebd: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0ec0: bipush 22
      // 0ec2: aaload
      // 0ec3: bipush 70
      // 0ec5: sipush 324
      // 0ec8: iastore
      // 0ec9: aload 0
      // 0eca: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0ecd: bipush 52
      // 0ecf: aaload
      // 0ed0: bipush 28
      // 0ed2: sipush 323
      // 0ed5: iastore
      // 0ed6: aload 0
      // 0ed7: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0eda: bipush 23
      // 0edc: aaload
      // 0edd: bipush 40
      // 0edf: sipush 322
      // 0ee2: iastore
      // 0ee3: aload 0
      // 0ee4: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0ee7: bipush 28
      // 0ee9: aaload
      // 0eea: bipush 50
      // 0eec: sipush 321
      // 0eef: iastore
      // 0ef0: aload 0
      // 0ef1: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0ef4: bipush 42
      // 0ef6: aaload
      // 0ef7: bipush 91
      // 0ef9: sipush 320
      // 0efc: iastore
      // 0efd: aload 0
      // 0efe: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0f01: bipush 47
      // 0f03: aaload
      // 0f04: bipush 76
      // 0f06: sipush 319
      // 0f09: iastore
      // 0f0a: aload 0
      // 0f0b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0f0e: bipush 15
      // 0f10: aaload
      // 0f11: bipush 42
      // 0f13: sipush 318
      // 0f16: iastore
      // 0f17: aload 0
      // 0f18: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0f1b: bipush 43
      // 0f1d: aaload
      // 0f1e: bipush 55
      // 0f20: sipush 317
      // 0f23: iastore
      // 0f24: aload 0
      // 0f25: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0f28: bipush 29
      // 0f2a: aaload
      // 0f2b: bipush 84
      // 0f2d: sipush 316
      // 0f30: iastore
      // 0f31: aload 0
      // 0f32: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0f35: bipush 44
      // 0f37: aaload
      // 0f38: bipush 90
      // 0f3a: sipush 315
      // 0f3d: iastore
      // 0f3e: aload 0
      // 0f3f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0f42: bipush 53
      // 0f44: aaload
      // 0f45: bipush 16
      // 0f47: sipush 314
      // 0f4a: iastore
      // 0f4b: aload 0
      // 0f4c: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0f4f: bipush 22
      // 0f51: aaload
      // 0f52: bipush 93
      // 0f54: sipush 313
      // 0f57: iastore
      // 0f58: aload 0
      // 0f59: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0f5c: bipush 34
      // 0f5e: aaload
      // 0f5f: bipush 10
      // 0f61: sipush 312
      // 0f64: iastore
      // 0f65: aload 0
      // 0f66: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0f69: bipush 32
      // 0f6b: aaload
      // 0f6c: bipush 53
      // 0f6e: sipush 311
      // 0f71: iastore
      // 0f72: aload 0
      // 0f73: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0f76: bipush 43
      // 0f78: aaload
      // 0f79: bipush 65
      // 0f7b: sipush 310
      // 0f7e: iastore
      // 0f7f: aload 0
      // 0f80: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0f83: bipush 28
      // 0f85: aaload
      // 0f86: bipush 7
      // 0f88: sipush 309
      // 0f8b: iastore
      // 0f8c: aload 0
      // 0f8d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0f90: bipush 35
      // 0f92: aaload
      // 0f93: bipush 46
      // 0f95: sipush 308
      // 0f98: iastore
      // 0f99: aload 0
      // 0f9a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0f9d: bipush 21
      // 0f9f: aaload
      // 0fa0: bipush 39
      // 0fa2: sipush 307
      // 0fa5: iastore
      // 0fa6: aload 0
      // 0fa7: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0faa: bipush 44
      // 0fac: aaload
      // 0fad: bipush 18
      // 0faf: sipush 306
      // 0fb2: iastore
      // 0fb3: aload 0
      // 0fb4: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0fb7: bipush 40
      // 0fb9: aaload
      // 0fba: bipush 10
      // 0fbc: sipush 305
      // 0fbf: iastore
      // 0fc0: aload 0
      // 0fc1: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0fc4: bipush 54
      // 0fc6: aaload
      // 0fc7: bipush 53
      // 0fc9: sipush 304
      // 0fcc: iastore
      // 0fcd: aload 0
      // 0fce: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0fd1: bipush 38
      // 0fd3: aaload
      // 0fd4: bipush 74
      // 0fd6: sipush 303
      // 0fd9: iastore
      // 0fda: aload 0
      // 0fdb: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0fde: bipush 28
      // 0fe0: aaload
      // 0fe1: bipush 26
      // 0fe3: sipush 302
      // 0fe6: iastore
      // 0fe7: aload 0
      // 0fe8: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0feb: bipush 15
      // 0fed: aaload
      // 0fee: bipush 13
      // 0ff0: sipush 301
      // 0ff3: iastore
      // 0ff4: aload 0
      // 0ff5: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 0ff8: bipush 39
      // 0ffa: aaload
      // 0ffb: bipush 34
      // 0ffd: sipush 300
      // 1000: iastore
      // 1001: aload 0
      // 1002: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1005: bipush 39
      // 1007: aaload
      // 1008: bipush 46
      // 100a: sipush 299
      // 100d: iastore
      // 100e: aload 0
      // 100f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1012: bipush 42
      // 1014: aaload
      // 1015: bipush 66
      // 1017: sipush 298
      // 101a: iastore
      // 101b: aload 0
      // 101c: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 101f: bipush 33
      // 1021: aaload
      // 1022: bipush 58
      // 1024: sipush 297
      // 1027: iastore
      // 1028: aload 0
      // 1029: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 102c: bipush 15
      // 102e: aaload
      // 102f: bipush 56
      // 1031: sipush 296
      // 1034: iastore
      // 1035: aload 0
      // 1036: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1039: bipush 18
      // 103b: aaload
      // 103c: bipush 51
      // 103e: sipush 295
      // 1041: iastore
      // 1042: aload 0
      // 1043: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1046: bipush 49
      // 1048: aaload
      // 1049: bipush 68
      // 104b: sipush 294
      // 104e: iastore
      // 104f: aload 0
      // 1050: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1053: bipush 30
      // 1055: aaload
      // 1056: bipush 37
      // 1058: sipush 293
      // 105b: iastore
      // 105c: aload 0
      // 105d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1060: bipush 51
      // 1062: aaload
      // 1063: bipush 84
      // 1065: sipush 292
      // 1068: iastore
      // 1069: aload 0
      // 106a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 106d: bipush 51
      // 106f: aaload
      // 1070: bipush 9
      // 1072: sipush 291
      // 1075: iastore
      // 1076: aload 0
      // 1077: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 107a: bipush 40
      // 107c: aaload
      // 107d: bipush 70
      // 107f: sipush 290
      // 1082: iastore
      // 1083: aload 0
      // 1084: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1087: bipush 41
      // 1089: aaload
      // 108a: bipush 84
      // 108c: sipush 289
      // 108f: iastore
      // 1090: aload 0
      // 1091: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1094: bipush 28
      // 1096: aaload
      // 1097: bipush 64
      // 1099: sipush 288
      // 109c: iastore
      // 109d: aload 0
      // 109e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 10a1: bipush 32
      // 10a3: aaload
      // 10a4: bipush 88
      // 10a6: sipush 287
      // 10a9: iastore
      // 10aa: aload 0
      // 10ab: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 10ae: bipush 24
      // 10b0: aaload
      // 10b1: bipush 5
      // 10b2: sipush 286
      // 10b5: iastore
      // 10b6: aload 0
      // 10b7: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 10ba: bipush 53
      // 10bc: aaload
      // 10bd: bipush 23
      // 10bf: sipush 285
      // 10c2: iastore
      // 10c3: aload 0
      // 10c4: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 10c7: bipush 42
      // 10c9: aaload
      // 10ca: bipush 27
      // 10cc: sipush 284
      // 10cf: iastore
      // 10d0: aload 0
      // 10d1: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 10d4: bipush 22
      // 10d6: aaload
      // 10d7: bipush 38
      // 10d9: sipush 283
      // 10dc: iastore
      // 10dd: aload 0
      // 10de: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 10e1: bipush 32
      // 10e3: aaload
      // 10e4: bipush 86
      // 10e6: sipush 282
      // 10e9: iastore
      // 10ea: aload 0
      // 10eb: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 10ee: bipush 34
      // 10f0: aaload
      // 10f1: bipush 30
      // 10f3: sipush 281
      // 10f6: iastore
      // 10f7: aload 0
      // 10f8: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 10fb: bipush 38
      // 10fd: aaload
      // 10fe: bipush 63
      // 1100: sipush 280
      // 1103: iastore
      // 1104: aload 0
      // 1105: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1108: bipush 24
      // 110a: aaload
      // 110b: bipush 59
      // 110d: sipush 279
      // 1110: iastore
      // 1111: aload 0
      // 1112: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1115: bipush 22
      // 1117: aaload
      // 1118: bipush 81
      // 111a: sipush 278
      // 111d: iastore
      // 111e: aload 0
      // 111f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1122: bipush 32
      // 1124: aaload
      // 1125: bipush 11
      // 1127: sipush 277
      // 112a: iastore
      // 112b: aload 0
      // 112c: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 112f: bipush 51
      // 1131: aaload
      // 1132: bipush 21
      // 1134: sipush 276
      // 1137: iastore
      // 1138: aload 0
      // 1139: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 113c: bipush 54
      // 113e: aaload
      // 113f: bipush 41
      // 1141: sipush 275
      // 1144: iastore
      // 1145: aload 0
      // 1146: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1149: bipush 21
      // 114b: aaload
      // 114c: bipush 50
      // 114e: sipush 274
      // 1151: iastore
      // 1152: aload 0
      // 1153: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1156: bipush 23
      // 1158: aaload
      // 1159: bipush 89
      // 115b: sipush 273
      // 115e: iastore
      // 115f: aload 0
      // 1160: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1163: bipush 19
      // 1165: aaload
      // 1166: bipush 87
      // 1168: sipush 272
      // 116b: iastore
      // 116c: aload 0
      // 116d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1170: bipush 26
      // 1172: aaload
      // 1173: bipush 7
      // 1175: sipush 271
      // 1178: iastore
      // 1179: aload 0
      // 117a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 117d: bipush 30
      // 117f: aaload
      // 1180: bipush 75
      // 1182: sipush 270
      // 1185: iastore
      // 1186: aload 0
      // 1187: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 118a: bipush 43
      // 118c: aaload
      // 118d: bipush 84
      // 118f: sipush 269
      // 1192: iastore
      // 1193: aload 0
      // 1194: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1197: bipush 51
      // 1199: aaload
      // 119a: bipush 25
      // 119c: sipush 268
      // 119f: iastore
      // 11a0: aload 0
      // 11a1: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 11a4: bipush 16
      // 11a6: aaload
      // 11a7: bipush 67
      // 11a9: sipush 267
      // 11ac: iastore
      // 11ad: aload 0
      // 11ae: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 11b1: bipush 32
      // 11b3: aaload
      // 11b4: bipush 9
      // 11b6: sipush 266
      // 11b9: iastore
      // 11ba: aload 0
      // 11bb: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 11be: bipush 48
      // 11c0: aaload
      // 11c1: bipush 51
      // 11c3: sipush 265
      // 11c6: iastore
      // 11c7: aload 0
      // 11c8: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 11cb: bipush 39
      // 11cd: aaload
      // 11ce: bipush 7
      // 11d0: sipush 264
      // 11d3: iastore
      // 11d4: aload 0
      // 11d5: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 11d8: bipush 44
      // 11da: aaload
      // 11db: bipush 88
      // 11dd: sipush 263
      // 11e0: iastore
      // 11e1: aload 0
      // 11e2: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 11e5: bipush 52
      // 11e7: aaload
      // 11e8: bipush 24
      // 11ea: sipush 262
      // 11ed: iastore
      // 11ee: aload 0
      // 11ef: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 11f2: bipush 23
      // 11f4: aaload
      // 11f5: bipush 34
      // 11f7: sipush 261
      // 11fa: iastore
      // 11fb: aload 0
      // 11fc: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 11ff: bipush 32
      // 1201: aaload
      // 1202: bipush 75
      // 1204: sipush 260
      // 1207: iastore
      // 1208: aload 0
      // 1209: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 120c: bipush 19
      // 120e: aaload
      // 120f: bipush 10
      // 1211: sipush 259
      // 1214: iastore
      // 1215: aload 0
      // 1216: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1219: bipush 28
      // 121b: aaload
      // 121c: bipush 91
      // 121e: sipush 258
      // 1221: iastore
      // 1222: aload 0
      // 1223: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1226: bipush 32
      // 1228: aaload
      // 1229: bipush 83
      // 122b: sipush 257
      // 122e: iastore
      // 122f: aload 0
      // 1230: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1233: bipush 25
      // 1235: aaload
      // 1236: bipush 75
      // 1238: sipush 256
      // 123b: iastore
      // 123c: aload 0
      // 123d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1240: bipush 53
      // 1242: aaload
      // 1243: bipush 45
      // 1245: sipush 255
      // 1248: iastore
      // 1249: aload 0
      // 124a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 124d: bipush 29
      // 124f: aaload
      // 1250: bipush 85
      // 1252: sipush 254
      // 1255: iastore
      // 1256: aload 0
      // 1257: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 125a: bipush 53
      // 125c: aaload
      // 125d: bipush 59
      // 125f: sipush 253
      // 1262: iastore
      // 1263: aload 0
      // 1264: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1267: bipush 16
      // 1269: aaload
      // 126a: bipush 2
      // 126b: sipush 252
      // 126e: iastore
      // 126f: aload 0
      // 1270: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1273: bipush 19
      // 1275: aaload
      // 1276: bipush 78
      // 1278: sipush 251
      // 127b: iastore
      // 127c: aload 0
      // 127d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1280: bipush 15
      // 1282: aaload
      // 1283: bipush 75
      // 1285: sipush 250
      // 1288: iastore
      // 1289: aload 0
      // 128a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 128d: bipush 51
      // 128f: aaload
      // 1290: bipush 42
      // 1292: sipush 249
      // 1295: iastore
      // 1296: aload 0
      // 1297: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 129a: bipush 45
      // 129c: aaload
      // 129d: bipush 67
      // 129f: sipush 248
      // 12a2: iastore
      // 12a3: aload 0
      // 12a4: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 12a7: bipush 15
      // 12a9: aaload
      // 12aa: bipush 74
      // 12ac: sipush 247
      // 12af: iastore
      // 12b0: aload 0
      // 12b1: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 12b4: bipush 25
      // 12b6: aaload
      // 12b7: bipush 81
      // 12b9: sipush 246
      // 12bc: iastore
      // 12bd: aload 0
      // 12be: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 12c1: bipush 37
      // 12c3: aaload
      // 12c4: bipush 62
      // 12c6: sipush 245
      // 12c9: iastore
      // 12ca: aload 0
      // 12cb: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 12ce: bipush 16
      // 12d0: aaload
      // 12d1: bipush 55
      // 12d3: sipush 244
      // 12d6: iastore
      // 12d7: aload 0
      // 12d8: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 12db: bipush 18
      // 12dd: aaload
      // 12de: bipush 38
      // 12e0: sipush 243
      // 12e3: iastore
      // 12e4: aload 0
      // 12e5: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 12e8: bipush 23
      // 12ea: aaload
      // 12eb: bipush 23
      // 12ed: sipush 242
      // 12f0: iastore
      // 12f1: aload 0
      // 12f2: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 12f5: bipush 38
      // 12f7: aaload
      // 12f8: bipush 30
      // 12fa: sipush 241
      // 12fd: iastore
      // 12fe: aload 0
      // 12ff: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1302: bipush 17
      // 1304: aaload
      // 1305: bipush 28
      // 1307: sipush 240
      // 130a: iastore
      // 130b: aload 0
      // 130c: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 130f: bipush 44
      // 1311: aaload
      // 1312: bipush 73
      // 1314: sipush 239
      // 1317: iastore
      // 1318: aload 0
      // 1319: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 131c: bipush 23
      // 131e: aaload
      // 131f: bipush 78
      // 1321: sipush 238
      // 1324: iastore
      // 1325: aload 0
      // 1326: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1329: bipush 40
      // 132b: aaload
      // 132c: bipush 77
      // 132e: sipush 237
      // 1331: iastore
      // 1332: aload 0
      // 1333: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1336: bipush 38
      // 1338: aaload
      // 1339: bipush 87
      // 133b: sipush 236
      // 133e: iastore
      // 133f: aload 0
      // 1340: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1343: bipush 27
      // 1345: aaload
      // 1346: bipush 19
      // 1348: sipush 235
      // 134b: iastore
      // 134c: aload 0
      // 134d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1350: bipush 38
      // 1352: aaload
      // 1353: bipush 82
      // 1355: sipush 234
      // 1358: iastore
      // 1359: aload 0
      // 135a: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 135d: bipush 37
      // 135f: aaload
      // 1360: bipush 22
      // 1362: sipush 233
      // 1365: iastore
      // 1366: aload 0
      // 1367: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 136a: bipush 41
      // 136c: aaload
      // 136d: bipush 30
      // 136f: sipush 232
      // 1372: iastore
      // 1373: aload 0
      // 1374: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1377: bipush 54
      // 1379: aaload
      // 137a: bipush 9
      // 137c: sipush 231
      // 137f: iastore
      // 1380: aload 0
      // 1381: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1384: bipush 32
      // 1386: aaload
      // 1387: bipush 30
      // 1389: sipush 230
      // 138c: iastore
      // 138d: aload 0
      // 138e: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1391: bipush 30
      // 1393: aaload
      // 1394: bipush 52
      // 1396: sipush 229
      // 1399: iastore
      // 139a: aload 0
      // 139b: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 139e: bipush 40
      // 13a0: aaload
      // 13a1: bipush 84
      // 13a3: sipush 228
      // 13a6: iastore
      // 13a7: aload 0
      // 13a8: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 13ab: bipush 53
      // 13ad: aaload
      // 13ae: bipush 57
      // 13b0: sipush 227
      // 13b3: iastore
      // 13b4: aload 0
      // 13b5: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 13b8: bipush 27
      // 13ba: aaload
      // 13bb: bipush 27
      // 13bd: sipush 226
      // 13c0: iastore
      // 13c1: aload 0
      // 13c2: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 13c5: bipush 38
      // 13c7: aaload
      // 13c8: bipush 64
      // 13ca: sipush 225
      // 13cd: iastore
      // 13ce: aload 0
      // 13cf: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 13d2: bipush 18
      // 13d4: aaload
      // 13d5: bipush 43
      // 13d7: sipush 224
      // 13da: iastore
      // 13db: aload 0
      // 13dc: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 13df: bipush 23
      // 13e1: aaload
      // 13e2: bipush 69
      // 13e4: sipush 223
      // 13e7: iastore
      // 13e8: aload 0
      // 13e9: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 13ec: bipush 28
      // 13ee: aaload
      // 13ef: bipush 12
      // 13f1: sipush 222
      // 13f4: iastore
      // 13f5: aload 0
      // 13f6: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 13f9: bipush 50
      // 13fb: aaload
      // 13fc: bipush 78
      // 13fe: sipush 221
      // 1401: iastore
      // 1402: aload 0
      // 1403: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1406: bipush 50
      // 1408: aaload
      // 1409: bipush 1
      // 140a: sipush 220
      // 140d: iastore
      // 140e: aload 0
      // 140f: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1412: bipush 26
      // 1414: aaload
      // 1415: bipush 88
      // 1417: sipush 219
      // 141a: iastore
      // 141b: aload 0
      // 141c: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 141f: bipush 36
      // 1421: aaload
      // 1422: bipush 40
      // 1424: sipush 218
      // 1427: iastore
      // 1428: aload 0
      // 1429: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 142c: bipush 33
      // 142e: aaload
      // 142f: bipush 89
      // 1431: sipush 217
      // 1434: iastore
      // 1435: aload 0
      // 1436: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1439: bipush 41
      // 143b: aaload
      // 143c: bipush 28
      // 143e: sipush 216
      // 1441: iastore
      // 1442: aload 0
      // 1443: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1446: bipush 31
      // 1448: aaload
      // 1449: bipush 77
      // 144b: sipush 215
      // 144e: iastore
      // 144f: aload 0
      // 1450: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1453: bipush 46
      // 1455: aaload
      // 1456: bipush 1
      // 1457: sipush 214
      // 145a: iastore
      // 145b: aload 0
      // 145c: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 145f: bipush 47
      // 1461: aaload
      // 1462: bipush 19
      // 1464: sipush 213
      // 1467: iastore
      // 1468: aload 0
      // 1469: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 146c: bipush 35
      // 146e: aaload
      // 146f: bipush 55
      // 1471: sipush 212
      // 1474: iastore
      // 1475: aload 0
      // 1476: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1479: bipush 41
      // 147b: aaload
      // 147c: bipush 21
      // 147e: sipush 211
      // 1481: iastore
      // 1482: aload 0
      // 1483: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1486: bipush 27
      // 1488: aaload
      // 1489: bipush 10
      // 148b: sipush 210
      // 148e: iastore
      // 148f: aload 0
      // 1490: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1493: bipush 32
      // 1495: aaload
      // 1496: bipush 77
      // 1498: sipush 209
      // 149b: iastore
      // 149c: aload 0
      // 149d: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 14a0: bipush 26
      // 14a2: aaload
      // 14a3: bipush 37
      // 14a5: sipush 208
      // 14a8: iastore
      // 14a9: aload 0
      // 14aa: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 14ad: bipush 20
      // 14af: aaload
      // 14b0: bipush 33
      // 14b2: sipush 207
      // 14b5: iastore
      // 14b6: aload 0
      // 14b7: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 14ba: bipush 41
      // 14bc: aaload
      // 14bd: bipush 52
      // 14bf: sipush 206
      // 14c2: iastore
      // 14c3: aload 0
      // 14c4: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 14c7: bipush 32
      // 14c9: aaload
      // 14ca: bipush 18
      // 14cc: sipush 205
      // 14cf: iastore
      // 14d0: aload 0
      // 14d1: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 14d4: bipush 38
      // 14d6: aaload
      // 14d7: bipush 13
      // 14d9: sipush 204
      // 14dc: iastore
      // 14dd: aload 0
      // 14de: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 14e1: bipush 20
      // 14e3: aaload
      // 14e4: bipush 18
      // 14e6: sipush 203
      // 14e9: iastore
      // 14ea: aload 0
      // 14eb: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 14ee: bipush 20
      // 14f0: aaload
      // 14f1: bipush 24
      // 14f3: sipush 202
      // 14f6: iastore
      // 14f7: aload 0
      // 14f8: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 14fb: bipush 45
      // 14fd: aaload
      // 14fe: bipush 19
      // 1500: sipush 201
      // 1503: iastore
      // 1504: aload 0
      // 1505: getfield io/legado/app/help/BytesEncodingDetect.GBFreq [[I
      // 1508: bipush 18
      // 150a: aaload
      // 150b: bipush 53
      // 150d: sipush 200
      // 1510: iastore
      // 1511: aload 0
      // 1512: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1515: bipush 9
      // 1517: aaload
      // 1518: bipush 89
      // 151a: sipush 600
      // 151d: iastore
      // 151e: aload 0
      // 151f: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1522: bipush 11
      // 1524: aaload
      // 1525: bipush 15
      // 1527: sipush 599
      // 152a: iastore
      // 152b: aload 0
      // 152c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 152f: bipush 3
      // 1530: aaload
      // 1531: bipush 66
      // 1533: sipush 598
      // 1536: iastore
      // 1537: aload 0
      // 1538: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 153b: bipush 6
      // 153d: aaload
      // 153e: bipush 121
      // 1540: sipush 597
      // 1543: iastore
      // 1544: aload 0
      // 1545: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1548: bipush 3
      // 1549: aaload
      // 154a: bipush 0
      // 154b: sipush 596
      // 154e: iastore
      // 154f: aload 0
      // 1550: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1553: bipush 5
      // 1554: aaload
      // 1555: bipush 82
      // 1557: sipush 595
      // 155a: iastore
      // 155b: aload 0
      // 155c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 155f: bipush 3
      // 1560: aaload
      // 1561: bipush 42
      // 1563: sipush 594
      // 1566: iastore
      // 1567: aload 0
      // 1568: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 156b: bipush 5
      // 156c: aaload
      // 156d: bipush 34
      // 156f: sipush 593
      // 1572: iastore
      // 1573: aload 0
      // 1574: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1577: bipush 3
      // 1578: aaload
      // 1579: bipush 8
      // 157b: sipush 592
      // 157e: iastore
      // 157f: aload 0
      // 1580: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1583: bipush 3
      // 1584: aaload
      // 1585: bipush 6
      // 1587: sipush 591
      // 158a: iastore
      // 158b: aload 0
      // 158c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 158f: bipush 3
      // 1590: aaload
      // 1591: bipush 67
      // 1593: sipush 590
      // 1596: iastore
      // 1597: aload 0
      // 1598: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 159b: bipush 7
      // 159d: aaload
      // 159e: sipush 139
      // 15a1: sipush 589
      // 15a4: iastore
      // 15a5: aload 0
      // 15a6: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 15a9: bipush 23
      // 15ab: aaload
      // 15ac: sipush 137
      // 15af: sipush 588
      // 15b2: iastore
      // 15b3: aload 0
      // 15b4: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 15b7: bipush 12
      // 15b9: aaload
      // 15ba: bipush 46
      // 15bc: sipush 587
      // 15bf: iastore
      // 15c0: aload 0
      // 15c1: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 15c4: bipush 4
      // 15c5: aaload
      // 15c6: bipush 8
      // 15c8: sipush 586
      // 15cb: iastore
      // 15cc: aload 0
      // 15cd: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 15d0: bipush 4
      // 15d1: aaload
      // 15d2: bipush 41
      // 15d4: sipush 585
      // 15d7: iastore
      // 15d8: aload 0
      // 15d9: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 15dc: bipush 18
      // 15de: aaload
      // 15df: bipush 47
      // 15e1: sipush 584
      // 15e4: iastore
      // 15e5: aload 0
      // 15e6: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 15e9: bipush 12
      // 15eb: aaload
      // 15ec: bipush 114
      // 15ee: sipush 583
      // 15f1: iastore
      // 15f2: aload 0
      // 15f3: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 15f6: bipush 6
      // 15f8: aaload
      // 15f9: bipush 1
      // 15fa: sipush 582
      // 15fd: iastore
      // 15fe: aload 0
      // 15ff: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1602: bipush 22
      // 1604: aaload
      // 1605: bipush 60
      // 1607: sipush 581
      // 160a: iastore
      // 160b: aload 0
      // 160c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 160f: bipush 5
      // 1610: aaload
      // 1611: bipush 46
      // 1613: sipush 580
      // 1616: iastore
      // 1617: aload 0
      // 1618: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 161b: bipush 11
      // 161d: aaload
      // 161e: bipush 79
      // 1620: sipush 579
      // 1623: iastore
      // 1624: aload 0
      // 1625: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1628: bipush 3
      // 1629: aaload
      // 162a: bipush 23
      // 162c: sipush 578
      // 162f: iastore
      // 1630: aload 0
      // 1631: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1634: bipush 7
      // 1636: aaload
      // 1637: bipush 114
      // 1639: sipush 577
      // 163c: iastore
      // 163d: aload 0
      // 163e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1641: bipush 29
      // 1643: aaload
      // 1644: bipush 102
      // 1646: sipush 576
      // 1649: iastore
      // 164a: aload 0
      // 164b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 164e: bipush 19
      // 1650: aaload
      // 1651: bipush 14
      // 1653: sipush 575
      // 1656: iastore
      // 1657: aload 0
      // 1658: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 165b: bipush 4
      // 165c: aaload
      // 165d: sipush 133
      // 1660: sipush 574
      // 1663: iastore
      // 1664: aload 0
      // 1665: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1668: bipush 3
      // 1669: aaload
      // 166a: bipush 29
      // 166c: sipush 573
      // 166f: iastore
      // 1670: aload 0
      // 1671: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1674: bipush 4
      // 1675: aaload
      // 1676: bipush 109
      // 1678: sipush 572
      // 167b: iastore
      // 167c: aload 0
      // 167d: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1680: bipush 14
      // 1682: aaload
      // 1683: bipush 127
      // 1685: sipush 571
      // 1688: iastore
      // 1689: aload 0
      // 168a: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 168d: bipush 5
      // 168e: aaload
      // 168f: bipush 48
      // 1691: sipush 570
      // 1694: iastore
      // 1695: aload 0
      // 1696: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1699: bipush 13
      // 169b: aaload
      // 169c: bipush 104
      // 169e: sipush 569
      // 16a1: iastore
      // 16a2: aload 0
      // 16a3: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 16a6: bipush 3
      // 16a7: aaload
      // 16a8: sipush 132
      // 16ab: sipush 568
      // 16ae: iastore
      // 16af: aload 0
      // 16b0: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 16b3: bipush 26
      // 16b5: aaload
      // 16b6: bipush 64
      // 16b8: sipush 567
      // 16bb: iastore
      // 16bc: aload 0
      // 16bd: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 16c0: bipush 7
      // 16c2: aaload
      // 16c3: bipush 19
      // 16c5: sipush 566
      // 16c8: iastore
      // 16c9: aload 0
      // 16ca: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 16cd: bipush 4
      // 16ce: aaload
      // 16cf: bipush 12
      // 16d1: sipush 565
      // 16d4: iastore
      // 16d5: aload 0
      // 16d6: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 16d9: bipush 11
      // 16db: aaload
      // 16dc: bipush 124
      // 16de: sipush 564
      // 16e1: iastore
      // 16e2: aload 0
      // 16e3: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 16e6: bipush 7
      // 16e8: aaload
      // 16e9: bipush 89
      // 16eb: sipush 563
      // 16ee: iastore
      // 16ef: aload 0
      // 16f0: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 16f3: bipush 15
      // 16f5: aaload
      // 16f6: bipush 124
      // 16f8: sipush 562
      // 16fb: iastore
      // 16fc: aload 0
      // 16fd: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1700: bipush 4
      // 1701: aaload
      // 1702: bipush 108
      // 1704: sipush 561
      // 1707: iastore
      // 1708: aload 0
      // 1709: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 170c: bipush 19
      // 170e: aaload
      // 170f: bipush 66
      // 1711: sipush 560
      // 1714: iastore
      // 1715: aload 0
      // 1716: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1719: bipush 3
      // 171a: aaload
      // 171b: bipush 21
      // 171d: sipush 559
      // 1720: iastore
      // 1721: aload 0
      // 1722: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1725: bipush 24
      // 1727: aaload
      // 1728: bipush 12
      // 172a: sipush 558
      // 172d: iastore
      // 172e: aload 0
      // 172f: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1732: bipush 28
      // 1734: aaload
      // 1735: bipush 111
      // 1737: sipush 557
      // 173a: iastore
      // 173b: aload 0
      // 173c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 173f: bipush 12
      // 1741: aaload
      // 1742: bipush 107
      // 1744: sipush 556
      // 1747: iastore
      // 1748: aload 0
      // 1749: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 174c: bipush 3
      // 174d: aaload
      // 174e: bipush 112
      // 1750: sipush 555
      // 1753: iastore
      // 1754: aload 0
      // 1755: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1758: bipush 8
      // 175a: aaload
      // 175b: bipush 113
      // 175d: sipush 554
      // 1760: iastore
      // 1761: aload 0
      // 1762: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1765: bipush 5
      // 1766: aaload
      // 1767: bipush 40
      // 1769: sipush 553
      // 176c: iastore
      // 176d: aload 0
      // 176e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1771: bipush 26
      // 1773: aaload
      // 1774: sipush 145
      // 1777: sipush 552
      // 177a: iastore
      // 177b: aload 0
      // 177c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 177f: bipush 3
      // 1780: aaload
      // 1781: bipush 48
      // 1783: sipush 551
      // 1786: iastore
      // 1787: aload 0
      // 1788: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 178b: bipush 3
      // 178c: aaload
      // 178d: bipush 70
      // 178f: sipush 550
      // 1792: iastore
      // 1793: aload 0
      // 1794: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1797: bipush 22
      // 1799: aaload
      // 179a: bipush 17
      // 179c: sipush 549
      // 179f: iastore
      // 17a0: aload 0
      // 17a1: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 17a4: bipush 16
      // 17a6: aaload
      // 17a7: bipush 47
      // 17a9: sipush 548
      // 17ac: iastore
      // 17ad: aload 0
      // 17ae: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 17b1: bipush 3
      // 17b2: aaload
      // 17b3: bipush 53
      // 17b5: sipush 547
      // 17b8: iastore
      // 17b9: aload 0
      // 17ba: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 17bd: bipush 4
      // 17be: aaload
      // 17bf: bipush 24
      // 17c1: sipush 546
      // 17c4: iastore
      // 17c5: aload 0
      // 17c6: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 17c9: bipush 32
      // 17cb: aaload
      // 17cc: bipush 120
      // 17ce: sipush 545
      // 17d1: iastore
      // 17d2: aload 0
      // 17d3: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 17d6: bipush 24
      // 17d8: aaload
      // 17d9: bipush 49
      // 17db: sipush 544
      // 17de: iastore
      // 17df: aload 0
      // 17e0: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 17e3: bipush 24
      // 17e5: aaload
      // 17e6: sipush 142
      // 17e9: sipush 543
      // 17ec: iastore
      // 17ed: aload 0
      // 17ee: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 17f1: bipush 18
      // 17f3: aaload
      // 17f4: bipush 66
      // 17f6: sipush 542
      // 17f9: iastore
      // 17fa: aload 0
      // 17fb: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 17fe: bipush 29
      // 1800: aaload
      // 1801: sipush 150
      // 1804: sipush 541
      // 1807: iastore
      // 1808: aload 0
      // 1809: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 180c: bipush 5
      // 180d: aaload
      // 180e: bipush 122
      // 1810: sipush 540
      // 1813: iastore
      // 1814: aload 0
      // 1815: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1818: bipush 5
      // 1819: aaload
      // 181a: bipush 114
      // 181c: sipush 539
      // 181f: iastore
      // 1820: aload 0
      // 1821: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1824: bipush 3
      // 1825: aaload
      // 1826: bipush 44
      // 1828: sipush 538
      // 182b: iastore
      // 182c: aload 0
      // 182d: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1830: bipush 10
      // 1832: aaload
      // 1833: sipush 128
      // 1836: sipush 537
      // 1839: iastore
      // 183a: aload 0
      // 183b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 183e: bipush 15
      // 1840: aaload
      // 1841: bipush 20
      // 1843: sipush 536
      // 1846: iastore
      // 1847: aload 0
      // 1848: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 184b: bipush 13
      // 184d: aaload
      // 184e: bipush 33
      // 1850: sipush 535
      // 1853: iastore
      // 1854: aload 0
      // 1855: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1858: bipush 14
      // 185a: aaload
      // 185b: bipush 87
      // 185d: sipush 534
      // 1860: iastore
      // 1861: aload 0
      // 1862: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1865: bipush 3
      // 1866: aaload
      // 1867: bipush 126
      // 1869: sipush 533
      // 186c: iastore
      // 186d: aload 0
      // 186e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1871: bipush 4
      // 1872: aaload
      // 1873: bipush 53
      // 1875: sipush 532
      // 1878: iastore
      // 1879: aload 0
      // 187a: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 187d: bipush 4
      // 187e: aaload
      // 187f: bipush 40
      // 1881: sipush 531
      // 1884: iastore
      // 1885: aload 0
      // 1886: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1889: bipush 9
      // 188b: aaload
      // 188c: bipush 93
      // 188e: sipush 530
      // 1891: iastore
      // 1892: aload 0
      // 1893: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1896: bipush 15
      // 1898: aaload
      // 1899: sipush 137
      // 189c: sipush 529
      // 189f: iastore
      // 18a0: aload 0
      // 18a1: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 18a4: bipush 10
      // 18a6: aaload
      // 18a7: bipush 123
      // 18a9: sipush 528
      // 18ac: iastore
      // 18ad: aload 0
      // 18ae: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 18b1: bipush 4
      // 18b2: aaload
      // 18b3: bipush 56
      // 18b5: sipush 527
      // 18b8: iastore
      // 18b9: aload 0
      // 18ba: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 18bd: bipush 5
      // 18be: aaload
      // 18bf: bipush 71
      // 18c1: sipush 526
      // 18c4: iastore
      // 18c5: aload 0
      // 18c6: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 18c9: bipush 10
      // 18cb: aaload
      // 18cc: bipush 8
      // 18ce: sipush 525
      // 18d1: iastore
      // 18d2: aload 0
      // 18d3: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 18d6: bipush 5
      // 18d7: aaload
      // 18d8: bipush 16
      // 18da: sipush 524
      // 18dd: iastore
      // 18de: aload 0
      // 18df: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 18e2: bipush 5
      // 18e3: aaload
      // 18e4: sipush 146
      // 18e7: sipush 523
      // 18ea: iastore
      // 18eb: aload 0
      // 18ec: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 18ef: bipush 18
      // 18f1: aaload
      // 18f2: bipush 88
      // 18f4: sipush 522
      // 18f7: iastore
      // 18f8: aload 0
      // 18f9: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 18fc: bipush 24
      // 18fe: aaload
      // 18ff: bipush 4
      // 1900: sipush 521
      // 1903: iastore
      // 1904: aload 0
      // 1905: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1908: bipush 20
      // 190a: aaload
      // 190b: bipush 47
      // 190d: sipush 520
      // 1910: iastore
      // 1911: aload 0
      // 1912: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1915: bipush 5
      // 1916: aaload
      // 1917: bipush 33
      // 1919: sipush 519
      // 191c: iastore
      // 191d: aload 0
      // 191e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1921: bipush 9
      // 1923: aaload
      // 1924: bipush 43
      // 1926: sipush 518
      // 1929: iastore
      // 192a: aload 0
      // 192b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 192e: bipush 20
      // 1930: aaload
      // 1931: bipush 12
      // 1933: sipush 517
      // 1936: iastore
      // 1937: aload 0
      // 1938: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 193b: bipush 20
      // 193d: aaload
      // 193e: bipush 13
      // 1940: sipush 516
      // 1943: iastore
      // 1944: aload 0
      // 1945: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1948: bipush 5
      // 1949: aaload
      // 194a: sipush 156
      // 194d: sipush 515
      // 1950: iastore
      // 1951: aload 0
      // 1952: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1955: bipush 22
      // 1957: aaload
      // 1958: sipush 140
      // 195b: sipush 514
      // 195e: iastore
      // 195f: aload 0
      // 1960: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1963: bipush 8
      // 1965: aaload
      // 1966: sipush 146
      // 1969: sipush 513
      // 196c: iastore
      // 196d: aload 0
      // 196e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1971: bipush 21
      // 1973: aaload
      // 1974: bipush 123
      // 1976: sipush 512
      // 1979: iastore
      // 197a: aload 0
      // 197b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 197e: bipush 4
      // 197f: aaload
      // 1980: bipush 90
      // 1982: sipush 511
      // 1985: iastore
      // 1986: aload 0
      // 1987: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 198a: bipush 5
      // 198b: aaload
      // 198c: bipush 62
      // 198e: sipush 510
      // 1991: iastore
      // 1992: aload 0
      // 1993: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1996: bipush 17
      // 1998: aaload
      // 1999: bipush 59
      // 199b: sipush 509
      // 199e: iastore
      // 199f: aload 0
      // 19a0: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 19a3: bipush 10
      // 19a5: aaload
      // 19a6: bipush 37
      // 19a8: sipush 508
      // 19ab: iastore
      // 19ac: aload 0
      // 19ad: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 19b0: bipush 18
      // 19b2: aaload
      // 19b3: bipush 107
      // 19b5: sipush 507
      // 19b8: iastore
      // 19b9: aload 0
      // 19ba: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 19bd: bipush 14
      // 19bf: aaload
      // 19c0: bipush 53
      // 19c2: sipush 506
      // 19c5: iastore
      // 19c6: aload 0
      // 19c7: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 19ca: bipush 22
      // 19cc: aaload
      // 19cd: bipush 51
      // 19cf: sipush 505
      // 19d2: iastore
      // 19d3: aload 0
      // 19d4: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 19d7: bipush 8
      // 19d9: aaload
      // 19da: bipush 13
      // 19dc: sipush 504
      // 19df: iastore
      // 19e0: aload 0
      // 19e1: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 19e4: bipush 5
      // 19e5: aaload
      // 19e6: bipush 29
      // 19e8: sipush 503
      // 19eb: iastore
      // 19ec: aload 0
      // 19ed: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 19f0: bipush 9
      // 19f2: aaload
      // 19f3: bipush 7
      // 19f5: sipush 502
      // 19f8: iastore
      // 19f9: aload 0
      // 19fa: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 19fd: bipush 22
      // 19ff: aaload
      // 1a00: bipush 14
      // 1a02: sipush 501
      // 1a05: iastore
      // 1a06: aload 0
      // 1a07: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1a0a: bipush 8
      // 1a0c: aaload
      // 1a0d: bipush 55
      // 1a0f: sipush 500
      // 1a12: iastore
      // 1a13: aload 0
      // 1a14: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1a17: bipush 33
      // 1a19: aaload
      // 1a1a: bipush 9
      // 1a1c: sipush 499
      // 1a1f: iastore
      // 1a20: aload 0
      // 1a21: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1a24: bipush 16
      // 1a26: aaload
      // 1a27: bipush 64
      // 1a29: sipush 498
      // 1a2c: iastore
      // 1a2d: aload 0
      // 1a2e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1a31: bipush 7
      // 1a33: aaload
      // 1a34: sipush 131
      // 1a37: sipush 497
      // 1a3a: iastore
      // 1a3b: aload 0
      // 1a3c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1a3f: bipush 34
      // 1a41: aaload
      // 1a42: bipush 4
      // 1a43: sipush 496
      // 1a46: iastore
      // 1a47: aload 0
      // 1a48: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1a4b: bipush 7
      // 1a4d: aaload
      // 1a4e: bipush 101
      // 1a50: sipush 495
      // 1a53: iastore
      // 1a54: aload 0
      // 1a55: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1a58: bipush 11
      // 1a5a: aaload
      // 1a5b: sipush 139
      // 1a5e: sipush 494
      // 1a61: iastore
      // 1a62: aload 0
      // 1a63: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1a66: bipush 3
      // 1a67: aaload
      // 1a68: sipush 135
      // 1a6b: sipush 493
      // 1a6e: iastore
      // 1a6f: aload 0
      // 1a70: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1a73: bipush 7
      // 1a75: aaload
      // 1a76: bipush 102
      // 1a78: sipush 492
      // 1a7b: iastore
      // 1a7c: aload 0
      // 1a7d: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1a80: bipush 17
      // 1a82: aaload
      // 1a83: bipush 13
      // 1a85: sipush 491
      // 1a88: iastore
      // 1a89: aload 0
      // 1a8a: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1a8d: bipush 3
      // 1a8e: aaload
      // 1a8f: bipush 20
      // 1a91: sipush 490
      // 1a94: iastore
      // 1a95: aload 0
      // 1a96: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1a99: bipush 27
      // 1a9b: aaload
      // 1a9c: bipush 106
      // 1a9e: sipush 489
      // 1aa1: iastore
      // 1aa2: aload 0
      // 1aa3: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1aa6: bipush 5
      // 1aa7: aaload
      // 1aa8: bipush 88
      // 1aaa: sipush 488
      // 1aad: iastore
      // 1aae: aload 0
      // 1aaf: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1ab2: bipush 6
      // 1ab4: aaload
      // 1ab5: bipush 33
      // 1ab7: sipush 487
      // 1aba: iastore
      // 1abb: aload 0
      // 1abc: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1abf: bipush 5
      // 1ac0: aaload
      // 1ac1: sipush 139
      // 1ac4: sipush 486
      // 1ac7: iastore
      // 1ac8: aload 0
      // 1ac9: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1acc: bipush 6
      // 1ace: aaload
      // 1acf: bipush 0
      // 1ad0: sipush 485
      // 1ad3: iastore
      // 1ad4: aload 0
      // 1ad5: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1ad8: bipush 17
      // 1ada: aaload
      // 1adb: bipush 58
      // 1add: sipush 484
      // 1ae0: iastore
      // 1ae1: aload 0
      // 1ae2: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1ae5: bipush 5
      // 1ae6: aaload
      // 1ae7: sipush 133
      // 1aea: sipush 483
      // 1aed: iastore
      // 1aee: aload 0
      // 1aef: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1af2: bipush 9
      // 1af4: aaload
      // 1af5: bipush 107
      // 1af7: sipush 482
      // 1afa: iastore
      // 1afb: aload 0
      // 1afc: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1aff: bipush 23
      // 1b01: aaload
      // 1b02: bipush 39
      // 1b04: sipush 481
      // 1b07: iastore
      // 1b08: aload 0
      // 1b09: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1b0c: bipush 5
      // 1b0d: aaload
      // 1b0e: bipush 23
      // 1b10: sipush 480
      // 1b13: iastore
      // 1b14: aload 0
      // 1b15: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1b18: bipush 3
      // 1b19: aaload
      // 1b1a: bipush 79
      // 1b1c: sipush 479
      // 1b1f: iastore
      // 1b20: aload 0
      // 1b21: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1b24: bipush 32
      // 1b26: aaload
      // 1b27: bipush 97
      // 1b29: sipush 478
      // 1b2c: iastore
      // 1b2d: aload 0
      // 1b2e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1b31: bipush 3
      // 1b32: aaload
      // 1b33: sipush 136
      // 1b36: sipush 477
      // 1b39: iastore
      // 1b3a: aload 0
      // 1b3b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1b3e: bipush 4
      // 1b3f: aaload
      // 1b40: bipush 94
      // 1b42: sipush 476
      // 1b45: iastore
      // 1b46: aload 0
      // 1b47: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1b4a: bipush 21
      // 1b4c: aaload
      // 1b4d: bipush 61
      // 1b4f: sipush 475
      // 1b52: iastore
      // 1b53: aload 0
      // 1b54: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1b57: bipush 23
      // 1b59: aaload
      // 1b5a: bipush 123
      // 1b5c: sipush 474
      // 1b5f: iastore
      // 1b60: aload 0
      // 1b61: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1b64: bipush 26
      // 1b66: aaload
      // 1b67: bipush 16
      // 1b69: sipush 473
      // 1b6c: iastore
      // 1b6d: aload 0
      // 1b6e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1b71: bipush 24
      // 1b73: aaload
      // 1b74: sipush 137
      // 1b77: sipush 472
      // 1b7a: iastore
      // 1b7b: aload 0
      // 1b7c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1b7f: bipush 22
      // 1b81: aaload
      // 1b82: bipush 18
      // 1b84: sipush 471
      // 1b87: iastore
      // 1b88: aload 0
      // 1b89: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1b8c: bipush 5
      // 1b8d: aaload
      // 1b8e: bipush 1
      // 1b8f: sipush 470
      // 1b92: iastore
      // 1b93: aload 0
      // 1b94: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1b97: bipush 20
      // 1b99: aaload
      // 1b9a: bipush 119
      // 1b9c: sipush 469
      // 1b9f: iastore
      // 1ba0: aload 0
      // 1ba1: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1ba4: bipush 3
      // 1ba5: aaload
      // 1ba6: bipush 7
      // 1ba8: sipush 468
      // 1bab: iastore
      // 1bac: aload 0
      // 1bad: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1bb0: bipush 10
      // 1bb2: aaload
      // 1bb3: bipush 79
      // 1bb5: sipush 467
      // 1bb8: iastore
      // 1bb9: aload 0
      // 1bba: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1bbd: bipush 15
      // 1bbf: aaload
      // 1bc0: bipush 105
      // 1bc2: sipush 466
      // 1bc5: iastore
      // 1bc6: aload 0
      // 1bc7: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1bca: bipush 3
      // 1bcb: aaload
      // 1bcc: sipush 144
      // 1bcf: sipush 465
      // 1bd2: iastore
      // 1bd3: aload 0
      // 1bd4: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1bd7: bipush 12
      // 1bd9: aaload
      // 1bda: bipush 80
      // 1bdc: sipush 464
      // 1bdf: iastore
      // 1be0: aload 0
      // 1be1: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1be4: bipush 15
      // 1be6: aaload
      // 1be7: bipush 73
      // 1be9: sipush 463
      // 1bec: iastore
      // 1bed: aload 0
      // 1bee: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1bf1: bipush 3
      // 1bf2: aaload
      // 1bf3: bipush 19
      // 1bf5: sipush 462
      // 1bf8: iastore
      // 1bf9: aload 0
      // 1bfa: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1bfd: bipush 8
      // 1bff: aaload
      // 1c00: bipush 109
      // 1c02: sipush 461
      // 1c05: iastore
      // 1c06: aload 0
      // 1c07: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1c0a: bipush 3
      // 1c0b: aaload
      // 1c0c: bipush 15
      // 1c0e: sipush 460
      // 1c11: iastore
      // 1c12: aload 0
      // 1c13: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1c16: bipush 31
      // 1c18: aaload
      // 1c19: bipush 82
      // 1c1b: sipush 459
      // 1c1e: iastore
      // 1c1f: aload 0
      // 1c20: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1c23: bipush 3
      // 1c24: aaload
      // 1c25: bipush 43
      // 1c27: sipush 458
      // 1c2a: iastore
      // 1c2b: aload 0
      // 1c2c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1c2f: bipush 25
      // 1c31: aaload
      // 1c32: bipush 119
      // 1c34: sipush 457
      // 1c37: iastore
      // 1c38: aload 0
      // 1c39: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1c3c: bipush 16
      // 1c3e: aaload
      // 1c3f: bipush 111
      // 1c41: sipush 456
      // 1c44: iastore
      // 1c45: aload 0
      // 1c46: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1c49: bipush 7
      // 1c4b: aaload
      // 1c4c: bipush 77
      // 1c4e: sipush 455
      // 1c51: iastore
      // 1c52: aload 0
      // 1c53: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1c56: bipush 3
      // 1c57: aaload
      // 1c58: bipush 95
      // 1c5a: sipush 454
      // 1c5d: iastore
      // 1c5e: aload 0
      // 1c5f: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1c62: bipush 24
      // 1c64: aaload
      // 1c65: bipush 82
      // 1c67: sipush 453
      // 1c6a: iastore
      // 1c6b: aload 0
      // 1c6c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1c6f: bipush 7
      // 1c71: aaload
      // 1c72: bipush 52
      // 1c74: sipush 452
      // 1c77: iastore
      // 1c78: aload 0
      // 1c79: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1c7c: bipush 9
      // 1c7e: aaload
      // 1c7f: sipush 151
      // 1c82: sipush 451
      // 1c85: iastore
      // 1c86: aload 0
      // 1c87: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1c8a: bipush 3
      // 1c8b: aaload
      // 1c8c: sipush 129
      // 1c8f: sipush 450
      // 1c92: iastore
      // 1c93: aload 0
      // 1c94: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1c97: bipush 5
      // 1c98: aaload
      // 1c99: bipush 87
      // 1c9b: sipush 449
      // 1c9e: iastore
      // 1c9f: aload 0
      // 1ca0: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1ca3: bipush 3
      // 1ca4: aaload
      // 1ca5: bipush 55
      // 1ca7: sipush 448
      // 1caa: iastore
      // 1cab: aload 0
      // 1cac: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1caf: bipush 8
      // 1cb1: aaload
      // 1cb2: sipush 153
      // 1cb5: sipush 447
      // 1cb8: iastore
      // 1cb9: aload 0
      // 1cba: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1cbd: bipush 4
      // 1cbe: aaload
      // 1cbf: bipush 83
      // 1cc1: sipush 446
      // 1cc4: iastore
      // 1cc5: aload 0
      // 1cc6: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1cc9: bipush 3
      // 1cca: aaload
      // 1ccb: bipush 114
      // 1ccd: sipush 445
      // 1cd0: iastore
      // 1cd1: aload 0
      // 1cd2: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1cd5: bipush 23
      // 1cd7: aaload
      // 1cd8: sipush 147
      // 1cdb: sipush 444
      // 1cde: iastore
      // 1cdf: aload 0
      // 1ce0: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1ce3: bipush 15
      // 1ce5: aaload
      // 1ce6: bipush 31
      // 1ce8: sipush 443
      // 1ceb: iastore
      // 1cec: aload 0
      // 1ced: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1cf0: bipush 3
      // 1cf1: aaload
      // 1cf2: bipush 54
      // 1cf4: sipush 442
      // 1cf7: iastore
      // 1cf8: aload 0
      // 1cf9: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1cfc: bipush 11
      // 1cfe: aaload
      // 1cff: bipush 122
      // 1d01: sipush 441
      // 1d04: iastore
      // 1d05: aload 0
      // 1d06: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1d09: bipush 4
      // 1d0a: aaload
      // 1d0b: bipush 4
      // 1d0c: sipush 440
      // 1d0f: iastore
      // 1d10: aload 0
      // 1d11: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1d14: bipush 34
      // 1d16: aaload
      // 1d17: sipush 149
      // 1d1a: sipush 439
      // 1d1d: iastore
      // 1d1e: aload 0
      // 1d1f: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1d22: bipush 3
      // 1d23: aaload
      // 1d24: bipush 17
      // 1d26: sipush 438
      // 1d29: iastore
      // 1d2a: aload 0
      // 1d2b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1d2e: bipush 21
      // 1d30: aaload
      // 1d31: bipush 64
      // 1d33: sipush 437
      // 1d36: iastore
      // 1d37: aload 0
      // 1d38: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1d3b: bipush 26
      // 1d3d: aaload
      // 1d3e: sipush 144
      // 1d41: sipush 436
      // 1d44: iastore
      // 1d45: aload 0
      // 1d46: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1d49: bipush 4
      // 1d4a: aaload
      // 1d4b: bipush 62
      // 1d4d: sipush 435
      // 1d50: iastore
      // 1d51: aload 0
      // 1d52: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1d55: bipush 8
      // 1d57: aaload
      // 1d58: bipush 15
      // 1d5a: sipush 434
      // 1d5d: iastore
      // 1d5e: aload 0
      // 1d5f: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1d62: bipush 35
      // 1d64: aaload
      // 1d65: bipush 80
      // 1d67: sipush 433
      // 1d6a: iastore
      // 1d6b: aload 0
      // 1d6c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1d6f: bipush 7
      // 1d71: aaload
      // 1d72: bipush 110
      // 1d74: sipush 432
      // 1d77: iastore
      // 1d78: aload 0
      // 1d79: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1d7c: bipush 23
      // 1d7e: aaload
      // 1d7f: bipush 114
      // 1d81: sipush 431
      // 1d84: iastore
      // 1d85: aload 0
      // 1d86: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1d89: bipush 3
      // 1d8a: aaload
      // 1d8b: bipush 108
      // 1d8d: sipush 430
      // 1d90: iastore
      // 1d91: aload 0
      // 1d92: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1d95: bipush 3
      // 1d96: aaload
      // 1d97: bipush 62
      // 1d99: sipush 429
      // 1d9c: iastore
      // 1d9d: aload 0
      // 1d9e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1da1: bipush 21
      // 1da3: aaload
      // 1da4: bipush 41
      // 1da6: sipush 428
      // 1da9: iastore
      // 1daa: aload 0
      // 1dab: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1dae: bipush 15
      // 1db0: aaload
      // 1db1: bipush 99
      // 1db3: sipush 427
      // 1db6: iastore
      // 1db7: aload 0
      // 1db8: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1dbb: bipush 5
      // 1dbc: aaload
      // 1dbd: bipush 47
      // 1dbf: sipush 426
      // 1dc2: iastore
      // 1dc3: aload 0
      // 1dc4: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1dc7: bipush 4
      // 1dc8: aaload
      // 1dc9: bipush 96
      // 1dcb: sipush 425
      // 1dce: iastore
      // 1dcf: aload 0
      // 1dd0: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1dd3: bipush 20
      // 1dd5: aaload
      // 1dd6: bipush 122
      // 1dd8: sipush 424
      // 1ddb: iastore
      // 1ddc: aload 0
      // 1ddd: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1de0: bipush 5
      // 1de1: aaload
      // 1de2: bipush 21
      // 1de4: sipush 423
      // 1de7: iastore
      // 1de8: aload 0
      // 1de9: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1dec: bipush 4
      // 1ded: aaload
      // 1dee: sipush 157
      // 1df1: sipush 422
      // 1df4: iastore
      // 1df5: aload 0
      // 1df6: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1df9: bipush 16
      // 1dfb: aaload
      // 1dfc: bipush 14
      // 1dfe: sipush 421
      // 1e01: iastore
      // 1e02: aload 0
      // 1e03: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1e06: bipush 3
      // 1e07: aaload
      // 1e08: bipush 117
      // 1e0a: sipush 420
      // 1e0d: iastore
      // 1e0e: aload 0
      // 1e0f: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1e12: bipush 7
      // 1e14: aaload
      // 1e15: sipush 129
      // 1e18: sipush 419
      // 1e1b: iastore
      // 1e1c: aload 0
      // 1e1d: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1e20: bipush 4
      // 1e21: aaload
      // 1e22: bipush 27
      // 1e24: sipush 418
      // 1e27: iastore
      // 1e28: aload 0
      // 1e29: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1e2c: bipush 5
      // 1e2d: aaload
      // 1e2e: bipush 30
      // 1e30: sipush 417
      // 1e33: iastore
      // 1e34: aload 0
      // 1e35: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1e38: bipush 22
      // 1e3a: aaload
      // 1e3b: bipush 16
      // 1e3d: sipush 416
      // 1e40: iastore
      // 1e41: aload 0
      // 1e42: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1e45: bipush 5
      // 1e46: aaload
      // 1e47: bipush 64
      // 1e49: sipush 415
      // 1e4c: iastore
      // 1e4d: aload 0
      // 1e4e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1e51: bipush 17
      // 1e53: aaload
      // 1e54: bipush 99
      // 1e56: sipush 414
      // 1e59: iastore
      // 1e5a: aload 0
      // 1e5b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1e5e: bipush 17
      // 1e60: aaload
      // 1e61: bipush 57
      // 1e63: sipush 413
      // 1e66: iastore
      // 1e67: aload 0
      // 1e68: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1e6b: bipush 8
      // 1e6d: aaload
      // 1e6e: bipush 105
      // 1e70: sipush 412
      // 1e73: iastore
      // 1e74: aload 0
      // 1e75: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1e78: bipush 5
      // 1e79: aaload
      // 1e7a: bipush 112
      // 1e7c: sipush 411
      // 1e7f: iastore
      // 1e80: aload 0
      // 1e81: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1e84: bipush 20
      // 1e86: aaload
      // 1e87: bipush 59
      // 1e89: sipush 410
      // 1e8c: iastore
      // 1e8d: aload 0
      // 1e8e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1e91: bipush 6
      // 1e93: aaload
      // 1e94: sipush 129
      // 1e97: sipush 409
      // 1e9a: iastore
      // 1e9b: aload 0
      // 1e9c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1e9f: bipush 18
      // 1ea1: aaload
      // 1ea2: bipush 17
      // 1ea4: sipush 408
      // 1ea7: iastore
      // 1ea8: aload 0
      // 1ea9: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1eac: bipush 3
      // 1ead: aaload
      // 1eae: bipush 92
      // 1eb0: sipush 407
      // 1eb3: iastore
      // 1eb4: aload 0
      // 1eb5: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1eb8: bipush 28
      // 1eba: aaload
      // 1ebb: bipush 118
      // 1ebd: sipush 406
      // 1ec0: iastore
      // 1ec1: aload 0
      // 1ec2: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1ec5: bipush 3
      // 1ec6: aaload
      // 1ec7: bipush 109
      // 1ec9: sipush 405
      // 1ecc: iastore
      // 1ecd: aload 0
      // 1ece: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1ed1: bipush 31
      // 1ed3: aaload
      // 1ed4: bipush 51
      // 1ed6: sipush 404
      // 1ed9: iastore
      // 1eda: aload 0
      // 1edb: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1ede: bipush 13
      // 1ee0: aaload
      // 1ee1: bipush 116
      // 1ee3: sipush 403
      // 1ee6: iastore
      // 1ee7: aload 0
      // 1ee8: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1eeb: bipush 6
      // 1eed: aaload
      // 1eee: bipush 15
      // 1ef0: sipush 402
      // 1ef3: iastore
      // 1ef4: aload 0
      // 1ef5: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1ef8: bipush 36
      // 1efa: aaload
      // 1efb: sipush 136
      // 1efe: sipush 401
      // 1f01: iastore
      // 1f02: aload 0
      // 1f03: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1f06: bipush 12
      // 1f08: aaload
      // 1f09: bipush 74
      // 1f0b: sipush 400
      // 1f0e: iastore
      // 1f0f: aload 0
      // 1f10: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1f13: bipush 20
      // 1f15: aaload
      // 1f16: bipush 88
      // 1f18: sipush 399
      // 1f1b: iastore
      // 1f1c: aload 0
      // 1f1d: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1f20: bipush 36
      // 1f22: aaload
      // 1f23: bipush 68
      // 1f25: sipush 398
      // 1f28: iastore
      // 1f29: aload 0
      // 1f2a: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1f2d: bipush 3
      // 1f2e: aaload
      // 1f2f: sipush 147
      // 1f32: sipush 397
      // 1f35: iastore
      // 1f36: aload 0
      // 1f37: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1f3a: bipush 15
      // 1f3c: aaload
      // 1f3d: bipush 84
      // 1f3f: sipush 396
      // 1f42: iastore
      // 1f43: aload 0
      // 1f44: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1f47: bipush 16
      // 1f49: aaload
      // 1f4a: bipush 32
      // 1f4c: sipush 395
      // 1f4f: iastore
      // 1f50: aload 0
      // 1f51: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1f54: bipush 16
      // 1f56: aaload
      // 1f57: bipush 58
      // 1f59: sipush 394
      // 1f5c: iastore
      // 1f5d: aload 0
      // 1f5e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1f61: bipush 7
      // 1f63: aaload
      // 1f64: bipush 66
      // 1f66: sipush 393
      // 1f69: iastore
      // 1f6a: aload 0
      // 1f6b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1f6e: bipush 23
      // 1f70: aaload
      // 1f71: bipush 107
      // 1f73: sipush 392
      // 1f76: iastore
      // 1f77: aload 0
      // 1f78: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1f7b: bipush 9
      // 1f7d: aaload
      // 1f7e: bipush 6
      // 1f80: sipush 391
      // 1f83: iastore
      // 1f84: aload 0
      // 1f85: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1f88: bipush 12
      // 1f8a: aaload
      // 1f8b: bipush 86
      // 1f8d: sipush 390
      // 1f90: iastore
      // 1f91: aload 0
      // 1f92: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1f95: bipush 23
      // 1f97: aaload
      // 1f98: bipush 112
      // 1f9a: sipush 389
      // 1f9d: iastore
      // 1f9e: aload 0
      // 1f9f: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1fa2: bipush 37
      // 1fa4: aaload
      // 1fa5: bipush 23
      // 1fa7: sipush 388
      // 1faa: iastore
      // 1fab: aload 0
      // 1fac: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1faf: bipush 3
      // 1fb0: aaload
      // 1fb1: sipush 138
      // 1fb4: sipush 387
      // 1fb7: iastore
      // 1fb8: aload 0
      // 1fb9: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1fbc: bipush 20
      // 1fbe: aaload
      // 1fbf: bipush 68
      // 1fc1: sipush 386
      // 1fc4: iastore
      // 1fc5: aload 0
      // 1fc6: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1fc9: bipush 15
      // 1fcb: aaload
      // 1fcc: bipush 116
      // 1fce: sipush 385
      // 1fd1: iastore
      // 1fd2: aload 0
      // 1fd3: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1fd6: bipush 18
      // 1fd8: aaload
      // 1fd9: bipush 64
      // 1fdb: sipush 384
      // 1fde: iastore
      // 1fdf: aload 0
      // 1fe0: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1fe3: bipush 12
      // 1fe5: aaload
      // 1fe6: sipush 139
      // 1fe9: sipush 383
      // 1fec: iastore
      // 1fed: aload 0
      // 1fee: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1ff1: bipush 11
      // 1ff3: aaload
      // 1ff4: sipush 155
      // 1ff7: sipush 382
      // 1ffa: iastore
      // 1ffb: aload 0
      // 1ffc: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 1fff: bipush 4
      // 2000: aaload
      // 2001: sipush 156
      // 2004: sipush 381
      // 2007: iastore
      // 2008: aload 0
      // 2009: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 200c: bipush 12
      // 200e: aaload
      // 200f: bipush 84
      // 2011: sipush 380
      // 2014: iastore
      // 2015: aload 0
      // 2016: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2019: bipush 18
      // 201b: aaload
      // 201c: bipush 49
      // 201e: sipush 379
      // 2021: iastore
      // 2022: aload 0
      // 2023: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2026: bipush 25
      // 2028: aaload
      // 2029: bipush 125
      // 202b: sipush 378
      // 202e: iastore
      // 202f: aload 0
      // 2030: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2033: bipush 25
      // 2035: aaload
      // 2036: sipush 147
      // 2039: sipush 377
      // 203c: iastore
      // 203d: aload 0
      // 203e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2041: bipush 15
      // 2043: aaload
      // 2044: bipush 110
      // 2046: sipush 376
      // 2049: iastore
      // 204a: aload 0
      // 204b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 204e: bipush 19
      // 2050: aaload
      // 2051: bipush 96
      // 2053: sipush 375
      // 2056: iastore
      // 2057: aload 0
      // 2058: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 205b: bipush 30
      // 205d: aaload
      // 205e: sipush 152
      // 2061: sipush 374
      // 2064: iastore
      // 2065: aload 0
      // 2066: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2069: bipush 6
      // 206b: aaload
      // 206c: bipush 31
      // 206e: sipush 373
      // 2071: iastore
      // 2072: aload 0
      // 2073: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2076: bipush 27
      // 2078: aaload
      // 2079: bipush 117
      // 207b: sipush 372
      // 207e: iastore
      // 207f: aload 0
      // 2080: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2083: bipush 3
      // 2084: aaload
      // 2085: bipush 10
      // 2087: sipush 371
      // 208a: iastore
      // 208b: aload 0
      // 208c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 208f: bipush 6
      // 2091: aaload
      // 2092: sipush 131
      // 2095: sipush 370
      // 2098: iastore
      // 2099: aload 0
      // 209a: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 209d: bipush 13
      // 209f: aaload
      // 20a0: bipush 112
      // 20a2: sipush 369
      // 20a5: iastore
      // 20a6: aload 0
      // 20a7: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 20aa: bipush 36
      // 20ac: aaload
      // 20ad: sipush 156
      // 20b0: sipush 368
      // 20b3: iastore
      // 20b4: aload 0
      // 20b5: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 20b8: bipush 4
      // 20b9: aaload
      // 20ba: bipush 60
      // 20bc: sipush 367
      // 20bf: iastore
      // 20c0: aload 0
      // 20c1: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 20c4: bipush 15
      // 20c6: aaload
      // 20c7: bipush 121
      // 20c9: sipush 366
      // 20cc: iastore
      // 20cd: aload 0
      // 20ce: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 20d1: bipush 4
      // 20d2: aaload
      // 20d3: bipush 112
      // 20d5: sipush 365
      // 20d8: iastore
      // 20d9: aload 0
      // 20da: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 20dd: bipush 30
      // 20df: aaload
      // 20e0: sipush 142
      // 20e3: sipush 364
      // 20e6: iastore
      // 20e7: aload 0
      // 20e8: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 20eb: bipush 23
      // 20ed: aaload
      // 20ee: sipush 154
      // 20f1: sipush 363
      // 20f4: iastore
      // 20f5: aload 0
      // 20f6: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 20f9: bipush 27
      // 20fb: aaload
      // 20fc: bipush 101
      // 20fe: sipush 362
      // 2101: iastore
      // 2102: aload 0
      // 2103: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2106: bipush 9
      // 2108: aaload
      // 2109: sipush 140
      // 210c: sipush 361
      // 210f: iastore
      // 2110: aload 0
      // 2111: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2114: bipush 3
      // 2115: aaload
      // 2116: bipush 89
      // 2118: sipush 360
      // 211b: iastore
      // 211c: aload 0
      // 211d: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2120: bipush 18
      // 2122: aaload
      // 2123: sipush 148
      // 2126: sipush 359
      // 2129: iastore
      // 212a: aload 0
      // 212b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 212e: bipush 4
      // 212f: aaload
      // 2130: bipush 69
      // 2132: sipush 358
      // 2135: iastore
      // 2136: aload 0
      // 2137: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 213a: bipush 16
      // 213c: aaload
      // 213d: bipush 49
      // 213f: sipush 357
      // 2142: iastore
      // 2143: aload 0
      // 2144: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2147: bipush 6
      // 2149: aaload
      // 214a: bipush 117
      // 214c: sipush 356
      // 214f: iastore
      // 2150: aload 0
      // 2151: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2154: bipush 36
      // 2156: aaload
      // 2157: bipush 55
      // 2159: sipush 355
      // 215c: iastore
      // 215d: aload 0
      // 215e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2161: bipush 5
      // 2162: aaload
      // 2163: bipush 123
      // 2165: sipush 354
      // 2168: iastore
      // 2169: aload 0
      // 216a: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 216d: bipush 4
      // 216e: aaload
      // 216f: bipush 126
      // 2171: sipush 353
      // 2174: iastore
      // 2175: aload 0
      // 2176: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2179: bipush 4
      // 217a: aaload
      // 217b: bipush 119
      // 217d: sipush 352
      // 2180: iastore
      // 2181: aload 0
      // 2182: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2185: bipush 9
      // 2187: aaload
      // 2188: bipush 95
      // 218a: sipush 351
      // 218d: iastore
      // 218e: aload 0
      // 218f: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2192: bipush 5
      // 2193: aaload
      // 2194: bipush 24
      // 2196: sipush 350
      // 2199: iastore
      // 219a: aload 0
      // 219b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 219e: bipush 16
      // 21a0: aaload
      // 21a1: sipush 133
      // 21a4: sipush 349
      // 21a7: iastore
      // 21a8: aload 0
      // 21a9: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 21ac: bipush 10
      // 21ae: aaload
      // 21af: sipush 134
      // 21b2: sipush 348
      // 21b5: iastore
      // 21b6: aload 0
      // 21b7: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 21ba: bipush 26
      // 21bc: aaload
      // 21bd: bipush 59
      // 21bf: sipush 347
      // 21c2: iastore
      // 21c3: aload 0
      // 21c4: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 21c7: bipush 6
      // 21c9: aaload
      // 21ca: bipush 41
      // 21cc: sipush 346
      // 21cf: iastore
      // 21d0: aload 0
      // 21d1: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 21d4: bipush 6
      // 21d6: aaload
      // 21d7: sipush 146
      // 21da: sipush 345
      // 21dd: iastore
      // 21de: aload 0
      // 21df: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 21e2: bipush 19
      // 21e4: aaload
      // 21e5: bipush 24
      // 21e7: sipush 344
      // 21ea: iastore
      // 21eb: aload 0
      // 21ec: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 21ef: bipush 5
      // 21f0: aaload
      // 21f1: bipush 113
      // 21f3: sipush 343
      // 21f6: iastore
      // 21f7: aload 0
      // 21f8: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 21fb: bipush 10
      // 21fd: aaload
      // 21fe: bipush 118
      // 2200: sipush 342
      // 2203: iastore
      // 2204: aload 0
      // 2205: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2208: bipush 34
      // 220a: aaload
      // 220b: sipush 151
      // 220e: sipush 341
      // 2211: iastore
      // 2212: aload 0
      // 2213: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2216: bipush 9
      // 2218: aaload
      // 2219: bipush 72
      // 221b: sipush 340
      // 221e: iastore
      // 221f: aload 0
      // 2220: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2223: bipush 31
      // 2225: aaload
      // 2226: bipush 25
      // 2228: sipush 339
      // 222b: iastore
      // 222c: aload 0
      // 222d: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2230: bipush 18
      // 2232: aaload
      // 2233: bipush 126
      // 2235: sipush 338
      // 2238: iastore
      // 2239: aload 0
      // 223a: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 223d: bipush 18
      // 223f: aaload
      // 2240: bipush 28
      // 2242: sipush 337
      // 2245: iastore
      // 2246: aload 0
      // 2247: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 224a: bipush 4
      // 224b: aaload
      // 224c: sipush 153
      // 224f: sipush 336
      // 2252: iastore
      // 2253: aload 0
      // 2254: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2257: bipush 3
      // 2258: aaload
      // 2259: bipush 84
      // 225b: sipush 335
      // 225e: iastore
      // 225f: aload 0
      // 2260: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2263: bipush 21
      // 2265: aaload
      // 2266: bipush 18
      // 2268: sipush 334
      // 226b: iastore
      // 226c: aload 0
      // 226d: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2270: bipush 25
      // 2272: aaload
      // 2273: sipush 129
      // 2276: sipush 333
      // 2279: iastore
      // 227a: aload 0
      // 227b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 227e: bipush 6
      // 2280: aaload
      // 2281: bipush 107
      // 2283: sipush 332
      // 2286: iastore
      // 2287: aload 0
      // 2288: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 228b: bipush 12
      // 228d: aaload
      // 228e: bipush 25
      // 2290: sipush 331
      // 2293: iastore
      // 2294: aload 0
      // 2295: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2298: bipush 17
      // 229a: aaload
      // 229b: bipush 109
      // 229d: sipush 330
      // 22a0: iastore
      // 22a1: aload 0
      // 22a2: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 22a5: bipush 7
      // 22a7: aaload
      // 22a8: bipush 76
      // 22aa: sipush 329
      // 22ad: iastore
      // 22ae: aload 0
      // 22af: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 22b2: bipush 15
      // 22b4: aaload
      // 22b5: bipush 15
      // 22b7: sipush 328
      // 22ba: iastore
      // 22bb: aload 0
      // 22bc: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 22bf: bipush 4
      // 22c0: aaload
      // 22c1: bipush 14
      // 22c3: sipush 327
      // 22c6: iastore
      // 22c7: aload 0
      // 22c8: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 22cb: bipush 23
      // 22cd: aaload
      // 22ce: bipush 88
      // 22d0: sipush 326
      // 22d3: iastore
      // 22d4: aload 0
      // 22d5: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 22d8: bipush 18
      // 22da: aaload
      // 22db: bipush 2
      // 22dc: sipush 325
      // 22df: iastore
      // 22e0: aload 0
      // 22e1: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 22e4: bipush 6
      // 22e6: aaload
      // 22e7: bipush 88
      // 22e9: sipush 324
      // 22ec: iastore
      // 22ed: aload 0
      // 22ee: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 22f1: bipush 16
      // 22f3: aaload
      // 22f4: bipush 84
      // 22f6: sipush 323
      // 22f9: iastore
      // 22fa: aload 0
      // 22fb: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 22fe: bipush 12
      // 2300: aaload
      // 2301: bipush 48
      // 2303: sipush 322
      // 2306: iastore
      // 2307: aload 0
      // 2308: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 230b: bipush 7
      // 230d: aaload
      // 230e: bipush 68
      // 2310: sipush 321
      // 2313: iastore
      // 2314: aload 0
      // 2315: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2318: bipush 5
      // 2319: aaload
      // 231a: bipush 50
      // 231c: sipush 320
      // 231f: iastore
      // 2320: aload 0
      // 2321: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2324: bipush 13
      // 2326: aaload
      // 2327: bipush 54
      // 2329: sipush 319
      // 232c: iastore
      // 232d: aload 0
      // 232e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2331: bipush 7
      // 2333: aaload
      // 2334: bipush 98
      // 2336: sipush 318
      // 2339: iastore
      // 233a: aload 0
      // 233b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 233e: bipush 11
      // 2340: aaload
      // 2341: bipush 6
      // 2343: sipush 317
      // 2346: iastore
      // 2347: aload 0
      // 2348: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 234b: bipush 9
      // 234d: aaload
      // 234e: bipush 80
      // 2350: sipush 316
      // 2353: iastore
      // 2354: aload 0
      // 2355: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2358: bipush 16
      // 235a: aaload
      // 235b: bipush 41
      // 235d: sipush 315
      // 2360: iastore
      // 2361: aload 0
      // 2362: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2365: bipush 7
      // 2367: aaload
      // 2368: bipush 43
      // 236a: sipush 314
      // 236d: iastore
      // 236e: aload 0
      // 236f: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2372: bipush 28
      // 2374: aaload
      // 2375: bipush 117
      // 2377: sipush 313
      // 237a: iastore
      // 237b: aload 0
      // 237c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 237f: bipush 3
      // 2380: aaload
      // 2381: bipush 51
      // 2383: sipush 312
      // 2386: iastore
      // 2387: aload 0
      // 2388: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 238b: bipush 7
      // 238d: aaload
      // 238e: bipush 3
      // 238f: sipush 311
      // 2392: iastore
      // 2393: aload 0
      // 2394: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2397: bipush 20
      // 2399: aaload
      // 239a: bipush 81
      // 239c: sipush 310
      // 239f: iastore
      // 23a0: aload 0
      // 23a1: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 23a4: bipush 4
      // 23a5: aaload
      // 23a6: bipush 2
      // 23a7: sipush 309
      // 23aa: iastore
      // 23ab: aload 0
      // 23ac: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 23af: bipush 11
      // 23b1: aaload
      // 23b2: bipush 16
      // 23b4: sipush 308
      // 23b7: iastore
      // 23b8: aload 0
      // 23b9: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 23bc: bipush 10
      // 23be: aaload
      // 23bf: bipush 4
      // 23c0: sipush 307
      // 23c3: iastore
      // 23c4: aload 0
      // 23c5: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 23c8: bipush 10
      // 23ca: aaload
      // 23cb: bipush 119
      // 23cd: sipush 306
      // 23d0: iastore
      // 23d1: aload 0
      // 23d2: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 23d5: bipush 6
      // 23d7: aaload
      // 23d8: sipush 142
      // 23db: sipush 305
      // 23de: iastore
      // 23df: aload 0
      // 23e0: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 23e3: bipush 18
      // 23e5: aaload
      // 23e6: bipush 51
      // 23e8: sipush 304
      // 23eb: iastore
      // 23ec: aload 0
      // 23ed: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 23f0: bipush 8
      // 23f2: aaload
      // 23f3: sipush 144
      // 23f6: sipush 303
      // 23f9: iastore
      // 23fa: aload 0
      // 23fb: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 23fe: bipush 10
      // 2400: aaload
      // 2401: bipush 65
      // 2403: sipush 302
      // 2406: iastore
      // 2407: aload 0
      // 2408: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 240b: bipush 11
      // 240d: aaload
      // 240e: bipush 64
      // 2410: sipush 301
      // 2413: iastore
      // 2414: aload 0
      // 2415: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2418: bipush 11
      // 241a: aaload
      // 241b: sipush 130
      // 241e: sipush 300
      // 2421: iastore
      // 2422: aload 0
      // 2423: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2426: bipush 9
      // 2428: aaload
      // 2429: bipush 92
      // 242b: sipush 299
      // 242e: iastore
      // 242f: aload 0
      // 2430: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2433: bipush 18
      // 2435: aaload
      // 2436: bipush 29
      // 2438: sipush 298
      // 243b: iastore
      // 243c: aload 0
      // 243d: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2440: bipush 18
      // 2442: aaload
      // 2443: bipush 78
      // 2445: sipush 297
      // 2448: iastore
      // 2449: aload 0
      // 244a: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 244d: bipush 18
      // 244f: aaload
      // 2450: sipush 151
      // 2453: sipush 296
      // 2456: iastore
      // 2457: aload 0
      // 2458: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 245b: bipush 33
      // 245d: aaload
      // 245e: bipush 127
      // 2460: sipush 295
      // 2463: iastore
      // 2464: aload 0
      // 2465: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2468: bipush 35
      // 246a: aaload
      // 246b: bipush 113
      // 246d: sipush 294
      // 2470: iastore
      // 2471: aload 0
      // 2472: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2475: bipush 10
      // 2477: aaload
      // 2478: sipush 155
      // 247b: sipush 293
      // 247e: iastore
      // 247f: aload 0
      // 2480: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2483: bipush 3
      // 2484: aaload
      // 2485: bipush 76
      // 2487: sipush 292
      // 248a: iastore
      // 248b: aload 0
      // 248c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 248f: bipush 36
      // 2491: aaload
      // 2492: bipush 123
      // 2494: sipush 291
      // 2497: iastore
      // 2498: aload 0
      // 2499: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 249c: bipush 13
      // 249e: aaload
      // 249f: sipush 143
      // 24a2: sipush 290
      // 24a5: iastore
      // 24a6: aload 0
      // 24a7: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 24aa: bipush 5
      // 24ab: aaload
      // 24ac: sipush 135
      // 24af: sipush 289
      // 24b2: iastore
      // 24b3: aload 0
      // 24b4: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 24b7: bipush 23
      // 24b9: aaload
      // 24ba: bipush 116
      // 24bc: sipush 288
      // 24bf: iastore
      // 24c0: aload 0
      // 24c1: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 24c4: bipush 6
      // 24c6: aaload
      // 24c7: bipush 101
      // 24c9: sipush 287
      // 24cc: iastore
      // 24cd: aload 0
      // 24ce: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 24d1: bipush 14
      // 24d3: aaload
      // 24d4: bipush 74
      // 24d6: sipush 286
      // 24d9: iastore
      // 24da: aload 0
      // 24db: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 24de: bipush 7
      // 24e0: aaload
      // 24e1: sipush 153
      // 24e4: sipush 285
      // 24e7: iastore
      // 24e8: aload 0
      // 24e9: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 24ec: bipush 3
      // 24ed: aaload
      // 24ee: bipush 101
      // 24f0: sipush 284
      // 24f3: iastore
      // 24f4: aload 0
      // 24f5: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 24f8: bipush 9
      // 24fa: aaload
      // 24fb: bipush 74
      // 24fd: sipush 283
      // 2500: iastore
      // 2501: aload 0
      // 2502: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2505: bipush 3
      // 2506: aaload
      // 2507: sipush 156
      // 250a: sipush 282
      // 250d: iastore
      // 250e: aload 0
      // 250f: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2512: bipush 4
      // 2513: aaload
      // 2514: sipush 147
      // 2517: sipush 281
      // 251a: iastore
      // 251b: aload 0
      // 251c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 251f: bipush 9
      // 2521: aaload
      // 2522: bipush 12
      // 2524: sipush 280
      // 2527: iastore
      // 2528: aload 0
      // 2529: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 252c: bipush 18
      // 252e: aaload
      // 252f: sipush 133
      // 2532: sipush 279
      // 2535: iastore
      // 2536: aload 0
      // 2537: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 253a: bipush 4
      // 253b: aaload
      // 253c: bipush 0
      // 253d: sipush 278
      // 2540: iastore
      // 2541: aload 0
      // 2542: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2545: bipush 7
      // 2547: aaload
      // 2548: sipush 155
      // 254b: sipush 277
      // 254e: iastore
      // 254f: aload 0
      // 2550: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2553: bipush 9
      // 2555: aaload
      // 2556: sipush 144
      // 2559: sipush 276
      // 255c: iastore
      // 255d: aload 0
      // 255e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2561: bipush 23
      // 2563: aaload
      // 2564: bipush 49
      // 2566: sipush 275
      // 2569: iastore
      // 256a: aload 0
      // 256b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 256e: bipush 5
      // 256f: aaload
      // 2570: bipush 89
      // 2572: sipush 274
      // 2575: iastore
      // 2576: aload 0
      // 2577: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 257a: bipush 10
      // 257c: aaload
      // 257d: bipush 11
      // 257f: sipush 273
      // 2582: iastore
      // 2583: aload 0
      // 2584: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2587: bipush 3
      // 2588: aaload
      // 2589: bipush 110
      // 258b: sipush 272
      // 258e: iastore
      // 258f: aload 0
      // 2590: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2593: bipush 3
      // 2594: aaload
      // 2595: bipush 40
      // 2597: sipush 271
      // 259a: iastore
      // 259b: aload 0
      // 259c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 259f: bipush 29
      // 25a1: aaload
      // 25a2: bipush 115
      // 25a4: sipush 270
      // 25a7: iastore
      // 25a8: aload 0
      // 25a9: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 25ac: bipush 9
      // 25ae: aaload
      // 25af: bipush 100
      // 25b1: sipush 269
      // 25b4: iastore
      // 25b5: aload 0
      // 25b6: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 25b9: bipush 21
      // 25bb: aaload
      // 25bc: bipush 67
      // 25be: sipush 268
      // 25c1: iastore
      // 25c2: aload 0
      // 25c3: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 25c6: bipush 23
      // 25c8: aaload
      // 25c9: sipush 145
      // 25cc: sipush 267
      // 25cf: iastore
      // 25d0: aload 0
      // 25d1: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 25d4: bipush 10
      // 25d6: aaload
      // 25d7: bipush 47
      // 25d9: sipush 266
      // 25dc: iastore
      // 25dd: aload 0
      // 25de: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 25e1: bipush 4
      // 25e2: aaload
      // 25e3: bipush 31
      // 25e5: sipush 265
      // 25e8: iastore
      // 25e9: aload 0
      // 25ea: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 25ed: bipush 4
      // 25ee: aaload
      // 25ef: bipush 81
      // 25f1: sipush 264
      // 25f4: iastore
      // 25f5: aload 0
      // 25f6: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 25f9: bipush 22
      // 25fb: aaload
      // 25fc: bipush 62
      // 25fe: sipush 263
      // 2601: iastore
      // 2602: aload 0
      // 2603: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2606: bipush 4
      // 2607: aaload
      // 2608: bipush 28
      // 260a: sipush 262
      // 260d: iastore
      // 260e: aload 0
      // 260f: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2612: bipush 27
      // 2614: aaload
      // 2615: bipush 39
      // 2617: sipush 261
      // 261a: iastore
      // 261b: aload 0
      // 261c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 261f: bipush 27
      // 2621: aaload
      // 2622: bipush 54
      // 2624: sipush 260
      // 2627: iastore
      // 2628: aload 0
      // 2629: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 262c: bipush 32
      // 262e: aaload
      // 262f: bipush 46
      // 2631: sipush 259
      // 2634: iastore
      // 2635: aload 0
      // 2636: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2639: bipush 4
      // 263a: aaload
      // 263b: bipush 76
      // 263d: sipush 258
      // 2640: iastore
      // 2641: aload 0
      // 2642: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2645: bipush 26
      // 2647: aaload
      // 2648: bipush 15
      // 264a: sipush 257
      // 264d: iastore
      // 264e: aload 0
      // 264f: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2652: bipush 12
      // 2654: aaload
      // 2655: sipush 154
      // 2658: sipush 256
      // 265b: iastore
      // 265c: aload 0
      // 265d: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2660: bipush 9
      // 2662: aaload
      // 2663: sipush 150
      // 2666: sipush 255
      // 2669: iastore
      // 266a: aload 0
      // 266b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 266e: bipush 15
      // 2670: aaload
      // 2671: bipush 17
      // 2673: sipush 254
      // 2676: iastore
      // 2677: aload 0
      // 2678: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 267b: bipush 5
      // 267c: aaload
      // 267d: sipush 129
      // 2680: sipush 253
      // 2683: iastore
      // 2684: aload 0
      // 2685: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2688: bipush 10
      // 268a: aaload
      // 268b: bipush 40
      // 268d: sipush 252
      // 2690: iastore
      // 2691: aload 0
      // 2692: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2695: bipush 13
      // 2697: aaload
      // 2698: bipush 37
      // 269a: sipush 251
      // 269d: iastore
      // 269e: aload 0
      // 269f: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 26a2: bipush 31
      // 26a4: aaload
      // 26a5: bipush 104
      // 26a7: sipush 250
      // 26aa: iastore
      // 26ab: aload 0
      // 26ac: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 26af: bipush 3
      // 26b0: aaload
      // 26b1: sipush 152
      // 26b4: sipush 249
      // 26b7: iastore
      // 26b8: aload 0
      // 26b9: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 26bc: bipush 5
      // 26bd: aaload
      // 26be: bipush 22
      // 26c0: sipush 248
      // 26c3: iastore
      // 26c4: aload 0
      // 26c5: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 26c8: bipush 8
      // 26ca: aaload
      // 26cb: bipush 48
      // 26cd: sipush 247
      // 26d0: iastore
      // 26d1: aload 0
      // 26d2: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 26d5: bipush 4
      // 26d6: aaload
      // 26d7: bipush 74
      // 26d9: sipush 246
      // 26dc: iastore
      // 26dd: aload 0
      // 26de: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 26e1: bipush 6
      // 26e3: aaload
      // 26e4: bipush 17
      // 26e6: sipush 245
      // 26e9: iastore
      // 26ea: aload 0
      // 26eb: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 26ee: bipush 30
      // 26f0: aaload
      // 26f1: bipush 82
      // 26f3: sipush 244
      // 26f6: iastore
      // 26f7: aload 0
      // 26f8: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 26fb: bipush 4
      // 26fc: aaload
      // 26fd: bipush 116
      // 26ff: sipush 243
      // 2702: iastore
      // 2703: aload 0
      // 2704: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2707: bipush 16
      // 2709: aaload
      // 270a: bipush 42
      // 270c: sipush 242
      // 270f: iastore
      // 2710: aload 0
      // 2711: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2714: bipush 5
      // 2715: aaload
      // 2716: bipush 55
      // 2718: sipush 241
      // 271b: iastore
      // 271c: aload 0
      // 271d: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2720: bipush 4
      // 2721: aaload
      // 2722: bipush 64
      // 2724: sipush 240
      // 2727: iastore
      // 2728: aload 0
      // 2729: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 272c: bipush 14
      // 272e: aaload
      // 272f: bipush 19
      // 2731: sipush 239
      // 2734: iastore
      // 2735: aload 0
      // 2736: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2739: bipush 35
      // 273b: aaload
      // 273c: bipush 82
      // 273e: sipush 238
      // 2741: iastore
      // 2742: aload 0
      // 2743: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2746: bipush 30
      // 2748: aaload
      // 2749: sipush 139
      // 274c: sipush 237
      // 274f: iastore
      // 2750: aload 0
      // 2751: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2754: bipush 26
      // 2756: aaload
      // 2757: sipush 152
      // 275a: sipush 236
      // 275d: iastore
      // 275e: aload 0
      // 275f: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2762: bipush 32
      // 2764: aaload
      // 2765: bipush 32
      // 2767: sipush 235
      // 276a: iastore
      // 276b: aload 0
      // 276c: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 276f: bipush 21
      // 2771: aaload
      // 2772: bipush 102
      // 2774: sipush 234
      // 2777: iastore
      // 2778: aload 0
      // 2779: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 277c: bipush 10
      // 277e: aaload
      // 277f: sipush 131
      // 2782: sipush 233
      // 2785: iastore
      // 2786: aload 0
      // 2787: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 278a: bipush 9
      // 278c: aaload
      // 278d: sipush 128
      // 2790: sipush 232
      // 2793: iastore
      // 2794: aload 0
      // 2795: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2798: bipush 3
      // 2799: aaload
      // 279a: bipush 87
      // 279c: sipush 231
      // 279f: iastore
      // 27a0: aload 0
      // 27a1: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 27a4: bipush 4
      // 27a5: aaload
      // 27a6: bipush 51
      // 27a8: sipush 230
      // 27ab: iastore
      // 27ac: aload 0
      // 27ad: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 27b0: bipush 10
      // 27b2: aaload
      // 27b3: bipush 15
      // 27b5: sipush 229
      // 27b8: iastore
      // 27b9: aload 0
      // 27ba: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 27bd: bipush 4
      // 27be: aaload
      // 27bf: sipush 150
      // 27c2: sipush 228
      // 27c5: iastore
      // 27c6: aload 0
      // 27c7: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 27ca: bipush 7
      // 27cc: aaload
      // 27cd: bipush 4
      // 27ce: sipush 227
      // 27d1: iastore
      // 27d2: aload 0
      // 27d3: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 27d6: bipush 7
      // 27d8: aaload
      // 27d9: bipush 51
      // 27db: sipush 226
      // 27de: iastore
      // 27df: aload 0
      // 27e0: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 27e3: bipush 7
      // 27e5: aaload
      // 27e6: sipush 157
      // 27e9: sipush 225
      // 27ec: iastore
      // 27ed: aload 0
      // 27ee: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 27f1: bipush 4
      // 27f2: aaload
      // 27f3: sipush 146
      // 27f6: sipush 224
      // 27f9: iastore
      // 27fa: aload 0
      // 27fb: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 27fe: bipush 4
      // 27ff: aaload
      // 2800: bipush 91
      // 2802: sipush 223
      // 2805: iastore
      // 2806: aload 0
      // 2807: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 280a: bipush 7
      // 280c: aaload
      // 280d: bipush 13
      // 280f: sipush 222
      // 2812: iastore
      // 2813: aload 0
      // 2814: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2817: bipush 17
      // 2819: aaload
      // 281a: bipush 116
      // 281c: sipush 221
      // 281f: iastore
      // 2820: aload 0
      // 2821: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2824: bipush 23
      // 2826: aaload
      // 2827: bipush 21
      // 2829: sipush 220
      // 282c: iastore
      // 282d: aload 0
      // 282e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2831: bipush 5
      // 2832: aaload
      // 2833: bipush 106
      // 2835: sipush 219
      // 2838: iastore
      // 2839: aload 0
      // 283a: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 283d: bipush 14
      // 283f: aaload
      // 2840: bipush 100
      // 2842: sipush 218
      // 2845: iastore
      // 2846: aload 0
      // 2847: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 284a: bipush 10
      // 284c: aaload
      // 284d: sipush 152
      // 2850: sipush 217
      // 2853: iastore
      // 2854: aload 0
      // 2855: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2858: bipush 14
      // 285a: aaload
      // 285b: bipush 89
      // 285d: sipush 216
      // 2860: iastore
      // 2861: aload 0
      // 2862: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2865: bipush 6
      // 2867: aaload
      // 2868: sipush 138
      // 286b: sipush 215
      // 286e: iastore
      // 286f: aload 0
      // 2870: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2873: bipush 12
      // 2875: aaload
      // 2876: sipush 157
      // 2879: sipush 214
      // 287c: iastore
      // 287d: aload 0
      // 287e: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2881: bipush 10
      // 2883: aaload
      // 2884: bipush 102
      // 2886: sipush 213
      // 2889: iastore
      // 288a: aload 0
      // 288b: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 288e: bipush 19
      // 2890: aaload
      // 2891: bipush 94
      // 2893: sipush 212
      // 2896: iastore
      // 2897: aload 0
      // 2898: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 289b: bipush 7
      // 289d: aaload
      // 289e: bipush 74
      // 28a0: sipush 211
      // 28a3: iastore
      // 28a4: aload 0
      // 28a5: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 28a8: bipush 18
      // 28aa: aaload
      // 28ab: sipush 128
      // 28ae: sipush 210
      // 28b1: iastore
      // 28b2: aload 0
      // 28b3: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 28b6: bipush 27
      // 28b8: aaload
      // 28b9: bipush 111
      // 28bb: sipush 209
      // 28be: iastore
      // 28bf: aload 0
      // 28c0: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 28c3: bipush 11
      // 28c5: aaload
      // 28c6: bipush 57
      // 28c8: sipush 208
      // 28cb: iastore
      // 28cc: aload 0
      // 28cd: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 28d0: bipush 3
      // 28d1: aaload
      // 28d2: sipush 131
      // 28d5: sipush 207
      // 28d8: iastore
      // 28d9: aload 0
      // 28da: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 28dd: bipush 30
      // 28df: aaload
      // 28e0: bipush 23
      // 28e2: sipush 206
      // 28e5: iastore
      // 28e6: aload 0
      // 28e7: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 28ea: bipush 30
      // 28ec: aaload
      // 28ed: bipush 126
      // 28ef: sipush 205
      // 28f2: iastore
      // 28f3: aload 0
      // 28f4: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 28f7: bipush 4
      // 28f8: aaload
      // 28f9: bipush 36
      // 28fb: sipush 204
      // 28fe: iastore
      // 28ff: aload 0
      // 2900: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2903: bipush 26
      // 2905: aaload
      // 2906: bipush 124
      // 2908: sipush 203
      // 290b: iastore
      // 290c: aload 0
      // 290d: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 2910: bipush 4
      // 2911: aaload
      // 2912: bipush 19
      // 2914: sipush 202
      // 2917: iastore
      // 2918: aload 0
      // 2919: getfield io/legado/app/help/BytesEncodingDetect.Big5Freq [[I
      // 291c: bipush 9
      // 291e: aaload
      // 291f: sipush 152
      // 2922: sipush 201
      // 2925: iastore
      // 2926: aload 0
      // 2927: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 292a: bipush 41
      // 292c: aaload
      // 292d: bipush 122
      // 292f: sipush 600
      // 2932: iastore
      // 2933: aload 0
      // 2934: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2937: bipush 35
      // 2939: aaload
      // 293a: bipush 0
      // 293b: sipush 599
      // 293e: iastore
      // 293f: aload 0
      // 2940: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2943: bipush 43
      // 2945: aaload
      // 2946: bipush 15
      // 2948: sipush 598
      // 294b: iastore
      // 294c: aload 0
      // 294d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2950: bipush 35
      // 2952: aaload
      // 2953: bipush 99
      // 2955: sipush 597
      // 2958: iastore
      // 2959: aload 0
      // 295a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 295d: bipush 35
      // 295f: aaload
      // 2960: bipush 6
      // 2962: sipush 596
      // 2965: iastore
      // 2966: aload 0
      // 2967: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 296a: bipush 35
      // 296c: aaload
      // 296d: bipush 8
      // 296f: sipush 595
      // 2972: iastore
      // 2973: aload 0
      // 2974: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2977: bipush 38
      // 2979: aaload
      // 297a: sipush 154
      // 297d: sipush 594
      // 2980: iastore
      // 2981: aload 0
      // 2982: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2985: bipush 37
      // 2987: aaload
      // 2988: bipush 34
      // 298a: sipush 593
      // 298d: iastore
      // 298e: aload 0
      // 298f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2992: bipush 37
      // 2994: aaload
      // 2995: bipush 115
      // 2997: sipush 592
      // 299a: iastore
      // 299b: aload 0
      // 299c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 299f: bipush 36
      // 29a1: aaload
      // 29a2: bipush 12
      // 29a4: sipush 591
      // 29a7: iastore
      // 29a8: aload 0
      // 29a9: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 29ac: bipush 18
      // 29ae: aaload
      // 29af: bipush 77
      // 29b1: sipush 590
      // 29b4: iastore
      // 29b5: aload 0
      // 29b6: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 29b9: bipush 35
      // 29bb: aaload
      // 29bc: bipush 100
      // 29be: sipush 589
      // 29c1: iastore
      // 29c2: aload 0
      // 29c3: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 29c6: bipush 35
      // 29c8: aaload
      // 29c9: bipush 42
      // 29cb: sipush 588
      // 29ce: iastore
      // 29cf: aload 0
      // 29d0: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 29d3: bipush 120
      // 29d5: aaload
      // 29d6: bipush 75
      // 29d8: sipush 587
      // 29db: iastore
      // 29dc: aload 0
      // 29dd: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 29e0: bipush 35
      // 29e2: aaload
      // 29e3: bipush 23
      // 29e5: sipush 586
      // 29e8: iastore
      // 29e9: aload 0
      // 29ea: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 29ed: bipush 13
      // 29ef: aaload
      // 29f0: bipush 72
      // 29f2: sipush 585
      // 29f5: iastore
      // 29f6: aload 0
      // 29f7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 29fa: bipush 0
      // 29fb: aaload
      // 29fc: bipush 67
      // 29fe: sipush 584
      // 2a01: iastore
      // 2a02: aload 0
      // 2a03: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2a06: bipush 39
      // 2a08: aaload
      // 2a09: sipush 172
      // 2a0c: sipush 583
      // 2a0f: iastore
      // 2a10: aload 0
      // 2a11: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2a14: bipush 22
      // 2a16: aaload
      // 2a17: sipush 182
      // 2a1a: sipush 582
      // 2a1d: iastore
      // 2a1e: aload 0
      // 2a1f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2a22: bipush 15
      // 2a24: aaload
      // 2a25: sipush 186
      // 2a28: sipush 581
      // 2a2b: iastore
      // 2a2c: aload 0
      // 2a2d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2a30: bipush 15
      // 2a32: aaload
      // 2a33: sipush 165
      // 2a36: sipush 580
      // 2a39: iastore
      // 2a3a: aload 0
      // 2a3b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2a3e: bipush 35
      // 2a40: aaload
      // 2a41: bipush 44
      // 2a43: sipush 579
      // 2a46: iastore
      // 2a47: aload 0
      // 2a48: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2a4b: bipush 40
      // 2a4d: aaload
      // 2a4e: bipush 13
      // 2a50: sipush 578
      // 2a53: iastore
      // 2a54: aload 0
      // 2a55: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2a58: bipush 38
      // 2a5a: aaload
      // 2a5b: bipush 1
      // 2a5c: sipush 577
      // 2a5f: iastore
      // 2a60: aload 0
      // 2a61: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2a64: bipush 37
      // 2a66: aaload
      // 2a67: bipush 33
      // 2a69: sipush 576
      // 2a6c: iastore
      // 2a6d: aload 0
      // 2a6e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2a71: bipush 36
      // 2a73: aaload
      // 2a74: bipush 24
      // 2a76: sipush 575
      // 2a79: iastore
      // 2a7a: aload 0
      // 2a7b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2a7e: bipush 56
      // 2a80: aaload
      // 2a81: bipush 4
      // 2a82: sipush 574
      // 2a85: iastore
      // 2a86: aload 0
      // 2a87: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2a8a: bipush 35
      // 2a8c: aaload
      // 2a8d: bipush 29
      // 2a8f: sipush 573
      // 2a92: iastore
      // 2a93: aload 0
      // 2a94: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2a97: bipush 9
      // 2a99: aaload
      // 2a9a: bipush 96
      // 2a9c: sipush 572
      // 2a9f: iastore
      // 2aa0: aload 0
      // 2aa1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2aa4: bipush 37
      // 2aa6: aaload
      // 2aa7: bipush 62
      // 2aa9: sipush 571
      // 2aac: iastore
      // 2aad: aload 0
      // 2aae: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2ab1: bipush 48
      // 2ab3: aaload
      // 2ab4: bipush 47
      // 2ab6: sipush 570
      // 2ab9: iastore
      // 2aba: aload 0
      // 2abb: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2abe: bipush 51
      // 2ac0: aaload
      // 2ac1: bipush 14
      // 2ac3: sipush 569
      // 2ac6: iastore
      // 2ac7: aload 0
      // 2ac8: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2acb: bipush 39
      // 2acd: aaload
      // 2ace: bipush 122
      // 2ad0: sipush 568
      // 2ad3: iastore
      // 2ad4: aload 0
      // 2ad5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2ad8: bipush 44
      // 2ada: aaload
      // 2adb: bipush 46
      // 2add: sipush 567
      // 2ae0: iastore
      // 2ae1: aload 0
      // 2ae2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2ae5: bipush 35
      // 2ae7: aaload
      // 2ae8: bipush 21
      // 2aea: sipush 566
      // 2aed: iastore
      // 2aee: aload 0
      // 2aef: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2af2: bipush 36
      // 2af4: aaload
      // 2af5: bipush 8
      // 2af7: sipush 565
      // 2afa: iastore
      // 2afb: aload 0
      // 2afc: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2aff: bipush 36
      // 2b01: aaload
      // 2b02: sipush 141
      // 2b05: sipush 564
      // 2b08: iastore
      // 2b09: aload 0
      // 2b0a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2b0d: bipush 3
      // 2b0e: aaload
      // 2b0f: bipush 81
      // 2b11: sipush 563
      // 2b14: iastore
      // 2b15: aload 0
      // 2b16: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2b19: bipush 37
      // 2b1b: aaload
      // 2b1c: sipush 155
      // 2b1f: sipush 562
      // 2b22: iastore
      // 2b23: aload 0
      // 2b24: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2b27: bipush 42
      // 2b29: aaload
      // 2b2a: bipush 84
      // 2b2c: sipush 561
      // 2b2f: iastore
      // 2b30: aload 0
      // 2b31: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2b34: bipush 36
      // 2b36: aaload
      // 2b37: bipush 40
      // 2b39: sipush 560
      // 2b3c: iastore
      // 2b3d: aload 0
      // 2b3e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2b41: bipush 35
      // 2b43: aaload
      // 2b44: bipush 103
      // 2b46: sipush 559
      // 2b49: iastore
      // 2b4a: aload 0
      // 2b4b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2b4e: bipush 11
      // 2b50: aaload
      // 2b51: bipush 84
      // 2b53: sipush 558
      // 2b56: iastore
      // 2b57: aload 0
      // 2b58: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2b5b: bipush 45
      // 2b5d: aaload
      // 2b5e: bipush 33
      // 2b60: sipush 557
      // 2b63: iastore
      // 2b64: aload 0
      // 2b65: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2b68: bipush 121
      // 2b6a: aaload
      // 2b6b: bipush 79
      // 2b6d: sipush 556
      // 2b70: iastore
      // 2b71: aload 0
      // 2b72: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2b75: bipush 2
      // 2b76: aaload
      // 2b77: bipush 77
      // 2b79: sipush 555
      // 2b7c: iastore
      // 2b7d: aload 0
      // 2b7e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2b81: bipush 36
      // 2b83: aaload
      // 2b84: bipush 41
      // 2b86: sipush 554
      // 2b89: iastore
      // 2b8a: aload 0
      // 2b8b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2b8e: bipush 37
      // 2b90: aaload
      // 2b91: bipush 47
      // 2b93: sipush 553
      // 2b96: iastore
      // 2b97: aload 0
      // 2b98: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2b9b: bipush 39
      // 2b9d: aaload
      // 2b9e: bipush 125
      // 2ba0: sipush 552
      // 2ba3: iastore
      // 2ba4: aload 0
      // 2ba5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2ba8: bipush 37
      // 2baa: aaload
      // 2bab: bipush 26
      // 2bad: sipush 551
      // 2bb0: iastore
      // 2bb1: aload 0
      // 2bb2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2bb5: bipush 35
      // 2bb7: aaload
      // 2bb8: bipush 48
      // 2bba: sipush 550
      // 2bbd: iastore
      // 2bbe: aload 0
      // 2bbf: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2bc2: bipush 35
      // 2bc4: aaload
      // 2bc5: bipush 28
      // 2bc7: sipush 549
      // 2bca: iastore
      // 2bcb: aload 0
      // 2bcc: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2bcf: bipush 35
      // 2bd1: aaload
      // 2bd2: sipush 159
      // 2bd5: sipush 548
      // 2bd8: iastore
      // 2bd9: aload 0
      // 2bda: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2bdd: bipush 37
      // 2bdf: aaload
      // 2be0: bipush 40
      // 2be2: sipush 547
      // 2be5: iastore
      // 2be6: aload 0
      // 2be7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2bea: bipush 35
      // 2bec: aaload
      // 2bed: sipush 145
      // 2bf0: sipush 546
      // 2bf3: iastore
      // 2bf4: aload 0
      // 2bf5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2bf8: bipush 37
      // 2bfa: aaload
      // 2bfb: sipush 147
      // 2bfe: sipush 545
      // 2c01: iastore
      // 2c02: aload 0
      // 2c03: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2c06: bipush 46
      // 2c08: aaload
      // 2c09: sipush 160
      // 2c0c: sipush 544
      // 2c0f: iastore
      // 2c10: aload 0
      // 2c11: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2c14: bipush 37
      // 2c16: aaload
      // 2c17: bipush 46
      // 2c19: sipush 543
      // 2c1c: iastore
      // 2c1d: aload 0
      // 2c1e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2c21: bipush 50
      // 2c23: aaload
      // 2c24: bipush 99
      // 2c26: sipush 542
      // 2c29: iastore
      // 2c2a: aload 0
      // 2c2b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2c2e: bipush 52
      // 2c30: aaload
      // 2c31: bipush 13
      // 2c33: sipush 541
      // 2c36: iastore
      // 2c37: aload 0
      // 2c38: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2c3b: bipush 10
      // 2c3d: aaload
      // 2c3e: bipush 82
      // 2c40: sipush 540
      // 2c43: iastore
      // 2c44: aload 0
      // 2c45: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2c48: bipush 35
      // 2c4a: aaload
      // 2c4b: sipush 169
      // 2c4e: sipush 539
      // 2c51: iastore
      // 2c52: aload 0
      // 2c53: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2c56: bipush 35
      // 2c58: aaload
      // 2c59: bipush 31
      // 2c5b: sipush 538
      // 2c5e: iastore
      // 2c5f: aload 0
      // 2c60: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2c63: bipush 47
      // 2c65: aaload
      // 2c66: bipush 31
      // 2c68: sipush 537
      // 2c6b: iastore
      // 2c6c: aload 0
      // 2c6d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2c70: bipush 18
      // 2c72: aaload
      // 2c73: bipush 79
      // 2c75: sipush 536
      // 2c78: iastore
      // 2c79: aload 0
      // 2c7a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2c7d: bipush 16
      // 2c7f: aaload
      // 2c80: bipush 113
      // 2c82: sipush 535
      // 2c85: iastore
      // 2c86: aload 0
      // 2c87: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2c8a: bipush 37
      // 2c8c: aaload
      // 2c8d: bipush 104
      // 2c8f: sipush 534
      // 2c92: iastore
      // 2c93: aload 0
      // 2c94: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2c97: bipush 39
      // 2c99: aaload
      // 2c9a: sipush 134
      // 2c9d: sipush 533
      // 2ca0: iastore
      // 2ca1: aload 0
      // 2ca2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2ca5: bipush 36
      // 2ca7: aaload
      // 2ca8: bipush 53
      // 2caa: sipush 532
      // 2cad: iastore
      // 2cae: aload 0
      // 2caf: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2cb2: bipush 38
      // 2cb4: aaload
      // 2cb5: bipush 0
      // 2cb6: sipush 531
      // 2cb9: iastore
      // 2cba: aload 0
      // 2cbb: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2cbe: bipush 4
      // 2cbf: aaload
      // 2cc0: bipush 86
      // 2cc2: sipush 530
      // 2cc5: iastore
      // 2cc6: aload 0
      // 2cc7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2cca: bipush 54
      // 2ccc: aaload
      // 2ccd: bipush 17
      // 2ccf: sipush 529
      // 2cd2: iastore
      // 2cd3: aload 0
      // 2cd4: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2cd7: bipush 43
      // 2cd9: aaload
      // 2cda: sipush 157
      // 2cdd: sipush 528
      // 2ce0: iastore
      // 2ce1: aload 0
      // 2ce2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2ce5: bipush 35
      // 2ce7: aaload
      // 2ce8: sipush 165
      // 2ceb: sipush 527
      // 2cee: iastore
      // 2cef: aload 0
      // 2cf0: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2cf3: bipush 69
      // 2cf5: aaload
      // 2cf6: sipush 147
      // 2cf9: sipush 526
      // 2cfc: iastore
      // 2cfd: aload 0
      // 2cfe: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2d01: bipush 117
      // 2d03: aaload
      // 2d04: bipush 95
      // 2d06: sipush 525
      // 2d09: iastore
      // 2d0a: aload 0
      // 2d0b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2d0e: bipush 35
      // 2d10: aaload
      // 2d11: sipush 162
      // 2d14: sipush 524
      // 2d17: iastore
      // 2d18: aload 0
      // 2d19: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2d1c: bipush 35
      // 2d1e: aaload
      // 2d1f: bipush 17
      // 2d21: sipush 523
      // 2d24: iastore
      // 2d25: aload 0
      // 2d26: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2d29: bipush 36
      // 2d2b: aaload
      // 2d2c: sipush 142
      // 2d2f: sipush 522
      // 2d32: iastore
      // 2d33: aload 0
      // 2d34: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2d37: bipush 36
      // 2d39: aaload
      // 2d3a: bipush 4
      // 2d3b: sipush 521
      // 2d3e: iastore
      // 2d3f: aload 0
      // 2d40: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2d43: bipush 37
      // 2d45: aaload
      // 2d46: sipush 166
      // 2d49: sipush 520
      // 2d4c: iastore
      // 2d4d: aload 0
      // 2d4e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2d51: bipush 35
      // 2d53: aaload
      // 2d54: sipush 168
      // 2d57: sipush 519
      // 2d5a: iastore
      // 2d5b: aload 0
      // 2d5c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2d5f: bipush 35
      // 2d61: aaload
      // 2d62: bipush 19
      // 2d64: sipush 518
      // 2d67: iastore
      // 2d68: aload 0
      // 2d69: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2d6c: bipush 37
      // 2d6e: aaload
      // 2d6f: bipush 48
      // 2d71: sipush 517
      // 2d74: iastore
      // 2d75: aload 0
      // 2d76: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2d79: bipush 42
      // 2d7b: aaload
      // 2d7c: bipush 37
      // 2d7e: sipush 516
      // 2d81: iastore
      // 2d82: aload 0
      // 2d83: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2d86: bipush 40
      // 2d88: aaload
      // 2d89: sipush 146
      // 2d8c: sipush 515
      // 2d8f: iastore
      // 2d90: aload 0
      // 2d91: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2d94: bipush 36
      // 2d96: aaload
      // 2d97: bipush 123
      // 2d99: sipush 514
      // 2d9c: iastore
      // 2d9d: aload 0
      // 2d9e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2da1: bipush 22
      // 2da3: aaload
      // 2da4: bipush 41
      // 2da6: sipush 513
      // 2da9: iastore
      // 2daa: aload 0
      // 2dab: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2dae: bipush 20
      // 2db0: aaload
      // 2db1: bipush 119
      // 2db3: sipush 512
      // 2db6: iastore
      // 2db7: aload 0
      // 2db8: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2dbb: bipush 2
      // 2dbc: aaload
      // 2dbd: bipush 74
      // 2dbf: sipush 511
      // 2dc2: iastore
      // 2dc3: aload 0
      // 2dc4: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2dc7: bipush 44
      // 2dc9: aaload
      // 2dca: bipush 113
      // 2dcc: sipush 510
      // 2dcf: iastore
      // 2dd0: aload 0
      // 2dd1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2dd4: bipush 35
      // 2dd6: aaload
      // 2dd7: bipush 125
      // 2dd9: sipush 509
      // 2ddc: iastore
      // 2ddd: aload 0
      // 2dde: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2de1: bipush 37
      // 2de3: aaload
      // 2de4: bipush 16
      // 2de6: sipush 508
      // 2de9: iastore
      // 2dea: aload 0
      // 2deb: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2dee: bipush 35
      // 2df0: aaload
      // 2df1: bipush 20
      // 2df3: sipush 507
      // 2df6: iastore
      // 2df7: aload 0
      // 2df8: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2dfb: bipush 35
      // 2dfd: aaload
      // 2dfe: bipush 55
      // 2e00: sipush 506
      // 2e03: iastore
      // 2e04: aload 0
      // 2e05: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2e08: bipush 37
      // 2e0a: aaload
      // 2e0b: sipush 145
      // 2e0e: sipush 505
      // 2e11: iastore
      // 2e12: aload 0
      // 2e13: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2e16: bipush 0
      // 2e17: aaload
      // 2e18: bipush 88
      // 2e1a: sipush 504
      // 2e1d: iastore
      // 2e1e: aload 0
      // 2e1f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2e22: bipush 3
      // 2e23: aaload
      // 2e24: bipush 94
      // 2e26: sipush 503
      // 2e29: iastore
      // 2e2a: aload 0
      // 2e2b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2e2e: bipush 6
      // 2e30: aaload
      // 2e31: bipush 65
      // 2e33: sipush 502
      // 2e36: iastore
      // 2e37: aload 0
      // 2e38: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2e3b: bipush 26
      // 2e3d: aaload
      // 2e3e: bipush 15
      // 2e40: sipush 501
      // 2e43: iastore
      // 2e44: aload 0
      // 2e45: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2e48: bipush 41
      // 2e4a: aaload
      // 2e4b: bipush 126
      // 2e4d: sipush 500
      // 2e50: iastore
      // 2e51: aload 0
      // 2e52: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2e55: bipush 36
      // 2e57: aaload
      // 2e58: sipush 129
      // 2e5b: sipush 499
      // 2e5e: iastore
      // 2e5f: aload 0
      // 2e60: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2e63: bipush 31
      // 2e65: aaload
      // 2e66: bipush 75
      // 2e68: sipush 498
      // 2e6b: iastore
      // 2e6c: aload 0
      // 2e6d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2e70: bipush 19
      // 2e72: aaload
      // 2e73: bipush 61
      // 2e75: sipush 497
      // 2e78: iastore
      // 2e79: aload 0
      // 2e7a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2e7d: bipush 35
      // 2e7f: aaload
      // 2e80: sipush 128
      // 2e83: sipush 496
      // 2e86: iastore
      // 2e87: aload 0
      // 2e88: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2e8b: bipush 29
      // 2e8d: aaload
      // 2e8e: bipush 79
      // 2e90: sipush 495
      // 2e93: iastore
      // 2e94: aload 0
      // 2e95: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2e98: bipush 36
      // 2e9a: aaload
      // 2e9b: bipush 62
      // 2e9d: sipush 494
      // 2ea0: iastore
      // 2ea1: aload 0
      // 2ea2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2ea5: bipush 37
      // 2ea7: aaload
      // 2ea8: sipush 189
      // 2eab: sipush 493
      // 2eae: iastore
      // 2eaf: aload 0
      // 2eb0: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2eb3: bipush 39
      // 2eb5: aaload
      // 2eb6: bipush 109
      // 2eb8: sipush 492
      // 2ebb: iastore
      // 2ebc: aload 0
      // 2ebd: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2ec0: bipush 39
      // 2ec2: aaload
      // 2ec3: sipush 135
      // 2ec6: sipush 491
      // 2ec9: iastore
      // 2eca: aload 0
      // 2ecb: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2ece: bipush 72
      // 2ed0: aaload
      // 2ed1: bipush 15
      // 2ed3: sipush 490
      // 2ed6: iastore
      // 2ed7: aload 0
      // 2ed8: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2edb: bipush 47
      // 2edd: aaload
      // 2ede: bipush 106
      // 2ee0: sipush 489
      // 2ee3: iastore
      // 2ee4: aload 0
      // 2ee5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2ee8: bipush 54
      // 2eea: aaload
      // 2eeb: bipush 14
      // 2eed: sipush 488
      // 2ef0: iastore
      // 2ef1: aload 0
      // 2ef2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2ef5: bipush 24
      // 2ef7: aaload
      // 2ef8: bipush 52
      // 2efa: sipush 487
      // 2efd: iastore
      // 2efe: aload 0
      // 2eff: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2f02: bipush 38
      // 2f04: aaload
      // 2f05: sipush 162
      // 2f08: sipush 486
      // 2f0b: iastore
      // 2f0c: aload 0
      // 2f0d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2f10: bipush 41
      // 2f12: aaload
      // 2f13: bipush 43
      // 2f15: sipush 485
      // 2f18: iastore
      // 2f19: aload 0
      // 2f1a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2f1d: bipush 37
      // 2f1f: aaload
      // 2f20: bipush 121
      // 2f22: sipush 484
      // 2f25: iastore
      // 2f26: aload 0
      // 2f27: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2f2a: bipush 14
      // 2f2c: aaload
      // 2f2d: bipush 66
      // 2f2f: sipush 483
      // 2f32: iastore
      // 2f33: aload 0
      // 2f34: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2f37: bipush 37
      // 2f39: aaload
      // 2f3a: bipush 30
      // 2f3c: sipush 482
      // 2f3f: iastore
      // 2f40: aload 0
      // 2f41: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2f44: bipush 35
      // 2f46: aaload
      // 2f47: bipush 7
      // 2f49: sipush 481
      // 2f4c: iastore
      // 2f4d: aload 0
      // 2f4e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2f51: bipush 49
      // 2f53: aaload
      // 2f54: bipush 58
      // 2f56: sipush 480
      // 2f59: iastore
      // 2f5a: aload 0
      // 2f5b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2f5e: bipush 43
      // 2f60: aaload
      // 2f61: sipush 188
      // 2f64: sipush 479
      // 2f67: iastore
      // 2f68: aload 0
      // 2f69: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2f6c: bipush 24
      // 2f6e: aaload
      // 2f6f: bipush 66
      // 2f71: sipush 478
      // 2f74: iastore
      // 2f75: aload 0
      // 2f76: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2f79: bipush 35
      // 2f7b: aaload
      // 2f7c: sipush 171
      // 2f7f: sipush 477
      // 2f82: iastore
      // 2f83: aload 0
      // 2f84: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2f87: bipush 40
      // 2f89: aaload
      // 2f8a: sipush 186
      // 2f8d: sipush 476
      // 2f90: iastore
      // 2f91: aload 0
      // 2f92: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2f95: bipush 39
      // 2f97: aaload
      // 2f98: sipush 164
      // 2f9b: sipush 475
      // 2f9e: iastore
      // 2f9f: aload 0
      // 2fa0: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2fa3: bipush 78
      // 2fa5: aaload
      // 2fa6: sipush 186
      // 2fa9: sipush 474
      // 2fac: iastore
      // 2fad: aload 0
      // 2fae: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2fb1: bipush 8
      // 2fb3: aaload
      // 2fb4: bipush 72
      // 2fb6: sipush 473
      // 2fb9: iastore
      // 2fba: aload 0
      // 2fbb: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2fbe: bipush 36
      // 2fc0: aaload
      // 2fc1: sipush 190
      // 2fc4: sipush 472
      // 2fc7: iastore
      // 2fc8: aload 0
      // 2fc9: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2fcc: bipush 35
      // 2fce: aaload
      // 2fcf: bipush 53
      // 2fd1: sipush 471
      // 2fd4: iastore
      // 2fd5: aload 0
      // 2fd6: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2fd9: bipush 35
      // 2fdb: aaload
      // 2fdc: bipush 54
      // 2fde: sipush 470
      // 2fe1: iastore
      // 2fe2: aload 0
      // 2fe3: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2fe6: bipush 22
      // 2fe8: aaload
      // 2fe9: sipush 159
      // 2fec: sipush 469
      // 2fef: iastore
      // 2ff0: aload 0
      // 2ff1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 2ff4: bipush 35
      // 2ff6: aaload
      // 2ff7: bipush 9
      // 2ff9: sipush 468
      // 2ffc: iastore
      // 2ffd: aload 0
      // 2ffe: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3001: bipush 41
      // 3003: aaload
      // 3004: sipush 140
      // 3007: sipush 467
      // 300a: iastore
      // 300b: aload 0
      // 300c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 300f: bipush 37
      // 3011: aaload
      // 3012: bipush 22
      // 3014: sipush 466
      // 3017: iastore
      // 3018: aload 0
      // 3019: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 301c: bipush 48
      // 301e: aaload
      // 301f: bipush 97
      // 3021: sipush 465
      // 3024: iastore
      // 3025: aload 0
      // 3026: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3029: bipush 50
      // 302b: aaload
      // 302c: bipush 97
      // 302e: sipush 464
      // 3031: iastore
      // 3032: aload 0
      // 3033: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3036: bipush 36
      // 3038: aaload
      // 3039: bipush 127
      // 303b: sipush 463
      // 303e: iastore
      // 303f: aload 0
      // 3040: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3043: bipush 37
      // 3045: aaload
      // 3046: bipush 23
      // 3048: sipush 462
      // 304b: iastore
      // 304c: aload 0
      // 304d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3050: bipush 40
      // 3052: aaload
      // 3053: bipush 55
      // 3055: sipush 461
      // 3058: iastore
      // 3059: aload 0
      // 305a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 305d: bipush 35
      // 305f: aaload
      // 3060: bipush 43
      // 3062: sipush 460
      // 3065: iastore
      // 3066: aload 0
      // 3067: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 306a: bipush 26
      // 306c: aaload
      // 306d: bipush 22
      // 306f: sipush 459
      // 3072: iastore
      // 3073: aload 0
      // 3074: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3077: bipush 35
      // 3079: aaload
      // 307a: bipush 15
      // 307c: sipush 458
      // 307f: iastore
      // 3080: aload 0
      // 3081: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3084: bipush 72
      // 3086: aaload
      // 3087: sipush 179
      // 308a: sipush 457
      // 308d: iastore
      // 308e: aload 0
      // 308f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3092: bipush 20
      // 3094: aaload
      // 3095: sipush 129
      // 3098: sipush 456
      // 309b: iastore
      // 309c: aload 0
      // 309d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 30a0: bipush 52
      // 30a2: aaload
      // 30a3: bipush 101
      // 30a5: sipush 455
      // 30a8: iastore
      // 30a9: aload 0
      // 30aa: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 30ad: bipush 35
      // 30af: aaload
      // 30b0: bipush 12
      // 30b2: sipush 454
      // 30b5: iastore
      // 30b6: aload 0
      // 30b7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 30ba: bipush 42
      // 30bc: aaload
      // 30bd: sipush 156
      // 30c0: sipush 453
      // 30c3: iastore
      // 30c4: aload 0
      // 30c5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 30c8: bipush 15
      // 30ca: aaload
      // 30cb: sipush 157
      // 30ce: sipush 452
      // 30d1: iastore
      // 30d2: aload 0
      // 30d3: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 30d6: bipush 50
      // 30d8: aaload
      // 30d9: sipush 140
      // 30dc: sipush 451
      // 30df: iastore
      // 30e0: aload 0
      // 30e1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 30e4: bipush 26
      // 30e6: aaload
      // 30e7: bipush 28
      // 30e9: sipush 450
      // 30ec: iastore
      // 30ed: aload 0
      // 30ee: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 30f1: bipush 54
      // 30f3: aaload
      // 30f4: bipush 51
      // 30f6: sipush 449
      // 30f9: iastore
      // 30fa: aload 0
      // 30fb: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 30fe: bipush 35
      // 3100: aaload
      // 3101: bipush 112
      // 3103: sipush 448
      // 3106: iastore
      // 3107: aload 0
      // 3108: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 310b: bipush 36
      // 310d: aaload
      // 310e: bipush 116
      // 3110: sipush 447
      // 3113: iastore
      // 3114: aload 0
      // 3115: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3118: bipush 42
      // 311a: aaload
      // 311b: bipush 11
      // 311d: sipush 446
      // 3120: iastore
      // 3121: aload 0
      // 3122: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3125: bipush 37
      // 3127: aaload
      // 3128: sipush 172
      // 312b: sipush 445
      // 312e: iastore
      // 312f: aload 0
      // 3130: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3133: bipush 37
      // 3135: aaload
      // 3136: bipush 29
      // 3138: sipush 444
      // 313b: iastore
      // 313c: aload 0
      // 313d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3140: bipush 44
      // 3142: aaload
      // 3143: bipush 107
      // 3145: sipush 443
      // 3148: iastore
      // 3149: aload 0
      // 314a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 314d: bipush 50
      // 314f: aaload
      // 3150: bipush 17
      // 3152: sipush 442
      // 3155: iastore
      // 3156: aload 0
      // 3157: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 315a: bipush 39
      // 315c: aaload
      // 315d: bipush 107
      // 315f: sipush 441
      // 3162: iastore
      // 3163: aload 0
      // 3164: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3167: bipush 19
      // 3169: aaload
      // 316a: bipush 109
      // 316c: sipush 440
      // 316f: iastore
      // 3170: aload 0
      // 3171: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3174: bipush 36
      // 3176: aaload
      // 3177: bipush 60
      // 3179: sipush 439
      // 317c: iastore
      // 317d: aload 0
      // 317e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3181: bipush 49
      // 3183: aaload
      // 3184: sipush 132
      // 3187: sipush 438
      // 318a: iastore
      // 318b: aload 0
      // 318c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 318f: bipush 26
      // 3191: aaload
      // 3192: bipush 16
      // 3194: sipush 437
      // 3197: iastore
      // 3198: aload 0
      // 3199: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 319c: bipush 43
      // 319e: aaload
      // 319f: sipush 155
      // 31a2: sipush 436
      // 31a5: iastore
      // 31a6: aload 0
      // 31a7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 31aa: bipush 37
      // 31ac: aaload
      // 31ad: bipush 120
      // 31af: sipush 435
      // 31b2: iastore
      // 31b3: aload 0
      // 31b4: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 31b7: bipush 15
      // 31b9: aaload
      // 31ba: sipush 159
      // 31bd: sipush 434
      // 31c0: iastore
      // 31c1: aload 0
      // 31c2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 31c5: bipush 43
      // 31c7: aaload
      // 31c8: bipush 6
      // 31ca: sipush 433
      // 31cd: iastore
      // 31ce: aload 0
      // 31cf: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 31d2: bipush 45
      // 31d4: aaload
      // 31d5: sipush 188
      // 31d8: sipush 432
      // 31db: iastore
      // 31dc: aload 0
      // 31dd: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 31e0: bipush 35
      // 31e2: aaload
      // 31e3: bipush 38
      // 31e5: sipush 431
      // 31e8: iastore
      // 31e9: aload 0
      // 31ea: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 31ed: bipush 39
      // 31ef: aaload
      // 31f0: sipush 143
      // 31f3: sipush 430
      // 31f6: iastore
      // 31f7: aload 0
      // 31f8: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 31fb: bipush 48
      // 31fd: aaload
      // 31fe: sipush 144
      // 3201: sipush 429
      // 3204: iastore
      // 3205: aload 0
      // 3206: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3209: bipush 37
      // 320b: aaload
      // 320c: sipush 168
      // 320f: sipush 428
      // 3212: iastore
      // 3213: aload 0
      // 3214: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3217: bipush 37
      // 3219: aaload
      // 321a: bipush 1
      // 321b: sipush 427
      // 321e: iastore
      // 321f: aload 0
      // 3220: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3223: bipush 36
      // 3225: aaload
      // 3226: bipush 109
      // 3228: sipush 426
      // 322b: iastore
      // 322c: aload 0
      // 322d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3230: bipush 46
      // 3232: aaload
      // 3233: bipush 53
      // 3235: sipush 425
      // 3238: iastore
      // 3239: aload 0
      // 323a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 323d: bipush 38
      // 323f: aaload
      // 3240: bipush 54
      // 3242: sipush 424
      // 3245: iastore
      // 3246: aload 0
      // 3247: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 324a: bipush 36
      // 324c: aaload
      // 324d: bipush 0
      // 324e: sipush 423
      // 3251: iastore
      // 3252: aload 0
      // 3253: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3256: bipush 72
      // 3258: aaload
      // 3259: bipush 33
      // 325b: sipush 422
      // 325e: iastore
      // 325f: aload 0
      // 3260: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3263: bipush 42
      // 3265: aaload
      // 3266: bipush 8
      // 3268: sipush 421
      // 326b: iastore
      // 326c: aload 0
      // 326d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3270: bipush 36
      // 3272: aaload
      // 3273: bipush 31
      // 3275: sipush 420
      // 3278: iastore
      // 3279: aload 0
      // 327a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 327d: bipush 35
      // 327f: aaload
      // 3280: sipush 150
      // 3283: sipush 419
      // 3286: iastore
      // 3287: aload 0
      // 3288: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 328b: bipush 118
      // 328d: aaload
      // 328e: bipush 93
      // 3290: sipush 418
      // 3293: iastore
      // 3294: aload 0
      // 3295: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3298: bipush 37
      // 329a: aaload
      // 329b: bipush 61
      // 329d: sipush 417
      // 32a0: iastore
      // 32a1: aload 0
      // 32a2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 32a5: bipush 0
      // 32a6: aaload
      // 32a7: bipush 85
      // 32a9: sipush 416
      // 32ac: iastore
      // 32ad: aload 0
      // 32ae: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 32b1: bipush 36
      // 32b3: aaload
      // 32b4: bipush 27
      // 32b6: sipush 415
      // 32b9: iastore
      // 32ba: aload 0
      // 32bb: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 32be: bipush 35
      // 32c0: aaload
      // 32c1: sipush 134
      // 32c4: sipush 414
      // 32c7: iastore
      // 32c8: aload 0
      // 32c9: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 32cc: bipush 36
      // 32ce: aaload
      // 32cf: sipush 145
      // 32d2: sipush 413
      // 32d5: iastore
      // 32d6: aload 0
      // 32d7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 32da: bipush 6
      // 32dc: aaload
      // 32dd: bipush 96
      // 32df: sipush 412
      // 32e2: iastore
      // 32e3: aload 0
      // 32e4: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 32e7: bipush 36
      // 32e9: aaload
      // 32ea: bipush 14
      // 32ec: sipush 411
      // 32ef: iastore
      // 32f0: aload 0
      // 32f1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 32f4: bipush 16
      // 32f6: aaload
      // 32f7: bipush 36
      // 32f9: sipush 410
      // 32fc: iastore
      // 32fd: aload 0
      // 32fe: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3301: bipush 15
      // 3303: aaload
      // 3304: sipush 175
      // 3307: sipush 409
      // 330a: iastore
      // 330b: aload 0
      // 330c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 330f: bipush 35
      // 3311: aaload
      // 3312: bipush 10
      // 3314: sipush 408
      // 3317: iastore
      // 3318: aload 0
      // 3319: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 331c: bipush 36
      // 331e: aaload
      // 331f: sipush 189
      // 3322: sipush 407
      // 3325: iastore
      // 3326: aload 0
      // 3327: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 332a: bipush 35
      // 332c: aaload
      // 332d: bipush 51
      // 332f: sipush 406
      // 3332: iastore
      // 3333: aload 0
      // 3334: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3337: bipush 35
      // 3339: aaload
      // 333a: bipush 109
      // 333c: sipush 405
      // 333f: iastore
      // 3340: aload 0
      // 3341: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3344: bipush 35
      // 3346: aaload
      // 3347: sipush 147
      // 334a: sipush 404
      // 334d: iastore
      // 334e: aload 0
      // 334f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3352: bipush 35
      // 3354: aaload
      // 3355: sipush 180
      // 3358: sipush 403
      // 335b: iastore
      // 335c: aload 0
      // 335d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3360: bipush 72
      // 3362: aaload
      // 3363: bipush 5
      // 3364: sipush 402
      // 3367: iastore
      // 3368: aload 0
      // 3369: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 336c: bipush 36
      // 336e: aaload
      // 336f: bipush 107
      // 3371: sipush 401
      // 3374: iastore
      // 3375: aload 0
      // 3376: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3379: bipush 49
      // 337b: aaload
      // 337c: bipush 116
      // 337e: sipush 400
      // 3381: iastore
      // 3382: aload 0
      // 3383: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3386: bipush 73
      // 3388: aaload
      // 3389: bipush 30
      // 338b: sipush 399
      // 338e: iastore
      // 338f: aload 0
      // 3390: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3393: bipush 6
      // 3395: aaload
      // 3396: bipush 90
      // 3398: sipush 398
      // 339b: iastore
      // 339c: aload 0
      // 339d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 33a0: bipush 2
      // 33a1: aaload
      // 33a2: bipush 70
      // 33a4: sipush 397
      // 33a7: iastore
      // 33a8: aload 0
      // 33a9: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 33ac: bipush 17
      // 33ae: aaload
      // 33af: sipush 141
      // 33b2: sipush 396
      // 33b5: iastore
      // 33b6: aload 0
      // 33b7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 33ba: bipush 35
      // 33bc: aaload
      // 33bd: bipush 62
      // 33bf: sipush 395
      // 33c2: iastore
      // 33c3: aload 0
      // 33c4: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 33c7: bipush 16
      // 33c9: aaload
      // 33ca: sipush 180
      // 33cd: sipush 394
      // 33d0: iastore
      // 33d1: aload 0
      // 33d2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 33d5: bipush 4
      // 33d6: aaload
      // 33d7: bipush 91
      // 33d9: sipush 393
      // 33dc: iastore
      // 33dd: aload 0
      // 33de: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 33e1: bipush 15
      // 33e3: aaload
      // 33e4: sipush 171
      // 33e7: sipush 392
      // 33ea: iastore
      // 33eb: aload 0
      // 33ec: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 33ef: bipush 35
      // 33f1: aaload
      // 33f2: sipush 177
      // 33f5: sipush 391
      // 33f8: iastore
      // 33f9: aload 0
      // 33fa: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 33fd: bipush 37
      // 33ff: aaload
      // 3400: sipush 173
      // 3403: sipush 390
      // 3406: iastore
      // 3407: aload 0
      // 3408: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 340b: bipush 16
      // 340d: aaload
      // 340e: bipush 121
      // 3410: sipush 389
      // 3413: iastore
      // 3414: aload 0
      // 3415: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3418: bipush 35
      // 341a: aaload
      // 341b: bipush 5
      // 341c: sipush 388
      // 341f: iastore
      // 3420: aload 0
      // 3421: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3424: bipush 46
      // 3426: aaload
      // 3427: bipush 122
      // 3429: sipush 387
      // 342c: iastore
      // 342d: aload 0
      // 342e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3431: bipush 40
      // 3433: aaload
      // 3434: sipush 138
      // 3437: sipush 386
      // 343a: iastore
      // 343b: aload 0
      // 343c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 343f: bipush 50
      // 3441: aaload
      // 3442: bipush 49
      // 3444: sipush 385
      // 3447: iastore
      // 3448: aload 0
      // 3449: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 344c: bipush 36
      // 344e: aaload
      // 344f: sipush 152
      // 3452: sipush 384
      // 3455: iastore
      // 3456: aload 0
      // 3457: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 345a: bipush 13
      // 345c: aaload
      // 345d: bipush 43
      // 345f: sipush 383
      // 3462: iastore
      // 3463: aload 0
      // 3464: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3467: bipush 9
      // 3469: aaload
      // 346a: bipush 88
      // 346c: sipush 382
      // 346f: iastore
      // 3470: aload 0
      // 3471: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3474: bipush 36
      // 3476: aaload
      // 3477: sipush 159
      // 347a: sipush 381
      // 347d: iastore
      // 347e: aload 0
      // 347f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3482: bipush 27
      // 3484: aaload
      // 3485: bipush 62
      // 3487: sipush 380
      // 348a: iastore
      // 348b: aload 0
      // 348c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 348f: bipush 40
      // 3491: aaload
      // 3492: bipush 18
      // 3494: sipush 379
      // 3497: iastore
      // 3498: aload 0
      // 3499: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 349c: bipush 17
      // 349e: aaload
      // 349f: sipush 129
      // 34a2: sipush 378
      // 34a5: iastore
      // 34a6: aload 0
      // 34a7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 34aa: bipush 43
      // 34ac: aaload
      // 34ad: bipush 97
      // 34af: sipush 377
      // 34b2: iastore
      // 34b3: aload 0
      // 34b4: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 34b7: bipush 13
      // 34b9: aaload
      // 34ba: sipush 131
      // 34bd: sipush 376
      // 34c0: iastore
      // 34c1: aload 0
      // 34c2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 34c5: bipush 46
      // 34c7: aaload
      // 34c8: bipush 107
      // 34ca: sipush 375
      // 34cd: iastore
      // 34ce: aload 0
      // 34cf: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 34d2: bipush 60
      // 34d4: aaload
      // 34d5: bipush 64
      // 34d7: sipush 374
      // 34da: iastore
      // 34db: aload 0
      // 34dc: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 34df: bipush 36
      // 34e1: aaload
      // 34e2: sipush 179
      // 34e5: sipush 373
      // 34e8: iastore
      // 34e9: aload 0
      // 34ea: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 34ed: bipush 37
      // 34ef: aaload
      // 34f0: bipush 55
      // 34f2: sipush 372
      // 34f5: iastore
      // 34f6: aload 0
      // 34f7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 34fa: bipush 41
      // 34fc: aaload
      // 34fd: sipush 173
      // 3500: sipush 371
      // 3503: iastore
      // 3504: aload 0
      // 3505: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3508: bipush 44
      // 350a: aaload
      // 350b: sipush 172
      // 350e: sipush 370
      // 3511: iastore
      // 3512: aload 0
      // 3513: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3516: bipush 23
      // 3518: aaload
      // 3519: sipush 187
      // 351c: sipush 369
      // 351f: iastore
      // 3520: aload 0
      // 3521: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3524: bipush 36
      // 3526: aaload
      // 3527: sipush 149
      // 352a: sipush 368
      // 352d: iastore
      // 352e: aload 0
      // 352f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3532: bipush 17
      // 3534: aaload
      // 3535: bipush 125
      // 3537: sipush 367
      // 353a: iastore
      // 353b: aload 0
      // 353c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 353f: bipush 55
      // 3541: aaload
      // 3542: sipush 180
      // 3545: sipush 366
      // 3548: iastore
      // 3549: aload 0
      // 354a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 354d: bipush 51
      // 354f: aaload
      // 3550: sipush 129
      // 3553: sipush 365
      // 3556: iastore
      // 3557: aload 0
      // 3558: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 355b: bipush 36
      // 355d: aaload
      // 355e: bipush 51
      // 3560: sipush 364
      // 3563: iastore
      // 3564: aload 0
      // 3565: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3568: bipush 37
      // 356a: aaload
      // 356b: bipush 122
      // 356d: sipush 363
      // 3570: iastore
      // 3571: aload 0
      // 3572: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3575: bipush 48
      // 3577: aaload
      // 3578: bipush 32
      // 357a: sipush 362
      // 357d: iastore
      // 357e: aload 0
      // 357f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3582: bipush 51
      // 3584: aaload
      // 3585: bipush 99
      // 3587: sipush 361
      // 358a: iastore
      // 358b: aload 0
      // 358c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 358f: bipush 54
      // 3591: aaload
      // 3592: bipush 16
      // 3594: sipush 360
      // 3597: iastore
      // 3598: aload 0
      // 3599: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 359c: bipush 41
      // 359e: aaload
      // 359f: sipush 183
      // 35a2: sipush 359
      // 35a5: iastore
      // 35a6: aload 0
      // 35a7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 35aa: bipush 37
      // 35ac: aaload
      // 35ad: sipush 179
      // 35b0: sipush 358
      // 35b3: iastore
      // 35b4: aload 0
      // 35b5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 35b8: bipush 38
      // 35ba: aaload
      // 35bb: sipush 179
      // 35be: sipush 357
      // 35c1: iastore
      // 35c2: aload 0
      // 35c3: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 35c6: bipush 35
      // 35c8: aaload
      // 35c9: sipush 143
      // 35cc: sipush 356
      // 35cf: iastore
      // 35d0: aload 0
      // 35d1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 35d4: bipush 37
      // 35d6: aaload
      // 35d7: bipush 24
      // 35d9: sipush 355
      // 35dc: iastore
      // 35dd: aload 0
      // 35de: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 35e1: bipush 40
      // 35e3: aaload
      // 35e4: sipush 177
      // 35e7: sipush 354
      // 35ea: iastore
      // 35eb: aload 0
      // 35ec: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 35ef: bipush 47
      // 35f1: aaload
      // 35f2: bipush 117
      // 35f4: sipush 353
      // 35f7: iastore
      // 35f8: aload 0
      // 35f9: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 35fc: bipush 39
      // 35fe: aaload
      // 35ff: bipush 52
      // 3601: sipush 352
      // 3604: iastore
      // 3605: aload 0
      // 3606: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3609: bipush 22
      // 360b: aaload
      // 360c: bipush 99
      // 360e: sipush 351
      // 3611: iastore
      // 3612: aload 0
      // 3613: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3616: bipush 40
      // 3618: aaload
      // 3619: sipush 142
      // 361c: sipush 350
      // 361f: iastore
      // 3620: aload 0
      // 3621: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3624: bipush 36
      // 3626: aaload
      // 3627: bipush 49
      // 3629: sipush 349
      // 362c: iastore
      // 362d: aload 0
      // 362e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3631: bipush 38
      // 3633: aaload
      // 3634: bipush 17
      // 3636: sipush 348
      // 3639: iastore
      // 363a: aload 0
      // 363b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 363e: bipush 39
      // 3640: aaload
      // 3641: sipush 188
      // 3644: sipush 347
      // 3647: iastore
      // 3648: aload 0
      // 3649: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 364c: bipush 36
      // 364e: aaload
      // 364f: sipush 186
      // 3652: sipush 346
      // 3655: iastore
      // 3656: aload 0
      // 3657: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 365a: bipush 35
      // 365c: aaload
      // 365d: sipush 189
      // 3660: sipush 345
      // 3663: iastore
      // 3664: aload 0
      // 3665: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3668: bipush 41
      // 366a: aaload
      // 366b: bipush 7
      // 366d: sipush 344
      // 3670: iastore
      // 3671: aload 0
      // 3672: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3675: bipush 18
      // 3677: aaload
      // 3678: bipush 91
      // 367a: sipush 343
      // 367d: iastore
      // 367e: aload 0
      // 367f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3682: bipush 43
      // 3684: aaload
      // 3685: sipush 137
      // 3688: sipush 342
      // 368b: iastore
      // 368c: aload 0
      // 368d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3690: bipush 35
      // 3692: aaload
      // 3693: sipush 142
      // 3696: sipush 341
      // 3699: iastore
      // 369a: aload 0
      // 369b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 369e: bipush 35
      // 36a0: aaload
      // 36a1: bipush 117
      // 36a3: sipush 340
      // 36a6: iastore
      // 36a7: aload 0
      // 36a8: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 36ab: bipush 39
      // 36ad: aaload
      // 36ae: sipush 138
      // 36b1: sipush 339
      // 36b4: iastore
      // 36b5: aload 0
      // 36b6: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 36b9: bipush 16
      // 36bb: aaload
      // 36bc: bipush 59
      // 36be: sipush 338
      // 36c1: iastore
      // 36c2: aload 0
      // 36c3: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 36c6: bipush 39
      // 36c8: aaload
      // 36c9: sipush 174
      // 36cc: sipush 337
      // 36cf: iastore
      // 36d0: aload 0
      // 36d1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 36d4: bipush 55
      // 36d6: aaload
      // 36d7: sipush 145
      // 36da: sipush 336
      // 36dd: iastore
      // 36de: aload 0
      // 36df: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 36e2: bipush 37
      // 36e4: aaload
      // 36e5: bipush 21
      // 36e7: sipush 335
      // 36ea: iastore
      // 36eb: aload 0
      // 36ec: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 36ef: bipush 36
      // 36f1: aaload
      // 36f2: sipush 180
      // 36f5: sipush 334
      // 36f8: iastore
      // 36f9: aload 0
      // 36fa: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 36fd: bipush 37
      // 36ff: aaload
      // 3700: sipush 156
      // 3703: sipush 333
      // 3706: iastore
      // 3707: aload 0
      // 3708: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 370b: bipush 49
      // 370d: aaload
      // 370e: bipush 13
      // 3710: sipush 332
      // 3713: iastore
      // 3714: aload 0
      // 3715: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3718: bipush 41
      // 371a: aaload
      // 371b: bipush 107
      // 371d: sipush 331
      // 3720: iastore
      // 3721: aload 0
      // 3722: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3725: bipush 36
      // 3727: aaload
      // 3728: bipush 56
      // 372a: sipush 330
      // 372d: iastore
      // 372e: aload 0
      // 372f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3732: bipush 53
      // 3734: aaload
      // 3735: bipush 8
      // 3737: sipush 329
      // 373a: iastore
      // 373b: aload 0
      // 373c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 373f: bipush 22
      // 3741: aaload
      // 3742: bipush 114
      // 3744: sipush 328
      // 3747: iastore
      // 3748: aload 0
      // 3749: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 374c: bipush 5
      // 374d: aaload
      // 374e: bipush 95
      // 3750: sipush 327
      // 3753: iastore
      // 3754: aload 0
      // 3755: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3758: bipush 37
      // 375a: aaload
      // 375b: bipush 0
      // 375c: sipush 326
      // 375f: iastore
      // 3760: aload 0
      // 3761: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3764: bipush 26
      // 3766: aaload
      // 3767: sipush 183
      // 376a: sipush 325
      // 376d: iastore
      // 376e: aload 0
      // 376f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3772: bipush 22
      // 3774: aaload
      // 3775: bipush 66
      // 3777: sipush 324
      // 377a: iastore
      // 377b: aload 0
      // 377c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 377f: bipush 35
      // 3781: aaload
      // 3782: bipush 58
      // 3784: sipush 323
      // 3787: iastore
      // 3788: aload 0
      // 3789: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 378c: bipush 48
      // 378e: aaload
      // 378f: bipush 117
      // 3791: sipush 322
      // 3794: iastore
      // 3795: aload 0
      // 3796: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3799: bipush 36
      // 379b: aaload
      // 379c: bipush 102
      // 379e: sipush 321
      // 37a1: iastore
      // 37a2: aload 0
      // 37a3: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 37a6: bipush 22
      // 37a8: aaload
      // 37a9: bipush 122
      // 37ab: sipush 320
      // 37ae: iastore
      // 37af: aload 0
      // 37b0: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 37b3: bipush 35
      // 37b5: aaload
      // 37b6: bipush 11
      // 37b8: sipush 319
      // 37bb: iastore
      // 37bc: aload 0
      // 37bd: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 37c0: bipush 46
      // 37c2: aaload
      // 37c3: bipush 19
      // 37c5: sipush 318
      // 37c8: iastore
      // 37c9: aload 0
      // 37ca: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 37cd: bipush 22
      // 37cf: aaload
      // 37d0: bipush 49
      // 37d2: sipush 317
      // 37d5: iastore
      // 37d6: aload 0
      // 37d7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 37da: bipush 48
      // 37dc: aaload
      // 37dd: sipush 166
      // 37e0: sipush 316
      // 37e3: iastore
      // 37e4: aload 0
      // 37e5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 37e8: bipush 41
      // 37ea: aaload
      // 37eb: bipush 125
      // 37ed: sipush 315
      // 37f0: iastore
      // 37f1: aload 0
      // 37f2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 37f5: bipush 41
      // 37f7: aaload
      // 37f8: bipush 1
      // 37f9: sipush 314
      // 37fc: iastore
      // 37fd: aload 0
      // 37fe: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3801: bipush 35
      // 3803: aaload
      // 3804: sipush 178
      // 3807: sipush 313
      // 380a: iastore
      // 380b: aload 0
      // 380c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 380f: bipush 41
      // 3811: aaload
      // 3812: bipush 12
      // 3814: sipush 312
      // 3817: iastore
      // 3818: aload 0
      // 3819: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 381c: bipush 26
      // 381e: aaload
      // 381f: sipush 167
      // 3822: sipush 311
      // 3825: iastore
      // 3826: aload 0
      // 3827: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 382a: bipush 42
      // 382c: aaload
      // 382d: sipush 152
      // 3830: sipush 310
      // 3833: iastore
      // 3834: aload 0
      // 3835: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3838: bipush 42
      // 383a: aaload
      // 383b: bipush 46
      // 383d: sipush 309
      // 3840: iastore
      // 3841: aload 0
      // 3842: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3845: bipush 42
      // 3847: aaload
      // 3848: sipush 151
      // 384b: sipush 308
      // 384e: iastore
      // 384f: aload 0
      // 3850: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3853: bipush 20
      // 3855: aaload
      // 3856: sipush 135
      // 3859: sipush 307
      // 385c: iastore
      // 385d: aload 0
      // 385e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3861: bipush 37
      // 3863: aaload
      // 3864: sipush 162
      // 3867: sipush 306
      // 386a: iastore
      // 386b: aload 0
      // 386c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 386f: bipush 37
      // 3871: aaload
      // 3872: bipush 50
      // 3874: sipush 305
      // 3877: iastore
      // 3878: aload 0
      // 3879: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 387c: bipush 22
      // 387e: aaload
      // 387f: sipush 185
      // 3882: sipush 304
      // 3885: iastore
      // 3886: aload 0
      // 3887: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 388a: bipush 36
      // 388c: aaload
      // 388d: sipush 166
      // 3890: sipush 303
      // 3893: iastore
      // 3894: aload 0
      // 3895: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3898: bipush 19
      // 389a: aaload
      // 389b: bipush 40
      // 389d: sipush 302
      // 38a0: iastore
      // 38a1: aload 0
      // 38a2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 38a5: bipush 22
      // 38a7: aaload
      // 38a8: bipush 107
      // 38aa: sipush 301
      // 38ad: iastore
      // 38ae: aload 0
      // 38af: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 38b2: bipush 22
      // 38b4: aaload
      // 38b5: bipush 102
      // 38b7: sipush 300
      // 38ba: iastore
      // 38bb: aload 0
      // 38bc: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 38bf: bipush 57
      // 38c1: aaload
      // 38c2: sipush 162
      // 38c5: sipush 299
      // 38c8: iastore
      // 38c9: aload 0
      // 38ca: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 38cd: bipush 22
      // 38cf: aaload
      // 38d0: bipush 124
      // 38d2: sipush 298
      // 38d5: iastore
      // 38d6: aload 0
      // 38d7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 38da: bipush 37
      // 38dc: aaload
      // 38dd: sipush 138
      // 38e0: sipush 297
      // 38e3: iastore
      // 38e4: aload 0
      // 38e5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 38e8: bipush 37
      // 38ea: aaload
      // 38eb: bipush 25
      // 38ed: sipush 296
      // 38f0: iastore
      // 38f1: aload 0
      // 38f2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 38f5: bipush 0
      // 38f6: aaload
      // 38f7: bipush 69
      // 38f9: sipush 295
      // 38fc: iastore
      // 38fd: aload 0
      // 38fe: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3901: bipush 43
      // 3903: aaload
      // 3904: sipush 172
      // 3907: sipush 294
      // 390a: iastore
      // 390b: aload 0
      // 390c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 390f: bipush 42
      // 3911: aaload
      // 3912: sipush 167
      // 3915: sipush 293
      // 3918: iastore
      // 3919: aload 0
      // 391a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 391d: bipush 35
      // 391f: aaload
      // 3920: bipush 120
      // 3922: sipush 292
      // 3925: iastore
      // 3926: aload 0
      // 3927: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 392a: bipush 41
      // 392c: aaload
      // 392d: sipush 128
      // 3930: sipush 291
      // 3933: iastore
      // 3934: aload 0
      // 3935: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3938: bipush 2
      // 3939: aaload
      // 393a: bipush 88
      // 393c: sipush 290
      // 393f: iastore
      // 3940: aload 0
      // 3941: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3944: bipush 20
      // 3946: aaload
      // 3947: bipush 123
      // 3949: sipush 289
      // 394c: iastore
      // 394d: aload 0
      // 394e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3951: bipush 35
      // 3953: aaload
      // 3954: bipush 123
      // 3956: sipush 288
      // 3959: iastore
      // 395a: aload 0
      // 395b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 395e: bipush 36
      // 3960: aaload
      // 3961: bipush 28
      // 3963: sipush 287
      // 3966: iastore
      // 3967: aload 0
      // 3968: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 396b: bipush 42
      // 396d: aaload
      // 396e: sipush 188
      // 3971: sipush 286
      // 3974: iastore
      // 3975: aload 0
      // 3976: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3979: bipush 42
      // 397b: aaload
      // 397c: sipush 164
      // 397f: sipush 285
      // 3982: iastore
      // 3983: aload 0
      // 3984: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3987: bipush 42
      // 3989: aaload
      // 398a: bipush 4
      // 398b: sipush 284
      // 398e: iastore
      // 398f: aload 0
      // 3990: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3993: bipush 43
      // 3995: aaload
      // 3996: bipush 57
      // 3998: sipush 283
      // 399b: iastore
      // 399c: aload 0
      // 399d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 39a0: bipush 39
      // 39a2: aaload
      // 39a3: bipush 3
      // 39a4: sipush 282
      // 39a7: iastore
      // 39a8: aload 0
      // 39a9: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 39ac: bipush 42
      // 39ae: aaload
      // 39af: bipush 3
      // 39b0: sipush 281
      // 39b3: iastore
      // 39b4: aload 0
      // 39b5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 39b8: bipush 57
      // 39ba: aaload
      // 39bb: sipush 158
      // 39be: sipush 280
      // 39c1: iastore
      // 39c2: aload 0
      // 39c3: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 39c6: bipush 35
      // 39c8: aaload
      // 39c9: sipush 146
      // 39cc: sipush 279
      // 39cf: iastore
      // 39d0: aload 0
      // 39d1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 39d4: bipush 24
      // 39d6: aaload
      // 39d7: bipush 54
      // 39d9: sipush 278
      // 39dc: iastore
      // 39dd: aload 0
      // 39de: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 39e1: bipush 13
      // 39e3: aaload
      // 39e4: bipush 110
      // 39e6: sipush 277
      // 39e9: iastore
      // 39ea: aload 0
      // 39eb: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 39ee: bipush 23
      // 39f0: aaload
      // 39f1: sipush 132
      // 39f4: sipush 276
      // 39f7: iastore
      // 39f8: aload 0
      // 39f9: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 39fc: bipush 26
      // 39fe: aaload
      // 39ff: bipush 102
      // 3a01: sipush 275
      // 3a04: iastore
      // 3a05: aload 0
      // 3a06: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3a09: bipush 55
      // 3a0b: aaload
      // 3a0c: sipush 178
      // 3a0f: sipush 274
      // 3a12: iastore
      // 3a13: aload 0
      // 3a14: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3a17: bipush 17
      // 3a19: aaload
      // 3a1a: bipush 117
      // 3a1c: sipush 273
      // 3a1f: iastore
      // 3a20: aload 0
      // 3a21: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3a24: bipush 41
      // 3a26: aaload
      // 3a27: sipush 161
      // 3a2a: sipush 272
      // 3a2d: iastore
      // 3a2e: aload 0
      // 3a2f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3a32: bipush 38
      // 3a34: aaload
      // 3a35: sipush 150
      // 3a38: sipush 271
      // 3a3b: iastore
      // 3a3c: aload 0
      // 3a3d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3a40: bipush 10
      // 3a42: aaload
      // 3a43: bipush 71
      // 3a45: sipush 270
      // 3a48: iastore
      // 3a49: aload 0
      // 3a4a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3a4d: bipush 47
      // 3a4f: aaload
      // 3a50: bipush 60
      // 3a52: sipush 269
      // 3a55: iastore
      // 3a56: aload 0
      // 3a57: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3a5a: bipush 16
      // 3a5c: aaload
      // 3a5d: bipush 114
      // 3a5f: sipush 268
      // 3a62: iastore
      // 3a63: aload 0
      // 3a64: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3a67: bipush 21
      // 3a69: aaload
      // 3a6a: bipush 47
      // 3a6c: sipush 267
      // 3a6f: iastore
      // 3a70: aload 0
      // 3a71: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3a74: bipush 39
      // 3a76: aaload
      // 3a77: bipush 101
      // 3a79: sipush 266
      // 3a7c: iastore
      // 3a7d: aload 0
      // 3a7e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3a81: bipush 18
      // 3a83: aaload
      // 3a84: bipush 45
      // 3a86: sipush 265
      // 3a89: iastore
      // 3a8a: aload 0
      // 3a8b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3a8e: bipush 40
      // 3a90: aaload
      // 3a91: bipush 121
      // 3a93: sipush 264
      // 3a96: iastore
      // 3a97: aload 0
      // 3a98: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3a9b: bipush 45
      // 3a9d: aaload
      // 3a9e: bipush 41
      // 3aa0: sipush 263
      // 3aa3: iastore
      // 3aa4: aload 0
      // 3aa5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3aa8: bipush 22
      // 3aaa: aaload
      // 3aab: sipush 167
      // 3aae: sipush 262
      // 3ab1: iastore
      // 3ab2: aload 0
      // 3ab3: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3ab6: bipush 26
      // 3ab8: aaload
      // 3ab9: sipush 149
      // 3abc: sipush 261
      // 3abf: iastore
      // 3ac0: aload 0
      // 3ac1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3ac4: bipush 15
      // 3ac6: aaload
      // 3ac7: sipush 189
      // 3aca: sipush 260
      // 3acd: iastore
      // 3ace: aload 0
      // 3acf: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3ad2: bipush 41
      // 3ad4: aaload
      // 3ad5: sipush 177
      // 3ad8: sipush 259
      // 3adb: iastore
      // 3adc: aload 0
      // 3add: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3ae0: bipush 46
      // 3ae2: aaload
      // 3ae3: bipush 36
      // 3ae5: sipush 258
      // 3ae8: iastore
      // 3ae9: aload 0
      // 3aea: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3aed: bipush 20
      // 3aef: aaload
      // 3af0: bipush 40
      // 3af2: sipush 257
      // 3af5: iastore
      // 3af6: aload 0
      // 3af7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3afa: bipush 41
      // 3afc: aaload
      // 3afd: bipush 54
      // 3aff: sipush 256
      // 3b02: iastore
      // 3b03: aload 0
      // 3b04: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3b07: bipush 3
      // 3b08: aaload
      // 3b09: bipush 87
      // 3b0b: sipush 255
      // 3b0e: iastore
      // 3b0f: aload 0
      // 3b10: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3b13: bipush 40
      // 3b15: aaload
      // 3b16: bipush 16
      // 3b18: sipush 254
      // 3b1b: iastore
      // 3b1c: aload 0
      // 3b1d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3b20: bipush 42
      // 3b22: aaload
      // 3b23: bipush 15
      // 3b25: sipush 253
      // 3b28: iastore
      // 3b29: aload 0
      // 3b2a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3b2d: bipush 11
      // 3b2f: aaload
      // 3b30: bipush 83
      // 3b32: sipush 252
      // 3b35: iastore
      // 3b36: aload 0
      // 3b37: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3b3a: bipush 0
      // 3b3b: aaload
      // 3b3c: bipush 94
      // 3b3e: sipush 251
      // 3b41: iastore
      // 3b42: aload 0
      // 3b43: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3b46: bipush 122
      // 3b48: aaload
      // 3b49: bipush 81
      // 3b4b: sipush 250
      // 3b4e: iastore
      // 3b4f: aload 0
      // 3b50: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3b53: bipush 41
      // 3b55: aaload
      // 3b56: bipush 26
      // 3b58: sipush 249
      // 3b5b: iastore
      // 3b5c: aload 0
      // 3b5d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3b60: bipush 36
      // 3b62: aaload
      // 3b63: bipush 34
      // 3b65: sipush 248
      // 3b68: iastore
      // 3b69: aload 0
      // 3b6a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3b6d: bipush 44
      // 3b6f: aaload
      // 3b70: sipush 148
      // 3b73: sipush 247
      // 3b76: iastore
      // 3b77: aload 0
      // 3b78: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3b7b: bipush 35
      // 3b7d: aaload
      // 3b7e: bipush 3
      // 3b7f: sipush 246
      // 3b82: iastore
      // 3b83: aload 0
      // 3b84: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3b87: bipush 36
      // 3b89: aaload
      // 3b8a: bipush 114
      // 3b8c: sipush 245
      // 3b8f: iastore
      // 3b90: aload 0
      // 3b91: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3b94: bipush 42
      // 3b96: aaload
      // 3b97: bipush 112
      // 3b99: sipush 244
      // 3b9c: iastore
      // 3b9d: aload 0
      // 3b9e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3ba1: bipush 35
      // 3ba3: aaload
      // 3ba4: sipush 183
      // 3ba7: sipush 243
      // 3baa: iastore
      // 3bab: aload 0
      // 3bac: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3baf: bipush 49
      // 3bb1: aaload
      // 3bb2: bipush 73
      // 3bb4: sipush 242
      // 3bb7: iastore
      // 3bb8: aload 0
      // 3bb9: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3bbc: bipush 39
      // 3bbe: aaload
      // 3bbf: bipush 2
      // 3bc0: sipush 241
      // 3bc3: iastore
      // 3bc4: aload 0
      // 3bc5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3bc8: bipush 38
      // 3bca: aaload
      // 3bcb: bipush 121
      // 3bcd: sipush 240
      // 3bd0: iastore
      // 3bd1: aload 0
      // 3bd2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3bd5: bipush 44
      // 3bd7: aaload
      // 3bd8: bipush 114
      // 3bda: sipush 239
      // 3bdd: iastore
      // 3bde: aload 0
      // 3bdf: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3be2: bipush 49
      // 3be4: aaload
      // 3be5: bipush 32
      // 3be7: sipush 238
      // 3bea: iastore
      // 3beb: aload 0
      // 3bec: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3bef: bipush 1
      // 3bf0: aaload
      // 3bf1: bipush 65
      // 3bf3: sipush 237
      // 3bf6: iastore
      // 3bf7: aload 0
      // 3bf8: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3bfb: bipush 38
      // 3bfd: aaload
      // 3bfe: bipush 25
      // 3c00: sipush 236
      // 3c03: iastore
      // 3c04: aload 0
      // 3c05: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3c08: bipush 39
      // 3c0a: aaload
      // 3c0b: bipush 4
      // 3c0c: sipush 235
      // 3c0f: iastore
      // 3c10: aload 0
      // 3c11: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3c14: bipush 42
      // 3c16: aaload
      // 3c17: bipush 62
      // 3c19: sipush 234
      // 3c1c: iastore
      // 3c1d: aload 0
      // 3c1e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3c21: bipush 35
      // 3c23: aaload
      // 3c24: bipush 40
      // 3c26: sipush 233
      // 3c29: iastore
      // 3c2a: aload 0
      // 3c2b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3c2e: bipush 24
      // 3c30: aaload
      // 3c31: bipush 2
      // 3c32: sipush 232
      // 3c35: iastore
      // 3c36: aload 0
      // 3c37: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3c3a: bipush 53
      // 3c3c: aaload
      // 3c3d: bipush 49
      // 3c3f: sipush 231
      // 3c42: iastore
      // 3c43: aload 0
      // 3c44: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3c47: bipush 41
      // 3c49: aaload
      // 3c4a: sipush 133
      // 3c4d: sipush 230
      // 3c50: iastore
      // 3c51: aload 0
      // 3c52: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3c55: bipush 43
      // 3c57: aaload
      // 3c58: sipush 134
      // 3c5b: sipush 229
      // 3c5e: iastore
      // 3c5f: aload 0
      // 3c60: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3c63: bipush 3
      // 3c64: aaload
      // 3c65: bipush 83
      // 3c67: sipush 228
      // 3c6a: iastore
      // 3c6b: aload 0
      // 3c6c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3c6f: bipush 38
      // 3c71: aaload
      // 3c72: sipush 158
      // 3c75: sipush 227
      // 3c78: iastore
      // 3c79: aload 0
      // 3c7a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3c7d: bipush 24
      // 3c7f: aaload
      // 3c80: bipush 17
      // 3c82: sipush 226
      // 3c85: iastore
      // 3c86: aload 0
      // 3c87: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3c8a: bipush 52
      // 3c8c: aaload
      // 3c8d: bipush 59
      // 3c8f: sipush 225
      // 3c92: iastore
      // 3c93: aload 0
      // 3c94: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3c97: bipush 38
      // 3c99: aaload
      // 3c9a: bipush 41
      // 3c9c: sipush 224
      // 3c9f: iastore
      // 3ca0: aload 0
      // 3ca1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3ca4: bipush 37
      // 3ca6: aaload
      // 3ca7: bipush 127
      // 3ca9: sipush 223
      // 3cac: iastore
      // 3cad: aload 0
      // 3cae: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3cb1: bipush 22
      // 3cb3: aaload
      // 3cb4: sipush 175
      // 3cb7: sipush 222
      // 3cba: iastore
      // 3cbb: aload 0
      // 3cbc: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3cbf: bipush 44
      // 3cc1: aaload
      // 3cc2: bipush 30
      // 3cc4: sipush 221
      // 3cc7: iastore
      // 3cc8: aload 0
      // 3cc9: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3ccc: bipush 47
      // 3cce: aaload
      // 3ccf: sipush 178
      // 3cd2: sipush 220
      // 3cd5: iastore
      // 3cd6: aload 0
      // 3cd7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3cda: bipush 43
      // 3cdc: aaload
      // 3cdd: bipush 99
      // 3cdf: sipush 219
      // 3ce2: iastore
      // 3ce3: aload 0
      // 3ce4: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3ce7: bipush 19
      // 3ce9: aaload
      // 3cea: bipush 4
      // 3ceb: sipush 218
      // 3cee: iastore
      // 3cef: aload 0
      // 3cf0: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3cf3: bipush 37
      // 3cf5: aaload
      // 3cf6: bipush 97
      // 3cf8: sipush 217
      // 3cfb: iastore
      // 3cfc: aload 0
      // 3cfd: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3d00: bipush 38
      // 3d02: aaload
      // 3d03: sipush 181
      // 3d06: sipush 216
      // 3d09: iastore
      // 3d0a: aload 0
      // 3d0b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3d0e: bipush 45
      // 3d10: aaload
      // 3d11: bipush 103
      // 3d13: sipush 215
      // 3d16: iastore
      // 3d17: aload 0
      // 3d18: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3d1b: bipush 1
      // 3d1c: aaload
      // 3d1d: bipush 86
      // 3d1f: sipush 214
      // 3d22: iastore
      // 3d23: aload 0
      // 3d24: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3d27: bipush 40
      // 3d29: aaload
      // 3d2a: bipush 15
      // 3d2c: sipush 213
      // 3d2f: iastore
      // 3d30: aload 0
      // 3d31: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3d34: bipush 22
      // 3d36: aaload
      // 3d37: sipush 136
      // 3d3a: sipush 212
      // 3d3d: iastore
      // 3d3e: aload 0
      // 3d3f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3d42: bipush 75
      // 3d44: aaload
      // 3d45: sipush 165
      // 3d48: sipush 211
      // 3d4b: iastore
      // 3d4c: aload 0
      // 3d4d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3d50: bipush 36
      // 3d52: aaload
      // 3d53: bipush 15
      // 3d55: sipush 210
      // 3d58: iastore
      // 3d59: aload 0
      // 3d5a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3d5d: bipush 46
      // 3d5f: aaload
      // 3d60: bipush 80
      // 3d62: sipush 209
      // 3d65: iastore
      // 3d66: aload 0
      // 3d67: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3d6a: bipush 59
      // 3d6c: aaload
      // 3d6d: bipush 55
      // 3d6f: sipush 208
      // 3d72: iastore
      // 3d73: aload 0
      // 3d74: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3d77: bipush 37
      // 3d79: aaload
      // 3d7a: bipush 108
      // 3d7c: sipush 207
      // 3d7f: iastore
      // 3d80: aload 0
      // 3d81: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3d84: bipush 21
      // 3d86: aaload
      // 3d87: bipush 109
      // 3d89: sipush 206
      // 3d8c: iastore
      // 3d8d: aload 0
      // 3d8e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3d91: bipush 24
      // 3d93: aaload
      // 3d94: sipush 165
      // 3d97: sipush 205
      // 3d9a: iastore
      // 3d9b: aload 0
      // 3d9c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3d9f: bipush 79
      // 3da1: aaload
      // 3da2: sipush 158
      // 3da5: sipush 204
      // 3da8: iastore
      // 3da9: aload 0
      // 3daa: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3dad: bipush 44
      // 3daf: aaload
      // 3db0: sipush 139
      // 3db3: sipush 203
      // 3db6: iastore
      // 3db7: aload 0
      // 3db8: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3dbb: bipush 36
      // 3dbd: aaload
      // 3dbe: bipush 124
      // 3dc0: sipush 202
      // 3dc3: iastore
      // 3dc4: aload 0
      // 3dc5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3dc8: bipush 42
      // 3dca: aaload
      // 3dcb: sipush 185
      // 3dce: sipush 201
      // 3dd1: iastore
      // 3dd2: aload 0
      // 3dd3: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3dd6: bipush 39
      // 3dd8: aaload
      // 3dd9: sipush 186
      // 3ddc: sipush 200
      // 3ddf: iastore
      // 3de0: aload 0
      // 3de1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3de4: bipush 22
      // 3de6: aaload
      // 3de7: sipush 128
      // 3dea: sipush 199
      // 3ded: iastore
      // 3dee: aload 0
      // 3def: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3df2: bipush 40
      // 3df4: aaload
      // 3df5: bipush 44
      // 3df7: sipush 198
      // 3dfa: iastore
      // 3dfb: aload 0
      // 3dfc: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3dff: bipush 41
      // 3e01: aaload
      // 3e02: bipush 105
      // 3e04: sipush 197
      // 3e07: iastore
      // 3e08: aload 0
      // 3e09: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3e0c: bipush 1
      // 3e0d: aaload
      // 3e0e: bipush 70
      // 3e10: sipush 196
      // 3e13: iastore
      // 3e14: aload 0
      // 3e15: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3e18: bipush 1
      // 3e19: aaload
      // 3e1a: bipush 68
      // 3e1c: sipush 195
      // 3e1f: iastore
      // 3e20: aload 0
      // 3e21: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3e24: bipush 53
      // 3e26: aaload
      // 3e27: bipush 22
      // 3e29: sipush 194
      // 3e2c: iastore
      // 3e2d: aload 0
      // 3e2e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3e31: bipush 36
      // 3e33: aaload
      // 3e34: bipush 54
      // 3e36: sipush 193
      // 3e39: iastore
      // 3e3a: aload 0
      // 3e3b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3e3e: bipush 47
      // 3e40: aaload
      // 3e41: sipush 147
      // 3e44: sipush 192
      // 3e47: iastore
      // 3e48: aload 0
      // 3e49: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3e4c: bipush 35
      // 3e4e: aaload
      // 3e4f: bipush 36
      // 3e51: sipush 191
      // 3e54: iastore
      // 3e55: aload 0
      // 3e56: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3e59: bipush 35
      // 3e5b: aaload
      // 3e5c: sipush 185
      // 3e5f: sipush 190
      // 3e62: iastore
      // 3e63: aload 0
      // 3e64: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3e67: bipush 45
      // 3e69: aaload
      // 3e6a: bipush 37
      // 3e6c: sipush 189
      // 3e6f: iastore
      // 3e70: aload 0
      // 3e71: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3e74: bipush 43
      // 3e76: aaload
      // 3e77: sipush 163
      // 3e7a: sipush 188
      // 3e7d: iastore
      // 3e7e: aload 0
      // 3e7f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3e82: bipush 56
      // 3e84: aaload
      // 3e85: bipush 115
      // 3e87: sipush 187
      // 3e8a: iastore
      // 3e8b: aload 0
      // 3e8c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3e8f: bipush 38
      // 3e91: aaload
      // 3e92: sipush 164
      // 3e95: sipush 186
      // 3e98: iastore
      // 3e99: aload 0
      // 3e9a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3e9d: bipush 35
      // 3e9f: aaload
      // 3ea0: sipush 141
      // 3ea3: sipush 185
      // 3ea6: iastore
      // 3ea7: aload 0
      // 3ea8: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3eab: bipush 42
      // 3ead: aaload
      // 3eae: sipush 132
      // 3eb1: sipush 184
      // 3eb4: iastore
      // 3eb5: aload 0
      // 3eb6: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3eb9: bipush 46
      // 3ebb: aaload
      // 3ebc: bipush 120
      // 3ebe: sipush 183
      // 3ec1: iastore
      // 3ec2: aload 0
      // 3ec3: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3ec6: bipush 69
      // 3ec8: aaload
      // 3ec9: sipush 142
      // 3ecc: sipush 182
      // 3ecf: iastore
      // 3ed0: aload 0
      // 3ed1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3ed4: bipush 38
      // 3ed6: aaload
      // 3ed7: sipush 175
      // 3eda: sipush 181
      // 3edd: iastore
      // 3ede: aload 0
      // 3edf: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3ee2: bipush 22
      // 3ee4: aaload
      // 3ee5: bipush 112
      // 3ee7: sipush 180
      // 3eea: iastore
      // 3eeb: aload 0
      // 3eec: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3eef: bipush 38
      // 3ef1: aaload
      // 3ef2: sipush 142
      // 3ef5: sipush 179
      // 3ef8: iastore
      // 3ef9: aload 0
      // 3efa: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3efd: bipush 40
      // 3eff: aaload
      // 3f00: bipush 37
      // 3f02: sipush 178
      // 3f05: iastore
      // 3f06: aload 0
      // 3f07: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3f0a: bipush 37
      // 3f0c: aaload
      // 3f0d: bipush 109
      // 3f0f: sipush 177
      // 3f12: iastore
      // 3f13: aload 0
      // 3f14: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3f17: bipush 40
      // 3f19: aaload
      // 3f1a: sipush 144
      // 3f1d: sipush 176
      // 3f20: iastore
      // 3f21: aload 0
      // 3f22: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3f25: bipush 44
      // 3f27: aaload
      // 3f28: bipush 117
      // 3f2a: sipush 175
      // 3f2d: iastore
      // 3f2e: aload 0
      // 3f2f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3f32: bipush 35
      // 3f34: aaload
      // 3f35: sipush 181
      // 3f38: sipush 174
      // 3f3b: iastore
      // 3f3c: aload 0
      // 3f3d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3f40: bipush 26
      // 3f42: aaload
      // 3f43: bipush 105
      // 3f45: sipush 173
      // 3f48: iastore
      // 3f49: aload 0
      // 3f4a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3f4d: bipush 16
      // 3f4f: aaload
      // 3f50: bipush 48
      // 3f52: sipush 172
      // 3f55: iastore
      // 3f56: aload 0
      // 3f57: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3f5a: bipush 44
      // 3f5c: aaload
      // 3f5d: bipush 122
      // 3f5f: sipush 171
      // 3f62: iastore
      // 3f63: aload 0
      // 3f64: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3f67: bipush 12
      // 3f69: aaload
      // 3f6a: bipush 86
      // 3f6c: sipush 170
      // 3f6f: iastore
      // 3f70: aload 0
      // 3f71: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3f74: bipush 84
      // 3f76: aaload
      // 3f77: bipush 53
      // 3f79: sipush 169
      // 3f7c: iastore
      // 3f7d: aload 0
      // 3f7e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3f81: bipush 17
      // 3f83: aaload
      // 3f84: bipush 44
      // 3f86: sipush 168
      // 3f89: iastore
      // 3f8a: aload 0
      // 3f8b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3f8e: bipush 59
      // 3f90: aaload
      // 3f91: bipush 54
      // 3f93: sipush 167
      // 3f96: iastore
      // 3f97: aload 0
      // 3f98: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3f9b: bipush 36
      // 3f9d: aaload
      // 3f9e: bipush 98
      // 3fa0: sipush 166
      // 3fa3: iastore
      // 3fa4: aload 0
      // 3fa5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3fa8: bipush 45
      // 3faa: aaload
      // 3fab: bipush 115
      // 3fad: sipush 165
      // 3fb0: iastore
      // 3fb1: aload 0
      // 3fb2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3fb5: bipush 73
      // 3fb7: aaload
      // 3fb8: bipush 9
      // 3fba: sipush 164
      // 3fbd: iastore
      // 3fbe: aload 0
      // 3fbf: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3fc2: bipush 44
      // 3fc4: aaload
      // 3fc5: bipush 123
      // 3fc7: sipush 163
      // 3fca: iastore
      // 3fcb: aload 0
      // 3fcc: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3fcf: bipush 37
      // 3fd1: aaload
      // 3fd2: sipush 188
      // 3fd5: sipush 162
      // 3fd8: iastore
      // 3fd9: aload 0
      // 3fda: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3fdd: bipush 51
      // 3fdf: aaload
      // 3fe0: bipush 117
      // 3fe2: sipush 161
      // 3fe5: iastore
      // 3fe6: aload 0
      // 3fe7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3fea: bipush 15
      // 3fec: aaload
      // 3fed: sipush 156
      // 3ff0: sipush 160
      // 3ff3: iastore
      // 3ff4: aload 0
      // 3ff5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 3ff8: bipush 36
      // 3ffa: aaload
      // 3ffb: sipush 155
      // 3ffe: sipush 159
      // 4001: iastore
      // 4002: aload 0
      // 4003: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4006: bipush 44
      // 4008: aaload
      // 4009: bipush 25
      // 400b: sipush 158
      // 400e: iastore
      // 400f: aload 0
      // 4010: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4013: bipush 38
      // 4015: aaload
      // 4016: bipush 12
      // 4018: sipush 157
      // 401b: iastore
      // 401c: aload 0
      // 401d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4020: bipush 38
      // 4022: aaload
      // 4023: sipush 140
      // 4026: sipush 156
      // 4029: iastore
      // 402a: aload 0
      // 402b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 402e: bipush 23
      // 4030: aaload
      // 4031: bipush 4
      // 4032: sipush 155
      // 4035: iastore
      // 4036: aload 0
      // 4037: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 403a: bipush 45
      // 403c: aaload
      // 403d: sipush 149
      // 4040: sipush 154
      // 4043: iastore
      // 4044: aload 0
      // 4045: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4048: bipush 22
      // 404a: aaload
      // 404b: sipush 189
      // 404e: sipush 153
      // 4051: iastore
      // 4052: aload 0
      // 4053: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4056: bipush 38
      // 4058: aaload
      // 4059: sipush 147
      // 405c: sipush 152
      // 405f: iastore
      // 4060: aload 0
      // 4061: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4064: bipush 27
      // 4066: aaload
      // 4067: bipush 5
      // 4068: sipush 151
      // 406b: iastore
      // 406c: aload 0
      // 406d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4070: bipush 22
      // 4072: aaload
      // 4073: bipush 42
      // 4075: sipush 150
      // 4078: iastore
      // 4079: aload 0
      // 407a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 407d: bipush 3
      // 407e: aaload
      // 407f: bipush 68
      // 4081: sipush 149
      // 4084: iastore
      // 4085: aload 0
      // 4086: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4089: bipush 39
      // 408b: aaload
      // 408c: bipush 51
      // 408e: sipush 148
      // 4091: iastore
      // 4092: aload 0
      // 4093: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4096: bipush 36
      // 4098: aaload
      // 4099: bipush 29
      // 409b: sipush 147
      // 409e: iastore
      // 409f: aload 0
      // 40a0: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 40a3: bipush 20
      // 40a5: aaload
      // 40a6: bipush 108
      // 40a8: sipush 146
      // 40ab: iastore
      // 40ac: aload 0
      // 40ad: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 40b0: bipush 50
      // 40b2: aaload
      // 40b3: bipush 57
      // 40b5: sipush 145
      // 40b8: iastore
      // 40b9: aload 0
      // 40ba: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 40bd: bipush 55
      // 40bf: aaload
      // 40c0: bipush 104
      // 40c2: sipush 144
      // 40c5: iastore
      // 40c6: aload 0
      // 40c7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 40ca: bipush 22
      // 40cc: aaload
      // 40cd: bipush 46
      // 40cf: sipush 143
      // 40d2: iastore
      // 40d3: aload 0
      // 40d4: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 40d7: bipush 18
      // 40d9: aaload
      // 40da: sipush 164
      // 40dd: sipush 142
      // 40e0: iastore
      // 40e1: aload 0
      // 40e2: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 40e5: bipush 50
      // 40e7: aaload
      // 40e8: sipush 159
      // 40eb: sipush 141
      // 40ee: iastore
      // 40ef: aload 0
      // 40f0: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 40f3: bipush 85
      // 40f5: aaload
      // 40f6: sipush 131
      // 40f9: sipush 140
      // 40fc: iastore
      // 40fd: aload 0
      // 40fe: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4101: bipush 26
      // 4103: aaload
      // 4104: bipush 79
      // 4106: sipush 139
      // 4109: iastore
      // 410a: aload 0
      // 410b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 410e: bipush 38
      // 4110: aaload
      // 4111: bipush 100
      // 4113: sipush 138
      // 4116: iastore
      // 4117: aload 0
      // 4118: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 411b: bipush 53
      // 411d: aaload
      // 411e: bipush 112
      // 4120: sipush 137
      // 4123: iastore
      // 4124: aload 0
      // 4125: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4128: bipush 20
      // 412a: aaload
      // 412b: sipush 190
      // 412e: sipush 136
      // 4131: iastore
      // 4132: aload 0
      // 4133: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4136: bipush 14
      // 4138: aaload
      // 4139: bipush 69
      // 413b: sipush 135
      // 413e: iastore
      // 413f: aload 0
      // 4140: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4143: bipush 23
      // 4145: aaload
      // 4146: bipush 11
      // 4148: sipush 134
      // 414b: iastore
      // 414c: aload 0
      // 414d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4150: bipush 40
      // 4152: aaload
      // 4153: bipush 114
      // 4155: sipush 133
      // 4158: iastore
      // 4159: aload 0
      // 415a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 415d: bipush 40
      // 415f: aaload
      // 4160: sipush 148
      // 4163: sipush 132
      // 4166: iastore
      // 4167: aload 0
      // 4168: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 416b: bipush 53
      // 416d: aaload
      // 416e: sipush 130
      // 4171: sipush 131
      // 4174: iastore
      // 4175: aload 0
      // 4176: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4179: bipush 36
      // 417b: aaload
      // 417c: bipush 2
      // 417d: sipush 130
      // 4180: iastore
      // 4181: aload 0
      // 4182: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4185: bipush 66
      // 4187: aaload
      // 4188: bipush 82
      // 418a: sipush 129
      // 418d: iastore
      // 418e: aload 0
      // 418f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4192: bipush 45
      // 4194: aaload
      // 4195: sipush 166
      // 4198: sipush 128
      // 419b: iastore
      // 419c: aload 0
      // 419d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 41a0: bipush 4
      // 41a1: aaload
      // 41a2: bipush 88
      // 41a4: bipush 127
      // 41a6: iastore
      // 41a7: aload 0
      // 41a8: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 41ab: bipush 16
      // 41ad: aaload
      // 41ae: bipush 57
      // 41b0: bipush 126
      // 41b2: iastore
      // 41b3: aload 0
      // 41b4: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 41b7: bipush 22
      // 41b9: aaload
      // 41ba: bipush 116
      // 41bc: bipush 125
      // 41be: iastore
      // 41bf: aload 0
      // 41c0: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 41c3: bipush 36
      // 41c5: aaload
      // 41c6: bipush 108
      // 41c8: bipush 124
      // 41ca: iastore
      // 41cb: aload 0
      // 41cc: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 41cf: bipush 13
      // 41d1: aaload
      // 41d2: bipush 48
      // 41d4: bipush 123
      // 41d6: iastore
      // 41d7: aload 0
      // 41d8: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 41db: bipush 54
      // 41dd: aaload
      // 41de: bipush 12
      // 41e0: bipush 122
      // 41e2: iastore
      // 41e3: aload 0
      // 41e4: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 41e7: bipush 40
      // 41e9: aaload
      // 41ea: sipush 136
      // 41ed: bipush 121
      // 41ef: iastore
      // 41f0: aload 0
      // 41f1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 41f4: bipush 36
      // 41f6: aaload
      // 41f7: sipush 128
      // 41fa: bipush 120
      // 41fc: iastore
      // 41fd: aload 0
      // 41fe: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4201: bipush 23
      // 4203: aaload
      // 4204: bipush 6
      // 4206: bipush 119
      // 4208: iastore
      // 4209: aload 0
      // 420a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 420d: bipush 38
      // 420f: aaload
      // 4210: bipush 125
      // 4212: bipush 118
      // 4214: iastore
      // 4215: aload 0
      // 4216: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4219: bipush 45
      // 421b: aaload
      // 421c: sipush 154
      // 421f: bipush 117
      // 4221: iastore
      // 4222: aload 0
      // 4223: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4226: bipush 51
      // 4228: aaload
      // 4229: bipush 127
      // 422b: bipush 116
      // 422d: iastore
      // 422e: aload 0
      // 422f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4232: bipush 44
      // 4234: aaload
      // 4235: sipush 163
      // 4238: bipush 115
      // 423a: iastore
      // 423b: aload 0
      // 423c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 423f: bipush 16
      // 4241: aaload
      // 4242: sipush 173
      // 4245: bipush 114
      // 4247: iastore
      // 4248: aload 0
      // 4249: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 424c: bipush 43
      // 424e: aaload
      // 424f: bipush 49
      // 4251: bipush 113
      // 4253: iastore
      // 4254: aload 0
      // 4255: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4258: bipush 20
      // 425a: aaload
      // 425b: bipush 112
      // 425d: bipush 112
      // 425f: iastore
      // 4260: aload 0
      // 4261: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4264: bipush 15
      // 4266: aaload
      // 4267: sipush 168
      // 426a: bipush 111
      // 426c: iastore
      // 426d: aload 0
      // 426e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4271: bipush 35
      // 4273: aaload
      // 4274: sipush 129
      // 4277: bipush 110
      // 4279: iastore
      // 427a: aload 0
      // 427b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 427e: bipush 20
      // 4280: aaload
      // 4281: bipush 45
      // 4283: bipush 109
      // 4285: iastore
      // 4286: aload 0
      // 4287: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 428a: bipush 38
      // 428c: aaload
      // 428d: bipush 10
      // 428f: bipush 108
      // 4291: iastore
      // 4292: aload 0
      // 4293: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4296: bipush 57
      // 4298: aaload
      // 4299: sipush 171
      // 429c: bipush 107
      // 429e: iastore
      // 429f: aload 0
      // 42a0: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 42a3: bipush 44
      // 42a5: aaload
      // 42a6: sipush 190
      // 42a9: bipush 106
      // 42ab: iastore
      // 42ac: aload 0
      // 42ad: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 42b0: bipush 40
      // 42b2: aaload
      // 42b3: bipush 56
      // 42b5: bipush 105
      // 42b7: iastore
      // 42b8: aload 0
      // 42b9: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 42bc: bipush 36
      // 42be: aaload
      // 42bf: sipush 156
      // 42c2: bipush 104
      // 42c4: iastore
      // 42c5: aload 0
      // 42c6: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 42c9: bipush 3
      // 42ca: aaload
      // 42cb: bipush 88
      // 42cd: bipush 103
      // 42cf: iastore
      // 42d0: aload 0
      // 42d1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 42d4: bipush 50
      // 42d6: aaload
      // 42d7: bipush 122
      // 42d9: bipush 102
      // 42db: iastore
      // 42dc: aload 0
      // 42dd: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 42e0: bipush 36
      // 42e2: aaload
      // 42e3: bipush 7
      // 42e5: bipush 101
      // 42e7: iastore
      // 42e8: aload 0
      // 42e9: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 42ec: bipush 39
      // 42ee: aaload
      // 42ef: bipush 43
      // 42f1: bipush 100
      // 42f3: iastore
      // 42f4: aload 0
      // 42f5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 42f8: bipush 15
      // 42fa: aaload
      // 42fb: sipush 166
      // 42fe: bipush 99
      // 4300: iastore
      // 4301: aload 0
      // 4302: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4305: bipush 42
      // 4307: aaload
      // 4308: sipush 136
      // 430b: bipush 98
      // 430d: iastore
      // 430e: aload 0
      // 430f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4312: bipush 22
      // 4314: aaload
      // 4315: sipush 131
      // 4318: bipush 97
      // 431a: iastore
      // 431b: aload 0
      // 431c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 431f: bipush 44
      // 4321: aaload
      // 4322: bipush 23
      // 4324: bipush 96
      // 4326: iastore
      // 4327: aload 0
      // 4328: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 432b: bipush 54
      // 432d: aaload
      // 432e: sipush 147
      // 4331: bipush 95
      // 4333: iastore
      // 4334: aload 0
      // 4335: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4338: bipush 41
      // 433a: aaload
      // 433b: bipush 32
      // 433d: bipush 94
      // 433f: iastore
      // 4340: aload 0
      // 4341: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4344: bipush 23
      // 4346: aaload
      // 4347: bipush 121
      // 4349: bipush 93
      // 434b: iastore
      // 434c: aload 0
      // 434d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4350: bipush 39
      // 4352: aaload
      // 4353: bipush 108
      // 4355: bipush 92
      // 4357: iastore
      // 4358: aload 0
      // 4359: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 435c: bipush 2
      // 435d: aaload
      // 435e: bipush 78
      // 4360: bipush 91
      // 4362: iastore
      // 4363: aload 0
      // 4364: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4367: bipush 40
      // 4369: aaload
      // 436a: sipush 155
      // 436d: bipush 90
      // 436f: iastore
      // 4370: aload 0
      // 4371: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4374: bipush 55
      // 4376: aaload
      // 4377: bipush 51
      // 4379: bipush 89
      // 437b: iastore
      // 437c: aload 0
      // 437d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4380: bipush 19
      // 4382: aaload
      // 4383: bipush 34
      // 4385: bipush 88
      // 4387: iastore
      // 4388: aload 0
      // 4389: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 438c: bipush 48
      // 438e: aaload
      // 438f: sipush 128
      // 4392: bipush 87
      // 4394: iastore
      // 4395: aload 0
      // 4396: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4399: bipush 48
      // 439b: aaload
      // 439c: sipush 159
      // 439f: bipush 86
      // 43a1: iastore
      // 43a2: aload 0
      // 43a3: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 43a6: bipush 20
      // 43a8: aaload
      // 43a9: bipush 70
      // 43ab: bipush 85
      // 43ad: iastore
      // 43ae: aload 0
      // 43af: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 43b2: bipush 34
      // 43b4: aaload
      // 43b5: bipush 71
      // 43b7: bipush 84
      // 43b9: iastore
      // 43ba: aload 0
      // 43bb: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 43be: bipush 16
      // 43c0: aaload
      // 43c1: bipush 31
      // 43c3: bipush 83
      // 43c5: iastore
      // 43c6: aload 0
      // 43c7: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 43ca: bipush 42
      // 43cc: aaload
      // 43cd: sipush 157
      // 43d0: bipush 82
      // 43d2: iastore
      // 43d3: aload 0
      // 43d4: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 43d7: bipush 20
      // 43d9: aaload
      // 43da: bipush 44
      // 43dc: bipush 81
      // 43de: iastore
      // 43df: aload 0
      // 43e0: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 43e3: bipush 11
      // 43e5: aaload
      // 43e6: bipush 92
      // 43e8: bipush 80
      // 43ea: iastore
      // 43eb: aload 0
      // 43ec: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 43ef: bipush 44
      // 43f1: aaload
      // 43f2: sipush 180
      // 43f5: bipush 79
      // 43f7: iastore
      // 43f8: aload 0
      // 43f9: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 43fc: bipush 84
      // 43fe: aaload
      // 43ff: bipush 33
      // 4401: bipush 78
      // 4403: iastore
      // 4404: aload 0
      // 4405: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4408: bipush 16
      // 440a: aaload
      // 440b: bipush 116
      // 440d: bipush 77
      // 440f: iastore
      // 4410: aload 0
      // 4411: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4414: bipush 61
      // 4416: aaload
      // 4417: sipush 163
      // 441a: bipush 76
      // 441c: iastore
      // 441d: aload 0
      // 441e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4421: bipush 35
      // 4423: aaload
      // 4424: sipush 164
      // 4427: bipush 75
      // 4429: iastore
      // 442a: aload 0
      // 442b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 442e: bipush 36
      // 4430: aaload
      // 4431: bipush 42
      // 4433: bipush 74
      // 4435: iastore
      // 4436: aload 0
      // 4437: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 443a: bipush 13
      // 443c: aaload
      // 443d: bipush 40
      // 443f: bipush 73
      // 4441: iastore
      // 4442: aload 0
      // 4443: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4446: bipush 43
      // 4448: aaload
      // 4449: sipush 176
      // 444c: bipush 72
      // 444e: iastore
      // 444f: aload 0
      // 4450: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4453: bipush 2
      // 4454: aaload
      // 4455: bipush 66
      // 4457: bipush 71
      // 4459: iastore
      // 445a: aload 0
      // 445b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 445e: bipush 20
      // 4460: aaload
      // 4461: sipush 133
      // 4464: bipush 70
      // 4466: iastore
      // 4467: aload 0
      // 4468: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 446b: bipush 36
      // 446d: aaload
      // 446e: bipush 65
      // 4470: bipush 69
      // 4472: iastore
      // 4473: aload 0
      // 4474: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4477: bipush 38
      // 4479: aaload
      // 447a: bipush 33
      // 447c: bipush 68
      // 447e: iastore
      // 447f: aload 0
      // 4480: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4483: bipush 12
      // 4485: aaload
      // 4486: bipush 91
      // 4488: bipush 67
      // 448a: iastore
      // 448b: aload 0
      // 448c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 448f: bipush 36
      // 4491: aaload
      // 4492: bipush 26
      // 4494: bipush 66
      // 4496: iastore
      // 4497: aload 0
      // 4498: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 449b: bipush 15
      // 449d: aaload
      // 449e: sipush 174
      // 44a1: bipush 65
      // 44a3: iastore
      // 44a4: aload 0
      // 44a5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 44a8: bipush 77
      // 44aa: aaload
      // 44ab: bipush 32
      // 44ad: bipush 64
      // 44af: iastore
      // 44b0: aload 0
      // 44b1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 44b4: bipush 16
      // 44b6: aaload
      // 44b7: bipush 1
      // 44b8: bipush 63
      // 44ba: iastore
      // 44bb: aload 0
      // 44bc: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 44bf: bipush 25
      // 44c1: aaload
      // 44c2: bipush 86
      // 44c4: bipush 62
      // 44c6: iastore
      // 44c7: aload 0
      // 44c8: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 44cb: bipush 17
      // 44cd: aaload
      // 44ce: bipush 13
      // 44d0: bipush 61
      // 44d2: iastore
      // 44d3: aload 0
      // 44d4: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 44d7: bipush 5
      // 44d8: aaload
      // 44d9: bipush 75
      // 44db: bipush 60
      // 44dd: iastore
      // 44de: aload 0
      // 44df: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 44e2: bipush 36
      // 44e4: aaload
      // 44e5: bipush 52
      // 44e7: bipush 59
      // 44e9: iastore
      // 44ea: aload 0
      // 44eb: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 44ee: bipush 51
      // 44f0: aaload
      // 44f1: sipush 164
      // 44f4: bipush 58
      // 44f6: iastore
      // 44f7: aload 0
      // 44f8: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 44fb: bipush 12
      // 44fd: aaload
      // 44fe: bipush 85
      // 4500: bipush 57
      // 4502: iastore
      // 4503: aload 0
      // 4504: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4507: bipush 39
      // 4509: aaload
      // 450a: sipush 168
      // 450d: bipush 56
      // 450f: iastore
      // 4510: aload 0
      // 4511: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4514: bipush 43
      // 4516: aaload
      // 4517: bipush 16
      // 4519: bipush 55
      // 451b: iastore
      // 451c: aload 0
      // 451d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4520: bipush 40
      // 4522: aaload
      // 4523: bipush 69
      // 4525: bipush 54
      // 4527: iastore
      // 4528: aload 0
      // 4529: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 452c: bipush 26
      // 452e: aaload
      // 452f: bipush 108
      // 4531: bipush 53
      // 4533: iastore
      // 4534: aload 0
      // 4535: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4538: bipush 51
      // 453a: aaload
      // 453b: bipush 56
      // 453d: bipush 52
      // 453f: iastore
      // 4540: aload 0
      // 4541: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4544: bipush 16
      // 4546: aaload
      // 4547: bipush 37
      // 4549: bipush 51
      // 454b: iastore
      // 454c: aload 0
      // 454d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4550: bipush 40
      // 4552: aaload
      // 4553: bipush 29
      // 4555: bipush 50
      // 4557: iastore
      // 4558: aload 0
      // 4559: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 455c: bipush 46
      // 455e: aaload
      // 455f: sipush 171
      // 4562: bipush 49
      // 4564: iastore
      // 4565: aload 0
      // 4566: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4569: bipush 40
      // 456b: aaload
      // 456c: sipush 128
      // 456f: bipush 48
      // 4571: iastore
      // 4572: aload 0
      // 4573: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4576: bipush 72
      // 4578: aaload
      // 4579: bipush 114
      // 457b: bipush 47
      // 457d: iastore
      // 457e: aload 0
      // 457f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4582: bipush 21
      // 4584: aaload
      // 4585: bipush 103
      // 4587: bipush 46
      // 4589: iastore
      // 458a: aload 0
      // 458b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 458e: bipush 22
      // 4590: aaload
      // 4591: bipush 44
      // 4593: bipush 45
      // 4595: iastore
      // 4596: aload 0
      // 4597: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 459a: bipush 40
      // 459c: aaload
      // 459d: bipush 115
      // 459f: bipush 44
      // 45a1: iastore
      // 45a2: aload 0
      // 45a3: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 45a6: bipush 43
      // 45a8: aaload
      // 45a9: bipush 7
      // 45ab: bipush 43
      // 45ad: iastore
      // 45ae: aload 0
      // 45af: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 45b2: bipush 43
      // 45b4: aaload
      // 45b5: sipush 153
      // 45b8: bipush 42
      // 45ba: iastore
      // 45bb: aload 0
      // 45bc: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 45bf: bipush 17
      // 45c1: aaload
      // 45c2: bipush 20
      // 45c4: bipush 41
      // 45c6: iastore
      // 45c7: aload 0
      // 45c8: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 45cb: bipush 16
      // 45cd: aaload
      // 45ce: bipush 49
      // 45d0: bipush 40
      // 45d2: iastore
      // 45d3: aload 0
      // 45d4: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 45d7: bipush 36
      // 45d9: aaload
      // 45da: bipush 57
      // 45dc: bipush 39
      // 45de: iastore
      // 45df: aload 0
      // 45e0: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 45e3: bipush 18
      // 45e5: aaload
      // 45e6: bipush 38
      // 45e8: bipush 38
      // 45ea: iastore
      // 45eb: aload 0
      // 45ec: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 45ef: bipush 45
      // 45f1: aaload
      // 45f2: sipush 184
      // 45f5: bipush 37
      // 45f7: iastore
      // 45f8: aload 0
      // 45f9: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 45fc: bipush 37
      // 45fe: aaload
      // 45ff: sipush 167
      // 4602: bipush 36
      // 4604: iastore
      // 4605: aload 0
      // 4606: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4609: bipush 26
      // 460b: aaload
      // 460c: bipush 106
      // 460e: bipush 35
      // 4610: iastore
      // 4611: aload 0
      // 4612: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4615: bipush 61
      // 4617: aaload
      // 4618: bipush 121
      // 461a: bipush 34
      // 461c: iastore
      // 461d: aload 0
      // 461e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4621: bipush 89
      // 4623: aaload
      // 4624: sipush 140
      // 4627: bipush 33
      // 4629: iastore
      // 462a: aload 0
      // 462b: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 462e: bipush 46
      // 4630: aaload
      // 4631: bipush 61
      // 4633: bipush 32
      // 4635: iastore
      // 4636: aload 0
      // 4637: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 463a: bipush 39
      // 463c: aaload
      // 463d: sipush 163
      // 4640: bipush 31
      // 4642: iastore
      // 4643: aload 0
      // 4644: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4647: bipush 40
      // 4649: aaload
      // 464a: bipush 62
      // 464c: bipush 30
      // 464e: iastore
      // 464f: aload 0
      // 4650: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4653: bipush 38
      // 4655: aaload
      // 4656: sipush 165
      // 4659: bipush 29
      // 465b: iastore
      // 465c: aload 0
      // 465d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4660: bipush 47
      // 4662: aaload
      // 4663: bipush 37
      // 4665: bipush 28
      // 4667: iastore
      // 4668: aload 0
      // 4669: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 466c: bipush 18
      // 466e: aaload
      // 466f: sipush 155
      // 4672: bipush 27
      // 4674: iastore
      // 4675: aload 0
      // 4676: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4679: bipush 20
      // 467b: aaload
      // 467c: bipush 33
      // 467e: bipush 26
      // 4680: iastore
      // 4681: aload 0
      // 4682: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4685: bipush 29
      // 4687: aaload
      // 4688: bipush 90
      // 468a: bipush 25
      // 468c: iastore
      // 468d: aload 0
      // 468e: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4691: bipush 20
      // 4693: aaload
      // 4694: bipush 103
      // 4696: bipush 24
      // 4698: iastore
      // 4699: aload 0
      // 469a: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 469d: bipush 37
      // 469f: aaload
      // 46a0: bipush 51
      // 46a2: bipush 23
      // 46a4: iastore
      // 46a5: aload 0
      // 46a6: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 46a9: bipush 57
      // 46ab: aaload
      // 46ac: bipush 0
      // 46ad: bipush 22
      // 46af: iastore
      // 46b0: aload 0
      // 46b1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 46b4: bipush 40
      // 46b6: aaload
      // 46b7: bipush 31
      // 46b9: bipush 21
      // 46bb: iastore
      // 46bc: aload 0
      // 46bd: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 46c0: bipush 45
      // 46c2: aaload
      // 46c3: bipush 32
      // 46c5: bipush 20
      // 46c7: iastore
      // 46c8: aload 0
      // 46c9: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 46cc: bipush 59
      // 46ce: aaload
      // 46cf: bipush 23
      // 46d1: bipush 19
      // 46d3: iastore
      // 46d4: aload 0
      // 46d5: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 46d8: bipush 18
      // 46da: aaload
      // 46db: bipush 47
      // 46dd: bipush 18
      // 46df: iastore
      // 46e0: aload 0
      // 46e1: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 46e4: bipush 45
      // 46e6: aaload
      // 46e7: sipush 134
      // 46ea: bipush 17
      // 46ec: iastore
      // 46ed: aload 0
      // 46ee: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 46f1: bipush 37
      // 46f3: aaload
      // 46f4: bipush 59
      // 46f6: bipush 16
      // 46f8: iastore
      // 46f9: aload 0
      // 46fa: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 46fd: bipush 21
      // 46ff: aaload
      // 4700: sipush 128
      // 4703: bipush 15
      // 4705: iastore
      // 4706: aload 0
      // 4707: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 470a: bipush 36
      // 470c: aaload
      // 470d: bipush 106
      // 470f: bipush 14
      // 4711: iastore
      // 4712: aload 0
      // 4713: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4716: bipush 31
      // 4718: aaload
      // 4719: bipush 39
      // 471b: bipush 13
      // 471d: iastore
      // 471e: aload 0
      // 471f: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4722: bipush 40
      // 4724: aaload
      // 4725: sipush 182
      // 4728: bipush 12
      // 472a: iastore
      // 472b: aload 0
      // 472c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 472f: bipush 52
      // 4731: aaload
      // 4732: sipush 155
      // 4735: bipush 11
      // 4737: iastore
      // 4738: aload 0
      // 4739: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 473c: bipush 42
      // 473e: aaload
      // 473f: sipush 166
      // 4742: bipush 10
      // 4744: iastore
      // 4745: aload 0
      // 4746: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4749: bipush 35
      // 474b: aaload
      // 474c: bipush 27
      // 474e: bipush 9
      // 4750: iastore
      // 4751: aload 0
      // 4752: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4755: bipush 38
      // 4757: aaload
      // 4758: bipush 3
      // 4759: bipush 8
      // 475b: iastore
      // 475c: aload 0
      // 475d: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4760: bipush 13
      // 4762: aaload
      // 4763: bipush 44
      // 4765: bipush 7
      // 4767: iastore
      // 4768: aload 0
      // 4769: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 476c: bipush 58
      // 476e: aaload
      // 476f: sipush 157
      // 4772: bipush 6
      // 4774: iastore
      // 4775: aload 0
      // 4776: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4779: bipush 47
      // 477b: aaload
      // 477c: bipush 51
      // 477e: bipush 5
      // 477f: iastore
      // 4780: aload 0
      // 4781: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 4784: bipush 41
      // 4786: aaload
      // 4787: bipush 37
      // 4789: bipush 4
      // 478a: iastore
      // 478b: aload 0
      // 478c: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 478f: bipush 41
      // 4791: aaload
      // 4792: sipush 172
      // 4795: bipush 3
      // 4796: iastore
      // 4797: aload 0
      // 4798: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 479b: bipush 51
      // 479d: aaload
      // 479e: sipush 165
      // 47a1: bipush 2
      // 47a2: iastore
      // 47a3: aload 0
      // 47a4: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 47a7: bipush 15
      // 47a9: aaload
      // 47aa: sipush 161
      // 47ad: bipush 1
      // 47ae: iastore
      // 47af: aload 0
      // 47b0: getfield io/legado/app/help/BytesEncodingDetect.Big5PFreq [[I
      // 47b3: bipush 24
      // 47b5: aaload
      // 47b6: sipush 181
      // 47b9: bipush 0
      // 47ba: iastore
      // 47bb: aload 0
      // 47bc: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 47bf: bipush 48
      // 47c1: aaload
      // 47c2: bipush 49
      // 47c4: sipush 599
      // 47c7: iastore
      // 47c8: aload 0
      // 47c9: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 47cc: bipush 35
      // 47ce: aaload
      // 47cf: bipush 65
      // 47d1: sipush 598
      // 47d4: iastore
      // 47d5: aload 0
      // 47d6: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 47d9: bipush 41
      // 47db: aaload
      // 47dc: bipush 27
      // 47de: sipush 597
      // 47e1: iastore
      // 47e2: aload 0
      // 47e3: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 47e6: bipush 35
      // 47e8: aaload
      // 47e9: bipush 0
      // 47ea: sipush 596
      // 47ed: iastore
      // 47ee: aload 0
      // 47ef: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 47f2: bipush 39
      // 47f4: aaload
      // 47f5: bipush 19
      // 47f7: sipush 595
      // 47fa: iastore
      // 47fb: aload 0
      // 47fc: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 47ff: bipush 35
      // 4801: aaload
      // 4802: bipush 42
      // 4804: sipush 594
      // 4807: iastore
      // 4808: aload 0
      // 4809: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 480c: bipush 38
      // 480e: aaload
      // 480f: bipush 66
      // 4811: sipush 593
      // 4814: iastore
      // 4815: aload 0
      // 4816: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4819: bipush 35
      // 481b: aaload
      // 481c: bipush 8
      // 481e: sipush 592
      // 4821: iastore
      // 4822: aload 0
      // 4823: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4826: bipush 35
      // 4828: aaload
      // 4829: bipush 6
      // 482b: sipush 591
      // 482e: iastore
      // 482f: aload 0
      // 4830: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4833: bipush 35
      // 4835: aaload
      // 4836: bipush 66
      // 4838: sipush 590
      // 483b: iastore
      // 483c: aload 0
      // 483d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4840: bipush 43
      // 4842: aaload
      // 4843: bipush 14
      // 4845: sipush 589
      // 4848: iastore
      // 4849: aload 0
      // 484a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 484d: bipush 69
      // 484f: aaload
      // 4850: bipush 80
      // 4852: sipush 588
      // 4855: iastore
      // 4856: aload 0
      // 4857: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 485a: bipush 50
      // 485c: aaload
      // 485d: bipush 48
      // 485f: sipush 587
      // 4862: iastore
      // 4863: aload 0
      // 4864: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4867: bipush 36
      // 4869: aaload
      // 486a: bipush 71
      // 486c: sipush 586
      // 486f: iastore
      // 4870: aload 0
      // 4871: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4874: bipush 37
      // 4876: aaload
      // 4877: bipush 10
      // 4879: sipush 585
      // 487c: iastore
      // 487d: aload 0
      // 487e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4881: bipush 60
      // 4883: aaload
      // 4884: bipush 52
      // 4886: sipush 584
      // 4889: iastore
      // 488a: aload 0
      // 488b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 488e: bipush 51
      // 4890: aaload
      // 4891: bipush 21
      // 4893: sipush 583
      // 4896: iastore
      // 4897: aload 0
      // 4898: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 489b: bipush 40
      // 489d: aaload
      // 489e: bipush 2
      // 489f: sipush 582
      // 48a2: iastore
      // 48a3: aload 0
      // 48a4: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 48a7: bipush 67
      // 48a9: aaload
      // 48aa: bipush 35
      // 48ac: sipush 581
      // 48af: iastore
      // 48b0: aload 0
      // 48b1: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 48b4: bipush 38
      // 48b6: aaload
      // 48b7: bipush 78
      // 48b9: sipush 580
      // 48bc: iastore
      // 48bd: aload 0
      // 48be: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 48c1: bipush 49
      // 48c3: aaload
      // 48c4: bipush 18
      // 48c6: sipush 579
      // 48c9: iastore
      // 48ca: aload 0
      // 48cb: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 48ce: bipush 35
      // 48d0: aaload
      // 48d1: bipush 23
      // 48d3: sipush 578
      // 48d6: iastore
      // 48d7: aload 0
      // 48d8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 48db: bipush 42
      // 48dd: aaload
      // 48de: bipush 83
      // 48e0: sipush 577
      // 48e3: iastore
      // 48e4: aload 0
      // 48e5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 48e8: bipush 79
      // 48ea: aaload
      // 48eb: bipush 47
      // 48ed: sipush 576
      // 48f0: iastore
      // 48f1: aload 0
      // 48f2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 48f5: bipush 61
      // 48f7: aaload
      // 48f8: bipush 82
      // 48fa: sipush 575
      // 48fd: iastore
      // 48fe: aload 0
      // 48ff: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4902: bipush 38
      // 4904: aaload
      // 4905: bipush 7
      // 4907: sipush 574
      // 490a: iastore
      // 490b: aload 0
      // 490c: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 490f: bipush 35
      // 4911: aaload
      // 4912: bipush 29
      // 4914: sipush 573
      // 4917: iastore
      // 4918: aload 0
      // 4919: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 491c: bipush 37
      // 491e: aaload
      // 491f: bipush 77
      // 4921: sipush 572
      // 4924: iastore
      // 4925: aload 0
      // 4926: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4929: bipush 54
      // 492b: aaload
      // 492c: bipush 67
      // 492e: sipush 571
      // 4931: iastore
      // 4932: aload 0
      // 4933: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4936: bipush 38
      // 4938: aaload
      // 4939: bipush 80
      // 493b: sipush 570
      // 493e: iastore
      // 493f: aload 0
      // 4940: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4943: bipush 52
      // 4945: aaload
      // 4946: bipush 74
      // 4948: sipush 569
      // 494b: iastore
      // 494c: aload 0
      // 494d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4950: bipush 36
      // 4952: aaload
      // 4953: bipush 37
      // 4955: sipush 568
      // 4958: iastore
      // 4959: aload 0
      // 495a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 495d: bipush 74
      // 495f: aaload
      // 4960: bipush 8
      // 4962: sipush 567
      // 4965: iastore
      // 4966: aload 0
      // 4967: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 496a: bipush 41
      // 496c: aaload
      // 496d: bipush 83
      // 496f: sipush 566
      // 4972: iastore
      // 4973: aload 0
      // 4974: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4977: bipush 36
      // 4979: aaload
      // 497a: bipush 75
      // 497c: sipush 565
      // 497f: iastore
      // 4980: aload 0
      // 4981: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4984: bipush 49
      // 4986: aaload
      // 4987: bipush 63
      // 4989: sipush 564
      // 498c: iastore
      // 498d: aload 0
      // 498e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4991: bipush 42
      // 4993: aaload
      // 4994: bipush 58
      // 4996: sipush 563
      // 4999: iastore
      // 499a: aload 0
      // 499b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 499e: bipush 56
      // 49a0: aaload
      // 49a1: bipush 33
      // 49a3: sipush 562
      // 49a6: iastore
      // 49a7: aload 0
      // 49a8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 49ab: bipush 37
      // 49ad: aaload
      // 49ae: bipush 76
      // 49b0: sipush 561
      // 49b3: iastore
      // 49b4: aload 0
      // 49b5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 49b8: bipush 62
      // 49ba: aaload
      // 49bb: bipush 39
      // 49bd: sipush 560
      // 49c0: iastore
      // 49c1: aload 0
      // 49c2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 49c5: bipush 35
      // 49c7: aaload
      // 49c8: bipush 21
      // 49ca: sipush 559
      // 49cd: iastore
      // 49ce: aload 0
      // 49cf: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 49d2: bipush 70
      // 49d4: aaload
      // 49d5: bipush 19
      // 49d7: sipush 558
      // 49da: iastore
      // 49db: aload 0
      // 49dc: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 49df: bipush 77
      // 49e1: aaload
      // 49e2: bipush 88
      // 49e4: sipush 557
      // 49e7: iastore
      // 49e8: aload 0
      // 49e9: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 49ec: bipush 51
      // 49ee: aaload
      // 49ef: bipush 14
      // 49f1: sipush 556
      // 49f4: iastore
      // 49f5: aload 0
      // 49f6: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 49f9: bipush 36
      // 49fb: aaload
      // 49fc: bipush 17
      // 49fe: sipush 555
      // 4a01: iastore
      // 4a02: aload 0
      // 4a03: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4a06: bipush 44
      // 4a08: aaload
      // 4a09: bipush 51
      // 4a0b: sipush 554
      // 4a0e: iastore
      // 4a0f: aload 0
      // 4a10: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4a13: bipush 38
      // 4a15: aaload
      // 4a16: bipush 72
      // 4a18: sipush 553
      // 4a1b: iastore
      // 4a1c: aload 0
      // 4a1d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4a20: bipush 74
      // 4a22: aaload
      // 4a23: bipush 90
      // 4a25: sipush 552
      // 4a28: iastore
      // 4a29: aload 0
      // 4a2a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4a2d: bipush 35
      // 4a2f: aaload
      // 4a30: bipush 48
      // 4a32: sipush 551
      // 4a35: iastore
      // 4a36: aload 0
      // 4a37: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4a3a: bipush 35
      // 4a3c: aaload
      // 4a3d: bipush 69
      // 4a3f: sipush 550
      // 4a42: iastore
      // 4a43: aload 0
      // 4a44: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4a47: bipush 66
      // 4a49: aaload
      // 4a4a: bipush 86
      // 4a4c: sipush 549
      // 4a4f: iastore
      // 4a50: aload 0
      // 4a51: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4a54: bipush 57
      // 4a56: aaload
      // 4a57: bipush 20
      // 4a59: sipush 548
      // 4a5c: iastore
      // 4a5d: aload 0
      // 4a5e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4a61: bipush 35
      // 4a63: aaload
      // 4a64: bipush 53
      // 4a66: sipush 547
      // 4a69: iastore
      // 4a6a: aload 0
      // 4a6b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4a6e: bipush 36
      // 4a70: aaload
      // 4a71: bipush 87
      // 4a73: sipush 546
      // 4a76: iastore
      // 4a77: aload 0
      // 4a78: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4a7b: bipush 84
      // 4a7d: aaload
      // 4a7e: bipush 67
      // 4a80: sipush 545
      // 4a83: iastore
      // 4a84: aload 0
      // 4a85: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4a88: bipush 70
      // 4a8a: aaload
      // 4a8b: bipush 56
      // 4a8d: sipush 544
      // 4a90: iastore
      // 4a91: aload 0
      // 4a92: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4a95: bipush 71
      // 4a97: aaload
      // 4a98: bipush 54
      // 4a9a: sipush 543
      // 4a9d: iastore
      // 4a9e: aload 0
      // 4a9f: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4aa2: bipush 60
      // 4aa4: aaload
      // 4aa5: bipush 70
      // 4aa7: sipush 542
      // 4aaa: iastore
      // 4aab: aload 0
      // 4aac: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4aaf: bipush 80
      // 4ab1: aaload
      // 4ab2: bipush 1
      // 4ab3: sipush 541
      // 4ab6: iastore
      // 4ab7: aload 0
      // 4ab8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4abb: bipush 39
      // 4abd: aaload
      // 4abe: bipush 59
      // 4ac0: sipush 540
      // 4ac3: iastore
      // 4ac4: aload 0
      // 4ac5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4ac8: bipush 39
      // 4aca: aaload
      // 4acb: bipush 51
      // 4acd: sipush 539
      // 4ad0: iastore
      // 4ad1: aload 0
      // 4ad2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4ad5: bipush 35
      // 4ad7: aaload
      // 4ad8: bipush 44
      // 4ada: sipush 538
      // 4add: iastore
      // 4ade: aload 0
      // 4adf: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4ae2: bipush 48
      // 4ae4: aaload
      // 4ae5: bipush 4
      // 4ae6: sipush 537
      // 4ae9: iastore
      // 4aea: aload 0
      // 4aeb: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4aee: bipush 55
      // 4af0: aaload
      // 4af1: bipush 24
      // 4af3: sipush 536
      // 4af6: iastore
      // 4af7: aload 0
      // 4af8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4afb: bipush 52
      // 4afd: aaload
      // 4afe: bipush 4
      // 4aff: sipush 535
      // 4b02: iastore
      // 4b03: aload 0
      // 4b04: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4b07: bipush 54
      // 4b09: aaload
      // 4b0a: bipush 26
      // 4b0c: sipush 534
      // 4b0f: iastore
      // 4b10: aload 0
      // 4b11: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4b14: bipush 36
      // 4b16: aaload
      // 4b17: bipush 31
      // 4b19: sipush 533
      // 4b1c: iastore
      // 4b1d: aload 0
      // 4b1e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4b21: bipush 37
      // 4b23: aaload
      // 4b24: bipush 22
      // 4b26: sipush 532
      // 4b29: iastore
      // 4b2a: aload 0
      // 4b2b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4b2e: bipush 37
      // 4b30: aaload
      // 4b31: bipush 9
      // 4b33: sipush 531
      // 4b36: iastore
      // 4b37: aload 0
      // 4b38: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4b3b: bipush 46
      // 4b3d: aaload
      // 4b3e: bipush 0
      // 4b3f: sipush 530
      // 4b42: iastore
      // 4b43: aload 0
      // 4b44: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4b47: bipush 56
      // 4b49: aaload
      // 4b4a: bipush 46
      // 4b4c: sipush 529
      // 4b4f: iastore
      // 4b50: aload 0
      // 4b51: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4b54: bipush 47
      // 4b56: aaload
      // 4b57: bipush 93
      // 4b59: sipush 528
      // 4b5c: iastore
      // 4b5d: aload 0
      // 4b5e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4b61: bipush 37
      // 4b63: aaload
      // 4b64: bipush 25
      // 4b66: sipush 527
      // 4b69: iastore
      // 4b6a: aload 0
      // 4b6b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4b6e: bipush 39
      // 4b70: aaload
      // 4b71: bipush 8
      // 4b73: sipush 526
      // 4b76: iastore
      // 4b77: aload 0
      // 4b78: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4b7b: bipush 46
      // 4b7d: aaload
      // 4b7e: bipush 73
      // 4b80: sipush 525
      // 4b83: iastore
      // 4b84: aload 0
      // 4b85: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4b88: bipush 38
      // 4b8a: aaload
      // 4b8b: bipush 48
      // 4b8d: sipush 524
      // 4b90: iastore
      // 4b91: aload 0
      // 4b92: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4b95: bipush 39
      // 4b97: aaload
      // 4b98: bipush 83
      // 4b9a: sipush 523
      // 4b9d: iastore
      // 4b9e: aload 0
      // 4b9f: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4ba2: bipush 60
      // 4ba4: aaload
      // 4ba5: bipush 92
      // 4ba7: sipush 522
      // 4baa: iastore
      // 4bab: aload 0
      // 4bac: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4baf: bipush 70
      // 4bb1: aaload
      // 4bb2: bipush 11
      // 4bb4: sipush 521
      // 4bb7: iastore
      // 4bb8: aload 0
      // 4bb9: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4bbc: bipush 63
      // 4bbe: aaload
      // 4bbf: bipush 84
      // 4bc1: sipush 520
      // 4bc4: iastore
      // 4bc5: aload 0
      // 4bc6: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4bc9: bipush 38
      // 4bcb: aaload
      // 4bcc: bipush 65
      // 4bce: sipush 519
      // 4bd1: iastore
      // 4bd2: aload 0
      // 4bd3: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4bd6: bipush 45
      // 4bd8: aaload
      // 4bd9: bipush 45
      // 4bdb: sipush 518
      // 4bde: iastore
      // 4bdf: aload 0
      // 4be0: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4be3: bipush 63
      // 4be5: aaload
      // 4be6: bipush 49
      // 4be8: sipush 517
      // 4beb: iastore
      // 4bec: aload 0
      // 4bed: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4bf0: bipush 63
      // 4bf2: aaload
      // 4bf3: bipush 50
      // 4bf5: sipush 516
      // 4bf8: iastore
      // 4bf9: aload 0
      // 4bfa: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4bfd: bipush 39
      // 4bff: aaload
      // 4c00: bipush 93
      // 4c02: sipush 515
      // 4c05: iastore
      // 4c06: aload 0
      // 4c07: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4c0a: bipush 68
      // 4c0c: aaload
      // 4c0d: bipush 20
      // 4c0f: sipush 514
      // 4c12: iastore
      // 4c13: aload 0
      // 4c14: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4c17: bipush 44
      // 4c19: aaload
      // 4c1a: bipush 84
      // 4c1c: sipush 513
      // 4c1f: iastore
      // 4c20: aload 0
      // 4c21: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4c24: bipush 66
      // 4c26: aaload
      // 4c27: bipush 34
      // 4c29: sipush 512
      // 4c2c: iastore
      // 4c2d: aload 0
      // 4c2e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4c31: bipush 37
      // 4c33: aaload
      // 4c34: bipush 58
      // 4c36: sipush 511
      // 4c39: iastore
      // 4c3a: aload 0
      // 4c3b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4c3e: bipush 39
      // 4c40: aaload
      // 4c41: bipush 0
      // 4c42: sipush 510
      // 4c45: iastore
      // 4c46: aload 0
      // 4c47: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4c4a: bipush 59
      // 4c4c: aaload
      // 4c4d: bipush 1
      // 4c4e: sipush 509
      // 4c51: iastore
      // 4c52: aload 0
      // 4c53: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4c56: bipush 47
      // 4c58: aaload
      // 4c59: bipush 8
      // 4c5b: sipush 508
      // 4c5e: iastore
      // 4c5f: aload 0
      // 4c60: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4c63: bipush 61
      // 4c65: aaload
      // 4c66: bipush 17
      // 4c68: sipush 507
      // 4c6b: iastore
      // 4c6c: aload 0
      // 4c6d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4c70: bipush 53
      // 4c72: aaload
      // 4c73: bipush 87
      // 4c75: sipush 506
      // 4c78: iastore
      // 4c79: aload 0
      // 4c7a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4c7d: bipush 67
      // 4c7f: aaload
      // 4c80: bipush 26
      // 4c82: sipush 505
      // 4c85: iastore
      // 4c86: aload 0
      // 4c87: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4c8a: bipush 43
      // 4c8c: aaload
      // 4c8d: bipush 46
      // 4c8f: sipush 504
      // 4c92: iastore
      // 4c93: aload 0
      // 4c94: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4c97: bipush 38
      // 4c99: aaload
      // 4c9a: bipush 61
      // 4c9c: sipush 503
      // 4c9f: iastore
      // 4ca0: aload 0
      // 4ca1: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4ca4: bipush 45
      // 4ca6: aaload
      // 4ca7: bipush 9
      // 4ca9: sipush 502
      // 4cac: iastore
      // 4cad: aload 0
      // 4cae: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4cb1: bipush 66
      // 4cb3: aaload
      // 4cb4: bipush 83
      // 4cb6: sipush 501
      // 4cb9: iastore
      // 4cba: aload 0
      // 4cbb: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4cbe: bipush 43
      // 4cc0: aaload
      // 4cc1: bipush 88
      // 4cc3: sipush 500
      // 4cc6: iastore
      // 4cc7: aload 0
      // 4cc8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4ccb: bipush 85
      // 4ccd: aaload
      // 4cce: bipush 20
      // 4cd0: sipush 499
      // 4cd3: iastore
      // 4cd4: aload 0
      // 4cd5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4cd8: bipush 57
      // 4cda: aaload
      // 4cdb: bipush 36
      // 4cdd: sipush 498
      // 4ce0: iastore
      // 4ce1: aload 0
      // 4ce2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4ce5: bipush 43
      // 4ce7: aaload
      // 4ce8: bipush 6
      // 4cea: sipush 497
      // 4ced: iastore
      // 4cee: aload 0
      // 4cef: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4cf2: bipush 86
      // 4cf4: aaload
      // 4cf5: bipush 77
      // 4cf7: sipush 496
      // 4cfa: iastore
      // 4cfb: aload 0
      // 4cfc: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4cff: bipush 42
      // 4d01: aaload
      // 4d02: bipush 70
      // 4d04: sipush 495
      // 4d07: iastore
      // 4d08: aload 0
      // 4d09: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4d0c: bipush 49
      // 4d0e: aaload
      // 4d0f: bipush 78
      // 4d11: sipush 494
      // 4d14: iastore
      // 4d15: aload 0
      // 4d16: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4d19: bipush 36
      // 4d1b: aaload
      // 4d1c: bipush 40
      // 4d1e: sipush 493
      // 4d21: iastore
      // 4d22: aload 0
      // 4d23: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4d26: bipush 42
      // 4d28: aaload
      // 4d29: bipush 71
      // 4d2b: sipush 492
      // 4d2e: iastore
      // 4d2f: aload 0
      // 4d30: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4d33: bipush 58
      // 4d35: aaload
      // 4d36: bipush 49
      // 4d38: sipush 491
      // 4d3b: iastore
      // 4d3c: aload 0
      // 4d3d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4d40: bipush 35
      // 4d42: aaload
      // 4d43: bipush 20
      // 4d45: sipush 490
      // 4d48: iastore
      // 4d49: aload 0
      // 4d4a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4d4d: bipush 76
      // 4d4f: aaload
      // 4d50: bipush 20
      // 4d52: sipush 489
      // 4d55: iastore
      // 4d56: aload 0
      // 4d57: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4d5a: bipush 39
      // 4d5c: aaload
      // 4d5d: bipush 25
      // 4d5f: sipush 488
      // 4d62: iastore
      // 4d63: aload 0
      // 4d64: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4d67: bipush 40
      // 4d69: aaload
      // 4d6a: bipush 34
      // 4d6c: sipush 487
      // 4d6f: iastore
      // 4d70: aload 0
      // 4d71: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4d74: bipush 39
      // 4d76: aaload
      // 4d77: bipush 76
      // 4d79: sipush 486
      // 4d7c: iastore
      // 4d7d: aload 0
      // 4d7e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4d81: bipush 40
      // 4d83: aaload
      // 4d84: bipush 1
      // 4d85: sipush 485
      // 4d88: iastore
      // 4d89: aload 0
      // 4d8a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4d8d: bipush 59
      // 4d8f: aaload
      // 4d90: bipush 0
      // 4d91: sipush 484
      // 4d94: iastore
      // 4d95: aload 0
      // 4d96: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4d99: bipush 39
      // 4d9b: aaload
      // 4d9c: bipush 70
      // 4d9e: sipush 483
      // 4da1: iastore
      // 4da2: aload 0
      // 4da3: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4da6: bipush 46
      // 4da8: aaload
      // 4da9: bipush 14
      // 4dab: sipush 482
      // 4dae: iastore
      // 4daf: aload 0
      // 4db0: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4db3: bipush 68
      // 4db5: aaload
      // 4db6: bipush 77
      // 4db8: sipush 481
      // 4dbb: iastore
      // 4dbc: aload 0
      // 4dbd: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4dc0: bipush 38
      // 4dc2: aaload
      // 4dc3: bipush 55
      // 4dc5: sipush 480
      // 4dc8: iastore
      // 4dc9: aload 0
      // 4dca: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4dcd: bipush 35
      // 4dcf: aaload
      // 4dd0: bipush 78
      // 4dd2: sipush 479
      // 4dd5: iastore
      // 4dd6: aload 0
      // 4dd7: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4dda: bipush 84
      // 4ddc: aaload
      // 4ddd: bipush 44
      // 4ddf: sipush 478
      // 4de2: iastore
      // 4de3: aload 0
      // 4de4: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4de7: bipush 36
      // 4de9: aaload
      // 4dea: bipush 41
      // 4dec: sipush 477
      // 4def: iastore
      // 4df0: aload 0
      // 4df1: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4df4: bipush 37
      // 4df6: aaload
      // 4df7: bipush 62
      // 4df9: sipush 476
      // 4dfc: iastore
      // 4dfd: aload 0
      // 4dfe: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4e01: bipush 65
      // 4e03: aaload
      // 4e04: bipush 67
      // 4e06: sipush 475
      // 4e09: iastore
      // 4e0a: aload 0
      // 4e0b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4e0e: bipush 69
      // 4e10: aaload
      // 4e11: bipush 66
      // 4e13: sipush 474
      // 4e16: iastore
      // 4e17: aload 0
      // 4e18: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4e1b: bipush 73
      // 4e1d: aaload
      // 4e1e: bipush 55
      // 4e20: sipush 473
      // 4e23: iastore
      // 4e24: aload 0
      // 4e25: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4e28: bipush 71
      // 4e2a: aaload
      // 4e2b: bipush 49
      // 4e2d: sipush 472
      // 4e30: iastore
      // 4e31: aload 0
      // 4e32: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4e35: bipush 66
      // 4e37: aaload
      // 4e38: bipush 87
      // 4e3a: sipush 471
      // 4e3d: iastore
      // 4e3e: aload 0
      // 4e3f: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4e42: bipush 38
      // 4e44: aaload
      // 4e45: bipush 33
      // 4e47: sipush 470
      // 4e4a: iastore
      // 4e4b: aload 0
      // 4e4c: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4e4f: bipush 64
      // 4e51: aaload
      // 4e52: bipush 61
      // 4e54: sipush 469
      // 4e57: iastore
      // 4e58: aload 0
      // 4e59: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4e5c: bipush 35
      // 4e5e: aaload
      // 4e5f: bipush 7
      // 4e61: sipush 468
      // 4e64: iastore
      // 4e65: aload 0
      // 4e66: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4e69: bipush 47
      // 4e6b: aaload
      // 4e6c: bipush 49
      // 4e6e: sipush 467
      // 4e71: iastore
      // 4e72: aload 0
      // 4e73: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4e76: bipush 56
      // 4e78: aaload
      // 4e79: bipush 14
      // 4e7b: sipush 466
      // 4e7e: iastore
      // 4e7f: aload 0
      // 4e80: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4e83: bipush 36
      // 4e85: aaload
      // 4e86: bipush 49
      // 4e88: sipush 465
      // 4e8b: iastore
      // 4e8c: aload 0
      // 4e8d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4e90: bipush 50
      // 4e92: aaload
      // 4e93: bipush 81
      // 4e95: sipush 464
      // 4e98: iastore
      // 4e99: aload 0
      // 4e9a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4e9d: bipush 55
      // 4e9f: aaload
      // 4ea0: bipush 76
      // 4ea2: sipush 463
      // 4ea5: iastore
      // 4ea6: aload 0
      // 4ea7: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4eaa: bipush 35
      // 4eac: aaload
      // 4ead: bipush 19
      // 4eaf: sipush 462
      // 4eb2: iastore
      // 4eb3: aload 0
      // 4eb4: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4eb7: bipush 44
      // 4eb9: aaload
      // 4eba: bipush 47
      // 4ebc: sipush 461
      // 4ebf: iastore
      // 4ec0: aload 0
      // 4ec1: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4ec4: bipush 35
      // 4ec6: aaload
      // 4ec7: bipush 15
      // 4ec9: sipush 460
      // 4ecc: iastore
      // 4ecd: aload 0
      // 4ece: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4ed1: bipush 82
      // 4ed3: aaload
      // 4ed4: bipush 59
      // 4ed6: sipush 459
      // 4ed9: iastore
      // 4eda: aload 0
      // 4edb: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4ede: bipush 35
      // 4ee0: aaload
      // 4ee1: bipush 43
      // 4ee3: sipush 458
      // 4ee6: iastore
      // 4ee7: aload 0
      // 4ee8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4eeb: bipush 73
      // 4eed: aaload
      // 4eee: bipush 0
      // 4eef: sipush 457
      // 4ef2: iastore
      // 4ef3: aload 0
      // 4ef4: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4ef7: bipush 57
      // 4ef9: aaload
      // 4efa: bipush 83
      // 4efc: sipush 456
      // 4eff: iastore
      // 4f00: aload 0
      // 4f01: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4f04: bipush 42
      // 4f06: aaload
      // 4f07: bipush 46
      // 4f09: sipush 455
      // 4f0c: iastore
      // 4f0d: aload 0
      // 4f0e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4f11: bipush 36
      // 4f13: aaload
      // 4f14: bipush 0
      // 4f15: sipush 454
      // 4f18: iastore
      // 4f19: aload 0
      // 4f1a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4f1d: bipush 70
      // 4f1f: aaload
      // 4f20: bipush 88
      // 4f22: sipush 453
      // 4f25: iastore
      // 4f26: aload 0
      // 4f27: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4f2a: bipush 42
      // 4f2c: aaload
      // 4f2d: bipush 22
      // 4f2f: sipush 452
      // 4f32: iastore
      // 4f33: aload 0
      // 4f34: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4f37: bipush 46
      // 4f39: aaload
      // 4f3a: bipush 58
      // 4f3c: sipush 451
      // 4f3f: iastore
      // 4f40: aload 0
      // 4f41: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4f44: bipush 36
      // 4f46: aaload
      // 4f47: bipush 34
      // 4f49: sipush 450
      // 4f4c: iastore
      // 4f4d: aload 0
      // 4f4e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4f51: bipush 39
      // 4f53: aaload
      // 4f54: bipush 24
      // 4f56: sipush 449
      // 4f59: iastore
      // 4f5a: aload 0
      // 4f5b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4f5e: bipush 35
      // 4f60: aaload
      // 4f61: bipush 55
      // 4f63: sipush 448
      // 4f66: iastore
      // 4f67: aload 0
      // 4f68: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4f6b: bipush 44
      // 4f6d: aaload
      // 4f6e: bipush 91
      // 4f70: sipush 447
      // 4f73: iastore
      // 4f74: aload 0
      // 4f75: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4f78: bipush 37
      // 4f7a: aaload
      // 4f7b: bipush 51
      // 4f7d: sipush 446
      // 4f80: iastore
      // 4f81: aload 0
      // 4f82: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4f85: bipush 36
      // 4f87: aaload
      // 4f88: bipush 19
      // 4f8a: sipush 445
      // 4f8d: iastore
      // 4f8e: aload 0
      // 4f8f: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4f92: bipush 69
      // 4f94: aaload
      // 4f95: bipush 90
      // 4f97: sipush 444
      // 4f9a: iastore
      // 4f9b: aload 0
      // 4f9c: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4f9f: bipush 55
      // 4fa1: aaload
      // 4fa2: bipush 35
      // 4fa4: sipush 443
      // 4fa7: iastore
      // 4fa8: aload 0
      // 4fa9: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4fac: bipush 35
      // 4fae: aaload
      // 4faf: bipush 54
      // 4fb1: sipush 442
      // 4fb4: iastore
      // 4fb5: aload 0
      // 4fb6: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4fb9: bipush 49
      // 4fbb: aaload
      // 4fbc: bipush 61
      // 4fbe: sipush 441
      // 4fc1: iastore
      // 4fc2: aload 0
      // 4fc3: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4fc6: bipush 36
      // 4fc8: aaload
      // 4fc9: bipush 67
      // 4fcb: sipush 440
      // 4fce: iastore
      // 4fcf: aload 0
      // 4fd0: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4fd3: bipush 88
      // 4fd5: aaload
      // 4fd6: bipush 34
      // 4fd8: sipush 439
      // 4fdb: iastore
      // 4fdc: aload 0
      // 4fdd: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4fe0: bipush 35
      // 4fe2: aaload
      // 4fe3: bipush 17
      // 4fe5: sipush 438
      // 4fe8: iastore
      // 4fe9: aload 0
      // 4fea: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4fed: bipush 65
      // 4fef: aaload
      // 4ff0: bipush 69
      // 4ff2: sipush 437
      // 4ff5: iastore
      // 4ff6: aload 0
      // 4ff7: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 4ffa: bipush 74
      // 4ffc: aaload
      // 4ffd: bipush 89
      // 4fff: sipush 436
      // 5002: iastore
      // 5003: aload 0
      // 5004: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5007: bipush 37
      // 5009: aaload
      // 500a: bipush 31
      // 500c: sipush 435
      // 500f: iastore
      // 5010: aload 0
      // 5011: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5014: bipush 43
      // 5016: aaload
      // 5017: bipush 48
      // 5019: sipush 434
      // 501c: iastore
      // 501d: aload 0
      // 501e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5021: bipush 89
      // 5023: aaload
      // 5024: bipush 27
      // 5026: sipush 433
      // 5029: iastore
      // 502a: aload 0
      // 502b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 502e: bipush 42
      // 5030: aaload
      // 5031: bipush 79
      // 5033: sipush 432
      // 5036: iastore
      // 5037: aload 0
      // 5038: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 503b: bipush 69
      // 503d: aaload
      // 503e: bipush 57
      // 5040: sipush 431
      // 5043: iastore
      // 5044: aload 0
      // 5045: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5048: bipush 36
      // 504a: aaload
      // 504b: bipush 13
      // 504d: sipush 430
      // 5050: iastore
      // 5051: aload 0
      // 5052: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5055: bipush 35
      // 5057: aaload
      // 5058: bipush 62
      // 505a: sipush 429
      // 505d: iastore
      // 505e: aload 0
      // 505f: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5062: bipush 65
      // 5064: aaload
      // 5065: bipush 47
      // 5067: sipush 428
      // 506a: iastore
      // 506b: aload 0
      // 506c: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 506f: bipush 56
      // 5071: aaload
      // 5072: bipush 8
      // 5074: sipush 427
      // 5077: iastore
      // 5078: aload 0
      // 5079: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 507c: bipush 38
      // 507e: aaload
      // 507f: bipush 79
      // 5081: sipush 426
      // 5084: iastore
      // 5085: aload 0
      // 5086: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5089: bipush 37
      // 508b: aaload
      // 508c: bipush 64
      // 508e: sipush 425
      // 5091: iastore
      // 5092: aload 0
      // 5093: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5096: bipush 64
      // 5098: aaload
      // 5099: bipush 64
      // 509b: sipush 424
      // 509e: iastore
      // 509f: aload 0
      // 50a0: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 50a3: bipush 38
      // 50a5: aaload
      // 50a6: bipush 53
      // 50a8: sipush 423
      // 50ab: iastore
      // 50ac: aload 0
      // 50ad: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 50b0: bipush 38
      // 50b2: aaload
      // 50b3: bipush 31
      // 50b5: sipush 422
      // 50b8: iastore
      // 50b9: aload 0
      // 50ba: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 50bd: bipush 56
      // 50bf: aaload
      // 50c0: bipush 81
      // 50c2: sipush 421
      // 50c5: iastore
      // 50c6: aload 0
      // 50c7: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 50ca: bipush 36
      // 50cc: aaload
      // 50cd: bipush 22
      // 50cf: sipush 420
      // 50d2: iastore
      // 50d3: aload 0
      // 50d4: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 50d7: bipush 43
      // 50d9: aaload
      // 50da: bipush 4
      // 50db: sipush 419
      // 50de: iastore
      // 50df: aload 0
      // 50e0: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 50e3: bipush 36
      // 50e5: aaload
      // 50e6: bipush 90
      // 50e8: sipush 418
      // 50eb: iastore
      // 50ec: aload 0
      // 50ed: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 50f0: bipush 38
      // 50f2: aaload
      // 50f3: bipush 62
      // 50f5: sipush 417
      // 50f8: iastore
      // 50f9: aload 0
      // 50fa: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 50fd: bipush 66
      // 50ff: aaload
      // 5100: bipush 85
      // 5102: sipush 416
      // 5105: iastore
      // 5106: aload 0
      // 5107: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 510a: bipush 39
      // 510c: aaload
      // 510d: bipush 1
      // 510e: sipush 415
      // 5111: iastore
      // 5112: aload 0
      // 5113: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5116: bipush 59
      // 5118: aaload
      // 5119: bipush 40
      // 511b: sipush 414
      // 511e: iastore
      // 511f: aload 0
      // 5120: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5123: bipush 58
      // 5125: aaload
      // 5126: bipush 93
      // 5128: sipush 413
      // 512b: iastore
      // 512c: aload 0
      // 512d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5130: bipush 44
      // 5132: aaload
      // 5133: bipush 43
      // 5135: sipush 412
      // 5138: iastore
      // 5139: aload 0
      // 513a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 513d: bipush 39
      // 513f: aaload
      // 5140: bipush 49
      // 5142: sipush 411
      // 5145: iastore
      // 5146: aload 0
      // 5147: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 514a: bipush 64
      // 514c: aaload
      // 514d: bipush 2
      // 514e: sipush 410
      // 5151: iastore
      // 5152: aload 0
      // 5153: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5156: bipush 41
      // 5158: aaload
      // 5159: bipush 35
      // 515b: sipush 409
      // 515e: iastore
      // 515f: aload 0
      // 5160: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5163: bipush 60
      // 5165: aaload
      // 5166: bipush 22
      // 5168: sipush 408
      // 516b: iastore
      // 516c: aload 0
      // 516d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5170: bipush 35
      // 5172: aaload
      // 5173: bipush 91
      // 5175: sipush 407
      // 5178: iastore
      // 5179: aload 0
      // 517a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 517d: bipush 78
      // 517f: aaload
      // 5180: bipush 1
      // 5181: sipush 406
      // 5184: iastore
      // 5185: aload 0
      // 5186: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5189: bipush 36
      // 518b: aaload
      // 518c: bipush 14
      // 518e: sipush 405
      // 5191: iastore
      // 5192: aload 0
      // 5193: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5196: bipush 82
      // 5198: aaload
      // 5199: bipush 29
      // 519b: sipush 404
      // 519e: iastore
      // 519f: aload 0
      // 51a0: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 51a3: bipush 52
      // 51a5: aaload
      // 51a6: bipush 86
      // 51a8: sipush 403
      // 51ab: iastore
      // 51ac: aload 0
      // 51ad: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 51b0: bipush 40
      // 51b2: aaload
      // 51b3: bipush 16
      // 51b5: sipush 402
      // 51b8: iastore
      // 51b9: aload 0
      // 51ba: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 51bd: bipush 91
      // 51bf: aaload
      // 51c0: bipush 52
      // 51c2: sipush 401
      // 51c5: iastore
      // 51c6: aload 0
      // 51c7: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 51ca: bipush 50
      // 51cc: aaload
      // 51cd: bipush 75
      // 51cf: sipush 400
      // 51d2: iastore
      // 51d3: aload 0
      // 51d4: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 51d7: bipush 64
      // 51d9: aaload
      // 51da: bipush 30
      // 51dc: sipush 399
      // 51df: iastore
      // 51e0: aload 0
      // 51e1: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 51e4: bipush 90
      // 51e6: aaload
      // 51e7: bipush 78
      // 51e9: sipush 398
      // 51ec: iastore
      // 51ed: aload 0
      // 51ee: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 51f1: bipush 36
      // 51f3: aaload
      // 51f4: bipush 52
      // 51f6: sipush 397
      // 51f9: iastore
      // 51fa: aload 0
      // 51fb: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 51fe: bipush 55
      // 5200: aaload
      // 5201: bipush 87
      // 5203: sipush 396
      // 5206: iastore
      // 5207: aload 0
      // 5208: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 520b: bipush 57
      // 520d: aaload
      // 520e: bipush 5
      // 520f: sipush 395
      // 5212: iastore
      // 5213: aload 0
      // 5214: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5217: bipush 57
      // 5219: aaload
      // 521a: bipush 31
      // 521c: sipush 394
      // 521f: iastore
      // 5220: aload 0
      // 5221: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5224: bipush 42
      // 5226: aaload
      // 5227: bipush 35
      // 5229: sipush 393
      // 522c: iastore
      // 522d: aload 0
      // 522e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5231: bipush 69
      // 5233: aaload
      // 5234: bipush 50
      // 5236: sipush 392
      // 5239: iastore
      // 523a: aload 0
      // 523b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 523e: bipush 45
      // 5240: aaload
      // 5241: bipush 8
      // 5243: sipush 391
      // 5246: iastore
      // 5247: aload 0
      // 5248: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 524b: bipush 50
      // 524d: aaload
      // 524e: bipush 87
      // 5250: sipush 390
      // 5253: iastore
      // 5254: aload 0
      // 5255: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5258: bipush 69
      // 525a: aaload
      // 525b: bipush 55
      // 525d: sipush 389
      // 5260: iastore
      // 5261: aload 0
      // 5262: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5265: bipush 92
      // 5267: aaload
      // 5268: bipush 3
      // 5269: sipush 388
      // 526c: iastore
      // 526d: aload 0
      // 526e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5271: bipush 36
      // 5273: aaload
      // 5274: bipush 43
      // 5276: sipush 387
      // 5279: iastore
      // 527a: aload 0
      // 527b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 527e: bipush 64
      // 5280: aaload
      // 5281: bipush 10
      // 5283: sipush 386
      // 5286: iastore
      // 5287: aload 0
      // 5288: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 528b: bipush 56
      // 528d: aaload
      // 528e: bipush 25
      // 5290: sipush 385
      // 5293: iastore
      // 5294: aload 0
      // 5295: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5298: bipush 60
      // 529a: aaload
      // 529b: bipush 68
      // 529d: sipush 384
      // 52a0: iastore
      // 52a1: aload 0
      // 52a2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 52a5: bipush 51
      // 52a7: aaload
      // 52a8: bipush 46
      // 52aa: sipush 383
      // 52ad: iastore
      // 52ae: aload 0
      // 52af: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 52b2: bipush 50
      // 52b4: aaload
      // 52b5: bipush 0
      // 52b6: sipush 382
      // 52b9: iastore
      // 52ba: aload 0
      // 52bb: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 52be: bipush 38
      // 52c0: aaload
      // 52c1: bipush 30
      // 52c3: sipush 381
      // 52c6: iastore
      // 52c7: aload 0
      // 52c8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 52cb: bipush 50
      // 52cd: aaload
      // 52ce: bipush 85
      // 52d0: sipush 380
      // 52d3: iastore
      // 52d4: aload 0
      // 52d5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 52d8: bipush 60
      // 52da: aaload
      // 52db: bipush 54
      // 52dd: sipush 379
      // 52e0: iastore
      // 52e1: aload 0
      // 52e2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 52e5: bipush 73
      // 52e7: aaload
      // 52e8: bipush 6
      // 52ea: sipush 378
      // 52ed: iastore
      // 52ee: aload 0
      // 52ef: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 52f2: bipush 73
      // 52f4: aaload
      // 52f5: bipush 28
      // 52f7: sipush 377
      // 52fa: iastore
      // 52fb: aload 0
      // 52fc: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 52ff: bipush 56
      // 5301: aaload
      // 5302: bipush 19
      // 5304: sipush 376
      // 5307: iastore
      // 5308: aload 0
      // 5309: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 530c: bipush 62
      // 530e: aaload
      // 530f: bipush 69
      // 5311: sipush 375
      // 5314: iastore
      // 5315: aload 0
      // 5316: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5319: bipush 81
      // 531b: aaload
      // 531c: bipush 66
      // 531e: sipush 374
      // 5321: iastore
      // 5322: aload 0
      // 5323: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5326: bipush 40
      // 5328: aaload
      // 5329: bipush 32
      // 532b: sipush 373
      // 532e: iastore
      // 532f: aload 0
      // 5330: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5333: bipush 76
      // 5335: aaload
      // 5336: bipush 31
      // 5338: sipush 372
      // 533b: iastore
      // 533c: aload 0
      // 533d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5340: bipush 35
      // 5342: aaload
      // 5343: bipush 10
      // 5345: sipush 371
      // 5348: iastore
      // 5349: aload 0
      // 534a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 534d: bipush 41
      // 534f: aaload
      // 5350: bipush 37
      // 5352: sipush 370
      // 5355: iastore
      // 5356: aload 0
      // 5357: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 535a: bipush 52
      // 535c: aaload
      // 535d: bipush 82
      // 535f: sipush 369
      // 5362: iastore
      // 5363: aload 0
      // 5364: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5367: bipush 91
      // 5369: aaload
      // 536a: bipush 72
      // 536c: sipush 368
      // 536f: iastore
      // 5370: aload 0
      // 5371: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5374: bipush 37
      // 5376: aaload
      // 5377: bipush 29
      // 5379: sipush 367
      // 537c: iastore
      // 537d: aload 0
      // 537e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5381: bipush 56
      // 5383: aaload
      // 5384: bipush 30
      // 5386: sipush 366
      // 5389: iastore
      // 538a: aload 0
      // 538b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 538e: bipush 37
      // 5390: aaload
      // 5391: bipush 80
      // 5393: sipush 365
      // 5396: iastore
      // 5397: aload 0
      // 5398: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 539b: bipush 81
      // 539d: aaload
      // 539e: bipush 56
      // 53a0: sipush 364
      // 53a3: iastore
      // 53a4: aload 0
      // 53a5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 53a8: bipush 70
      // 53aa: aaload
      // 53ab: bipush 3
      // 53ac: sipush 363
      // 53af: iastore
      // 53b0: aload 0
      // 53b1: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 53b4: bipush 76
      // 53b6: aaload
      // 53b7: bipush 15
      // 53b9: sipush 362
      // 53bc: iastore
      // 53bd: aload 0
      // 53be: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 53c1: bipush 46
      // 53c3: aaload
      // 53c4: bipush 47
      // 53c6: sipush 361
      // 53c9: iastore
      // 53ca: aload 0
      // 53cb: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 53ce: bipush 35
      // 53d0: aaload
      // 53d1: bipush 88
      // 53d3: sipush 360
      // 53d6: iastore
      // 53d7: aload 0
      // 53d8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 53db: bipush 61
      // 53dd: aaload
      // 53de: bipush 58
      // 53e0: sipush 359
      // 53e3: iastore
      // 53e4: aload 0
      // 53e5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 53e8: bipush 37
      // 53ea: aaload
      // 53eb: bipush 37
      // 53ed: sipush 358
      // 53f0: iastore
      // 53f1: aload 0
      // 53f2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 53f5: bipush 57
      // 53f7: aaload
      // 53f8: bipush 22
      // 53fa: sipush 357
      // 53fd: iastore
      // 53fe: aload 0
      // 53ff: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5402: bipush 41
      // 5404: aaload
      // 5405: bipush 23
      // 5407: sipush 356
      // 540a: iastore
      // 540b: aload 0
      // 540c: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 540f: bipush 90
      // 5411: aaload
      // 5412: bipush 66
      // 5414: sipush 355
      // 5417: iastore
      // 5418: aload 0
      // 5419: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 541c: bipush 39
      // 541e: aaload
      // 541f: bipush 60
      // 5421: sipush 354
      // 5424: iastore
      // 5425: aload 0
      // 5426: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5429: bipush 38
      // 542b: aaload
      // 542c: bipush 0
      // 542d: sipush 353
      // 5430: iastore
      // 5431: aload 0
      // 5432: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5435: bipush 37
      // 5437: aaload
      // 5438: bipush 87
      // 543a: sipush 352
      // 543d: iastore
      // 543e: aload 0
      // 543f: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5442: bipush 46
      // 5444: aaload
      // 5445: bipush 2
      // 5446: sipush 351
      // 5449: iastore
      // 544a: aload 0
      // 544b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 544e: bipush 38
      // 5450: aaload
      // 5451: bipush 56
      // 5453: sipush 350
      // 5456: iastore
      // 5457: aload 0
      // 5458: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 545b: bipush 58
      // 545d: aaload
      // 545e: bipush 11
      // 5460: sipush 349
      // 5463: iastore
      // 5464: aload 0
      // 5465: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5468: bipush 48
      // 546a: aaload
      // 546b: bipush 10
      // 546d: sipush 348
      // 5470: iastore
      // 5471: aload 0
      // 5472: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5475: bipush 74
      // 5477: aaload
      // 5478: bipush 4
      // 5479: sipush 347
      // 547c: iastore
      // 547d: aload 0
      // 547e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5481: bipush 40
      // 5483: aaload
      // 5484: bipush 42
      // 5486: sipush 346
      // 5489: iastore
      // 548a: aload 0
      // 548b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 548e: bipush 41
      // 5490: aaload
      // 5491: bipush 52
      // 5493: sipush 345
      // 5496: iastore
      // 5497: aload 0
      // 5498: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 549b: bipush 61
      // 549d: aaload
      // 549e: bipush 92
      // 54a0: sipush 344
      // 54a3: iastore
      // 54a4: aload 0
      // 54a5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 54a8: bipush 39
      // 54aa: aaload
      // 54ab: bipush 50
      // 54ad: sipush 343
      // 54b0: iastore
      // 54b1: aload 0
      // 54b2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 54b5: bipush 47
      // 54b7: aaload
      // 54b8: bipush 88
      // 54ba: sipush 342
      // 54bd: iastore
      // 54be: aload 0
      // 54bf: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 54c2: bipush 88
      // 54c4: aaload
      // 54c5: bipush 36
      // 54c7: sipush 341
      // 54ca: iastore
      // 54cb: aload 0
      // 54cc: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 54cf: bipush 45
      // 54d1: aaload
      // 54d2: bipush 73
      // 54d4: sipush 340
      // 54d7: iastore
      // 54d8: aload 0
      // 54d9: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 54dc: bipush 82
      // 54de: aaload
      // 54df: bipush 3
      // 54e0: sipush 339
      // 54e3: iastore
      // 54e4: aload 0
      // 54e5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 54e8: bipush 61
      // 54ea: aaload
      // 54eb: bipush 36
      // 54ed: sipush 338
      // 54f0: iastore
      // 54f1: aload 0
      // 54f2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 54f5: bipush 60
      // 54f7: aaload
      // 54f8: bipush 33
      // 54fa: sipush 337
      // 54fd: iastore
      // 54fe: aload 0
      // 54ff: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5502: bipush 38
      // 5504: aaload
      // 5505: bipush 27
      // 5507: sipush 336
      // 550a: iastore
      // 550b: aload 0
      // 550c: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 550f: bipush 35
      // 5511: aaload
      // 5512: bipush 83
      // 5514: sipush 335
      // 5517: iastore
      // 5518: aload 0
      // 5519: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 551c: bipush 65
      // 551e: aaload
      // 551f: bipush 24
      // 5521: sipush 334
      // 5524: iastore
      // 5525: aload 0
      // 5526: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5529: bipush 73
      // 552b: aaload
      // 552c: bipush 10
      // 552e: sipush 333
      // 5531: iastore
      // 5532: aload 0
      // 5533: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5536: bipush 41
      // 5538: aaload
      // 5539: bipush 13
      // 553b: sipush 332
      // 553e: iastore
      // 553f: aload 0
      // 5540: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5543: bipush 50
      // 5545: aaload
      // 5546: bipush 27
      // 5548: sipush 331
      // 554b: iastore
      // 554c: aload 0
      // 554d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5550: bipush 59
      // 5552: aaload
      // 5553: bipush 50
      // 5555: sipush 330
      // 5558: iastore
      // 5559: aload 0
      // 555a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 555d: bipush 42
      // 555f: aaload
      // 5560: bipush 45
      // 5562: sipush 329
      // 5565: iastore
      // 5566: aload 0
      // 5567: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 556a: bipush 55
      // 556c: aaload
      // 556d: bipush 19
      // 556f: sipush 328
      // 5572: iastore
      // 5573: aload 0
      // 5574: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5577: bipush 36
      // 5579: aaload
      // 557a: bipush 77
      // 557c: sipush 327
      // 557f: iastore
      // 5580: aload 0
      // 5581: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5584: bipush 69
      // 5586: aaload
      // 5587: bipush 31
      // 5589: sipush 326
      // 558c: iastore
      // 558d: aload 0
      // 558e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5591: bipush 60
      // 5593: aaload
      // 5594: bipush 7
      // 5596: sipush 325
      // 5599: iastore
      // 559a: aload 0
      // 559b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 559e: bipush 40
      // 55a0: aaload
      // 55a1: bipush 88
      // 55a3: sipush 324
      // 55a6: iastore
      // 55a7: aload 0
      // 55a8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 55ab: bipush 57
      // 55ad: aaload
      // 55ae: bipush 56
      // 55b0: sipush 323
      // 55b3: iastore
      // 55b4: aload 0
      // 55b5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 55b8: bipush 50
      // 55ba: aaload
      // 55bb: bipush 50
      // 55bd: sipush 322
      // 55c0: iastore
      // 55c1: aload 0
      // 55c2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 55c5: bipush 42
      // 55c7: aaload
      // 55c8: bipush 37
      // 55ca: sipush 321
      // 55cd: iastore
      // 55ce: aload 0
      // 55cf: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 55d2: bipush 38
      // 55d4: aaload
      // 55d5: bipush 82
      // 55d7: sipush 320
      // 55da: iastore
      // 55db: aload 0
      // 55dc: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 55df: bipush 52
      // 55e1: aaload
      // 55e2: bipush 25
      // 55e4: sipush 319
      // 55e7: iastore
      // 55e8: aload 0
      // 55e9: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 55ec: bipush 42
      // 55ee: aaload
      // 55ef: bipush 67
      // 55f1: sipush 318
      // 55f4: iastore
      // 55f5: aload 0
      // 55f6: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 55f9: bipush 48
      // 55fb: aaload
      // 55fc: bipush 40
      // 55fe: sipush 317
      // 5601: iastore
      // 5602: aload 0
      // 5603: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5606: bipush 45
      // 5608: aaload
      // 5609: bipush 81
      // 560b: sipush 316
      // 560e: iastore
      // 560f: aload 0
      // 5610: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5613: bipush 57
      // 5615: aaload
      // 5616: bipush 14
      // 5618: sipush 315
      // 561b: iastore
      // 561c: aload 0
      // 561d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5620: bipush 42
      // 5622: aaload
      // 5623: bipush 13
      // 5625: sipush 314
      // 5628: iastore
      // 5629: aload 0
      // 562a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 562d: bipush 78
      // 562f: aaload
      // 5630: bipush 0
      // 5631: sipush 313
      // 5634: iastore
      // 5635: aload 0
      // 5636: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5639: bipush 35
      // 563b: aaload
      // 563c: bipush 51
      // 563e: sipush 312
      // 5641: iastore
      // 5642: aload 0
      // 5643: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5646: bipush 41
      // 5648: aaload
      // 5649: bipush 67
      // 564b: sipush 311
      // 564e: iastore
      // 564f: aload 0
      // 5650: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5653: bipush 64
      // 5655: aaload
      // 5656: bipush 23
      // 5658: sipush 310
      // 565b: iastore
      // 565c: aload 0
      // 565d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5660: bipush 36
      // 5662: aaload
      // 5663: bipush 65
      // 5665: sipush 309
      // 5668: iastore
      // 5669: aload 0
      // 566a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 566d: bipush 48
      // 566f: aaload
      // 5670: bipush 50
      // 5672: sipush 308
      // 5675: iastore
      // 5676: aload 0
      // 5677: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 567a: bipush 46
      // 567c: aaload
      // 567d: bipush 69
      // 567f: sipush 307
      // 5682: iastore
      // 5683: aload 0
      // 5684: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5687: bipush 47
      // 5689: aaload
      // 568a: bipush 89
      // 568c: sipush 306
      // 568f: iastore
      // 5690: aload 0
      // 5691: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5694: bipush 41
      // 5696: aaload
      // 5697: bipush 48
      // 5699: sipush 305
      // 569c: iastore
      // 569d: aload 0
      // 569e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 56a1: bipush 60
      // 56a3: aaload
      // 56a4: bipush 56
      // 56a6: sipush 304
      // 56a9: iastore
      // 56aa: aload 0
      // 56ab: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 56ae: bipush 44
      // 56b0: aaload
      // 56b1: bipush 82
      // 56b3: sipush 303
      // 56b6: iastore
      // 56b7: aload 0
      // 56b8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 56bb: bipush 47
      // 56bd: aaload
      // 56be: bipush 35
      // 56c0: sipush 302
      // 56c3: iastore
      // 56c4: aload 0
      // 56c5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 56c8: bipush 49
      // 56ca: aaload
      // 56cb: bipush 3
      // 56cc: sipush 301
      // 56cf: iastore
      // 56d0: aload 0
      // 56d1: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 56d4: bipush 49
      // 56d6: aaload
      // 56d7: bipush 69
      // 56d9: sipush 300
      // 56dc: iastore
      // 56dd: aload 0
      // 56de: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 56e1: bipush 45
      // 56e3: aaload
      // 56e4: bipush 93
      // 56e6: sipush 299
      // 56e9: iastore
      // 56ea: aload 0
      // 56eb: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 56ee: bipush 60
      // 56f0: aaload
      // 56f1: bipush 34
      // 56f3: sipush 298
      // 56f6: iastore
      // 56f7: aload 0
      // 56f8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 56fb: bipush 60
      // 56fd: aaload
      // 56fe: bipush 82
      // 5700: sipush 297
      // 5703: iastore
      // 5704: aload 0
      // 5705: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5708: bipush 61
      // 570a: aaload
      // 570b: bipush 61
      // 570d: sipush 296
      // 5710: iastore
      // 5711: aload 0
      // 5712: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5715: bipush 86
      // 5717: aaload
      // 5718: bipush 42
      // 571a: sipush 295
      // 571d: iastore
      // 571e: aload 0
      // 571f: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5722: bipush 89
      // 5724: aaload
      // 5725: bipush 60
      // 5727: sipush 294
      // 572a: iastore
      // 572b: aload 0
      // 572c: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 572f: bipush 48
      // 5731: aaload
      // 5732: bipush 31
      // 5734: sipush 293
      // 5737: iastore
      // 5738: aload 0
      // 5739: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 573c: bipush 35
      // 573e: aaload
      // 573f: bipush 75
      // 5741: sipush 292
      // 5744: iastore
      // 5745: aload 0
      // 5746: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5749: bipush 91
      // 574b: aaload
      // 574c: bipush 39
      // 574e: sipush 291
      // 5751: iastore
      // 5752: aload 0
      // 5753: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5756: bipush 53
      // 5758: aaload
      // 5759: bipush 19
      // 575b: sipush 290
      // 575e: iastore
      // 575f: aload 0
      // 5760: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5763: bipush 39
      // 5765: aaload
      // 5766: bipush 72
      // 5768: sipush 289
      // 576b: iastore
      // 576c: aload 0
      // 576d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5770: bipush 69
      // 5772: aaload
      // 5773: bipush 59
      // 5775: sipush 288
      // 5778: iastore
      // 5779: aload 0
      // 577a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 577d: bipush 41
      // 577f: aaload
      // 5780: bipush 7
      // 5782: sipush 287
      // 5785: iastore
      // 5786: aload 0
      // 5787: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 578a: bipush 54
      // 578c: aaload
      // 578d: bipush 13
      // 578f: sipush 286
      // 5792: iastore
      // 5793: aload 0
      // 5794: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5797: bipush 43
      // 5799: aaload
      // 579a: bipush 28
      // 579c: sipush 285
      // 579f: iastore
      // 57a0: aload 0
      // 57a1: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 57a4: bipush 36
      // 57a6: aaload
      // 57a7: bipush 6
      // 57a9: sipush 284
      // 57ac: iastore
      // 57ad: aload 0
      // 57ae: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 57b1: bipush 45
      // 57b3: aaload
      // 57b4: bipush 75
      // 57b6: sipush 283
      // 57b9: iastore
      // 57ba: aload 0
      // 57bb: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 57be: bipush 36
      // 57c0: aaload
      // 57c1: bipush 61
      // 57c3: sipush 282
      // 57c6: iastore
      // 57c7: aload 0
      // 57c8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 57cb: bipush 38
      // 57cd: aaload
      // 57ce: bipush 21
      // 57d0: sipush 281
      // 57d3: iastore
      // 57d4: aload 0
      // 57d5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 57d8: bipush 45
      // 57da: aaload
      // 57db: bipush 14
      // 57dd: sipush 280
      // 57e0: iastore
      // 57e1: aload 0
      // 57e2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 57e5: bipush 61
      // 57e7: aaload
      // 57e8: bipush 43
      // 57ea: sipush 279
      // 57ed: iastore
      // 57ee: aload 0
      // 57ef: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 57f2: bipush 36
      // 57f4: aaload
      // 57f5: bipush 63
      // 57f7: sipush 278
      // 57fa: iastore
      // 57fb: aload 0
      // 57fc: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 57ff: bipush 43
      // 5801: aaload
      // 5802: bipush 30
      // 5804: sipush 277
      // 5807: iastore
      // 5808: aload 0
      // 5809: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 580c: bipush 46
      // 580e: aaload
      // 580f: bipush 51
      // 5811: sipush 276
      // 5814: iastore
      // 5815: aload 0
      // 5816: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5819: bipush 68
      // 581b: aaload
      // 581c: bipush 87
      // 581e: sipush 275
      // 5821: iastore
      // 5822: aload 0
      // 5823: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5826: bipush 39
      // 5828: aaload
      // 5829: bipush 26
      // 582b: sipush 274
      // 582e: iastore
      // 582f: aload 0
      // 5830: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5833: bipush 46
      // 5835: aaload
      // 5836: bipush 76
      // 5838: sipush 273
      // 583b: iastore
      // 583c: aload 0
      // 583d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5840: bipush 36
      // 5842: aaload
      // 5843: bipush 15
      // 5845: sipush 272
      // 5848: iastore
      // 5849: aload 0
      // 584a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 584d: bipush 35
      // 584f: aaload
      // 5850: bipush 40
      // 5852: sipush 271
      // 5855: iastore
      // 5856: aload 0
      // 5857: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 585a: bipush 79
      // 585c: aaload
      // 585d: bipush 60
      // 585f: sipush 270
      // 5862: iastore
      // 5863: aload 0
      // 5864: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5867: bipush 46
      // 5869: aaload
      // 586a: bipush 7
      // 586c: sipush 269
      // 586f: iastore
      // 5870: aload 0
      // 5871: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5874: bipush 65
      // 5876: aaload
      // 5877: bipush 72
      // 5879: sipush 268
      // 587c: iastore
      // 587d: aload 0
      // 587e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5881: bipush 69
      // 5883: aaload
      // 5884: bipush 88
      // 5886: sipush 267
      // 5889: iastore
      // 588a: aload 0
      // 588b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 588e: bipush 47
      // 5890: aaload
      // 5891: bipush 18
      // 5893: sipush 266
      // 5896: iastore
      // 5897: aload 0
      // 5898: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 589b: bipush 37
      // 589d: aaload
      // 589e: bipush 0
      // 589f: sipush 265
      // 58a2: iastore
      // 58a3: aload 0
      // 58a4: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 58a7: bipush 37
      // 58a9: aaload
      // 58aa: bipush 49
      // 58ac: sipush 264
      // 58af: iastore
      // 58b0: aload 0
      // 58b1: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 58b4: bipush 67
      // 58b6: aaload
      // 58b7: bipush 37
      // 58b9: sipush 263
      // 58bc: iastore
      // 58bd: aload 0
      // 58be: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 58c1: bipush 36
      // 58c3: aaload
      // 58c4: bipush 91
      // 58c6: sipush 262
      // 58c9: iastore
      // 58ca: aload 0
      // 58cb: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 58ce: bipush 75
      // 58d0: aaload
      // 58d1: bipush 48
      // 58d3: sipush 261
      // 58d6: iastore
      // 58d7: aload 0
      // 58d8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 58db: bipush 75
      // 58dd: aaload
      // 58de: bipush 63
      // 58e0: sipush 260
      // 58e3: iastore
      // 58e4: aload 0
      // 58e5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 58e8: bipush 83
      // 58ea: aaload
      // 58eb: bipush 87
      // 58ed: sipush 259
      // 58f0: iastore
      // 58f1: aload 0
      // 58f2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 58f5: bipush 37
      // 58f7: aaload
      // 58f8: bipush 44
      // 58fa: sipush 258
      // 58fd: iastore
      // 58fe: aload 0
      // 58ff: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5902: bipush 73
      // 5904: aaload
      // 5905: bipush 54
      // 5907: sipush 257
      // 590a: iastore
      // 590b: aload 0
      // 590c: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 590f: bipush 51
      // 5911: aaload
      // 5912: bipush 61
      // 5914: sipush 256
      // 5917: iastore
      // 5918: aload 0
      // 5919: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 591c: bipush 46
      // 591e: aaload
      // 591f: bipush 57
      // 5921: sipush 255
      // 5924: iastore
      // 5925: aload 0
      // 5926: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5929: bipush 55
      // 592b: aaload
      // 592c: bipush 21
      // 592e: sipush 254
      // 5931: iastore
      // 5932: aload 0
      // 5933: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5936: bipush 39
      // 5938: aaload
      // 5939: bipush 66
      // 593b: sipush 253
      // 593e: iastore
      // 593f: aload 0
      // 5940: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5943: bipush 47
      // 5945: aaload
      // 5946: bipush 11
      // 5948: sipush 252
      // 594b: iastore
      // 594c: aload 0
      // 594d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5950: bipush 52
      // 5952: aaload
      // 5953: bipush 8
      // 5955: sipush 251
      // 5958: iastore
      // 5959: aload 0
      // 595a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 595d: bipush 82
      // 595f: aaload
      // 5960: bipush 81
      // 5962: sipush 250
      // 5965: iastore
      // 5966: aload 0
      // 5967: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 596a: bipush 36
      // 596c: aaload
      // 596d: bipush 57
      // 596f: sipush 249
      // 5972: iastore
      // 5973: aload 0
      // 5974: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5977: bipush 38
      // 5979: aaload
      // 597a: bipush 54
      // 597c: sipush 248
      // 597f: iastore
      // 5980: aload 0
      // 5981: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5984: bipush 43
      // 5986: aaload
      // 5987: bipush 81
      // 5989: sipush 247
      // 598c: iastore
      // 598d: aload 0
      // 598e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5991: bipush 37
      // 5993: aaload
      // 5994: bipush 42
      // 5996: sipush 246
      // 5999: iastore
      // 599a: aload 0
      // 599b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 599e: bipush 40
      // 59a0: aaload
      // 59a1: bipush 18
      // 59a3: sipush 245
      // 59a6: iastore
      // 59a7: aload 0
      // 59a8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 59ab: bipush 80
      // 59ad: aaload
      // 59ae: bipush 90
      // 59b0: sipush 244
      // 59b3: iastore
      // 59b4: aload 0
      // 59b5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 59b8: bipush 37
      // 59ba: aaload
      // 59bb: bipush 84
      // 59bd: sipush 243
      // 59c0: iastore
      // 59c1: aload 0
      // 59c2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 59c5: bipush 57
      // 59c7: aaload
      // 59c8: bipush 15
      // 59ca: sipush 242
      // 59cd: iastore
      // 59ce: aload 0
      // 59cf: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 59d2: bipush 38
      // 59d4: aaload
      // 59d5: bipush 87
      // 59d7: sipush 241
      // 59da: iastore
      // 59db: aload 0
      // 59dc: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 59df: bipush 37
      // 59e1: aaload
      // 59e2: bipush 32
      // 59e4: sipush 240
      // 59e7: iastore
      // 59e8: aload 0
      // 59e9: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 59ec: bipush 53
      // 59ee: aaload
      // 59ef: bipush 53
      // 59f1: sipush 239
      // 59f4: iastore
      // 59f5: aload 0
      // 59f6: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 59f9: bipush 89
      // 59fb: aaload
      // 59fc: bipush 29
      // 59fe: sipush 238
      // 5a01: iastore
      // 5a02: aload 0
      // 5a03: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5a06: bipush 81
      // 5a08: aaload
      // 5a09: bipush 53
      // 5a0b: sipush 237
      // 5a0e: iastore
      // 5a0f: aload 0
      // 5a10: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5a13: bipush 75
      // 5a15: aaload
      // 5a16: bipush 3
      // 5a17: sipush 236
      // 5a1a: iastore
      // 5a1b: aload 0
      // 5a1c: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5a1f: bipush 83
      // 5a21: aaload
      // 5a22: bipush 73
      // 5a24: sipush 235
      // 5a27: iastore
      // 5a28: aload 0
      // 5a29: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5a2c: bipush 66
      // 5a2e: aaload
      // 5a2f: bipush 13
      // 5a31: sipush 234
      // 5a34: iastore
      // 5a35: aload 0
      // 5a36: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5a39: bipush 48
      // 5a3b: aaload
      // 5a3c: bipush 7
      // 5a3e: sipush 233
      // 5a41: iastore
      // 5a42: aload 0
      // 5a43: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5a46: bipush 46
      // 5a48: aaload
      // 5a49: bipush 35
      // 5a4b: sipush 232
      // 5a4e: iastore
      // 5a4f: aload 0
      // 5a50: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5a53: bipush 35
      // 5a55: aaload
      // 5a56: bipush 86
      // 5a58: sipush 231
      // 5a5b: iastore
      // 5a5c: aload 0
      // 5a5d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5a60: bipush 37
      // 5a62: aaload
      // 5a63: bipush 20
      // 5a65: sipush 230
      // 5a68: iastore
      // 5a69: aload 0
      // 5a6a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5a6d: bipush 46
      // 5a6f: aaload
      // 5a70: bipush 80
      // 5a72: sipush 229
      // 5a75: iastore
      // 5a76: aload 0
      // 5a77: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5a7a: bipush 38
      // 5a7c: aaload
      // 5a7d: bipush 24
      // 5a7f: sipush 228
      // 5a82: iastore
      // 5a83: aload 0
      // 5a84: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5a87: bipush 41
      // 5a89: aaload
      // 5a8a: bipush 68
      // 5a8c: sipush 227
      // 5a8f: iastore
      // 5a90: aload 0
      // 5a91: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5a94: bipush 42
      // 5a96: aaload
      // 5a97: bipush 21
      // 5a99: sipush 226
      // 5a9c: iastore
      // 5a9d: aload 0
      // 5a9e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5aa1: bipush 43
      // 5aa3: aaload
      // 5aa4: bipush 32
      // 5aa6: sipush 225
      // 5aa9: iastore
      // 5aaa: aload 0
      // 5aab: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5aae: bipush 38
      // 5ab0: aaload
      // 5ab1: bipush 20
      // 5ab3: sipush 224
      // 5ab6: iastore
      // 5ab7: aload 0
      // 5ab8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5abb: bipush 37
      // 5abd: aaload
      // 5abe: bipush 59
      // 5ac0: sipush 223
      // 5ac3: iastore
      // 5ac4: aload 0
      // 5ac5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5ac8: bipush 41
      // 5aca: aaload
      // 5acb: bipush 77
      // 5acd: sipush 222
      // 5ad0: iastore
      // 5ad1: aload 0
      // 5ad2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5ad5: bipush 59
      // 5ad7: aaload
      // 5ad8: bipush 57
      // 5ada: sipush 221
      // 5add: iastore
      // 5ade: aload 0
      // 5adf: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5ae2: bipush 68
      // 5ae4: aaload
      // 5ae5: bipush 59
      // 5ae7: sipush 220
      // 5aea: iastore
      // 5aeb: aload 0
      // 5aec: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5aef: bipush 39
      // 5af1: aaload
      // 5af2: bipush 43
      // 5af4: sipush 219
      // 5af7: iastore
      // 5af8: aload 0
      // 5af9: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5afc: bipush 54
      // 5afe: aaload
      // 5aff: bipush 39
      // 5b01: sipush 218
      // 5b04: iastore
      // 5b05: aload 0
      // 5b06: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5b09: bipush 48
      // 5b0b: aaload
      // 5b0c: bipush 28
      // 5b0e: sipush 217
      // 5b11: iastore
      // 5b12: aload 0
      // 5b13: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5b16: bipush 54
      // 5b18: aaload
      // 5b19: bipush 28
      // 5b1b: sipush 216
      // 5b1e: iastore
      // 5b1f: aload 0
      // 5b20: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5b23: bipush 41
      // 5b25: aaload
      // 5b26: bipush 44
      // 5b28: sipush 215
      // 5b2b: iastore
      // 5b2c: aload 0
      // 5b2d: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5b30: bipush 51
      // 5b32: aaload
      // 5b33: bipush 64
      // 5b35: sipush 214
      // 5b38: iastore
      // 5b39: aload 0
      // 5b3a: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5b3d: bipush 47
      // 5b3f: aaload
      // 5b40: bipush 72
      // 5b42: sipush 213
      // 5b45: iastore
      // 5b46: aload 0
      // 5b47: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5b4a: bipush 62
      // 5b4c: aaload
      // 5b4d: bipush 67
      // 5b4f: sipush 212
      // 5b52: iastore
      // 5b53: aload 0
      // 5b54: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5b57: bipush 42
      // 5b59: aaload
      // 5b5a: bipush 43
      // 5b5c: sipush 211
      // 5b5f: iastore
      // 5b60: aload 0
      // 5b61: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5b64: bipush 61
      // 5b66: aaload
      // 5b67: bipush 38
      // 5b69: sipush 210
      // 5b6c: iastore
      // 5b6d: aload 0
      // 5b6e: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5b71: bipush 76
      // 5b73: aaload
      // 5b74: bipush 25
      // 5b76: sipush 209
      // 5b79: iastore
      // 5b7a: aload 0
      // 5b7b: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5b7e: bipush 48
      // 5b80: aaload
      // 5b81: bipush 91
      // 5b83: sipush 208
      // 5b86: iastore
      // 5b87: aload 0
      // 5b88: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5b8b: bipush 36
      // 5b8d: aaload
      // 5b8e: bipush 36
      // 5b90: sipush 207
      // 5b93: iastore
      // 5b94: aload 0
      // 5b95: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5b98: bipush 80
      // 5b9a: aaload
      // 5b9b: bipush 32
      // 5b9d: sipush 206
      // 5ba0: iastore
      // 5ba1: aload 0
      // 5ba2: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5ba5: bipush 81
      // 5ba7: aaload
      // 5ba8: bipush 40
      // 5baa: sipush 205
      // 5bad: iastore
      // 5bae: aload 0
      // 5baf: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5bb2: bipush 37
      // 5bb4: aaload
      // 5bb5: bipush 5
      // 5bb6: sipush 204
      // 5bb9: iastore
      // 5bba: aload 0
      // 5bbb: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5bbe: bipush 74
      // 5bc0: aaload
      // 5bc1: bipush 69
      // 5bc3: sipush 203
      // 5bc6: iastore
      // 5bc7: aload 0
      // 5bc8: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5bcb: bipush 36
      // 5bcd: aaload
      // 5bce: bipush 82
      // 5bd0: sipush 202
      // 5bd3: iastore
      // 5bd4: aload 0
      // 5bd5: getfield io/legado/app/help/BytesEncodingDetect.EUC_TWFreq [[I
      // 5bd8: bipush 46
      // 5bda: aaload
      // 5bdb: bipush 59
      // 5bdd: sipush 201
      // 5be0: iastore
      // 5be1: aload 0
      // 5be2: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5be5: bipush 52
      // 5be7: aaload
      // 5be8: sipush 132
      // 5beb: sipush 600
      // 5bee: iastore
      // 5bef: aload 0
      // 5bf0: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5bf3: bipush 73
      // 5bf5: aaload
      // 5bf6: sipush 135
      // 5bf9: sipush 599
      // 5bfc: iastore
      // 5bfd: aload 0
      // 5bfe: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5c01: bipush 49
      // 5c03: aaload
      // 5c04: bipush 123
      // 5c06: sipush 598
      // 5c09: iastore
      // 5c0a: aload 0
      // 5c0b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5c0e: bipush 77
      // 5c10: aaload
      // 5c11: sipush 146
      // 5c14: sipush 597
      // 5c17: iastore
      // 5c18: aload 0
      // 5c19: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5c1c: bipush 81
      // 5c1e: aaload
      // 5c1f: bipush 123
      // 5c21: sipush 596
      // 5c24: iastore
      // 5c25: aload 0
      // 5c26: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5c29: bipush 82
      // 5c2b: aaload
      // 5c2c: sipush 144
      // 5c2f: sipush 595
      // 5c32: iastore
      // 5c33: aload 0
      // 5c34: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5c37: bipush 51
      // 5c39: aaload
      // 5c3a: sipush 179
      // 5c3d: sipush 594
      // 5c40: iastore
      // 5c41: aload 0
      // 5c42: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5c45: bipush 83
      // 5c47: aaload
      // 5c48: sipush 154
      // 5c4b: sipush 593
      // 5c4e: iastore
      // 5c4f: aload 0
      // 5c50: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5c53: bipush 71
      // 5c55: aaload
      // 5c56: sipush 139
      // 5c59: sipush 592
      // 5c5c: iastore
      // 5c5d: aload 0
      // 5c5e: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5c61: bipush 64
      // 5c63: aaload
      // 5c64: sipush 139
      // 5c67: sipush 591
      // 5c6a: iastore
      // 5c6b: aload 0
      // 5c6c: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5c6f: bipush 85
      // 5c71: aaload
      // 5c72: sipush 144
      // 5c75: sipush 590
      // 5c78: iastore
      // 5c79: aload 0
      // 5c7a: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5c7d: bipush 52
      // 5c7f: aaload
      // 5c80: bipush 125
      // 5c82: sipush 589
      // 5c85: iastore
      // 5c86: aload 0
      // 5c87: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5c8a: bipush 88
      // 5c8c: aaload
      // 5c8d: bipush 25
      // 5c8f: sipush 588
      // 5c92: iastore
      // 5c93: aload 0
      // 5c94: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5c97: bipush 81
      // 5c99: aaload
      // 5c9a: bipush 106
      // 5c9c: sipush 587
      // 5c9f: iastore
      // 5ca0: aload 0
      // 5ca1: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5ca4: bipush 81
      // 5ca6: aaload
      // 5ca7: sipush 148
      // 5caa: sipush 586
      // 5cad: iastore
      // 5cae: aload 0
      // 5caf: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5cb2: bipush 62
      // 5cb4: aaload
      // 5cb5: sipush 137
      // 5cb8: sipush 585
      // 5cbb: iastore
      // 5cbc: aload 0
      // 5cbd: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5cc0: bipush 94
      // 5cc2: aaload
      // 5cc3: bipush 0
      // 5cc4: sipush 584
      // 5cc7: iastore
      // 5cc8: aload 0
      // 5cc9: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5ccc: bipush 1
      // 5ccd: aaload
      // 5cce: bipush 64
      // 5cd0: sipush 583
      // 5cd3: iastore
      // 5cd4: aload 0
      // 5cd5: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5cd8: bipush 67
      // 5cda: aaload
      // 5cdb: sipush 163
      // 5cde: sipush 582
      // 5ce1: iastore
      // 5ce2: aload 0
      // 5ce3: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5ce6: bipush 20
      // 5ce8: aaload
      // 5ce9: sipush 190
      // 5cec: sipush 581
      // 5cef: iastore
      // 5cf0: aload 0
      // 5cf1: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5cf4: bipush 57
      // 5cf6: aaload
      // 5cf7: sipush 131
      // 5cfa: sipush 580
      // 5cfd: iastore
      // 5cfe: aload 0
      // 5cff: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5d02: bipush 29
      // 5d04: aaload
      // 5d05: sipush 169
      // 5d08: sipush 579
      // 5d0b: iastore
      // 5d0c: aload 0
      // 5d0d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5d10: bipush 72
      // 5d12: aaload
      // 5d13: sipush 143
      // 5d16: sipush 578
      // 5d19: iastore
      // 5d1a: aload 0
      // 5d1b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5d1e: bipush 0
      // 5d1f: aaload
      // 5d20: sipush 173
      // 5d23: sipush 577
      // 5d26: iastore
      // 5d27: aload 0
      // 5d28: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5d2b: bipush 11
      // 5d2d: aaload
      // 5d2e: bipush 23
      // 5d30: sipush 576
      // 5d33: iastore
      // 5d34: aload 0
      // 5d35: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5d38: bipush 61
      // 5d3a: aaload
      // 5d3b: sipush 141
      // 5d3e: sipush 575
      // 5d41: iastore
      // 5d42: aload 0
      // 5d43: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5d46: bipush 60
      // 5d48: aaload
      // 5d49: bipush 123
      // 5d4b: sipush 574
      // 5d4e: iastore
      // 5d4f: aload 0
      // 5d50: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5d53: bipush 81
      // 5d55: aaload
      // 5d56: bipush 114
      // 5d58: sipush 573
      // 5d5b: iastore
      // 5d5c: aload 0
      // 5d5d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5d60: bipush 82
      // 5d62: aaload
      // 5d63: sipush 131
      // 5d66: sipush 572
      // 5d69: iastore
      // 5d6a: aload 0
      // 5d6b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5d6e: bipush 67
      // 5d70: aaload
      // 5d71: sipush 156
      // 5d74: sipush 571
      // 5d77: iastore
      // 5d78: aload 0
      // 5d79: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5d7c: bipush 71
      // 5d7e: aaload
      // 5d7f: sipush 167
      // 5d82: sipush 570
      // 5d85: iastore
      // 5d86: aload 0
      // 5d87: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5d8a: bipush 20
      // 5d8c: aaload
      // 5d8d: bipush 50
      // 5d8f: sipush 569
      // 5d92: iastore
      // 5d93: aload 0
      // 5d94: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5d97: bipush 77
      // 5d99: aaload
      // 5d9a: sipush 132
      // 5d9d: sipush 568
      // 5da0: iastore
      // 5da1: aload 0
      // 5da2: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5da5: bipush 84
      // 5da7: aaload
      // 5da8: bipush 38
      // 5daa: sipush 567
      // 5dad: iastore
      // 5dae: aload 0
      // 5daf: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5db2: bipush 26
      // 5db4: aaload
      // 5db5: bipush 29
      // 5db7: sipush 566
      // 5dba: iastore
      // 5dbb: aload 0
      // 5dbc: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5dbf: bipush 74
      // 5dc1: aaload
      // 5dc2: sipush 187
      // 5dc5: sipush 565
      // 5dc8: iastore
      // 5dc9: aload 0
      // 5dca: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5dcd: bipush 62
      // 5dcf: aaload
      // 5dd0: bipush 116
      // 5dd2: sipush 564
      // 5dd5: iastore
      // 5dd6: aload 0
      // 5dd7: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5dda: bipush 67
      // 5ddc: aaload
      // 5ddd: sipush 135
      // 5de0: sipush 563
      // 5de3: iastore
      // 5de4: aload 0
      // 5de5: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5de8: bipush 5
      // 5de9: aaload
      // 5dea: bipush 86
      // 5dec: sipush 562
      // 5def: iastore
      // 5df0: aload 0
      // 5df1: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5df4: bipush 72
      // 5df6: aaload
      // 5df7: sipush 186
      // 5dfa: sipush 561
      // 5dfd: iastore
      // 5dfe: aload 0
      // 5dff: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5e02: bipush 75
      // 5e04: aaload
      // 5e05: sipush 161
      // 5e08: sipush 560
      // 5e0b: iastore
      // 5e0c: aload 0
      // 5e0d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5e10: bipush 78
      // 5e12: aaload
      // 5e13: sipush 130
      // 5e16: sipush 559
      // 5e19: iastore
      // 5e1a: aload 0
      // 5e1b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5e1e: bipush 94
      // 5e20: aaload
      // 5e21: bipush 30
      // 5e23: sipush 558
      // 5e26: iastore
      // 5e27: aload 0
      // 5e28: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5e2b: bipush 84
      // 5e2d: aaload
      // 5e2e: bipush 72
      // 5e30: sipush 557
      // 5e33: iastore
      // 5e34: aload 0
      // 5e35: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5e38: bipush 1
      // 5e39: aaload
      // 5e3a: bipush 67
      // 5e3c: sipush 556
      // 5e3f: iastore
      // 5e40: aload 0
      // 5e41: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5e44: bipush 75
      // 5e46: aaload
      // 5e47: sipush 172
      // 5e4a: sipush 555
      // 5e4d: iastore
      // 5e4e: aload 0
      // 5e4f: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5e52: bipush 74
      // 5e54: aaload
      // 5e55: sipush 185
      // 5e58: sipush 554
      // 5e5b: iastore
      // 5e5c: aload 0
      // 5e5d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5e60: bipush 53
      // 5e62: aaload
      // 5e63: sipush 160
      // 5e66: sipush 553
      // 5e69: iastore
      // 5e6a: aload 0
      // 5e6b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5e6e: bipush 123
      // 5e70: aaload
      // 5e71: bipush 14
      // 5e73: sipush 552
      // 5e76: iastore
      // 5e77: aload 0
      // 5e78: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5e7b: bipush 79
      // 5e7d: aaload
      // 5e7e: bipush 97
      // 5e80: sipush 551
      // 5e83: iastore
      // 5e84: aload 0
      // 5e85: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5e88: bipush 85
      // 5e8a: aaload
      // 5e8b: bipush 110
      // 5e8d: sipush 550
      // 5e90: iastore
      // 5e91: aload 0
      // 5e92: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5e95: bipush 78
      // 5e97: aaload
      // 5e98: sipush 171
      // 5e9b: sipush 549
      // 5e9e: iastore
      // 5e9f: aload 0
      // 5ea0: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5ea3: bipush 52
      // 5ea5: aaload
      // 5ea6: sipush 131
      // 5ea9: sipush 548
      // 5eac: iastore
      // 5ead: aload 0
      // 5eae: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5eb1: bipush 56
      // 5eb3: aaload
      // 5eb4: bipush 100
      // 5eb6: sipush 547
      // 5eb9: iastore
      // 5eba: aload 0
      // 5ebb: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5ebe: bipush 50
      // 5ec0: aaload
      // 5ec1: sipush 182
      // 5ec4: sipush 546
      // 5ec7: iastore
      // 5ec8: aload 0
      // 5ec9: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5ecc: bipush 94
      // 5ece: aaload
      // 5ecf: bipush 64
      // 5ed1: sipush 545
      // 5ed4: iastore
      // 5ed5: aload 0
      // 5ed6: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5ed9: bipush 106
      // 5edb: aaload
      // 5edc: bipush 74
      // 5ede: sipush 544
      // 5ee1: iastore
      // 5ee2: aload 0
      // 5ee3: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5ee6: bipush 11
      // 5ee8: aaload
      // 5ee9: bipush 102
      // 5eeb: sipush 543
      // 5eee: iastore
      // 5eef: aload 0
      // 5ef0: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5ef3: bipush 53
      // 5ef5: aaload
      // 5ef6: bipush 124
      // 5ef8: sipush 542
      // 5efb: iastore
      // 5efc: aload 0
      // 5efd: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5f00: bipush 24
      // 5f02: aaload
      // 5f03: bipush 3
      // 5f04: sipush 541
      // 5f07: iastore
      // 5f08: aload 0
      // 5f09: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5f0c: bipush 86
      // 5f0e: aaload
      // 5f0f: sipush 148
      // 5f12: sipush 540
      // 5f15: iastore
      // 5f16: aload 0
      // 5f17: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5f1a: bipush 53
      // 5f1c: aaload
      // 5f1d: sipush 184
      // 5f20: sipush 539
      // 5f23: iastore
      // 5f24: aload 0
      // 5f25: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5f28: bipush 86
      // 5f2a: aaload
      // 5f2b: sipush 147
      // 5f2e: sipush 538
      // 5f31: iastore
      // 5f32: aload 0
      // 5f33: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5f36: bipush 96
      // 5f38: aaload
      // 5f39: sipush 161
      // 5f3c: sipush 537
      // 5f3f: iastore
      // 5f40: aload 0
      // 5f41: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5f44: bipush 82
      // 5f46: aaload
      // 5f47: bipush 77
      // 5f49: sipush 536
      // 5f4c: iastore
      // 5f4d: aload 0
      // 5f4e: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5f51: bipush 59
      // 5f53: aaload
      // 5f54: sipush 146
      // 5f57: sipush 535
      // 5f5a: iastore
      // 5f5b: aload 0
      // 5f5c: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5f5f: bipush 84
      // 5f61: aaload
      // 5f62: bipush 126
      // 5f64: sipush 534
      // 5f67: iastore
      // 5f68: aload 0
      // 5f69: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5f6c: bipush 79
      // 5f6e: aaload
      // 5f6f: sipush 132
      // 5f72: sipush 533
      // 5f75: iastore
      // 5f76: aload 0
      // 5f77: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5f7a: bipush 85
      // 5f7c: aaload
      // 5f7d: bipush 123
      // 5f7f: sipush 532
      // 5f82: iastore
      // 5f83: aload 0
      // 5f84: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5f87: bipush 71
      // 5f89: aaload
      // 5f8a: bipush 101
      // 5f8c: sipush 531
      // 5f8f: iastore
      // 5f90: aload 0
      // 5f91: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5f94: bipush 85
      // 5f96: aaload
      // 5f97: bipush 106
      // 5f99: sipush 530
      // 5f9c: iastore
      // 5f9d: aload 0
      // 5f9e: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5fa1: bipush 6
      // 5fa3: aaload
      // 5fa4: sipush 184
      // 5fa7: sipush 529
      // 5faa: iastore
      // 5fab: aload 0
      // 5fac: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5faf: bipush 57
      // 5fb1: aaload
      // 5fb2: sipush 156
      // 5fb5: sipush 528
      // 5fb8: iastore
      // 5fb9: aload 0
      // 5fba: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5fbd: bipush 75
      // 5fbf: aaload
      // 5fc0: bipush 104
      // 5fc2: sipush 527
      // 5fc5: iastore
      // 5fc6: aload 0
      // 5fc7: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5fca: bipush 50
      // 5fcc: aaload
      // 5fcd: sipush 137
      // 5fd0: sipush 526
      // 5fd3: iastore
      // 5fd4: aload 0
      // 5fd5: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5fd8: bipush 79
      // 5fda: aaload
      // 5fdb: sipush 133
      // 5fde: sipush 525
      // 5fe1: iastore
      // 5fe2: aload 0
      // 5fe3: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5fe6: bipush 76
      // 5fe8: aaload
      // 5fe9: bipush 108
      // 5feb: sipush 524
      // 5fee: iastore
      // 5fef: aload 0
      // 5ff0: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 5ff3: bipush 57
      // 5ff5: aaload
      // 5ff6: sipush 142
      // 5ff9: sipush 523
      // 5ffc: iastore
      // 5ffd: aload 0
      // 5ffe: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6001: bipush 84
      // 6003: aaload
      // 6004: sipush 130
      // 6007: sipush 522
      // 600a: iastore
      // 600b: aload 0
      // 600c: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 600f: bipush 52
      // 6011: aaload
      // 6012: sipush 128
      // 6015: sipush 521
      // 6018: iastore
      // 6019: aload 0
      // 601a: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 601d: bipush 47
      // 601f: aaload
      // 6020: bipush 44
      // 6022: sipush 520
      // 6025: iastore
      // 6026: aload 0
      // 6027: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 602a: bipush 52
      // 602c: aaload
      // 602d: sipush 152
      // 6030: sipush 519
      // 6033: iastore
      // 6034: aload 0
      // 6035: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6038: bipush 54
      // 603a: aaload
      // 603b: bipush 104
      // 603d: sipush 518
      // 6040: iastore
      // 6041: aload 0
      // 6042: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6045: bipush 30
      // 6047: aaload
      // 6048: bipush 47
      // 604a: sipush 517
      // 604d: iastore
      // 604e: aload 0
      // 604f: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6052: bipush 71
      // 6054: aaload
      // 6055: bipush 123
      // 6057: sipush 516
      // 605a: iastore
      // 605b: aload 0
      // 605c: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 605f: bipush 52
      // 6061: aaload
      // 6062: bipush 107
      // 6064: sipush 515
      // 6067: iastore
      // 6068: aload 0
      // 6069: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 606c: bipush 45
      // 606e: aaload
      // 606f: bipush 84
      // 6071: sipush 514
      // 6074: iastore
      // 6075: aload 0
      // 6076: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6079: bipush 107
      // 607b: aaload
      // 607c: bipush 118
      // 607e: sipush 513
      // 6081: iastore
      // 6082: aload 0
      // 6083: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6086: bipush 5
      // 6087: aaload
      // 6088: sipush 161
      // 608b: sipush 512
      // 608e: iastore
      // 608f: aload 0
      // 6090: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6093: bipush 48
      // 6095: aaload
      // 6096: bipush 126
      // 6098: sipush 511
      // 609b: iastore
      // 609c: aload 0
      // 609d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 60a0: bipush 67
      // 60a2: aaload
      // 60a3: sipush 170
      // 60a6: sipush 510
      // 60a9: iastore
      // 60aa: aload 0
      // 60ab: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 60ae: bipush 43
      // 60b0: aaload
      // 60b1: bipush 6
      // 60b3: sipush 509
      // 60b6: iastore
      // 60b7: aload 0
      // 60b8: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 60bb: bipush 70
      // 60bd: aaload
      // 60be: bipush 112
      // 60c0: sipush 508
      // 60c3: iastore
      // 60c4: aload 0
      // 60c5: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 60c8: bipush 86
      // 60ca: aaload
      // 60cb: sipush 174
      // 60ce: sipush 507
      // 60d1: iastore
      // 60d2: aload 0
      // 60d3: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 60d6: bipush 84
      // 60d8: aaload
      // 60d9: sipush 166
      // 60dc: sipush 506
      // 60df: iastore
      // 60e0: aload 0
      // 60e1: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 60e4: bipush 79
      // 60e6: aaload
      // 60e7: sipush 130
      // 60ea: sipush 505
      // 60ed: iastore
      // 60ee: aload 0
      // 60ef: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 60f2: bipush 57
      // 60f4: aaload
      // 60f5: sipush 141
      // 60f8: sipush 504
      // 60fb: iastore
      // 60fc: aload 0
      // 60fd: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6100: bipush 81
      // 6102: aaload
      // 6103: sipush 178
      // 6106: sipush 503
      // 6109: iastore
      // 610a: aload 0
      // 610b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 610e: bipush 56
      // 6110: aaload
      // 6111: sipush 187
      // 6114: sipush 502
      // 6117: iastore
      // 6118: aload 0
      // 6119: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 611c: bipush 81
      // 611e: aaload
      // 611f: sipush 162
      // 6122: sipush 501
      // 6125: iastore
      // 6126: aload 0
      // 6127: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 612a: bipush 53
      // 612c: aaload
      // 612d: bipush 104
      // 612f: sipush 500
      // 6132: iastore
      // 6133: aload 0
      // 6134: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6137: bipush 123
      // 6139: aaload
      // 613a: bipush 35
      // 613c: sipush 499
      // 613f: iastore
      // 6140: aload 0
      // 6141: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6144: bipush 70
      // 6146: aaload
      // 6147: sipush 169
      // 614a: sipush 498
      // 614d: iastore
      // 614e: aload 0
      // 614f: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6152: bipush 69
      // 6154: aaload
      // 6155: sipush 164
      // 6158: sipush 497
      // 615b: iastore
      // 615c: aload 0
      // 615d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6160: bipush 109
      // 6162: aaload
      // 6163: bipush 61
      // 6165: sipush 496
      // 6168: iastore
      // 6169: aload 0
      // 616a: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 616d: bipush 73
      // 616f: aaload
      // 6170: sipush 130
      // 6173: sipush 495
      // 6176: iastore
      // 6177: aload 0
      // 6178: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 617b: bipush 62
      // 617d: aaload
      // 617e: sipush 134
      // 6181: sipush 494
      // 6184: iastore
      // 6185: aload 0
      // 6186: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6189: bipush 54
      // 618b: aaload
      // 618c: bipush 125
      // 618e: sipush 493
      // 6191: iastore
      // 6192: aload 0
      // 6193: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6196: bipush 79
      // 6198: aaload
      // 6199: bipush 105
      // 619b: sipush 492
      // 619e: iastore
      // 619f: aload 0
      // 61a0: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 61a3: bipush 70
      // 61a5: aaload
      // 61a6: sipush 165
      // 61a9: sipush 491
      // 61ac: iastore
      // 61ad: aload 0
      // 61ae: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 61b1: bipush 71
      // 61b3: aaload
      // 61b4: sipush 189
      // 61b7: sipush 490
      // 61ba: iastore
      // 61bb: aload 0
      // 61bc: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 61bf: bipush 23
      // 61c1: aaload
      // 61c2: sipush 147
      // 61c5: sipush 489
      // 61c8: iastore
      // 61c9: aload 0
      // 61ca: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 61cd: bipush 51
      // 61cf: aaload
      // 61d0: sipush 139
      // 61d3: sipush 488
      // 61d6: iastore
      // 61d7: aload 0
      // 61d8: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 61db: bipush 47
      // 61dd: aaload
      // 61de: sipush 137
      // 61e1: sipush 487
      // 61e4: iastore
      // 61e5: aload 0
      // 61e6: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 61e9: bipush 77
      // 61eb: aaload
      // 61ec: bipush 123
      // 61ee: sipush 486
      // 61f1: iastore
      // 61f2: aload 0
      // 61f3: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 61f6: bipush 86
      // 61f8: aaload
      // 61f9: sipush 183
      // 61fc: sipush 485
      // 61ff: iastore
      // 6200: aload 0
      // 6201: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6204: bipush 63
      // 6206: aaload
      // 6207: sipush 173
      // 620a: sipush 484
      // 620d: iastore
      // 620e: aload 0
      // 620f: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6212: bipush 79
      // 6214: aaload
      // 6215: sipush 144
      // 6218: sipush 483
      // 621b: iastore
      // 621c: aload 0
      // 621d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6220: bipush 84
      // 6222: aaload
      // 6223: sipush 159
      // 6226: sipush 482
      // 6229: iastore
      // 622a: aload 0
      // 622b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 622e: bipush 60
      // 6230: aaload
      // 6231: bipush 91
      // 6233: sipush 481
      // 6236: iastore
      // 6237: aload 0
      // 6238: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 623b: bipush 66
      // 623d: aaload
      // 623e: sipush 187
      // 6241: sipush 480
      // 6244: iastore
      // 6245: aload 0
      // 6246: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6249: bipush 73
      // 624b: aaload
      // 624c: bipush 114
      // 624e: sipush 479
      // 6251: iastore
      // 6252: aload 0
      // 6253: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6256: bipush 85
      // 6258: aaload
      // 6259: bipush 56
      // 625b: sipush 478
      // 625e: iastore
      // 625f: aload 0
      // 6260: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6263: bipush 71
      // 6265: aaload
      // 6266: sipush 149
      // 6269: sipush 477
      // 626c: iastore
      // 626d: aload 0
      // 626e: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6271: bipush 84
      // 6273: aaload
      // 6274: sipush 189
      // 6277: sipush 476
      // 627a: iastore
      // 627b: aload 0
      // 627c: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 627f: bipush 104
      // 6281: aaload
      // 6282: bipush 31
      // 6284: sipush 475
      // 6287: iastore
      // 6288: aload 0
      // 6289: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 628c: bipush 83
      // 628e: aaload
      // 628f: bipush 82
      // 6291: sipush 474
      // 6294: iastore
      // 6295: aload 0
      // 6296: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6299: bipush 68
      // 629b: aaload
      // 629c: bipush 35
      // 629e: sipush 473
      // 62a1: iastore
      // 62a2: aload 0
      // 62a3: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 62a6: bipush 11
      // 62a8: aaload
      // 62a9: bipush 77
      // 62ab: sipush 472
      // 62ae: iastore
      // 62af: aload 0
      // 62b0: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 62b3: bipush 15
      // 62b5: aaload
      // 62b6: sipush 155
      // 62b9: sipush 471
      // 62bc: iastore
      // 62bd: aload 0
      // 62be: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 62c1: bipush 83
      // 62c3: aaload
      // 62c4: sipush 153
      // 62c7: sipush 470
      // 62ca: iastore
      // 62cb: aload 0
      // 62cc: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 62cf: bipush 71
      // 62d1: aaload
      // 62d2: bipush 1
      // 62d3: sipush 469
      // 62d6: iastore
      // 62d7: aload 0
      // 62d8: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 62db: bipush 53
      // 62dd: aaload
      // 62de: sipush 190
      // 62e1: sipush 468
      // 62e4: iastore
      // 62e5: aload 0
      // 62e6: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 62e9: bipush 50
      // 62eb: aaload
      // 62ec: sipush 135
      // 62ef: sipush 467
      // 62f2: iastore
      // 62f3: aload 0
      // 62f4: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 62f7: bipush 3
      // 62f8: aaload
      // 62f9: sipush 147
      // 62fc: sipush 466
      // 62ff: iastore
      // 6300: aload 0
      // 6301: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6304: bipush 48
      // 6306: aaload
      // 6307: sipush 136
      // 630a: sipush 465
      // 630d: iastore
      // 630e: aload 0
      // 630f: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6312: bipush 66
      // 6314: aaload
      // 6315: sipush 166
      // 6318: sipush 464
      // 631b: iastore
      // 631c: aload 0
      // 631d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6320: bipush 55
      // 6322: aaload
      // 6323: sipush 159
      // 6326: sipush 463
      // 6329: iastore
      // 632a: aload 0
      // 632b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 632e: bipush 82
      // 6330: aaload
      // 6331: sipush 150
      // 6334: sipush 462
      // 6337: iastore
      // 6338: aload 0
      // 6339: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 633c: bipush 58
      // 633e: aaload
      // 633f: sipush 178
      // 6342: sipush 461
      // 6345: iastore
      // 6346: aload 0
      // 6347: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 634a: bipush 64
      // 634c: aaload
      // 634d: bipush 102
      // 634f: sipush 460
      // 6352: iastore
      // 6353: aload 0
      // 6354: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6357: bipush 16
      // 6359: aaload
      // 635a: bipush 106
      // 635c: sipush 459
      // 635f: iastore
      // 6360: aload 0
      // 6361: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6364: bipush 68
      // 6366: aaload
      // 6367: bipush 110
      // 6369: sipush 458
      // 636c: iastore
      // 636d: aload 0
      // 636e: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6371: bipush 54
      // 6373: aaload
      // 6374: bipush 14
      // 6376: sipush 457
      // 6379: iastore
      // 637a: aload 0
      // 637b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 637e: bipush 60
      // 6380: aaload
      // 6381: sipush 140
      // 6384: sipush 456
      // 6387: iastore
      // 6388: aload 0
      // 6389: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 638c: bipush 91
      // 638e: aaload
      // 638f: bipush 71
      // 6391: sipush 455
      // 6394: iastore
      // 6395: aload 0
      // 6396: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6399: bipush 54
      // 639b: aaload
      // 639c: sipush 150
      // 639f: sipush 454
      // 63a2: iastore
      // 63a3: aload 0
      // 63a4: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 63a7: bipush 78
      // 63a9: aaload
      // 63aa: sipush 177
      // 63ad: sipush 453
      // 63b0: iastore
      // 63b1: aload 0
      // 63b2: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 63b5: bipush 78
      // 63b7: aaload
      // 63b8: bipush 117
      // 63ba: sipush 452
      // 63bd: iastore
      // 63be: aload 0
      // 63bf: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 63c2: bipush 104
      // 63c4: aaload
      // 63c5: bipush 12
      // 63c7: sipush 451
      // 63ca: iastore
      // 63cb: aload 0
      // 63cc: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 63cf: bipush 73
      // 63d1: aaload
      // 63d2: sipush 150
      // 63d5: sipush 450
      // 63d8: iastore
      // 63d9: aload 0
      // 63da: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 63dd: bipush 51
      // 63df: aaload
      // 63e0: sipush 142
      // 63e3: sipush 449
      // 63e6: iastore
      // 63e7: aload 0
      // 63e8: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 63eb: bipush 81
      // 63ed: aaload
      // 63ee: sipush 145
      // 63f1: sipush 448
      // 63f4: iastore
      // 63f5: aload 0
      // 63f6: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 63f9: bipush 66
      // 63fb: aaload
      // 63fc: sipush 183
      // 63ff: sipush 447
      // 6402: iastore
      // 6403: aload 0
      // 6404: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6407: bipush 51
      // 6409: aaload
      // 640a: sipush 178
      // 640d: sipush 446
      // 6410: iastore
      // 6411: aload 0
      // 6412: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6415: bipush 75
      // 6417: aaload
      // 6418: bipush 107
      // 641a: sipush 445
      // 641d: iastore
      // 641e: aload 0
      // 641f: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6422: bipush 65
      // 6424: aaload
      // 6425: bipush 119
      // 6427: sipush 444
      // 642a: iastore
      // 642b: aload 0
      // 642c: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 642f: bipush 69
      // 6431: aaload
      // 6432: sipush 176
      // 6435: sipush 443
      // 6438: iastore
      // 6439: aload 0
      // 643a: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 643d: bipush 59
      // 643f: aaload
      // 6440: bipush 122
      // 6442: sipush 442
      // 6445: iastore
      // 6446: aload 0
      // 6447: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 644a: bipush 78
      // 644c: aaload
      // 644d: sipush 160
      // 6450: sipush 441
      // 6453: iastore
      // 6454: aload 0
      // 6455: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6458: bipush 85
      // 645a: aaload
      // 645b: sipush 183
      // 645e: sipush 440
      // 6461: iastore
      // 6462: aload 0
      // 6463: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6466: bipush 105
      // 6468: aaload
      // 6469: bipush 16
      // 646b: sipush 439
      // 646e: iastore
      // 646f: aload 0
      // 6470: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6473: bipush 73
      // 6475: aaload
      // 6476: bipush 110
      // 6478: sipush 438
      // 647b: iastore
      // 647c: aload 0
      // 647d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6480: bipush 104
      // 6482: aaload
      // 6483: bipush 39
      // 6485: sipush 437
      // 6488: iastore
      // 6489: aload 0
      // 648a: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 648d: bipush 119
      // 648f: aaload
      // 6490: bipush 16
      // 6492: sipush 436
      // 6495: iastore
      // 6496: aload 0
      // 6497: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 649a: bipush 76
      // 649c: aaload
      // 649d: sipush 162
      // 64a0: sipush 435
      // 64a3: iastore
      // 64a4: aload 0
      // 64a5: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 64a8: bipush 67
      // 64aa: aaload
      // 64ab: sipush 152
      // 64ae: sipush 434
      // 64b1: iastore
      // 64b2: aload 0
      // 64b3: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 64b6: bipush 82
      // 64b8: aaload
      // 64b9: bipush 24
      // 64bb: sipush 433
      // 64be: iastore
      // 64bf: aload 0
      // 64c0: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 64c3: bipush 73
      // 64c5: aaload
      // 64c6: bipush 121
      // 64c8: sipush 432
      // 64cb: iastore
      // 64cc: aload 0
      // 64cd: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 64d0: bipush 83
      // 64d2: aaload
      // 64d3: bipush 83
      // 64d5: sipush 431
      // 64d8: iastore
      // 64d9: aload 0
      // 64da: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 64dd: bipush 82
      // 64df: aaload
      // 64e0: sipush 145
      // 64e3: sipush 430
      // 64e6: iastore
      // 64e7: aload 0
      // 64e8: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 64eb: bipush 49
      // 64ed: aaload
      // 64ee: sipush 133
      // 64f1: sipush 429
      // 64f4: iastore
      // 64f5: aload 0
      // 64f6: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 64f9: bipush 94
      // 64fb: aaload
      // 64fc: bipush 13
      // 64fe: sipush 428
      // 6501: iastore
      // 6502: aload 0
      // 6503: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6506: bipush 58
      // 6508: aaload
      // 6509: sipush 139
      // 650c: sipush 427
      // 650f: iastore
      // 6510: aload 0
      // 6511: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6514: bipush 74
      // 6516: aaload
      // 6517: sipush 189
      // 651a: sipush 426
      // 651d: iastore
      // 651e: aload 0
      // 651f: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6522: bipush 66
      // 6524: aaload
      // 6525: sipush 177
      // 6528: sipush 425
      // 652b: iastore
      // 652c: aload 0
      // 652d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6530: bipush 85
      // 6532: aaload
      // 6533: sipush 184
      // 6536: sipush 424
      // 6539: iastore
      // 653a: aload 0
      // 653b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 653e: bipush 55
      // 6540: aaload
      // 6541: sipush 183
      // 6544: sipush 423
      // 6547: iastore
      // 6548: aload 0
      // 6549: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 654c: bipush 71
      // 654e: aaload
      // 654f: bipush 107
      // 6551: sipush 422
      // 6554: iastore
      // 6555: aload 0
      // 6556: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6559: bipush 11
      // 655b: aaload
      // 655c: bipush 98
      // 655e: sipush 421
      // 6561: iastore
      // 6562: aload 0
      // 6563: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6566: bipush 72
      // 6568: aaload
      // 6569: sipush 153
      // 656c: sipush 420
      // 656f: iastore
      // 6570: aload 0
      // 6571: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6574: bipush 2
      // 6575: aaload
      // 6576: sipush 137
      // 6579: sipush 419
      // 657c: iastore
      // 657d: aload 0
      // 657e: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6581: bipush 59
      // 6583: aaload
      // 6584: sipush 147
      // 6587: sipush 418
      // 658a: iastore
      // 658b: aload 0
      // 658c: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 658f: bipush 58
      // 6591: aaload
      // 6592: sipush 152
      // 6595: sipush 417
      // 6598: iastore
      // 6599: aload 0
      // 659a: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 659d: bipush 55
      // 659f: aaload
      // 65a0: sipush 144
      // 65a3: sipush 416
      // 65a6: iastore
      // 65a7: aload 0
      // 65a8: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 65ab: bipush 73
      // 65ad: aaload
      // 65ae: bipush 125
      // 65b0: sipush 415
      // 65b3: iastore
      // 65b4: aload 0
      // 65b5: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 65b8: bipush 52
      // 65ba: aaload
      // 65bb: sipush 154
      // 65be: sipush 414
      // 65c1: iastore
      // 65c2: aload 0
      // 65c3: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 65c6: bipush 70
      // 65c8: aaload
      // 65c9: sipush 178
      // 65cc: sipush 413
      // 65cf: iastore
      // 65d0: aload 0
      // 65d1: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 65d4: bipush 79
      // 65d6: aaload
      // 65d7: sipush 148
      // 65da: sipush 412
      // 65dd: iastore
      // 65de: aload 0
      // 65df: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 65e2: bipush 63
      // 65e4: aaload
      // 65e5: sipush 143
      // 65e8: sipush 411
      // 65eb: iastore
      // 65ec: aload 0
      // 65ed: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 65f0: bipush 50
      // 65f2: aaload
      // 65f3: sipush 140
      // 65f6: sipush 410
      // 65f9: iastore
      // 65fa: aload 0
      // 65fb: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 65fe: bipush 47
      // 6600: aaload
      // 6601: sipush 145
      // 6604: sipush 409
      // 6607: iastore
      // 6608: aload 0
      // 6609: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 660c: bipush 48
      // 660e: aaload
      // 660f: bipush 123
      // 6611: sipush 408
      // 6614: iastore
      // 6615: aload 0
      // 6616: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6619: bipush 56
      // 661b: aaload
      // 661c: bipush 107
      // 661e: sipush 407
      // 6621: iastore
      // 6622: aload 0
      // 6623: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6626: bipush 84
      // 6628: aaload
      // 6629: bipush 83
      // 662b: sipush 406
      // 662e: iastore
      // 662f: aload 0
      // 6630: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6633: bipush 59
      // 6635: aaload
      // 6636: bipush 112
      // 6638: sipush 405
      // 663b: iastore
      // 663c: aload 0
      // 663d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6640: bipush 124
      // 6642: aaload
      // 6643: bipush 72
      // 6645: sipush 404
      // 6648: iastore
      // 6649: aload 0
      // 664a: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 664d: bipush 79
      // 664f: aaload
      // 6650: bipush 99
      // 6652: sipush 403
      // 6655: iastore
      // 6656: aload 0
      // 6657: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 665a: bipush 3
      // 665b: aaload
      // 665c: bipush 37
      // 665e: sipush 402
      // 6661: iastore
      // 6662: aload 0
      // 6663: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6666: bipush 114
      // 6668: aaload
      // 6669: bipush 55
      // 666b: sipush 401
      // 666e: iastore
      // 666f: aload 0
      // 6670: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6673: bipush 85
      // 6675: aaload
      // 6676: sipush 152
      // 6679: sipush 400
      // 667c: iastore
      // 667d: aload 0
      // 667e: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6681: bipush 60
      // 6683: aaload
      // 6684: bipush 47
      // 6686: sipush 399
      // 6689: iastore
      // 668a: aload 0
      // 668b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 668e: bipush 65
      // 6690: aaload
      // 6691: bipush 96
      // 6693: sipush 398
      // 6696: iastore
      // 6697: aload 0
      // 6698: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 669b: bipush 74
      // 669d: aaload
      // 669e: bipush 110
      // 66a0: sipush 397
      // 66a3: iastore
      // 66a4: aload 0
      // 66a5: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 66a8: bipush 86
      // 66aa: aaload
      // 66ab: sipush 182
      // 66ae: sipush 396
      // 66b1: iastore
      // 66b2: aload 0
      // 66b3: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 66b6: bipush 50
      // 66b8: aaload
      // 66b9: bipush 99
      // 66bb: sipush 395
      // 66be: iastore
      // 66bf: aload 0
      // 66c0: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 66c3: bipush 67
      // 66c5: aaload
      // 66c6: sipush 186
      // 66c9: sipush 394
      // 66cc: iastore
      // 66cd: aload 0
      // 66ce: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 66d1: bipush 81
      // 66d3: aaload
      // 66d4: bipush 74
      // 66d6: sipush 393
      // 66d9: iastore
      // 66da: aload 0
      // 66db: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 66de: bipush 80
      // 66e0: aaload
      // 66e1: bipush 37
      // 66e3: sipush 392
      // 66e6: iastore
      // 66e7: aload 0
      // 66e8: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 66eb: bipush 21
      // 66ed: aaload
      // 66ee: bipush 60
      // 66f0: sipush 391
      // 66f3: iastore
      // 66f4: aload 0
      // 66f5: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 66f8: bipush 110
      // 66fa: aaload
      // 66fb: bipush 12
      // 66fd: sipush 390
      // 6700: iastore
      // 6701: aload 0
      // 6702: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6705: bipush 60
      // 6707: aaload
      // 6708: sipush 162
      // 670b: sipush 389
      // 670e: iastore
      // 670f: aload 0
      // 6710: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6713: bipush 29
      // 6715: aaload
      // 6716: bipush 115
      // 6718: sipush 388
      // 671b: iastore
      // 671c: aload 0
      // 671d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6720: bipush 83
      // 6722: aaload
      // 6723: sipush 130
      // 6726: sipush 387
      // 6729: iastore
      // 672a: aload 0
      // 672b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 672e: bipush 52
      // 6730: aaload
      // 6731: sipush 136
      // 6734: sipush 386
      // 6737: iastore
      // 6738: aload 0
      // 6739: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 673c: bipush 63
      // 673e: aaload
      // 673f: bipush 114
      // 6741: sipush 385
      // 6744: iastore
      // 6745: aload 0
      // 6746: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6749: bipush 49
      // 674b: aaload
      // 674c: bipush 127
      // 674e: sipush 384
      // 6751: iastore
      // 6752: aload 0
      // 6753: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6756: bipush 83
      // 6758: aaload
      // 6759: bipush 109
      // 675b: sipush 383
      // 675e: iastore
      // 675f: aload 0
      // 6760: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6763: bipush 66
      // 6765: aaload
      // 6766: sipush 128
      // 6769: sipush 382
      // 676c: iastore
      // 676d: aload 0
      // 676e: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6771: bipush 78
      // 6773: aaload
      // 6774: sipush 136
      // 6777: sipush 381
      // 677a: iastore
      // 677b: aload 0
      // 677c: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 677f: bipush 81
      // 6781: aaload
      // 6782: sipush 180
      // 6785: sipush 380
      // 6788: iastore
      // 6789: aload 0
      // 678a: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 678d: bipush 76
      // 678f: aaload
      // 6790: bipush 104
      // 6792: sipush 379
      // 6795: iastore
      // 6796: aload 0
      // 6797: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 679a: bipush 56
      // 679c: aaload
      // 679d: sipush 156
      // 67a0: sipush 378
      // 67a3: iastore
      // 67a4: aload 0
      // 67a5: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 67a8: bipush 61
      // 67aa: aaload
      // 67ab: bipush 23
      // 67ad: sipush 377
      // 67b0: iastore
      // 67b1: aload 0
      // 67b2: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 67b5: bipush 4
      // 67b6: aaload
      // 67b7: bipush 30
      // 67b9: sipush 376
      // 67bc: iastore
      // 67bd: aload 0
      // 67be: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 67c1: bipush 69
      // 67c3: aaload
      // 67c4: sipush 154
      // 67c7: sipush 375
      // 67ca: iastore
      // 67cb: aload 0
      // 67cc: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 67cf: bipush 100
      // 67d1: aaload
      // 67d2: bipush 37
      // 67d4: sipush 374
      // 67d7: iastore
      // 67d8: aload 0
      // 67d9: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 67dc: bipush 54
      // 67de: aaload
      // 67df: sipush 177
      // 67e2: sipush 373
      // 67e5: iastore
      // 67e6: aload 0
      // 67e7: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 67ea: bipush 23
      // 67ec: aaload
      // 67ed: bipush 119
      // 67ef: sipush 372
      // 67f2: iastore
      // 67f3: aload 0
      // 67f4: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 67f7: bipush 71
      // 67f9: aaload
      // 67fa: sipush 171
      // 67fd: sipush 371
      // 6800: iastore
      // 6801: aload 0
      // 6802: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6805: bipush 84
      // 6807: aaload
      // 6808: sipush 146
      // 680b: sipush 370
      // 680e: iastore
      // 680f: aload 0
      // 6810: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6813: bipush 20
      // 6815: aaload
      // 6816: sipush 184
      // 6819: sipush 369
      // 681c: iastore
      // 681d: aload 0
      // 681e: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6821: bipush 86
      // 6823: aaload
      // 6824: bipush 76
      // 6826: sipush 368
      // 6829: iastore
      // 682a: aload 0
      // 682b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 682e: bipush 74
      // 6830: aaload
      // 6831: sipush 132
      // 6834: sipush 367
      // 6837: iastore
      // 6838: aload 0
      // 6839: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 683c: bipush 47
      // 683e: aaload
      // 683f: bipush 97
      // 6841: sipush 366
      // 6844: iastore
      // 6845: aload 0
      // 6846: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6849: bipush 82
      // 684b: aaload
      // 684c: sipush 137
      // 684f: sipush 365
      // 6852: iastore
      // 6853: aload 0
      // 6854: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6857: bipush 94
      // 6859: aaload
      // 685a: bipush 56
      // 685c: sipush 364
      // 685f: iastore
      // 6860: aload 0
      // 6861: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6864: bipush 92
      // 6866: aaload
      // 6867: bipush 30
      // 6869: sipush 363
      // 686c: iastore
      // 686d: aload 0
      // 686e: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6871: bipush 19
      // 6873: aaload
      // 6874: bipush 117
      // 6876: sipush 362
      // 6879: iastore
      // 687a: aload 0
      // 687b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 687e: bipush 48
      // 6880: aaload
      // 6881: sipush 173
      // 6884: sipush 361
      // 6887: iastore
      // 6888: aload 0
      // 6889: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 688c: bipush 2
      // 688d: aaload
      // 688e: sipush 136
      // 6891: sipush 360
      // 6894: iastore
      // 6895: aload 0
      // 6896: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6899: bipush 7
      // 689b: aaload
      // 689c: sipush 182
      // 689f: sipush 359
      // 68a2: iastore
      // 68a3: aload 0
      // 68a4: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 68a7: bipush 74
      // 68a9: aaload
      // 68aa: sipush 188
      // 68ad: sipush 358
      // 68b0: iastore
      // 68b1: aload 0
      // 68b2: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 68b5: bipush 14
      // 68b7: aaload
      // 68b8: sipush 132
      // 68bb: sipush 357
      // 68be: iastore
      // 68bf: aload 0
      // 68c0: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 68c3: bipush 62
      // 68c5: aaload
      // 68c6: sipush 172
      // 68c9: sipush 356
      // 68cc: iastore
      // 68cd: aload 0
      // 68ce: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 68d1: bipush 25
      // 68d3: aaload
      // 68d4: bipush 39
      // 68d6: sipush 355
      // 68d9: iastore
      // 68da: aload 0
      // 68db: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 68de: bipush 85
      // 68e0: aaload
      // 68e1: sipush 129
      // 68e4: sipush 354
      // 68e7: iastore
      // 68e8: aload 0
      // 68e9: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 68ec: bipush 64
      // 68ee: aaload
      // 68ef: bipush 98
      // 68f1: sipush 353
      // 68f4: iastore
      // 68f5: aload 0
      // 68f6: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 68f9: bipush 67
      // 68fb: aaload
      // 68fc: bipush 127
      // 68fe: sipush 352
      // 6901: iastore
      // 6902: aload 0
      // 6903: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6906: bipush 72
      // 6908: aaload
      // 6909: sipush 167
      // 690c: sipush 351
      // 690f: iastore
      // 6910: aload 0
      // 6911: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6914: bipush 57
      // 6916: aaload
      // 6917: sipush 143
      // 691a: sipush 350
      // 691d: iastore
      // 691e: aload 0
      // 691f: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6922: bipush 76
      // 6924: aaload
      // 6925: sipush 187
      // 6928: sipush 349
      // 692b: iastore
      // 692c: aload 0
      // 692d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6930: bipush 83
      // 6932: aaload
      // 6933: sipush 181
      // 6936: sipush 348
      // 6939: iastore
      // 693a: aload 0
      // 693b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 693e: bipush 84
      // 6940: aaload
      // 6941: bipush 10
      // 6943: sipush 347
      // 6946: iastore
      // 6947: aload 0
      // 6948: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 694b: bipush 55
      // 694d: aaload
      // 694e: sipush 166
      // 6951: sipush 346
      // 6954: iastore
      // 6955: aload 0
      // 6956: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6959: bipush 55
      // 695b: aaload
      // 695c: sipush 188
      // 695f: sipush 345
      // 6962: iastore
      // 6963: aload 0
      // 6964: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6967: bipush 13
      // 6969: aaload
      // 696a: sipush 151
      // 696d: sipush 344
      // 6970: iastore
      // 6971: aload 0
      // 6972: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6975: bipush 62
      // 6977: aaload
      // 6978: bipush 124
      // 697a: sipush 343
      // 697d: iastore
      // 697e: aload 0
      // 697f: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6982: bipush 53
      // 6984: aaload
      // 6985: sipush 136
      // 6988: sipush 342
      // 698b: iastore
      // 698c: aload 0
      // 698d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6990: bipush 106
      // 6992: aaload
      // 6993: bipush 57
      // 6995: sipush 341
      // 6998: iastore
      // 6999: aload 0
      // 699a: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 699d: bipush 47
      // 699f: aaload
      // 69a0: sipush 166
      // 69a3: sipush 340
      // 69a6: iastore
      // 69a7: aload 0
      // 69a8: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 69ab: bipush 109
      // 69ad: aaload
      // 69ae: bipush 30
      // 69b0: sipush 339
      // 69b3: iastore
      // 69b4: aload 0
      // 69b5: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 69b8: bipush 78
      // 69ba: aaload
      // 69bb: bipush 114
      // 69bd: sipush 338
      // 69c0: iastore
      // 69c1: aload 0
      // 69c2: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 69c5: bipush 83
      // 69c7: aaload
      // 69c8: bipush 19
      // 69ca: sipush 337
      // 69cd: iastore
      // 69ce: aload 0
      // 69cf: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 69d2: bipush 56
      // 69d4: aaload
      // 69d5: sipush 162
      // 69d8: sipush 336
      // 69db: iastore
      // 69dc: aload 0
      // 69dd: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 69e0: bipush 60
      // 69e2: aaload
      // 69e3: sipush 177
      // 69e6: sipush 335
      // 69e9: iastore
      // 69ea: aload 0
      // 69eb: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 69ee: bipush 88
      // 69f0: aaload
      // 69f1: bipush 9
      // 69f3: sipush 334
      // 69f6: iastore
      // 69f7: aload 0
      // 69f8: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 69fb: bipush 74
      // 69fd: aaload
      // 69fe: sipush 163
      // 6a01: sipush 333
      // 6a04: iastore
      // 6a05: aload 0
      // 6a06: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6a09: bipush 52
      // 6a0b: aaload
      // 6a0c: sipush 156
      // 6a0f: sipush 332
      // 6a12: iastore
      // 6a13: aload 0
      // 6a14: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6a17: bipush 71
      // 6a19: aaload
      // 6a1a: sipush 180
      // 6a1d: sipush 331
      // 6a20: iastore
      // 6a21: aload 0
      // 6a22: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6a25: bipush 60
      // 6a27: aaload
      // 6a28: bipush 57
      // 6a2a: sipush 330
      // 6a2d: iastore
      // 6a2e: aload 0
      // 6a2f: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6a32: bipush 72
      // 6a34: aaload
      // 6a35: sipush 173
      // 6a38: sipush 329
      // 6a3b: iastore
      // 6a3c: aload 0
      // 6a3d: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6a40: bipush 82
      // 6a42: aaload
      // 6a43: bipush 91
      // 6a45: sipush 328
      // 6a48: iastore
      // 6a49: aload 0
      // 6a4a: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6a4d: bipush 51
      // 6a4f: aaload
      // 6a50: sipush 186
      // 6a53: sipush 327
      // 6a56: iastore
      // 6a57: aload 0
      // 6a58: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6a5b: bipush 75
      // 6a5d: aaload
      // 6a5e: bipush 86
      // 6a60: sipush 326
      // 6a63: iastore
      // 6a64: aload 0
      // 6a65: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6a68: bipush 75
      // 6a6a: aaload
      // 6a6b: bipush 78
      // 6a6d: sipush 325
      // 6a70: iastore
      // 6a71: aload 0
      // 6a72: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6a75: bipush 76
      // 6a77: aaload
      // 6a78: sipush 170
      // 6a7b: sipush 324
      // 6a7e: iastore
      // 6a7f: aload 0
      // 6a80: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6a83: bipush 60
      // 6a85: aaload
      // 6a86: sipush 147
      // 6a89: sipush 323
      // 6a8c: iastore
      // 6a8d: aload 0
      // 6a8e: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6a91: bipush 82
      // 6a93: aaload
      // 6a94: bipush 75
      // 6a96: sipush 322
      // 6a99: iastore
      // 6a9a: aload 0
      // 6a9b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6a9e: bipush 80
      // 6aa0: aaload
      // 6aa1: sipush 148
      // 6aa4: sipush 321
      // 6aa7: iastore
      // 6aa8: aload 0
      // 6aa9: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6aac: bipush 86
      // 6aae: aaload
      // 6aaf: sipush 150
      // 6ab2: sipush 320
      // 6ab5: iastore
      // 6ab6: aload 0
      // 6ab7: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6aba: bipush 13
      // 6abc: aaload
      // 6abd: bipush 95
      // 6abf: sipush 319
      // 6ac2: iastore
      // 6ac3: aload 0
      // 6ac4: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6ac7: bipush 0
      // 6ac8: aaload
      // 6ac9: bipush 11
      // 6acb: sipush 318
      // 6ace: iastore
      // 6acf: aload 0
      // 6ad0: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6ad3: bipush 84
      // 6ad5: aaload
      // 6ad6: sipush 190
      // 6ad9: sipush 317
      // 6adc: iastore
      // 6add: aload 0
      // 6ade: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6ae1: bipush 76
      // 6ae3: aaload
      // 6ae4: sipush 166
      // 6ae7: sipush 316
      // 6aea: iastore
      // 6aeb: aload 0
      // 6aec: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6aef: bipush 14
      // 6af1: aaload
      // 6af2: bipush 72
      // 6af4: sipush 315
      // 6af7: iastore
      // 6af8: aload 0
      // 6af9: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6afc: bipush 67
      // 6afe: aaload
      // 6aff: sipush 144
      // 6b02: sipush 314
      // 6b05: iastore
      // 6b06: aload 0
      // 6b07: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6b0a: bipush 84
      // 6b0c: aaload
      // 6b0d: bipush 44
      // 6b0f: sipush 313
      // 6b12: iastore
      // 6b13: aload 0
      // 6b14: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6b17: bipush 72
      // 6b19: aaload
      // 6b1a: bipush 125
      // 6b1c: sipush 312
      // 6b1f: iastore
      // 6b20: aload 0
      // 6b21: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6b24: bipush 66
      // 6b26: aaload
      // 6b27: bipush 127
      // 6b29: sipush 311
      // 6b2c: iastore
      // 6b2d: aload 0
      // 6b2e: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6b31: bipush 60
      // 6b33: aaload
      // 6b34: bipush 25
      // 6b36: sipush 310
      // 6b39: iastore
      // 6b3a: aload 0
      // 6b3b: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6b3e: bipush 70
      // 6b40: aaload
      // 6b41: sipush 146
      // 6b44: sipush 309
      // 6b47: iastore
      // 6b48: aload 0
      // 6b49: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6b4c: bipush 79
      // 6b4e: aaload
      // 6b4f: sipush 135
      // 6b52: sipush 308
      // 6b55: iastore
      // 6b56: aload 0
      // 6b57: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6b5a: bipush 54
      // 6b5c: aaload
      // 6b5d: sipush 135
      // 6b60: sipush 307
      // 6b63: iastore
      // 6b64: aload 0
      // 6b65: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6b68: bipush 60
      // 6b6a: aaload
      // 6b6b: bipush 104
      // 6b6d: sipush 306
      // 6b70: iastore
      // 6b71: aload 0
      // 6b72: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6b75: bipush 55
      // 6b77: aaload
      // 6b78: sipush 132
      // 6b7b: sipush 305
      // 6b7e: iastore
      // 6b7f: aload 0
      // 6b80: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6b83: bipush 94
      // 6b85: aaload
      // 6b86: bipush 2
      // 6b87: sipush 304
      // 6b8a: iastore
      // 6b8b: aload 0
      // 6b8c: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6b8f: bipush 54
      // 6b91: aaload
      // 6b92: sipush 133
      // 6b95: sipush 303
      // 6b98: iastore
      // 6b99: aload 0
      // 6b9a: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6b9d: bipush 56
      // 6b9f: aaload
      // 6ba0: sipush 190
      // 6ba3: sipush 302
      // 6ba6: iastore
      // 6ba7: aload 0
      // 6ba8: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6bab: bipush 58
      // 6bad: aaload
      // 6bae: sipush 174
      // 6bb1: sipush 301
      // 6bb4: iastore
      // 6bb5: aload 0
      // 6bb6: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6bb9: bipush 80
      // 6bbb: aaload
      // 6bbc: sipush 144
      // 6bbf: sipush 300
      // 6bc2: iastore
      // 6bc3: aload 0
      // 6bc4: getfield io/legado/app/help/BytesEncodingDetect.GBKFreq [[I
      // 6bc7: bipush 85
      // 6bc9: aaload
      // 6bca: bipush 113
      // 6bcc: sipush 299
      // 6bcf: iastore
      // 6bd0: aload 0
      // 6bd1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6bd4: bipush 31
      // 6bd6: aaload
      // 6bd7: bipush 43
      // 6bd9: sipush 600
      // 6bdc: iastore
      // 6bdd: aload 0
      // 6bde: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6be1: bipush 19
      // 6be3: aaload
      // 6be4: bipush 56
      // 6be6: sipush 599
      // 6be9: iastore
      // 6bea: aload 0
      // 6beb: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6bee: bipush 38
      // 6bf0: aaload
      // 6bf1: bipush 46
      // 6bf3: sipush 598
      // 6bf6: iastore
      // 6bf7: aload 0
      // 6bf8: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6bfb: bipush 3
      // 6bfc: aaload
      // 6bfd: bipush 3
      // 6bfe: sipush 597
      // 6c01: iastore
      // 6c02: aload 0
      // 6c03: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6c06: bipush 29
      // 6c08: aaload
      // 6c09: bipush 77
      // 6c0b: sipush 596
      // 6c0e: iastore
      // 6c0f: aload 0
      // 6c10: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6c13: bipush 19
      // 6c15: aaload
      // 6c16: bipush 33
      // 6c18: sipush 595
      // 6c1b: iastore
      // 6c1c: aload 0
      // 6c1d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6c20: bipush 30
      // 6c22: aaload
      // 6c23: bipush 0
      // 6c24: sipush 594
      // 6c27: iastore
      // 6c28: aload 0
      // 6c29: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6c2c: bipush 29
      // 6c2e: aaload
      // 6c2f: bipush 89
      // 6c31: sipush 593
      // 6c34: iastore
      // 6c35: aload 0
      // 6c36: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6c39: bipush 31
      // 6c3b: aaload
      // 6c3c: bipush 26
      // 6c3e: sipush 592
      // 6c41: iastore
      // 6c42: aload 0
      // 6c43: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6c46: bipush 31
      // 6c48: aaload
      // 6c49: bipush 38
      // 6c4b: sipush 591
      // 6c4e: iastore
      // 6c4f: aload 0
      // 6c50: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6c53: bipush 32
      // 6c55: aaload
      // 6c56: bipush 85
      // 6c58: sipush 590
      // 6c5b: iastore
      // 6c5c: aload 0
      // 6c5d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6c60: bipush 15
      // 6c62: aaload
      // 6c63: bipush 0
      // 6c64: sipush 589
      // 6c67: iastore
      // 6c68: aload 0
      // 6c69: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6c6c: bipush 16
      // 6c6e: aaload
      // 6c6f: bipush 54
      // 6c71: sipush 588
      // 6c74: iastore
      // 6c75: aload 0
      // 6c76: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6c79: bipush 15
      // 6c7b: aaload
      // 6c7c: bipush 76
      // 6c7e: sipush 587
      // 6c81: iastore
      // 6c82: aload 0
      // 6c83: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6c86: bipush 31
      // 6c88: aaload
      // 6c89: bipush 25
      // 6c8b: sipush 586
      // 6c8e: iastore
      // 6c8f: aload 0
      // 6c90: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6c93: bipush 23
      // 6c95: aaload
      // 6c96: bipush 13
      // 6c98: sipush 585
      // 6c9b: iastore
      // 6c9c: aload 0
      // 6c9d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6ca0: bipush 28
      // 6ca2: aaload
      // 6ca3: bipush 34
      // 6ca5: sipush 584
      // 6ca8: iastore
      // 6ca9: aload 0
      // 6caa: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6cad: bipush 18
      // 6caf: aaload
      // 6cb0: bipush 9
      // 6cb2: sipush 583
      // 6cb5: iastore
      // 6cb6: aload 0
      // 6cb7: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6cba: bipush 29
      // 6cbc: aaload
      // 6cbd: bipush 37
      // 6cbf: sipush 582
      // 6cc2: iastore
      // 6cc3: aload 0
      // 6cc4: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6cc7: bipush 22
      // 6cc9: aaload
      // 6cca: bipush 45
      // 6ccc: sipush 581
      // 6ccf: iastore
      // 6cd0: aload 0
      // 6cd1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6cd4: bipush 19
      // 6cd6: aaload
      // 6cd7: bipush 46
      // 6cd9: sipush 580
      // 6cdc: iastore
      // 6cdd: aload 0
      // 6cde: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6ce1: bipush 16
      // 6ce3: aaload
      // 6ce4: bipush 65
      // 6ce6: sipush 579
      // 6ce9: iastore
      // 6cea: aload 0
      // 6ceb: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6cee: bipush 23
      // 6cf0: aaload
      // 6cf1: bipush 5
      // 6cf2: sipush 578
      // 6cf5: iastore
      // 6cf6: aload 0
      // 6cf7: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6cfa: bipush 26
      // 6cfc: aaload
      // 6cfd: bipush 70
      // 6cff: sipush 577
      // 6d02: iastore
      // 6d03: aload 0
      // 6d04: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6d07: bipush 31
      // 6d09: aaload
      // 6d0a: bipush 53
      // 6d0c: sipush 576
      // 6d0f: iastore
      // 6d10: aload 0
      // 6d11: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6d14: bipush 27
      // 6d16: aaload
      // 6d17: bipush 12
      // 6d19: sipush 575
      // 6d1c: iastore
      // 6d1d: aload 0
      // 6d1e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6d21: bipush 30
      // 6d23: aaload
      // 6d24: bipush 67
      // 6d26: sipush 574
      // 6d29: iastore
      // 6d2a: aload 0
      // 6d2b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6d2e: bipush 31
      // 6d30: aaload
      // 6d31: bipush 57
      // 6d33: sipush 573
      // 6d36: iastore
      // 6d37: aload 0
      // 6d38: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6d3b: bipush 20
      // 6d3d: aaload
      // 6d3e: bipush 20
      // 6d40: sipush 572
      // 6d43: iastore
      // 6d44: aload 0
      // 6d45: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6d48: bipush 30
      // 6d4a: aaload
      // 6d4b: bipush 31
      // 6d4d: sipush 571
      // 6d50: iastore
      // 6d51: aload 0
      // 6d52: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6d55: bipush 20
      // 6d57: aaload
      // 6d58: bipush 72
      // 6d5a: sipush 570
      // 6d5d: iastore
      // 6d5e: aload 0
      // 6d5f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6d62: bipush 15
      // 6d64: aaload
      // 6d65: bipush 51
      // 6d67: sipush 569
      // 6d6a: iastore
      // 6d6b: aload 0
      // 6d6c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6d6f: bipush 3
      // 6d70: aaload
      // 6d71: bipush 8
      // 6d73: sipush 568
      // 6d76: iastore
      // 6d77: aload 0
      // 6d78: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6d7b: bipush 32
      // 6d7d: aaload
      // 6d7e: bipush 53
      // 6d80: sipush 567
      // 6d83: iastore
      // 6d84: aload 0
      // 6d85: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6d88: bipush 27
      // 6d8a: aaload
      // 6d8b: bipush 85
      // 6d8d: sipush 566
      // 6d90: iastore
      // 6d91: aload 0
      // 6d92: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6d95: bipush 25
      // 6d97: aaload
      // 6d98: bipush 23
      // 6d9a: sipush 565
      // 6d9d: iastore
      // 6d9e: aload 0
      // 6d9f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6da2: bipush 15
      // 6da4: aaload
      // 6da5: bipush 44
      // 6da7: sipush 564
      // 6daa: iastore
      // 6dab: aload 0
      // 6dac: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6daf: bipush 32
      // 6db1: aaload
      // 6db2: bipush 3
      // 6db3: sipush 563
      // 6db6: iastore
      // 6db7: aload 0
      // 6db8: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6dbb: bipush 31
      // 6dbd: aaload
      // 6dbe: bipush 68
      // 6dc0: sipush 562
      // 6dc3: iastore
      // 6dc4: aload 0
      // 6dc5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6dc8: bipush 30
      // 6dca: aaload
      // 6dcb: bipush 24
      // 6dcd: sipush 561
      // 6dd0: iastore
      // 6dd1: aload 0
      // 6dd2: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6dd5: bipush 29
      // 6dd7: aaload
      // 6dd8: bipush 49
      // 6dda: sipush 560
      // 6ddd: iastore
      // 6dde: aload 0
      // 6ddf: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6de2: bipush 27
      // 6de4: aaload
      // 6de5: bipush 49
      // 6de7: sipush 559
      // 6dea: iastore
      // 6deb: aload 0
      // 6dec: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6def: bipush 23
      // 6df1: aaload
      // 6df2: bipush 23
      // 6df4: sipush 558
      // 6df7: iastore
      // 6df8: aload 0
      // 6df9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6dfc: bipush 31
      // 6dfe: aaload
      // 6dff: bipush 91
      // 6e01: sipush 557
      // 6e04: iastore
      // 6e05: aload 0
      // 6e06: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6e09: bipush 31
      // 6e0b: aaload
      // 6e0c: bipush 46
      // 6e0e: sipush 556
      // 6e11: iastore
      // 6e12: aload 0
      // 6e13: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6e16: bipush 19
      // 6e18: aaload
      // 6e19: bipush 74
      // 6e1b: sipush 555
      // 6e1e: iastore
      // 6e1f: aload 0
      // 6e20: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6e23: bipush 27
      // 6e25: aaload
      // 6e26: bipush 27
      // 6e28: sipush 554
      // 6e2b: iastore
      // 6e2c: aload 0
      // 6e2d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6e30: bipush 3
      // 6e31: aaload
      // 6e32: bipush 17
      // 6e34: sipush 553
      // 6e37: iastore
      // 6e38: aload 0
      // 6e39: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6e3c: bipush 20
      // 6e3e: aaload
      // 6e3f: bipush 38
      // 6e41: sipush 552
      // 6e44: iastore
      // 6e45: aload 0
      // 6e46: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6e49: bipush 21
      // 6e4b: aaload
      // 6e4c: bipush 82
      // 6e4e: sipush 551
      // 6e51: iastore
      // 6e52: aload 0
      // 6e53: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6e56: bipush 28
      // 6e58: aaload
      // 6e59: bipush 25
      // 6e5b: sipush 550
      // 6e5e: iastore
      // 6e5f: aload 0
      // 6e60: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6e63: bipush 32
      // 6e65: aaload
      // 6e66: bipush 5
      // 6e67: sipush 549
      // 6e6a: iastore
      // 6e6b: aload 0
      // 6e6c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6e6f: bipush 31
      // 6e71: aaload
      // 6e72: bipush 23
      // 6e74: sipush 548
      // 6e77: iastore
      // 6e78: aload 0
      // 6e79: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6e7c: bipush 25
      // 6e7e: aaload
      // 6e7f: bipush 45
      // 6e81: sipush 547
      // 6e84: iastore
      // 6e85: aload 0
      // 6e86: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6e89: bipush 32
      // 6e8b: aaload
      // 6e8c: bipush 87
      // 6e8e: sipush 546
      // 6e91: iastore
      // 6e92: aload 0
      // 6e93: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6e96: bipush 18
      // 6e98: aaload
      // 6e99: bipush 26
      // 6e9b: sipush 545
      // 6e9e: iastore
      // 6e9f: aload 0
      // 6ea0: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6ea3: bipush 24
      // 6ea5: aaload
      // 6ea6: bipush 10
      // 6ea8: sipush 544
      // 6eab: iastore
      // 6eac: aload 0
      // 6ead: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6eb0: bipush 26
      // 6eb2: aaload
      // 6eb3: bipush 82
      // 6eb5: sipush 543
      // 6eb8: iastore
      // 6eb9: aload 0
      // 6eba: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6ebd: bipush 15
      // 6ebf: aaload
      // 6ec0: bipush 89
      // 6ec2: sipush 542
      // 6ec5: iastore
      // 6ec6: aload 0
      // 6ec7: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6eca: bipush 28
      // 6ecc: aaload
      // 6ecd: bipush 36
      // 6ecf: sipush 541
      // 6ed2: iastore
      // 6ed3: aload 0
      // 6ed4: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6ed7: bipush 28
      // 6ed9: aaload
      // 6eda: bipush 31
      // 6edc: sipush 540
      // 6edf: iastore
      // 6ee0: aload 0
      // 6ee1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6ee4: bipush 16
      // 6ee6: aaload
      // 6ee7: bipush 23
      // 6ee9: sipush 539
      // 6eec: iastore
      // 6eed: aload 0
      // 6eee: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6ef1: bipush 16
      // 6ef3: aaload
      // 6ef4: bipush 77
      // 6ef6: sipush 538
      // 6ef9: iastore
      // 6efa: aload 0
      // 6efb: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6efe: bipush 19
      // 6f00: aaload
      // 6f01: bipush 84
      // 6f03: sipush 537
      // 6f06: iastore
      // 6f07: aload 0
      // 6f08: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6f0b: bipush 23
      // 6f0d: aaload
      // 6f0e: bipush 72
      // 6f10: sipush 536
      // 6f13: iastore
      // 6f14: aload 0
      // 6f15: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6f18: bipush 38
      // 6f1a: aaload
      // 6f1b: bipush 48
      // 6f1d: sipush 535
      // 6f20: iastore
      // 6f21: aload 0
      // 6f22: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6f25: bipush 23
      // 6f27: aaload
      // 6f28: bipush 2
      // 6f29: sipush 534
      // 6f2c: iastore
      // 6f2d: aload 0
      // 6f2e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6f31: bipush 30
      // 6f33: aaload
      // 6f34: bipush 20
      // 6f36: sipush 533
      // 6f39: iastore
      // 6f3a: aload 0
      // 6f3b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6f3e: bipush 38
      // 6f40: aaload
      // 6f41: bipush 47
      // 6f43: sipush 532
      // 6f46: iastore
      // 6f47: aload 0
      // 6f48: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6f4b: bipush 39
      // 6f4d: aaload
      // 6f4e: bipush 12
      // 6f50: sipush 531
      // 6f53: iastore
      // 6f54: aload 0
      // 6f55: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6f58: bipush 23
      // 6f5a: aaload
      // 6f5b: bipush 21
      // 6f5d: sipush 530
      // 6f60: iastore
      // 6f61: aload 0
      // 6f62: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6f65: bipush 18
      // 6f67: aaload
      // 6f68: bipush 17
      // 6f6a: sipush 529
      // 6f6d: iastore
      // 6f6e: aload 0
      // 6f6f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6f72: bipush 30
      // 6f74: aaload
      // 6f75: bipush 87
      // 6f77: sipush 528
      // 6f7a: iastore
      // 6f7b: aload 0
      // 6f7c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6f7f: bipush 29
      // 6f81: aaload
      // 6f82: bipush 62
      // 6f84: sipush 527
      // 6f87: iastore
      // 6f88: aload 0
      // 6f89: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6f8c: bipush 29
      // 6f8e: aaload
      // 6f8f: bipush 87
      // 6f91: sipush 526
      // 6f94: iastore
      // 6f95: aload 0
      // 6f96: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6f99: bipush 34
      // 6f9b: aaload
      // 6f9c: bipush 53
      // 6f9e: sipush 525
      // 6fa1: iastore
      // 6fa2: aload 0
      // 6fa3: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6fa6: bipush 32
      // 6fa8: aaload
      // 6fa9: bipush 29
      // 6fab: sipush 524
      // 6fae: iastore
      // 6faf: aload 0
      // 6fb0: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6fb3: bipush 35
      // 6fb5: aaload
      // 6fb6: bipush 0
      // 6fb7: sipush 523
      // 6fba: iastore
      // 6fbb: aload 0
      // 6fbc: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6fbf: bipush 24
      // 6fc1: aaload
      // 6fc2: bipush 43
      // 6fc4: sipush 522
      // 6fc7: iastore
      // 6fc8: aload 0
      // 6fc9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6fcc: bipush 36
      // 6fce: aaload
      // 6fcf: bipush 44
      // 6fd1: sipush 521
      // 6fd4: iastore
      // 6fd5: aload 0
      // 6fd6: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6fd9: bipush 20
      // 6fdb: aaload
      // 6fdc: bipush 30
      // 6fde: sipush 520
      // 6fe1: iastore
      // 6fe2: aload 0
      // 6fe3: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6fe6: bipush 39
      // 6fe8: aaload
      // 6fe9: bipush 86
      // 6feb: sipush 519
      // 6fee: iastore
      // 6fef: aload 0
      // 6ff0: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 6ff3: bipush 22
      // 6ff5: aaload
      // 6ff6: bipush 14
      // 6ff8: sipush 518
      // 6ffb: iastore
      // 6ffc: aload 0
      // 6ffd: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7000: bipush 29
      // 7002: aaload
      // 7003: bipush 39
      // 7005: sipush 517
      // 7008: iastore
      // 7009: aload 0
      // 700a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 700d: bipush 28
      // 700f: aaload
      // 7010: bipush 38
      // 7012: sipush 516
      // 7015: iastore
      // 7016: aload 0
      // 7017: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 701a: bipush 23
      // 701c: aaload
      // 701d: bipush 79
      // 701f: sipush 515
      // 7022: iastore
      // 7023: aload 0
      // 7024: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7027: bipush 24
      // 7029: aaload
      // 702a: bipush 56
      // 702c: sipush 514
      // 702f: iastore
      // 7030: aload 0
      // 7031: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7034: bipush 29
      // 7036: aaload
      // 7037: bipush 63
      // 7039: sipush 513
      // 703c: iastore
      // 703d: aload 0
      // 703e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7041: bipush 31
      // 7043: aaload
      // 7044: bipush 45
      // 7046: sipush 512
      // 7049: iastore
      // 704a: aload 0
      // 704b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 704e: bipush 23
      // 7050: aaload
      // 7051: bipush 26
      // 7053: sipush 511
      // 7056: iastore
      // 7057: aload 0
      // 7058: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 705b: bipush 15
      // 705d: aaload
      // 705e: bipush 87
      // 7060: sipush 510
      // 7063: iastore
      // 7064: aload 0
      // 7065: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7068: bipush 30
      // 706a: aaload
      // 706b: bipush 74
      // 706d: sipush 509
      // 7070: iastore
      // 7071: aload 0
      // 7072: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7075: bipush 24
      // 7077: aaload
      // 7078: bipush 69
      // 707a: sipush 508
      // 707d: iastore
      // 707e: aload 0
      // 707f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7082: bipush 20
      // 7084: aaload
      // 7085: bipush 4
      // 7086: sipush 507
      // 7089: iastore
      // 708a: aload 0
      // 708b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 708e: bipush 27
      // 7090: aaload
      // 7091: bipush 50
      // 7093: sipush 506
      // 7096: iastore
      // 7097: aload 0
      // 7098: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 709b: bipush 30
      // 709d: aaload
      // 709e: bipush 75
      // 70a0: sipush 505
      // 70a3: iastore
      // 70a4: aload 0
      // 70a5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 70a8: bipush 24
      // 70aa: aaload
      // 70ab: bipush 13
      // 70ad: sipush 504
      // 70b0: iastore
      // 70b1: aload 0
      // 70b2: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 70b5: bipush 30
      // 70b7: aaload
      // 70b8: bipush 8
      // 70ba: sipush 503
      // 70bd: iastore
      // 70be: aload 0
      // 70bf: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 70c2: bipush 31
      // 70c4: aaload
      // 70c5: bipush 6
      // 70c7: sipush 502
      // 70ca: iastore
      // 70cb: aload 0
      // 70cc: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 70cf: bipush 25
      // 70d1: aaload
      // 70d2: bipush 80
      // 70d4: sipush 501
      // 70d7: iastore
      // 70d8: aload 0
      // 70d9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 70dc: bipush 36
      // 70de: aaload
      // 70df: bipush 8
      // 70e1: sipush 500
      // 70e4: iastore
      // 70e5: aload 0
      // 70e6: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 70e9: bipush 15
      // 70eb: aaload
      // 70ec: bipush 18
      // 70ee: sipush 499
      // 70f1: iastore
      // 70f2: aload 0
      // 70f3: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 70f6: bipush 39
      // 70f8: aaload
      // 70f9: bipush 23
      // 70fb: sipush 498
      // 70fe: iastore
      // 70ff: aload 0
      // 7100: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7103: bipush 16
      // 7105: aaload
      // 7106: bipush 24
      // 7108: sipush 497
      // 710b: iastore
      // 710c: aload 0
      // 710d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7110: bipush 31
      // 7112: aaload
      // 7113: bipush 89
      // 7115: sipush 496
      // 7118: iastore
      // 7119: aload 0
      // 711a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 711d: bipush 15
      // 711f: aaload
      // 7120: bipush 71
      // 7122: sipush 495
      // 7125: iastore
      // 7126: aload 0
      // 7127: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 712a: bipush 15
      // 712c: aaload
      // 712d: bipush 57
      // 712f: sipush 494
      // 7132: iastore
      // 7133: aload 0
      // 7134: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7137: bipush 30
      // 7139: aaload
      // 713a: bipush 11
      // 713c: sipush 493
      // 713f: iastore
      // 7140: aload 0
      // 7141: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7144: bipush 15
      // 7146: aaload
      // 7147: bipush 36
      // 7149: sipush 492
      // 714c: iastore
      // 714d: aload 0
      // 714e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7151: bipush 16
      // 7153: aaload
      // 7154: bipush 60
      // 7156: sipush 491
      // 7159: iastore
      // 715a: aload 0
      // 715b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 715e: bipush 24
      // 7160: aaload
      // 7161: bipush 45
      // 7163: sipush 490
      // 7166: iastore
      // 7167: aload 0
      // 7168: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 716b: bipush 37
      // 716d: aaload
      // 716e: bipush 35
      // 7170: sipush 489
      // 7173: iastore
      // 7174: aload 0
      // 7175: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7178: bipush 24
      // 717a: aaload
      // 717b: bipush 87
      // 717d: sipush 488
      // 7180: iastore
      // 7181: aload 0
      // 7182: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7185: bipush 20
      // 7187: aaload
      // 7188: bipush 45
      // 718a: sipush 487
      // 718d: iastore
      // 718e: aload 0
      // 718f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7192: bipush 31
      // 7194: aaload
      // 7195: bipush 90
      // 7197: sipush 486
      // 719a: iastore
      // 719b: aload 0
      // 719c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 719f: bipush 32
      // 71a1: aaload
      // 71a2: bipush 21
      // 71a4: sipush 485
      // 71a7: iastore
      // 71a8: aload 0
      // 71a9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 71ac: bipush 19
      // 71ae: aaload
      // 71af: bipush 70
      // 71b1: sipush 484
      // 71b4: iastore
      // 71b5: aload 0
      // 71b6: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 71b9: bipush 24
      // 71bb: aaload
      // 71bc: bipush 15
      // 71be: sipush 483
      // 71c1: iastore
      // 71c2: aload 0
      // 71c3: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 71c6: bipush 26
      // 71c8: aaload
      // 71c9: bipush 92
      // 71cb: sipush 482
      // 71ce: iastore
      // 71cf: aload 0
      // 71d0: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 71d3: bipush 37
      // 71d5: aaload
      // 71d6: bipush 13
      // 71d8: sipush 481
      // 71db: iastore
      // 71dc: aload 0
      // 71dd: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 71e0: bipush 39
      // 71e2: aaload
      // 71e3: bipush 2
      // 71e4: sipush 480
      // 71e7: iastore
      // 71e8: aload 0
      // 71e9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 71ec: bipush 23
      // 71ee: aaload
      // 71ef: bipush 70
      // 71f1: sipush 479
      // 71f4: iastore
      // 71f5: aload 0
      // 71f6: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 71f9: bipush 27
      // 71fb: aaload
      // 71fc: bipush 25
      // 71fe: sipush 478
      // 7201: iastore
      // 7202: aload 0
      // 7203: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7206: bipush 15
      // 7208: aaload
      // 7209: bipush 69
      // 720b: sipush 477
      // 720e: iastore
      // 720f: aload 0
      // 7210: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7213: bipush 19
      // 7215: aaload
      // 7216: bipush 61
      // 7218: sipush 476
      // 721b: iastore
      // 721c: aload 0
      // 721d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7220: bipush 31
      // 7222: aaload
      // 7223: bipush 58
      // 7225: sipush 475
      // 7228: iastore
      // 7229: aload 0
      // 722a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 722d: bipush 24
      // 722f: aaload
      // 7230: bipush 57
      // 7232: sipush 474
      // 7235: iastore
      // 7236: aload 0
      // 7237: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 723a: bipush 36
      // 723c: aaload
      // 723d: bipush 74
      // 723f: sipush 473
      // 7242: iastore
      // 7243: aload 0
      // 7244: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7247: bipush 21
      // 7249: aaload
      // 724a: bipush 6
      // 724c: sipush 472
      // 724f: iastore
      // 7250: aload 0
      // 7251: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7254: bipush 30
      // 7256: aaload
      // 7257: bipush 44
      // 7259: sipush 471
      // 725c: iastore
      // 725d: aload 0
      // 725e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7261: bipush 15
      // 7263: aaload
      // 7264: bipush 91
      // 7266: sipush 470
      // 7269: iastore
      // 726a: aload 0
      // 726b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 726e: bipush 27
      // 7270: aaload
      // 7271: bipush 16
      // 7273: sipush 469
      // 7276: iastore
      // 7277: aload 0
      // 7278: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 727b: bipush 29
      // 727d: aaload
      // 727e: bipush 42
      // 7280: sipush 468
      // 7283: iastore
      // 7284: aload 0
      // 7285: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7288: bipush 33
      // 728a: aaload
      // 728b: bipush 86
      // 728d: sipush 467
      // 7290: iastore
      // 7291: aload 0
      // 7292: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7295: bipush 29
      // 7297: aaload
      // 7298: bipush 41
      // 729a: sipush 466
      // 729d: iastore
      // 729e: aload 0
      // 729f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 72a2: bipush 20
      // 72a4: aaload
      // 72a5: bipush 68
      // 72a7: sipush 465
      // 72aa: iastore
      // 72ab: aload 0
      // 72ac: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 72af: bipush 25
      // 72b1: aaload
      // 72b2: bipush 47
      // 72b4: sipush 464
      // 72b7: iastore
      // 72b8: aload 0
      // 72b9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 72bc: bipush 22
      // 72be: aaload
      // 72bf: bipush 0
      // 72c0: sipush 463
      // 72c3: iastore
      // 72c4: aload 0
      // 72c5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 72c8: bipush 18
      // 72ca: aaload
      // 72cb: bipush 14
      // 72cd: sipush 462
      // 72d0: iastore
      // 72d1: aload 0
      // 72d2: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 72d5: bipush 31
      // 72d7: aaload
      // 72d8: bipush 28
      // 72da: sipush 461
      // 72dd: iastore
      // 72de: aload 0
      // 72df: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 72e2: bipush 15
      // 72e4: aaload
      // 72e5: bipush 2
      // 72e6: sipush 460
      // 72e9: iastore
      // 72ea: aload 0
      // 72eb: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 72ee: bipush 23
      // 72f0: aaload
      // 72f1: bipush 76
      // 72f3: sipush 459
      // 72f6: iastore
      // 72f7: aload 0
      // 72f8: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 72fb: bipush 38
      // 72fd: aaload
      // 72fe: bipush 32
      // 7300: sipush 458
      // 7303: iastore
      // 7304: aload 0
      // 7305: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7308: bipush 29
      // 730a: aaload
      // 730b: bipush 82
      // 730d: sipush 457
      // 7310: iastore
      // 7311: aload 0
      // 7312: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7315: bipush 21
      // 7317: aaload
      // 7318: bipush 86
      // 731a: sipush 456
      // 731d: iastore
      // 731e: aload 0
      // 731f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7322: bipush 24
      // 7324: aaload
      // 7325: bipush 62
      // 7327: sipush 455
      // 732a: iastore
      // 732b: aload 0
      // 732c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 732f: bipush 31
      // 7331: aaload
      // 7332: bipush 64
      // 7334: sipush 454
      // 7337: iastore
      // 7338: aload 0
      // 7339: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 733c: bipush 38
      // 733e: aaload
      // 733f: bipush 26
      // 7341: sipush 453
      // 7344: iastore
      // 7345: aload 0
      // 7346: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7349: bipush 32
      // 734b: aaload
      // 734c: bipush 86
      // 734e: sipush 452
      // 7351: iastore
      // 7352: aload 0
      // 7353: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7356: bipush 22
      // 7358: aaload
      // 7359: bipush 32
      // 735b: sipush 451
      // 735e: iastore
      // 735f: aload 0
      // 7360: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7363: bipush 19
      // 7365: aaload
      // 7366: bipush 59
      // 7368: sipush 450
      // 736b: iastore
      // 736c: aload 0
      // 736d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7370: bipush 34
      // 7372: aaload
      // 7373: bipush 18
      // 7375: sipush 449
      // 7378: iastore
      // 7379: aload 0
      // 737a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 737d: bipush 18
      // 737f: aaload
      // 7380: bipush 54
      // 7382: sipush 448
      // 7385: iastore
      // 7386: aload 0
      // 7387: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 738a: bipush 38
      // 738c: aaload
      // 738d: bipush 63
      // 738f: sipush 447
      // 7392: iastore
      // 7393: aload 0
      // 7394: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7397: bipush 36
      // 7399: aaload
      // 739a: bipush 23
      // 739c: sipush 446
      // 739f: iastore
      // 73a0: aload 0
      // 73a1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 73a4: bipush 35
      // 73a6: aaload
      // 73a7: bipush 35
      // 73a9: sipush 445
      // 73ac: iastore
      // 73ad: aload 0
      // 73ae: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 73b1: bipush 32
      // 73b3: aaload
      // 73b4: bipush 62
      // 73b6: sipush 444
      // 73b9: iastore
      // 73ba: aload 0
      // 73bb: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 73be: bipush 28
      // 73c0: aaload
      // 73c1: bipush 35
      // 73c3: sipush 443
      // 73c6: iastore
      // 73c7: aload 0
      // 73c8: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 73cb: bipush 27
      // 73cd: aaload
      // 73ce: bipush 13
      // 73d0: sipush 442
      // 73d3: iastore
      // 73d4: aload 0
      // 73d5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 73d8: bipush 31
      // 73da: aaload
      // 73db: bipush 59
      // 73dd: sipush 441
      // 73e0: iastore
      // 73e1: aload 0
      // 73e2: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 73e5: bipush 29
      // 73e7: aaload
      // 73e8: bipush 29
      // 73ea: sipush 440
      // 73ed: iastore
      // 73ee: aload 0
      // 73ef: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 73f2: bipush 15
      // 73f4: aaload
      // 73f5: bipush 64
      // 73f7: sipush 439
      // 73fa: iastore
      // 73fb: aload 0
      // 73fc: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 73ff: bipush 26
      // 7401: aaload
      // 7402: bipush 84
      // 7404: sipush 438
      // 7407: iastore
      // 7408: aload 0
      // 7409: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 740c: bipush 21
      // 740e: aaload
      // 740f: bipush 90
      // 7411: sipush 437
      // 7414: iastore
      // 7415: aload 0
      // 7416: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7419: bipush 20
      // 741b: aaload
      // 741c: bipush 24
      // 741e: sipush 436
      // 7421: iastore
      // 7422: aload 0
      // 7423: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7426: bipush 16
      // 7428: aaload
      // 7429: bipush 18
      // 742b: sipush 435
      // 742e: iastore
      // 742f: aload 0
      // 7430: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7433: bipush 22
      // 7435: aaload
      // 7436: bipush 23
      // 7438: sipush 434
      // 743b: iastore
      // 743c: aload 0
      // 743d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7440: bipush 31
      // 7442: aaload
      // 7443: bipush 14
      // 7445: sipush 433
      // 7448: iastore
      // 7449: aload 0
      // 744a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 744d: bipush 15
      // 744f: aaload
      // 7450: bipush 1
      // 7451: sipush 432
      // 7454: iastore
      // 7455: aload 0
      // 7456: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7459: bipush 18
      // 745b: aaload
      // 745c: bipush 63
      // 745e: sipush 431
      // 7461: iastore
      // 7462: aload 0
      // 7463: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7466: bipush 19
      // 7468: aaload
      // 7469: bipush 10
      // 746b: sipush 430
      // 746e: iastore
      // 746f: aload 0
      // 7470: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7473: bipush 25
      // 7475: aaload
      // 7476: bipush 49
      // 7478: sipush 429
      // 747b: iastore
      // 747c: aload 0
      // 747d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7480: bipush 36
      // 7482: aaload
      // 7483: bipush 57
      // 7485: sipush 428
      // 7488: iastore
      // 7489: aload 0
      // 748a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 748d: bipush 20
      // 748f: aaload
      // 7490: bipush 22
      // 7492: sipush 427
      // 7495: iastore
      // 7496: aload 0
      // 7497: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 749a: bipush 15
      // 749c: aaload
      // 749d: bipush 15
      // 749f: sipush 426
      // 74a2: iastore
      // 74a3: aload 0
      // 74a4: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 74a7: bipush 31
      // 74a9: aaload
      // 74aa: bipush 51
      // 74ac: sipush 425
      // 74af: iastore
      // 74b0: aload 0
      // 74b1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 74b4: bipush 24
      // 74b6: aaload
      // 74b7: bipush 60
      // 74b9: sipush 424
      // 74bc: iastore
      // 74bd: aload 0
      // 74be: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 74c1: bipush 31
      // 74c3: aaload
      // 74c4: bipush 70
      // 74c6: sipush 423
      // 74c9: iastore
      // 74ca: aload 0
      // 74cb: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 74ce: bipush 15
      // 74d0: aaload
      // 74d1: bipush 7
      // 74d3: sipush 422
      // 74d6: iastore
      // 74d7: aload 0
      // 74d8: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 74db: bipush 28
      // 74dd: aaload
      // 74de: bipush 40
      // 74e0: sipush 421
      // 74e3: iastore
      // 74e4: aload 0
      // 74e5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 74e8: bipush 18
      // 74ea: aaload
      // 74eb: bipush 41
      // 74ed: sipush 420
      // 74f0: iastore
      // 74f1: aload 0
      // 74f2: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 74f5: bipush 15
      // 74f7: aaload
      // 74f8: bipush 38
      // 74fa: sipush 419
      // 74fd: iastore
      // 74fe: aload 0
      // 74ff: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7502: bipush 32
      // 7504: aaload
      // 7505: bipush 0
      // 7506: sipush 418
      // 7509: iastore
      // 750a: aload 0
      // 750b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 750e: bipush 19
      // 7510: aaload
      // 7511: bipush 51
      // 7513: sipush 417
      // 7516: iastore
      // 7517: aload 0
      // 7518: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 751b: bipush 34
      // 751d: aaload
      // 751e: bipush 62
      // 7520: sipush 416
      // 7523: iastore
      // 7524: aload 0
      // 7525: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7528: bipush 16
      // 752a: aaload
      // 752b: bipush 27
      // 752d: sipush 415
      // 7530: iastore
      // 7531: aload 0
      // 7532: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7535: bipush 20
      // 7537: aaload
      // 7538: bipush 70
      // 753a: sipush 414
      // 753d: iastore
      // 753e: aload 0
      // 753f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7542: bipush 22
      // 7544: aaload
      // 7545: bipush 33
      // 7547: sipush 413
      // 754a: iastore
      // 754b: aload 0
      // 754c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 754f: bipush 26
      // 7551: aaload
      // 7552: bipush 73
      // 7554: sipush 412
      // 7557: iastore
      // 7558: aload 0
      // 7559: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 755c: bipush 20
      // 755e: aaload
      // 755f: bipush 79
      // 7561: sipush 411
      // 7564: iastore
      // 7565: aload 0
      // 7566: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7569: bipush 23
      // 756b: aaload
      // 756c: bipush 6
      // 756e: sipush 410
      // 7571: iastore
      // 7572: aload 0
      // 7573: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7576: bipush 24
      // 7578: aaload
      // 7579: bipush 85
      // 757b: sipush 409
      // 757e: iastore
      // 757f: aload 0
      // 7580: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7583: bipush 38
      // 7585: aaload
      // 7586: bipush 51
      // 7588: sipush 408
      // 758b: iastore
      // 758c: aload 0
      // 758d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7590: bipush 29
      // 7592: aaload
      // 7593: bipush 88
      // 7595: sipush 407
      // 7598: iastore
      // 7599: aload 0
      // 759a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 759d: bipush 38
      // 759f: aaload
      // 75a0: bipush 55
      // 75a2: sipush 406
      // 75a5: iastore
      // 75a6: aload 0
      // 75a7: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 75aa: bipush 32
      // 75ac: aaload
      // 75ad: bipush 32
      // 75af: sipush 405
      // 75b2: iastore
      // 75b3: aload 0
      // 75b4: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 75b7: bipush 27
      // 75b9: aaload
      // 75ba: bipush 18
      // 75bc: sipush 404
      // 75bf: iastore
      // 75c0: aload 0
      // 75c1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 75c4: bipush 23
      // 75c6: aaload
      // 75c7: bipush 87
      // 75c9: sipush 403
      // 75cc: iastore
      // 75cd: aload 0
      // 75ce: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 75d1: bipush 35
      // 75d3: aaload
      // 75d4: bipush 6
      // 75d6: sipush 402
      // 75d9: iastore
      // 75da: aload 0
      // 75db: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 75de: bipush 34
      // 75e0: aaload
      // 75e1: bipush 27
      // 75e3: sipush 401
      // 75e6: iastore
      // 75e7: aload 0
      // 75e8: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 75eb: bipush 39
      // 75ed: aaload
      // 75ee: bipush 35
      // 75f0: sipush 400
      // 75f3: iastore
      // 75f4: aload 0
      // 75f5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 75f8: bipush 30
      // 75fa: aaload
      // 75fb: bipush 88
      // 75fd: sipush 399
      // 7600: iastore
      // 7601: aload 0
      // 7602: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7605: bipush 32
      // 7607: aaload
      // 7608: bipush 92
      // 760a: sipush 398
      // 760d: iastore
      // 760e: aload 0
      // 760f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7612: bipush 32
      // 7614: aaload
      // 7615: bipush 49
      // 7617: sipush 397
      // 761a: iastore
      // 761b: aload 0
      // 761c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 761f: bipush 24
      // 7621: aaload
      // 7622: bipush 61
      // 7624: sipush 396
      // 7627: iastore
      // 7628: aload 0
      // 7629: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 762c: bipush 18
      // 762e: aaload
      // 762f: bipush 74
      // 7631: sipush 395
      // 7634: iastore
      // 7635: aload 0
      // 7636: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7639: bipush 23
      // 763b: aaload
      // 763c: bipush 77
      // 763e: sipush 394
      // 7641: iastore
      // 7642: aload 0
      // 7643: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7646: bipush 23
      // 7648: aaload
      // 7649: bipush 50
      // 764b: sipush 393
      // 764e: iastore
      // 764f: aload 0
      // 7650: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7653: bipush 23
      // 7655: aaload
      // 7656: bipush 32
      // 7658: sipush 392
      // 765b: iastore
      // 765c: aload 0
      // 765d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7660: bipush 23
      // 7662: aaload
      // 7663: bipush 36
      // 7665: sipush 391
      // 7668: iastore
      // 7669: aload 0
      // 766a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 766d: bipush 38
      // 766f: aaload
      // 7670: bipush 38
      // 7672: sipush 390
      // 7675: iastore
      // 7676: aload 0
      // 7677: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 767a: bipush 29
      // 767c: aaload
      // 767d: bipush 86
      // 767f: sipush 389
      // 7682: iastore
      // 7683: aload 0
      // 7684: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7687: bipush 36
      // 7689: aaload
      // 768a: bipush 15
      // 768c: sipush 388
      // 768f: iastore
      // 7690: aload 0
      // 7691: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7694: bipush 31
      // 7696: aaload
      // 7697: bipush 50
      // 7699: sipush 387
      // 769c: iastore
      // 769d: aload 0
      // 769e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 76a1: bipush 15
      // 76a3: aaload
      // 76a4: bipush 86
      // 76a6: sipush 386
      // 76a9: iastore
      // 76aa: aload 0
      // 76ab: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 76ae: bipush 39
      // 76b0: aaload
      // 76b1: bipush 13
      // 76b3: sipush 385
      // 76b6: iastore
      // 76b7: aload 0
      // 76b8: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 76bb: bipush 34
      // 76bd: aaload
      // 76be: bipush 26
      // 76c0: sipush 384
      // 76c3: iastore
      // 76c4: aload 0
      // 76c5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 76c8: bipush 19
      // 76ca: aaload
      // 76cb: bipush 34
      // 76cd: sipush 383
      // 76d0: iastore
      // 76d1: aload 0
      // 76d2: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 76d5: bipush 16
      // 76d7: aaload
      // 76d8: bipush 3
      // 76d9: sipush 382
      // 76dc: iastore
      // 76dd: aload 0
      // 76de: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 76e1: bipush 26
      // 76e3: aaload
      // 76e4: bipush 93
      // 76e6: sipush 381
      // 76e9: iastore
      // 76ea: aload 0
      // 76eb: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 76ee: bipush 19
      // 76f0: aaload
      // 76f1: bipush 67
      // 76f3: sipush 380
      // 76f6: iastore
      // 76f7: aload 0
      // 76f8: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 76fb: bipush 24
      // 76fd: aaload
      // 76fe: bipush 72
      // 7700: sipush 379
      // 7703: iastore
      // 7704: aload 0
      // 7705: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7708: bipush 29
      // 770a: aaload
      // 770b: bipush 17
      // 770d: sipush 378
      // 7710: iastore
      // 7711: aload 0
      // 7712: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7715: bipush 23
      // 7717: aaload
      // 7718: bipush 24
      // 771a: sipush 377
      // 771d: iastore
      // 771e: aload 0
      // 771f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7722: bipush 25
      // 7724: aaload
      // 7725: bipush 19
      // 7727: sipush 376
      // 772a: iastore
      // 772b: aload 0
      // 772c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 772f: bipush 18
      // 7731: aaload
      // 7732: bipush 65
      // 7734: sipush 375
      // 7737: iastore
      // 7738: aload 0
      // 7739: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 773c: bipush 30
      // 773e: aaload
      // 773f: bipush 78
      // 7741: sipush 374
      // 7744: iastore
      // 7745: aload 0
      // 7746: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7749: bipush 27
      // 774b: aaload
      // 774c: bipush 52
      // 774e: sipush 373
      // 7751: iastore
      // 7752: aload 0
      // 7753: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7756: bipush 22
      // 7758: aaload
      // 7759: bipush 18
      // 775b: sipush 372
      // 775e: iastore
      // 775f: aload 0
      // 7760: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7763: bipush 16
      // 7765: aaload
      // 7766: bipush 38
      // 7768: sipush 371
      // 776b: iastore
      // 776c: aload 0
      // 776d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7770: bipush 21
      // 7772: aaload
      // 7773: bipush 26
      // 7775: sipush 370
      // 7778: iastore
      // 7779: aload 0
      // 777a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 777d: bipush 34
      // 777f: aaload
      // 7780: bipush 20
      // 7782: sipush 369
      // 7785: iastore
      // 7786: aload 0
      // 7787: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 778a: bipush 15
      // 778c: aaload
      // 778d: bipush 42
      // 778f: sipush 368
      // 7792: iastore
      // 7793: aload 0
      // 7794: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7797: bipush 16
      // 7799: aaload
      // 779a: bipush 71
      // 779c: sipush 367
      // 779f: iastore
      // 77a0: aload 0
      // 77a1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 77a4: bipush 17
      // 77a6: aaload
      // 77a7: bipush 17
      // 77a9: sipush 366
      // 77ac: iastore
      // 77ad: aload 0
      // 77ae: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 77b1: bipush 24
      // 77b3: aaload
      // 77b4: bipush 71
      // 77b6: sipush 365
      // 77b9: iastore
      // 77ba: aload 0
      // 77bb: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 77be: bipush 18
      // 77c0: aaload
      // 77c1: bipush 84
      // 77c3: sipush 364
      // 77c6: iastore
      // 77c7: aload 0
      // 77c8: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 77cb: bipush 15
      // 77cd: aaload
      // 77ce: bipush 40
      // 77d0: sipush 363
      // 77d3: iastore
      // 77d4: aload 0
      // 77d5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 77d8: bipush 31
      // 77da: aaload
      // 77db: bipush 62
      // 77dd: sipush 362
      // 77e0: iastore
      // 77e1: aload 0
      // 77e2: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 77e5: bipush 15
      // 77e7: aaload
      // 77e8: bipush 8
      // 77ea: sipush 361
      // 77ed: iastore
      // 77ee: aload 0
      // 77ef: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 77f2: bipush 16
      // 77f4: aaload
      // 77f5: bipush 69
      // 77f7: sipush 360
      // 77fa: iastore
      // 77fb: aload 0
      // 77fc: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 77ff: bipush 29
      // 7801: aaload
      // 7802: bipush 79
      // 7804: sipush 359
      // 7807: iastore
      // 7808: aload 0
      // 7809: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 780c: bipush 38
      // 780e: aaload
      // 780f: bipush 91
      // 7811: sipush 358
      // 7814: iastore
      // 7815: aload 0
      // 7816: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7819: bipush 31
      // 781b: aaload
      // 781c: bipush 92
      // 781e: sipush 357
      // 7821: iastore
      // 7822: aload 0
      // 7823: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7826: bipush 20
      // 7828: aaload
      // 7829: bipush 77
      // 782b: sipush 356
      // 782e: iastore
      // 782f: aload 0
      // 7830: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7833: bipush 3
      // 7834: aaload
      // 7835: bipush 16
      // 7837: sipush 355
      // 783a: iastore
      // 783b: aload 0
      // 783c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 783f: bipush 27
      // 7841: aaload
      // 7842: bipush 87
      // 7844: sipush 354
      // 7847: iastore
      // 7848: aload 0
      // 7849: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 784c: bipush 16
      // 784e: aaload
      // 784f: bipush 25
      // 7851: sipush 353
      // 7854: iastore
      // 7855: aload 0
      // 7856: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7859: bipush 36
      // 785b: aaload
      // 785c: bipush 33
      // 785e: sipush 352
      // 7861: iastore
      // 7862: aload 0
      // 7863: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7866: bipush 37
      // 7868: aaload
      // 7869: bipush 76
      // 786b: sipush 351
      // 786e: iastore
      // 786f: aload 0
      // 7870: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7873: bipush 30
      // 7875: aaload
      // 7876: bipush 12
      // 7878: sipush 350
      // 787b: iastore
      // 787c: aload 0
      // 787d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7880: bipush 26
      // 7882: aaload
      // 7883: bipush 75
      // 7885: sipush 349
      // 7888: iastore
      // 7889: aload 0
      // 788a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 788d: bipush 25
      // 788f: aaload
      // 7890: bipush 14
      // 7892: sipush 348
      // 7895: iastore
      // 7896: aload 0
      // 7897: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 789a: bipush 32
      // 789c: aaload
      // 789d: bipush 26
      // 789f: sipush 347
      // 78a2: iastore
      // 78a3: aload 0
      // 78a4: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 78a7: bipush 23
      // 78a9: aaload
      // 78aa: bipush 22
      // 78ac: sipush 346
      // 78af: iastore
      // 78b0: aload 0
      // 78b1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 78b4: bipush 20
      // 78b6: aaload
      // 78b7: bipush 90
      // 78b9: sipush 345
      // 78bc: iastore
      // 78bd: aload 0
      // 78be: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 78c1: bipush 19
      // 78c3: aaload
      // 78c4: bipush 8
      // 78c6: sipush 344
      // 78c9: iastore
      // 78ca: aload 0
      // 78cb: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 78ce: bipush 38
      // 78d0: aaload
      // 78d1: bipush 41
      // 78d3: sipush 343
      // 78d6: iastore
      // 78d7: aload 0
      // 78d8: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 78db: bipush 34
      // 78dd: aaload
      // 78de: bipush 2
      // 78df: sipush 342
      // 78e2: iastore
      // 78e3: aload 0
      // 78e4: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 78e7: bipush 39
      // 78e9: aaload
      // 78ea: bipush 4
      // 78eb: sipush 341
      // 78ee: iastore
      // 78ef: aload 0
      // 78f0: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 78f3: bipush 27
      // 78f5: aaload
      // 78f6: bipush 89
      // 78f8: sipush 340
      // 78fb: iastore
      // 78fc: aload 0
      // 78fd: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7900: bipush 28
      // 7902: aaload
      // 7903: bipush 41
      // 7905: sipush 339
      // 7908: iastore
      // 7909: aload 0
      // 790a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 790d: bipush 28
      // 790f: aaload
      // 7910: bipush 44
      // 7912: sipush 338
      // 7915: iastore
      // 7916: aload 0
      // 7917: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 791a: bipush 24
      // 791c: aaload
      // 791d: bipush 92
      // 791f: sipush 337
      // 7922: iastore
      // 7923: aload 0
      // 7924: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7927: bipush 34
      // 7929: aaload
      // 792a: bipush 65
      // 792c: sipush 336
      // 792f: iastore
      // 7930: aload 0
      // 7931: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7934: bipush 39
      // 7936: aaload
      // 7937: bipush 14
      // 7939: sipush 335
      // 793c: iastore
      // 793d: aload 0
      // 793e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7941: bipush 21
      // 7943: aaload
      // 7944: bipush 38
      // 7946: sipush 334
      // 7949: iastore
      // 794a: aload 0
      // 794b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 794e: bipush 19
      // 7950: aaload
      // 7951: bipush 31
      // 7953: sipush 333
      // 7956: iastore
      // 7957: aload 0
      // 7958: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 795b: bipush 37
      // 795d: aaload
      // 795e: bipush 39
      // 7960: sipush 332
      // 7963: iastore
      // 7964: aload 0
      // 7965: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7968: bipush 33
      // 796a: aaload
      // 796b: bipush 41
      // 796d: sipush 331
      // 7970: iastore
      // 7971: aload 0
      // 7972: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7975: bipush 38
      // 7977: aaload
      // 7978: bipush 4
      // 7979: sipush 330
      // 797c: iastore
      // 797d: aload 0
      // 797e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7981: bipush 23
      // 7983: aaload
      // 7984: bipush 80
      // 7986: sipush 329
      // 7989: iastore
      // 798a: aload 0
      // 798b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 798e: bipush 25
      // 7990: aaload
      // 7991: bipush 24
      // 7993: sipush 328
      // 7996: iastore
      // 7997: aload 0
      // 7998: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 799b: bipush 37
      // 799d: aaload
      // 799e: bipush 17
      // 79a0: sipush 327
      // 79a3: iastore
      // 79a4: aload 0
      // 79a5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 79a8: bipush 22
      // 79aa: aaload
      // 79ab: bipush 16
      // 79ad: sipush 326
      // 79b0: iastore
      // 79b1: aload 0
      // 79b2: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 79b5: bipush 22
      // 79b7: aaload
      // 79b8: bipush 46
      // 79ba: sipush 325
      // 79bd: iastore
      // 79be: aload 0
      // 79bf: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 79c2: bipush 33
      // 79c4: aaload
      // 79c5: bipush 91
      // 79c7: sipush 324
      // 79ca: iastore
      // 79cb: aload 0
      // 79cc: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 79cf: bipush 24
      // 79d1: aaload
      // 79d2: bipush 89
      // 79d4: sipush 323
      // 79d7: iastore
      // 79d8: aload 0
      // 79d9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 79dc: bipush 30
      // 79de: aaload
      // 79df: bipush 52
      // 79e1: sipush 322
      // 79e4: iastore
      // 79e5: aload 0
      // 79e6: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 79e9: bipush 29
      // 79eb: aaload
      // 79ec: bipush 38
      // 79ee: sipush 321
      // 79f1: iastore
      // 79f2: aload 0
      // 79f3: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 79f6: bipush 38
      // 79f8: aaload
      // 79f9: bipush 85
      // 79fb: sipush 320
      // 79fe: iastore
      // 79ff: aload 0
      // 7a00: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7a03: bipush 15
      // 7a05: aaload
      // 7a06: bipush 12
      // 7a08: sipush 319
      // 7a0b: iastore
      // 7a0c: aload 0
      // 7a0d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7a10: bipush 27
      // 7a12: aaload
      // 7a13: bipush 58
      // 7a15: sipush 318
      // 7a18: iastore
      // 7a19: aload 0
      // 7a1a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7a1d: bipush 29
      // 7a1f: aaload
      // 7a20: bipush 52
      // 7a22: sipush 317
      // 7a25: iastore
      // 7a26: aload 0
      // 7a27: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7a2a: bipush 37
      // 7a2c: aaload
      // 7a2d: bipush 38
      // 7a2f: sipush 316
      // 7a32: iastore
      // 7a33: aload 0
      // 7a34: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7a37: bipush 34
      // 7a39: aaload
      // 7a3a: bipush 41
      // 7a3c: sipush 315
      // 7a3f: iastore
      // 7a40: aload 0
      // 7a41: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7a44: bipush 31
      // 7a46: aaload
      // 7a47: bipush 65
      // 7a49: sipush 314
      // 7a4c: iastore
      // 7a4d: aload 0
      // 7a4e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7a51: bipush 29
      // 7a53: aaload
      // 7a54: bipush 53
      // 7a56: sipush 313
      // 7a59: iastore
      // 7a5a: aload 0
      // 7a5b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7a5e: bipush 22
      // 7a60: aaload
      // 7a61: bipush 47
      // 7a63: sipush 312
      // 7a66: iastore
      // 7a67: aload 0
      // 7a68: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7a6b: bipush 22
      // 7a6d: aaload
      // 7a6e: bipush 19
      // 7a70: sipush 311
      // 7a73: iastore
      // 7a74: aload 0
      // 7a75: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7a78: bipush 26
      // 7a7a: aaload
      // 7a7b: bipush 0
      // 7a7c: sipush 310
      // 7a7f: iastore
      // 7a80: aload 0
      // 7a81: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7a84: bipush 37
      // 7a86: aaload
      // 7a87: bipush 86
      // 7a89: sipush 309
      // 7a8c: iastore
      // 7a8d: aload 0
      // 7a8e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7a91: bipush 35
      // 7a93: aaload
      // 7a94: bipush 4
      // 7a95: sipush 308
      // 7a98: iastore
      // 7a99: aload 0
      // 7a9a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7a9d: bipush 36
      // 7a9f: aaload
      // 7aa0: bipush 54
      // 7aa2: sipush 307
      // 7aa5: iastore
      // 7aa6: aload 0
      // 7aa7: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7aaa: bipush 20
      // 7aac: aaload
      // 7aad: bipush 76
      // 7aaf: sipush 306
      // 7ab2: iastore
      // 7ab3: aload 0
      // 7ab4: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7ab7: bipush 30
      // 7ab9: aaload
      // 7aba: bipush 9
      // 7abc: sipush 305
      // 7abf: iastore
      // 7ac0: aload 0
      // 7ac1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7ac4: bipush 30
      // 7ac6: aaload
      // 7ac7: bipush 33
      // 7ac9: sipush 304
      // 7acc: iastore
      // 7acd: aload 0
      // 7ace: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7ad1: bipush 23
      // 7ad3: aaload
      // 7ad4: bipush 17
      // 7ad6: sipush 303
      // 7ad9: iastore
      // 7ada: aload 0
      // 7adb: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7ade: bipush 23
      // 7ae0: aaload
      // 7ae1: bipush 33
      // 7ae3: sipush 302
      // 7ae6: iastore
      // 7ae7: aload 0
      // 7ae8: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7aeb: bipush 38
      // 7aed: aaload
      // 7aee: bipush 52
      // 7af0: sipush 301
      // 7af3: iastore
      // 7af4: aload 0
      // 7af5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7af8: bipush 15
      // 7afa: aaload
      // 7afb: bipush 19
      // 7afd: sipush 300
      // 7b00: iastore
      // 7b01: aload 0
      // 7b02: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7b05: bipush 28
      // 7b07: aaload
      // 7b08: bipush 45
      // 7b0a: sipush 299
      // 7b0d: iastore
      // 7b0e: aload 0
      // 7b0f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7b12: bipush 29
      // 7b14: aaload
      // 7b15: bipush 78
      // 7b17: sipush 298
      // 7b1a: iastore
      // 7b1b: aload 0
      // 7b1c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7b1f: bipush 23
      // 7b21: aaload
      // 7b22: bipush 15
      // 7b24: sipush 297
      // 7b27: iastore
      // 7b28: aload 0
      // 7b29: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7b2c: bipush 33
      // 7b2e: aaload
      // 7b2f: bipush 5
      // 7b30: sipush 296
      // 7b33: iastore
      // 7b34: aload 0
      // 7b35: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7b38: bipush 17
      // 7b3a: aaload
      // 7b3b: bipush 40
      // 7b3d: sipush 295
      // 7b40: iastore
      // 7b41: aload 0
      // 7b42: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7b45: bipush 30
      // 7b47: aaload
      // 7b48: bipush 83
      // 7b4a: sipush 294
      // 7b4d: iastore
      // 7b4e: aload 0
      // 7b4f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7b52: bipush 18
      // 7b54: aaload
      // 7b55: bipush 1
      // 7b56: sipush 293
      // 7b59: iastore
      // 7b5a: aload 0
      // 7b5b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7b5e: bipush 30
      // 7b60: aaload
      // 7b61: bipush 81
      // 7b63: sipush 292
      // 7b66: iastore
      // 7b67: aload 0
      // 7b68: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7b6b: bipush 19
      // 7b6d: aaload
      // 7b6e: bipush 40
      // 7b70: sipush 291
      // 7b73: iastore
      // 7b74: aload 0
      // 7b75: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7b78: bipush 24
      // 7b7a: aaload
      // 7b7b: bipush 47
      // 7b7d: sipush 290
      // 7b80: iastore
      // 7b81: aload 0
      // 7b82: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7b85: bipush 17
      // 7b87: aaload
      // 7b88: bipush 56
      // 7b8a: sipush 289
      // 7b8d: iastore
      // 7b8e: aload 0
      // 7b8f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7b92: bipush 39
      // 7b94: aaload
      // 7b95: bipush 80
      // 7b97: sipush 288
      // 7b9a: iastore
      // 7b9b: aload 0
      // 7b9c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7b9f: bipush 30
      // 7ba1: aaload
      // 7ba2: bipush 46
      // 7ba4: sipush 287
      // 7ba7: iastore
      // 7ba8: aload 0
      // 7ba9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7bac: bipush 16
      // 7bae: aaload
      // 7baf: bipush 61
      // 7bb1: sipush 286
      // 7bb4: iastore
      // 7bb5: aload 0
      // 7bb6: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7bb9: bipush 26
      // 7bbb: aaload
      // 7bbc: bipush 78
      // 7bbe: sipush 285
      // 7bc1: iastore
      // 7bc2: aload 0
      // 7bc3: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7bc6: bipush 26
      // 7bc8: aaload
      // 7bc9: bipush 57
      // 7bcb: sipush 284
      // 7bce: iastore
      // 7bcf: aload 0
      // 7bd0: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7bd3: bipush 20
      // 7bd5: aaload
      // 7bd6: bipush 46
      // 7bd8: sipush 283
      // 7bdb: iastore
      // 7bdc: aload 0
      // 7bdd: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7be0: bipush 25
      // 7be2: aaload
      // 7be3: bipush 15
      // 7be5: sipush 282
      // 7be8: iastore
      // 7be9: aload 0
      // 7bea: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7bed: bipush 25
      // 7bef: aaload
      // 7bf0: bipush 91
      // 7bf2: sipush 281
      // 7bf5: iastore
      // 7bf6: aload 0
      // 7bf7: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7bfa: bipush 21
      // 7bfc: aaload
      // 7bfd: bipush 83
      // 7bff: sipush 280
      // 7c02: iastore
      // 7c03: aload 0
      // 7c04: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7c07: bipush 30
      // 7c09: aaload
      // 7c0a: bipush 77
      // 7c0c: sipush 279
      // 7c0f: iastore
      // 7c10: aload 0
      // 7c11: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7c14: bipush 35
      // 7c16: aaload
      // 7c17: bipush 30
      // 7c19: sipush 278
      // 7c1c: iastore
      // 7c1d: aload 0
      // 7c1e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7c21: bipush 30
      // 7c23: aaload
      // 7c24: bipush 34
      // 7c26: sipush 277
      // 7c29: iastore
      // 7c2a: aload 0
      // 7c2b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7c2e: bipush 20
      // 7c30: aaload
      // 7c31: bipush 69
      // 7c33: sipush 276
      // 7c36: iastore
      // 7c37: aload 0
      // 7c38: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7c3b: bipush 35
      // 7c3d: aaload
      // 7c3e: bipush 10
      // 7c40: sipush 275
      // 7c43: iastore
      // 7c44: aload 0
      // 7c45: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7c48: bipush 29
      // 7c4a: aaload
      // 7c4b: bipush 70
      // 7c4d: sipush 274
      // 7c50: iastore
      // 7c51: aload 0
      // 7c52: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7c55: bipush 22
      // 7c57: aaload
      // 7c58: bipush 50
      // 7c5a: sipush 273
      // 7c5d: iastore
      // 7c5e: aload 0
      // 7c5f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7c62: bipush 18
      // 7c64: aaload
      // 7c65: bipush 0
      // 7c66: sipush 272
      // 7c69: iastore
      // 7c6a: aload 0
      // 7c6b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7c6e: bipush 22
      // 7c70: aaload
      // 7c71: bipush 64
      // 7c73: sipush 271
      // 7c76: iastore
      // 7c77: aload 0
      // 7c78: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7c7b: bipush 38
      // 7c7d: aaload
      // 7c7e: bipush 65
      // 7c80: sipush 270
      // 7c83: iastore
      // 7c84: aload 0
      // 7c85: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7c88: bipush 22
      // 7c8a: aaload
      // 7c8b: bipush 70
      // 7c8d: sipush 269
      // 7c90: iastore
      // 7c91: aload 0
      // 7c92: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7c95: bipush 24
      // 7c97: aaload
      // 7c98: bipush 58
      // 7c9a: sipush 268
      // 7c9d: iastore
      // 7c9e: aload 0
      // 7c9f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7ca2: bipush 19
      // 7ca4: aaload
      // 7ca5: bipush 66
      // 7ca7: sipush 267
      // 7caa: iastore
      // 7cab: aload 0
      // 7cac: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7caf: bipush 30
      // 7cb1: aaload
      // 7cb2: bipush 59
      // 7cb4: sipush 266
      // 7cb7: iastore
      // 7cb8: aload 0
      // 7cb9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7cbc: bipush 37
      // 7cbe: aaload
      // 7cbf: bipush 14
      // 7cc1: sipush 265
      // 7cc4: iastore
      // 7cc5: aload 0
      // 7cc6: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7cc9: bipush 16
      // 7ccb: aaload
      // 7ccc: bipush 56
      // 7cce: sipush 264
      // 7cd1: iastore
      // 7cd2: aload 0
      // 7cd3: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7cd6: bipush 29
      // 7cd8: aaload
      // 7cd9: bipush 85
      // 7cdb: sipush 263
      // 7cde: iastore
      // 7cdf: aload 0
      // 7ce0: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7ce3: bipush 31
      // 7ce5: aaload
      // 7ce6: bipush 15
      // 7ce8: sipush 262
      // 7ceb: iastore
      // 7cec: aload 0
      // 7ced: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7cf0: bipush 36
      // 7cf2: aaload
      // 7cf3: bipush 84
      // 7cf5: sipush 261
      // 7cf8: iastore
      // 7cf9: aload 0
      // 7cfa: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7cfd: bipush 39
      // 7cff: aaload
      // 7d00: bipush 15
      // 7d02: sipush 260
      // 7d05: iastore
      // 7d06: aload 0
      // 7d07: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7d0a: bipush 39
      // 7d0c: aaload
      // 7d0d: bipush 90
      // 7d0f: sipush 259
      // 7d12: iastore
      // 7d13: aload 0
      // 7d14: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7d17: bipush 18
      // 7d19: aaload
      // 7d1a: bipush 12
      // 7d1c: sipush 258
      // 7d1f: iastore
      // 7d20: aload 0
      // 7d21: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7d24: bipush 21
      // 7d26: aaload
      // 7d27: bipush 93
      // 7d29: sipush 257
      // 7d2c: iastore
      // 7d2d: aload 0
      // 7d2e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7d31: bipush 24
      // 7d33: aaload
      // 7d34: bipush 66
      // 7d36: sipush 256
      // 7d39: iastore
      // 7d3a: aload 0
      // 7d3b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7d3e: bipush 27
      // 7d40: aaload
      // 7d41: bipush 90
      // 7d43: sipush 255
      // 7d46: iastore
      // 7d47: aload 0
      // 7d48: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7d4b: bipush 25
      // 7d4d: aaload
      // 7d4e: bipush 90
      // 7d50: sipush 254
      // 7d53: iastore
      // 7d54: aload 0
      // 7d55: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7d58: bipush 22
      // 7d5a: aaload
      // 7d5b: bipush 24
      // 7d5d: sipush 253
      // 7d60: iastore
      // 7d61: aload 0
      // 7d62: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7d65: bipush 36
      // 7d67: aaload
      // 7d68: bipush 67
      // 7d6a: sipush 252
      // 7d6d: iastore
      // 7d6e: aload 0
      // 7d6f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7d72: bipush 33
      // 7d74: aaload
      // 7d75: bipush 90
      // 7d77: sipush 251
      // 7d7a: iastore
      // 7d7b: aload 0
      // 7d7c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7d7f: bipush 15
      // 7d81: aaload
      // 7d82: bipush 60
      // 7d84: sipush 250
      // 7d87: iastore
      // 7d88: aload 0
      // 7d89: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7d8c: bipush 23
      // 7d8e: aaload
      // 7d8f: bipush 85
      // 7d91: sipush 249
      // 7d94: iastore
      // 7d95: aload 0
      // 7d96: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7d99: bipush 34
      // 7d9b: aaload
      // 7d9c: bipush 1
      // 7d9d: sipush 248
      // 7da0: iastore
      // 7da1: aload 0
      // 7da2: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7da5: bipush 39
      // 7da7: aaload
      // 7da8: bipush 37
      // 7daa: sipush 247
      // 7dad: iastore
      // 7dae: aload 0
      // 7daf: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7db2: bipush 21
      // 7db4: aaload
      // 7db5: bipush 18
      // 7db7: sipush 246
      // 7dba: iastore
      // 7dbb: aload 0
      // 7dbc: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7dbf: bipush 34
      // 7dc1: aaload
      // 7dc2: bipush 4
      // 7dc3: sipush 245
      // 7dc6: iastore
      // 7dc7: aload 0
      // 7dc8: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7dcb: bipush 28
      // 7dcd: aaload
      // 7dce: bipush 33
      // 7dd0: sipush 244
      // 7dd3: iastore
      // 7dd4: aload 0
      // 7dd5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7dd8: bipush 15
      // 7dda: aaload
      // 7ddb: bipush 13
      // 7ddd: sipush 243
      // 7de0: iastore
      // 7de1: aload 0
      // 7de2: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7de5: bipush 32
      // 7de7: aaload
      // 7de8: bipush 22
      // 7dea: sipush 242
      // 7ded: iastore
      // 7dee: aload 0
      // 7def: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7df2: bipush 30
      // 7df4: aaload
      // 7df5: bipush 76
      // 7df7: sipush 241
      // 7dfa: iastore
      // 7dfb: aload 0
      // 7dfc: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7dff: bipush 20
      // 7e01: aaload
      // 7e02: bipush 21
      // 7e04: sipush 240
      // 7e07: iastore
      // 7e08: aload 0
      // 7e09: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7e0c: bipush 38
      // 7e0e: aaload
      // 7e0f: bipush 66
      // 7e11: sipush 239
      // 7e14: iastore
      // 7e15: aload 0
      // 7e16: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7e19: bipush 32
      // 7e1b: aaload
      // 7e1c: bipush 55
      // 7e1e: sipush 238
      // 7e21: iastore
      // 7e22: aload 0
      // 7e23: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7e26: bipush 32
      // 7e28: aaload
      // 7e29: bipush 89
      // 7e2b: sipush 237
      // 7e2e: iastore
      // 7e2f: aload 0
      // 7e30: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7e33: bipush 25
      // 7e35: aaload
      // 7e36: bipush 26
      // 7e38: sipush 236
      // 7e3b: iastore
      // 7e3c: aload 0
      // 7e3d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7e40: bipush 16
      // 7e42: aaload
      // 7e43: bipush 80
      // 7e45: sipush 235
      // 7e48: iastore
      // 7e49: aload 0
      // 7e4a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7e4d: bipush 15
      // 7e4f: aaload
      // 7e50: bipush 43
      // 7e52: sipush 234
      // 7e55: iastore
      // 7e56: aload 0
      // 7e57: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7e5a: bipush 38
      // 7e5c: aaload
      // 7e5d: bipush 54
      // 7e5f: sipush 233
      // 7e62: iastore
      // 7e63: aload 0
      // 7e64: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7e67: bipush 39
      // 7e69: aaload
      // 7e6a: bipush 68
      // 7e6c: sipush 232
      // 7e6f: iastore
      // 7e70: aload 0
      // 7e71: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7e74: bipush 22
      // 7e76: aaload
      // 7e77: bipush 88
      // 7e79: sipush 231
      // 7e7c: iastore
      // 7e7d: aload 0
      // 7e7e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7e81: bipush 21
      // 7e83: aaload
      // 7e84: bipush 84
      // 7e86: sipush 230
      // 7e89: iastore
      // 7e8a: aload 0
      // 7e8b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7e8e: bipush 21
      // 7e90: aaload
      // 7e91: bipush 17
      // 7e93: sipush 229
      // 7e96: iastore
      // 7e97: aload 0
      // 7e98: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7e9b: bipush 20
      // 7e9d: aaload
      // 7e9e: bipush 28
      // 7ea0: sipush 228
      // 7ea3: iastore
      // 7ea4: aload 0
      // 7ea5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7ea8: bipush 32
      // 7eaa: aaload
      // 7eab: bipush 1
      // 7eac: sipush 227
      // 7eaf: iastore
      // 7eb0: aload 0
      // 7eb1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7eb4: bipush 33
      // 7eb6: aaload
      // 7eb7: bipush 87
      // 7eb9: sipush 226
      // 7ebc: iastore
      // 7ebd: aload 0
      // 7ebe: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7ec1: bipush 38
      // 7ec3: aaload
      // 7ec4: bipush 71
      // 7ec6: sipush 225
      // 7ec9: iastore
      // 7eca: aload 0
      // 7ecb: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7ece: bipush 37
      // 7ed0: aaload
      // 7ed1: bipush 47
      // 7ed3: sipush 224
      // 7ed6: iastore
      // 7ed7: aload 0
      // 7ed8: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7edb: bipush 18
      // 7edd: aaload
      // 7ede: bipush 77
      // 7ee0: sipush 223
      // 7ee3: iastore
      // 7ee4: aload 0
      // 7ee5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7ee8: bipush 37
      // 7eea: aaload
      // 7eeb: bipush 58
      // 7eed: sipush 222
      // 7ef0: iastore
      // 7ef1: aload 0
      // 7ef2: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7ef5: bipush 34
      // 7ef7: aaload
      // 7ef8: bipush 74
      // 7efa: sipush 221
      // 7efd: iastore
      // 7efe: aload 0
      // 7eff: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7f02: bipush 32
      // 7f04: aaload
      // 7f05: bipush 54
      // 7f07: sipush 220
      // 7f0a: iastore
      // 7f0b: aload 0
      // 7f0c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7f0f: bipush 27
      // 7f11: aaload
      // 7f12: bipush 33
      // 7f14: sipush 219
      // 7f17: iastore
      // 7f18: aload 0
      // 7f19: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7f1c: bipush 32
      // 7f1e: aaload
      // 7f1f: bipush 93
      // 7f21: sipush 218
      // 7f24: iastore
      // 7f25: aload 0
      // 7f26: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7f29: bipush 23
      // 7f2b: aaload
      // 7f2c: bipush 51
      // 7f2e: sipush 217
      // 7f31: iastore
      // 7f32: aload 0
      // 7f33: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7f36: bipush 20
      // 7f38: aaload
      // 7f39: bipush 57
      // 7f3b: sipush 216
      // 7f3e: iastore
      // 7f3f: aload 0
      // 7f40: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7f43: bipush 22
      // 7f45: aaload
      // 7f46: bipush 37
      // 7f48: sipush 215
      // 7f4b: iastore
      // 7f4c: aload 0
      // 7f4d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7f50: bipush 39
      // 7f52: aaload
      // 7f53: bipush 10
      // 7f55: sipush 214
      // 7f58: iastore
      // 7f59: aload 0
      // 7f5a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7f5d: bipush 39
      // 7f5f: aaload
      // 7f60: bipush 17
      // 7f62: sipush 213
      // 7f65: iastore
      // 7f66: aload 0
      // 7f67: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7f6a: bipush 33
      // 7f6c: aaload
      // 7f6d: bipush 4
      // 7f6e: sipush 212
      // 7f71: iastore
      // 7f72: aload 0
      // 7f73: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7f76: bipush 32
      // 7f78: aaload
      // 7f79: bipush 84
      // 7f7b: sipush 211
      // 7f7e: iastore
      // 7f7f: aload 0
      // 7f80: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7f83: bipush 34
      // 7f85: aaload
      // 7f86: bipush 3
      // 7f87: sipush 210
      // 7f8a: iastore
      // 7f8b: aload 0
      // 7f8c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7f8f: bipush 28
      // 7f91: aaload
      // 7f92: bipush 27
      // 7f94: sipush 209
      // 7f97: iastore
      // 7f98: aload 0
      // 7f99: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7f9c: bipush 15
      // 7f9e: aaload
      // 7f9f: bipush 79
      // 7fa1: sipush 208
      // 7fa4: iastore
      // 7fa5: aload 0
      // 7fa6: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7fa9: bipush 34
      // 7fab: aaload
      // 7fac: bipush 21
      // 7fae: sipush 207
      // 7fb1: iastore
      // 7fb2: aload 0
      // 7fb3: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7fb6: bipush 34
      // 7fb8: aaload
      // 7fb9: bipush 69
      // 7fbb: sipush 206
      // 7fbe: iastore
      // 7fbf: aload 0
      // 7fc0: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7fc3: bipush 21
      // 7fc5: aaload
      // 7fc6: bipush 62
      // 7fc8: sipush 205
      // 7fcb: iastore
      // 7fcc: aload 0
      // 7fcd: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7fd0: bipush 36
      // 7fd2: aaload
      // 7fd3: bipush 24
      // 7fd5: sipush 204
      // 7fd8: iastore
      // 7fd9: aload 0
      // 7fda: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7fdd: bipush 16
      // 7fdf: aaload
      // 7fe0: bipush 89
      // 7fe2: sipush 203
      // 7fe5: iastore
      // 7fe6: aload 0
      // 7fe7: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7fea: bipush 18
      // 7fec: aaload
      // 7fed: bipush 48
      // 7fef: sipush 202
      // 7ff2: iastore
      // 7ff3: aload 0
      // 7ff4: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 7ff7: bipush 38
      // 7ff9: aaload
      // 7ffa: bipush 15
      // 7ffc: sipush 201
      // 7fff: iastore
      // 8000: aload 0
      // 8001: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8004: bipush 36
      // 8006: aaload
      // 8007: bipush 58
      // 8009: sipush 200
      // 800c: iastore
      // 800d: aload 0
      // 800e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8011: bipush 21
      // 8013: aaload
      // 8014: bipush 56
      // 8016: sipush 199
      // 8019: iastore
      // 801a: aload 0
      // 801b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 801e: bipush 34
      // 8020: aaload
      // 8021: bipush 48
      // 8023: sipush 198
      // 8026: iastore
      // 8027: aload 0
      // 8028: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 802b: bipush 21
      // 802d: aaload
      // 802e: bipush 15
      // 8030: sipush 197
      // 8033: iastore
      // 8034: aload 0
      // 8035: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8038: bipush 39
      // 803a: aaload
      // 803b: bipush 3
      // 803c: sipush 196
      // 803f: iastore
      // 8040: aload 0
      // 8041: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8044: bipush 16
      // 8046: aaload
      // 8047: bipush 44
      // 8049: sipush 195
      // 804c: iastore
      // 804d: aload 0
      // 804e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8051: bipush 18
      // 8053: aaload
      // 8054: bipush 79
      // 8056: sipush 194
      // 8059: iastore
      // 805a: aload 0
      // 805b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 805e: bipush 25
      // 8060: aaload
      // 8061: bipush 13
      // 8063: sipush 193
      // 8066: iastore
      // 8067: aload 0
      // 8068: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 806b: bipush 29
      // 806d: aaload
      // 806e: bipush 47
      // 8070: sipush 192
      // 8073: iastore
      // 8074: aload 0
      // 8075: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8078: bipush 38
      // 807a: aaload
      // 807b: bipush 88
      // 807d: sipush 191
      // 8080: iastore
      // 8081: aload 0
      // 8082: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8085: bipush 20
      // 8087: aaload
      // 8088: bipush 71
      // 808a: sipush 190
      // 808d: iastore
      // 808e: aload 0
      // 808f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8092: bipush 16
      // 8094: aaload
      // 8095: bipush 58
      // 8097: sipush 189
      // 809a: iastore
      // 809b: aload 0
      // 809c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 809f: bipush 35
      // 80a1: aaload
      // 80a2: bipush 57
      // 80a4: sipush 188
      // 80a7: iastore
      // 80a8: aload 0
      // 80a9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 80ac: bipush 29
      // 80ae: aaload
      // 80af: bipush 30
      // 80b1: sipush 187
      // 80b4: iastore
      // 80b5: aload 0
      // 80b6: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 80b9: bipush 29
      // 80bb: aaload
      // 80bc: bipush 23
      // 80be: sipush 186
      // 80c1: iastore
      // 80c2: aload 0
      // 80c3: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 80c6: bipush 34
      // 80c8: aaload
      // 80c9: bipush 93
      // 80cb: sipush 185
      // 80ce: iastore
      // 80cf: aload 0
      // 80d0: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 80d3: bipush 30
      // 80d5: aaload
      // 80d6: bipush 85
      // 80d8: sipush 184
      // 80db: iastore
      // 80dc: aload 0
      // 80dd: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 80e0: bipush 15
      // 80e2: aaload
      // 80e3: bipush 80
      // 80e5: sipush 183
      // 80e8: iastore
      // 80e9: aload 0
      // 80ea: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 80ed: bipush 32
      // 80ef: aaload
      // 80f0: bipush 78
      // 80f2: sipush 182
      // 80f5: iastore
      // 80f6: aload 0
      // 80f7: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 80fa: bipush 37
      // 80fc: aaload
      // 80fd: bipush 82
      // 80ff: sipush 181
      // 8102: iastore
      // 8103: aload 0
      // 8104: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8107: bipush 22
      // 8109: aaload
      // 810a: bipush 40
      // 810c: sipush 180
      // 810f: iastore
      // 8110: aload 0
      // 8111: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8114: bipush 21
      // 8116: aaload
      // 8117: bipush 69
      // 8119: sipush 179
      // 811c: iastore
      // 811d: aload 0
      // 811e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8121: bipush 26
      // 8123: aaload
      // 8124: bipush 85
      // 8126: sipush 178
      // 8129: iastore
      // 812a: aload 0
      // 812b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 812e: bipush 31
      // 8130: aaload
      // 8131: bipush 31
      // 8133: sipush 177
      // 8136: iastore
      // 8137: aload 0
      // 8138: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 813b: bipush 28
      // 813d: aaload
      // 813e: bipush 64
      // 8140: sipush 176
      // 8143: iastore
      // 8144: aload 0
      // 8145: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8148: bipush 38
      // 814a: aaload
      // 814b: bipush 13
      // 814d: sipush 175
      // 8150: iastore
      // 8151: aload 0
      // 8152: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8155: bipush 25
      // 8157: aaload
      // 8158: bipush 2
      // 8159: sipush 174
      // 815c: iastore
      // 815d: aload 0
      // 815e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8161: bipush 22
      // 8163: aaload
      // 8164: bipush 34
      // 8166: sipush 173
      // 8169: iastore
      // 816a: aload 0
      // 816b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 816e: bipush 28
      // 8170: aaload
      // 8171: bipush 28
      // 8173: sipush 172
      // 8176: iastore
      // 8177: aload 0
      // 8178: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 817b: bipush 24
      // 817d: aaload
      // 817e: bipush 91
      // 8180: sipush 171
      // 8183: iastore
      // 8184: aload 0
      // 8185: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8188: bipush 33
      // 818a: aaload
      // 818b: bipush 74
      // 818d: sipush 170
      // 8190: iastore
      // 8191: aload 0
      // 8192: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8195: bipush 29
      // 8197: aaload
      // 8198: bipush 40
      // 819a: sipush 169
      // 819d: iastore
      // 819e: aload 0
      // 819f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 81a2: bipush 15
      // 81a4: aaload
      // 81a5: bipush 77
      // 81a7: sipush 168
      // 81aa: iastore
      // 81ab: aload 0
      // 81ac: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 81af: bipush 32
      // 81b1: aaload
      // 81b2: bipush 80
      // 81b4: sipush 167
      // 81b7: iastore
      // 81b8: aload 0
      // 81b9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 81bc: bipush 30
      // 81be: aaload
      // 81bf: bipush 41
      // 81c1: sipush 166
      // 81c4: iastore
      // 81c5: aload 0
      // 81c6: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 81c9: bipush 23
      // 81cb: aaload
      // 81cc: bipush 30
      // 81ce: sipush 165
      // 81d1: iastore
      // 81d2: aload 0
      // 81d3: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 81d6: bipush 24
      // 81d8: aaload
      // 81d9: bipush 63
      // 81db: sipush 164
      // 81de: iastore
      // 81df: aload 0
      // 81e0: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 81e3: bipush 30
      // 81e5: aaload
      // 81e6: bipush 53
      // 81e8: sipush 163
      // 81eb: iastore
      // 81ec: aload 0
      // 81ed: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 81f0: bipush 39
      // 81f2: aaload
      // 81f3: bipush 70
      // 81f5: sipush 162
      // 81f8: iastore
      // 81f9: aload 0
      // 81fa: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 81fd: bipush 23
      // 81ff: aaload
      // 8200: bipush 61
      // 8202: sipush 161
      // 8205: iastore
      // 8206: aload 0
      // 8207: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 820a: bipush 37
      // 820c: aaload
      // 820d: bipush 27
      // 820f: sipush 160
      // 8212: iastore
      // 8213: aload 0
      // 8214: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8217: bipush 16
      // 8219: aaload
      // 821a: bipush 55
      // 821c: sipush 159
      // 821f: iastore
      // 8220: aload 0
      // 8221: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8224: bipush 22
      // 8226: aaload
      // 8227: bipush 74
      // 8229: sipush 158
      // 822c: iastore
      // 822d: aload 0
      // 822e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8231: bipush 26
      // 8233: aaload
      // 8234: bipush 50
      // 8236: sipush 157
      // 8239: iastore
      // 823a: aload 0
      // 823b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 823e: bipush 16
      // 8240: aaload
      // 8241: bipush 10
      // 8243: sipush 156
      // 8246: iastore
      // 8247: aload 0
      // 8248: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 824b: bipush 34
      // 824d: aaload
      // 824e: bipush 63
      // 8250: sipush 155
      // 8253: iastore
      // 8254: aload 0
      // 8255: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8258: bipush 35
      // 825a: aaload
      // 825b: bipush 14
      // 825d: sipush 154
      // 8260: iastore
      // 8261: aload 0
      // 8262: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8265: bipush 17
      // 8267: aaload
      // 8268: bipush 7
      // 826a: sipush 153
      // 826d: iastore
      // 826e: aload 0
      // 826f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8272: bipush 15
      // 8274: aaload
      // 8275: bipush 59
      // 8277: sipush 152
      // 827a: iastore
      // 827b: aload 0
      // 827c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 827f: bipush 27
      // 8281: aaload
      // 8282: bipush 23
      // 8284: sipush 151
      // 8287: iastore
      // 8288: aload 0
      // 8289: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 828c: bipush 18
      // 828e: aaload
      // 828f: bipush 70
      // 8291: sipush 150
      // 8294: iastore
      // 8295: aload 0
      // 8296: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8299: bipush 32
      // 829b: aaload
      // 829c: bipush 56
      // 829e: sipush 149
      // 82a1: iastore
      // 82a2: aload 0
      // 82a3: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 82a6: bipush 37
      // 82a8: aaload
      // 82a9: bipush 87
      // 82ab: sipush 148
      // 82ae: iastore
      // 82af: aload 0
      // 82b0: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 82b3: bipush 17
      // 82b5: aaload
      // 82b6: bipush 61
      // 82b8: sipush 147
      // 82bb: iastore
      // 82bc: aload 0
      // 82bd: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 82c0: bipush 18
      // 82c2: aaload
      // 82c3: bipush 83
      // 82c5: sipush 146
      // 82c8: iastore
      // 82c9: aload 0
      // 82ca: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 82cd: bipush 23
      // 82cf: aaload
      // 82d0: bipush 86
      // 82d2: sipush 145
      // 82d5: iastore
      // 82d6: aload 0
      // 82d7: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 82da: bipush 17
      // 82dc: aaload
      // 82dd: bipush 31
      // 82df: sipush 144
      // 82e2: iastore
      // 82e3: aload 0
      // 82e4: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 82e7: bipush 23
      // 82e9: aaload
      // 82ea: bipush 83
      // 82ec: sipush 143
      // 82ef: iastore
      // 82f0: aload 0
      // 82f1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 82f4: bipush 35
      // 82f6: aaload
      // 82f7: bipush 2
      // 82f8: sipush 142
      // 82fb: iastore
      // 82fc: aload 0
      // 82fd: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8300: bipush 18
      // 8302: aaload
      // 8303: bipush 64
      // 8305: sipush 141
      // 8308: iastore
      // 8309: aload 0
      // 830a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 830d: bipush 27
      // 830f: aaload
      // 8310: bipush 43
      // 8312: sipush 140
      // 8315: iastore
      // 8316: aload 0
      // 8317: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 831a: bipush 32
      // 831c: aaload
      // 831d: bipush 42
      // 831f: sipush 139
      // 8322: iastore
      // 8323: aload 0
      // 8324: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8327: bipush 25
      // 8329: aaload
      // 832a: bipush 76
      // 832c: sipush 138
      // 832f: iastore
      // 8330: aload 0
      // 8331: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8334: bipush 19
      // 8336: aaload
      // 8337: bipush 85
      // 8339: sipush 137
      // 833c: iastore
      // 833d: aload 0
      // 833e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8341: bipush 37
      // 8343: aaload
      // 8344: bipush 81
      // 8346: sipush 136
      // 8349: iastore
      // 834a: aload 0
      // 834b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 834e: bipush 38
      // 8350: aaload
      // 8351: bipush 83
      // 8353: sipush 135
      // 8356: iastore
      // 8357: aload 0
      // 8358: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 835b: bipush 35
      // 835d: aaload
      // 835e: bipush 7
      // 8360: sipush 134
      // 8363: iastore
      // 8364: aload 0
      // 8365: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8368: bipush 16
      // 836a: aaload
      // 836b: bipush 51
      // 836d: sipush 133
      // 8370: iastore
      // 8371: aload 0
      // 8372: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8375: bipush 27
      // 8377: aaload
      // 8378: bipush 22
      // 837a: sipush 132
      // 837d: iastore
      // 837e: aload 0
      // 837f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8382: bipush 16
      // 8384: aaload
      // 8385: bipush 76
      // 8387: sipush 131
      // 838a: iastore
      // 838b: aload 0
      // 838c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 838f: bipush 22
      // 8391: aaload
      // 8392: bipush 4
      // 8393: sipush 130
      // 8396: iastore
      // 8397: aload 0
      // 8398: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 839b: bipush 38
      // 839d: aaload
      // 839e: bipush 84
      // 83a0: sipush 129
      // 83a3: iastore
      // 83a4: aload 0
      // 83a5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 83a8: bipush 17
      // 83aa: aaload
      // 83ab: bipush 83
      // 83ad: sipush 128
      // 83b0: iastore
      // 83b1: aload 0
      // 83b2: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 83b5: bipush 24
      // 83b7: aaload
      // 83b8: bipush 46
      // 83ba: bipush 127
      // 83bc: iastore
      // 83bd: aload 0
      // 83be: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 83c1: bipush 33
      // 83c3: aaload
      // 83c4: bipush 15
      // 83c6: bipush 126
      // 83c8: iastore
      // 83c9: aload 0
      // 83ca: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 83cd: bipush 20
      // 83cf: aaload
      // 83d0: bipush 48
      // 83d2: bipush 125
      // 83d4: iastore
      // 83d5: aload 0
      // 83d6: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 83d9: bipush 17
      // 83db: aaload
      // 83dc: bipush 30
      // 83de: bipush 124
      // 83e0: iastore
      // 83e1: aload 0
      // 83e2: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 83e5: bipush 30
      // 83e7: aaload
      // 83e8: bipush 93
      // 83ea: bipush 123
      // 83ec: iastore
      // 83ed: aload 0
      // 83ee: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 83f1: bipush 28
      // 83f3: aaload
      // 83f4: bipush 11
      // 83f6: bipush 122
      // 83f8: iastore
      // 83f9: aload 0
      // 83fa: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 83fd: bipush 28
      // 83ff: aaload
      // 8400: bipush 30
      // 8402: bipush 121
      // 8404: iastore
      // 8405: aload 0
      // 8406: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8409: bipush 15
      // 840b: aaload
      // 840c: bipush 62
      // 840e: bipush 120
      // 8410: iastore
      // 8411: aload 0
      // 8412: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8415: bipush 17
      // 8417: aaload
      // 8418: bipush 87
      // 841a: bipush 119
      // 841c: iastore
      // 841d: aload 0
      // 841e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8421: bipush 32
      // 8423: aaload
      // 8424: bipush 81
      // 8426: bipush 118
      // 8428: iastore
      // 8429: aload 0
      // 842a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 842d: bipush 23
      // 842f: aaload
      // 8430: bipush 37
      // 8432: bipush 117
      // 8434: iastore
      // 8435: aload 0
      // 8436: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8439: bipush 30
      // 843b: aaload
      // 843c: bipush 22
      // 843e: bipush 116
      // 8440: iastore
      // 8441: aload 0
      // 8442: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8445: bipush 32
      // 8447: aaload
      // 8448: bipush 66
      // 844a: bipush 115
      // 844c: iastore
      // 844d: aload 0
      // 844e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8451: bipush 33
      // 8453: aaload
      // 8454: bipush 78
      // 8456: bipush 114
      // 8458: iastore
      // 8459: aload 0
      // 845a: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 845d: bipush 21
      // 845f: aaload
      // 8460: bipush 4
      // 8461: bipush 113
      // 8463: iastore
      // 8464: aload 0
      // 8465: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8468: bipush 31
      // 846a: aaload
      // 846b: bipush 17
      // 846d: bipush 112
      // 846f: iastore
      // 8470: aload 0
      // 8471: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8474: bipush 39
      // 8476: aaload
      // 8477: bipush 61
      // 8479: bipush 111
      // 847b: iastore
      // 847c: aload 0
      // 847d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8480: bipush 18
      // 8482: aaload
      // 8483: bipush 76
      // 8485: bipush 110
      // 8487: iastore
      // 8488: aload 0
      // 8489: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 848c: bipush 15
      // 848e: aaload
      // 848f: bipush 85
      // 8491: bipush 109
      // 8493: iastore
      // 8494: aload 0
      // 8495: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8498: bipush 31
      // 849a: aaload
      // 849b: bipush 47
      // 849d: bipush 108
      // 849f: iastore
      // 84a0: aload 0
      // 84a1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 84a4: bipush 19
      // 84a6: aaload
      // 84a7: bipush 57
      // 84a9: bipush 107
      // 84ab: iastore
      // 84ac: aload 0
      // 84ad: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 84b0: bipush 23
      // 84b2: aaload
      // 84b3: bipush 55
      // 84b5: bipush 106
      // 84b7: iastore
      // 84b8: aload 0
      // 84b9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 84bc: bipush 27
      // 84be: aaload
      // 84bf: bipush 29
      // 84c1: bipush 105
      // 84c3: iastore
      // 84c4: aload 0
      // 84c5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 84c8: bipush 29
      // 84ca: aaload
      // 84cb: bipush 46
      // 84cd: bipush 104
      // 84cf: iastore
      // 84d0: aload 0
      // 84d1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 84d4: bipush 33
      // 84d6: aaload
      // 84d7: bipush 0
      // 84d8: bipush 103
      // 84da: iastore
      // 84db: aload 0
      // 84dc: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 84df: bipush 16
      // 84e1: aaload
      // 84e2: bipush 83
      // 84e4: bipush 102
      // 84e6: iastore
      // 84e7: aload 0
      // 84e8: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 84eb: bipush 39
      // 84ed: aaload
      // 84ee: bipush 78
      // 84f0: bipush 101
      // 84f2: iastore
      // 84f3: aload 0
      // 84f4: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 84f7: bipush 32
      // 84f9: aaload
      // 84fa: bipush 77
      // 84fc: bipush 100
      // 84fe: iastore
      // 84ff: aload 0
      // 8500: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8503: bipush 36
      // 8505: aaload
      // 8506: bipush 25
      // 8508: bipush 99
      // 850a: iastore
      // 850b: aload 0
      // 850c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 850f: bipush 34
      // 8511: aaload
      // 8512: bipush 19
      // 8514: bipush 98
      // 8516: iastore
      // 8517: aload 0
      // 8518: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 851b: bipush 38
      // 851d: aaload
      // 851e: bipush 49
      // 8520: bipush 97
      // 8522: iastore
      // 8523: aload 0
      // 8524: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8527: bipush 19
      // 8529: aaload
      // 852a: bipush 25
      // 852c: bipush 96
      // 852e: iastore
      // 852f: aload 0
      // 8530: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8533: bipush 23
      // 8535: aaload
      // 8536: bipush 53
      // 8538: bipush 95
      // 853a: iastore
      // 853b: aload 0
      // 853c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 853f: bipush 28
      // 8541: aaload
      // 8542: bipush 43
      // 8544: bipush 94
      // 8546: iastore
      // 8547: aload 0
      // 8548: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 854b: bipush 31
      // 854d: aaload
      // 854e: bipush 44
      // 8550: bipush 93
      // 8552: iastore
      // 8553: aload 0
      // 8554: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8557: bipush 36
      // 8559: aaload
      // 855a: bipush 34
      // 855c: bipush 92
      // 855e: iastore
      // 855f: aload 0
      // 8560: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8563: bipush 16
      // 8565: aaload
      // 8566: bipush 34
      // 8568: bipush 91
      // 856a: iastore
      // 856b: aload 0
      // 856c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 856f: bipush 35
      // 8571: aaload
      // 8572: bipush 1
      // 8573: bipush 90
      // 8575: iastore
      // 8576: aload 0
      // 8577: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 857a: bipush 19
      // 857c: aaload
      // 857d: bipush 87
      // 857f: bipush 89
      // 8581: iastore
      // 8582: aload 0
      // 8583: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8586: bipush 18
      // 8588: aaload
      // 8589: bipush 53
      // 858b: bipush 88
      // 858d: iastore
      // 858e: aload 0
      // 858f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8592: bipush 29
      // 8594: aaload
      // 8595: bipush 54
      // 8597: bipush 87
      // 8599: iastore
      // 859a: aload 0
      // 859b: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 859e: bipush 22
      // 85a0: aaload
      // 85a1: bipush 41
      // 85a3: bipush 86
      // 85a5: iastore
      // 85a6: aload 0
      // 85a7: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 85aa: bipush 38
      // 85ac: aaload
      // 85ad: bipush 18
      // 85af: bipush 85
      // 85b1: iastore
      // 85b2: aload 0
      // 85b3: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 85b6: bipush 22
      // 85b8: aaload
      // 85b9: bipush 2
      // 85ba: bipush 84
      // 85bc: iastore
      // 85bd: aload 0
      // 85be: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 85c1: bipush 20
      // 85c3: aaload
      // 85c4: bipush 3
      // 85c5: bipush 83
      // 85c7: iastore
      // 85c8: aload 0
      // 85c9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 85cc: bipush 39
      // 85ce: aaload
      // 85cf: bipush 69
      // 85d1: bipush 82
      // 85d3: iastore
      // 85d4: aload 0
      // 85d5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 85d8: bipush 30
      // 85da: aaload
      // 85db: bipush 29
      // 85dd: bipush 81
      // 85df: iastore
      // 85e0: aload 0
      // 85e1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 85e4: bipush 28
      // 85e6: aaload
      // 85e7: bipush 19
      // 85e9: bipush 80
      // 85eb: iastore
      // 85ec: aload 0
      // 85ed: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 85f0: bipush 29
      // 85f2: aaload
      // 85f3: bipush 90
      // 85f5: bipush 79
      // 85f7: iastore
      // 85f8: aload 0
      // 85f9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 85fc: bipush 17
      // 85fe: aaload
      // 85ff: bipush 86
      // 8601: bipush 78
      // 8603: iastore
      // 8604: aload 0
      // 8605: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8608: bipush 15
      // 860a: aaload
      // 860b: bipush 9
      // 860d: bipush 77
      // 860f: iastore
      // 8610: aload 0
      // 8611: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8614: bipush 39
      // 8616: aaload
      // 8617: bipush 73
      // 8619: bipush 76
      // 861b: iastore
      // 861c: aload 0
      // 861d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8620: bipush 15
      // 8622: aaload
      // 8623: bipush 37
      // 8625: bipush 75
      // 8627: iastore
      // 8628: aload 0
      // 8629: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 862c: bipush 35
      // 862e: aaload
      // 862f: bipush 40
      // 8631: bipush 74
      // 8633: iastore
      // 8634: aload 0
      // 8635: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8638: bipush 33
      // 863a: aaload
      // 863b: bipush 77
      // 863d: bipush 73
      // 863f: iastore
      // 8640: aload 0
      // 8641: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8644: bipush 27
      // 8646: aaload
      // 8647: bipush 86
      // 8649: bipush 72
      // 864b: iastore
      // 864c: aload 0
      // 864d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8650: bipush 36
      // 8652: aaload
      // 8653: bipush 79
      // 8655: bipush 71
      // 8657: iastore
      // 8658: aload 0
      // 8659: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 865c: bipush 23
      // 865e: aaload
      // 865f: bipush 18
      // 8661: bipush 70
      // 8663: iastore
      // 8664: aload 0
      // 8665: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8668: bipush 34
      // 866a: aaload
      // 866b: bipush 87
      // 866d: bipush 69
      // 866f: iastore
      // 8670: aload 0
      // 8671: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8674: bipush 39
      // 8676: aaload
      // 8677: bipush 24
      // 8679: bipush 68
      // 867b: iastore
      // 867c: aload 0
      // 867d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8680: bipush 26
      // 8682: aaload
      // 8683: bipush 8
      // 8685: bipush 67
      // 8687: iastore
      // 8688: aload 0
      // 8689: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 868c: bipush 33
      // 868e: aaload
      // 868f: bipush 48
      // 8691: bipush 66
      // 8693: iastore
      // 8694: aload 0
      // 8695: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8698: bipush 39
      // 869a: aaload
      // 869b: bipush 30
      // 869d: bipush 65
      // 869f: iastore
      // 86a0: aload 0
      // 86a1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 86a4: bipush 33
      // 86a6: aaload
      // 86a7: bipush 28
      // 86a9: bipush 64
      // 86ab: iastore
      // 86ac: aload 0
      // 86ad: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 86b0: bipush 16
      // 86b2: aaload
      // 86b3: bipush 67
      // 86b5: bipush 63
      // 86b7: iastore
      // 86b8: aload 0
      // 86b9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 86bc: bipush 31
      // 86be: aaload
      // 86bf: bipush 78
      // 86c1: bipush 62
      // 86c3: iastore
      // 86c4: aload 0
      // 86c5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 86c8: bipush 32
      // 86ca: aaload
      // 86cb: bipush 23
      // 86cd: bipush 61
      // 86cf: iastore
      // 86d0: aload 0
      // 86d1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 86d4: bipush 24
      // 86d6: aaload
      // 86d7: bipush 55
      // 86d9: bipush 60
      // 86db: iastore
      // 86dc: aload 0
      // 86dd: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 86e0: bipush 30
      // 86e2: aaload
      // 86e3: bipush 68
      // 86e5: bipush 59
      // 86e7: iastore
      // 86e8: aload 0
      // 86e9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 86ec: bipush 18
      // 86ee: aaload
      // 86ef: bipush 60
      // 86f1: bipush 58
      // 86f3: iastore
      // 86f4: aload 0
      // 86f5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 86f8: bipush 15
      // 86fa: aaload
      // 86fb: bipush 17
      // 86fd: bipush 57
      // 86ff: iastore
      // 8700: aload 0
      // 8701: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8704: bipush 23
      // 8706: aaload
      // 8707: bipush 34
      // 8709: bipush 56
      // 870b: iastore
      // 870c: aload 0
      // 870d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8710: bipush 20
      // 8712: aaload
      // 8713: bipush 49
      // 8715: bipush 55
      // 8717: iastore
      // 8718: aload 0
      // 8719: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 871c: bipush 15
      // 871e: aaload
      // 871f: bipush 78
      // 8721: bipush 54
      // 8723: iastore
      // 8724: aload 0
      // 8725: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8728: bipush 24
      // 872a: aaload
      // 872b: bipush 14
      // 872d: bipush 53
      // 872f: iastore
      // 8730: aload 0
      // 8731: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8734: bipush 19
      // 8736: aaload
      // 8737: bipush 41
      // 8739: bipush 52
      // 873b: iastore
      // 873c: aload 0
      // 873d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8740: bipush 31
      // 8742: aaload
      // 8743: bipush 55
      // 8745: bipush 51
      // 8747: iastore
      // 8748: aload 0
      // 8749: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 874c: bipush 21
      // 874e: aaload
      // 874f: bipush 39
      // 8751: bipush 50
      // 8753: iastore
      // 8754: aload 0
      // 8755: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8758: bipush 35
      // 875a: aaload
      // 875b: bipush 9
      // 875d: bipush 49
      // 875f: iastore
      // 8760: aload 0
      // 8761: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8764: bipush 30
      // 8766: aaload
      // 8767: bipush 15
      // 8769: bipush 48
      // 876b: iastore
      // 876c: aload 0
      // 876d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8770: bipush 20
      // 8772: aaload
      // 8773: bipush 52
      // 8775: bipush 47
      // 8777: iastore
      // 8778: aload 0
      // 8779: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 877c: bipush 35
      // 877e: aaload
      // 877f: bipush 71
      // 8781: bipush 46
      // 8783: iastore
      // 8784: aload 0
      // 8785: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8788: bipush 20
      // 878a: aaload
      // 878b: bipush 7
      // 878d: bipush 45
      // 878f: iastore
      // 8790: aload 0
      // 8791: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8794: bipush 29
      // 8796: aaload
      // 8797: bipush 72
      // 8799: bipush 44
      // 879b: iastore
      // 879c: aload 0
      // 879d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 87a0: bipush 37
      // 87a2: aaload
      // 87a3: bipush 77
      // 87a5: bipush 43
      // 87a7: iastore
      // 87a8: aload 0
      // 87a9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 87ac: bipush 22
      // 87ae: aaload
      // 87af: bipush 35
      // 87b1: bipush 42
      // 87b3: iastore
      // 87b4: aload 0
      // 87b5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 87b8: bipush 20
      // 87ba: aaload
      // 87bb: bipush 61
      // 87bd: bipush 41
      // 87bf: iastore
      // 87c0: aload 0
      // 87c1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 87c4: bipush 31
      // 87c6: aaload
      // 87c7: bipush 60
      // 87c9: bipush 40
      // 87cb: iastore
      // 87cc: aload 0
      // 87cd: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 87d0: bipush 20
      // 87d2: aaload
      // 87d3: bipush 93
      // 87d5: bipush 39
      // 87d7: iastore
      // 87d8: aload 0
      // 87d9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 87dc: bipush 27
      // 87de: aaload
      // 87df: bipush 92
      // 87e1: bipush 38
      // 87e3: iastore
      // 87e4: aload 0
      // 87e5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 87e8: bipush 28
      // 87ea: aaload
      // 87eb: bipush 16
      // 87ed: bipush 37
      // 87ef: iastore
      // 87f0: aload 0
      // 87f1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 87f4: bipush 36
      // 87f6: aaload
      // 87f7: bipush 26
      // 87f9: bipush 36
      // 87fb: iastore
      // 87fc: aload 0
      // 87fd: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8800: bipush 18
      // 8802: aaload
      // 8803: bipush 89
      // 8805: bipush 35
      // 8807: iastore
      // 8808: aload 0
      // 8809: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 880c: bipush 21
      // 880e: aaload
      // 880f: bipush 63
      // 8811: bipush 34
      // 8813: iastore
      // 8814: aload 0
      // 8815: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8818: bipush 22
      // 881a: aaload
      // 881b: bipush 52
      // 881d: bipush 33
      // 881f: iastore
      // 8820: aload 0
      // 8821: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8824: bipush 24
      // 8826: aaload
      // 8827: bipush 65
      // 8829: bipush 32
      // 882b: iastore
      // 882c: aload 0
      // 882d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8830: bipush 31
      // 8832: aaload
      // 8833: bipush 8
      // 8835: bipush 31
      // 8837: iastore
      // 8838: aload 0
      // 8839: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 883c: bipush 31
      // 883e: aaload
      // 883f: bipush 49
      // 8841: bipush 30
      // 8843: iastore
      // 8844: aload 0
      // 8845: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8848: bipush 33
      // 884a: aaload
      // 884b: bipush 30
      // 884d: bipush 29
      // 884f: iastore
      // 8850: aload 0
      // 8851: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8854: bipush 37
      // 8856: aaload
      // 8857: bipush 15
      // 8859: bipush 28
      // 885b: iastore
      // 885c: aload 0
      // 885d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8860: bipush 18
      // 8862: aaload
      // 8863: bipush 18
      // 8865: bipush 27
      // 8867: iastore
      // 8868: aload 0
      // 8869: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 886c: bipush 25
      // 886e: aaload
      // 886f: bipush 50
      // 8871: bipush 26
      // 8873: iastore
      // 8874: aload 0
      // 8875: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8878: bipush 29
      // 887a: aaload
      // 887b: bipush 20
      // 887d: bipush 25
      // 887f: iastore
      // 8880: aload 0
      // 8881: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8884: bipush 35
      // 8886: aaload
      // 8887: bipush 48
      // 8889: bipush 24
      // 888b: iastore
      // 888c: aload 0
      // 888d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8890: bipush 38
      // 8892: aaload
      // 8893: bipush 75
      // 8895: bipush 23
      // 8897: iastore
      // 8898: aload 0
      // 8899: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 889c: bipush 26
      // 889e: aaload
      // 889f: bipush 83
      // 88a1: bipush 22
      // 88a3: iastore
      // 88a4: aload 0
      // 88a5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 88a8: bipush 21
      // 88aa: aaload
      // 88ab: bipush 87
      // 88ad: bipush 21
      // 88af: iastore
      // 88b0: aload 0
      // 88b1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 88b4: bipush 27
      // 88b6: aaload
      // 88b7: bipush 71
      // 88b9: bipush 20
      // 88bb: iastore
      // 88bc: aload 0
      // 88bd: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 88c0: bipush 32
      // 88c2: aaload
      // 88c3: bipush 91
      // 88c5: bipush 19
      // 88c7: iastore
      // 88c8: aload 0
      // 88c9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 88cc: bipush 25
      // 88ce: aaload
      // 88cf: bipush 73
      // 88d1: bipush 18
      // 88d3: iastore
      // 88d4: aload 0
      // 88d5: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 88d8: bipush 16
      // 88da: aaload
      // 88db: bipush 84
      // 88dd: bipush 17
      // 88df: iastore
      // 88e0: aload 0
      // 88e1: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 88e4: bipush 25
      // 88e6: aaload
      // 88e7: bipush 31
      // 88e9: bipush 16
      // 88eb: iastore
      // 88ec: aload 0
      // 88ed: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 88f0: bipush 17
      // 88f2: aaload
      // 88f3: bipush 90
      // 88f5: bipush 15
      // 88f7: iastore
      // 88f8: aload 0
      // 88f9: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 88fc: bipush 18
      // 88fe: aaload
      // 88ff: bipush 40
      // 8901: bipush 14
      // 8903: iastore
      // 8904: aload 0
      // 8905: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8908: bipush 17
      // 890a: aaload
      // 890b: bipush 77
      // 890d: bipush 13
      // 890f: iastore
      // 8910: aload 0
      // 8911: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8914: bipush 17
      // 8916: aaload
      // 8917: bipush 35
      // 8919: bipush 12
      // 891b: iastore
      // 891c: aload 0
      // 891d: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8920: bipush 23
      // 8922: aaload
      // 8923: bipush 52
      // 8925: bipush 11
      // 8927: iastore
      // 8928: aload 0
      // 8929: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 892c: bipush 23
      // 892e: aaload
      // 892f: bipush 35
      // 8931: bipush 10
      // 8933: iastore
      // 8934: aload 0
      // 8935: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8938: bipush 16
      // 893a: aaload
      // 893b: bipush 5
      // 893c: bipush 9
      // 893e: iastore
      // 893f: aload 0
      // 8940: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8943: bipush 23
      // 8945: aaload
      // 8946: bipush 58
      // 8948: bipush 8
      // 894a: iastore
      // 894b: aload 0
      // 894c: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 894f: bipush 19
      // 8951: aaload
      // 8952: bipush 60
      // 8954: bipush 7
      // 8956: iastore
      // 8957: aload 0
      // 8958: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 895b: bipush 30
      // 895d: aaload
      // 895e: bipush 32
      // 8960: bipush 6
      // 8962: iastore
      // 8963: aload 0
      // 8964: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8967: bipush 38
      // 8969: aaload
      // 896a: bipush 34
      // 896c: bipush 5
      // 896d: iastore
      // 896e: aload 0
      // 896f: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8972: bipush 23
      // 8974: aaload
      // 8975: bipush 4
      // 8976: bipush 4
      // 8977: iastore
      // 8978: aload 0
      // 8979: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 897c: bipush 23
      // 897e: aaload
      // 897f: bipush 1
      // 8980: bipush 3
      // 8981: iastore
      // 8982: aload 0
      // 8983: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8986: bipush 27
      // 8988: aaload
      // 8989: bipush 57
      // 898b: bipush 2
      // 898c: iastore
      // 898d: aload 0
      // 898e: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 8991: bipush 39
      // 8993: aaload
      // 8994: bipush 38
      // 8996: bipush 1
      // 8997: iastore
      // 8998: aload 0
      // 8999: getfield io/legado/app/help/BytesEncodingDetect.KRFreq [[I
      // 899c: bipush 32
      // 899e: aaload
      // 899f: bipush 33
      // 89a1: bipush 0
      // 89a2: iastore
      // 89a3: aload 0
      // 89a4: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 89a7: bipush 3
      // 89a8: aaload
      // 89a9: bipush 74
      // 89ab: sipush 600
      // 89ae: iastore
      // 89af: aload 0
      // 89b0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 89b3: bipush 3
      // 89b4: aaload
      // 89b5: bipush 45
      // 89b7: sipush 599
      // 89ba: iastore
      // 89bb: aload 0
      // 89bc: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 89bf: bipush 3
      // 89c0: aaload
      // 89c1: bipush 3
      // 89c2: sipush 598
      // 89c5: iastore
      // 89c6: aload 0
      // 89c7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 89ca: bipush 3
      // 89cb: aaload
      // 89cc: bipush 24
      // 89ce: sipush 597
      // 89d1: iastore
      // 89d2: aload 0
      // 89d3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 89d6: bipush 3
      // 89d7: aaload
      // 89d8: bipush 30
      // 89da: sipush 596
      // 89dd: iastore
      // 89de: aload 0
      // 89df: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 89e2: bipush 3
      // 89e3: aaload
      // 89e4: bipush 42
      // 89e6: sipush 595
      // 89e9: iastore
      // 89ea: aload 0
      // 89eb: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 89ee: bipush 3
      // 89ef: aaload
      // 89f0: bipush 46
      // 89f2: sipush 594
      // 89f5: iastore
      // 89f6: aload 0
      // 89f7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 89fa: bipush 3
      // 89fb: aaload
      // 89fc: bipush 39
      // 89fe: sipush 593
      // 8a01: iastore
      // 8a02: aload 0
      // 8a03: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8a06: bipush 3
      // 8a07: aaload
      // 8a08: bipush 11
      // 8a0a: sipush 592
      // 8a0d: iastore
      // 8a0e: aload 0
      // 8a0f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8a12: bipush 3
      // 8a13: aaload
      // 8a14: bipush 37
      // 8a16: sipush 591
      // 8a19: iastore
      // 8a1a: aload 0
      // 8a1b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8a1e: bipush 3
      // 8a1f: aaload
      // 8a20: bipush 38
      // 8a22: sipush 590
      // 8a25: iastore
      // 8a26: aload 0
      // 8a27: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8a2a: bipush 3
      // 8a2b: aaload
      // 8a2c: bipush 31
      // 8a2e: sipush 589
      // 8a31: iastore
      // 8a32: aload 0
      // 8a33: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8a36: bipush 3
      // 8a37: aaload
      // 8a38: bipush 41
      // 8a3a: sipush 588
      // 8a3d: iastore
      // 8a3e: aload 0
      // 8a3f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8a42: bipush 3
      // 8a43: aaload
      // 8a44: bipush 5
      // 8a45: sipush 587
      // 8a48: iastore
      // 8a49: aload 0
      // 8a4a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8a4d: bipush 3
      // 8a4e: aaload
      // 8a4f: bipush 10
      // 8a51: sipush 586
      // 8a54: iastore
      // 8a55: aload 0
      // 8a56: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8a59: bipush 3
      // 8a5a: aaload
      // 8a5b: bipush 75
      // 8a5d: sipush 585
      // 8a60: iastore
      // 8a61: aload 0
      // 8a62: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8a65: bipush 3
      // 8a66: aaload
      // 8a67: bipush 65
      // 8a69: sipush 584
      // 8a6c: iastore
      // 8a6d: aload 0
      // 8a6e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8a71: bipush 3
      // 8a72: aaload
      // 8a73: bipush 72
      // 8a75: sipush 583
      // 8a78: iastore
      // 8a79: aload 0
      // 8a7a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8a7d: bipush 37
      // 8a7f: aaload
      // 8a80: bipush 91
      // 8a82: sipush 582
      // 8a85: iastore
      // 8a86: aload 0
      // 8a87: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8a8a: bipush 0
      // 8a8b: aaload
      // 8a8c: bipush 27
      // 8a8e: sipush 581
      // 8a91: iastore
      // 8a92: aload 0
      // 8a93: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8a96: bipush 3
      // 8a97: aaload
      // 8a98: bipush 18
      // 8a9a: sipush 580
      // 8a9d: iastore
      // 8a9e: aload 0
      // 8a9f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8aa2: bipush 3
      // 8aa3: aaload
      // 8aa4: bipush 22
      // 8aa6: sipush 579
      // 8aa9: iastore
      // 8aaa: aload 0
      // 8aab: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8aae: bipush 3
      // 8aaf: aaload
      // 8ab0: bipush 61
      // 8ab2: sipush 578
      // 8ab5: iastore
      // 8ab6: aload 0
      // 8ab7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8aba: bipush 3
      // 8abb: aaload
      // 8abc: bipush 14
      // 8abe: sipush 577
      // 8ac1: iastore
      // 8ac2: aload 0
      // 8ac3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8ac6: bipush 24
      // 8ac8: aaload
      // 8ac9: bipush 80
      // 8acb: sipush 576
      // 8ace: iastore
      // 8acf: aload 0
      // 8ad0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8ad3: bipush 4
      // 8ad4: aaload
      // 8ad5: bipush 82
      // 8ad7: sipush 575
      // 8ada: iastore
      // 8adb: aload 0
      // 8adc: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8adf: bipush 17
      // 8ae1: aaload
      // 8ae2: bipush 80
      // 8ae4: sipush 574
      // 8ae7: iastore
      // 8ae8: aload 0
      // 8ae9: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8aec: bipush 30
      // 8aee: aaload
      // 8aef: bipush 44
      // 8af1: sipush 573
      // 8af4: iastore
      // 8af5: aload 0
      // 8af6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8af9: bipush 3
      // 8afa: aaload
      // 8afb: bipush 73
      // 8afd: sipush 572
      // 8b00: iastore
      // 8b01: aload 0
      // 8b02: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8b05: bipush 3
      // 8b06: aaload
      // 8b07: bipush 64
      // 8b09: sipush 571
      // 8b0c: iastore
      // 8b0d: aload 0
      // 8b0e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8b11: bipush 38
      // 8b13: aaload
      // 8b14: bipush 14
      // 8b16: sipush 570
      // 8b19: iastore
      // 8b1a: aload 0
      // 8b1b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8b1e: bipush 33
      // 8b20: aaload
      // 8b21: bipush 70
      // 8b23: sipush 569
      // 8b26: iastore
      // 8b27: aload 0
      // 8b28: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8b2b: bipush 3
      // 8b2c: aaload
      // 8b2d: bipush 1
      // 8b2e: sipush 568
      // 8b31: iastore
      // 8b32: aload 0
      // 8b33: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8b36: bipush 3
      // 8b37: aaload
      // 8b38: bipush 16
      // 8b3a: sipush 567
      // 8b3d: iastore
      // 8b3e: aload 0
      // 8b3f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8b42: bipush 3
      // 8b43: aaload
      // 8b44: bipush 35
      // 8b46: sipush 566
      // 8b49: iastore
      // 8b4a: aload 0
      // 8b4b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8b4e: bipush 3
      // 8b4f: aaload
      // 8b50: bipush 40
      // 8b52: sipush 565
      // 8b55: iastore
      // 8b56: aload 0
      // 8b57: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8b5a: bipush 4
      // 8b5b: aaload
      // 8b5c: bipush 74
      // 8b5e: sipush 564
      // 8b61: iastore
      // 8b62: aload 0
      // 8b63: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8b66: bipush 4
      // 8b67: aaload
      // 8b68: bipush 24
      // 8b6a: sipush 563
      // 8b6d: iastore
      // 8b6e: aload 0
      // 8b6f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8b72: bipush 42
      // 8b74: aaload
      // 8b75: bipush 59
      // 8b77: sipush 562
      // 8b7a: iastore
      // 8b7b: aload 0
      // 8b7c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8b7f: bipush 3
      // 8b80: aaload
      // 8b81: bipush 7
      // 8b83: sipush 561
      // 8b86: iastore
      // 8b87: aload 0
      // 8b88: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8b8b: bipush 3
      // 8b8c: aaload
      // 8b8d: bipush 71
      // 8b8f: sipush 560
      // 8b92: iastore
      // 8b93: aload 0
      // 8b94: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8b97: bipush 3
      // 8b98: aaload
      // 8b99: bipush 12
      // 8b9b: sipush 559
      // 8b9e: iastore
      // 8b9f: aload 0
      // 8ba0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8ba3: bipush 15
      // 8ba5: aaload
      // 8ba6: bipush 75
      // 8ba8: sipush 558
      // 8bab: iastore
      // 8bac: aload 0
      // 8bad: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8bb0: bipush 3
      // 8bb1: aaload
      // 8bb2: bipush 20
      // 8bb4: sipush 557
      // 8bb7: iastore
      // 8bb8: aload 0
      // 8bb9: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8bbc: bipush 4
      // 8bbd: aaload
      // 8bbe: bipush 39
      // 8bc0: sipush 556
      // 8bc3: iastore
      // 8bc4: aload 0
      // 8bc5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8bc8: bipush 34
      // 8bca: aaload
      // 8bcb: bipush 69
      // 8bcd: sipush 555
      // 8bd0: iastore
      // 8bd1: aload 0
      // 8bd2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8bd5: bipush 3
      // 8bd6: aaload
      // 8bd7: bipush 28
      // 8bd9: sipush 554
      // 8bdc: iastore
      // 8bdd: aload 0
      // 8bde: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8be1: bipush 35
      // 8be3: aaload
      // 8be4: bipush 24
      // 8be6: sipush 553
      // 8be9: iastore
      // 8bea: aload 0
      // 8beb: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8bee: bipush 3
      // 8bef: aaload
      // 8bf0: bipush 82
      // 8bf2: sipush 552
      // 8bf5: iastore
      // 8bf6: aload 0
      // 8bf7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8bfa: bipush 28
      // 8bfc: aaload
      // 8bfd: bipush 47
      // 8bff: sipush 551
      // 8c02: iastore
      // 8c03: aload 0
      // 8c04: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8c07: bipush 3
      // 8c08: aaload
      // 8c09: bipush 67
      // 8c0b: sipush 550
      // 8c0e: iastore
      // 8c0f: aload 0
      // 8c10: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8c13: bipush 37
      // 8c15: aaload
      // 8c16: bipush 16
      // 8c18: sipush 549
      // 8c1b: iastore
      // 8c1c: aload 0
      // 8c1d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8c20: bipush 26
      // 8c22: aaload
      // 8c23: bipush 93
      // 8c25: sipush 548
      // 8c28: iastore
      // 8c29: aload 0
      // 8c2a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8c2d: bipush 4
      // 8c2e: aaload
      // 8c2f: bipush 1
      // 8c30: sipush 547
      // 8c33: iastore
      // 8c34: aload 0
      // 8c35: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8c38: bipush 26
      // 8c3a: aaload
      // 8c3b: bipush 85
      // 8c3d: sipush 546
      // 8c40: iastore
      // 8c41: aload 0
      // 8c42: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8c45: bipush 31
      // 8c47: aaload
      // 8c48: bipush 14
      // 8c4a: sipush 545
      // 8c4d: iastore
      // 8c4e: aload 0
      // 8c4f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8c52: bipush 4
      // 8c53: aaload
      // 8c54: bipush 3
      // 8c55: sipush 544
      // 8c58: iastore
      // 8c59: aload 0
      // 8c5a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8c5d: bipush 4
      // 8c5e: aaload
      // 8c5f: bipush 72
      // 8c61: sipush 543
      // 8c64: iastore
      // 8c65: aload 0
      // 8c66: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8c69: bipush 24
      // 8c6b: aaload
      // 8c6c: bipush 51
      // 8c6e: sipush 542
      // 8c71: iastore
      // 8c72: aload 0
      // 8c73: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8c76: bipush 27
      // 8c78: aaload
      // 8c79: bipush 51
      // 8c7b: sipush 541
      // 8c7e: iastore
      // 8c7f: aload 0
      // 8c80: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8c83: bipush 27
      // 8c85: aaload
      // 8c86: bipush 49
      // 8c88: sipush 540
      // 8c8b: iastore
      // 8c8c: aload 0
      // 8c8d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8c90: bipush 22
      // 8c92: aaload
      // 8c93: bipush 77
      // 8c95: sipush 539
      // 8c98: iastore
      // 8c99: aload 0
      // 8c9a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8c9d: bipush 27
      // 8c9f: aaload
      // 8ca0: bipush 10
      // 8ca2: sipush 538
      // 8ca5: iastore
      // 8ca6: aload 0
      // 8ca7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8caa: bipush 29
      // 8cac: aaload
      // 8cad: bipush 68
      // 8caf: sipush 537
      // 8cb2: iastore
      // 8cb3: aload 0
      // 8cb4: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8cb7: bipush 20
      // 8cb9: aaload
      // 8cba: bipush 35
      // 8cbc: sipush 536
      // 8cbf: iastore
      // 8cc0: aload 0
      // 8cc1: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8cc4: bipush 41
      // 8cc6: aaload
      // 8cc7: bipush 11
      // 8cc9: sipush 535
      // 8ccc: iastore
      // 8ccd: aload 0
      // 8cce: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8cd1: bipush 24
      // 8cd3: aaload
      // 8cd4: bipush 70
      // 8cd6: sipush 534
      // 8cd9: iastore
      // 8cda: aload 0
      // 8cdb: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8cde: bipush 36
      // 8ce0: aaload
      // 8ce1: bipush 61
      // 8ce3: sipush 533
      // 8ce6: iastore
      // 8ce7: aload 0
      // 8ce8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8ceb: bipush 31
      // 8ced: aaload
      // 8cee: bipush 23
      // 8cf0: sipush 532
      // 8cf3: iastore
      // 8cf4: aload 0
      // 8cf5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8cf8: bipush 43
      // 8cfa: aaload
      // 8cfb: bipush 16
      // 8cfd: sipush 531
      // 8d00: iastore
      // 8d01: aload 0
      // 8d02: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8d05: bipush 23
      // 8d07: aaload
      // 8d08: bipush 68
      // 8d0a: sipush 530
      // 8d0d: iastore
      // 8d0e: aload 0
      // 8d0f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8d12: bipush 32
      // 8d14: aaload
      // 8d15: bipush 15
      // 8d17: sipush 529
      // 8d1a: iastore
      // 8d1b: aload 0
      // 8d1c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8d1f: bipush 3
      // 8d20: aaload
      // 8d21: bipush 32
      // 8d23: sipush 528
      // 8d26: iastore
      // 8d27: aload 0
      // 8d28: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8d2b: bipush 19
      // 8d2d: aaload
      // 8d2e: bipush 53
      // 8d30: sipush 527
      // 8d33: iastore
      // 8d34: aload 0
      // 8d35: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8d38: bipush 40
      // 8d3a: aaload
      // 8d3b: bipush 83
      // 8d3d: sipush 526
      // 8d40: iastore
      // 8d41: aload 0
      // 8d42: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8d45: bipush 4
      // 8d46: aaload
      // 8d47: bipush 14
      // 8d49: sipush 525
      // 8d4c: iastore
      // 8d4d: aload 0
      // 8d4e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8d51: bipush 36
      // 8d53: aaload
      // 8d54: bipush 9
      // 8d56: sipush 524
      // 8d59: iastore
      // 8d5a: aload 0
      // 8d5b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8d5e: bipush 4
      // 8d5f: aaload
      // 8d60: bipush 73
      // 8d62: sipush 523
      // 8d65: iastore
      // 8d66: aload 0
      // 8d67: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8d6a: bipush 23
      // 8d6c: aaload
      // 8d6d: bipush 10
      // 8d6f: sipush 522
      // 8d72: iastore
      // 8d73: aload 0
      // 8d74: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8d77: bipush 3
      // 8d78: aaload
      // 8d79: bipush 63
      // 8d7b: sipush 521
      // 8d7e: iastore
      // 8d7f: aload 0
      // 8d80: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8d83: bipush 39
      // 8d85: aaload
      // 8d86: bipush 14
      // 8d88: sipush 520
      // 8d8b: iastore
      // 8d8c: aload 0
      // 8d8d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8d90: bipush 3
      // 8d91: aaload
      // 8d92: bipush 78
      // 8d94: sipush 519
      // 8d97: iastore
      // 8d98: aload 0
      // 8d99: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8d9c: bipush 33
      // 8d9e: aaload
      // 8d9f: bipush 47
      // 8da1: sipush 518
      // 8da4: iastore
      // 8da5: aload 0
      // 8da6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8da9: bipush 21
      // 8dab: aaload
      // 8dac: bipush 39
      // 8dae: sipush 517
      // 8db1: iastore
      // 8db2: aload 0
      // 8db3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8db6: bipush 34
      // 8db8: aaload
      // 8db9: bipush 46
      // 8dbb: sipush 516
      // 8dbe: iastore
      // 8dbf: aload 0
      // 8dc0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8dc3: bipush 36
      // 8dc5: aaload
      // 8dc6: bipush 75
      // 8dc8: sipush 515
      // 8dcb: iastore
      // 8dcc: aload 0
      // 8dcd: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8dd0: bipush 41
      // 8dd2: aaload
      // 8dd3: bipush 92
      // 8dd5: sipush 514
      // 8dd8: iastore
      // 8dd9: aload 0
      // 8dda: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8ddd: bipush 37
      // 8ddf: aaload
      // 8de0: bipush 93
      // 8de2: sipush 513
      // 8de5: iastore
      // 8de6: aload 0
      // 8de7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8dea: bipush 4
      // 8deb: aaload
      // 8dec: bipush 34
      // 8dee: sipush 512
      // 8df1: iastore
      // 8df2: aload 0
      // 8df3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8df6: bipush 15
      // 8df8: aaload
      // 8df9: bipush 86
      // 8dfb: sipush 511
      // 8dfe: iastore
      // 8dff: aload 0
      // 8e00: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8e03: bipush 46
      // 8e05: aaload
      // 8e06: bipush 1
      // 8e07: sipush 510
      // 8e0a: iastore
      // 8e0b: aload 0
      // 8e0c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8e0f: bipush 37
      // 8e11: aaload
      // 8e12: bipush 65
      // 8e14: sipush 509
      // 8e17: iastore
      // 8e18: aload 0
      // 8e19: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8e1c: bipush 3
      // 8e1d: aaload
      // 8e1e: bipush 62
      // 8e20: sipush 508
      // 8e23: iastore
      // 8e24: aload 0
      // 8e25: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8e28: bipush 32
      // 8e2a: aaload
      // 8e2b: bipush 73
      // 8e2d: sipush 507
      // 8e30: iastore
      // 8e31: aload 0
      // 8e32: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8e35: bipush 21
      // 8e37: aaload
      // 8e38: bipush 65
      // 8e3a: sipush 506
      // 8e3d: iastore
      // 8e3e: aload 0
      // 8e3f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8e42: bipush 29
      // 8e44: aaload
      // 8e45: bipush 75
      // 8e47: sipush 505
      // 8e4a: iastore
      // 8e4b: aload 0
      // 8e4c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8e4f: bipush 26
      // 8e51: aaload
      // 8e52: bipush 51
      // 8e54: sipush 504
      // 8e57: iastore
      // 8e58: aload 0
      // 8e59: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8e5c: bipush 3
      // 8e5d: aaload
      // 8e5e: bipush 34
      // 8e60: sipush 503
      // 8e63: iastore
      // 8e64: aload 0
      // 8e65: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8e68: bipush 4
      // 8e69: aaload
      // 8e6a: bipush 10
      // 8e6c: sipush 502
      // 8e6f: iastore
      // 8e70: aload 0
      // 8e71: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8e74: bipush 30
      // 8e76: aaload
      // 8e77: bipush 22
      // 8e79: sipush 501
      // 8e7c: iastore
      // 8e7d: aload 0
      // 8e7e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8e81: bipush 35
      // 8e83: aaload
      // 8e84: bipush 73
      // 8e86: sipush 500
      // 8e89: iastore
      // 8e8a: aload 0
      // 8e8b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8e8e: bipush 17
      // 8e90: aaload
      // 8e91: bipush 82
      // 8e93: sipush 499
      // 8e96: iastore
      // 8e97: aload 0
      // 8e98: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8e9b: bipush 45
      // 8e9d: aaload
      // 8e9e: bipush 8
      // 8ea0: sipush 498
      // 8ea3: iastore
      // 8ea4: aload 0
      // 8ea5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8ea8: bipush 27
      // 8eaa: aaload
      // 8eab: bipush 73
      // 8ead: sipush 497
      // 8eb0: iastore
      // 8eb1: aload 0
      // 8eb2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8eb5: bipush 18
      // 8eb7: aaload
      // 8eb8: bipush 55
      // 8eba: sipush 496
      // 8ebd: iastore
      // 8ebe: aload 0
      // 8ebf: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8ec2: bipush 25
      // 8ec4: aaload
      // 8ec5: bipush 2
      // 8ec6: sipush 495
      // 8ec9: iastore
      // 8eca: aload 0
      // 8ecb: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8ece: bipush 3
      // 8ecf: aaload
      // 8ed0: bipush 26
      // 8ed2: sipush 494
      // 8ed5: iastore
      // 8ed6: aload 0
      // 8ed7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8eda: bipush 45
      // 8edc: aaload
      // 8edd: bipush 46
      // 8edf: sipush 493
      // 8ee2: iastore
      // 8ee3: aload 0
      // 8ee4: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8ee7: bipush 4
      // 8ee8: aaload
      // 8ee9: bipush 22
      // 8eeb: sipush 492
      // 8eee: iastore
      // 8eef: aload 0
      // 8ef0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8ef3: bipush 4
      // 8ef4: aaload
      // 8ef5: bipush 40
      // 8ef7: sipush 491
      // 8efa: iastore
      // 8efb: aload 0
      // 8efc: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8eff: bipush 18
      // 8f01: aaload
      // 8f02: bipush 10
      // 8f04: sipush 490
      // 8f07: iastore
      // 8f08: aload 0
      // 8f09: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8f0c: bipush 32
      // 8f0e: aaload
      // 8f0f: bipush 9
      // 8f11: sipush 489
      // 8f14: iastore
      // 8f15: aload 0
      // 8f16: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8f19: bipush 26
      // 8f1b: aaload
      // 8f1c: bipush 49
      // 8f1e: sipush 488
      // 8f21: iastore
      // 8f22: aload 0
      // 8f23: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8f26: bipush 3
      // 8f27: aaload
      // 8f28: bipush 47
      // 8f2a: sipush 487
      // 8f2d: iastore
      // 8f2e: aload 0
      // 8f2f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8f32: bipush 24
      // 8f34: aaload
      // 8f35: bipush 65
      // 8f37: sipush 486
      // 8f3a: iastore
      // 8f3b: aload 0
      // 8f3c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8f3f: bipush 4
      // 8f40: aaload
      // 8f41: bipush 76
      // 8f43: sipush 485
      // 8f46: iastore
      // 8f47: aload 0
      // 8f48: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8f4b: bipush 43
      // 8f4d: aaload
      // 8f4e: bipush 67
      // 8f50: sipush 484
      // 8f53: iastore
      // 8f54: aload 0
      // 8f55: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8f58: bipush 3
      // 8f59: aaload
      // 8f5a: bipush 9
      // 8f5c: sipush 483
      // 8f5f: iastore
      // 8f60: aload 0
      // 8f61: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8f64: bipush 41
      // 8f66: aaload
      // 8f67: bipush 37
      // 8f69: sipush 482
      // 8f6c: iastore
      // 8f6d: aload 0
      // 8f6e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8f71: bipush 33
      // 8f73: aaload
      // 8f74: bipush 68
      // 8f76: sipush 481
      // 8f79: iastore
      // 8f7a: aload 0
      // 8f7b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8f7e: bipush 43
      // 8f80: aaload
      // 8f81: bipush 31
      // 8f83: sipush 480
      // 8f86: iastore
      // 8f87: aload 0
      // 8f88: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8f8b: bipush 19
      // 8f8d: aaload
      // 8f8e: bipush 55
      // 8f90: sipush 479
      // 8f93: iastore
      // 8f94: aload 0
      // 8f95: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8f98: bipush 4
      // 8f99: aaload
      // 8f9a: bipush 30
      // 8f9c: sipush 478
      // 8f9f: iastore
      // 8fa0: aload 0
      // 8fa1: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8fa4: bipush 27
      // 8fa6: aaload
      // 8fa7: bipush 33
      // 8fa9: sipush 477
      // 8fac: iastore
      // 8fad: aload 0
      // 8fae: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8fb1: bipush 16
      // 8fb3: aaload
      // 8fb4: bipush 62
      // 8fb6: sipush 476
      // 8fb9: iastore
      // 8fba: aload 0
      // 8fbb: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8fbe: bipush 36
      // 8fc0: aaload
      // 8fc1: bipush 35
      // 8fc3: sipush 475
      // 8fc6: iastore
      // 8fc7: aload 0
      // 8fc8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8fcb: bipush 37
      // 8fcd: aaload
      // 8fce: bipush 15
      // 8fd0: sipush 474
      // 8fd3: iastore
      // 8fd4: aload 0
      // 8fd5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8fd8: bipush 27
      // 8fda: aaload
      // 8fdb: bipush 70
      // 8fdd: sipush 473
      // 8fe0: iastore
      // 8fe1: aload 0
      // 8fe2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8fe5: bipush 22
      // 8fe7: aaload
      // 8fe8: bipush 71
      // 8fea: sipush 472
      // 8fed: iastore
      // 8fee: aload 0
      // 8fef: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8ff2: bipush 33
      // 8ff4: aaload
      // 8ff5: bipush 45
      // 8ff7: sipush 471
      // 8ffa: iastore
      // 8ffb: aload 0
      // 8ffc: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 8fff: bipush 31
      // 9001: aaload
      // 9002: bipush 78
      // 9004: sipush 470
      // 9007: iastore
      // 9008: aload 0
      // 9009: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 900c: bipush 43
      // 900e: aaload
      // 900f: bipush 59
      // 9011: sipush 469
      // 9014: iastore
      // 9015: aload 0
      // 9016: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9019: bipush 32
      // 901b: aaload
      // 901c: bipush 19
      // 901e: sipush 468
      // 9021: iastore
      // 9022: aload 0
      // 9023: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9026: bipush 17
      // 9028: aaload
      // 9029: bipush 28
      // 902b: sipush 467
      // 902e: iastore
      // 902f: aload 0
      // 9030: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9033: bipush 40
      // 9035: aaload
      // 9036: bipush 28
      // 9038: sipush 466
      // 903b: iastore
      // 903c: aload 0
      // 903d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9040: bipush 20
      // 9042: aaload
      // 9043: bipush 93
      // 9045: sipush 465
      // 9048: iastore
      // 9049: aload 0
      // 904a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 904d: bipush 18
      // 904f: aaload
      // 9050: bipush 15
      // 9052: sipush 464
      // 9055: iastore
      // 9056: aload 0
      // 9057: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 905a: bipush 4
      // 905b: aaload
      // 905c: bipush 23
      // 905e: sipush 463
      // 9061: iastore
      // 9062: aload 0
      // 9063: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9066: bipush 3
      // 9067: aaload
      // 9068: bipush 23
      // 906a: sipush 462
      // 906d: iastore
      // 906e: aload 0
      // 906f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9072: bipush 26
      // 9074: aaload
      // 9075: bipush 64
      // 9077: sipush 461
      // 907a: iastore
      // 907b: aload 0
      // 907c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 907f: bipush 44
      // 9081: aaload
      // 9082: bipush 92
      // 9084: sipush 460
      // 9087: iastore
      // 9088: aload 0
      // 9089: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 908c: bipush 17
      // 908e: aaload
      // 908f: bipush 27
      // 9091: sipush 459
      // 9094: iastore
      // 9095: aload 0
      // 9096: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9099: bipush 3
      // 909a: aaload
      // 909b: bipush 56
      // 909d: sipush 458
      // 90a0: iastore
      // 90a1: aload 0
      // 90a2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 90a5: bipush 25
      // 90a7: aaload
      // 90a8: bipush 38
      // 90aa: sipush 457
      // 90ad: iastore
      // 90ae: aload 0
      // 90af: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 90b2: bipush 23
      // 90b4: aaload
      // 90b5: bipush 31
      // 90b7: sipush 456
      // 90ba: iastore
      // 90bb: aload 0
      // 90bc: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 90bf: bipush 35
      // 90c1: aaload
      // 90c2: bipush 43
      // 90c4: sipush 455
      // 90c7: iastore
      // 90c8: aload 0
      // 90c9: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 90cc: bipush 4
      // 90cd: aaload
      // 90ce: bipush 54
      // 90d0: sipush 454
      // 90d3: iastore
      // 90d4: aload 0
      // 90d5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 90d8: bipush 35
      // 90da: aaload
      // 90db: bipush 19
      // 90dd: sipush 453
      // 90e0: iastore
      // 90e1: aload 0
      // 90e2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 90e5: bipush 22
      // 90e7: aaload
      // 90e8: bipush 47
      // 90ea: sipush 452
      // 90ed: iastore
      // 90ee: aload 0
      // 90ef: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 90f2: bipush 42
      // 90f4: aaload
      // 90f5: bipush 0
      // 90f6: sipush 451
      // 90f9: iastore
      // 90fa: aload 0
      // 90fb: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 90fe: bipush 23
      // 9100: aaload
      // 9101: bipush 28
      // 9103: sipush 450
      // 9106: iastore
      // 9107: aload 0
      // 9108: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 910b: bipush 46
      // 910d: aaload
      // 910e: bipush 33
      // 9110: sipush 449
      // 9113: iastore
      // 9114: aload 0
      // 9115: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9118: bipush 36
      // 911a: aaload
      // 911b: bipush 85
      // 911d: sipush 448
      // 9120: iastore
      // 9121: aload 0
      // 9122: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9125: bipush 31
      // 9127: aaload
      // 9128: bipush 12
      // 912a: sipush 447
      // 912d: iastore
      // 912e: aload 0
      // 912f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9132: bipush 3
      // 9133: aaload
      // 9134: bipush 76
      // 9136: sipush 446
      // 9139: iastore
      // 913a: aload 0
      // 913b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 913e: bipush 4
      // 913f: aaload
      // 9140: bipush 75
      // 9142: sipush 445
      // 9145: iastore
      // 9146: aload 0
      // 9147: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 914a: bipush 36
      // 914c: aaload
      // 914d: bipush 56
      // 914f: sipush 444
      // 9152: iastore
      // 9153: aload 0
      // 9154: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9157: bipush 4
      // 9158: aaload
      // 9159: bipush 64
      // 915b: sipush 443
      // 915e: iastore
      // 915f: aload 0
      // 9160: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9163: bipush 25
      // 9165: aaload
      // 9166: bipush 77
      // 9168: sipush 442
      // 916b: iastore
      // 916c: aload 0
      // 916d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9170: bipush 15
      // 9172: aaload
      // 9173: bipush 52
      // 9175: sipush 441
      // 9178: iastore
      // 9179: aload 0
      // 917a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 917d: bipush 33
      // 917f: aaload
      // 9180: bipush 73
      // 9182: sipush 440
      // 9185: iastore
      // 9186: aload 0
      // 9187: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 918a: bipush 3
      // 918b: aaload
      // 918c: bipush 55
      // 918e: sipush 439
      // 9191: iastore
      // 9192: aload 0
      // 9193: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9196: bipush 43
      // 9198: aaload
      // 9199: bipush 82
      // 919b: sipush 438
      // 919e: iastore
      // 919f: aload 0
      // 91a0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 91a3: bipush 27
      // 91a5: aaload
      // 91a6: bipush 82
      // 91a8: sipush 437
      // 91ab: iastore
      // 91ac: aload 0
      // 91ad: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 91b0: bipush 20
      // 91b2: aaload
      // 91b3: bipush 3
      // 91b4: sipush 436
      // 91b7: iastore
      // 91b8: aload 0
      // 91b9: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 91bc: bipush 40
      // 91be: aaload
      // 91bf: bipush 51
      // 91c1: sipush 435
      // 91c4: iastore
      // 91c5: aload 0
      // 91c6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 91c9: bipush 3
      // 91ca: aaload
      // 91cb: bipush 17
      // 91cd: sipush 434
      // 91d0: iastore
      // 91d1: aload 0
      // 91d2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 91d5: bipush 27
      // 91d7: aaload
      // 91d8: bipush 71
      // 91da: sipush 433
      // 91dd: iastore
      // 91de: aload 0
      // 91df: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 91e2: bipush 4
      // 91e3: aaload
      // 91e4: bipush 52
      // 91e6: sipush 432
      // 91e9: iastore
      // 91ea: aload 0
      // 91eb: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 91ee: bipush 44
      // 91f0: aaload
      // 91f1: bipush 48
      // 91f3: sipush 431
      // 91f6: iastore
      // 91f7: aload 0
      // 91f8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 91fb: bipush 27
      // 91fd: aaload
      // 91fe: bipush 2
      // 91ff: sipush 430
      // 9202: iastore
      // 9203: aload 0
      // 9204: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9207: bipush 17
      // 9209: aaload
      // 920a: bipush 39
      // 920c: sipush 429
      // 920f: iastore
      // 9210: aload 0
      // 9211: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9214: bipush 31
      // 9216: aaload
      // 9217: bipush 8
      // 9219: sipush 428
      // 921c: iastore
      // 921d: aload 0
      // 921e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9221: bipush 44
      // 9223: aaload
      // 9224: bipush 54
      // 9226: sipush 427
      // 9229: iastore
      // 922a: aload 0
      // 922b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 922e: bipush 43
      // 9230: aaload
      // 9231: bipush 18
      // 9233: sipush 426
      // 9236: iastore
      // 9237: aload 0
      // 9238: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 923b: bipush 43
      // 923d: aaload
      // 923e: bipush 77
      // 9240: sipush 425
      // 9243: iastore
      // 9244: aload 0
      // 9245: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9248: bipush 4
      // 9249: aaload
      // 924a: bipush 61
      // 924c: sipush 424
      // 924f: iastore
      // 9250: aload 0
      // 9251: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9254: bipush 19
      // 9256: aaload
      // 9257: bipush 91
      // 9259: sipush 423
      // 925c: iastore
      // 925d: aload 0
      // 925e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9261: bipush 31
      // 9263: aaload
      // 9264: bipush 13
      // 9266: sipush 422
      // 9269: iastore
      // 926a: aload 0
      // 926b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 926e: bipush 44
      // 9270: aaload
      // 9271: bipush 71
      // 9273: sipush 421
      // 9276: iastore
      // 9277: aload 0
      // 9278: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 927b: bipush 20
      // 927d: aaload
      // 927e: bipush 0
      // 927f: sipush 420
      // 9282: iastore
      // 9283: aload 0
      // 9284: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9287: bipush 23
      // 9289: aaload
      // 928a: bipush 87
      // 928c: sipush 419
      // 928f: iastore
      // 9290: aload 0
      // 9291: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9294: bipush 21
      // 9296: aaload
      // 9297: bipush 14
      // 9299: sipush 418
      // 929c: iastore
      // 929d: aload 0
      // 929e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 92a1: bipush 29
      // 92a3: aaload
      // 92a4: bipush 13
      // 92a6: sipush 417
      // 92a9: iastore
      // 92aa: aload 0
      // 92ab: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 92ae: bipush 3
      // 92af: aaload
      // 92b0: bipush 58
      // 92b2: sipush 416
      // 92b5: iastore
      // 92b6: aload 0
      // 92b7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 92ba: bipush 26
      // 92bc: aaload
      // 92bd: bipush 18
      // 92bf: sipush 415
      // 92c2: iastore
      // 92c3: aload 0
      // 92c4: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 92c7: bipush 4
      // 92c8: aaload
      // 92c9: bipush 47
      // 92cb: sipush 414
      // 92ce: iastore
      // 92cf: aload 0
      // 92d0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 92d3: bipush 4
      // 92d4: aaload
      // 92d5: bipush 18
      // 92d7: sipush 413
      // 92da: iastore
      // 92db: aload 0
      // 92dc: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 92df: bipush 3
      // 92e0: aaload
      // 92e1: bipush 53
      // 92e3: sipush 412
      // 92e6: iastore
      // 92e7: aload 0
      // 92e8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 92eb: bipush 26
      // 92ed: aaload
      // 92ee: bipush 92
      // 92f0: sipush 411
      // 92f3: iastore
      // 92f4: aload 0
      // 92f5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 92f8: bipush 21
      // 92fa: aaload
      // 92fb: bipush 7
      // 92fd: sipush 410
      // 9300: iastore
      // 9301: aload 0
      // 9302: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9305: bipush 4
      // 9306: aaload
      // 9307: bipush 37
      // 9309: sipush 409
      // 930c: iastore
      // 930d: aload 0
      // 930e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9311: bipush 4
      // 9312: aaload
      // 9313: bipush 63
      // 9315: sipush 408
      // 9318: iastore
      // 9319: aload 0
      // 931a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 931d: bipush 36
      // 931f: aaload
      // 9320: bipush 51
      // 9322: sipush 407
      // 9325: iastore
      // 9326: aload 0
      // 9327: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 932a: bipush 4
      // 932b: aaload
      // 932c: bipush 32
      // 932e: sipush 406
      // 9331: iastore
      // 9332: aload 0
      // 9333: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9336: bipush 28
      // 9338: aaload
      // 9339: bipush 73
      // 933b: sipush 405
      // 933e: iastore
      // 933f: aload 0
      // 9340: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9343: bipush 4
      // 9344: aaload
      // 9345: bipush 50
      // 9347: sipush 404
      // 934a: iastore
      // 934b: aload 0
      // 934c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 934f: bipush 41
      // 9351: aaload
      // 9352: bipush 60
      // 9354: sipush 403
      // 9357: iastore
      // 9358: aload 0
      // 9359: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 935c: bipush 23
      // 935e: aaload
      // 935f: bipush 1
      // 9360: sipush 402
      // 9363: iastore
      // 9364: aload 0
      // 9365: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9368: bipush 36
      // 936a: aaload
      // 936b: bipush 92
      // 936d: sipush 401
      // 9370: iastore
      // 9371: aload 0
      // 9372: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9375: bipush 15
      // 9377: aaload
      // 9378: bipush 41
      // 937a: sipush 400
      // 937d: iastore
      // 937e: aload 0
      // 937f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9382: bipush 21
      // 9384: aaload
      // 9385: bipush 71
      // 9387: sipush 399
      // 938a: iastore
      // 938b: aload 0
      // 938c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 938f: bipush 41
      // 9391: aaload
      // 9392: bipush 30
      // 9394: sipush 398
      // 9397: iastore
      // 9398: aload 0
      // 9399: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 939c: bipush 32
      // 939e: aaload
      // 939f: bipush 76
      // 93a1: sipush 397
      // 93a4: iastore
      // 93a5: aload 0
      // 93a6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 93a9: bipush 17
      // 93ab: aaload
      // 93ac: bipush 34
      // 93ae: sipush 396
      // 93b1: iastore
      // 93b2: aload 0
      // 93b3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 93b6: bipush 26
      // 93b8: aaload
      // 93b9: bipush 15
      // 93bb: sipush 395
      // 93be: iastore
      // 93bf: aload 0
      // 93c0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 93c3: bipush 26
      // 93c5: aaload
      // 93c6: bipush 25
      // 93c8: sipush 394
      // 93cb: iastore
      // 93cc: aload 0
      // 93cd: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 93d0: bipush 31
      // 93d2: aaload
      // 93d3: bipush 77
      // 93d5: sipush 393
      // 93d8: iastore
      // 93d9: aload 0
      // 93da: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 93dd: bipush 31
      // 93df: aaload
      // 93e0: bipush 3
      // 93e1: sipush 392
      // 93e4: iastore
      // 93e5: aload 0
      // 93e6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 93e9: bipush 46
      // 93eb: aaload
      // 93ec: bipush 34
      // 93ee: sipush 391
      // 93f1: iastore
      // 93f2: aload 0
      // 93f3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 93f6: bipush 27
      // 93f8: aaload
      // 93f9: bipush 84
      // 93fb: sipush 390
      // 93fe: iastore
      // 93ff: aload 0
      // 9400: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9403: bipush 23
      // 9405: aaload
      // 9406: bipush 8
      // 9408: sipush 389
      // 940b: iastore
      // 940c: aload 0
      // 940d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9410: bipush 16
      // 9412: aaload
      // 9413: bipush 0
      // 9414: sipush 388
      // 9417: iastore
      // 9418: aload 0
      // 9419: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 941c: bipush 28
      // 941e: aaload
      // 941f: bipush 80
      // 9421: sipush 387
      // 9424: iastore
      // 9425: aload 0
      // 9426: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9429: bipush 26
      // 942b: aaload
      // 942c: bipush 54
      // 942e: sipush 386
      // 9431: iastore
      // 9432: aload 0
      // 9433: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9436: bipush 33
      // 9438: aaload
      // 9439: bipush 18
      // 943b: sipush 385
      // 943e: iastore
      // 943f: aload 0
      // 9440: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9443: bipush 31
      // 9445: aaload
      // 9446: bipush 20
      // 9448: sipush 384
      // 944b: iastore
      // 944c: aload 0
      // 944d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9450: bipush 31
      // 9452: aaload
      // 9453: bipush 62
      // 9455: sipush 383
      // 9458: iastore
      // 9459: aload 0
      // 945a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 945d: bipush 30
      // 945f: aaload
      // 9460: bipush 41
      // 9462: sipush 382
      // 9465: iastore
      // 9466: aload 0
      // 9467: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 946a: bipush 33
      // 946c: aaload
      // 946d: bipush 30
      // 946f: sipush 381
      // 9472: iastore
      // 9473: aload 0
      // 9474: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9477: bipush 45
      // 9479: aaload
      // 947a: bipush 45
      // 947c: sipush 380
      // 947f: iastore
      // 9480: aload 0
      // 9481: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9484: bipush 37
      // 9486: aaload
      // 9487: bipush 82
      // 9489: sipush 379
      // 948c: iastore
      // 948d: aload 0
      // 948e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9491: bipush 15
      // 9493: aaload
      // 9494: bipush 33
      // 9496: sipush 378
      // 9499: iastore
      // 949a: aload 0
      // 949b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 949e: bipush 20
      // 94a0: aaload
      // 94a1: bipush 12
      // 94a3: sipush 377
      // 94a6: iastore
      // 94a7: aload 0
      // 94a8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 94ab: bipush 18
      // 94ad: aaload
      // 94ae: bipush 5
      // 94af: sipush 376
      // 94b2: iastore
      // 94b3: aload 0
      // 94b4: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 94b7: bipush 28
      // 94b9: aaload
      // 94ba: bipush 86
      // 94bc: sipush 375
      // 94bf: iastore
      // 94c0: aload 0
      // 94c1: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 94c4: bipush 30
      // 94c6: aaload
      // 94c7: bipush 19
      // 94c9: sipush 374
      // 94cc: iastore
      // 94cd: aload 0
      // 94ce: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 94d1: bipush 42
      // 94d3: aaload
      // 94d4: bipush 43
      // 94d6: sipush 373
      // 94d9: iastore
      // 94da: aload 0
      // 94db: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 94de: bipush 36
      // 94e0: aaload
      // 94e1: bipush 31
      // 94e3: sipush 372
      // 94e6: iastore
      // 94e7: aload 0
      // 94e8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 94eb: bipush 17
      // 94ed: aaload
      // 94ee: bipush 93
      // 94f0: sipush 371
      // 94f3: iastore
      // 94f4: aload 0
      // 94f5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 94f8: bipush 4
      // 94f9: aaload
      // 94fa: bipush 15
      // 94fc: sipush 370
      // 94ff: iastore
      // 9500: aload 0
      // 9501: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9504: bipush 21
      // 9506: aaload
      // 9507: bipush 20
      // 9509: sipush 369
      // 950c: iastore
      // 950d: aload 0
      // 950e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9511: bipush 23
      // 9513: aaload
      // 9514: bipush 21
      // 9516: sipush 368
      // 9519: iastore
      // 951a: aload 0
      // 951b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 951e: bipush 28
      // 9520: aaload
      // 9521: bipush 72
      // 9523: sipush 367
      // 9526: iastore
      // 9527: aload 0
      // 9528: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 952b: bipush 4
      // 952c: aaload
      // 952d: bipush 20
      // 952f: sipush 366
      // 9532: iastore
      // 9533: aload 0
      // 9534: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9537: bipush 26
      // 9539: aaload
      // 953a: bipush 55
      // 953c: sipush 365
      // 953f: iastore
      // 9540: aload 0
      // 9541: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9544: bipush 21
      // 9546: aaload
      // 9547: bipush 5
      // 9548: sipush 364
      // 954b: iastore
      // 954c: aload 0
      // 954d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9550: bipush 19
      // 9552: aaload
      // 9553: bipush 16
      // 9555: sipush 363
      // 9558: iastore
      // 9559: aload 0
      // 955a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 955d: bipush 23
      // 955f: aaload
      // 9560: bipush 64
      // 9562: sipush 362
      // 9565: iastore
      // 9566: aload 0
      // 9567: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 956a: bipush 40
      // 956c: aaload
      // 956d: bipush 59
      // 956f: sipush 361
      // 9572: iastore
      // 9573: aload 0
      // 9574: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9577: bipush 37
      // 9579: aaload
      // 957a: bipush 26
      // 957c: sipush 360
      // 957f: iastore
      // 9580: aload 0
      // 9581: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9584: bipush 26
      // 9586: aaload
      // 9587: bipush 56
      // 9589: sipush 359
      // 958c: iastore
      // 958d: aload 0
      // 958e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9591: bipush 4
      // 9592: aaload
      // 9593: bipush 12
      // 9595: sipush 358
      // 9598: iastore
      // 9599: aload 0
      // 959a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 959d: bipush 33
      // 959f: aaload
      // 95a0: bipush 71
      // 95a2: sipush 357
      // 95a5: iastore
      // 95a6: aload 0
      // 95a7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 95aa: bipush 32
      // 95ac: aaload
      // 95ad: bipush 39
      // 95af: sipush 356
      // 95b2: iastore
      // 95b3: aload 0
      // 95b4: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 95b7: bipush 38
      // 95b9: aaload
      // 95ba: bipush 40
      // 95bc: sipush 355
      // 95bf: iastore
      // 95c0: aload 0
      // 95c1: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 95c4: bipush 22
      // 95c6: aaload
      // 95c7: bipush 74
      // 95c9: sipush 354
      // 95cc: iastore
      // 95cd: aload 0
      // 95ce: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 95d1: bipush 3
      // 95d2: aaload
      // 95d3: bipush 25
      // 95d5: sipush 353
      // 95d8: iastore
      // 95d9: aload 0
      // 95da: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 95dd: bipush 15
      // 95df: aaload
      // 95e0: bipush 48
      // 95e2: sipush 352
      // 95e5: iastore
      // 95e6: aload 0
      // 95e7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 95ea: bipush 41
      // 95ec: aaload
      // 95ed: bipush 82
      // 95ef: sipush 351
      // 95f2: iastore
      // 95f3: aload 0
      // 95f4: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 95f7: bipush 41
      // 95f9: aaload
      // 95fa: bipush 9
      // 95fc: sipush 350
      // 95ff: iastore
      // 9600: aload 0
      // 9601: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9604: bipush 25
      // 9606: aaload
      // 9607: bipush 48
      // 9609: sipush 349
      // 960c: iastore
      // 960d: aload 0
      // 960e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9611: bipush 31
      // 9613: aaload
      // 9614: bipush 71
      // 9616: sipush 348
      // 9619: iastore
      // 961a: aload 0
      // 961b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 961e: bipush 43
      // 9620: aaload
      // 9621: bipush 29
      // 9623: sipush 347
      // 9626: iastore
      // 9627: aload 0
      // 9628: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 962b: bipush 26
      // 962d: aaload
      // 962e: bipush 80
      // 9630: sipush 346
      // 9633: iastore
      // 9634: aload 0
      // 9635: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9638: bipush 4
      // 9639: aaload
      // 963a: bipush 5
      // 963b: sipush 345
      // 963e: iastore
      // 963f: aload 0
      // 9640: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9643: bipush 18
      // 9645: aaload
      // 9646: bipush 71
      // 9648: sipush 344
      // 964b: iastore
      // 964c: aload 0
      // 964d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9650: bipush 29
      // 9652: aaload
      // 9653: bipush 0
      // 9654: sipush 343
      // 9657: iastore
      // 9658: aload 0
      // 9659: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 965c: bipush 43
      // 965e: aaload
      // 965f: bipush 43
      // 9661: sipush 342
      // 9664: iastore
      // 9665: aload 0
      // 9666: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9669: bipush 23
      // 966b: aaload
      // 966c: bipush 81
      // 966e: sipush 341
      // 9671: iastore
      // 9672: aload 0
      // 9673: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9676: bipush 4
      // 9677: aaload
      // 9678: bipush 42
      // 967a: sipush 340
      // 967d: iastore
      // 967e: aload 0
      // 967f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9682: bipush 44
      // 9684: aaload
      // 9685: bipush 28
      // 9687: sipush 339
      // 968a: iastore
      // 968b: aload 0
      // 968c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 968f: bipush 23
      // 9691: aaload
      // 9692: bipush 93
      // 9694: sipush 338
      // 9697: iastore
      // 9698: aload 0
      // 9699: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 969c: bipush 17
      // 969e: aaload
      // 969f: bipush 81
      // 96a1: sipush 337
      // 96a4: iastore
      // 96a5: aload 0
      // 96a6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 96a9: bipush 25
      // 96ab: aaload
      // 96ac: bipush 25
      // 96ae: sipush 336
      // 96b1: iastore
      // 96b2: aload 0
      // 96b3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 96b6: bipush 41
      // 96b8: aaload
      // 96b9: bipush 23
      // 96bb: sipush 335
      // 96be: iastore
      // 96bf: aload 0
      // 96c0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 96c3: bipush 34
      // 96c5: aaload
      // 96c6: bipush 35
      // 96c8: sipush 334
      // 96cb: iastore
      // 96cc: aload 0
      // 96cd: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 96d0: bipush 4
      // 96d1: aaload
      // 96d2: bipush 53
      // 96d4: sipush 333
      // 96d7: iastore
      // 96d8: aload 0
      // 96d9: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 96dc: bipush 28
      // 96de: aaload
      // 96df: bipush 36
      // 96e1: sipush 332
      // 96e4: iastore
      // 96e5: aload 0
      // 96e6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 96e9: bipush 4
      // 96ea: aaload
      // 96eb: bipush 41
      // 96ed: sipush 331
      // 96f0: iastore
      // 96f1: aload 0
      // 96f2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 96f5: bipush 25
      // 96f7: aaload
      // 96f8: bipush 60
      // 96fa: sipush 330
      // 96fd: iastore
      // 96fe: aload 0
      // 96ff: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9702: bipush 23
      // 9704: aaload
      // 9705: bipush 20
      // 9707: sipush 329
      // 970a: iastore
      // 970b: aload 0
      // 970c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 970f: bipush 3
      // 9710: aaload
      // 9711: bipush 43
      // 9713: sipush 328
      // 9716: iastore
      // 9717: aload 0
      // 9718: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 971b: bipush 24
      // 971d: aaload
      // 971e: bipush 79
      // 9720: sipush 327
      // 9723: iastore
      // 9724: aload 0
      // 9725: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9728: bipush 29
      // 972a: aaload
      // 972b: bipush 41
      // 972d: sipush 326
      // 9730: iastore
      // 9731: aload 0
      // 9732: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9735: bipush 30
      // 9737: aaload
      // 9738: bipush 83
      // 973a: sipush 325
      // 973d: iastore
      // 973e: aload 0
      // 973f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9742: bipush 3
      // 9743: aaload
      // 9744: bipush 50
      // 9746: sipush 324
      // 9749: iastore
      // 974a: aload 0
      // 974b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 974e: bipush 22
      // 9750: aaload
      // 9751: bipush 18
      // 9753: sipush 323
      // 9756: iastore
      // 9757: aload 0
      // 9758: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 975b: bipush 18
      // 975d: aaload
      // 975e: bipush 3
      // 975f: sipush 322
      // 9762: iastore
      // 9763: aload 0
      // 9764: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9767: bipush 39
      // 9769: aaload
      // 976a: bipush 30
      // 976c: sipush 321
      // 976f: iastore
      // 9770: aload 0
      // 9771: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9774: bipush 4
      // 9775: aaload
      // 9776: bipush 28
      // 9778: sipush 320
      // 977b: iastore
      // 977c: aload 0
      // 977d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9780: bipush 21
      // 9782: aaload
      // 9783: bipush 64
      // 9785: sipush 319
      // 9788: iastore
      // 9789: aload 0
      // 978a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 978d: bipush 4
      // 978e: aaload
      // 978f: bipush 68
      // 9791: sipush 318
      // 9794: iastore
      // 9795: aload 0
      // 9796: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9799: bipush 17
      // 979b: aaload
      // 979c: bipush 71
      // 979e: sipush 317
      // 97a1: iastore
      // 97a2: aload 0
      // 97a3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 97a6: bipush 27
      // 97a8: aaload
      // 97a9: bipush 0
      // 97aa: sipush 316
      // 97ad: iastore
      // 97ae: aload 0
      // 97af: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 97b2: bipush 39
      // 97b4: aaload
      // 97b5: bipush 28
      // 97b7: sipush 315
      // 97ba: iastore
      // 97bb: aload 0
      // 97bc: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 97bf: bipush 30
      // 97c1: aaload
      // 97c2: bipush 13
      // 97c4: sipush 314
      // 97c7: iastore
      // 97c8: aload 0
      // 97c9: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 97cc: bipush 36
      // 97ce: aaload
      // 97cf: bipush 70
      // 97d1: sipush 313
      // 97d4: iastore
      // 97d5: aload 0
      // 97d6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 97d9: bipush 20
      // 97db: aaload
      // 97dc: bipush 82
      // 97de: sipush 312
      // 97e1: iastore
      // 97e2: aload 0
      // 97e3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 97e6: bipush 33
      // 97e8: aaload
      // 97e9: bipush 38
      // 97eb: sipush 311
      // 97ee: iastore
      // 97ef: aload 0
      // 97f0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 97f3: bipush 44
      // 97f5: aaload
      // 97f6: bipush 87
      // 97f8: sipush 310
      // 97fb: iastore
      // 97fc: aload 0
      // 97fd: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9800: bipush 34
      // 9802: aaload
      // 9803: bipush 45
      // 9805: sipush 309
      // 9808: iastore
      // 9809: aload 0
      // 980a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 980d: bipush 4
      // 980e: aaload
      // 980f: bipush 26
      // 9811: sipush 308
      // 9814: iastore
      // 9815: aload 0
      // 9816: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9819: bipush 24
      // 981b: aaload
      // 981c: bipush 44
      // 981e: sipush 307
      // 9821: iastore
      // 9822: aload 0
      // 9823: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9826: bipush 38
      // 9828: aaload
      // 9829: bipush 67
      // 982b: sipush 306
      // 982e: iastore
      // 982f: aload 0
      // 9830: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9833: bipush 38
      // 9835: aaload
      // 9836: bipush 6
      // 9838: sipush 305
      // 983b: iastore
      // 983c: aload 0
      // 983d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9840: bipush 30
      // 9842: aaload
      // 9843: bipush 68
      // 9845: sipush 304
      // 9848: iastore
      // 9849: aload 0
      // 984a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 984d: bipush 15
      // 984f: aaload
      // 9850: bipush 89
      // 9852: sipush 303
      // 9855: iastore
      // 9856: aload 0
      // 9857: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 985a: bipush 24
      // 985c: aaload
      // 985d: bipush 93
      // 985f: sipush 302
      // 9862: iastore
      // 9863: aload 0
      // 9864: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9867: bipush 40
      // 9869: aaload
      // 986a: bipush 41
      // 986c: sipush 301
      // 986f: iastore
      // 9870: aload 0
      // 9871: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9874: bipush 38
      // 9876: aaload
      // 9877: bipush 3
      // 9878: sipush 300
      // 987b: iastore
      // 987c: aload 0
      // 987d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9880: bipush 28
      // 9882: aaload
      // 9883: bipush 23
      // 9885: sipush 299
      // 9888: iastore
      // 9889: aload 0
      // 988a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 988d: bipush 26
      // 988f: aaload
      // 9890: bipush 17
      // 9892: sipush 298
      // 9895: iastore
      // 9896: aload 0
      // 9897: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 989a: bipush 4
      // 989b: aaload
      // 989c: bipush 38
      // 989e: sipush 297
      // 98a1: iastore
      // 98a2: aload 0
      // 98a3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 98a6: bipush 22
      // 98a8: aaload
      // 98a9: bipush 78
      // 98ab: sipush 296
      // 98ae: iastore
      // 98af: aload 0
      // 98b0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 98b3: bipush 15
      // 98b5: aaload
      // 98b6: bipush 37
      // 98b8: sipush 295
      // 98bb: iastore
      // 98bc: aload 0
      // 98bd: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 98c0: bipush 25
      // 98c2: aaload
      // 98c3: bipush 85
      // 98c5: sipush 294
      // 98c8: iastore
      // 98c9: aload 0
      // 98ca: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 98cd: bipush 4
      // 98ce: aaload
      // 98cf: bipush 9
      // 98d1: sipush 293
      // 98d4: iastore
      // 98d5: aload 0
      // 98d6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 98d9: bipush 4
      // 98da: aaload
      // 98db: bipush 7
      // 98dd: sipush 292
      // 98e0: iastore
      // 98e1: aload 0
      // 98e2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 98e5: bipush 27
      // 98e7: aaload
      // 98e8: bipush 53
      // 98ea: sipush 291
      // 98ed: iastore
      // 98ee: aload 0
      // 98ef: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 98f2: bipush 39
      // 98f4: aaload
      // 98f5: bipush 29
      // 98f7: sipush 290
      // 98fa: iastore
      // 98fb: aload 0
      // 98fc: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 98ff: bipush 41
      // 9901: aaload
      // 9902: bipush 43
      // 9904: sipush 289
      // 9907: iastore
      // 9908: aload 0
      // 9909: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 990c: bipush 25
      // 990e: aaload
      // 990f: bipush 62
      // 9911: sipush 288
      // 9914: iastore
      // 9915: aload 0
      // 9916: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9919: bipush 4
      // 991a: aaload
      // 991b: bipush 48
      // 991d: sipush 287
      // 9920: iastore
      // 9921: aload 0
      // 9922: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9925: bipush 28
      // 9927: aaload
      // 9928: bipush 28
      // 992a: sipush 286
      // 992d: iastore
      // 992e: aload 0
      // 992f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9932: bipush 21
      // 9934: aaload
      // 9935: bipush 40
      // 9937: sipush 285
      // 993a: iastore
      // 993b: aload 0
      // 993c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 993f: bipush 36
      // 9941: aaload
      // 9942: bipush 73
      // 9944: sipush 284
      // 9947: iastore
      // 9948: aload 0
      // 9949: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 994c: bipush 26
      // 994e: aaload
      // 994f: bipush 39
      // 9951: sipush 283
      // 9954: iastore
      // 9955: aload 0
      // 9956: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9959: bipush 22
      // 995b: aaload
      // 995c: bipush 54
      // 995e: sipush 282
      // 9961: iastore
      // 9962: aload 0
      // 9963: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9966: bipush 33
      // 9968: aaload
      // 9969: bipush 5
      // 996a: sipush 281
      // 996d: iastore
      // 996e: aload 0
      // 996f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9972: bipush 19
      // 9974: aaload
      // 9975: bipush 21
      // 9977: sipush 280
      // 997a: iastore
      // 997b: aload 0
      // 997c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 997f: bipush 46
      // 9981: aaload
      // 9982: bipush 31
      // 9984: sipush 279
      // 9987: iastore
      // 9988: aload 0
      // 9989: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 998c: bipush 20
      // 998e: aaload
      // 998f: bipush 64
      // 9991: sipush 278
      // 9994: iastore
      // 9995: aload 0
      // 9996: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9999: bipush 26
      // 999b: aaload
      // 999c: bipush 63
      // 999e: sipush 277
      // 99a1: iastore
      // 99a2: aload 0
      // 99a3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 99a6: bipush 22
      // 99a8: aaload
      // 99a9: bipush 23
      // 99ab: sipush 276
      // 99ae: iastore
      // 99af: aload 0
      // 99b0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 99b3: bipush 25
      // 99b5: aaload
      // 99b6: bipush 81
      // 99b8: sipush 275
      // 99bb: iastore
      // 99bc: aload 0
      // 99bd: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 99c0: bipush 4
      // 99c1: aaload
      // 99c2: bipush 62
      // 99c4: sipush 274
      // 99c7: iastore
      // 99c8: aload 0
      // 99c9: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 99cc: bipush 37
      // 99ce: aaload
      // 99cf: bipush 31
      // 99d1: sipush 273
      // 99d4: iastore
      // 99d5: aload 0
      // 99d6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 99d9: bipush 40
      // 99db: aaload
      // 99dc: bipush 52
      // 99de: sipush 272
      // 99e1: iastore
      // 99e2: aload 0
      // 99e3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 99e6: bipush 29
      // 99e8: aaload
      // 99e9: bipush 79
      // 99eb: sipush 271
      // 99ee: iastore
      // 99ef: aload 0
      // 99f0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 99f3: bipush 41
      // 99f5: aaload
      // 99f6: bipush 48
      // 99f8: sipush 270
      // 99fb: iastore
      // 99fc: aload 0
      // 99fd: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9a00: bipush 31
      // 9a02: aaload
      // 9a03: bipush 57
      // 9a05: sipush 269
      // 9a08: iastore
      // 9a09: aload 0
      // 9a0a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9a0d: bipush 32
      // 9a0f: aaload
      // 9a10: bipush 92
      // 9a12: sipush 268
      // 9a15: iastore
      // 9a16: aload 0
      // 9a17: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9a1a: bipush 36
      // 9a1c: aaload
      // 9a1d: bipush 36
      // 9a1f: sipush 267
      // 9a22: iastore
      // 9a23: aload 0
      // 9a24: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9a27: bipush 27
      // 9a29: aaload
      // 9a2a: bipush 7
      // 9a2c: sipush 266
      // 9a2f: iastore
      // 9a30: aload 0
      // 9a31: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9a34: bipush 35
      // 9a36: aaload
      // 9a37: bipush 29
      // 9a39: sipush 265
      // 9a3c: iastore
      // 9a3d: aload 0
      // 9a3e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9a41: bipush 37
      // 9a43: aaload
      // 9a44: bipush 34
      // 9a46: sipush 264
      // 9a49: iastore
      // 9a4a: aload 0
      // 9a4b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9a4e: bipush 34
      // 9a50: aaload
      // 9a51: bipush 42
      // 9a53: sipush 263
      // 9a56: iastore
      // 9a57: aload 0
      // 9a58: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9a5b: bipush 27
      // 9a5d: aaload
      // 9a5e: bipush 15
      // 9a60: sipush 262
      // 9a63: iastore
      // 9a64: aload 0
      // 9a65: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9a68: bipush 33
      // 9a6a: aaload
      // 9a6b: bipush 27
      // 9a6d: sipush 261
      // 9a70: iastore
      // 9a71: aload 0
      // 9a72: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9a75: bipush 31
      // 9a77: aaload
      // 9a78: bipush 38
      // 9a7a: sipush 260
      // 9a7d: iastore
      // 9a7e: aload 0
      // 9a7f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9a82: bipush 19
      // 9a84: aaload
      // 9a85: bipush 79
      // 9a87: sipush 259
      // 9a8a: iastore
      // 9a8b: aload 0
      // 9a8c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9a8f: bipush 4
      // 9a90: aaload
      // 9a91: bipush 31
      // 9a93: sipush 258
      // 9a96: iastore
      // 9a97: aload 0
      // 9a98: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9a9b: bipush 4
      // 9a9c: aaload
      // 9a9d: bipush 66
      // 9a9f: sipush 257
      // 9aa2: iastore
      // 9aa3: aload 0
      // 9aa4: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9aa7: bipush 17
      // 9aa9: aaload
      // 9aaa: bipush 32
      // 9aac: sipush 256
      // 9aaf: iastore
      // 9ab0: aload 0
      // 9ab1: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9ab4: bipush 26
      // 9ab6: aaload
      // 9ab7: bipush 67
      // 9ab9: sipush 255
      // 9abc: iastore
      // 9abd: aload 0
      // 9abe: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9ac1: bipush 16
      // 9ac3: aaload
      // 9ac4: bipush 30
      // 9ac6: sipush 254
      // 9ac9: iastore
      // 9aca: aload 0
      // 9acb: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9ace: bipush 26
      // 9ad0: aaload
      // 9ad1: bipush 46
      // 9ad3: sipush 253
      // 9ad6: iastore
      // 9ad7: aload 0
      // 9ad8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9adb: bipush 24
      // 9add: aaload
      // 9ade: bipush 26
      // 9ae0: sipush 252
      // 9ae3: iastore
      // 9ae4: aload 0
      // 9ae5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9ae8: bipush 35
      // 9aea: aaload
      // 9aeb: bipush 10
      // 9aed: sipush 251
      // 9af0: iastore
      // 9af1: aload 0
      // 9af2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9af5: bipush 18
      // 9af7: aaload
      // 9af8: bipush 37
      // 9afa: sipush 250
      // 9afd: iastore
      // 9afe: aload 0
      // 9aff: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9b02: bipush 3
      // 9b03: aaload
      // 9b04: bipush 19
      // 9b06: sipush 249
      // 9b09: iastore
      // 9b0a: aload 0
      // 9b0b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9b0e: bipush 33
      // 9b10: aaload
      // 9b11: bipush 69
      // 9b13: sipush 248
      // 9b16: iastore
      // 9b17: aload 0
      // 9b18: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9b1b: bipush 31
      // 9b1d: aaload
      // 9b1e: bipush 9
      // 9b20: sipush 247
      // 9b23: iastore
      // 9b24: aload 0
      // 9b25: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9b28: bipush 45
      // 9b2a: aaload
      // 9b2b: bipush 29
      // 9b2d: sipush 246
      // 9b30: iastore
      // 9b31: aload 0
      // 9b32: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9b35: bipush 3
      // 9b36: aaload
      // 9b37: bipush 15
      // 9b39: sipush 245
      // 9b3c: iastore
      // 9b3d: aload 0
      // 9b3e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9b41: bipush 18
      // 9b43: aaload
      // 9b44: bipush 54
      // 9b46: sipush 244
      // 9b49: iastore
      // 9b4a: aload 0
      // 9b4b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9b4e: bipush 3
      // 9b4f: aaload
      // 9b50: bipush 44
      // 9b52: sipush 243
      // 9b55: iastore
      // 9b56: aload 0
      // 9b57: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9b5a: bipush 31
      // 9b5c: aaload
      // 9b5d: bipush 29
      // 9b5f: sipush 242
      // 9b62: iastore
      // 9b63: aload 0
      // 9b64: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9b67: bipush 18
      // 9b69: aaload
      // 9b6a: bipush 45
      // 9b6c: sipush 241
      // 9b6f: iastore
      // 9b70: aload 0
      // 9b71: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9b74: bipush 38
      // 9b76: aaload
      // 9b77: bipush 28
      // 9b79: sipush 240
      // 9b7c: iastore
      // 9b7d: aload 0
      // 9b7e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9b81: bipush 24
      // 9b83: aaload
      // 9b84: bipush 12
      // 9b86: sipush 239
      // 9b89: iastore
      // 9b8a: aload 0
      // 9b8b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9b8e: bipush 35
      // 9b90: aaload
      // 9b91: bipush 82
      // 9b93: sipush 238
      // 9b96: iastore
      // 9b97: aload 0
      // 9b98: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9b9b: bipush 17
      // 9b9d: aaload
      // 9b9e: bipush 43
      // 9ba0: sipush 237
      // 9ba3: iastore
      // 9ba4: aload 0
      // 9ba5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9ba8: bipush 28
      // 9baa: aaload
      // 9bab: bipush 9
      // 9bad: sipush 236
      // 9bb0: iastore
      // 9bb1: aload 0
      // 9bb2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9bb5: bipush 23
      // 9bb7: aaload
      // 9bb8: bipush 25
      // 9bba: sipush 235
      // 9bbd: iastore
      // 9bbe: aload 0
      // 9bbf: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9bc2: bipush 44
      // 9bc4: aaload
      // 9bc5: bipush 37
      // 9bc7: sipush 234
      // 9bca: iastore
      // 9bcb: aload 0
      // 9bcc: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9bcf: bipush 23
      // 9bd1: aaload
      // 9bd2: bipush 75
      // 9bd4: sipush 233
      // 9bd7: iastore
      // 9bd8: aload 0
      // 9bd9: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9bdc: bipush 23
      // 9bde: aaload
      // 9bdf: bipush 92
      // 9be1: sipush 232
      // 9be4: iastore
      // 9be5: aload 0
      // 9be6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9be9: bipush 0
      // 9bea: aaload
      // 9beb: bipush 24
      // 9bed: sipush 231
      // 9bf0: iastore
      // 9bf1: aload 0
      // 9bf2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9bf5: bipush 19
      // 9bf7: aaload
      // 9bf8: bipush 74
      // 9bfa: sipush 230
      // 9bfd: iastore
      // 9bfe: aload 0
      // 9bff: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9c02: bipush 45
      // 9c04: aaload
      // 9c05: bipush 32
      // 9c07: sipush 229
      // 9c0a: iastore
      // 9c0b: aload 0
      // 9c0c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9c0f: bipush 16
      // 9c11: aaload
      // 9c12: bipush 72
      // 9c14: sipush 228
      // 9c17: iastore
      // 9c18: aload 0
      // 9c19: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9c1c: bipush 16
      // 9c1e: aaload
      // 9c1f: bipush 93
      // 9c21: sipush 227
      // 9c24: iastore
      // 9c25: aload 0
      // 9c26: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9c29: bipush 45
      // 9c2b: aaload
      // 9c2c: bipush 13
      // 9c2e: sipush 226
      // 9c31: iastore
      // 9c32: aload 0
      // 9c33: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9c36: bipush 24
      // 9c38: aaload
      // 9c39: bipush 8
      // 9c3b: sipush 225
      // 9c3e: iastore
      // 9c3f: aload 0
      // 9c40: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9c43: bipush 25
      // 9c45: aaload
      // 9c46: bipush 47
      // 9c48: sipush 224
      // 9c4b: iastore
      // 9c4c: aload 0
      // 9c4d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9c50: bipush 28
      // 9c52: aaload
      // 9c53: bipush 26
      // 9c55: sipush 223
      // 9c58: iastore
      // 9c59: aload 0
      // 9c5a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9c5d: bipush 43
      // 9c5f: aaload
      // 9c60: bipush 81
      // 9c62: sipush 222
      // 9c65: iastore
      // 9c66: aload 0
      // 9c67: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9c6a: bipush 32
      // 9c6c: aaload
      // 9c6d: bipush 71
      // 9c6f: sipush 221
      // 9c72: iastore
      // 9c73: aload 0
      // 9c74: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9c77: bipush 18
      // 9c79: aaload
      // 9c7a: bipush 41
      // 9c7c: sipush 220
      // 9c7f: iastore
      // 9c80: aload 0
      // 9c81: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9c84: bipush 26
      // 9c86: aaload
      // 9c87: bipush 62
      // 9c89: sipush 219
      // 9c8c: iastore
      // 9c8d: aload 0
      // 9c8e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9c91: bipush 41
      // 9c93: aaload
      // 9c94: bipush 24
      // 9c96: sipush 218
      // 9c99: iastore
      // 9c9a: aload 0
      // 9c9b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9c9e: bipush 40
      // 9ca0: aaload
      // 9ca1: bipush 11
      // 9ca3: sipush 217
      // 9ca6: iastore
      // 9ca7: aload 0
      // 9ca8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9cab: bipush 43
      // 9cad: aaload
      // 9cae: bipush 57
      // 9cb0: sipush 216
      // 9cb3: iastore
      // 9cb4: aload 0
      // 9cb5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9cb8: bipush 34
      // 9cba: aaload
      // 9cbb: bipush 53
      // 9cbd: sipush 215
      // 9cc0: iastore
      // 9cc1: aload 0
      // 9cc2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9cc5: bipush 20
      // 9cc7: aaload
      // 9cc8: bipush 32
      // 9cca: sipush 214
      // 9ccd: iastore
      // 9cce: aload 0
      // 9ccf: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9cd2: bipush 34
      // 9cd4: aaload
      // 9cd5: bipush 43
      // 9cd7: sipush 213
      // 9cda: iastore
      // 9cdb: aload 0
      // 9cdc: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9cdf: bipush 41
      // 9ce1: aaload
      // 9ce2: bipush 91
      // 9ce4: sipush 212
      // 9ce7: iastore
      // 9ce8: aload 0
      // 9ce9: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9cec: bipush 29
      // 9cee: aaload
      // 9cef: bipush 57
      // 9cf1: sipush 211
      // 9cf4: iastore
      // 9cf5: aload 0
      // 9cf6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9cf9: bipush 15
      // 9cfb: aaload
      // 9cfc: bipush 43
      // 9cfe: sipush 210
      // 9d01: iastore
      // 9d02: aload 0
      // 9d03: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9d06: bipush 22
      // 9d08: aaload
      // 9d09: bipush 89
      // 9d0b: sipush 209
      // 9d0e: iastore
      // 9d0f: aload 0
      // 9d10: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9d13: bipush 33
      // 9d15: aaload
      // 9d16: bipush 83
      // 9d18: sipush 208
      // 9d1b: iastore
      // 9d1c: aload 0
      // 9d1d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9d20: bipush 43
      // 9d22: aaload
      // 9d23: bipush 20
      // 9d25: sipush 207
      // 9d28: iastore
      // 9d29: aload 0
      // 9d2a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9d2d: bipush 25
      // 9d2f: aaload
      // 9d30: bipush 58
      // 9d32: sipush 206
      // 9d35: iastore
      // 9d36: aload 0
      // 9d37: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9d3a: bipush 30
      // 9d3c: aaload
      // 9d3d: bipush 30
      // 9d3f: sipush 205
      // 9d42: iastore
      // 9d43: aload 0
      // 9d44: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9d47: bipush 4
      // 9d48: aaload
      // 9d49: bipush 56
      // 9d4b: sipush 204
      // 9d4e: iastore
      // 9d4f: aload 0
      // 9d50: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9d53: bipush 17
      // 9d55: aaload
      // 9d56: bipush 64
      // 9d58: sipush 203
      // 9d5b: iastore
      // 9d5c: aload 0
      // 9d5d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9d60: bipush 23
      // 9d62: aaload
      // 9d63: bipush 0
      // 9d64: sipush 202
      // 9d67: iastore
      // 9d68: aload 0
      // 9d69: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9d6c: bipush 44
      // 9d6e: aaload
      // 9d6f: bipush 12
      // 9d71: sipush 201
      // 9d74: iastore
      // 9d75: aload 0
      // 9d76: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9d79: bipush 25
      // 9d7b: aaload
      // 9d7c: bipush 37
      // 9d7e: sipush 200
      // 9d81: iastore
      // 9d82: aload 0
      // 9d83: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9d86: bipush 35
      // 9d88: aaload
      // 9d89: bipush 13
      // 9d8b: sipush 199
      // 9d8e: iastore
      // 9d8f: aload 0
      // 9d90: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9d93: bipush 20
      // 9d95: aaload
      // 9d96: bipush 30
      // 9d98: sipush 198
      // 9d9b: iastore
      // 9d9c: aload 0
      // 9d9d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9da0: bipush 21
      // 9da2: aaload
      // 9da3: bipush 84
      // 9da5: sipush 197
      // 9da8: iastore
      // 9da9: aload 0
      // 9daa: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9dad: bipush 29
      // 9daf: aaload
      // 9db0: bipush 14
      // 9db2: sipush 196
      // 9db5: iastore
      // 9db6: aload 0
      // 9db7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9dba: bipush 30
      // 9dbc: aaload
      // 9dbd: bipush 5
      // 9dbe: sipush 195
      // 9dc1: iastore
      // 9dc2: aload 0
      // 9dc3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9dc6: bipush 37
      // 9dc8: aaload
      // 9dc9: bipush 2
      // 9dca: sipush 194
      // 9dcd: iastore
      // 9dce: aload 0
      // 9dcf: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9dd2: bipush 4
      // 9dd3: aaload
      // 9dd4: bipush 78
      // 9dd6: sipush 193
      // 9dd9: iastore
      // 9dda: aload 0
      // 9ddb: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9dde: bipush 29
      // 9de0: aaload
      // 9de1: bipush 78
      // 9de3: sipush 192
      // 9de6: iastore
      // 9de7: aload 0
      // 9de8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9deb: bipush 29
      // 9ded: aaload
      // 9dee: bipush 84
      // 9df0: sipush 191
      // 9df3: iastore
      // 9df4: aload 0
      // 9df5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9df8: bipush 32
      // 9dfa: aaload
      // 9dfb: bipush 86
      // 9dfd: sipush 190
      // 9e00: iastore
      // 9e01: aload 0
      // 9e02: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9e05: bipush 20
      // 9e07: aaload
      // 9e08: bipush 68
      // 9e0a: sipush 189
      // 9e0d: iastore
      // 9e0e: aload 0
      // 9e0f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9e12: bipush 30
      // 9e14: aaload
      // 9e15: bipush 39
      // 9e17: sipush 188
      // 9e1a: iastore
      // 9e1b: aload 0
      // 9e1c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9e1f: bipush 15
      // 9e21: aaload
      // 9e22: bipush 69
      // 9e24: sipush 187
      // 9e27: iastore
      // 9e28: aload 0
      // 9e29: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9e2c: bipush 4
      // 9e2d: aaload
      // 9e2e: bipush 60
      // 9e30: sipush 186
      // 9e33: iastore
      // 9e34: aload 0
      // 9e35: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9e38: bipush 20
      // 9e3a: aaload
      // 9e3b: bipush 61
      // 9e3d: sipush 185
      // 9e40: iastore
      // 9e41: aload 0
      // 9e42: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9e45: bipush 41
      // 9e47: aaload
      // 9e48: bipush 67
      // 9e4a: sipush 184
      // 9e4d: iastore
      // 9e4e: aload 0
      // 9e4f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9e52: bipush 16
      // 9e54: aaload
      // 9e55: bipush 35
      // 9e57: sipush 183
      // 9e5a: iastore
      // 9e5b: aload 0
      // 9e5c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9e5f: bipush 36
      // 9e61: aaload
      // 9e62: bipush 57
      // 9e64: sipush 182
      // 9e67: iastore
      // 9e68: aload 0
      // 9e69: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9e6c: bipush 39
      // 9e6e: aaload
      // 9e6f: bipush 80
      // 9e71: sipush 181
      // 9e74: iastore
      // 9e75: aload 0
      // 9e76: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9e79: bipush 4
      // 9e7a: aaload
      // 9e7b: bipush 59
      // 9e7d: sipush 180
      // 9e80: iastore
      // 9e81: aload 0
      // 9e82: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9e85: bipush 4
      // 9e86: aaload
      // 9e87: bipush 44
      // 9e89: sipush 179
      // 9e8c: iastore
      // 9e8d: aload 0
      // 9e8e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9e91: bipush 40
      // 9e93: aaload
      // 9e94: bipush 54
      // 9e96: sipush 178
      // 9e99: iastore
      // 9e9a: aload 0
      // 9e9b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9e9e: bipush 30
      // 9ea0: aaload
      // 9ea1: bipush 8
      // 9ea3: sipush 177
      // 9ea6: iastore
      // 9ea7: aload 0
      // 9ea8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9eab: bipush 44
      // 9ead: aaload
      // 9eae: bipush 30
      // 9eb0: sipush 176
      // 9eb3: iastore
      // 9eb4: aload 0
      // 9eb5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9eb8: bipush 31
      // 9eba: aaload
      // 9ebb: bipush 93
      // 9ebd: sipush 175
      // 9ec0: iastore
      // 9ec1: aload 0
      // 9ec2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9ec5: bipush 31
      // 9ec7: aaload
      // 9ec8: bipush 47
      // 9eca: sipush 174
      // 9ecd: iastore
      // 9ece: aload 0
      // 9ecf: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9ed2: bipush 16
      // 9ed4: aaload
      // 9ed5: bipush 70
      // 9ed7: sipush 173
      // 9eda: iastore
      // 9edb: aload 0
      // 9edc: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9edf: bipush 21
      // 9ee1: aaload
      // 9ee2: bipush 0
      // 9ee3: sipush 172
      // 9ee6: iastore
      // 9ee7: aload 0
      // 9ee8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9eeb: bipush 17
      // 9eed: aaload
      // 9eee: bipush 35
      // 9ef0: sipush 171
      // 9ef3: iastore
      // 9ef4: aload 0
      // 9ef5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9ef8: bipush 21
      // 9efa: aaload
      // 9efb: bipush 67
      // 9efd: sipush 170
      // 9f00: iastore
      // 9f01: aload 0
      // 9f02: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9f05: bipush 44
      // 9f07: aaload
      // 9f08: bipush 18
      // 9f0a: sipush 169
      // 9f0d: iastore
      // 9f0e: aload 0
      // 9f0f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9f12: bipush 36
      // 9f14: aaload
      // 9f15: bipush 29
      // 9f17: sipush 168
      // 9f1a: iastore
      // 9f1b: aload 0
      // 9f1c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9f1f: bipush 18
      // 9f21: aaload
      // 9f22: bipush 67
      // 9f24: sipush 167
      // 9f27: iastore
      // 9f28: aload 0
      // 9f29: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9f2c: bipush 24
      // 9f2e: aaload
      // 9f2f: bipush 28
      // 9f31: sipush 166
      // 9f34: iastore
      // 9f35: aload 0
      // 9f36: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9f39: bipush 36
      // 9f3b: aaload
      // 9f3c: bipush 24
      // 9f3e: sipush 165
      // 9f41: iastore
      // 9f42: aload 0
      // 9f43: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9f46: bipush 23
      // 9f48: aaload
      // 9f49: bipush 5
      // 9f4a: sipush 164
      // 9f4d: iastore
      // 9f4e: aload 0
      // 9f4f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9f52: bipush 31
      // 9f54: aaload
      // 9f55: bipush 65
      // 9f57: sipush 163
      // 9f5a: iastore
      // 9f5b: aload 0
      // 9f5c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9f5f: bipush 26
      // 9f61: aaload
      // 9f62: bipush 59
      // 9f64: sipush 162
      // 9f67: iastore
      // 9f68: aload 0
      // 9f69: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9f6c: bipush 28
      // 9f6e: aaload
      // 9f6f: bipush 2
      // 9f70: sipush 161
      // 9f73: iastore
      // 9f74: aload 0
      // 9f75: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9f78: bipush 39
      // 9f7a: aaload
      // 9f7b: bipush 69
      // 9f7d: sipush 160
      // 9f80: iastore
      // 9f81: aload 0
      // 9f82: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9f85: bipush 42
      // 9f87: aaload
      // 9f88: bipush 40
      // 9f8a: sipush 159
      // 9f8d: iastore
      // 9f8e: aload 0
      // 9f8f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9f92: bipush 37
      // 9f94: aaload
      // 9f95: bipush 80
      // 9f97: sipush 158
      // 9f9a: iastore
      // 9f9b: aload 0
      // 9f9c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9f9f: bipush 15
      // 9fa1: aaload
      // 9fa2: bipush 66
      // 9fa4: sipush 157
      // 9fa7: iastore
      // 9fa8: aload 0
      // 9fa9: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9fac: bipush 34
      // 9fae: aaload
      // 9faf: bipush 38
      // 9fb1: sipush 156
      // 9fb4: iastore
      // 9fb5: aload 0
      // 9fb6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9fb9: bipush 28
      // 9fbb: aaload
      // 9fbc: bipush 48
      // 9fbe: sipush 155
      // 9fc1: iastore
      // 9fc2: aload 0
      // 9fc3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9fc6: bipush 37
      // 9fc8: aaload
      // 9fc9: bipush 77
      // 9fcb: sipush 154
      // 9fce: iastore
      // 9fcf: aload 0
      // 9fd0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9fd3: bipush 29
      // 9fd5: aaload
      // 9fd6: bipush 34
      // 9fd8: sipush 153
      // 9fdb: iastore
      // 9fdc: aload 0
      // 9fdd: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9fe0: bipush 33
      // 9fe2: aaload
      // 9fe3: bipush 12
      // 9fe5: sipush 152
      // 9fe8: iastore
      // 9fe9: aload 0
      // 9fea: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9fed: bipush 4
      // 9fee: aaload
      // 9fef: bipush 65
      // 9ff1: sipush 151
      // 9ff4: iastore
      // 9ff5: aload 0
      // 9ff6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // 9ff9: bipush 30
      // 9ffb: aaload
      // 9ffc: bipush 31
      // 9ffe: sipush 150
      // a001: iastore
      // a002: aload 0
      // a003: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a006: bipush 27
      // a008: aaload
      // a009: bipush 92
      // a00b: sipush 149
      // a00e: iastore
      // a00f: aload 0
      // a010: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a013: bipush 4
      // a014: aaload
      // a015: bipush 2
      // a016: sipush 148
      // a019: iastore
      // a01a: aload 0
      // a01b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a01e: bipush 4
      // a01f: aaload
      // a020: bipush 51
      // a022: sipush 147
      // a025: iastore
      // a026: aload 0
      // a027: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a02a: bipush 23
      // a02c: aaload
      // a02d: bipush 77
      // a02f: sipush 146
      // a032: iastore
      // a033: aload 0
      // a034: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a037: bipush 4
      // a038: aaload
      // a039: bipush 35
      // a03b: sipush 145
      // a03e: iastore
      // a03f: aload 0
      // a040: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a043: bipush 3
      // a044: aaload
      // a045: bipush 13
      // a047: sipush 144
      // a04a: iastore
      // a04b: aload 0
      // a04c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a04f: bipush 26
      // a051: aaload
      // a052: bipush 26
      // a054: sipush 143
      // a057: iastore
      // a058: aload 0
      // a059: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a05c: bipush 44
      // a05e: aaload
      // a05f: bipush 4
      // a060: sipush 142
      // a063: iastore
      // a064: aload 0
      // a065: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a068: bipush 39
      // a06a: aaload
      // a06b: bipush 53
      // a06d: sipush 141
      // a070: iastore
      // a071: aload 0
      // a072: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a075: bipush 20
      // a077: aaload
      // a078: bipush 11
      // a07a: sipush 140
      // a07d: iastore
      // a07e: aload 0
      // a07f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a082: bipush 40
      // a084: aaload
      // a085: bipush 33
      // a087: sipush 139
      // a08a: iastore
      // a08b: aload 0
      // a08c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a08f: bipush 45
      // a091: aaload
      // a092: bipush 7
      // a094: sipush 138
      // a097: iastore
      // a098: aload 0
      // a099: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a09c: bipush 4
      // a09d: aaload
      // a09e: bipush 70
      // a0a0: sipush 137
      // a0a3: iastore
      // a0a4: aload 0
      // a0a5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a0a8: bipush 3
      // a0a9: aaload
      // a0aa: bipush 49
      // a0ac: sipush 136
      // a0af: iastore
      // a0b0: aload 0
      // a0b1: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a0b4: bipush 20
      // a0b6: aaload
      // a0b7: bipush 59
      // a0b9: sipush 135
      // a0bc: iastore
      // a0bd: aload 0
      // a0be: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a0c1: bipush 21
      // a0c3: aaload
      // a0c4: bipush 12
      // a0c6: sipush 134
      // a0c9: iastore
      // a0ca: aload 0
      // a0cb: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a0ce: bipush 33
      // a0d0: aaload
      // a0d1: bipush 53
      // a0d3: sipush 133
      // a0d6: iastore
      // a0d7: aload 0
      // a0d8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a0db: bipush 20
      // a0dd: aaload
      // a0de: bipush 14
      // a0e0: sipush 132
      // a0e3: iastore
      // a0e4: aload 0
      // a0e5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a0e8: bipush 37
      // a0ea: aaload
      // a0eb: bipush 18
      // a0ed: sipush 131
      // a0f0: iastore
      // a0f1: aload 0
      // a0f2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a0f5: bipush 18
      // a0f7: aaload
      // a0f8: bipush 17
      // a0fa: sipush 130
      // a0fd: iastore
      // a0fe: aload 0
      // a0ff: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a102: bipush 36
      // a104: aaload
      // a105: bipush 23
      // a107: sipush 129
      // a10a: iastore
      // a10b: aload 0
      // a10c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a10f: bipush 18
      // a111: aaload
      // a112: bipush 57
      // a114: sipush 128
      // a117: iastore
      // a118: aload 0
      // a119: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a11c: bipush 26
      // a11e: aaload
      // a11f: bipush 74
      // a121: bipush 127
      // a123: iastore
      // a124: aload 0
      // a125: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a128: bipush 35
      // a12a: aaload
      // a12b: bipush 2
      // a12c: bipush 126
      // a12e: iastore
      // a12f: aload 0
      // a130: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a133: bipush 38
      // a135: aaload
      // a136: bipush 58
      // a138: bipush 125
      // a13a: iastore
      // a13b: aload 0
      // a13c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a13f: bipush 34
      // a141: aaload
      // a142: bipush 68
      // a144: bipush 124
      // a146: iastore
      // a147: aload 0
      // a148: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a14b: bipush 29
      // a14d: aaload
      // a14e: bipush 81
      // a150: bipush 123
      // a152: iastore
      // a153: aload 0
      // a154: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a157: bipush 20
      // a159: aaload
      // a15a: bipush 69
      // a15c: bipush 122
      // a15e: iastore
      // a15f: aload 0
      // a160: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a163: bipush 39
      // a165: aaload
      // a166: bipush 86
      // a168: bipush 121
      // a16a: iastore
      // a16b: aload 0
      // a16c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a16f: bipush 4
      // a170: aaload
      // a171: bipush 16
      // a173: bipush 120
      // a175: iastore
      // a176: aload 0
      // a177: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a17a: bipush 16
      // a17c: aaload
      // a17d: bipush 49
      // a17f: bipush 119
      // a181: iastore
      // a182: aload 0
      // a183: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a186: bipush 15
      // a188: aaload
      // a189: bipush 72
      // a18b: bipush 118
      // a18d: iastore
      // a18e: aload 0
      // a18f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a192: bipush 26
      // a194: aaload
      // a195: bipush 35
      // a197: bipush 117
      // a199: iastore
      // a19a: aload 0
      // a19b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a19e: bipush 32
      // a1a0: aaload
      // a1a1: bipush 14
      // a1a3: bipush 116
      // a1a5: iastore
      // a1a6: aload 0
      // a1a7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a1aa: bipush 40
      // a1ac: aaload
      // a1ad: bipush 90
      // a1af: bipush 115
      // a1b1: iastore
      // a1b2: aload 0
      // a1b3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a1b6: bipush 33
      // a1b8: aaload
      // a1b9: bipush 79
      // a1bb: bipush 114
      // a1bd: iastore
      // a1be: aload 0
      // a1bf: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a1c2: bipush 35
      // a1c4: aaload
      // a1c5: bipush 4
      // a1c6: bipush 113
      // a1c8: iastore
      // a1c9: aload 0
      // a1ca: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a1cd: bipush 23
      // a1cf: aaload
      // a1d0: bipush 33
      // a1d2: bipush 112
      // a1d4: iastore
      // a1d5: aload 0
      // a1d6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a1d9: bipush 19
      // a1db: aaload
      // a1dc: bipush 19
      // a1de: bipush 111
      // a1e0: iastore
      // a1e1: aload 0
      // a1e2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a1e5: bipush 31
      // a1e7: aaload
      // a1e8: bipush 41
      // a1ea: bipush 110
      // a1ec: iastore
      // a1ed: aload 0
      // a1ee: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a1f1: bipush 44
      // a1f3: aaload
      // a1f4: bipush 1
      // a1f5: bipush 109
      // a1f7: iastore
      // a1f8: aload 0
      // a1f9: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a1fc: bipush 22
      // a1fe: aaload
      // a1ff: bipush 56
      // a201: bipush 108
      // a203: iastore
      // a204: aload 0
      // a205: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a208: bipush 31
      // a20a: aaload
      // a20b: bipush 27
      // a20d: bipush 107
      // a20f: iastore
      // a210: aload 0
      // a211: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a214: bipush 32
      // a216: aaload
      // a217: bipush 18
      // a219: bipush 106
      // a21b: iastore
      // a21c: aload 0
      // a21d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a220: bipush 27
      // a222: aaload
      // a223: bipush 32
      // a225: bipush 105
      // a227: iastore
      // a228: aload 0
      // a229: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a22c: bipush 37
      // a22e: aaload
      // a22f: bipush 39
      // a231: bipush 104
      // a233: iastore
      // a234: aload 0
      // a235: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a238: bipush 42
      // a23a: aaload
      // a23b: bipush 11
      // a23d: bipush 103
      // a23f: iastore
      // a240: aload 0
      // a241: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a244: bipush 29
      // a246: aaload
      // a247: bipush 71
      // a249: bipush 102
      // a24b: iastore
      // a24c: aload 0
      // a24d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a250: bipush 32
      // a252: aaload
      // a253: bipush 58
      // a255: bipush 101
      // a257: iastore
      // a258: aload 0
      // a259: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a25c: bipush 46
      // a25e: aaload
      // a25f: bipush 10
      // a261: bipush 100
      // a263: iastore
      // a264: aload 0
      // a265: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a268: bipush 17
      // a26a: aaload
      // a26b: bipush 30
      // a26d: bipush 99
      // a26f: iastore
      // a270: aload 0
      // a271: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a274: bipush 38
      // a276: aaload
      // a277: bipush 15
      // a279: bipush 98
      // a27b: iastore
      // a27c: aload 0
      // a27d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a280: bipush 29
      // a282: aaload
      // a283: bipush 60
      // a285: bipush 97
      // a287: iastore
      // a288: aload 0
      // a289: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a28c: bipush 4
      // a28d: aaload
      // a28e: bipush 11
      // a290: bipush 96
      // a292: iastore
      // a293: aload 0
      // a294: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a297: bipush 38
      // a299: aaload
      // a29a: bipush 31
      // a29c: bipush 95
      // a29e: iastore
      // a29f: aload 0
      // a2a0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a2a3: bipush 40
      // a2a5: aaload
      // a2a6: bipush 79
      // a2a8: bipush 94
      // a2aa: iastore
      // a2ab: aload 0
      // a2ac: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a2af: bipush 28
      // a2b1: aaload
      // a2b2: bipush 49
      // a2b4: bipush 93
      // a2b6: iastore
      // a2b7: aload 0
      // a2b8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a2bb: bipush 28
      // a2bd: aaload
      // a2be: bipush 84
      // a2c0: bipush 92
      // a2c2: iastore
      // a2c3: aload 0
      // a2c4: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a2c7: bipush 26
      // a2c9: aaload
      // a2ca: bipush 77
      // a2cc: bipush 91
      // a2ce: iastore
      // a2cf: aload 0
      // a2d0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a2d3: bipush 22
      // a2d5: aaload
      // a2d6: bipush 32
      // a2d8: bipush 90
      // a2da: iastore
      // a2db: aload 0
      // a2dc: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a2df: bipush 33
      // a2e1: aaload
      // a2e2: bipush 17
      // a2e4: bipush 89
      // a2e6: iastore
      // a2e7: aload 0
      // a2e8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a2eb: bipush 23
      // a2ed: aaload
      // a2ee: bipush 18
      // a2f0: bipush 88
      // a2f2: iastore
      // a2f3: aload 0
      // a2f4: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a2f7: bipush 32
      // a2f9: aaload
      // a2fa: bipush 64
      // a2fc: bipush 87
      // a2fe: iastore
      // a2ff: aload 0
      // a300: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a303: bipush 4
      // a304: aaload
      // a305: bipush 6
      // a307: bipush 86
      // a309: iastore
      // a30a: aload 0
      // a30b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a30e: bipush 33
      // a310: aaload
      // a311: bipush 51
      // a313: bipush 85
      // a315: iastore
      // a316: aload 0
      // a317: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a31a: bipush 44
      // a31c: aaload
      // a31d: bipush 77
      // a31f: bipush 84
      // a321: iastore
      // a322: aload 0
      // a323: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a326: bipush 29
      // a328: aaload
      // a329: bipush 5
      // a32a: bipush 83
      // a32c: iastore
      // a32d: aload 0
      // a32e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a331: bipush 46
      // a333: aaload
      // a334: bipush 25
      // a336: bipush 82
      // a338: iastore
      // a339: aload 0
      // a33a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a33d: bipush 19
      // a33f: aaload
      // a340: bipush 58
      // a342: bipush 81
      // a344: iastore
      // a345: aload 0
      // a346: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a349: bipush 4
      // a34a: aaload
      // a34b: bipush 46
      // a34d: bipush 80
      // a34f: iastore
      // a350: aload 0
      // a351: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a354: bipush 15
      // a356: aaload
      // a357: bipush 71
      // a359: bipush 79
      // a35b: iastore
      // a35c: aload 0
      // a35d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a360: bipush 18
      // a362: aaload
      // a363: bipush 58
      // a365: bipush 78
      // a367: iastore
      // a368: aload 0
      // a369: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a36c: bipush 26
      // a36e: aaload
      // a36f: bipush 45
      // a371: bipush 77
      // a373: iastore
      // a374: aload 0
      // a375: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a378: bipush 45
      // a37a: aaload
      // a37b: bipush 66
      // a37d: bipush 76
      // a37f: iastore
      // a380: aload 0
      // a381: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a384: bipush 34
      // a386: aaload
      // a387: bipush 10
      // a389: bipush 75
      // a38b: iastore
      // a38c: aload 0
      // a38d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a390: bipush 19
      // a392: aaload
      // a393: bipush 37
      // a395: bipush 74
      // a397: iastore
      // a398: aload 0
      // a399: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a39c: bipush 33
      // a39e: aaload
      // a39f: bipush 65
      // a3a1: bipush 73
      // a3a3: iastore
      // a3a4: aload 0
      // a3a5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a3a8: bipush 44
      // a3aa: aaload
      // a3ab: bipush 52
      // a3ad: bipush 72
      // a3af: iastore
      // a3b0: aload 0
      // a3b1: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a3b4: bipush 16
      // a3b6: aaload
      // a3b7: bipush 38
      // a3b9: bipush 71
      // a3bb: iastore
      // a3bc: aload 0
      // a3bd: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a3c0: bipush 36
      // a3c2: aaload
      // a3c3: bipush 46
      // a3c5: bipush 70
      // a3c7: iastore
      // a3c8: aload 0
      // a3c9: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a3cc: bipush 20
      // a3ce: aaload
      // a3cf: bipush 26
      // a3d1: bipush 69
      // a3d3: iastore
      // a3d4: aload 0
      // a3d5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a3d8: bipush 30
      // a3da: aaload
      // a3db: bipush 37
      // a3dd: bipush 68
      // a3df: iastore
      // a3e0: aload 0
      // a3e1: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a3e4: bipush 4
      // a3e5: aaload
      // a3e6: bipush 58
      // a3e8: bipush 67
      // a3ea: iastore
      // a3eb: aload 0
      // a3ec: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a3ef: bipush 43
      // a3f1: aaload
      // a3f2: bipush 2
      // a3f3: bipush 66
      // a3f5: iastore
      // a3f6: aload 0
      // a3f7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a3fa: bipush 30
      // a3fc: aaload
      // a3fd: bipush 18
      // a3ff: bipush 65
      // a401: iastore
      // a402: aload 0
      // a403: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a406: bipush 19
      // a408: aaload
      // a409: bipush 35
      // a40b: bipush 64
      // a40d: iastore
      // a40e: aload 0
      // a40f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a412: bipush 15
      // a414: aaload
      // a415: bipush 68
      // a417: bipush 63
      // a419: iastore
      // a41a: aload 0
      // a41b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a41e: bipush 3
      // a41f: aaload
      // a420: bipush 36
      // a422: bipush 62
      // a424: iastore
      // a425: aload 0
      // a426: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a429: bipush 35
      // a42b: aaload
      // a42c: bipush 40
      // a42e: bipush 61
      // a430: iastore
      // a431: aload 0
      // a432: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a435: bipush 36
      // a437: aaload
      // a438: bipush 32
      // a43a: bipush 60
      // a43c: iastore
      // a43d: aload 0
      // a43e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a441: bipush 37
      // a443: aaload
      // a444: bipush 14
      // a446: bipush 59
      // a448: iastore
      // a449: aload 0
      // a44a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a44d: bipush 17
      // a44f: aaload
      // a450: bipush 11
      // a452: bipush 58
      // a454: iastore
      // a455: aload 0
      // a456: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a459: bipush 19
      // a45b: aaload
      // a45c: bipush 78
      // a45e: bipush 57
      // a460: iastore
      // a461: aload 0
      // a462: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a465: bipush 37
      // a467: aaload
      // a468: bipush 11
      // a46a: bipush 56
      // a46c: iastore
      // a46d: aload 0
      // a46e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a471: bipush 28
      // a473: aaload
      // a474: bipush 63
      // a476: bipush 55
      // a478: iastore
      // a479: aload 0
      // a47a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a47d: bipush 29
      // a47f: aaload
      // a480: bipush 61
      // a482: bipush 54
      // a484: iastore
      // a485: aload 0
      // a486: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a489: bipush 33
      // a48b: aaload
      // a48c: bipush 3
      // a48d: bipush 53
      // a48f: iastore
      // a490: aload 0
      // a491: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a494: bipush 41
      // a496: aaload
      // a497: bipush 52
      // a499: bipush 52
      // a49b: iastore
      // a49c: aload 0
      // a49d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a4a0: bipush 33
      // a4a2: aaload
      // a4a3: bipush 63
      // a4a5: bipush 51
      // a4a7: iastore
      // a4a8: aload 0
      // a4a9: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a4ac: bipush 22
      // a4ae: aaload
      // a4af: bipush 41
      // a4b1: bipush 50
      // a4b3: iastore
      // a4b4: aload 0
      // a4b5: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a4b8: bipush 4
      // a4b9: aaload
      // a4ba: bipush 19
      // a4bc: bipush 49
      // a4be: iastore
      // a4bf: aload 0
      // a4c0: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a4c3: bipush 32
      // a4c5: aaload
      // a4c6: bipush 41
      // a4c8: bipush 48
      // a4ca: iastore
      // a4cb: aload 0
      // a4cc: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a4cf: bipush 24
      // a4d1: aaload
      // a4d2: bipush 4
      // a4d3: bipush 47
      // a4d5: iastore
      // a4d6: aload 0
      // a4d7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a4da: bipush 31
      // a4dc: aaload
      // a4dd: bipush 28
      // a4df: bipush 46
      // a4e1: iastore
      // a4e2: aload 0
      // a4e3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a4e6: bipush 43
      // a4e8: aaload
      // a4e9: bipush 30
      // a4eb: bipush 45
      // a4ed: iastore
      // a4ee: aload 0
      // a4ef: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a4f2: bipush 17
      // a4f4: aaload
      // a4f5: bipush 3
      // a4f6: bipush 44
      // a4f8: iastore
      // a4f9: aload 0
      // a4fa: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a4fd: bipush 43
      // a4ff: aaload
      // a500: bipush 70
      // a502: bipush 43
      // a504: iastore
      // a505: aload 0
      // a506: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a509: bipush 34
      // a50b: aaload
      // a50c: bipush 19
      // a50e: bipush 42
      // a510: iastore
      // a511: aload 0
      // a512: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a515: bipush 20
      // a517: aaload
      // a518: bipush 77
      // a51a: bipush 41
      // a51c: iastore
      // a51d: aload 0
      // a51e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a521: bipush 18
      // a523: aaload
      // a524: bipush 83
      // a526: bipush 40
      // a528: iastore
      // a529: aload 0
      // a52a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a52d: bipush 17
      // a52f: aaload
      // a530: bipush 15
      // a532: bipush 39
      // a534: iastore
      // a535: aload 0
      // a536: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a539: bipush 23
      // a53b: aaload
      // a53c: bipush 61
      // a53e: bipush 38
      // a540: iastore
      // a541: aload 0
      // a542: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a545: bipush 40
      // a547: aaload
      // a548: bipush 27
      // a54a: bipush 37
      // a54c: iastore
      // a54d: aload 0
      // a54e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a551: bipush 16
      // a553: aaload
      // a554: bipush 48
      // a556: bipush 36
      // a558: iastore
      // a559: aload 0
      // a55a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a55d: bipush 39
      // a55f: aaload
      // a560: bipush 78
      // a562: bipush 35
      // a564: iastore
      // a565: aload 0
      // a566: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a569: bipush 41
      // a56b: aaload
      // a56c: bipush 53
      // a56e: bipush 34
      // a570: iastore
      // a571: aload 0
      // a572: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a575: bipush 40
      // a577: aaload
      // a578: bipush 91
      // a57a: bipush 33
      // a57c: iastore
      // a57d: aload 0
      // a57e: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a581: bipush 40
      // a583: aaload
      // a584: bipush 72
      // a586: bipush 32
      // a588: iastore
      // a589: aload 0
      // a58a: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a58d: bipush 18
      // a58f: aaload
      // a590: bipush 52
      // a592: bipush 31
      // a594: iastore
      // a595: aload 0
      // a596: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a599: bipush 35
      // a59b: aaload
      // a59c: bipush 66
      // a59e: bipush 30
      // a5a0: iastore
      // a5a1: aload 0
      // a5a2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a5a5: bipush 39
      // a5a7: aaload
      // a5a8: bipush 93
      // a5aa: bipush 29
      // a5ac: iastore
      // a5ad: aload 0
      // a5ae: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a5b1: bipush 19
      // a5b3: aaload
      // a5b4: bipush 48
      // a5b6: bipush 28
      // a5b8: iastore
      // a5b9: aload 0
      // a5ba: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a5bd: bipush 26
      // a5bf: aaload
      // a5c0: bipush 36
      // a5c2: bipush 27
      // a5c4: iastore
      // a5c5: aload 0
      // a5c6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a5c9: bipush 27
      // a5cb: aaload
      // a5cc: bipush 25
      // a5ce: bipush 26
      // a5d0: iastore
      // a5d1: aload 0
      // a5d2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a5d5: bipush 42
      // a5d7: aaload
      // a5d8: bipush 71
      // a5da: bipush 25
      // a5dc: iastore
      // a5dd: aload 0
      // a5de: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a5e1: bipush 42
      // a5e3: aaload
      // a5e4: bipush 85
      // a5e6: bipush 24
      // a5e8: iastore
      // a5e9: aload 0
      // a5ea: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a5ed: bipush 26
      // a5ef: aaload
      // a5f0: bipush 48
      // a5f2: bipush 23
      // a5f4: iastore
      // a5f5: aload 0
      // a5f6: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a5f9: bipush 28
      // a5fb: aaload
      // a5fc: bipush 15
      // a5fe: bipush 22
      // a600: iastore
      // a601: aload 0
      // a602: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a605: bipush 3
      // a606: aaload
      // a607: bipush 66
      // a609: bipush 21
      // a60b: iastore
      // a60c: aload 0
      // a60d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a610: bipush 25
      // a612: aaload
      // a613: bipush 24
      // a615: bipush 20
      // a617: iastore
      // a618: aload 0
      // a619: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a61c: bipush 27
      // a61e: aaload
      // a61f: bipush 43
      // a621: bipush 19
      // a623: iastore
      // a624: aload 0
      // a625: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a628: bipush 27
      // a62a: aaload
      // a62b: bipush 78
      // a62d: bipush 18
      // a62f: iastore
      // a630: aload 0
      // a631: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a634: bipush 45
      // a636: aaload
      // a637: bipush 43
      // a639: bipush 17
      // a63b: iastore
      // a63c: aload 0
      // a63d: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a640: bipush 27
      // a642: aaload
      // a643: bipush 72
      // a645: bipush 16
      // a647: iastore
      // a648: aload 0
      // a649: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a64c: bipush 40
      // a64e: aaload
      // a64f: bipush 29
      // a651: bipush 15
      // a653: iastore
      // a654: aload 0
      // a655: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a658: bipush 41
      // a65a: aaload
      // a65b: bipush 0
      // a65c: bipush 14
      // a65e: iastore
      // a65f: aload 0
      // a660: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a663: bipush 19
      // a665: aaload
      // a666: bipush 57
      // a668: bipush 13
      // a66a: iastore
      // a66b: aload 0
      // a66c: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a66f: bipush 15
      // a671: aaload
      // a672: bipush 59
      // a674: bipush 12
      // a676: iastore
      // a677: aload 0
      // a678: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a67b: bipush 29
      // a67d: aaload
      // a67e: bipush 29
      // a680: bipush 11
      // a682: iastore
      // a683: aload 0
      // a684: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a687: bipush 4
      // a688: aaload
      // a689: bipush 25
      // a68b: bipush 10
      // a68d: iastore
      // a68e: aload 0
      // a68f: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a692: bipush 21
      // a694: aaload
      // a695: bipush 42
      // a697: bipush 9
      // a699: iastore
      // a69a: aload 0
      // a69b: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a69e: bipush 23
      // a6a0: aaload
      // a6a1: bipush 35
      // a6a3: bipush 8
      // a6a5: iastore
      // a6a6: aload 0
      // a6a7: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a6aa: bipush 33
      // a6ac: aaload
      // a6ad: bipush 1
      // a6ae: bipush 7
      // a6b0: iastore
      // a6b1: aload 0
      // a6b2: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a6b5: bipush 4
      // a6b6: aaload
      // a6b7: bipush 57
      // a6b9: bipush 6
      // a6bb: iastore
      // a6bc: aload 0
      // a6bd: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a6c0: bipush 17
      // a6c2: aaload
      // a6c3: bipush 60
      // a6c5: bipush 5
      // a6c6: iastore
      // a6c7: aload 0
      // a6c8: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a6cb: bipush 25
      // a6cd: aaload
      // a6ce: bipush 19
      // a6d0: bipush 4
      // a6d1: iastore
      // a6d2: aload 0
      // a6d3: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a6d6: bipush 22
      // a6d8: aaload
      // a6d9: bipush 65
      // a6db: bipush 3
      // a6dc: iastore
      // a6dd: aload 0
      // a6de: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a6e1: bipush 42
      // a6e3: aaload
      // a6e4: bipush 29
      // a6e6: bipush 2
      // a6e7: iastore
      // a6e8: aload 0
      // a6e9: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a6ec: bipush 27
      // a6ee: aaload
      // a6ef: bipush 66
      // a6f1: bipush 1
      // a6f2: iastore
      // a6f3: aload 0
      // a6f4: getfield io/legado/app/help/BytesEncodingDetect.JPFreq [[I
      // a6f7: bipush 26
      // a6f9: aaload
      // a6fa: bipush 89
      // a6fc: bipush 0
      // a6fd: iastore
      // a6fe: return
   }
}
