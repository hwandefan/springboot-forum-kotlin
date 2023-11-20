package com.hwandefan.forum.api.auth

data class RegisterRequest (
    val firstname:String,
    val lastname:String,
    val email:String,
    val password:String
)