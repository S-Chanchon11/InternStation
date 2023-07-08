package com.egci428.internstation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.egci428.internstation.Data.CompanyData



class CompanyAdapter (private val companyObject: List<CompanyData>): RecyclerView.Adapter<CompanyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return CompanyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return companyObject.size
    }
    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.txtJob.text = companyObject[position].name
        holder.txtBenefit.text = companyObject[position].job
        holder.txtQualif.text = companyObject[position].benefit
        //holder.txtDuration.text = companyObject[position].companyID[position].jobOf[position].duration
    }
}
class CompanyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var txtJob = itemView.findViewById<TextView>(R.id.txtName)
    var txtBenefit = itemView.findViewById<TextView>(R.id.txtYear)
    var txtQualif = itemView.findViewById<TextView>(R.id.txtStar)
    //var txtDuration = itemView.findViewById<TextView>(R.id.txtDuration)
}