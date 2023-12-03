package com.hwandefan.forum.api.topic.comment

import com.fasterxml.jackson.databind.ObjectMapper
import com.hwandefan.forum.model.Role
import com.hwandefan.forum.model.Status
import com.hwandefan.forum.model.User
import com.hwandefan.forum.model.UserPreferences
import com.hwandefan.forum.service.topic.comment.CommentService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import java.util.UUID
import org.mockito.Mockito.`when`
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@SpringBootTest
@AutoConfigureMockMvc
internal class CommentControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var commentService: CommentService

    private val objectMapper = ObjectMapper()
    private val topicId = UUID.randomUUID()
    private val commentId = UUID.randomUUID()
    private val mockUser = User(
        UUID.randomUUID(),
        "Test User Name",
        "Test User Surname",
        "test",
        "test",
        Role.USER,
        UserPreferences()
    )

    @Test
    fun `should add the comment to the post`() {
        val commentRequest = CommentRequest(topicId.toString(), "Test Comment")
        val commentResponse = CommentResponse(
            commentId.toString(),
            "Test Comment",
            mockUser.getUserId().toString(),
            Status.CREATED
        )
        `when`(commentService.addComment(commentRequest, mockUser))
            .thenReturn(commentResponse)
        mockMvc.post("/api/v1/comment/add") {
            content = objectMapper.writeValueAsString(commentRequest)
            contentType = MediaType.APPLICATION_JSON
            with(SecurityMockMvcRequestPostProcessors.user(mockUser))
        }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(commentResponse))
                }
            }
    }

    @Test
    fun `should edit the user's comment`() {
        val request = CommentRequest(null, "test")
        val response = CommentResponse(
            commentId.toString(),
            request.text!!,
            mockUser.getUserId().toString(),
            Status.EDITED
        )

        `when`(commentService.editComment(request,commentId,mockUser))
            .thenReturn(response)

        mockMvc.put("/api/v1/comment/edit/${commentId}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
            with(SecurityMockMvcRequestPostProcessors.user(mockUser))
        }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(response))
                }
            }
    }

    @Test
    fun `should delete the user's comment`() {
        val response = CommentResponse(
            commentId.toString(),
            "Test",
            mockUser.getUserId().toString(),
            Status.DELETED
        )

        `when`(commentService.deleteComment(commentId,mockUser))
            .thenReturn(response)

        mockMvc.delete("/api/v1/comment/delete/${commentId}") {
            with(SecurityMockMvcRequestPostProcessors.user(mockUser))
        }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(response))
                }
            }
    }
}