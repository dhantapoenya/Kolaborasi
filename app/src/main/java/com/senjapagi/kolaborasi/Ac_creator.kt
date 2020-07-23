package com.senjapagi.kolaborasi



import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_creator.*
import kotlinx.android.synthetic.main.activity_dashboard_user.*
import kotlinx.android.synthetic.main.activity_dashboard_user.btnToggleNavdraw
import kotlinx.android.synthetic.main.custom_navdraw.*
import kotlinx.android.synthetic.main.user_profile.*
import java.text.SimpleDateFormat
import java.util.*


class Ac_creator:AppCompatActivity() {

    var cal = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator)

        btnkegiatan.setBackgroundColor(Color.parseColor("#092532"))

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR,year)
                cal.set(Calendar.MONTH,month)
                cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                val myFormat = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val format1= sdf.format(cal.time)
                tanggal1.setText(format1)
            }
        tanggal1.setOnClickListener {
            DatePickerDialog(this@Ac_creator, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        val dateSetListener2 = object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view2: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR,year)
                cal.set(Calendar.MONTH,month)
                cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                val myFormat2 = "dd/MM/yyyy"
                val sdf2 = SimpleDateFormat(myFormat2, Locale.US)
                val format2= sdf2.format(cal.time)
                tanggal2.setText(format2)
            }
        }
        tanggal2.setOnClickListener{
            DatePickerDialog(this@Ac_creator, dateSetListener2,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        //button navdraw


    }

}