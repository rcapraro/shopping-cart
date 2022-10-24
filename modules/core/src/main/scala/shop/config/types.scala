package shop.config
import com.comcast.ip4s.{Host, Port}

import scala.concurrent.duration.FiniteDuration

case class AppConfig(httpServerConfig: HttpServerConfig)

case class HttpServerConfig(host: Host, port: Port)

case class HttpClientConfig(timeout: FiniteDuration, idleTimeInPool: FiniteDuration)

case class RedisURI(value: String) extends AnyVal
case class RedisConfig(value: RedisURI)

case class PaymentURI(value: String) extends AnyVal
case class PaymentConfig(uri: PaymentURI)
