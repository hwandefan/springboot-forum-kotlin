package com.hwandefan.forum.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.UUID

@Entity
@Table(name="_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private val id:UUID,
    @Column(name = "firstName")
    private var firstName:String,
    @Column(name = "lastName")
    private val lastName: String,
    @Column(name = "email", unique = true)
    private val email: String,
    @Column(name = "password")
    private val password: String,
    @Enumerated(EnumType.STRING)
    private val role:Role
):UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.name))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}