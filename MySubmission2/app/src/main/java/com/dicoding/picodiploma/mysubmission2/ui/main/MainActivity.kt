package com.dicoding.picodiploma.mysubmission2.ui.main

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
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mysubmission2.R
import com.dicoding.picodiploma.mysubmission2.ui.userdetails.UserDetailsActivity
import com.dicoding.picodiploma.mysubmission2.databinding.ActivityMainBinding
import com.dicoding.picodiploma.mysubmission2.network.UserResult
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MySubmission1)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User's Search"

        binding.rvUsers.setHasFixedSize(true)

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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchMenuItem = menu.findItem(R.id.search) as MenuItem
        val searchView = searchMenuItem.actionView as SearchView
        if (viewModel.lastQuery.isNotEmpty()) {
            searchMenuItem.expandActionView()
            searchView.setQuery(viewModel.lastQuery, false)
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
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

        return true
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