package com.senjapagi.kolaborasi.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.senjapagi.kolaborasi.Adapter.AdapterDivisi
import com.senjapagi.kolaborasi.Model.ModelDivisi
import com.senjapagi.kolaborasi.R
import com.senjapagi.kolaborasi.Services.Constant
import com.senjapagi.kolaborasi.Services.Preference
import com.senjapagi.kolaborasi.Services.URL
import kotlinx.android.synthetic.main.fragment_organization_manage_divisi.*
import kotlinx.android.synthetic.main.layout_add_divisi.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_organization_manage_divisi.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_organization_manage_divisi : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var data = ArrayList<ModelDivisi>()
    lateinit var adapterDivisi: AdapterDivisi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lyt_manage_divisi.setOnRefreshListener {
            retrieveDivisi()
            lyt_manage_divisi.isRefreshing = false
        }

        recyclerViewDivisi?.setHasFixedSize(true)
        recyclerViewDivisi?.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        retrieveDivisi()
        addDivisi?.setOnClickListener {
            lyt_add_divisi?.visibility = View.VISIBLE
            lyt_add_divisi?.animation = loadAnimation(context, R.anim.item_animation_appear_bottom)
        }

        btnCloseDivisi?.setOnClickListener {
            lyt_add_divisi?.visibility = View.GONE
            lyt_add_divisi?.animation = loadAnimation(context, R.anim.item_animation_gone_bottom)
        }

        btnDivisiAdd.setOnClickListener {
            uploadDivisi()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun retrieveDivisi() {
        data.clear()
        animation_lootie_loading.visibility = View.VISIBLE
        AndroidNetworking.post(URL.GET_DIVISI_USER)
            .addBodyParameter(
                "entitas_id",
                Preference(context!!).getPrefString(Constant.ID_ENTITAS)
            )
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    animation_lootie_loading.visibility = View.GONE
                    val a = response?.getJSONArray("data")?.length()
                    if (a!! < 1) {
                        makeToast("Anda Belum Memiliki Divisi")
                    } else {
                        makeToast("Jumlah Entitas : $a ${response.toString()}")
                        for (i in 0 until a!!) {
                            val resp = response.getJSONArray("data")
                            val id_divisi = resp.getJSONObject(i).getString("id")
                            val entitas_id = resp.getJSONObject(i).getString("entitas_id")
                            val nama = resp.getJSONObject(i).getString("nama")
                            val username = resp.getJSONObject(i).getString("username")
                            val desc = resp.getJSONObject(i).getString("desc")

                            data.add(
                                ModelDivisi(
                                    id = id_divisi,
                                    entitas_id = entitas_id,
                                    nama = nama,
                                    username = username,
                                    desc = desc
                                )
                            )
                        }
                        adapterDivisi = AdapterDivisi(data, context!!)
                        recyclerViewDivisi.adapter = adapterDivisi
                    }
                }

                override fun onError(anError: ANError?) {
                    animation_lootie_loading.visibility = View.GONE
                    makeToast("Gagal Terhubung Dengan Server,Silakan Coba Lagi Nanti (404)")
                }

            })
    }


    private fun uploadDivisi() {
        if (etDivisiNama.text.toString().isBlank()) {
            etDivisiNama.error = "Required"
        }
        if (etDivisiUsername.text.toString().isBlank()) {
            etDivisiUsername.error = "Required"
        }
        if (etDivisiDesc.text.toString().isBlank()) {
            etDivisiDesc.error = "Required"
        }
        if (etPasswordDivisi.text.toString().isBlank()) {
            etPasswordDivisi.error = "Required"
        }
        if (etPasswordDivisi.text.toString().length < 5) {
            etPasswordDivisi.error = "Minimum 5 Character"
        }
        animation_lootie_loading.visibility = View.VISIBLE
        AndroidNetworking.post(URL.ADD_DIVISI)
            .addBodyParameter(
                "entitas_id",
                Preference(context!!).getPrefString(Constant.ID_ENTITAS)
            )
            .addBodyParameter("username", etDivisiUsername.text.toString())
            .addBodyParameter("name", etDivisiNama.text.toString())
            .addBodyParameter("password", etPasswordDivisi.text.toString())
            .addBodyParameter("desc", etDivisiDesc.text.toString())
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    makeToast(response.toString())
                    animation_lootie_loading.visibility = View.GONE
                    if (response?.getBoolean("success")!!) {
                        makeToast("Berhasil Menambah Entitas")
                        lyt_add_divisi.visibility = View.GONE

                    }
                }

                override fun onError(anError: ANError?) {
                    animation_lootie_loading.visibility = View.GONE
                    makeToast(anError?.errorBody.toString())
                }

            })
        retrieveDivisi()
    }

    fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT)?.show()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_organization_manage_divisi, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_organization_manage_divisi.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_organization_manage_divisi().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}