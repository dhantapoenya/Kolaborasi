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
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard_user.*
import kotlinx.android.synthetic.main.activity_dashboard_user.btnToggleNavdraw
import kotlinx.android.synthetic.main.custom_navdraw.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_user_profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_user_profile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ndBtnHome.setOnClickListener { changeLayout(fragment_user_landing()) }
//        ndBtnProfile.setOnClickListener { changeLayout(fragment_user_profile()) }
        ndBtnLogOut.setOnClickListener { Logout(context!!).logoutDialog() }
        btnToggleNavdraw.setOnClickListener { NavDrawToggle("open") }
        btnCloseNavDraw.setOnClickListener { NavDrawToggle("close") }
        lyt_navdraw_shadow.setOnClickListener { NavDrawToggle("close") }


        retrieveUser()
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
            lyt_user_profile?.background?.alpha = 200
        } else {
            lyt_navdraw.animation =
                AnimationUtils.loadAnimation(context!!, R.anim.fade_transition_animation_go)
            lyt_navdraw.visibility = View.GONE
            lyt_user_profile?.background?.alpha = 255
        }
    }

    fun retrieveUser(){
        makeToast("retreive USer")
        AndroidNetworking.post(URL.GET_DETAIL_USER)
            .addBodyParameter("id", Preference(context!!).getPrefString(Constant.ID))
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getBoolean("success")!!){
                        makeToast(response.toString())
                        val raz = response.getJSONObject("data")
                        val nama = raz.getString("nama")
                        val email = raz.getString("email")
                        val kelas = raz.getString("kelas")
                        val angkatan = raz.getString("angkatan")
                        val line_id = raz.getString("line_id")
                        val telp_no = raz.getString("telp_no")
                        val gender = raz.getString("gender")
                        val birthDate = raz.getString("birthdate")
                        val prof_pic = raz.getString("birthdate")

                        Picasso.get()
                            .load(URL.PROFILE_PIC_URL+prof_pic)
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
                    }else{
                        makeToast(response.toString())
                    }
                }

                override fun onError(anError: ANError?) {
                    makeToast(anError?.errorDetail.toString())
                }

            })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_user_profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_user_profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}