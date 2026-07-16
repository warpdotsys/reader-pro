package com.htmake.reader.api.controller

import java.io.File
import java.util.Comparator

internal class `BookController$getLastBackFileFromWebdav$lambda-16$$inlined$sortByDescending$1`<T> : Comparator<T> {
   // QF: local property
internal fun <T> `<anonymous>`(a: T, b: T): Int {
      return ComparisonsKt.compareValues((b as File).lastModified(), (a as File).lastModified());
   }
}
