package org.example.tciesla.shoppinglist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import org.example.tciesla.shoppinglist.databinding.ActivityMainBinding
import org.example.tciesla.shoppinglist.repositories.ShoppingListRepository
import org.example.tciesla.shoppinglist.viewmodels.ShoppingListViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val shoppingListViewModel: ShoppingListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ShoppingListRepository.initialize(applicationContext)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        shoppingListViewModel.state.observe(this) { uiState ->
            invalidateOptionsMenu()
            updateActionBarTitle(uiState.selectedList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        shoppingListViewModel.state.value?.shoppingList?.map { it.list }?.toSet()?.forEach {
            menu?.add(it)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        shoppingListViewModel.onShoppingListSelected(item.toString())
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun updateActionBarTitle(selectedList: String) {
        supportActionBar?.title = "${getString(R.string.app_name)} > $selectedList"
    }
}