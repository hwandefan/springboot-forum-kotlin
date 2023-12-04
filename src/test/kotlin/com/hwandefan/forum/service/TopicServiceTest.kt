package com.hwandefan.forum.service

import com.hwandefan.forum.api.topic.TopicRequest
import com.hwandefan.forum.model.*
import com.hwandefan.forum.repository.topic.TopicRepository
import com.hwandefan.forum.repository.topic.comment.CommentRepository
import com.hwandefan.forum.service.topic.TopicService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.*


@ExtendWith(MockitoExtension::class)
internal class TopicServiceTest {

    @InjectMocks
    private lateinit var topicService: TopicService

    @Mock
    private lateinit var topicRepository: TopicRepository

    @Mock
    private lateinit var commentRepository: CommentRepository


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

    private val topicId = UUID.randomUUID()

    private val topicMockList = listOf(
        Topic(
            UUID.randomUUID(),
            "Test Theme",
            "Test text",
            arrayListOf(),
            mockUser,
            Status.ORIGINAL
        ),
        Topic(
            topicId,
            "Test Theme 2",
            "Test text 2",
            arrayListOf(),
            mockUser,
            Status.EDITED
        )
    )

    @Test
    fun `should return all topics`() {
        val page = 0
        val pageable: Pageable = PageRequest.of(page, 20)

        val pageList: Page<Topic> = PageImpl(topicMockList, pageable, topicMockList.size.toLong())

        `when`(topicRepository.findAll(pageable)).thenReturn(pageList)

        val result = topicService.getAllTopics(page)

        assert(result[1].text == "Test text 2")
        assert(result[0].status == Status.ORIGINAL)
        assert(result[1].status == Status.EDITED)
    }

    @Test
    fun `should return one topic by id`(){
        `when`(topicRepository.findById(topicId)).thenReturn(Optional.of(topicMockList[1]))
        val result = topicService.getTopicById(topicId)
        assert(result.userId == mockUser.getUserId().toString())
        assert(result.id == topicId.toString())
    }

    @Test
    fun `should return users topics`(){

    }

    @Test
    fun `should return filtered topics by name`(){
        val topicName = "Theme 2"
        `when`(topicRepository.findByThemeContaining(topicName)).thenReturn(
            topicMockList.filter {
                it.getTheme().contains(topicName)
            }
        )
        val result = topicService.getFilteredTopics(topicName)
        assert(result.size == 1)
        assert(result[0].text == "Test text 2")
    }

    @Test
    fun `should create a topic`() {
        topicService.createTopic("Theme", "Text", mockUser)
        verify(topicRepository).save(Mockito.any(Topic::class.java))
    }

    @Test
    fun `should delete the topic`() {
        `when`(topicRepository.findById(topicId)).thenReturn(Optional.of(topicMockList[1]))
        topicService.deleteTopic(topicId,mockUser.getUserId().toString())
        verify(topicRepository).delete(topicMockList[1])
    }

    @Test
    fun `should edit the topic`() {
        val topicRequest = TopicRequest(
            "Theme from request",
            "Text from request"
        )
        `when`(topicRepository.findById(topicId)).thenReturn(Optional.of(topicMockList[1]))
        val response = topicService.editTopic(topicId,topicRequest,mockUser.getUserId().toString())
        verify(topicRepository).save(topicMockList[1])
        assert(response.topicName == topicRequest.theme)
    }
}