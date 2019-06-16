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

class UserController @Inject()(usersRepo: UserRepository,
                                  cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  /**
   * The mapping for the user form.
   */
  val userForm: Form[CreateUserForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "surname" -> nonEmptyText,
      "email" -> nonEmptyText,
      "address" -> nonEmptyText,
      "token" -> text,
    )(CreateUserForm.apply)(CreateUserForm.unapply)
  }

  def addUser = Action.async { implicit request =>
    userForm.bindFromRequest.fold(
      // The error function. We return the User page with the error form, which will render the errors.
      // We also wrap the result in a successful future, since this action is synchronous, but we're required to return
      // a future because the user creation function returns a future.
      errorForm => {
        Future.successful(
            Ok(views.html.User(errorForm))
          )
      },
      // There were no errors in the from, so create the user.
      user => {
        usersRepo.create(user.name, user.surname, user.email, user.address, user.token).map { _ =>
          // If successful, we simply redirect to the User page.
          Redirect(routes.UserController.getUsers).flashing("success" -> "user.created")
        }
      }
    )
  }


  /**
   * A REST endpoint that gets all the people as JSON.
   */
  def getUsers = Action.async { implicit request =>
    usersRepo.list().map { users =>
      Ok(Json.toJson(users))
    }
  }

  def getUser(id: Integer) = Action.async { implicit request =>
    usersRepo.get(id).map{user => Ok(Json.toJson(user))}
  }

  def handleUserPost = Action.async { implicit request =>
    val name = request.body.asJson.get("name").as[String]
    val surname = request.body.asJson.get("surname").as[String]
    val email = request.body.asJson.get("email").as[String]
    val address = request.body.asJson.get("address").as[String]
    val token = request.body.asJson.get("token").as[String]

    usersRepo.create(name, surname, email, address, token).map { user =>
      Ok(Json.toJson(user))
    }
  }
}

/**
 * The create user form.
 *
 * Generally for forms, you should define separate objects to your models, since forms very often need to present data
 * in a different way to your models.  In this case, it doesn't make sense to have an id parameter in the form, since
 * that is generated once it's created.
 */
case class CreateUserForm(name: String, surname: String, email: String, address: String, token: String)
