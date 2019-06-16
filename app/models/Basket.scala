package models

import play.api.libs.json._

case class Basket(id: Int, user: Int, product: Int, quantity: Int, status: Int)

object Basket {
  implicit val basketFormat = Json.format[Basket]
}
