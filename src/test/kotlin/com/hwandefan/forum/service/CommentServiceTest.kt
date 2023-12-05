package com.hwandefan.forum.service

import com.hwandefan.forum.api.topic.comment.CommentRequest
import com.hwandefan.forum.model.*
import com.hwandefan.forum.repository.topic.TopicRepository
import com.hwandefan.forum.repository.topic.comment.CommentRepository
import com.hwandefan.forum.service.topic.comment.CommentService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class CommentServiceTest {
    @InjectMocks
    private lateinit var commentService: CommentService

    @Mock
    private lateinit var topicRepository: TopicRepository

    @Mock
    private lateinit var commentRepository: CommentRepository

    private val mockUser = User(
        UUID.randomUUID(),
        "Test User Name",
        "Test User Surname",
        "test",
        "test",
        Role.USER,
        UserPreferences()
    )

    private val topicId = UUID.randomUUID()
    private val mockCommentRequest = CommentRequest(
        topicId.toString(),
        "Test Text"
    )

    private val mockTopic = Topic(
            topicId,
            "Test Topic Theme",
            "Test Topic Text",
            arrayListOf(),
            mockUser,
            Status.ORIGINAL
    )

    private val mockComment = Comment(
        UUID.randomUUID(),
        "Comment text",
        mockTopic,
        mockUser,
        Status.ORIGINAL
    )

    @Test
    fun `should add new comment`() {
        `when`(topicRepository.findById(topicId)).thenReturn(Optional.of(mockTopic))
        commentService.addComment(mockCommentRequest,mockUser)
        verify(commentRepository).save(Mockito.any(Comment::class.java))
    }

    @Test
    fun `should edit the comment`() {
        val commentId = UUID.fromString(mockComment.getId())
        `when`(commentRepository.findById(commentId))
            .thenReturn(Optional.of(mockComment))
        commentService.editComment(mockCommentRequest,commentId,mockUser)
        verify(commentRepository).save(mockComment)
    }

    @Test
    fun `should delete the comment`() {
        val commentId = UUID.fromString(mockComment.getId())
        `when`(commentRepository.findById(commentId))
            .thenReturn(Optional.of(mockComment))
        val response = commentService.deleteComment(commentId,mockUser)
        verify(commentRepository).delete(mockComment)
        assert(response!!.status == Status.DELETED)
    }
}