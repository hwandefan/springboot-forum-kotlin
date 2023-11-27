package com.hwandefan.forum.api.topic

import com.hwandefan.forum.model.User
import com.hwandefan.forum.service.topic.TopicService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/topic")
class TopicController(
    private val topicService: TopicService
) {
    @PostMapping("/create")
    fun createTopic(
        @RequestBody request: TopicRequest
    ): ResponseEntity<TopicInfoResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        return if (auth != null && auth.principal is UserDetails) {
            ResponseEntity.ok(topicService.createTopic(request.theme!!,
                request.text!!,
                (auth.principal as User)
            ))
        } else {
            ResponseEntity.ok(TopicInfoResponse(request.theme!!, "Topic has NOT created"))
        }
    }

    @DeleteMapping("/delete/{id}")
    fun deleteTopic(
        @PathVariable id:String
    ): ResponseEntity<TopicInfoResponse> {
        return try {
            val auth = SecurityContextHolder.getContext().authentication
            if (auth != null && auth.principal is UserDetails) {
                ResponseEntity.ok(topicService.deleteTopic(UUID.fromString(id),
                    (auth.principal as User).getUserId().toString()))
            } else {
                ResponseEntity.ok(TopicInfoResponse(id.toString(),
                    "ERROR: Topic with this id has not deleted"))
            }
        } catch (e:Exception) {
            ResponseEntity.ok(TopicInfoResponse(id.toString(),
                "ERROR: Topic with this id has not deleted"))
        }
    }

    @PutMapping("/edit/{id}")
    fun editTopic(
        @PathVariable id:String,
        @RequestBody request: TopicRequest
    ): ResponseEntity<TopicInfoResponse> {
        try {
            val auth = SecurityContextHolder.getContext().authentication
            return if (auth != null && auth.principal is User) {
                ResponseEntity.ok(topicService
                    .editTopic(
                        UUID.fromString(id),
                        request,
                        (auth.principal as User).getUserId().toString())
                )
            } else
                ResponseEntity.ok(TopicInfoResponse(id.toString(),
                    "ERROR: Topic has not been updated"))
        } catch (e:Exception){
            return ResponseEntity.ok(TopicInfoResponse(id.toString(),
                "ERROR: Topic has not been updated"))
        }
    }

    @GetMapping("/topics")
    fun getTopics(
        @RequestParam page:Int
    ): ResponseEntity<List<TopicResponse>> {
        return ResponseEntity.ok(topicService.getAllTopics(page))
    }

    @GetMapping("/filter")
    fun getFilteredTopicsByName(
        @RequestParam name:String
    ):ResponseEntity<List<TopicResponse>> {
        return ResponseEntity.ok(topicService.getFilteredTopics(name))
    }

    @GetMapping("/{id}")
    fun getTopicById(
        @PathVariable id:String
    ): ResponseEntity<TopicResponse>
    {
        return ResponseEntity.ok(topicService.getTopicById(UUID.fromString(id)))
    }

    @GetMapping("/my_topics")
    fun getLoggedUserTopics(): ResponseEntity<List<TopicResponse>>{
        return try {
            val auth = SecurityContextHolder.getContext().authentication
            if (auth != null && auth.principal is User) {
                ResponseEntity.ok(topicService.getLoggedUserTopics(
                    (auth.principal as User)
                ))
            } else {
                ResponseEntity.ok(arrayListOf())
            }
        } catch (e:Exception) {
            ResponseEntity.ok(arrayListOf())
        }
    }
}