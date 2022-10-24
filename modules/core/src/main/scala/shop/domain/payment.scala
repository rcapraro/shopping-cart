package shop.domain

import cats.derived.*
import io.circe.Codec
import shop.domain.Money.given
import shop.domain.auth.UserId
import shop.domain.checkout.Card
import squants.market.Money

object payment {

  case class Payment(id: UserId, total: Money, card: Card) derives Codec.AsObject

}
