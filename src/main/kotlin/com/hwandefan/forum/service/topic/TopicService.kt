package com.hwandefan.forum.service.topic

import com.hwandefan.forum.api.topic.comment.CommentResponse
import com.hwandefan.forum.api.topic.TopicInfoResponse
import com.hwandefan.forum.api.topic.TopicRequest
import com.hwandefan.forum.api.topic.TopicResponse
import com.hwandefan.forum.model.Status
import com.hwandefan.forum.model.Topic
import com.hwandefan.forum.model.User
import com.hwandefan.forum.repository.topic.comment.CommentRepository
import com.hwandefan.forum.repository.topic.TopicRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TopicService(
    private val topicRepository: TopicRepository,
    private val commentRepository: CommentRepository
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

    fun deleteTopic(id:UUID, userId: String): TopicInfoResponse {
        return try {
            val topic = topicRepository.findById(id).get()
            if(topic.getUserId() == userId){
                topicRepository.delete(topic)
                TopicInfoResponse(topic.getTheme(), "Topic has deleted")
            } else {
                TopicInfoResponse(id.toString(), "ERROR: Topic with this id has not deleted")
            }
        } catch (e:Exception) {
            TopicInfoResponse(id.toString(), "ERROR: Topic with this id has not deleted")
        }
    }

    fun editTopic(id:UUID, request: TopicRequest, userId:String): TopicInfoResponse {
        return try {
            val topic = topicRepository.findById(id).get()
            if(topic.getUserId() == userId){
                if (request.text != null)
                    topic.setText(request.text)
                if (request.theme != null)
                    topic.setTheme(request.theme)
                topic.setStatus(Status.EDITED)
                topicRepository.save(topic)
                TopicInfoResponse(topic.getTheme(), "Topic has been updated")
            } else
                TopicInfoResponse(id.toString(), "ERROR: Topic has not been updated")
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
                        it.getId(),
                        it.getTheme(),
                        it.getText(),
                        it.getUserId(),
                        it.getStatus()
                    )
                )
            }
            topics
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getFilteredTopics(name:String): List<TopicResponse> {
        try{
            val topics: ArrayList<TopicResponse> = arrayListOf()
            topicRepository.findByThemeContaining(name).forEach{
                topics.add(
                    TopicResponse(
                        it.getId(),
                        it.getTheme(),
                        it.getText(),
                        it.getUserId(),
                        it.getStatus()
                    )
                )
            }
            return topics
        } catch (e:Exception) {
            return arrayListOf()
        }
    }

    fun getTopicById(id:UUID): TopicResponse {
        try {
            val topic = topicRepository.findById(id).get()
            val commentsForTopic:ArrayList<CommentResponse> = arrayListOf()
            commentRepository.findByTopic(topic).forEach{
                commentsForTopic.add(
                    CommentResponse(
                        it.getId(),
                        it.getText(),
                        it.getUserId(),
                        it.getStatus()
                    )
                )
            }
            return TopicResponse(
                topic.getId(),
                topic.getTheme(),
                topic.getText(),
                topic.getUserId(),
                topic.getStatus(),
                commentsForTopic
            )
        } catch (e:Exception) {
            return TopicResponse(
                "ID-NotFound",
                "NotFound",
                "NotFound",
                "NotFound",
                Status.NOT_FOUND
            )
        }
    }

    fun getLoggedUserTopics(user:User):List<TopicResponse> {
        return try {
            topicRepository.findByUser(user).map { topic ->
                val commentsForTopicInResponse = commentRepository.findByTopic(topic)
                    .map { comment ->
                        CommentResponse(
                            comment.getId(),
                            comment.getText(),
                            comment.getUserId(),
                            comment.getStatus()
                        )
                    }
                TopicResponse(
                    topic.getId(),
                    topic.getTheme(),
                    topic.getText(),
                    topic.getUserId(),
                    topic.getStatus(),
                    commentsForTopicInResponse
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}