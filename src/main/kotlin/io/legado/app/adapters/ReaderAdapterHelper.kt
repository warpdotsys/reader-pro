package io.legado.app.adapters

object ReaderAdapterHelper {
    var readerAdapter: ReaderAdapterInterface = DefaultAdpater()

    fun setAdapter(adapter: ReaderAdapterInterface) {
        readerAdapter = adapter
    }

    fun getAdapter(): ReaderAdapterInterface = readerAdapter
}
