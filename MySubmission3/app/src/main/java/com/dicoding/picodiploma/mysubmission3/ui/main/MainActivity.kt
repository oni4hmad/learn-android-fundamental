package com.dicoding.picodiploma.mysubmission3.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mysubmission3.R
import com.dicoding.picodiploma.mysubmission3.ui.userdetails.UserDetailsActivity
import com.dicoding.picodiploma.mysubmission3.databinding.ActivityMainBinding
import com.dicoding.picodiploma.mysubmission3.network.UserResult
import com.dicoding.picodiploma.mysubmission3.ui.favorite.FavoriteActivity
import com.dicoding.picodiploma.mysubmission3.ui.settings.SettingPreferences
import com.dicoding.picodiploma.mysubmission3.ui.settings.SettingsActivity
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MySubmission1)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User's Search"

        viewModel = ViewModelProvider(this, MainViewModelFactory(
            SettingPreferences.getInstance(dataStore)
        )).get(MainViewModel::class.java)

        binding.rvUsers.setHasFixedSize(true)

        viewModel.getThemeSettings().observe(this, { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })
        viewModel.listUsers.observe(this, { users ->
            showRecyclerList(users)
        })
        viewModel.toastText.observe(this, {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(this@MainActivity, toastText, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.isLoading.observe(this, {
            showLoading(it)
        })
        viewModel.snackbarText.observe(this, {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        setSearchView()
    }

    private fun setSearchView() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.search
        if (viewModel.lastQuery.isNotEmpty()) {
            searchView.setQuery(viewModel.lastQuery, false)
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            /* respon ketika submit */
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchUser(query)
                searchView.clearFocus()
                return true
            }
            /* respon ketika ada perubahan huruf */
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.lastQuery = newText
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun showRecyclerList(users: List<UserResult>) {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUsers.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvUsers.layoutManager = LinearLayoutManager(this)
        }

        val listUserAdapter = ListUserAdapter(users)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserResult) {
                 showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: UserResult) {
        val moveWithObjectIntent = Intent(this@MainActivity, UserDetailsActivity::class.java)
        moveWithObjectIntent.putExtra(UserDetailsActivity.EXTRA_USERNAME, user.login)
        startActivity(moveWithObjectIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}