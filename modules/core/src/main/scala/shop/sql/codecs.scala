package shop.sql

import shop.domain.auth.*
import shop.domain.brand.*
import shop.domain.category.*
import shop.domain.item.*
import shop.domain.order.*
import skunk.*
import skunk.codec.all.*
import squants.market.*

object codecs {
  val brandId: Codec[BrandId]     = uuid.imap[BrandId](BrandId(_))(_.value)
  val brandName: Codec[BrandName] = varchar.imap[BrandName](BrandName.apply)(_.value)

  val categoryId: Codec[CategoryId]     = uuid.imap[CategoryId](CategoryId.apply)(_.value)
  val categoryName: Codec[CategoryName] = varchar.imap[CategoryName](CategoryName.apply)(_.value)

  val itemId: Codec[ItemId]            = uuid.imap[ItemId](ItemId(_))(_.value)
  val itemName: Codec[ItemName]        = varchar.imap[ItemName](ItemName(_))(_.value)
  val itemDesc: Codec[ItemDescription] = varchar.imap[ItemDescription](ItemDescription(_))(_.value)

  val orderId: Codec[OrderId]     = uuid.imap[OrderId](OrderId(_))(_.value)
  val paymentId: Codec[PaymentId] = uuid.imap[PaymentId](PaymentId(_))(_.value)

  val userId: Codec[UserId]     = uuid.imap[UserId](UserId.apply)(_.value)
  val userName: Codec[UserName] = varchar.imap[UserName](UserName.apply)(_.value)

  val money: Codec[Money] = numeric.imap[Money](USD(_))(_.amount)

  val encPassword: Codec[EncryptedPassword] = varchar.imap[EncryptedPassword](EncryptedPassword.apply)(_.value)
}
