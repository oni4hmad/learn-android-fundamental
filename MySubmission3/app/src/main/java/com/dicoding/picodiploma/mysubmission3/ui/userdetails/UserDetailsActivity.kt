package com.dicoding.picodiploma.mysubmission3.ui.userdetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.mysubmission3.R
import com.dicoding.picodiploma.mysubmission3.database.FavoriteUser
import com.dicoding.picodiploma.mysubmission3.databinding.ActivityUserDetailsBinding
import com.dicoding.picodiploma.mysubmission3.ui.settings.SettingsActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var username: String
    private lateinit var viewModel: UserDetailsViewModel
    private var isFavorite = false

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.ab_title_detailuser)
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        username = intent.getStringExtra(EXTRA_USERNAME) as String
        viewModel = ViewModelProvider(this, UserDetailsViewModelFactory(this.application, username))[UserDetailsViewModel::class.java]

        viewModel.userInfo.observe(this) { userInfo ->
            Glide.with(this@UserDetailsActivity)
                .load(userInfo.avatarUrl)
                .into(binding.imgUser)
            binding.apply {
                tvName.text = userInfo.name
                tvUsername.text = getString(R.string.username, userInfo.login)
                tvLocation.text = userInfo.location
                if (userInfo.location.isNullOrEmpty()) ivLocation.visibility = View.INVISIBLE
                tvUserCount.text = getString(R.string.user_count, userInfo.followers, userInfo.following, userInfo.publicRepos)
            }
            showFab()
        }
        viewModel.isFavoriteUser().observe(this) { isFavoriteUser: Boolean ->
            if (isFavoriteUser)
                setFabFavorite(true)
            else setFabFavorite(false)
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.fabAddFavorite.setOnClickListener {
            viewModel.userInfo.value?.let { userInfo ->
                Log.d("userInfo", userInfo.toString())
                if (this.isFavorite) {
                    viewModel.deleteFavoriteUser(
                        FavoriteUser(userInfo.id, userInfo.login, userInfo.avatarUrl)
                    )
                    setFabFavorite(false)
                } else {
                    viewModel.insertFavoriteUser(
                        FavoriteUser(userInfo.id, userInfo.login, userInfo.avatarUrl)
                    )
                    setFabFavorite(true)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.userdetails_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "https://github.com/${username}")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }
            R.id.settings -> {
                val intent = Intent(this@UserDetailsActivity, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                imgUser.visibility = View.INVISIBLE
                tvUsername.visibility = View.INVISIBLE
                tvName.visibility = View.INVISIBLE
                ivLocation.visibility = View.INVISIBLE
                tvLocation.visibility = View.INVISIBLE
                tvUserCount.visibility = View.INVISIBLE
                progressBar2.visibility = View.VISIBLE
            } else {
                imgUser.visibility = View.VISIBLE
                tvUsername.visibility = View.VISIBLE
                tvName.visibility = View.VISIBLE
                ivLocation.visibility = View.VISIBLE
                tvLocation.visibility = View.VISIBLE
                tvUserCount.visibility = View.VISIBLE
                progressBar2.visibility = View.GONE
            }
        }
    }

    private fun showFab() {
        binding.fabAddFavorite.visibility = View.VISIBLE
    }

    private fun setFabFavorite(isFavorite: Boolean) {
        binding.fabAddFavorite.apply {
            if (isFavorite) {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_24))
                this@UserDetailsActivity.isFavorite = true
            } else {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_border_24))
                this@UserDetailsActivity.isFavorite = false
            }
        }
    }
}