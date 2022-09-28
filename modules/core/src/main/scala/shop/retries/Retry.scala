package shop.retries

import cats.effect.Temporal
import cats.syntax.show._
import org.typelevel.log4cats.Logger
import retry.{retryingOnAllErrors, RetryDetails, RetryPolicy}

trait Retry[F[_]] {
  def retry[A](policy: RetryPolicy[F], retriable: Retriable)(fa: F[A]): F[A]
}

object Retry {
  def apply[F[_]: Retry]: Retry[F] = implicitly

  implicit def forLoggerTemporal[F[_]: Logger: Temporal]: Retry[F] =
    new Retry[F] {
      override def retry[A](policy: RetryPolicy[F], retriable: Retriable)(fa: F[A]): F[A] = {
        def onError(e: Throwable, details: RetryDetails): F[Unit] =
          details match {
            case RetryDetails.GivingUp(totalRetries, _) =>
              Logger[F].error(s"Failed on ${retriable.show} after $totalRetries retries.")
            case RetryDetails.WillDelayAndRetry(_, retriesSoFar, _) =>
              Logger[F].error(s"Failed on ${retriable.show}. We retried $retriesSoFar times.")
          }

        retryingOnAllErrors(policy, onError)(fa)

      }
    }
}
