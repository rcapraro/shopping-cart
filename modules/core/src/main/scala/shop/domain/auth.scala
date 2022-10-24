package shop.domain

import cats.derived.*
import cats.{Eq, Show}
import io.circe.Codec

import java.util.UUID
import scala.util.control.NoStackTrace

object auth {

  final case class UserId(value: UUID) derives Codec.AsObject, Show, Eq

  final case class UserName(value: String) derives Codec.AsObject, Show, Eq

  final case class Password(value: String) derives Codec.AsObject, Show, Eq

  final case class EncryptedPassword(value: String) derives Codec.AsObject, Show, Eq

  final case class UserNameParam(value: String) derives Codec.AsObject {
    def toDomain: UserName = UserName(value.toLowerCase)
  }

  final case class PasswordParam(value: String) derives Codec.AsObject {
    def toDomain: Password = Password(value)
  }

  final case class CreateUser(username: UserNameParam, password: PasswordParam) derives Codec.AsObject

  final case class UserNotFound(username: UserName) extends NoStackTrace

  final case class UserNameInUse(username: UserName) extends NoStackTrace

  final case class InvalidPassword(username: UserName) extends NoStackTrace

  case object UnsupportedOperation extends NoStackTrace

  case object TokenNotFound extends NoStackTrace

  final case class LoginUser(username: UserNameParam, password: PasswordParam) derives Codec.AsObject

}
