package shop.programs

import cats.MonadThrow
import cats.data.NonEmptyList
import cats.implicits.catsSyntaxApplicativeError
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.monadError._
import retry.RetryPolicy
import shop.domain.auth.UserId
import shop.domain.checkout.Card
import shop.domain.order.{EmptyCartError, OrderId, PaymentError, PaymentId}
import shop.domain.payment.Payment
import shop.http.clients.PaymentClient
import shop.retries.{Retriable, Retry}
import shop.services.{Orders, ShoppingCart}

final case class Checkout[F[_]: MonadThrow: Retry](payments: PaymentClient[F], cart: ShoppingCart[F], orders: Orders[F], policy: RetryPolicy[F]) {

  private def ensureNonEmpty[A](xs: List[A]): F[NonEmptyList[A]] =
    MonadThrow[F].fromOption(NonEmptyList.fromList(xs), EmptyCartError)

  private def processPayment(in: Payment): F[PaymentId] =
    Retry[F]
      .retry(policy, Retriable.Payments)(payments.process(in))
      .adaptError { case e =>
        PaymentError(Option(e.getMessage).getOrElse("Unknown"))
      }

  def process(userId: UserId, card: Card): F[OrderId] =
    for {
      c   <- cart.get(userId)
      its <- ensureNonEmpty(c.items)
      pid <- processPayment(Payment(userId, c.total, card))
      oid <- orders.create(userId, pid, its, c.total)
      _   <- cart.delete(userId).attempt.void
    } yield oid

}
