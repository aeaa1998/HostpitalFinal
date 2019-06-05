package com.partners.hostpital.requests

import com.google.gson.annotations.SerializedName

class UpdateUserInfoRequest(){
    @SerializedName("new_name")
    var newName =""
    @SerializedName("new_last_name")
    var newLastName =""
    @SerializedName("new_username")
    var newUsername = ""
    @SerializedName("new_password")
    var newPassword = ""
    @SerializedName("is_doctor")
    var isDoctor = 0




}