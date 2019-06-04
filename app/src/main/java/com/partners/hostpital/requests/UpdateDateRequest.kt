package com.partners.hostpital.requests

import com.google.gson.annotations.SerializedName

class UpdateDateRequest(){
    @SerializedName("recipe")
    var recipe =""
    @SerializedName("observations")
    var observations =""
    @SerializedName("status")
    var status = 0
    @SerializedName("is_doctor")
    var isDoctor = 0



}