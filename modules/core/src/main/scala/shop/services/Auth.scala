package shop.services

import http4s.jwt.JwtToken
import shop.domain.auth.{Password, UserName}
import shop.http.auth.users.User

trait Auth[F[_]] {
  def findUser(token: JwtToken): F[Option[User]]
  def newUser(username: UserName, password: Password): F[JwtToken]
  def login(username: UserName, password: Password): F[JwtToken]
  def logout(token: JwtToken, username: UserName): F[Unit]
}
