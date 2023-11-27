package com.hwandefan.forum.api.topic

import com.hwandefan.forum.api.topic.comment.CommentResponse
import com.hwandefan.forum.model.Status

data class TopicResponse (
    val id: String,
    val theme:String,
    val text:String,
    val userId:String,
    var status: Status,
    var comments: List<CommentResponse>? = null
)