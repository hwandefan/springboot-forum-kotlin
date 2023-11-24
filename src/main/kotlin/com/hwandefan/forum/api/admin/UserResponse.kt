package com.hwandefan.forum.api.admin

import com.hwandefan.forum.model.Role


data class UserResponse(
    val email:String,
    val id: String,
    val role:Role,
    val lastName: String,
    val firstName: String,
    val imageSrc: String?
)