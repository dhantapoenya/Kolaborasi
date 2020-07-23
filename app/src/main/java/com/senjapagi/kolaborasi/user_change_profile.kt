package com.senjapagi.kolaborasi

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.senjapagi.kolaborasi.Beautifier.DialogBuilder
import com.senjapagi.kolaborasi.Services.Constant
import com.senjapagi.kolaborasi.Services.Preference
import com.senjapagi.kolaborasi.Services.URL
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_user_change_profile.*
import kotlinx.android.synthetic.main.custom_navdraw.view.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*
import kotlinx.android.synthetic.main.layout_register.*
import kotlinx.android.synthetic.main.layout_step_register_organization.*
import org.json.JSONObject
import java.io.File


class user_change_profile : AppCompatActivity() {
    var imageFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_change_profile)

        downloadPicasso()



        btnChangeProfile.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 22)
                checkAndRequestPermission()
            else openGallery()
        }

        btnSaveChangeProfile.setOnClickListener {
            if (imageFile==null){
                makeToast("Anda Belum Melakukan Perubahan")
            }else{
                updateProfilePic()
            }

        }


        btnBack.setOnClickListener {
            super.onBackPressed()
        }
    }


    private fun downloadPicasso(){
        animation_lootie_loading.visibility= View.VISIBLE
        Picasso.get()
            .load(URL.PROFILE_PIC_URL + Preference(this).getPrefString(Constant.USER_PROFILE_URL))
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .fit()
            .placeholder(R.drawable.add_profile)
            .error(R.drawable.add_profile)
            .into(ivProfilePicChange,object: com.squareup.picasso.Callback {
                override fun onSuccess() {
                    //set animations here
                    animation_lootie_loading.visibility= View.GONE
                }

                override fun onError(e: java.lang.Exception?) {
                    //do smth when there is picture loading error
                    animation_lootie_loading.visibility= View.GONE
                }
            })
    }

    private fun openGallery() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this@user_change_profile)
    }

    fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


    fun updateProfilePic() {
        animation_lootie_loading.visibility=View.VISIBLE
        AndroidNetworking.upload(URL.UPDATE_PESERTA_PROFILE)
            .addMultipartFile("imageupload", imageFile)
            .addMultipartParameter("id", Preference(this).getPrefString(Constant.ID))
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    animation_lootie_loading.visibility=View.GONE
                    if (response.getBoolean("success")) {
                        DialogBuilder(this@user_change_profile, window?.decorView!!).success(
                            "Success",
                            "Berhasil Mengganti Foto Profile",
                            "OK"
                        )
                        makeToast("Berhasil Mengganti Foto Profile")
                    } else {
                        makeToast("Gagal Mengganti Foto Profile")
                    }
                }

                override fun onError(anError: ANError) {
                    onBackPressed()
                    animation_lootie_loading.visibility=View.GONE
                    DialogBuilder(this@user_change_profile, window?.decorView!!).errorConnection(
                        "Gagal",
                        "Gagal Mengupdate Foto Profile",
                        "OK"
                    )
                    makeToast("Gagal Mengganti Foto Profil")
                }
            })

    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                ivProfilePicChange.setImageURI(resultUri)
                imageFile = File(resultUri.path)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                this!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                makeToast("Accept All Permission Request")
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
        } else openGallery()
    }
}