package com.senjapagi.kolaborasi.Beautifier

import android.content.Context
import android.media.Image
import android.view.View
import android.widget.ImageView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.senjapagi.kolaborasi.R
import com.senjapagi.kolaborasi.Services.Constant
import com.senjapagi.kolaborasi.Services.Preference
import com.senjapagi.kolaborasi.Services.URL
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_navdraw.view.*
import kotlinx.android.synthetic.main.fragment_user_landing.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class NavDrawSetter(val context: Context, val view: View,val ref:String) {

    fun SetNavInfo() {
        view.ndTvEmail.text = Preference(context).getPrefString(Constant.EMAIL)
        view.ndTvUsername.text = Preference(context).getPrefString(Constant.USERNAME)
        view.ndTvNama.text = Preference(context).getPrefString(Constant.NAMA)
        view.landingGreeter?.text = Preference(context).getPrefString(Constant.NAMA)

        AndroidNetworking.post(URL.GET_DETAIL_USER)
            .addBodyParameter("id", Preference(context!!).getPrefString(Constant.ID))
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getBoolean("success")!!) {
                        Preference(context!!).save(
                            Constant.USER_PROFILE_URL,
                            response.getJSONObject("data").getString("profile"))

                        view?.landingGreeter?.text =  response.getJSONObject("data").getString("nama")
                        view?.ndTvUsername?.text =  response.getJSONObject("data").getString("username")
                        view?.ndTvNama?.text =  response.getJSONObject("data").getString("nama")
                        view?.ndTvEmail?.text =  response.getJSONObject("data").getString("email")

                        Preference(context).save(Constant.NAMA,response.getJSONObject("data").getString("nama"))
                        Preference(context).save(Constant.USERNAME,response.getJSONObject("data").getString("username"))
                        Preference(context).save(Constant.EMAIL,response.getJSONObject("data").getString("email"))
                    }
                }

                override fun onError(anError: ANError?) {

                }
            })


        var target : ImageView = view.navDrawProfile
        when(ref){
            "profile"-> target = view.ivProfilePict
            "landing"-> target = view.navDrawProfile
        }
            downloadPicasso(target)
            downloadPicasso(view.navDrawProfile)

    }

    private fun downloadPicasso(target:ImageView){
        Picasso.get()
            .load(URL.PROFILE_PIC_URL + Preference(context).getPrefString(Constant.USER_PROFILE_URL))
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .fit()
            .placeholder(R.drawable.add_profile)
            .error(R.drawable.add_profile)
            .into(target ?:view.navDrawProfile)
    }
}