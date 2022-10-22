package shop

import cats.effect.{IO, IOApp}
import org.typelevel.log4cats.slf4j.{Slf4jFactory, Slf4jLogger}
import org.typelevel.log4cats.{LoggerFactory, SelfAwareStructuredLogger}

object Main extends IOApp.Simple {

  override def run: IO[Unit] = ???

  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

}
