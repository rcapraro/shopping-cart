package shop.effects

import cats.effect.Temporal
import cats.effect.std.Supervisor
import cats.syntax.apply.*
import cats.syntax.functor.*

import scala.concurrent.duration.FiniteDuration

trait Background[F[_]] {
  def schedule[A](fa: F[A], duration: FiniteDuration): F[Unit]
}

object Background {
  def apply[F[_]: Background]: Background[F] = summon

  given bgInstance[F[_]](using S: Supervisor[F], T: Temporal[F]): Background[F] with
    def schedule[A](fa: F[A], duration: FiniteDuration): F[Unit] =
      S.supervise(T.sleep(duration) *> fa).void

}
