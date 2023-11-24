package com.hwandefan.forum.api.admin

import com.hwandefan.forum.service.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
class AdminController(
    private val userService: UserService
) {
    @GetMapping("/all_users")
    fun getAllUsers():ResponseEntity<List<UserResponse>> {
        return ResponseEntity.ok(userService.getAllUsers())
    }

    @DeleteMapping("/delete_user/{id}")
    fun deleteUser(@PathVariable id:String): ResponseEntity<UserInfoResponse> {
        return ResponseEntity.ok(userService.deleteUserById(id))
    }

    @PostMapping("/grant_admin_to_user/{id}")
    fun grantAdmin(@PathVariable id:String): ResponseEntity<UserInfoResponse> {
        return ResponseEntity.ok(userService.grantAdminRights(id))
    }
}