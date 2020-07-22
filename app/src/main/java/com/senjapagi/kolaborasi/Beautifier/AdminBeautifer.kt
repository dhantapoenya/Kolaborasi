//package com.senjapagi.kolaborasi.Beautifier
//
//import android.content.Context
//import android.view.View
//import android.view.animation.Animation
//import android.view.animation.AnimationUtils
//import com.senjapagi.kolaborasi.R
//
//class AdminBeautifier(var mContext: Context, var view: View) {
//    var animation1: Animation
//    var animation2: Animation
//    var animation3: Animation
//    fun menuGone() {
//        view.findViewById<View>(R.id.containerAnim1).visibility = View.GONE
//        view.findViewById<View>(R.id.containerAnim2).visibility = View.GONE
//        view.findViewById<View>(R.id.containerAnim3).visibility = View.GONE
//        view.findViewById<View>(R.id.containerAnim4).visibility = View.GONE
//        view.findViewById<View>(R.id.containerAnim5).visibility = View.GONE
//        view.findViewById<View>(R.id.containerAnim6).visibility = View.GONE
//        view.findViewById<View>(R.id.containerAnim7).visibility = View.GONE
//        view.findViewById<View>(R.id.containerHelpdesk).visibility = View.GONE
//        view.findViewById<View>(R.id.containerAnim9).visibility = View.GONE
//    }
//
//    fun menuAppear() {
//        view.findViewById<View>(R.id.containerAnim1).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.containerAnim1).animation = animation3
//        view.findViewById<View>(R.id.containerAnim2).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.containerAnim2).animation = animation1
//        view.findViewById<View>(R.id.containerAnim3).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.containerAnim3).animation = animation1
//        view.findViewById<View>(R.id.containerAnim4).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.containerAnim4).animation = animation1
//        view.findViewById<View>(R.id.containerAnim5).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.containerAnim5).animation = animation2
//        view.findViewById<View>(R.id.containerAnim6).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.containerAnim6).animation = animation2
//        view.findViewById<View>(R.id.containerAnim7).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.containerAnim7).animation = animation2
//        view.findViewById<View>(R.id.containerHelpdesk).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.containerHelpdesk).animation = animation2
//        view.findViewById<View>(R.id.containerAnim9).visibility = View.VISIBLE
//        view.findViewById<View>(R.id.containerAnim9).animation = animation2
//    }
//
//    init {
//        animation1 = AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation3)
//        animation2 = AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation_flip)
//        animation3 = AnimationUtils.loadAnimation(mContext, R.anim.item_animation_falldown)
//    }
//}