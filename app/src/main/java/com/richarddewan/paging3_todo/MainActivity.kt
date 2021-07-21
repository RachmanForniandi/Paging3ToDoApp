package com.richarddewan.paging3_todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.richarddewan.paging3_todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController:NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupBottomNav(navController)
    }

    private fun setupBottomNav(navController: NavController) {
        val bottomNav = binding.bottomNavigation as BottomNavigationView
        bottomNav.setupWithNavController(navController)

        /*
        The set of destinations by id considered at the top level of your information hierarchy.
        The Up button will not be displayed when on these destinations.
         */
        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.nav_flow,R.id.nav_flow_rm,R.id.nav_rxjava, R.id.nav_rxjava_rm
            )
        )
        /*
       By calling this method, the title in the action bar will automatically be updated when
       the destination changes
        */
        setupActionBarWithNavController(navController,appBarConfig)


    }
}