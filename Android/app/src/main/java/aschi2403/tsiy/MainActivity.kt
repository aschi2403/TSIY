package aschi2403.tsiy

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import aschi2403.tsiy.screens.HomeFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val statisticsFragment = StatisticsFragment()
    private val settingsFragment = SettingsFragment()
    private val weightFragment = WeightFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        main_nav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    setFragment(homeFragment)
                }

                R.id.nav_settings -> {
                    setFragment(settingsFragment)
                }

                R.id.nav_statistics -> {
                    setFragment(statisticsFragment)
                }

                R.id.nav_weight -> {
                    setFragment(weightFragment)
                }
            }
            true
        }

        main_nav.selectedItemId = R.id.nav_home
        setFragment(homeFragment)

    }

    private fun setFragment(fragment: Fragment) {
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
