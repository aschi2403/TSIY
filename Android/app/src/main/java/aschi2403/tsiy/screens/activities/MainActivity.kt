package aschi2403.tsiy.screens.activities

import android.Manifest
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import aschi2403.tsiy.BuildConfig
import aschi2403.tsiy.R
import aschi2403.tsiy.helper.LanguageHelper
import aschi2403.tsiy.helper.PermissionHelper
import kotlinx.android.synthetic.main.activity_main.main_nav
import org.osmdroid.config.IConfigurationProvider
import org.osmdroid.tileprovider.util.StorageUtils.getStorage

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = this.getSharedPreferences("settings", Context.MODE_PRIVATE)

        PermissionHelper().askPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        )

        LanguageHelper(this).changeLanguage(
            resources,
            sharedPreferences.getString("language", "")!!
        )

        setContentView(R.layout.activity_main)

        val appBarLayout = findViewById<Toolbar>(R.id.appBarLayout)
        setSupportActionBar(appBarLayout)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (!sharedPreferences.contains("darkMode")) {
            when (applicationContext.resources.configuration.uiMode and UI_MODE_NIGHT_MASK) {
                UI_MODE_NIGHT_NO -> {
                    sharedPreferences.edit().putInt("darkMode", AppCompatDelegate.MODE_NIGHT_NO).apply()
                }
                UI_MODE_NIGHT_YES -> {
                    sharedPreferences.edit().putInt("darkMode", AppCompatDelegate.MODE_NIGHT_YES).apply()
                }
            }
        }

        AppCompatDelegate.setDefaultNightMode(sharedPreferences.getInt("darkMode", 0))

        val provider: IConfigurationProvider = org.osmdroid.config.Configuration.getInstance()
        provider.userAgentValue = BuildConfig.APPLICATION_ID
        provider.osmdroidBasePath = getStorage()
        provider.osmdroidTileCache = getStorage()
        setupView()
    }

    private fun setupView() {
        // Finding the Navigation Controller
        val navController = findNavController(R.id.fragNavHost)

        // Setting Navigation Controller with the BottomNavigationView
        main_nav.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        // menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }
}
