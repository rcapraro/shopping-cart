package shop.domain

import java.util.UUID

object auth {

  case class UserId(value: UUID) extends AnyVal

  case class UserName(value: String) extends AnyVal

  case class Password(value: String) extends AnyVal

  case class EncryptedPassword(value: String) extends AnyVal

}
