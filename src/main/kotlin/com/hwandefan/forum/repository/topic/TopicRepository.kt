package com.hwandefan.forum.repository.topic

import com.hwandefan.forum.model.Topic
import com.hwandefan.forum.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
public interface TopicRepository: JpaRepository<Topic,UUID> {
    fun findByThemeContaining(substring:String): List<Topic>
    fun findByUser(user: User): List<Topic>
}