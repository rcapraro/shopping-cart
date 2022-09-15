package shop.domain

import shop.domain.brand.{Brand, BrandId}
import shop.domain.category.{Category, CategoryId}
import squants.Money

import java.util.UUID

object item {
  case class ItemId(value: UUID) extends AnyVal

  case class ItemName(value: String) extends AnyVal

  case class ItemDescription(value: String) extends AnyVal

  case class Item(uuid: ItemId, name: ItemName, description: ItemDescription, price: Money, brand: Brand, category: Category)

  case class CreateItem(name: ItemName, description: ItemDescription, price: Money, brandId: BrandId, categoryId: CategoryId)

  case class UpdateItem(id: ItemId, price: Money)

}
