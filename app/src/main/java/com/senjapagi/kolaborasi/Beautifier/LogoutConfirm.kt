package com.senjapagi.kolaborasi.Beautifier

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.senjapagi.kolaborasi.MainActivity

class LogoutConfirm(var mContext: Context) : AppCompatActivity() {
    fun logoutConfirm() {
        val builder1 = AlertDialog.Builder(mContext)
        builder1.setMessage("Anda Yakin Ingin Logout ???")
        builder1.setCancelable(true)
        builder1.setNegativeButton(
                "Tidak"
        ) { dialog, id -> dialog.cancel() }
        builder1.setPositiveButton(
                "Ya"
        ) { dialog, id ->
            val a = Intent(mContext, MainActivity::class.java)
            mContext.startActivity(a)
            finish()
        }
        val alert11 = builder1.create()
        alert11.show()
    }

}