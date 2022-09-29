package shop.domain

import cats.derived.*
import cats.{Eq, Show}
import io.circe.Codec
import shop.Money.given
import shop.domain.cart.Quantity
import shop.domain.item.ItemId
import squants.market.Money

import java.util.UUID
import scala.util.control.NoStackTrace

object order {
  case class OrderId(uuid: UUID) derives Codec.AsObject, Show, Eq

  case class PaymentId(uuid: UUID) derives Codec.AsObject, Show, Eq

  case class Order(id: OrderId, pid: PaymentId, items: Map[ItemId, Quantity], total: Money) derives Codec.AsObject

  case object EmptyCartError extends NoStackTrace derives Show

  sealed trait OrderOrPaymentError extends NoStackTrace derives Show {
    def cause: String
  }

  case class OrderError(cause: String) extends OrderOrPaymentError derives Show

  case class PaymentError(cause: String) extends OrderOrPaymentError derives Show
}
