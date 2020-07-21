package com.senjapagi.kolaborasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.AnimationUtils.loadAnimation
import com.senjapagi.kolaborasi.Services.Constant
import com.senjapagi.kolaborasi.Services.Preference
import com.senjapagi.kolaborasi.Services.URL
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard_user.*
import kotlinx.android.synthetic.main.activity_dashboard_user.btnToggleNavdraw
import kotlinx.android.synthetic.main.custom_navdraw.*
import kotlinx.android.synthetic.main.user_profile.*

class user_landing : AppCompatActivity() {

    var isNavDrawOpen: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_user)
        lyt_navdraw.visibility = View.GONE
        isNavDrawOpen = false

        Picasso.get()
            .load(URL.PROFILE_PIC_URL+Preference(this).getPrefString(Constant.USERNAME))
            .memoryPolicy(MemoryPolicy.NO_CACHE )
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .error(R.drawable.add_profile)
            .into(ndIvProfile)


        ndTvNama.text = Preference(this).getPrefString(Constant.NAMA)
        ndTvEmail.text = Preference(this).getPrefString(Constant.EMAIL)
        ndTvUsername.text = Preference(this).getPrefString(Constant.USERNAME)

        btnToggleNavdraw.setOnClickListener {
            NavDrawToggle(1)
        }

        btnCloseNavDraw.setOnClickListener {
            NavDrawToggle(1)
        }

        lyt_navdraw_shadow.setOnClickListener {
            NavDrawToggle(1)
        }

        ndBtnProfile.setOnClickListener {
            NavDrawToggle(1)
            startActivity(Intent(this@user_landing, user_profile::class.java))
        }

        btnTextProfile.setOnClickListener {
            NavDrawToggle(0)
            startActivity(Intent(this@user_landing, user_profile::class.java))
        }
        btnTextRekruitasi.setOnClickListener {
            NavDrawToggle(0)
            startActivity(Intent(this@user_landing, user_recruitment_home::class.java))
        }
    }

    fun NavDrawToggle(indicator: Int) {
        if (indicator == 0) {
        closeNavDraw()
        } else {
            if (isNavDrawOpen) {
            closeNavDraw()
            } else {
               openNavDraw()
            }
        }

    }

    fun openNavDraw(){
        isNavDrawOpen = true
        lyt_navdraw.visibility = View.VISIBLE
        lyt_navdraw.animation =
            AnimationUtils.loadAnimation(this, R.anim.fade_transition_animation)
        lyt_landing_user.background.alpha = 200
    }

    fun closeNavDraw(){
        isNavDrawOpen = false
        lyt_navdraw.animation =
            AnimationUtils.loadAnimation(this, R.anim.fade_transition_animation_go)
        lyt_navdraw.visibility = View.GONE
        lyt_landing_user.background.alpha = 255
    }

}