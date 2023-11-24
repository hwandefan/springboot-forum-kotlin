package com.hwandefan.forum.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.UUID

@Entity
@Table(name="_users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private val id:UUID = UUID.randomUUID(),
    @Column(name = "firstName")
    var firstName:String,
    @Column(name = "lastName")
    var lastName: String,
    @Column(name = "email", unique = true)
    private val email: String,
    @Column(name = "password")
    private val password: String,
    @Enumerated(EnumType.STRING)
    private var role:Role,
    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "user")
    val preferences:UserPreferences
):UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.name))
    }
    fun getRole():Role{
        return this.role
    }

    fun setRole(role: Role) {
        this.role = role
    }

    fun getId():UUID {
        return this.id
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