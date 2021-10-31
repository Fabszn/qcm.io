package org.qcmio.environment.http.jwt

import org.qcmio.environment.config.Configuration.JwtConf
import org.qcmio.model.Candidat
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

import java.time.Instant

object JwtUtils {

  def buildToken(email: Candidat.Email, conf: JwtConf): String = {

    val claim = JwtClaim(
      expiration = Some(Instant.now.plusSeconds(10800).getEpochSecond)
      , issuedAt = Some(Instant.now.getEpochSecond)
    ) + ("email", email.value)
    val key = conf.secretKey
    val algo = JwtAlgorithm.HS256

    JwtCirce.encode(claim, key, algo)
  }

  def isValidToken(token: String, jwtConf: JwtConf): Boolean =
    JwtCirce.decode(token, jwtConf.secretKey, Seq(JwtAlgorithm.HS256)).isSuccess



}