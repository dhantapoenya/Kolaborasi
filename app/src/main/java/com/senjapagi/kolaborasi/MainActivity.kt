package com.senjapagi.kolaborasi

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.senjapagi.kolaborasi.Beautifier.DialogBuilder
import com.senjapagi.kolaborasi.Services.Constant
import com.senjapagi.kolaborasi.Services.Preference
import com.senjapagi.kolaborasi.Services.URL
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*
import kotlinx.android.synthetic.main.layout_login.*
import kotlinx.android.synthetic.main.layout_register.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var rgGender: RadioGroup? = null
    var radioButton: RadioButton? = null
    var imageFile: File? = null
    var gender = "L"
    val calendarInstance = Calendar.getInstance()
    lateinit var dialogBuilder: DialogBuilder
    val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dialogBuilder = DialogBuilder(this, window.decorView)
        checkPref()
        fab_reg.setOnClickListener { verifyRegister() }
        btn_cancel_register.setOnClickListener { lyt_register.visibility = View.GONE }
        container_profile_pic.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 22)
                checkAndRequestPermission()
            else openGallery()
        }



        btnLogin.setOnClickListener {

            if (etUsername.text.isNullOrEmpty()) {
                etUsername.error = "Isi Kolom Ini Terlebih Dahulu"
            }
            if (etPassword.text.isNullOrEmpty()) {
                etPassword.error = "Isi Kolom Ini Terlebih Dahulu"
            } else {
                btnLogin.visibility = View.GONE
                progress_loading.visibility = View.VISIBLE
                login("user")
            }
        }

        btnCloseReg.setOnClickListener {
            lyt_register.apply {
                animation = loadAnimation(context, R.anim.item_animation_gone_bottom)
                visibility = View.GONE
            }
        }

        btnCloseLogin.setOnClickListener {
            lyt_login.apply {
                animation = loadAnimation(context, R.anim.item_animation_gone_bottom)
                visibility = View.GONE
            }
        }

        btnStartLogin.setOnClickListener {
            lyt_login.apply {
                visibility = View.VISIBLE
                animation = loadAnimation(
                    context, R.anim.item_animation_appear_bottom
                )
            }
        }

        btnStartRegister.setOnClickListener {
            lyt_register.apply {
                visibility = View.VISIBLE
                animation = loadAnimation(
                    context, R.anim.item_animation_appear_bottom
                )
            }
        }
        datePick.setOnClickListener {
            DatePickerDialog(
                this@MainActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                calendarInstance.get(Calendar.YEAR),
                calendarInstance.get(Calendar.MONTH),
                calendarInstance.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
        }
    }

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendarInstance.set(Calendar.YEAR, year)
            calendarInstance.set(Calendar.MONTH, monthOfYear)
            calendarInstance.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "MM-dd-yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            birthDay.setText(sdf.format(calendarInstance.getTime()).toString())
        }


    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Toast.makeText(this, "Accept All Permission Request", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
        } else openGallery()
    }

    private fun checkPref() {
        showMessage(Preference(this).getPrefString(Constant.LEVEL).toString())
        if (Preference(this).getPrefString(Constant.LEVEL).isNullOrEmpty()) {
        } else {
            if (Preference(this).getPrefString(Constant.LEVEL) == "user") {
                startActivity(Intent(this@MainActivity, LandingContainer::class.java))
            }
//            etUsername.setText(Preference(this).getPrefString(Constant.USERNAME))
        }
    }

    private fun login(param: String) {


        val param = param
        var url: String? = null
        when (param) {
            "admin" -> url = URL.LOGIN_ADMIN
            "all" -> url = URL.LOGIN_ALL
            else -> url = URL.LOGIN_ALL
        }

        var login: Intent? = null
        dialogBuilder.progressType(
            title = "Loading",
            content = "Loading",
            contentText = "Checking Your Credential"
        )
        progress_loading.visibility = View.VISIBLE
        btnLogin.visibility = View.INVISIBLE
        AndroidNetworking.post(url)
            .addBodyParameter("username", etUsername.text.toString())
            .addBodyParameter("password", etPassword.text.toString())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    progress_loading.visibility = View.INVISIBLE
                    btnLogin.visibility = View.VISIBLE
                    dialogBuilder.destroyAll()
//                    dialogBuilder.success("Login Berhasil","","OK")
                    try {
                        if (response.getBoolean("success")) {
                            val resp = response.getJSONObject("data")
                            val id = resp.getString("id")
                            val username = resp.getString("username")
                            val nama = resp.getString("nama")
                            val email = resp.getString("email")
                            val status = resp.getString("status")
                            val level = resp.getString("level")
                            showMessage(level)
//                            showMessage(id +"nama $nama" + "email :" + email + status)

                            Preference(this@MainActivity).save(Constant.ID, id)
                            Preference(this@MainActivity).save(Constant.NAMA, nama)
                            Preference(this@MainActivity).save(Constant.USERNAME, username)
                            Preference(this@MainActivity).save(Constant.EMAIL, email)
                            Preference(this@MainActivity).save(Constant.LEVEL, level)
//                            Preference(this@MainActivity).save(Constant.S, status)

                            startActivity(Intent(this@MainActivity, LandingContainer::class.java))
                            finish()
                        } else {
                            showMessage(response.getString("message"))
                        }
//                        if (message.contains("success")) {
//                            Handler().postDelayed({
//
//                                startActivity(login)
//                                finish()
//                            }, 1300)
//                        } else {
//                            showMessage("Username atau Password Salah")
//                            dialogBuilder.neutralWarning(
//                                "Error",
//                                "Username atau Password Tidak Ditemukan",
//                                "Coba Lagi"
//                            )
//                        }
                    } catch (e: Exception) {
                        dialogBuilder.errorConnection(
                            "Gagal Terhubung Dengan Server",
                            "Silakan Coba Lagi Nanti",
                            "OK"
                        )
                        e.printStackTrace()
                        showMessage("Gagal Terhubung Dengan Server $e")
                    }
                }

                override fun onError(error: ANError) {
                    progress_loading.visibility = View.INVISIBLE
                    btnLogin.visibility = View.VISIBLE
                    dialogBuilder.destroyAll()
                    dialogBuilder.neutralWarning(
                        "Gagal Terhubung Dengan Server",
                        "Periksa Koneksi Internet Anda atau Coba Lagi Nanti",
                        "OK"
                    )
                }
            })
    }

    private fun openGallery() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this@MainActivity)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                iv_profile_pic.setImageURI(resultUri)
                imageFile = File(resultUri.path)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun verifyRegister() {
        var nid = et_nid.text.toString()
        var nama = et_nama.text.toString()
        var username = et_username.text.toString()
        var email = et_email.text.toString()
        var kelas = et_kelas.text.toString()
        var password = et_password.text.toString()
        var verifPassword = et_verify_password.text.toString()

        if (nid.isEmpty() || nama.isEmpty() || kelas.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || verifPassword.isEmpty()) {
            Toast.makeText(this@MainActivity, "Fill the Empty Form First !", Toast.LENGTH_SHORT)
                .show()
        } else {
            if (password == verifPassword) {
                if (imageFile == null) {
                    showMessage("Anda Belum Memilih Foto Profil")
                } else {
                    uploadData()
                }
            } else showMessage("Password Tidak Sesuai")
        }
    }

    private fun uploadData() {
        val id = rg_gender.checkedRadioButtonId
        when (id) {
            R.id.rb_man -> gender = "L"
            R.id.rb_girl -> gender = "P"
        }
        animation_lootie_loading.visibility = View.VISIBLE
        fab_reg.visibility = View.GONE
        loading_register.visibility = View.VISIBLE
        AndroidNetworking.upload(URL.REGISTASI_PESERTA)
            .addMultipartFile("imageupload", imageFile)
            .addMultipartParameter("nama", et_nama.text.toString())
            .addMultipartParameter("username", et_username.text.toString()) // nim as username at database
            .addMultipartParameter("nid", et_nid.text.toString())
            .addMultipartParameter("password", et_password.text.toString())
            .addMultipartParameter("kelas", et_kelas.text.toString())
            .addMultipartParameter("angkatan", et_angkatan.text.toString())
            .addMultipartParameter("gender", gender.toString())
            .addMultipartParameter("birthdate", birthDay.text.toString())
            .addMultipartParameter("line", et_line.text.toString())
            .addMultipartParameter("telp", et_telp.text.toString())
            .addMultipartParameter("email", et_email.text.toString())
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    if (response.getString("kode") == "1") {
                        showMessage("Pendaftaran Berhasil")
                    } else {
                        showMessage("Pendaftaran Gagal")
                    }
                    fab_reg.visibility = View.VISIBLE
                    animation_lootie_loading.visibility = View.GONE
                    Toast.makeText(this@MainActivity, response.toString(), Toast.LENGTH_SHORT)
                        .show()
                    loading_register.visibility = View.GONE
                }

                override fun onError(anError: ANError) {
                    dialogBuilder.errorConnection(
                        "Pendaftaran Gagal",
                        "Gagal Terhubung Dengan Server,Coba lagi nanti", "OK"
                    )
                    animation_lootie_loading.visibility = View.GONE
                    Toast.makeText(
                        this@MainActivity,
                        anError.errorDetail.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    fab_reg.visibility = View.VISIBLE
                    loading_register.visibility = View.GONE
                }
            })
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showURL(url: String) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage("Image URL : $url")
        builder1.setCancelable(true)
        val alert11 = builder1.create()
        alert11.show()
    }
}
//coba pull request