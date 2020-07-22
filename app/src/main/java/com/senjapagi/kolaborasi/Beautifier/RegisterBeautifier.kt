package com.senjapagi.kolaborasi.Beautifier
//
//import android.content.Context
//import android.content.Intent
//import android.os.Handler
//import android.view.View
//import android.view.animation.AnimationUtils
//import androidx.appcompat.app.AppCompatActivity
//import com.senjapagi.shsd.ActivityGeneral.LoginActivity
//import com.senjapagi.shsd.ActivityGeneral.MainActivity
//import com.senjapagi.shsd.R
//
//
//class RegisterBeautifier ( val mContext: Context,val view: View): AppCompatActivity() {
//
//   fun AnimFirst(){
//        view.findViewById<View>(R.id.container_profile_pic).visibility = View.GONE
//        view.findViewById<View>(R.id.lyt_additional_login).animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation)
//        view.findViewById<View>(R.id.lyt_additional_login).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.container_profile_pic).animation = AnimationUtils.loadAnimation(mContext, R.anim.item_animation_falldown)
//        view.findViewById<View>(R.id.container_profile_pic).visibility = View.VISIBLE
//    }
//    fun animGone() {
//        view.findViewById<View>(R.id.lyt_additional_login).animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation_go)
//        view.findViewById<View>(R.id.lyt_additional_login).visibility = View.GONE
//        view.findViewById<View>(R.id.container_profile_pic).visibility = View.GONE
//        view.findViewById<View>(R.id.container_profile_pic).animation = AnimationUtils.loadAnimation(mContext, R.anim.item_animation_fallup)
//
//    }
//
//}