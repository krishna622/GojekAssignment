package com.krishna.gojekassignment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.krishna.gojekassignment.R
import com.krishna.gojekassignment.repository.remote.NetworkChecker

class NoInternetFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_no_connection, container, false)
        val retry = view.findViewById<TextView>(R.id.retryText)
        retry.setOnClickListener {
            if(NetworkChecker.isInternetAvailable(context!!)) {
                activity?.supportFragmentManager?.beginTransaction()?.replace(
                    R.id.mainView,
                    TrendingFragment.newInstance()
                )
                    ?.commit()
            }else{
                Toast.makeText(context, "Please check internet Connection.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = NoInternetFragment()
    }
}