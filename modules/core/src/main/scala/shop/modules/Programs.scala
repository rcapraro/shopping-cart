package shop.modules

import cats.effect.Temporal
import org.typelevel.log4cats.Logger
import shop.effects.Background

class Programs {
  def make[F[_]: Background: Logger: Temporal] = ???
}
