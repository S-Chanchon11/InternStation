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
        holder.txtName.text = companyObject[position].company
        holder.txtJob.text = companyObject[position].job
        holder.txtDuration.text = companyObject[position].duration
        holder.txtQualification.text = companyObject[position].qualification
    }
}
class CompanyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var txtName = itemView.findViewById<TextView>(R.id.companyTxt)
    var txtJob = itemView.findViewById<TextView>(R.id.jobOf)
    var txtDuration = itemView.findViewById<TextView>(R.id.durationTxt)
    var txtQualification = itemView.findViewById<TextView>(R.id.qualiTxt)
}