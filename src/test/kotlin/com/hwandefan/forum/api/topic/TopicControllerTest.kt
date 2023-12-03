package com.hwandefan.forum.api.topic

import com.fasterxml.jackson.databind.ObjectMapper
import com.hwandefan.forum.api.topic.comment.CommentResponse
import com.hwandefan.forum.model.Role
import com.hwandefan.forum.model.Status
import com.hwandefan.forum.model.User
import com.hwandefan.forum.model.UserPreferences
import com.hwandefan.forum.service.topic.TopicService
import org.junit.jupiter.api.*
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.*
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
internal class TopicControllerTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var topicService: TopicService

    private val objectMapper = ObjectMapper()
    private val topicId: UUID = UUID.randomUUID()
    private val userId = UUID.randomUUID()

    private val mockUser = User(
        userId,
        "Test User Name",
        "Test User Surname",
        "test",
        "test",
        Role.USER,
        UserPreferences()
    )

    private val mockTopics: List<TopicResponse> = arrayListOf(
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
            mockUser.getUserId().toString(),
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
        val page = 0
        `when`(topicService.getAllTopics(eq(page)))
            .thenReturn(mockTopics)
        mockMvc.get("/api/v1/topic/topics") {
            param("page", page.toString())
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(mockTopics))
                }
            }
    }

    @Test
    @WithMockUser(username = "test", roles = ["USER"])
    fun `should return one topic`(){
        `when`(topicService.getTopicById(topicId))
            .thenReturn(mockTopics[1])
        mockMvc.get("/api/v1/topic/${topicId}")
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
    fun `should return all topics with defined user id`() {
        `when`(topicService.getLoggedUserTopics(mockUser))
            .thenReturn(arrayListOf(mockTopics[1]))
        mockMvc.get("/api/v1/topic/my_topics") {
            with(SecurityMockMvcRequestPostProcessors.user(mockUser))
        }
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
        val page = 0
        `when`(topicService.getAllTopics(page))
            .thenReturn(mockTopics)
        mockMvc.get("/api/v1/topic/topics") {
            param("page", page.toString())
        }
            .andExpect {
                status {
                    isForbidden()
                }
            }
    }

    @Test
    fun `should add the new topic`() {
        val topic = TopicRequest(
            "Topic Theme 3",
            "Topic Text 3"
        )

        val topicInfoResponse = TopicInfoResponse(
            "Topic Theme 3",
            "Topic has created"
        )

        `when`(topicService.createTopic(topic.theme!!,
            topic.text!!,mockUser))
            .thenReturn(TopicInfoResponse(topic.theme!!, "Topic has created"))

        mockMvc.post("/api/v1/topic/create") {
            with(SecurityMockMvcRequestPostProcessors.user(mockUser))
            content = objectMapper.writeValueAsString(topic)
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(topicInfoResponse))
                }
            }
    }

    @Test
    fun `should delete the topic`() {
        val returnedTopic = TopicInfoResponse(
            mockTopics[1].theme,
            "Topic has deleted"
        )
        `when`(topicService.deleteTopic(topicId, userId.toString()))
            .thenReturn(returnedTopic)
        mockMvc.delete("/api/v1/topic/delete/${topicId}") {
            with(SecurityMockMvcRequestPostProcessors.user(mockUser))
        }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(returnedTopic))
                }
            }
    }

    @Test
    fun `should edit the topic`() {
        val requestContent = TopicRequest(
            "Test Topic 3",
            "Test text topic 3"
        )
        val responseContent = TopicInfoResponse(
            requestContent.theme!!,
            "Topic has been updated"
        )

        `when`(topicService.editTopic(topicId, requestContent, userId.toString()))
            .thenReturn(responseContent)

        mockMvc.put("/api/v1/topic/edit/${topicId}") {
            with(SecurityMockMvcRequestPostProcessors.user(mockUser))
            content = objectMapper.writeValueAsString(requestContent)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(objectMapper.writeValueAsString(responseContent))
            }
        }
    }
}