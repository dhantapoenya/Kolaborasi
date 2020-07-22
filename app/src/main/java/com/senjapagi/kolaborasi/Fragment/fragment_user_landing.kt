package com.senjapagi.kolaborasi.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.senjapagi.kolaborasi.Logout
import com.senjapagi.kolaborasi.R
import com.senjapagi.kolaborasi.Services.Constant
import com.senjapagi.kolaborasi.Services.Preference
import com.senjapagi.kolaborasi.Services.URL
import com.senjapagi.kolaborasi.user_landing
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard_user.btnToggleNavdraw
import kotlinx.android.synthetic.main.activity_dashboard_user.lyt_landing_user
import kotlinx.android.synthetic.main.custom_navdraw.*
import kotlinx.android.synthetic.main.fragment_user_landing.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [user_landing.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_user_landing : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed(object : Runnable {
            override fun run() {
                val calendar = Calendar.getInstance()
                realClock?.text =
                    SimpleDateFormat("HH:mm:ss", Locale.US).format(calendar.time).toString()
                Handler().postDelayed(this,1000)
            }
        }, 0)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val url = URL.PROFILE_PIC_URL + Preference(context!!).getPrefString(Constant.ID)

        Picasso.get()
            .load(url)
            .placeholder(R.drawable.add_profile)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .error(R.drawable.add_profile)
            ?.into(navDrawProfile, object : com.squareup.picasso.Callback {
                override fun onSuccess() {

                }

                override fun onError(e: java.lang.Exception?) {
                    //do smth when there is picture loading error
                }
            })

        ndTvNama?.text = Preference(context!!).getPrefString(Constant.NAMA)
        ndTvEmail?.text = Preference(context!!).getPrefString(Constant.EMAIL)
        ndTvUsername?.text = Preference(context!!).getPrefString(Constant.USERNAME)


        ndBtnHome.setOnClickListener { changeLayout(fragment_user_landing()) }
        ndBtnProfile.setOnClickListener { changeLayout(fragment_user_profile()) }
        ndBtnLogOut.setOnClickListener { Logout(context!!).logoutDialog() }
        btnToggleNavdraw.setOnClickListener { NavDrawToggle("open") }
        btnCloseNavDraw.setOnClickListener { NavDrawToggle("close") }
        lyt_navdraw_shadow.setOnClickListener { NavDrawToggle("close") }
    }

    fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun changeLayout(dest: Fragment) {
        NavDrawToggle("close")
        Handler().postDelayed({
            val fragmentManager: FragmentManager? = fragmentManager
            fragmentManager?.beginTransaction()?.replace(R.id.landingFrameLayout, dest)
                ?.commit()
        }, 500)
    }

    private fun moveActivity(dest: Activity) {
        NavDrawToggle("close")
        activity?.finish()
        startActivity(Intent(activity, dest::class.java))
    }


    private fun NavDrawToggle(indicator: String) {
        if (indicator.equals("open")) {
            lyt_navdraw.visibility = View.VISIBLE
            lyt_navdraw.animation =
                AnimationUtils.loadAnimation(context!!, R.anim.fade_transition_animation)
            lyt_landing_user.background.alpha = 200
        } else {
            lyt_navdraw.animation =
                AnimationUtils.loadAnimation(context!!, R.anim.fade_transition_animation_go)
            lyt_navdraw.visibility = View.GONE
            lyt_landing_user.background.alpha = 255
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_landing, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment user_landing.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_user_landing().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}