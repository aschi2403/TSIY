package aschi2403.tsiy.screens.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.helper.LanguageHelper
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = this.getSharedPreferences("settings", Context.MODE_PRIVATE)

        requestPermissions()

        LanguageHelper(this).changeLanguage(
            resources,
            sharedPreferences.getString("language", "")!!
        )

        setContentView(R.layout.activity_main)

        val appBarLayout = findViewById<Toolbar>(R.id.appBarLayout)
        setSupportActionBar(appBarLayout)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        AppCompatDelegate.setDefaultNightMode(sharedPreferences.getInt("darkMode", 0))

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

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                showDialog()
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            500
        )
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.locationPermission)

        builder.setMessage("Location is needed to record the distance of an activity.")

        builder.setPositiveButton("ASK AGAIN") { _, _ ->
            requestLocationPermission()
        }
        builder.setNegativeButton(R.string.cancel, null)

        val dialog = builder.create()
        dialog.show()
    }
}
