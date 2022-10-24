package shop.domain

import cats.derived.*
import cats.{Eq, Show}
import io.circe.{Codec, Decoder, Encoder}
import monocle.Iso
import org.http4s.QueryParamDecoder
import shop.optics.IsUUID

import java.util.UUID
import scala.util.control.NoStackTrace

object brand {

  final case class BrandId(value: UUID) derives Codec.AsObject, Show, Eq
  object BrandId {
    given uuid: IsUUID[BrandId] = new IsUUID[BrandId] {
      override def _UUID: Iso[UUID, BrandId] = Iso[UUID, BrandId](apply)(_.value)
    }
  }

  final case class BrandName(value: String) derives Codec.AsObject, Show, Eq {
    def toBrand(brandId: BrandId): Brand = Brand(brandId, this)
  }

  final case class BrandParam(value: String) derives Show {
    def toDomain: BrandName = BrandName(value.toLowerCase.capitalize)
  }

  object BrandParam {
    given jsonEncoder: Encoder[BrandParam] =
      Encoder.forProduct1("name")(_.value)

    given jsonDecoder: Decoder[BrandParam] =
      Decoder.forProduct1("name")(BrandParam.apply)
  }

  final case class Brand(uuid: BrandId, name: BrandName) derives Codec.AsObject, Eq, Show

  final case class InvalidBrand(value: String) extends NoStackTrace derives Codec.AsObject

}
