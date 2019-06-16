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

class BasketController @Inject()(basketsRepo: BasketRepository, productsRepo: ProductRepository, usersRepo: UserRepository,
                                  cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  /**
   * The mapping for the basket form.
   */
  val basketForm: Form[CreateBasketForm] = Form {
    mapping(
      "user" -> number,
      "product" -> number,
      "quantity" -> number,
      "status" -> number,
    )(CreateBasketForm.apply)(CreateBasketForm.unapply)
  }

  def addBasket = Action.async { implicit request =>
    // Bind the form first, then fold the result, passing a function to handle errors, and a function to handle succes.
    var u:Seq[User] = Seq[User]()
    val users = usersRepo.list().onComplete{
      case Success(user) => u = user
      case Failure(_) => print("fail")
    }
    var p:Seq[Product] = Seq[Product]()
    val products = productsRepo.list().onComplete{
      case Success(product) => p = product
      case Failure(_) => print("fail")
    }

    basketForm.bindFromRequest.fold(
      // The error function. We return the Basket page with the error form, which will render the errors.
      // We also wrap the result in a successful future, since this action is synchronous, but we're required to return
      // a future because the basket creation function returns a future.
      errorForm => {
        Future.successful(
            Ok(views.html.Basket(errorForm, u, p))
          )
      },
      // There were no errors in the from, so create the basket.
      basket => {
        basketsRepo.create(basket.user, basket.product, basket.quantity, basket.status).map { _ =>
          // If successful, we simply redirect to the Basket page.
          Redirect(routes.BasketController.getBaskets).flashing("success" -> "basket.created")
        }
      }
    )
  }


  /**
   * A REST endpoint that gets all the people as JSON.
   */
  def getBaskets = Action.async { implicit request =>
    basketsRepo.list().map { baskets =>
      Ok(Json.toJson(baskets))
    }
  }

  def getByUser(id: Int) = Action.async { implicit  request =>
    basketsRepo.getByUser(id).map { baskets =>
      Ok(Json.toJson(baskets))
    }
  }

  def getByProduct(id: Int) = Action.async { implicit  request =>
    basketsRepo.getByProduct(id).map { baskets =>
      Ok(Json.toJson(baskets))
    }
  }

  def handleBasketPost = Action.async { implicit request =>
    val user = request.body.asJson.get("user").as[Int]
    val product = request.body.asJson.get("product").as[Int]

    basketsRepo.create(user, product, 0, 1).map { basket =>
      Ok(Json.toJson(basket))
    }
  }
}

/**
 * The create basket form.
 *
 * Generally for forms, you should define separate objects to your models, since forms very often need to present data
 * in a different way to your models.  In this case, it doesn't make sense to have an id parameter in the form, since
 * that is generated once it's created.
 */
case class CreateBasketForm(user: Int, product: Int, quantity: Int, status: Int)
