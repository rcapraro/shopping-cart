package shop.http.auth

import cats.derived.*
import cats.{Eq, Show}
import io.circe.Codec
import shop.domain.auth.{EncryptedPassword, UserId, UserName}

object users {

  case class User(id: UserId, name: UserName) derives Codec.AsObject, Show

  case class UserWithPassword(id: UserId, name: UserName, password: EncryptedPassword) derives Codec.AsObject

  case class CommonUser(value: User) derives Show

  case class AdminUser(value: User) derives Show

}
