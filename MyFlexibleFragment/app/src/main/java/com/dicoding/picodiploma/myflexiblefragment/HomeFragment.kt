package com.dicoding.picodiploma.myflexiblefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.commit

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnCategory: Button = view.findViewById(R.id.btn_category)
        btnCategory.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_category) {
            val mCategoryFragment = CategoryFragment()
            val mFragmentManager = parentFragmentManager
            /*mFragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, mCategoryFragment, CategoryFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }*/

            /* fragment ktx */
            mFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.frame_container, mCategoryFragment, CategoryFragment::class.java.simpleName)
            }
        }
    }

}
