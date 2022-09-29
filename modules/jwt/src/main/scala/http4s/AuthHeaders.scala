package http4s

import http4s.jwt.*
import org.http4s.*
import org.http4s.Credentials.Token
import org.http4s.headers.Authorization

object AuthHeaders {
  def getBearerToken[F[_]](request: Request[F]): Option[JwtToken] =
    request.headers.get[Authorization].collect { case Authorization(Token(AuthScheme.Bearer, token)) =>
      JwtToken(token)
    }
}
