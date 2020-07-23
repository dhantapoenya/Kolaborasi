package com.senjapagi.kolaborasi

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.senjapagi.kolaborasi.Beautifier.DialogBuilder
import com.senjapagi.kolaborasi.Services.URL
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_user_create_organization.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*
import kotlinx.android.synthetic.main.layout_step_register_organization.*
import org.json.JSONObject
import java.io.File

class user_create_organization : AppCompatActivity() {
    var imageFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_create_organization)

        btnBack.setOnClickListener{
            startActivity(Intent(this@user_create_organization,LandingContainer::class.java))
            finish()
        }

        ivOrgProfilePic.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 22)
                checkAndRequestPermission()
            else openGallery()
        }


        btnRegOrgFinish.setOnClickListener {
            verifyRegister()
        }

        btnBackReg.setOnClickListener {
            lyt_nextStepOrg.visibility = View.GONE
            lyt_nextStepOrg.animation = loadAnimation(this, R.anim.item_animation_gone_bottom)


            btnRegOrgNext.setOnClickListener {
                if (etRegOrgName.text.toString().isEmpty()) {
                    etRegOrgName.error = "Required"
                }
                if (etRegOrgUsername.text.toString().isBlank()) {
                    etRegOrgUsername.error = "Required"
                }
                if (etRegOrgDeskripsi.text.toString().isBlank()) {
                    etRegOrgDeskripsi.error = "Required"
                }
                if (etRegOrgPassAdmin.text.toString().isBlank()) {
                    etRegOrgPassAdmin.error = "Required"
                }
                if (etRegOrgPassAdmin.length() < 5) {
                    etRegOrgPassAdmin.error = "Minimum 5 Digit"
                } else {
                    lyt_nextStepOrg.visibility = View.VISIBLE
                    lyt_nextStepOrg.animation =
                        loadAnimation(this, R.anim.item_animation_appear_bottom)
                }
            }
        }

    }

    fun verifyRegister() {
        if (etRegOrgEmail.text.toString().isEmpty()) {
            etRegOrgEmail.error = "Required"
        }
        if (etRegOrgLine.text.toString().isEmpty()) {
            etRegOrgLine.error = "Required"
        }
        if (etRegOrgTelpNo.text.toString().isEmpty()) {
            etRegOrgTelpNo.error = "Required"
        }
        if (imageFile == null) {
            makeToast("Anda Belum Menambahkan Logo Organisasi")
        } else {
            register()
        }
    }


    private fun register() {
        animation_lootie_loading.visibility = View.VISIBLE
        AndroidNetworking.upload(URL.REGISTASI_ENTITAS)
            .addMultipartFile("imageupload", imageFile)
            .addMultipartParameter("username", etRegOrgUsername.text.toString())
            .addMultipartParameter("password", etRegOrgPassAdmin.text.toString())
            .addMultipartParameter("nama", etRegOrgName.text.toString())
            .addMultipartParameter("deskripsi", etRegOrgDeskripsi.text.toString())
            .addMultipartParameter("line", etRegOrgLine.text.toString())
            .addMultipartParameter("telp", etRegOrgTelpNo.text.toString())
            .addMultipartParameter("email", etRegOrgEmail.text.toString())
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    animation_lootie_loading.visibility = View.GONE
                    if (response.getBoolean("success")) {
                        makeToast("Pendaftaran Berhasil")
                        DialogBuilder(this@user_create_organization, window.decorView)
                            .success("Pendaftaran Berhasil","Pendaftaran Organisasi Berhasil ," +
                                    "Silakan Login di Menu Login Entitas","OK")
                        startActivity(Intent(this@user_create_organization,LandingContainer::class.java))
                        finish()
                    } else {
                        DialogBuilder(this@user_create_organization, window.decorView)
                            .neutralWarning("Pendaftaran Gagal","Verifikasi Data Anda dan Coba lagi Nanti",
                                "OK")
                    }
                    animation_lootie_loading.visibility = View.GONE
                }

                override fun onError(anError: ANError) {
                    animation_lootie_loading.visibility = View.GONE
                    animation_lootie_loading.visibility = View.GONE
                    makeToast("Gagal Terhubung dengan Server, Mohon Coba Lagi Nanti")
                    DialogBuilder(this@user_create_organization, window.decorView)
                        .neutralWarning("Pendaftaran Gagal","Gagal Terhubung dengan Server," +
                                "Silakan Coba lagi Nanti",
                            "OK")
                }
            })
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                ivOrgProfilePic.setImageURI(resultUri)
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

    private fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun openGallery() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this@user_create_organization)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, LandingContainer::class.java))
        finish()
    }
}