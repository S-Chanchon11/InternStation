package com.egci428.internstation

class userData(val id:String,val username:String,val password:String,
               val fullname:String, val Dob:String, val university: String) {
    constructor(): this("","", "","", "","")
}