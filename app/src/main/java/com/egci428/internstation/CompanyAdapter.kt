package com.egci428.internstation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.egci428.internstation.Data.CompanyData


class CompanyAdapter (private val movieObject: List<CompanyData>): RecyclerView.Adapter<CompanyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return CompanyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movieObject.size
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.txtName.text = movieObject[position].name
        holder.txtYear.text = movieObject[position].lat.toString()
        holder.txtStar.text = movieObject[position].long.toString()

    }
}

class CompanyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var txtName = itemView.findViewById<TextView>(R.id.txtName)
    var txtYear = itemView.findViewById<TextView>(R.id.txtYear)
    var txtStar = itemView.findViewById<TextView>(R.id.txtStar)

}