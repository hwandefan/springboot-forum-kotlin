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
        return ResponseEntity.ok(topicService.deleteTopic(UUID.fromString(id)))
    }

    @PutMapping("/edit/{id}")
    fun editTopic(
        @PathVariable id:String,
        @RequestBody request: TopicRequest
    ): ResponseEntity<TopicInfoResponse> {
        return ResponseEntity.ok(topicService.editTopic(UUID.fromString(id),request))
    }

    @GetMapping("/topics?page={page}")
    fun getTopics(
        @PathVariable page:Int
    ): ResponseEntity<List<TopicResponse>> {
        return ResponseEntity.ok(topicService.getAllTopics(page))
    }

    //TODO In service AND TEST ALL ENDPOINTS!!!! ALSO ADD COMMENTS FUNCTIONALITY!!!
    @GetMapping("/topics?name={name}&{page}")
    fun getFilteredTopicsByName(
        @PathVariable name:String,
        @PathVariable page:Int
    ):ResponseEntity<List<TopicResponse>> {
        return ResponseEntity.ok(topicService.getFilteredTopics(name, page))
    }

    @GetMapping("/{id}")
    fun getTopicById(
        @PathVariable id:String
    ): ResponseEntity<TopicResponse>
    {
        return ResponseEntity.ok(topicService.getTopicById(UUID.fromString(id)))
    }
}