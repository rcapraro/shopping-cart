package shop.services

import cats.effect.{MonadCancel, MonadCancelThrow, Resource}
import shop.domain.brand.{Brand, BrandId, BrandName}
import shop.effects.GenUUID
import shop.sql.codecs.*
import skunk.*
import skunk.implicits.*

trait Brands[F[_]] {
  def findAll: F[List[Brand]]
  def create(name: BrandName): F[BrandId]
}

object Brands {
  def make[F[_]: GenUUID: MonadCancelThrow](postgres: Resource[F, Session[F]]): Brands[F] =
    new Brands[F] {
      import BrandSQL.*
      override def findAll: F[List[Brand]] = ???

      override def create(name: BrandName): F[BrandId] = ???
    }
}

private object BrandSQL {

  val codec: Codec[Brand] = (brandId ~ brandName).gimap[Brand]

  val selectAll: Query[Void, Brand] =
    sql"""
        SELECT * FROM brands
       """.query(codec)

  val insertBrand: Command[Brand] =
    sql"""
        INSERT INTO brands
        VALUES ($codec)
        """.command

}
