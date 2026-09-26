package com.icerockdev.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.icerockdev.auth.acl.CLAIM_SUBJECT
import com.icerockdev.auth.acl.TYPE_CLAIM
import com.icerockdev.auth.acl.USER_CLAIM
import com.icerockdev.util.HashUtils
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.util.Date

private const val TTL_MULTIPLIER = 1000

open class JWTTokenGenerator<TUserKey : Any>(
    private val config: JwtConfig,
    val jsonConverter: ObjectMapper
) {
    private val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(HashUtils.sha512(config.secret)))
    val verifier: JwtParser = Jwts.parserBuilder()
        .requireAudience(config.audience)
        .requireIssuer(config.issuer)
        .requireSubject(CLAIM_SUBJECT)
        .setSigningKey(key)
        .build()

    fun generateTokens(
        user: TUserKey,
        block: JwtBuilder.() -> Unit = {}
    ): JwtTokens {
        val currentTime = System.currentTimeMillis()
        val accessToken = Jwts.builder()
            .setClaims(
                Jwts.claims()
                    .setSubject(CLAIM_SUBJECT)
                    .setAudience(config.audience)
                    .setIssuer(config.issuer)
                    .setIssuedAt(Date(currentTime))
            )
            .claim(TYPE_CLAIM, TokenTypes.TOKEN_TYPE_ACCESS.value)
            .claim(USER_CLAIM, user)
            .setExpiration(Date(currentTime + config.accessTokenTtl * TTL_MULTIPLIER))
            .signWith(key, SignatureAlgorithm.HS512)
        accessToken.apply(block)

        val refreshToken = Jwts.builder()
            .setClaims(
                Jwts.claims()
                    .setSubject(CLAIM_SUBJECT)
                    .setAudience(config.audience)
                    .setIssuer(config.issuer)
                    .setIssuedAt(Date(currentTime))
            )
            .claim(TYPE_CLAIM, TokenTypes.TOKEN_TYPE_REFRESH.value)
            .claim(USER_CLAIM, user)
            .setExpiration(Date(currentTime + config.refreshTokenTtl * TTL_MULTIPLIER))
            .signWith(key, SignatureAlgorithm.HS512)
        refreshToken.apply(block)


        return JwtTokens(
            accessToken = accessToken.compact(),
            refreshToken = refreshToken.compact()
        )
    }

    inline fun <reified TUserKey> getUserKey(jwtToken: String): TUserKey {
        return jsonConverter.convertValue(verifier.parseClaimsJws(jwtToken).body[USER_CLAIM], TUserKey::class.java)
    }

    fun getIssuedAt(jwtToken: String): Long {
        return verifier.parseClaimsJws(jwtToken).body.issuedAt.time
    }

    fun getAccessTokenTtl(): Long {
        return config.accessTokenTtl * TTL_MULTIPLIER
    }

    fun getRefreshTokenTtl(): Long {
        return config.refreshTokenTtl * TTL_MULTIPLIER
    }
}

enum class TokenTypes(val value: String) {
    TOKEN_TYPE_ACCESS("ACCESS"),
    TOKEN_TYPE_REFRESH("REFRESH")
}

data class JwtTokens(val accessToken: String, val refreshToken: String)

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val accessTokenTtl: Long,
    val refreshTokenTtl: Long
)
