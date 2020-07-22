package com.senjapagi.kolaborasi.Services

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.senjapagi.kolaborasi.Beautifier.DialogBuilder
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.layout_register.*
import org.json.JSONObject
import java.io.File

class ProfileImageUpdater(val activity: Activity, val view: View, val context: Context) :
    AppCompatActivity() {

    var imageFile: File? = null

    fun openGallery() {
        if (Build.VERSION.SDK_INT >= 22) {
            checkAndRequestPermission()
        } else {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(activity)
        }


    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                iv_profile_pic.setImageURI(resultUri)
                imageFile = File(resultUri.path)
                updateProfile()
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    fun updateProfile() {
        AndroidNetworking.upload(URL.UPDATE_PESERTA_PROFILE)
            .addMultipartFile("imageupload", imageFile)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    if (response.getBoolean("success")) {
                        DialogBuilder(context, view).success(
                            "Success",
                            "Berhasil Mengganti Foto Profile",
                            "OK"
                        )
                        makeToast("Pendaftaran Berhasil")
                    } else {
                        makeToast("Pendaftaran Gagal")
                    }
                }

                override fun onError(anError: ANError) {
                    DialogBuilder(context, view).errorConnection(
                        "Gagal",
                        "Gagal Mengupdate Foto Profile",
                        "OK"
                    )
                    makeToast("Gagal Mengganti Foro Profil")
                }
            })

    }

    fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Toast.makeText(context, "Accept All Permission Request", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
        } else openGallery()
    }

}