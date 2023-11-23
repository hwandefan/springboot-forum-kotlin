package com.hwandefan.forum.api.user

data class UserEditResponse (
    val successOrFailureMessage:String,
    val accountChanges: HashMap<String,String>? = null
)