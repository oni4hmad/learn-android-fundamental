package com.dicoding.picodiploma.mysubmission2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.mysubmission2.databinding.ActivityUserDetailsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var username: String
    private lateinit var viewModel: UserDetailsViewModel

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

        supportActionBar?.title = "Detail User"

        username = intent.getStringExtra(EXTRA_USERNAME) as String
//        by viewModels{ UserDetailsViewModelFactory(username) }
        viewModel = ViewModelProvider(this, UserDetailsViewModelFactory(username)).get(UserDetailsViewModel::class.java)

        /*val user = intent.getParcelableExtra<User>(EXTRA_USERNAME) as User
        binding.imgUser.setImageResource(user.avatar)
        binding.tvName.text = user.name
        binding.tvUsername.text = getString(R.string.username, user.username)
        binding.tvLocation.text = user.location
        binding.tvUserCount.text = getString(R.string.user_count, user.followers, user.following, user.repository)*/

        viewModel.userInfo.observe(this, { userInfo ->
            Glide.with(this@UserDetailsActivity)
                .load(userInfo.avatarUrl)
                .into(binding.imgUser)
            binding.tvName.text = userInfo.name
            binding.tvUsername.text = getString(R.string.username, userInfo.login)
            binding.tvLocation.text = userInfo.location
            if (userInfo.location.isNullOrEmpty()) binding.ivLocation.visibility = View.GONE
            binding.tvUserCount.text = getString(R.string.user_count, userInfo.followers, userInfo.following, userInfo.publicRepos)
        })
        viewModel.isLoading.observe(this, {
            showLoading(it)
        })

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.share_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "https://github.com/${binding.tvUsername.text.drop(1)}")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar2.visibility = View.VISIBLE
        } else {
            binding.progressBar2.visibility = View.GONE
        }
    }
}