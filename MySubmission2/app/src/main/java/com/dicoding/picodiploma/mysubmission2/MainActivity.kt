package com.dicoding.picodiploma.mysubmission2

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mysubmission2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MySubmission1)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User's"

        binding.rvUsers.setHasFixedSize(true)
        list.addAll(listUsers)
        showRecyclerList()
    }

    private val listUsers: ArrayList<User>
        get() {
            val username = resources.getStringArray(R.array.username)
            val name = resources.getStringArray(R.array.name)
            val location = resources.getStringArray(R.array.location)
            val company = resources.getStringArray(R.array.company)
            val avatar = resources.obtainTypedArray(R.array.avatar)
            val following = resources.getStringArray(R.array.following)
            val followers = resources.getStringArray(R.array.followers)
            val repository = resources.getStringArray(R.array.repository)

            val listUser = ArrayList<User>()
            for (i in username.indices) {
                val user = User(username[i], name[i], location[i], company[i], avatar.getResourceId(i, -1), following[i], followers[i], repository[i])
                listUser.add(user)
            }
            avatar.recycle()
            return listUser
        }

    private fun showSelectedUser(user: User) {
        val moveWithObjectIntent = Intent(this@MainActivity, UserDetailsActivity::class.java)
        moveWithObjectIntent.putExtra(UserDetailsActivity.EXTRA_USER, user)
        startActivity(moveWithObjectIntent)
    }

    private fun showRecyclerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUsers.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvUsers.layoutManager = LinearLayoutManager(this)
        }

        val listUserAdapter = ListUserAdapter(list)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }
}