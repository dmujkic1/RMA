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

class KuharskiAdapter(
    private val context: Context,
    private var biljke: List<Biljka>,
    private val onItemClicked: (biljka:Biljka) -> Unit) : RecyclerView.Adapter<KuharskiAdapter.BiljkaViewHolder>(){

    private var trefle=TrefleDAO()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiljkaViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.kuharski, parent, false)
        return BiljkaViewHolder(view)
    }

    override fun onBindViewHolder(holder: BiljkaViewHolder, position: Int) {
        val biljka=biljke[position]
        val db=BiljkaDatabase.getDatabase(context)
        holder.nazivItem.text = biljka.naziv
        holder.okus.text = biljka.profilOkusa?.opis
        try
        {
            holder.jelo1Item.text=biljka.jela[0]
        }catch(e:Exception)
        {
            holder.jelo1Item.text=""
        }
        try
        {
            holder.jelo2Item.text=biljka.jela[1]
        }catch(e:Exception)
        {
            holder.jelo2Item.text=""
        }
        try
        {
            holder.jelo3Item.text=biljka.jela[2]
        }catch(e:Exception)
        {
            holder.jelo3Item.text=""
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
        val okus: TextView = itemView.findViewById(R.id.profilOkusaItem)
        val jelo1Item: TextView = itemView.findViewById(R.id.jelo1Item)
        val jelo2Item: TextView = itemView.findViewById(R.id.jelo2Item)
        val jelo3Item: TextView = itemView.findViewById(R.id.jelo3Item)
        val slikaItem: ImageView = itemView.findViewById(R.id.slikaItem)
    }
}