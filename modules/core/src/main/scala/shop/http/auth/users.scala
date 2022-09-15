package shop.http.auth

import shop.domain.auth.{EncryptedPassword, UserId, UserName}

object users {

  case class User(id: UserId, name: UserName)

  case class UserWithPassword(id: UserId, name: UserName, password: EncryptedPassword)

  case class CommonUser(value: User)

  case class AdminUser(value: User)

}
