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
import com.example.yoga.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var mNavController: NavController

    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(R.id.yogaFragment, R.id.profileFragment)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bottomNavigationView.background = null
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container)
                as NavHostFragment
        mNavController = navHostFragment.findNavController()

        binding.bottomNavigationView.setupWithNavController(mNavController)

        setSupportActionBar(binding.toolbar)
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
        binding.fab.visibility = View.VISIBLE
        binding.bottomAppBar.visibility = View.VISIBLE
    }

    private fun hideBottomBar() {
        binding.fab.visibility = View.GONE
        binding.bottomAppBar.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        return mNavController.navigateUp() || super.onSupportNavigateUp()
    }
}