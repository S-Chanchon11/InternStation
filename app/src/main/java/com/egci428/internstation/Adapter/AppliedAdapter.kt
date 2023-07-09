package com.egci428.internstation

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.egci428.internstation.Data.AppliedData
import com.egci428.internstation.Data.CompanyData



class AppliedAdapter (private val appliedObj: List<AppliedData>): RecyclerView.Adapter<AppliedViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.appliedrow,parent,false)
        return AppliedViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return appliedObj.size
    }
    override fun onBindViewHolder(holder: AppliedViewHolder, position: Int) {

        holder.txtName.text = appliedObj[position].appliedName
        holder.txtJob.text = appliedObj[position].appliedJob
        holder.txtDuration.text = appliedObj[position].appliedDuration
        holder.txtQualification.text = appliedObj[position].appliedQualification
        Log.d("AdapterHolder", "success")

    }
}

class AppliedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var txtName = itemView.findViewById<TextView>(R.id.companyTxt)
    var txtJob = itemView.findViewById<TextView>(R.id.jobOf)
    var txtDuration = itemView.findViewById<TextView>(R.id.durationTxt)
    var txtQualification = itemView.findViewById<TextView>(R.id.qualiTxt)

}