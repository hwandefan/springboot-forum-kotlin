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
    private val text:String,
    @ManyToOne
    @JoinColumn(name="topicId")
    private val topic:Topic,
    @ManyToOne()
    @JoinColumn(name="userId")
    private val user: User,
    @Column(name="status")
    private val status: Status
)