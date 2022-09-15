package shop.domain

import shop.domain.item.ItemId
import squants.{Money, Quantity}

case class OrderId(uuid: UUID) extends AnyVal
case class PaymentId(uuid: UUID) extends AnyVal
case class Order(
                  id: OrderId,
                  pid: PaymentId,
                  items: Map[ItemId, Quantity],
                  total: Money
                )
