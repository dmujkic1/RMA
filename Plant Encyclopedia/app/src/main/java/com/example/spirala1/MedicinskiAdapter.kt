package com.example.spirala1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MedicinskiAdapter(
    private val context: Context,
    private var biljke: List<Biljka>,
    private val onItemClicked: (biljka:Biljka) -> Unit) : RecyclerView.Adapter<MedicinskiAdapter.BiljkaViewHolder>(){

    private var trefle=TrefleDAO()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiljkaViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.medicinski, parent, false)
        return BiljkaViewHolder(view)
    }

    override fun onBindViewHolder(holder: BiljkaViewHolder, position: Int) {
        val biljka=biljke[position]
        val db=BiljkaDatabase.getDatabase(context)
        holder.nazivItem.text = biljka.naziv
        try
        {
            holder.upozorenjeItem.text = biljka.medicinskoUpozorenje
        }catch(e:Exception)
        {
            holder.upozorenjeItem.text=""
        }
        try
        {
            holder.korist1Item.text=biljka.medicinskeKoristi[0].opis
        }catch(e:Exception)
        {
            holder.korist1Item.text=""
        }
        try
        {
            holder.korist2Item.text=biljka.medicinskeKoristi[1].opis
        }catch(e:Exception)
        {
            holder.korist2Item.text=""
        }
        try
        {
            holder.korist3Item.text=biljka.medicinskeKoristi[2].opis
        }catch(e:Exception)
        {
            holder.korist3Item.text=""
        }

        trefle.setContext(context)
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            val bitmap = trefle.getImage(biljka)
            if (biljka.onlineChecked) { holder.slikaItem.setImageBitmap(bitmap) }
            else {
                val bitmapa = biljka.id?.let { db.biljkaDao().getBiljkaBitmap(it) }
                if (bitmapa!=null) holder.slikaItem.setImageBitmap(bitmapa.bitmap)
                else if (bitmapa==null && biljka.id!=null) {db.biljkaDao().addImage(biljka.id, bitmap); holder.slikaItem.setImageBitmap(bitmap)}
                else if (bitmapa==null) holder.slikaItem.setImageBitmap(bitmap)
            }
            holder.itemView.setOnClickListener { onItemClicked(biljke[position]) }
        }

        /*
        val context: Context = holder.slikaItem.context
        val nazivMatch: String = biljka.naziv
            .substringBefore(" (")
            .lowercase()
            .replace(" ","_")
            .replace("č","c")
            .replace("š","s")
            .replace("ž","z")
        var id: Int = context.resources.getIdentifier(nazivMatch, "drawable", context.packageName)
        if (id==0) id=context.resources.getIdentifier("defaultplant", "drawable", context.packageName) //nismo nasli sliku za biljku
        holder.slikaItem.setImageResource(id)
        holder.itemView.setOnClickListener{ onItemClicked(biljke[position]) }
        */
    }

    override fun getItemCount(): Int {
        return biljke.size
    }

    fun updateBiljke(biljke: List<Biljka>) {
        this.biljke = biljke
        notifyDataSetChanged()
    }

    inner class BiljkaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val upozorenjeItem: TextView = itemView.findViewById(R.id.upozorenjeItem)
        val korist1Item: TextView = itemView.findViewById(R.id.korist1Item)
        val korist2Item: TextView = itemView.findViewById(R.id.korist2Item)
        val korist3Item: TextView = itemView.findViewById(R.id.korist3Item)
        val slikaItem: ImageView = itemView.findViewById(R.id.slikaItem)
    }
}