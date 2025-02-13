import java.util.Locale

object LanguageUtils {
    fun convertLanguageCodeToName(languageCode: String): String {
        return Locale(languageCode).displayLanguage
    }
}
