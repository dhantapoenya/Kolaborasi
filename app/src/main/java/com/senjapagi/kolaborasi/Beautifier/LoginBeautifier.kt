package com.senjapagi.kolaborasi.Beautifier
//
//import android.content.Context
//import android.media.MediaPlayer
//import android.view.View
//import android.view.animation.AnimationUtils
//import android.view.animation.AnimationUtils.loadAnimation
//import com.senjapagi.kolaborasi.R
//import com.senjapagi.shsd.R
//import kotlinx.android.synthetic.main.activity_login.*
//import kotlinx.android.synthetic.main.layout_choose_login.*
//
//class LoginBeautifier(var mContext: Context, var view: View) {
//
//    fun animFirst() {
//        MediaPlayer.create(mContext, R.raw.intro).start()
//        view.findViewById<View>(R.id.tv_cred).visibility = View.GONE
//        view.findViewById<View>(R.id.btn_forgot_password).visibility = View.GONE
//        view.findViewById<View>(R.id.logohmsi).visibility = View.GONE
//        view.findViewById<View>(R.id.tv_fakultas).visibility = View.GONE
//        view.findViewById<View>(R.id.tv_ead).visibility = View.GONE
//        view.findViewById<View>(R.id.btn_register).visibility = View.GONE
//        view.findViewById<View>(R.id.fab_login).visibility = View.INVISIBLE
//        view.findViewById<View>(R.id.rl_input).visibility = View.INVISIBLE
//        view.findViewById<View>(R.id.tv_cred).animation =loadAnimation(mContext, R.anim.fade_transition_animation_slowly)
//        view.findViewById<View>(R.id.rl_input).animation = loadAnimation(mContext, R.anim.fade_transition_animation_slowly)
//        view.findViewById<View>(R.id.logohmsi).animation = loadAnimation(mContext, R.anim.item_animation_falldown_slowly)
//        view.findViewById<View>(R.id.tv_fakultas).animation = loadAnimation(mContext, R.anim.fade_transition_animation_slowly)
//        view.findViewById<View>(R.id.btn_register).animation = loadAnimation(mContext, R.anim.fade_transition_animation)
//        view.findViewById<View>(R.id.fab_login).animation =loadAnimation(mContext, R.anim.fade_transition_animation)
//        view.findViewById<View>(R.id.tv_ead).animation = loadAnimation(mContext, R.anim.fade_transition_animation_slowly)
//        view.findViewById<View>(R.id.btn_forgot_password).animation = loadAnimation(mContext, R.anim.fade_transition_animation)
//        view.findViewById<View>(R.id.tv_cred).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.logohmsi).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.tv_fakultas).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.tv_ead).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.btn_register).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.rl_input).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.fab_login).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.btn_forgot_password).visibility = View.VISIBLE
////        val b = android.os.Handler()
////        b.postDelayed({
////
////            view.findViewById<View>(R.id.et_username).visibility = View.VISIBLE
////            view.findViewById<View>(R.id.et_password).visibility = View.VISIBLE
////        },700)
//    }
//
//    fun animSecond() {
//        view.findViewById<View>(R.id.btn_forgot_password).visibility = View.GONE
//        view.findViewById<View>(R.id.btn_forgot_password).animation = loadAnimation(mContext, R.anim.fade_transition_animation_go)
//        view.findViewById<View>(R.id.rl_input).animation =loadAnimation(mContext, R.anim.fade_transition_animation_go)
//        view.findViewById<View>(R.id.rl_input).visibility = View.GONE
//        view.findViewById<View>(R.id.fab_login).animation = loadAnimation(mContext, R.anim.fade_transition_animation_go)
//        view.findViewById<View>(R.id.fab_login).visibility = View.GONE
//        view.findViewById<View>(R.id.btn_register).visibility = View.GONE
//        view.findViewById<View>(R.id.btn_register).animation = loadAnimation(mContext, R.anim.fade_transition_animation_go)
//        view.findViewById<View>(R.id.logohmsi).animation =loadAnimation(mContext, R.anim.fade_transition_animation_go)
//        view.findViewById<View>(R.id.logohmsi).visibility = View.GONE
//        view.findViewById<View>(R.id.tv_cred).animation = loadAnimation(mContext, R.anim.fade_transition_animation_go)
//        view.findViewById<View>(R.id.tv_cred).visibility = View.GONE
//        view.findViewById<View>(R.id.tv_fakultas).animation = loadAnimation(mContext, R.anim.fade_transition_animation_go)
//        view.findViewById<View>(R.id.tv_fakultas).visibility = View.GONE
//        view.findViewById<View>(R.id.tv_ead).animation = loadAnimation(mContext, R.anim.fade_transition_animation_go)
//        view.findViewById<View>(R.id.tv_ead).visibility = View.GONE
//    }
//
//}