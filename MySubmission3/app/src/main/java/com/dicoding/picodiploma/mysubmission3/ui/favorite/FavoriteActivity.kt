package com.dicoding.picodiploma.mysubmission3.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mysubmission3.R
import com.dicoding.picodiploma.mysubmission3.database.FavoriteUser
import com.dicoding.picodiploma.mysubmission3.databinding.ActivityFavoriteBinding
import com.dicoding.picodiploma.mysubmission3.ui.settings.SettingsActivity
import com.dicoding.picodiploma.mysubmission3.ui.userdetails.UserDetailsActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel

    private lateinit var adapter: ListFavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite User"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = ListFavoriteAdapter()
        adapter.setOnItemClickCallback(object : ListFavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(user: FavoriteUser) {
                showSelectedUser(user)
            }
        })

        binding.rvFavoriteUsers.layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUsers.setHasFixedSize(true)
        binding.rvFavoriteUsers.adapter = adapter

        viewModel = ViewModelProvider(this, FavoriteViewModelFactory(application))[FavoriteViewModel::class.java]

        viewModel.getAllFavoriteUsers().observe(this, { favoriteUsers ->
            if (favoriteUsers != null) {
                adapter.setListFavoriteUsers(favoriteUsers)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.favorite_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.settings -> {
                val intent = Intent(this@FavoriteActivity, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun showSelectedUser(user: FavoriteUser) {
        val moveWithObjectIntent = Intent(this@FavoriteActivity, UserDetailsActivity::class.java)
        moveWithObjectIntent.putExtra(UserDetailsActivity.EXTRA_USERNAME, user.login)
        startActivity(moveWithObjectIntent)
    }
}