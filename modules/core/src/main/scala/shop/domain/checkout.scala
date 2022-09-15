package shop.domain

import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.And
import eu.timepit.refined.collection.Size
import eu.timepit.refined.string.{MatchesRegex, ValidInt}

object checkout {

  type Rgx = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$"

  type CardNamePred       = String Refined MatchesRegex[Rgx]
  type CardNumberPred     = Long Refined Size[16]
  type CardExpirationPred = String Refined (Size[4] And ValidInt)
  type CardCVVPred        = Int Refined Size[3]

  case class CardName(value: CardNamePred)

  case class CardNumber(value: CardNumberPred)

  case class CardExpiration(value: CardExpirationPred)

  case class CardCVV(value: CardCVVPred)

  case class Card(name: CardName, number: CardNumber, expiration: CardExpiration, cvv: CardCVV)
}
