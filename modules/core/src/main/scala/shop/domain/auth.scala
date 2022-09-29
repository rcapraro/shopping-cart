package shop.domain

import cats.derived.*
import cats.{Eq, Show}
import io.circe.Codec

import java.util.UUID
import scala.util.control.NoStackTrace

object auth {

  case class UserId(value: UUID) derives Codec.AsObject, Show, Eq

  case class UserName(value: String) derives Codec.AsObject, Show, Eq

  case class Password(value: String) derives Codec.AsObject, Show, Eq

  case class EncryptedPassword(value: String) derives Codec.AsObject, Show, Eq

  case class UserNameParam(value: String) derives Codec.AsObject {
    def toDomain: UserName = UserName(value.toLowerCase)
  }

  case class PasswordParam(value: String) derives Codec.AsObject {
    def toDomain: Password = Password(value)
  }

  case class CreateUser(username: UserNameParam, password: PasswordParam) derives Codec.AsObject

  case class UserNotFound(username: UserName) extends NoStackTrace

  case class UserNameInUse(username: UserName) extends NoStackTrace

  case class InvalidPassword(username: UserName) extends NoStackTrace

  case object UnsupportedOperation extends NoStackTrace

  case object TokenNotFound extends NoStackTrace

  case class LoginUser(username: UserNameParam, password: PasswordParam) derives Codec.AsObject

}
