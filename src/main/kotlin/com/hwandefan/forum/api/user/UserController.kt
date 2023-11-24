package com.hwandefan.forum.api.user

import com.hwandefan.forum.service.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService
) {
    @PutMapping("/edit")
    fun editUser(
        @RequestHeader("Authorization") authToken: String,
        @RequestBody request: UserEditRequest
    ): ResponseEntity<UserEditResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null && auth.principal is UserDetails) {
            val username = (auth.principal as UserDetails).username
            val message = userService.updateUser(username, arrayListOf(request.userStatus,
                request.firstName, request.lastName, request.profilePhoto)
            )
            if(message==null)
                return ResponseEntity.ok(UserEditResponse("failure|user not updated"))
            return ResponseEntity.ok(UserEditResponse("success"))
        }
        return ResponseEntity.ok(UserEditResponse("failure|no token"))
    }

    @DeleteMapping("/delete")
    fun deleteUser(
        @RequestHeader("Authorization") authToken: String
    ): ResponseEntity<UserDeleteResponse> {
        return null!!
    }
}