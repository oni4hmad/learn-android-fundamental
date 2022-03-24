package com.dicoding.picodiploma.mysubmission2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dicoding.picodiploma.mysubmission2.databinding.ActivityUserDetailsBinding

class UserDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUserDetailsBinding

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        binding.imgUser.setImageResource(user.avatar)
        binding.tvName.text = user.name
        binding.tvUsername.text = getString(R.string.username, user.username)
        binding.tvLocation.text = user.location
        binding.tvUserCount.text = getString(R.string.user_count, user.followers, user.following, user.repository)
        binding.btnShare.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "https://github.com/${binding.tvUsername.text.drop(1)}")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}