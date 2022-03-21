package com.dicoding.picodiploma.restaurantreview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.restaurantreview.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>() // ktx

    companion object {
        private const val TAG = "MainActivity"
        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        /* inisialisasi ViewModel dan observe setiap data dan panggil fungsi yang dibutuhkan */
        /*val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)*/ // diganti dg ktx
        mainViewModel.restaurant.observe(this, { restaurant ->
            setRestaurantData(restaurant)
        })
        mainViewModel.listReview.observe(this, { customerReviews ->
            setReviewData(customerReviews)
        })
        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })
        mainViewModel.snackbarText.observe(this, {
            /*Snackbar.make(
                window.decorView.rootView,
                it,
                Snackbar.LENGTH_SHORT
            ).show()*/
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        binding.btnSend.setOnClickListener { view ->
            /* postReview dg ViewModel */
            mainViewModel.postReview(binding.edReview.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setRestaurantData(restaurant: Restaurant) {
        binding.tvTitle.text = restaurant.name
        binding.tvDescription.text = restaurant.description
        Glide.with(this@MainActivity)
            .load("https://restaurant-api.dicoding.dev/images/large/${restaurant.pictureId}")
            .into(binding.ivPicture)
    }

    private fun setReviewData(customerReviews: List<CustomerReviewsItem>) {
        /*val listReview = ArrayList<String>()
        for (review in customerReviews) {
            listReview.add(
                """
                ${review.review}
                - ${review.name}
                """.trimIndent()
            )
        }*/
        val listReview = customerReviews.map {
            "${it.review}\n- ${it.name}"
        }
        val adapter = ReviewAdapter(listReview)
        binding.rvReview.adapter = adapter
        binding.edReview.setText("")
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}