package shop.http.clients

import cats.effect.kernel.MonadCancelThrow
import cats.syntax.applicativeError.*
import cats.syntax.either.*
import cats.syntax.flatMap.*
import io.circe.syntax.*
import org.http4s.Method.*
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.circe.{JsonDecoder, *}
import org.http4s.client.*
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.{Status, Uri}
import shop.config.PaymentConfig
import shop.domain.order.*
import shop.domain.payment.*

trait PaymentClient[F[_]] {
  def process(payment: Payment): F[PaymentId]
}

object PaymentClient {
  def make[F[_]: JsonDecoder: MonadCancelThrow](cfg: PaymentConfig, client: Client[F]): PaymentClient[F] =
    new PaymentClient[F] with Http4sClientDsl[F] {
      override def process(payment: Payment): F[PaymentId] =
        Uri.fromString(cfg.uri.value + "/payments").liftTo[F].flatMap { uri =>
          client.run(POST(payment, uri)).use { resp =>
            resp.status match
              case Status.Ok | Status.Conflict =>
                resp.asJsonDecode[PaymentId]
              case st =>
                PaymentError(Option(st.reason).getOrElse("unknown")).raiseError[F, PaymentId]
          }
        }
    }
}
