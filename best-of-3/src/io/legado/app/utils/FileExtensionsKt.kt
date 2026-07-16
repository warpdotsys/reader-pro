package io.legado.app.utils

import java.io.File
import java.util.Arrays

public fun File.getFile(vararg subDirFiles: String): File {
   return new File(FileUtils.INSTANCE.getPath(`$this$getFile`, Arrays.copyOf(subDirFiles, subDirFiles.length)));
}

public fun File.exists(vararg subDirFiles: String): Boolean {
   return getFile(`$this$exists`, Arrays.copyOf(subDirFiles, subDirFiles.length)).exists();
}
