package com.hwandefan.forum.api.topic.comment

import com.hwandefan.forum.model.User
import com.hwandefan.forum.service.topic.comment.CommentService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/api/v1/comment")
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping("/add")
    fun addComment(
        @RequestBody request: CommentRequest
    ): ResponseEntity<CommentResponse?> {
        val auth = SecurityContextHolder.getContext().authentication
        return if (auth != null && auth.principal is User) {
            ResponseEntity.ok(commentService.addComment(request, auth.principal as User))
        } else {
            ResponseEntity.ok(null)
        }
    }

    @PutMapping("/edit/{id}")
    fun editComment(
        @RequestBody request: CommentRequest,
        @PathVariable id:String
    ): ResponseEntity<CommentResponse?> {
        val auth = SecurityContextHolder.getContext().authentication
        return if (auth != null && auth.principal is User) {
            ResponseEntity.ok(commentService.editComment(request,
            UUID.fromString(id),
            auth.principal as User))
        } else {
            ResponseEntity.ok(null)
        }
    }

    @DeleteMapping("/delete/{id}")
    fun deleteComment(
        @PathVariable id:String
    ): ResponseEntity<CommentResponse?> {
        val auth = SecurityContextHolder.getContext().authentication
        return if (auth != null && auth.principal is User) {
            ResponseEntity.ok(commentService.deleteComment(UUID.fromString(id),
                auth.principal as User))
        } else {
            ResponseEntity.ok(null)
        }
    }
}