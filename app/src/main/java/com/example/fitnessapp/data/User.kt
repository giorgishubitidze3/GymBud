package com.example.fitnessapp.data

data class User(
    var uId: String = "",
    val name: String = "",
    val surname: String = "",
    val username: String = "",
    val mail: String = ""
) {
    constructor() : this("", "", "", "", "")
}
