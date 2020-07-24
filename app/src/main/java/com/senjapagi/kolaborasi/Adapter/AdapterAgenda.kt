package com.senjapagi.kolaborasi.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.senjapagi.kolaborasi.Model.ModelAgenda
import com.senjapagi.kolaborasi.Model.ModelDivisi
import com.senjapagi.kolaborasi.R
import kotlinx.android.synthetic.main.list_agenda.view.*
import kotlinx.android.synthetic.main.list_divisi.view.*


class AdapterAgenda(val dataAgenda: ArrayList<ModelAgenda>, val context: Context) :
    RecyclerView.Adapter<HolderAgenda>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderAgenda {
        return HolderAgenda(
            LayoutInflater.from(context).inflate(
                R.layout.list_agenda, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataAgenda.size
    }

    override fun onBindViewHolder(holder: HolderAgenda, position: Int) {
        holder.tvID.text = dataAgenda[position].id
        holder.tvName.text = dataAgenda[position].nama
        val open = "Start : " + dataAgenda[position].open_gate
        val close = "End : " + dataAgenda[position].close_gate
        holder.tvStart.text = open
        holder.tvEnd.text = close
        val status = dataAgenda[position].status

        if (status != "opened") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.statLeft.setBackgroundColor(
                    context.resources.getColor(
                        R.color.bootstrapRed,
                        context.theme
                    )
                )
                holder.statRight.setBackgroundColor(
                    context.resources.getColor(
                        R.color.bootstrapRed,
                        context.theme
                    )
                )
            }
            holder.statLeft.setBackgroundColor(context.resources.getColor(R.color.bootstrapRed))
            holder.statRight.setBackgroundColor(context.resources.getColor(R.color.bootstrapRed)
            )
        }
        holder.tvEO.text = dataAgenda[position].nama_divisi
    }

}


class HolderAgenda(view: View) : RecyclerView.ViewHolder(view) {
    val tvID: TextView = view.etAgendaListID
    val tvName = view.etAgendaListName
    val tvStart: TextView = view.etAgendaListStart
    val tvEnd = view.etAgendaListEnd
    val statLeft = view.leftAgendaListCircle
    val statRight = view.RightAgendaRightCircle
    val tvEO = view.etAgendaListEO
}