package com.example.uas

import android.content.ClipData.Item
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        val name = intent.getStringExtra("name")

        val overviewFragment = MainFragment()

        val profileFragment = ProfileFragment()

        val calendarFragment = CalendarFragment()

        val statisticsFragment = StatisticsFragment()

        val addFragment = AddFragment()



        setCurrentFragment(overviewFragment)

//        change fragment when bottom navigation is clicked
        val bottomNavigation = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.overview -> setCurrentFragment(overviewFragment)
                R.id.profile -> setCurrentFragment(profileFragment)
                R.id.calender -> setCurrentFragment(calendarFragment)
                R.id.statistics -> setCurrentFragment(statisticsFragment)
                R.id.add -> setCurrentFragment(addFragment)
            }
            true
        }
    }
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }


}