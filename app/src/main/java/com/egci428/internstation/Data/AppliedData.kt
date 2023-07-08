package com.egci428.internstation.Data

class AppliedData(
    val appliedId:String,
    val appliedName:String,
    val appliedJob:String,
    val appliedDuration: String,
    val appliedQualification: String) {
    constructor(): this("","", "","", "")
}