package com.hwandefan.forum.api.user

data class UserEditRequest (
    val firstName:String?,
    val lastName: String?,
    val profilePhoto: String?,
    val userStatus: String?
)