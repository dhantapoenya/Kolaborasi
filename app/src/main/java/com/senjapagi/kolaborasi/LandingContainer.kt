package com.senjapagi.kolaborasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.senjapagi.kolaborasi.Fragment.fragment_user_landing

class LandingContainer : AppCompatActivity() {

    lateinit var fragmentUserLanding : fragment_user_landing
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_container)

        fragmentUserLanding = fragment_user_landing()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.landingFrameLayout, fragmentUserLanding)
            .commit()

    }

    fun transactionFragment(Fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.landingFrameLayout, Fragment)
            .commit()
    }
}