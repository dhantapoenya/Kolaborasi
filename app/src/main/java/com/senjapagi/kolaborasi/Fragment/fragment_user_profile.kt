package com.senjapagi.kolaborasi.Fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.senjapagi.kolaborasi.Beautifier.DialogBuilder
import com.senjapagi.kolaborasi.Beautifier.NavDrawSetter
import com.senjapagi.kolaborasi.Logout
import com.senjapagi.kolaborasi.R
import com.senjapagi.kolaborasi.Services.Constant
import com.senjapagi.kolaborasi.Services.Preference
import com.senjapagi.kolaborasi.Services.URL
import com.senjapagi.kolaborasi.user_change_profile
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_dashboard_user.*
import kotlinx.android.synthetic.main.activity_dashboard_user.btnToggleNavdraw
import kotlinx.android.synthetic.main.custom_navdraw.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.rg_gender
import kotlinx.android.synthetic.main.layout_loading_transparent.*
import kotlinx.android.synthetic.main.layout_register.*
import org.json.JSONObject
import java.io.File

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
    var imageFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lyt_user_profile?.background?.alpha = 255

        ndBtnHome.setOnClickListener { changeLayout(fragment_user_landing()) }

        tvProfileName.text = Preference(context!!).getPrefString(Constant.NAMA)
        tvProfileTelp.text = "loading..."
        tvProfileUsername.setText(Preference(context!!).getPrefString(Constant.USERNAME))




//        ndBtnProfile.setOnClickListener { changeLayout(fragment_user_profile()) }
        ndBtnLogOut.setOnClickListener { Logout(context!!).logoutDialog() }
        ndBtnProfile.setOnClickListener { NavDrawToggle("close") }
        btnToggleNavdraw.setOnClickListener { NavDrawToggle("open") }
        btnCloseNavDraw.setOnClickListener { NavDrawToggle("close") }
        lyt_navdraw_shadow.setOnClickListener { NavDrawToggle("close") }
        retrieveUser()

        btnChangeProfile.setOnClickListener {
            startActivity(Intent(activity,user_change_profile::class.java))
        }

        btnUpdateProfile.setOnClickListener {

            if (etProfileName.text.isNullOrEmpty()) {
                etProfileName.error = "Required"
            }
            if (etProfileEmail.text.isNullOrEmpty()) {
                etProfileName.error = "Required"
            }
            if (etProfileLine.text.isNullOrEmpty()) {
                etProfileLine.error = "Required"
            }
            if (etProfileKAngkatan.text.isNullOrEmpty()) {
                etProfileKAngkatan.error = "Required"
            }
            if (etProfileBirthDate.text.isNullOrEmpty()) {
                etProfileBirthDate.error = "Required"
            }
            if (etProfileEmail.text.isNullOrEmpty()) {
                etProfileEmail.error = "Required"
            } else {
                updateProfile()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        retrieveUser()
    }

    fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG)?.show()
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

    fun retrieveUser() {
        animation_lootie_loading.visibility = View.VISIBLE
        AndroidNetworking.post(URL.GET_DETAIL_USER)
            .addBodyParameter("id", Preference(context!!).getPrefString(Constant.ID))
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    animation_lootie_loading.visibility = View.GONE
                    if (response?.getBoolean("success")!!) {
                        val raz = response.getJSONObject("data")
                        val nama = raz.getString("nama")
                        val email = raz.getString("email")
                        val kelas = raz.getString("kelas")
                        val angkatan = raz.getString("angkatan")
                        val line_id = raz.getString("line_id")
                        val telp_no = raz.getString("telp_no")
                        val gender = raz.getString("gender")
                        val birthDate = raz.getString("birthdate")
                        val prof_pic = raz.getString("profile")
                        val username = raz.getString("username")

                        tvProfileName?.text = nama
                        tvProfileUsername?.text = username
                        etProfileName?.setText(nama)
                        etProfileEmail?.setText(email)
                        etProfileLine?.setText(line_id)
                        etProfileTelp?.setText(telp_no)
                        etProfileBirthDate?.setText(birthDate)
                        etProfileKelas?.setText(kelas)
                        etProfileKAngkatan?.setText(angkatan)
                        tvProfileTelp?.text = telp_no
                        etProfileUsername?.setText(username)
                        when (gender) {
                            "L" -> rg_gender?.check(R.id.rb_man)
                            "P" -> rg_gender?.check(R.id.rb_girl)
                            "1" -> rg_gender?.check(R.id.rb_man)
                            "2" -> rg_gender?.check(R.id.rb_girl)
                            else -> rg_gender?.check(R.id.rb_man)
                        }
                        Preference(context!!).save(Constant.USER_PROFILE_URL, prof_pic)
                    } else {
                        makeToast("Gagal Terhubung Dengan Server")
                    }
                    NavDrawSetter(context!!, activity?.window?.decorView!!, "profile").SetNavInfo()
                    navDrawProfile?.setImageDrawable(ivProfilePict.drawable)
                }

                override fun onError(anError: ANError?) {
                    animation_lootie_loading?.visibility = View.GONE
                    makeToast(anError?.errorDetail.toString())
                }

            })
    }

    fun updateProfile() {
        var gender = if (rg_gender.checkedRadioButtonId == R.id.rb_man) {
            "1"
        } else {
            "2"
        }
        loading_update_profile.visibility = View.VISIBLE
        btnUpdateProfile.visibility = View.GONE
        AndroidNetworking.post(URL.UPDATE_PESERTA)
            .addBodyParameter("id", Preference(context!!).getPrefString(Constant.ID))
            .addBodyParameter("nama", etProfileName.text.toString())
            .addBodyParameter("email", etProfileEmail.text.toString())
            .addBodyParameter("line", etProfileLine.text.toString())
            .addBodyParameter("telp", etProfileTelp.text.toString())
            .addBodyParameter("birthdate", etProfileBirthDate.text.toString())
            .addBodyParameter("username", etProfileUsername.text.toString())
            .addBodyParameter("kelas", etProfileKelas.text.toString())
            .addBodyParameter("angkatan", etProfileKAngkatan.text.toString())
            .addBodyParameter("gender", gender)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    //TODO("Not yet implemented")
                    if (response?.getBoolean("success")!!) {
                        makeToast("Berhasil Update Profile")
                        retrieveUser()
                        loading_update_profile.visibility = View.GONE
                        btnUpdateProfile.visibility = View.VISIBLE
                    } else {
                        makeToast("Gagal Mengupdate Profile, Coba Lagi Nanti")
                    }
                }

                override fun onError(anError: ANError?) {
                    makeToast("Gagal Mengupdate Profile, Coba Lagi Nanti")
                    loading_update_profile.visibility = View.GONE
                    btnUpdateProfile.visibility = View.VISIBLE
                    //TODO("Not yet implemented")
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