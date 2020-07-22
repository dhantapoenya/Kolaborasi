package com.senjapagi.kolaborasi

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.senjapagi.kolaborasi.Services.Constant
import com.senjapagi.kolaborasi.Services.Preference
import com.senjapagi.kolaborasi.Services.URL
import kotlinx.android.synthetic.main.activity_dashboard_user.*
import kotlinx.android.synthetic.main.activity_dashboard_user.btnToggleNavdraw
import kotlinx.android.synthetic.main.custom_navdraw.*
import kotlinx.android.synthetic.main.user_profile.*
import org.json.JSONObject

class user_profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)
        lyt_user_profile.background.alpha = 255
        lyt_navdraw.visibility = View.GONE


        ndBtnHome.setOnClickListener { changeLayout(user_landing()) }
        ndBtnLogOut.setOnClickListener { Logout(this).logoutDialog() }
        btnToggleNavdraw.setOnClickListener { NavDrawToggle("open")}
        btnCloseNavDraw.setOnClickListener { NavDrawToggle("close")}
        lyt_navdraw_shadow.setOnClickListener { NavDrawToggle("close") }

        retrieveUser()

    }

    fun makeToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    fun retrieveUser(){
        makeToast("retreive USer")
        AndroidNetworking.post(URL.GET_DETAIL_USER)
            .addBodyParameter("id",Preference(this).getPrefString(Constant.ID))
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    if (response?.getBoolean("success")!!){
                        makeToast(response.toString())
                    }else{
                        makeToast(response.toString())
                    }
                }

                override fun onError(anError: ANError?) {
                    makeToast(anError?.errorDetail.toString())
                }

            })
    }

    private fun changeLayout(dest : Activity){
        NavDrawToggle("close")
        finish()
        startActivity(Intent(this@user_profile,dest::class.java))
    }

    override fun onBackPressed() {
        NavDrawToggle("close")
        finish()
        startActivity(Intent(this@user_profile,user_landing::class.java))
    }

    private fun NavDrawToggle(indicator: String) {
        if (indicator.equals("open")) {
            lyt_navdraw.visibility = View.VISIBLE
            lyt_navdraw.animation = loadAnimation(this, R.anim.fade_transition_animation)
            lyt_user_profile.background.alpha = 200
        } else {
            lyt_navdraw.animation = loadAnimation(this, R.anim.fade_transition_animation_go)
            lyt_navdraw.visibility = View.GONE
            lyt_user_profile.background.alpha = 255
        }
    }
}