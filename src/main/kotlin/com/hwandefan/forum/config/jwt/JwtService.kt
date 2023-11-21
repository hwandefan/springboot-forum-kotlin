package com.hwandefan.forum.config.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class JwtService {
    @Value("\${jwt.secret.key}")
    lateinit var secretKey: String
    fun extractUsername(token:String):String? {
        return extractClaim(token, Claims::getSubject);
    }

    fun isTokenValid(token: String, userDetails: UserDetails):Boolean {
        val username = extractUsername(token)
        return (username.equals(userDetails.username) && !isTokenExpired(token))
    }

    private fun isTokenExpired(token:String):Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token:String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    fun generateToken(userDetails: UserDetails):String? {
        return generateToken(mapOf(),userDetails)
    }

    fun generateToken(extraClaims: Map<String, Any>,
                      userDetails: UserDetails):String {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String):Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSignInKey():Key {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}