package com.hwandefan.forum.repository.user

import com.hwandefan.forum.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
public interface UserRepository: JpaRepository<User, UUID> {
    public fun findByEmail(email: String): UserDetails?
}