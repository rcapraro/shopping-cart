package shop.effects

import cats.effect.std.Supervisor
import cats.effect.Temporal

import scala.concurrent.duration.FiniteDuration
import cats.syntax.apply._
import cats.syntax.functor._

trait Background[F[_]] {
  def schedule[A](fa: F[A], duration: FiniteDuration): F[Unit]
}

object Background {
  def apply[F[_]: Background]: Background[F] = implicitly

  implicit def bgInstance[F[_]](implicit S: Supervisor[F], T: Temporal[F]): Background[F] =
    new Background[F] {
      override def schedule[A](fa: F[A], duration: FiniteDuration): F[Unit] =
        S.supervise(T.sleep(duration) *> fa).void
    }
}
