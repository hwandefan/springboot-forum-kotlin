package com.hwandefan.forum.service

import com.hwandefan.forum.model.*
import com.hwandefan.forum.repository.topic.TopicRepository
import com.hwandefan.forum.repository.topic.comment.CommentRepository
import com.hwandefan.forum.service.topic.TopicService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
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

    private val topicList = listOf(
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

        val pageList: Page<Topic> = PageImpl(topicList, pageable, topicList.size.toLong())

        `when`(topicRepository.findAll(pageable)).thenReturn(pageList)

        val result = topicService.getAllTopics(page)

        assert(result[1].text == "Test text 2")
        assert(result[0].status == Status.ORIGINAL)
        assert(result[1].status == Status.EDITED)
    }

    @Test
    fun `should return one topic by id`(){
        
    }

    @Test
    fun `should return users topics`(){

    }

    @Test
    fun `should return filtered topics`(){

    }

    @Test
    fun `should create topic`(){

    }

    @Test
    fun `should delete topic`(){

    }
}