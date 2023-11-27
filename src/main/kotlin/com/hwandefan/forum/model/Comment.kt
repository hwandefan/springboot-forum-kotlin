package com.hwandefan.forum.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name="comments")
data class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private val id:UUID = UUID.randomUUID(),
    @Column(name="text")
    private var text:String,
    @ManyToOne
    @JoinColumn(name="topicId")
    private val topic:Topic,
    @ManyToOne()
    @JoinColumn(name="userId")
    private val user: User,
    @Column(name="status")
    private var status: Status
) {
    fun getUserId():String{
        return this.user.getUserId().toString()
    }

    fun getStatus():Status{
        return this.status
    }

    fun getText():String{
        return this.text
    }

    fun getId():String{
        return this.id.toString()
    }

    fun setStatus(status: Status) {
        this.status = status
    }

    fun setText(text:String) {
        this.text = text
    }
}