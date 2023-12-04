package com.example.uas


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.uas.preference.SharedPreferencesManager

class MainActivity : AppCompatActivity() {

    private val sharedPreferencesManager by lazy {
        SharedPreferencesManager(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {


        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (sharedPreferencesManager.isLoggedIn) {
            val intent = Intent(this, AppActivity::class.java)
            startActivity(intent)
        }


        // Initialize the Navigation Component
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Create an AppBarConfiguration with the top-level destinations
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.loginFragment,
            R.id.registerFragment
            // Add more top-level destinations if needed
        ).build()

        // Set up the ActionBar with the Navigation Controller
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // Handle Up button navigation
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return NavigationUI.navigateUp(navController, null)
    }
}