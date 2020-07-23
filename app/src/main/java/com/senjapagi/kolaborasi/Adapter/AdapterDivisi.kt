package com.senjapagi.kolaborasi.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.senjapagi.kolaborasi.Model.ModelDivisi
import com.senjapagi.kolaborasi.R
import kotlinx.android.synthetic.main.list_divisi.view.*


class AdapterDivisi(val dataDivisi: ArrayList<ModelDivisi>, val context: Context) :
    RecyclerView.Adapter<HolderDivisi>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDivisi {
        return HolderDivisi(LayoutInflater.from(context).inflate(
                    R.layout.list_divisi, parent, false))
    }

    override fun getItemCount(): Int {
        return dataDivisi.size
    }

    override fun onBindViewHolder(holder: HolderDivisi, position: Int) {
        holder.tvID.text=dataDivisi[position].id
        holder.tvName.text=dataDivisi[position].nama
        holder.tvUsername.text=dataDivisi[position].username
        holder.tvDesc.text=dataDivisi[position].desc
        holder.tvEntitasID.text=dataDivisi[position].entitas_id
    }

}


class HolderDivisi(view: View) : RecyclerView.ViewHolder(view) {
    val tvID = view.etDivisiListID
    val tvName = view.etDivisiListName
    val tvDesc = view.etDivisiListDesc
    val tvUsername = view.etDivisiListUsername
    val tvEntitasID = view.etDivisiListEntitasID
}