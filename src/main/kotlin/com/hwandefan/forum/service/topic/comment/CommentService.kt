package com.hwandefan.forum.service.topic.comment

import com.hwandefan.forum.api.topic.comment.CommentRequest
import com.hwandefan.forum.api.topic.comment.CommentResponse
import com.hwandefan.forum.model.Comment
import com.hwandefan.forum.model.Status
import com.hwandefan.forum.model.User
import com.hwandefan.forum.repository.topic.TopicRepository
import com.hwandefan.forum.repository.topic.comment.CommentRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val topicRepository: TopicRepository
) {
    fun addComment(request: CommentRequest, user:User):CommentResponse? {
        try {
            val topic = topicRepository.findById(UUID.fromString(request.topicId)).get()
            val comment = Comment(
                UUID.randomUUID(),
                request.text!!,
                topic,
                user,
                Status.ORIGINAL)
            commentRepository.save(comment)
            return CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getUserId(),
                Status.CREATED
            )
        } catch (e:Exception) {
            return null
        }
    }

    fun editComment(request: CommentRequest, id:UUID, user:User):CommentResponse? {
        try {
            val comment = commentRepository.findById(id).get()
            if(comment.getUserId() == user.getUserId().toString()) {
                if(request.text != null) {
                    comment.setText(request.text)
                    comment.setStatus(Status.EDITED)
                    commentRepository.save(comment)
                    return CommentResponse(
                        comment.getId(),
                        comment.getText(),
                        comment.getUserId(),
                        Status.EDITED
                    )
                }
            }
            return null
        } catch (e:Exception) {
            return null
        }
    }

    fun deleteComment(id:UUID, user: User):CommentResponse? {
        try {
            val comment = commentRepository.findById(id).get()
            if (comment.getUserId() == user.getUserId().toString()){
                commentRepository.delete(comment)
                return CommentResponse(
                    comment.getId(),
                    comment.getText(),
                    comment.getUserId(),
                    Status.DELETED
                )
            }
            return null
        } catch (e:Exception) {
            return null
        }
    }
}