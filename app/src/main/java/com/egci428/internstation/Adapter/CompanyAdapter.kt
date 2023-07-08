package com.egci428.internstation

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.egci428.internstation.Data.CompanyData



class CompanyAdapter (private val companyObject: List<CompanyData>): RecyclerView.Adapter<CompanyViewHolder>(){

    private lateinit var cListener : onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        cListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return CompanyViewHolder(itemView,cListener)
    }

    override fun getItemCount(): Int {
        return companyObject.size
    }
    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {

        holder.txtName.text = companyObject[position].company
        holder.txtJob.text = companyObject[position].job
        holder.txtDuration.text = companyObject[position].duration
        holder.txtQualification.text = companyObject[position].qualification
        Log.d("AdapterHolder", "success")



    }
}



class CompanyViewHolder(itemView: View, listener: CompanyAdapter.onItemClickListener): RecyclerView.ViewHolder(itemView){
    var txtName = itemView.findViewById<TextView>(R.id.companyTxt)
    var txtJob = itemView.findViewById<TextView>(R.id.jobOf)
    var txtDuration = itemView.findViewById<TextView>(R.id.durationTxt)
    var txtQualification = itemView.findViewById<TextView>(R.id.qualiTxt)
    init {
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}