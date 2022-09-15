package shop.domain

import eu.timepit.refined.auto.autoUnwrap
import eu.timepit.refined.types.string.NonEmptyString

import java.util.UUID

object category {

  case class CategoryId(value: UUID) extends AnyVal

  case class CategoryName(value: String)

  case class CategoryParam(value: NonEmptyString) {
    def toDomain: CategoryName = CategoryName(value.toLowerCase.capitalize)
  }

  case class Category(uuid: CategoryId, name: CategoryName)
}
