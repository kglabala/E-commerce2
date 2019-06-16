package controllers

import javax.inject._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class OrderController @Inject()(basketsRepo: BasketRepository, ordersRepo: OrderRepository, usersRepo: UserRepository,
                                  cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  /**
   * The mapping for the order form.
   */
  val orderForm: Form[CreateOrderForm] = Form {
    mapping(
      "user" -> number,
      "basket" -> number,
      "address" -> nonEmptyText,
      "status" -> number,
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  def addOrder = Action.async { implicit request =>
    var u:Seq[User] = Seq[User]()
    val users = usersRepo.list().onComplete{
      case Success(user) => u = user
      case Failure(_) => print("fail")
    }
    var b:Seq[Basket] = Seq[Basket]()
    val baskets = basketsRepo.list().onComplete{
      case Success(basket) => b = basket
      case Failure(_) => print("fail")
    }

    orderForm.bindFromRequest.fold(
      // The error function. We return the Order page with the error form, which will render the errors.
      // We also wrap the result in a successful future, since this action is synchronous, but we're required to return
      // a future because the order creation function returns a future.
      errorForm => {
        Future.successful(
            Ok(views.html.Order(errorForm, u, b))
          )
      },
      // There were no errors in the from, so create the order.
      order => {
        ordersRepo.create(order.user, order.basket, order.address, order.status).map { _ =>
          // If successful, we simply redirect to the Order page.
          Redirect(routes.OrderController.getOrders).flashing("success" -> "order.created")
        }
      }
    )
  }


  /**
   * A REST endpoint that gets all the people as JSON.
   */
  def getOrders = Action.async { implicit request =>
    ordersRepo.list().map { orders =>
      Ok(Json.toJson(orders))
    }
  }

  def getOrder(id: Integer) = Action.async { implicit request =>
    ordersRepo.get(id).map{order => Ok(Json.toJson(order))}
  }

  def handleOrderPost = Action.async { implicit request =>
    val user = request.body.asJson.get("user").as[Int]
    val basket = request.body.asJson.get("basket").as[Int]
    val address = request.body.asJson.get("address").as[String]
    val status = request.body.asJson.get("status").as[Int]

    ordersRepo.create(user, basket, address, status).map { order =>
      Ok(Json.toJson(order))
    }
  }
}

/**
 * The create order form.
 *
 * Generally for forms, you should define separate objects to your models, since forms very often need to present data
 * in a different way to your models.  In this case, it doesn't make sense to have an id parameter in the form, since
 * that is generated once it's created.
 */
case class CreateOrderForm(user: Int, basket: Int, address: String, status: Int)
