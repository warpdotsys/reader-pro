package io.legado.app.adapters

public object ReaderAdapterHelper {
   public final var readerAdapter: ReaderAdapterInterface = (new DefaultAdpater()) as ReaderAdapterInterface
      internal set

   public fun setAdapter(adapter: ReaderAdapterInterface) {
      readerAdapter = adapter;
   }

   public fun getAdapter(): ReaderAdapterInterface {
      return readerAdapter;
   }
}
