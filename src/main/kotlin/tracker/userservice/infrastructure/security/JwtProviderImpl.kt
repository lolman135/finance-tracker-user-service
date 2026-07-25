package tracker.userservice.infrastructure.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import tracker.userservice.application.JwtProvider
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

@Component
class JwtProviderImpl(
    @param:Value("\${auth.jwt.secret-key}") private val key: String,
    @param:Value("\${auth.jwt.access-token-ttl-sec}") private val jwtExpirationSec: Long
) : JwtProvider {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(key.toByteArray())

    override fun generateToken(id: UUID): String {
        val now = Date()
        val expiryDate = Date(now.time + (jwtExpirationSec * 1000))

        return Jwts.builder()
            .setSubject(id.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    override fun getUserIdFromToken(token: String): UUID = UUID.fromString(
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    )

    override fun validateToken(token: String): Boolean = try {
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
        true
    } catch (_: Exception){
        false
    }
}