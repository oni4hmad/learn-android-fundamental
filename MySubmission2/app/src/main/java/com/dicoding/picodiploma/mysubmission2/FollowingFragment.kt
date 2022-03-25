package com.dicoding.picodiploma.mysubmission2

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mysubmission2.databinding.FollowingFragmentBinding

class FollowingFragment : Fragment() {

    private var _binding: FollowingFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowingViewModel
    private lateinit var username: String

    companion object {
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FollowingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = arguments?.getString(ARG_USERNAME) as String
        viewModel = ViewModelProvider(this, FollowingViewModelFactory(username)).get(FollowingViewModel::class.java)
        viewModel.userFollowing.observe(viewLifecycleOwner, { userFollowing ->
            if (userFollowing.isEmpty())
                Toast.makeText(view.context, "Following tidak ditemukan", Toast.LENGTH_SHORT).show()
            else showRecyclerList(userFollowing)
        })
        viewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })
    }

    private fun showRecyclerList(userFollowing: List<UserFollowing>) {
        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)

        val listFollowingAdapter = ListFollowingAdapter(userFollowing)
        binding.rvFollowing.adapter = listFollowingAdapter

        listFollowingAdapter.setOnItemClickCallback(object : ListFollowingAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserFollowing) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: UserFollowing) {
        val moveWithObjectIntent = Intent(activity, UserDetailsActivity::class.java)
        moveWithObjectIntent.putExtra(UserDetailsActivity.EXTRA_USERNAME, user.login)
        startActivity(moveWithObjectIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar4.visibility = View.VISIBLE
        } else {
            binding.progressBar4.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}