package com.egci428.internstation.Data

class UserData(val id:String,val username:String,val password:String,
               val fullname:String, val dob:String, val university: String) {
    constructor(): this("","", "","", "","")
}