package com.hwandefan.forum.repository.topic

import com.hwandefan.forum.model.Topic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
public interface TopicRepository: JpaRepository<Topic,UUID> {
}