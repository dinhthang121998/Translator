import java.util.Locale

fun String.convertLanguageCodeToName(): String {
    return Locale(this).displayLanguage
}
