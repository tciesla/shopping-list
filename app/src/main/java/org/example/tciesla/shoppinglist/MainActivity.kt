package org.example.tciesla.shoppinglist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import org.example.tciesla.shoppinglist.databinding.ActivityMainBinding
import org.example.tciesla.shoppinglist.state.ShoppingListState

const val DEFAULT_SHOPPING_LIST_NAME = "default"

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ShoppingListState.initialize(applicationContext)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        ShoppingListState.register(this) {
            invalidateOptionsMenu()
            updateActionBarTitle()
        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        updateActionBarTitle()
    }

    override fun onDestroy() {
        super.onDestroy()
        ShoppingListState.unregister(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        ShoppingListState.getShoppingListNames().forEach {
            menu?.add(it)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        ShoppingListState.onShoppingListSelected(item.toString())
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun updateActionBarTitle() {
        supportActionBar?.title =
            "${getString(R.string.app_name)} > ${ShoppingListState.getSelectedShoppingListName()}"
    }
}