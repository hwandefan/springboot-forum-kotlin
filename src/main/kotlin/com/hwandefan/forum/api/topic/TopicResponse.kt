package com.hwandefan.forum.api.topic

import com.hwandefan.forum.model.Status

data class TopicResponse (
    val theme:String,
    val text:String,
    val userId:String,
    val status: Status
)