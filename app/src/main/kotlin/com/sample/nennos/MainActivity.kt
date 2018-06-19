package com.sample.nennos

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.sample.nennos.ktx.toParcelable
import com.sample.nennos.persistence.Seed
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var destination: NavDestination? = null

    private val navController by lazy(LazyThreadSafetyMode.NONE) {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        NavigationUI.setupActionBarWithNavController(this, navController)
        navController.addOnNavigatedListener { _, newDestination ->
            destination = newDestination
            if (isAHomePage()) {
                customPizza.show()
            } else {
                customPizza.hide()
            }
            invalidateOptionsMenu()
        }

        customPizza.setOnClickListener {
            val args = bundleOf("details" to Seed.custom.toParcelable())
            navController.navigate(R.id.show_pizza_detail, args)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)

        val cartMenuItem = menu.findItem(R.id.show_cart_page)
        cartMenuItem?.actionView?.setOnClickListener {
            navController.navigate(R.id.show_cart_page)
        }

        cartMenuItem?.isVisible = isAHomePage()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun isAHomePage(): Boolean = destination?.id == R.id.pizza_list_home
}
