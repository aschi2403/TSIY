package aschi2403.tsiy.helper

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*

class LanguageHelper(var context: Context) {

    fun changeLanguage(resources: Resources, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}