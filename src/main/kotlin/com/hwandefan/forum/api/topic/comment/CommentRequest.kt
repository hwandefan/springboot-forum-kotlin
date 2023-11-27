package com.hwandefan.forum.api.topic.comment

data class CommentRequest (
    val topicId: String?,
    val text:String?
)