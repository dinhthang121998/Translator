package com.example.translator.domain.model

sealed class SearchLanguageItem {
    data class LanguageItem(
        val languageName: String = "",
        val isDownload: Downloadable = Downloadable.NO_NEED_DOWNLOAD,
    ) : SearchLanguageItem()

    data class TitleItem(
        val title: String = "",
    ) : SearchLanguageItem()
}

enum class Downloadable {
    IS_DOWNLOADED,
    NEED_DOWNLOAD,
    NO_NEED_DOWNLOAD,
}
