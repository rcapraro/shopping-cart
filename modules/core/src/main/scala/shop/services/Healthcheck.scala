package shop.services

import shop.domain.healthcheck.AppStatus

trait Healthcheck[F[_]] {
  def status: F[AppStatus]
}
