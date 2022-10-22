package shop.config

import cats.effect.kernel.Async
import ciris.*

object Config {

  def load[F[_]: Async]: F[AppConfig] =
    env("SC_APP_ENV")
      .as[AppEnvironment]
      .flatMap {
        case AppEnvironment.Test =>
          default[F](RedisURI("redis://localhost"), PaymentURI("https://payments.free.beeceptor.com"))
        case AppEnvironment.Prod =>
          default[F](RedisURI("redis://10.123.154.176"), PaymentURI("https://payments.net/api"))
      }
      .load[F]

  private def default[F[_]](redisUri: RedisURI, paymentUri: PaymentURI): ConfigValue[F, AppConfig] = ???

}
