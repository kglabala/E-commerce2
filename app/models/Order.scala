package models

import play.api.libs.json._

case class Order(id: Int, user: Int, product: Int, address: String, status: Int)

object Order {
  implicit val orderFormat = Json.format[Order]
}
