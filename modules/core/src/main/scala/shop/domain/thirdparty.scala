package shop.domain

import cats.syntax.contravariant.*
import cats.{Eq, Monoid, Show}
import http4s.jwt.JwtToken
import io.circe.{Decoder, Encoder}
import squants.market.{Currency, EUR, Money}

object Money {
  import Currency.given

  given Decoder[Money] =
    Decoder[BigDecimal].map(EUR.apply)

  given Encoder[Money] =
    Encoder[BigDecimal].contramap(_.amount)

  given Eq[Money] = Eq.and(Eq.by(_.amount), Eq.by(_.currency))

  given Show[Money] = Show.fromToString

  given Monoid[Money] =
    new Monoid[Money] {
      def empty: Money = EUR(0)

      def combine(x: Money, y: Money): Money = x + y
    }
}

object Currency {
  given Eq[Currency] = Eq.and(Eq.and(Eq.by(_.code), Eq.by(_.symbol)), Eq.by(_.name))
}

object JwtToken {
  given Encoder[JwtToken] =
    Encoder.forProduct1("access_token")(_.value)

  given Eq[JwtToken] = Eq.by(_.value)

  given Show[JwtToken] = Show[String].contramap[JwtToken](_.value)
}
