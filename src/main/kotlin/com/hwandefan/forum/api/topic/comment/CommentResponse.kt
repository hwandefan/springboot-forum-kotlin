package com.hwandefan.forum.api.topic.comment

import com.hwandefan.forum.model.Status

data class CommentResponse (
    val id:String,
    val text:String,
    val userId:String,
    val status: Status
)