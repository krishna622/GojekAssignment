package com.krishna.gojekassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.krishna.gojekassignment.ui.fragment.TrendingFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(
                R.id.mainView,
                TrendingFragment.newInstance()
            )
                .commit()
        }
    }
}
