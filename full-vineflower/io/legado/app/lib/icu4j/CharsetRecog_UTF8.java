package io.legado.app.lib.icu4j;

class CharsetRecog_UTF8 extends CharsetRecognizer {
   @Override
   String getName() {
      return "UTF-8";
   }

   @Override
   CharsetMatch match(CharsetDetector det) {
      boolean hasBOM = false;
      int numValid = 0;
      int numInvalid = 0;
      byte[] input = det.fRawInput;
      int trailBytes = 0;
      if (det.fRawLength >= 3 && (input[0] & 255) == 239 && (input[1] & 255) == 187 && (input[2] & 255) == 191) {
         hasBOM = true;
      }

      for (int i = 0; i < det.fRawLength; i++) {
         int b = input[i];
         if ((b & 128) != 0) {
            if ((b & 224) == 192) {
               trailBytes = 1;
            } else if ((b & 240) == 224) {
               trailBytes = 2;
            } else {
               if ((b & 248) != 240) {
                  numInvalid++;
                  continue;
               }

               trailBytes = 3;
            }

            while (++i < det.fRawLength) {
               int var11 = input[i];
               if ((var11 & 192) != 128) {
                  numInvalid++;
                  break;
               }

               if (--trailBytes == 0) {
                  numValid++;
                  break;
               }
            }
         }
      }

      int confidence = 0;
      if (hasBOM && numInvalid == 0) {
         confidence = 100;
      } else if (hasBOM && numValid > numInvalid * 10) {
         confidence = 80;
      } else if (numValid > 3 && numInvalid == 0) {
         confidence = 100;
      } else if (numValid > 0 && numInvalid == 0) {
         confidence = 80;
      } else if (numValid == 0 && numInvalid == 0) {
         confidence = 15;
      } else if (numValid > numInvalid * 10) {
         confidence = 25;
      }

      return confidence == 0 ? null : new CharsetMatch(det, this, confidence);
   }
}
