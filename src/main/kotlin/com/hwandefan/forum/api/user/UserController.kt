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
        @RequestBody request: UserEditRequest
    ): ResponseEntity<UserEditResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null && auth.principal is UserDetails) {
            val username = (auth.principal as UserDetails).username
            val message = userService.updateUser(
                username, arrayListOf(
                    request.userStatus,
                    request.firstName, request.lastName, request.profilePhoto
                )
            )
            if (message != "OK")
                return ResponseEntity.ok(UserEditResponse("failure|user not updated"))
            return ResponseEntity.ok(UserEditResponse("success"))
        }
        return ResponseEntity.ok(UserEditResponse("failure|no token"))
    }

    @DeleteMapping("/delete")
    fun deleteUser(): ResponseEntity<UserDeleteResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null && auth.principal is UserDetails) {
            val username = (auth.principal as UserDetails).username
            val confirmation = userService.deleteUser(username)
            return ResponseEntity.ok(UserDeleteResponse(username,confirmation))
        }
        return ResponseEntity.ok(UserDeleteResponse("NOT FOUND",false))
    }
}