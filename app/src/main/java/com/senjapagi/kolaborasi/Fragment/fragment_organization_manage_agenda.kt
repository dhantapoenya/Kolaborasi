package com.senjapagi.kolaborasi.Fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.jaredrummler.materialspinner.MaterialSpinner
import com.senjapagi.kolaborasi.Adapter.AdapterAgenda
import com.senjapagi.kolaborasi.Adapter.AdapterDivisi
import com.senjapagi.kolaborasi.Model.ModelAgenda
import com.senjapagi.kolaborasi.Model.ModelDivisi
import com.senjapagi.kolaborasi.R
import com.senjapagi.kolaborasi.Services.Constant
import com.senjapagi.kolaborasi.Services.Preference
import com.senjapagi.kolaborasi.Services.URL
import kotlinx.android.synthetic.main.fragment_organization_manage_agenda.*
import kotlinx.android.synthetic.main.fragment_organization_manage_divisi.*
import kotlinx.android.synthetic.main.layout_add_agenda.*
import kotlinx.android.synthetic.main.layout_add_divisi.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_organization_manage_agenda.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_organization_manage_agenda : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val calendar = Calendar.getInstance()
    val calendarTime = Calendar.getInstance()
    var choosenStartDate: String? = null
    var choosenEndDate: String? = null
    var startDateDB: String? = null
    var endDateDB: String? = null
    var divisiSelected: String = "def"
    var statusSelected: String = "def"


    lateinit var mutableDivisiID: MutableMap<String, Int>
    lateinit var mapDivisiID: Map<String, Int>
    lateinit var data: ArrayList<String>

    var dataAgenda = ArrayList<ModelAgenda>()
    lateinit var adapterAgenda: AdapterAgenda


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        var status = ArrayList<String>()
        status.add("Pilih Status")
        status.add("Opened")
        status.add("Closed")
        spinnerAgendaStatus?.setItems(status)

        recyclerViewAgenda?.setHasFixedSize(true)
        recyclerViewAgenda?.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)



        spinnerAgendaStatus.setOnItemSelectedListener { _, position, id, item ->
            statusSelected = item.toString()
        }
        spinnerAgendaDivisi.setOnItemSelectedListener { _, position, id, item ->
            makeToast(item.toString())
            divisiSelected = item.toString()
        }


        lyt_manage_agenda.isEnabled = true
        lyt_manage_agenda?.setOnRefreshListener {
            lyt_manage_agenda.isRefreshing = false
            retrieveDivisi()
            retrieveAgenda()

        }

        mutableDivisiID = mutableMapOf<String, Int>()
        data = ArrayList()
        retrieveDivisi()
        retrieveAgenda()


        btnAgendaAdd?.setOnClickListener {
            uploadAgenda()
        }
        addAgenda?.setOnClickListener {
            lyt_manage_agenda.isEnabled = false
            lyt_add_agenda.visibility = View.VISIBLE
            lyt_add_agenda.animation = loadAnimation(context!!, R.anim.item_animation_appear_bottom)
        }

        btnCloseAgenda?.setOnClickListener {
            lyt_manage_agenda.isEnabled = true
            lyt_add_agenda.visibility = View.GONE
            lyt_add_agenda.animation = loadAnimation(context!!, R.anim.item_animation_gone_bottom)
        }

        btnStartDate.setOnClickListener {
            pickStartDateTime()
        }

        btnEndDate.setOnClickListener {
            pickEndDateTime()
        }

        agendaStartTime.setOnClickListener {
            TimePickerDialog(
                context!!,
                StartTimeListener,
                calendarTime.get(Calendar.HOUR_OF_DAY),
                calendarTime.get(Calendar.MINUTE),
                true
            ).show()
            calendarTime.set(Calendar.SECOND, 0)
        }

        agendaEndTime.setOnClickListener {
            TimePickerDialog(
                context!!,
                EndTimeListener,
                calendarTime.get(Calendar.HOUR_OF_DAY),
                calendarTime.get(Calendar.MINUTE),
                true
            ).show()
            calendarTime.set(Calendar.SECOND, 0)
        }

        agendaStartDate.setOnClickListener {
            pickStartDateTime()
        }

        agendaEndDate.setOnClickListener {
            pickEndDateTime()
        }



        super.onViewCreated(view, savedInstanceState)
    }


    private fun uploadAgenda() {
        when {
            divisiSelected == "def" || divisiSelected == "Pilih Penyelenggara" -> {
                makeToast("Pilih Divisi Terlebih Dahulu")
            }
            statusSelected == "def" || statusSelected == "Pilih Status" -> {
                makeToast("Pilih Status Terlebih Dahulu")
            }
            etAgendaNama.text.toString().isBlank() -> {
                etAgendaNama.error = "Required"
                makeToast("Isi Semua Kolom Terlebih Dahulu")
            }
            etAgendaKuota.text.toString().isBlank() -> {
                etAgendaKuota.error = "Required"
                makeToast("Isi Semua Kolom Terlebih Dahulu")
            }
            etAgendaDesc.text.toString().isBlank() -> {
                etAgendaDesc.error = "Required"
                makeToast("Isi Semua Kolom Terlebih Dahulu")
            }
            agendaStartDate.text == "YYYY-MM-DD" -> {
                makeToast("Anda Belum Memilih Tanggal Mulai")
            }
            agendaEndDate.text == "YYYY-MM-DD" -> {
                makeToast("Anda Belum Memilih Tanggal Akhir")
            }
            else -> {

                val divisiID = mutableDivisiID[divisiSelected].toString()
                makeToast(divisiID)
                val dateStart = agendaStartDate.text.toString()
                val timeStart = agendaStartTime.text.toString()

                val dateEnd = agendaEndDate.text.toString()
                val timeEnd = agendaEndTime.text.toString()
                animation_lootie_loading.visibility = View.VISIBLE
                AndroidNetworking.post(URL.ADD_AGENDA)
                    .addBodyParameter("divisi_id", divisiID)
                    .addBodyParameter("nama", etAgendaNama.text.toString())
                    .addBodyParameter("kuota", etAgendaKuota.text.toString())
                    .addBodyParameter(
                        "entitas_id",
                        Preference(context!!).getPrefString(Constant.ID_ENTITAS)
                    )
                    .addBodyParameter("start", "$choosenStartDate $timeStart")
                    .addBodyParameter("end", "$choosenEndDate $timeEnd")
                    .addBodyParameter("desc", etAgendaDesc.text.toString())
                    .addBodyParameter("loc", etAgendaLoc.text.toString())
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            animation_lootie_loading.visibility = View.GONE
                            if (response?.getBoolean("success")!!) {
                                makeToast("Berhasil Menambah Jadwal")
                                lyt_add_agenda.visibility = View.GONE
                                lyt_manage_agenda.isEnabled = true
                            }
                        }

                        override fun onError(anError: ANError?) {
                            animation_lootie_loading.visibility = View.GONE
                            makeToast(anError?.errorBody.toString())
                            makeToast("Gagal Menambah Agenda, Koneksi dengan Server Bermasalah")
                        }

                    })
                retrieveDivisi()
                retrieveAgenda()
            }
        }
    }

    private fun retrieveAgenda() {
        dataAgenda.clear()
        animation_lootie_loading.visibility = View.VISIBLE
        AndroidNetworking.post(URL.GET_AGENDA_USER)
            .addBodyParameter(
                "entitas_id",
                Preference(context!!).getPrefString(Constant.ID_ENTITAS)
            )
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    animation_lootie_loading.visibility = View.GONE

                    if (response?.getBoolean("success")!!) {
                        val raz = response.getJSONArray("data")
                        for (i in 0 until raz.length()) {


                            val id = raz.getJSONObject(i).getString("id")
                            val nama = raz.getJSONObject(i).getString("nama")
                            val desc = raz.getJSONObject(i).getString("desc")
                            val id_divisi = raz.getJSONObject(i).getString("id_divisi")
                            val nama_divisi = raz.getJSONObject(i).getString("nama_divisi")
                            val id_entitas = raz.getJSONObject(i).getString("id_entitas")
                            val status = raz.getJSONObject(i).getString("status")
                            val open_gate = raz.getJSONObject(i).getString("open_gate")
                            val close_gate = raz.getJSONObject(i).getString("close_gate")
                            val tempat = raz.getJSONObject(i).getString("tempat")
                            val kuota = raz.getJSONObject(i).getString("kuota")

                            dataAgenda.add(
                                ModelAgenda(
                                    id = id,
                                    nama_divisi = nama_divisi,
                                    status = status,
                                    close_gate = close_gate,
                                    open_gate = open_gate,
                                    kuota = kuota,
                                    lokasi = tempat,
                                    nama = nama,
                                    id_divisi = id_divisi,
                                    id_entitas = id_entitas,
                                    deskripsi = desc
                                )
                            )
                        }
                        adapterAgenda = AdapterAgenda(dataAgenda, context!!)
                        recyclerViewAgenda.adapter = adapterAgenda
                    } else {
                        makeToast("Anda Belum Memiliki Agenda")
                    }
                }

                override fun onError(anError: ANError?) {
                    animation_lootie_loading.visibility = View.GONE
                    makeToast("Gagal Terhubung Dengan Server,Silakan Coba Lagi Nanti (404)")
                }

            })
    }

    private fun retrieveDivisi() {
        mutableDivisiID.clear()
        data.clear()
        data.add("Pilih Penyelenggara")
        animation_lootie_loading?.visibility = View.VISIBLE
        AndroidNetworking.post(URL.GET_DIVISI_USER)
            .addBodyParameter(
                "entitas_id",
                Preference(context!!).getPrefString(Constant.ID_ENTITAS)
            )
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    animation_lootie_loading?.visibility = View.GONE
                    val a = response?.getJSONArray("data")?.length()
                    if (a!! < 1) {
                        addAgenda?.setOnClickListener {
                            makeToast("Anda Belum Memiliki Divisi , Tambah Divisi Sebelum Menambah Agenda")
                        }
                    } else {
                        for (i in 0 until a) {
                            val resp = response.getJSONArray("data")
                            val id_divisi = resp.getJSONObject(i).getString("id")
                            val nama = resp.getJSONObject(i).getString("nama")

                            mutableDivisiID[nama] = id_divisi.toInt()

                            makeLongToast(mutableDivisiID.toString())


                            data.add(nama)
                            spinnerAgendaDivisi?.setItems(data)

//                            makeToast("${data.size.toString()} panjang mutable = ${mutableDivisiID.size.toString()}")
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    animation_lootie_loading.visibility = View.GONE
                    makeToast("Gagal Terhubung Dengan Server,Silakan Coba Lagi Nanti (404)")
                }

            })
    }


    fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT)?.show()
    }

    fun makeLongToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG)?.show()
    }

    private fun pickStartDateTime() {
        DatePickerDialog(
            context!!,
            startDateSetListener,
            // set DatePickerDialog to point to today's date when it loads up
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun pickEndDateTime() {
        DatePickerDialog(
            context!!,
            endDateSetListener,
            // set DatePickerDialog to point to today's date when it loads up
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private val startDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd-MM-yyyy" // format for application
            val databaseFormat = "yyyy-MM-dd" // format for database and API
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val sdfDatabase = SimpleDateFormat(databaseFormat, Locale.US)
            agendaStartDate.text = sdf.format(calendar.time).toString()
            choosenStartDate = sdfDatabase.format(calendar.time).toString()
        }
    private val endDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd-MM-yyyy" // format for application
            val databaseFormat = "yyyy-MM-dd" // format for database and API
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val sdfDatabase = SimpleDateFormat(databaseFormat, Locale.US)
            agendaEndDate.text = sdf.format(calendar.time).toString()
            choosenEndDate = sdfDatabase.format(calendar.time).toString()
        }

    val StartTimeListener =
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendarTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendarTime.set(Calendar.MINUTE, minute)
            val formatTime = "HH:mm:ss"
            val sdf = SimpleDateFormat(formatTime, Locale.ENGLISH)
            agendaStartTime.text = sdf.format(calendarTime.time).toString()
        }

    val EndTimeListener =
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendarTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendarTime.set(Calendar.MINUTE, minute)
            val formatTime = "HH:mm:ss"
            val sdf = SimpleDateFormat(formatTime, Locale.ENGLISH)
            agendaEndTime.text = sdf.format(calendarTime.time).toString()
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_organization_manage_agenda, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_organization_manage_agenda.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_organization_manage_agenda().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}