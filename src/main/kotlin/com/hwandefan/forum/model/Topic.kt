package com.hwandefan.forum.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name="topics")
data class Topic (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private val id: UUID = UUID.randomUUID(),
    @Column(name="theme")
    private var theme:String,
    @Column(name="topicText")
    private var topicText: String,
    @OneToMany(mappedBy = "topic", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val comments: List<Comment> = arrayListOf(),
    @ManyToOne()
    @JoinColumn(name="userId")
    private val user:User,
    @Column(name="status")
    private var status: Status
) {
    fun getTheme():String{
        return this.theme
    }

    fun setTheme(theme:String) {
        this.theme = theme
    }

    fun setText(text:String) {
        this.topicText = text
    }

    fun getText():String {
        return this.topicText
    }

    fun setStatus(status: Status){
        this.status = status
    }

    fun getStatus():Status {
        return this.status
    }

    fun getUserId():String {
        return this.user.getUserId().toString()
    }
}