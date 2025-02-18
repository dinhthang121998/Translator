package com.example.translator.domain.model

sealed class SearchLanguageItem {
    data class LanguageItem(
        val languageName: String = "",
        val languageCode: String = "",
        val downloadable: Downloadable = Downloadable.NO_NEED_DOWNLOAD,
    ) : SearchLanguageItem()

    data class TitleItem(
        val title: String = "",
    ) : SearchLanguageItem()
}

enum class Downloadable(val id: Int) {
    IS_DOWNLOADED(1),
    NEED_DOWNLOAD(2),
    NO_NEED_DOWNLOAD(3),
    ;

    companion object {
        fun fromId(id: Int): Downloadable {
            return entries.first { it.id == id }
        }
    }
}
