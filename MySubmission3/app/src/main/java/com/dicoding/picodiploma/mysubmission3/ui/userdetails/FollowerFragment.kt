package com.dicoding.picodiploma.mysubmission3.ui.userdetails

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mysubmission3.databinding.FollowerFragmentBinding
import com.dicoding.picodiploma.mysubmission3.network.UserFollower
import com.google.android.material.snackbar.Snackbar

class FollowerFragment : Fragment() {

    private var _binding: FollowerFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowerViewModel
    private lateinit var username: String

    companion object {
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FollowerFragmentBinding.inflate(inflater, container, false)
        val context = activity?.application
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = arguments?.getString(ARG_USERNAME) as String
        viewModel = ViewModelProvider(this, FollowerViewModelFactory(username)).get(
            FollowerViewModel::class.java)
        viewModel.userFollowers.observe(viewLifecycleOwner, { userFollowers ->
            showRecyclerList(userFollowers)
        })
        viewModel.toastText.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(view.context, toastText, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })
        viewModel.snackbarText.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    binding.root.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showRecyclerList(userFollowers: List<UserFollower>) {
        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)

        val listFollowerAdapter = ListFollowerAdapter(userFollowers)
        binding.rvFollowers.adapter = listFollowerAdapter

        listFollowerAdapter.setOnItemClickCallback(object :
            ListFollowerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserFollower) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: UserFollower) {
        val moveWithObjectIntent = Intent(activity, UserDetailsActivity::class.java)
        moveWithObjectIntent.putExtra(UserDetailsActivity.EXTRA_USERNAME, user.login)
        startActivity(moveWithObjectIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar3.visibility = View.VISIBLE
        } else {
            binding.progressBar3.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}