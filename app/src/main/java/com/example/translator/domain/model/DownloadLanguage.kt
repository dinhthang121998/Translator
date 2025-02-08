package com.example.translator.domain.model

data class DownloadLanguage(
    val languageName: String = "",
    val isDownload: Downloadable = Downloadable.NO_NEED_DOWNLOAD
)

enum class Downloadable {
    IS_DOWNLOADED,
    NEED_DOWNLOAD,
    NO_NEED_DOWNLOAD,
}
