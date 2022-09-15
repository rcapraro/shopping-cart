package shop.domain

import shop.domain.auth.UserId
import shop.domain.checkout.Card
import squants.Money

object payment {

  case class Payment(id: UserId, total: Money, card: Card)

}
