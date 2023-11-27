package com.hwandefan.forum.repository.topic.comment

import com.hwandefan.forum.model.Comment
import com.hwandefan.forum.model.Topic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
public interface CommentRepository: JpaRepository<Comment,UUID> {
    fun findByTopic(topic:Topic): List<Comment>
}