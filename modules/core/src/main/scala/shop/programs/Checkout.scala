package shop.programs

import cats.MonadThrow
import cats.data.NonEmptyList
import cats.implicits.catsSyntaxApplicativeError
import cats.syntax.apply.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.monadError.*
import org.typelevel.log4cats.Logger
import retry.RetryPolicy
import shop.domain.auth.UserId
import shop.domain.cart.CartItem
import shop.domain.checkout.Card
import shop.domain.order.*
import shop.domain.payment.Payment
import shop.effects.Background
import shop.http.clients.PaymentClient
import shop.retries.{Retriable, Retry}
import shop.services.{Orders, ShoppingCart}
import squants.market.Money

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

final case class Checkout[F[_]: Background: Logger: Retry: MonadThrow](
  payments: PaymentClient[F],
  cart: ShoppingCart[F],
  orders: Orders[F],
  policy: RetryPolicy[F]
) {

  private def ensureNonEmpty[A](xs: List[A]): F[NonEmptyList[A]] =
    MonadThrow[F].fromOption(NonEmptyList.fromList(xs), EmptyCartError)

  private def processPayment(in: Payment): F[PaymentId] =
    Retry[F]
      .retry(policy, Retriable.Payments)(payments.process(in))
      .adaptError { case e =>
        PaymentError(Option(e.getMessage).getOrElse("Unknown"))
      }

  private def createOrder(userId: UserId, paymentId: PaymentId, items: NonEmptyList[CartItem], total: Money): F[OrderId] = {
    val action =
      Retry[F]
        .retry(policy, Retriable.Orders)(orders.create(userId, paymentId, items, total))
        .adaptError { case e =>
          OrderError(e.getMessage)
        }

    def bgAction(fa: F[OrderId]): F[OrderId] =
      fa.onError { case _ =>
        Logger[F].error(s"Failed to create order for Payment: $paymentId.show") *> Background[F].schedule(bgAction(fa), 1 hour)
      }

    bgAction(action)
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
