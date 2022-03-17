package com.dicoding.picodiploma.mytablayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/* Apabila Anda menerapkannya di Fragment, gunakanFragmentActivity.  */
class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var appName: String = ""  /* jika ingin kirim data dari activity ke fragment */
                                    /* tambahkan property baru trus isi waktu di activity
                                    *   lalu kirim di createFragment() */

    /*  Jangan sampai Anda menuliskan tiga, tetapi fragment yang didefinisikan hanya dua.
        Bisa-bisa akan terjadi eror karena hal ini. */
    override fun createFragment(position: Int): Fragment {
        /*var fragment: Fragment? = null
        when (position) {
            0 -> fragment = HomeFragment()
            1 -> fragment = ProfileFragment()
        }
        return fragment as Fragment*/

        val fragment = HomeFragment()
        /* kirim data integer ke fragment Home : satu fragment yg mirip */
        fragment.arguments = Bundle().apply {
            putInt(HomeFragment.ARG_SECTION_NUMBER, position + 1)
            putString(HomeFragment.ARG_NAME, appName)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}