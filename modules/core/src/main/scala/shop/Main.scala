package shop

import cats.effect.{IO, IOApp}
import cats.syntax.flatMap.*
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}
import shop.config.Config

import scala.language.postfixOps

object Main extends IOApp.Simple {

  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  override def run: IO[Unit] = {
    Config.load[IO] >>= { cfg =>
      Logger[IO].info(s"Loaded config $cfg")
    }
    ???
  }

}
