package com.example.yoga

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mNavController: NavController

    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(R.id.yogaFragment, R.id.profileFragment)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation_view.background = null
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container)
                as NavHostFragment
        mNavController = navHostFragment.findNavController()

        bottom_navigation_view.setupWithNavController(mNavController)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(mNavController, appBarConfiguration)

        mNavController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.yogaFragment -> showBottomBar()
                R.id.profileFragment -> showBottomBar()
                else -> hideBottomBar()
            }
        }
    }

    private fun showBottomBar() {
        fab.visibility = View.VISIBLE
        bottom_app_bar.visibility = View.VISIBLE
    }

    private fun hideBottomBar() {
        fab.visibility = View.GONE
        bottom_app_bar.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        return mNavController.navigateUp() || super.onSupportNavigateUp()
    }
}