package shop.services

import shop.domain.auth.{EncryptedPassword, UserId, UserName}
import shop.http.auth.users.UserWithPassword

trait Users[F[_]] {
  def find(username: UserName): F[Option[UserWithPassword]]
  def create(username: UserName, password: EncryptedPassword): F[UserId]
}
