package com.hwandefan.forum.api.topic

import com.fasterxml.jackson.databind.ObjectMapper
import com.hwandefan.forum.api.topic.comment.CommentResponse
import com.hwandefan.forum.model.Status
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
internal class TopicControllerTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var topicController: TopicController

    private val objectMapper = ObjectMapper()
    private val topicId: UUID = UUID.randomUUID()
    private val userId = UUID.randomUUID()

    val mockTopics: List<TopicResponse> = arrayListOf(
        TopicResponse(
            UUID.randomUUID().toString(),
            "Test Topic Theme1",
            "Test Topic Text1",
            UUID.randomUUID().toString(),
            Status.ORIGINAL
        ),
        TopicResponse(
            topicId.toString(),
            "Test Topic Theme2",
            "Test Topic Text2",
            userId.toString(),
            Status.EDITED,
            arrayListOf(
                CommentResponse(
                    UUID.randomUUID().toString(),
                    "Comment 1",
                    UUID.randomUUID().toString(),
                    Status.ORIGINAL
                ),
                CommentResponse(
                    UUID.randomUUID().toString(),
                    "Comment 2",
                    UUID.randomUUID().toString(),
                    Status.EDITED
                )
            )
        )
    )

    @Test
    @WithMockUser(username = "test", roles = ["USER"])
    fun `should return all topics`() {
        Mockito.`when`(topicController.getTopics(0))
            .thenReturn(ResponseEntity.ok(mockTopics))
        mockMvc.get("/api/v1/topic/topics?page=0")
            .andDo { print() }
            .andExpect {
                status {
                    isOk()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(mockTopics))
                }
            }
    }

    @Test
    @WithMockUser(username = "test", roles = ["USER"])
    fun `should return one topic`(){
        Mockito.`when`(topicController.getTopicById(topicId.toString()))
            .thenReturn(ResponseEntity.ok(mockTopics[1]))
        mockMvc.get("/api/v1/topic/${topicId}")
            .andDo { print() }
            .andExpect {
                status {
                    isOk()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(mockTopics[1]))
                    jsonPath("$.theme") { value("Test Topic Theme2") }
                    jsonPath("$.comments[1].text") { value("Comment 2") }
                }
            }
    }

    @Test
    @WithMockUser(username = "test", roles = ["USER"])
    fun `should return all topics with defined id`() {
        Mockito.`when`(topicController.getLoggedUserTopics())
            .thenReturn(ResponseEntity.ok(
                mockTopics.filter{
                    it.userId == this.userId.toString()
                }
            ))
        mockMvc.get("/api/v1/topic/my_topics")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.length()") { value(1) }
                    jsonPath("$.[0].theme") { value("Test Topic Theme2") }
                    jsonPath("$.[0].comments[1].text") { value("Comment 2") }
                    json(objectMapper.writeValueAsString(arrayListOf(mockTopics[1])))
                }
            }
    }

    @Test
    fun `should return forbidden`() {
        Mockito.`when`(topicController.getTopics(0))
            .thenReturn(ResponseEntity.ok(mockTopics))
        mockMvc.get("/api/v1/topic/topics?page=0")
            .andDo { print() }
            .andExpect {
                status {
                    isForbidden()
                }
            }
    }
}