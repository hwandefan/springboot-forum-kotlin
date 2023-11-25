package com.hwandefan.forum.service.topic

import com.hwandefan.forum.api.topic.TopicInfoResponse
import com.hwandefan.forum.api.topic.TopicRequest
import com.hwandefan.forum.api.topic.TopicResponse
import com.hwandefan.forum.model.Status
import com.hwandefan.forum.model.Topic
import com.hwandefan.forum.model.User
import com.hwandefan.forum.repository.topic.TopicRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TopicService(
    private val topicRepository: TopicRepository
) {
    fun createTopic(theme:String, text:String, user:User): TopicInfoResponse {
        return try {
            topicRepository.save(
                Topic(
                    UUID.randomUUID(),
                    theme,
                    text,
                    arrayListOf(),
                    user,
                    Status.ORIGINAL
                )
            )
            TopicInfoResponse(theme, "Topic has created")
        } catch (e:Exception) {
            TopicInfoResponse(theme, "Topic has not created")
        }
    }

    fun deleteTopic(id:UUID): TopicInfoResponse {
        return try {
            val topic = topicRepository.findById(id).get()
            topicRepository.delete(topic)
            TopicInfoResponse(topic.getTheme(), "Topic has deleted")
        } catch (e:Exception) {
            TopicInfoResponse(id.toString(), "ERROR: Topic with this id has not deleted")
        }
    }

    fun editTopic(id:UUID, request: TopicRequest): TopicInfoResponse {
        return try {
            val topic = topicRepository.findById(id).get()
            if (request.text != null)
                topic.setText(request.text)
            if (request.theme != null)
                topic.setTheme(request.theme)
            topic.setStatus(Status.EDITED)
            topicRepository.save(topic)
            TopicInfoResponse(topic.getTheme(), "Topic has been updated")
        } catch (e:Exception) {
            TopicInfoResponse(id.toString(), "ERROR: Topic has not been updated")
        }
    }

    fun getAllTopics(page:Int): List<TopicResponse> {
        return try {
            val pageable:PageRequest  = PageRequest.of(page,20)
            val topics: ArrayList<TopicResponse> = arrayListOf()
            topicRepository.findAll(pageable).toList().forEach{
                topics.add(
                    TopicResponse(
                        it.getTheme(),
                        it.getText(),
                        it.getUserId(),
                        it.getStatus()
                    )
                )
            }
            topics
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    //TODO AND COMMENTS
    fun getFilteredTopics(name:String, page:Int): List<TopicResponse> {
        return arrayListOf()
    }

    fun getTopicById(id:UUID): TopicResponse {
        return null!!
    }
}