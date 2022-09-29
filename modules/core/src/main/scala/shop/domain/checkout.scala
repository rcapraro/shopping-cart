package shop.domain

import cats.data.{Validated, ValidatedNel}
import cats.derived.*
import cats.{Eq, Show}
import io.circe.{Codec, Encoder}

object checkout {

  type CardNamePred       = String
  type CardNumberPred     = Long
  type CardExpirationPred = String
  type CardCVVPred        = Int

  // TODO validation and custom decoders
  case class CardName(value: CardNamePred) derives Codec.AsObject, Show

  case class CardNumber(value: CardNumberPred) derives Codec.AsObject, Show

  case class CardExpiration(value: CardExpirationPred) derives Codec.AsObject, Show

  case class CardCVV(value: CardCVVPred) derives Codec.AsObject, Show

  case class Card(name: CardName, number: CardNumber, expiration: CardExpiration, cvv: CardCVV) derives Codec.AsObject, Show
}
