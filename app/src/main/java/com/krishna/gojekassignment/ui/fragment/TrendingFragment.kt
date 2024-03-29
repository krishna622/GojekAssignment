package com.krishna.gojekassignment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.krishna.gojekassignment.R
import com.krishna.gojekassignment.repository.remote.NetworkChecker
import com.krishna.gojekassignment.ui.adapter.MyTrendingRecyclerViewAdapter
import com.krishna.gojekassignment.ui.model.TrendDataItem
import com.krishna.gojekassignment.ui.utility.NameComparator
import com.krishna.gojekassignment.ui.utility.StarComparator
import com.krishna.gojekassignment.viewmodel.TrendingViewModel
import java.util.*
import kotlin.collections.ArrayList
import com.krishna.gojekassignment.viewmodel.TrendingViewModelFactory
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrendingFragment : Fragment() {

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private lateinit var adapter: MyTrendingRecyclerViewAdapter
//    private lateinit var trendingViewModel: TrendingViewModel

    val trendingViewModel : TrendingViewModel by viewModel()

    private var list: List<TrendDataItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trending, container, false)
        val menuImg = view.findViewById<ImageView>(R.id.menuImg)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        createViewModel()
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MyTrendingRecyclerViewAdapter()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
//        adapter.setTrendingData(list)
        swipeRefreshLayout?.setOnRefreshListener {
            if(NetworkChecker.isInternetAvailable(context?.applicationContext!!)){
                trendingViewModel.refreshTrendingData()
            }else{
                swipeRefreshLayout?.isRefreshing = false
                replaceFragment(NoInternetFragment.newInstance())
            }
        }
        observeData()

        menuImg.setOnClickListener {
            popupMenuDialog(it)
        }
        return view
    }


    fun popupMenuDialog(view: View) {
        try {
            val popupMenu = PopupMenu(context!!, view)
            popupMenu.inflate(R.menu.main_menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener { item ->
                val i = item.itemId
                if (i == R.id.sort_by_star) {
                    Collections.sort(list, StarComparator())
                } else if (i == R.id.sort_by_name) {
                    Collections.sort(list, NameComparator())
                }
                adapter.setTrendingData(list)
                popupMenu.dismiss()
                false
            }
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }

    private fun createViewModel(){
//        val viewModelFactory = TrendingViewModelFactory(activity!!.application)
//        trendingViewModel = ViewModelProviders.of(this, viewModelFactory).get(TrendingViewModel::class.java)
    }

    private fun observeData() {
        trendingViewModel.getTrendingData()?.observe(this, Observer<List<TrendDataItem>> {
            it?.let {
                list = it
                adapter.setTrendingData(it)
            }
        })
        trendingViewModel.getLoadingStatus().observe(this, Observer {
            if (it) {
                swipeRefreshLayout?.isRefreshing = false
            }
        })

        trendingViewModel.getIsDataNotAvailable().observe(this, Observer {
            if(it){
                replaceFragment(NoInternetFragment.newInstance())
            }
        })
    }


    companion object {
        @JvmStatic
        fun newInstance() = TrendingFragment()
    }


    fun replaceFragment(fragment: Fragment){
        fragmentManager?.beginTransaction()?.replace(R.id.mainView, fragment)?.commit()
    }
}
