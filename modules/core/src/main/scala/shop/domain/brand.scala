package shop.domain

import eu.timepit.refined.auto.autoUnwrap
import eu.timepit.refined.types.string.NonEmptyString

import java.util.UUID
import scala.util.control.NoStackTrace

object brand {

  case class BrandId(value: UUID) extends AnyVal

  case class BrandName(value: String) extends AnyVal {
    def toBrand(brandId: BrandId): Brand = Brand(brandId, this)
  }

  case class BrandParam(value: NonEmptyString) {
    def toDomain: BrandName = BrandName(value.toLowerCase.capitalize)
  }

  case class Brand(uuid: BrandId, name: BrandName)

  case class InvalidBrand(value: String) extends NoStackTrace

}
