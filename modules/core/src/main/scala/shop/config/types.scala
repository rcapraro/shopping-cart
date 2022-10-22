package shop.config
import com.comcast.ip4s.{Host, Port}

case class AppConfig(httpServerConfig: HttpServerConfig)

case class HttpServerConfig(host: Host, port: Port)

case class RedisURI(value: String) extends AnyVal
case class RedisConfig(value: RedisURI)

case class PaymentURI(value: String) extends AnyVal
case class PaymentConfig(uri: PaymentURI)
