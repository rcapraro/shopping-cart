package shop.domain

import shop.domain.cart.Quantity
import shop.domain.item.ItemId
import squants.Money

import java.util.UUID
import scala.util.control.NoStackTrace

object order {
  case class OrderId(uuid: UUID) extends AnyVal

  case class PaymentId(uuid: UUID) extends AnyVal

  case class Order(id: OrderId, pid: PaymentId, items: Map[ItemId, Quantity], total: Money)

  case object EmptyCartError extends NoStackTrace

  sealed trait OrderOrPaymentError extends NoStackTrace {
    def cause: String
  }

  case class OrderError(cause: String) extends OrderOrPaymentError

  case class PaymentError(cause: String) extends OrderOrPaymentError
}
