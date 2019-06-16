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

class CategoryController @Inject()(categoriesRepo: CategoryRepository,
                                  cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  /**
   * The mapping for the category form.
   */
  val categoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText,
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  /**
   * The Product action.
  def Product = Action.async { implicit request =>
    val categories = categoriesRepo.list()
    categories.map(cat => Ok(views.html.Product(categoryForm,cat)))

      /*
      .onComplete{
      case Success(categories) => Ok(views.html.Product(categoryForm,categories))
      case Failure(t) => print("")
    }*/
  }
    */

  /**
   * The add category action.
   *
   * This is asynchronous, since we're invoking the asynchronous methods on PersonRepository.
   */
/*
  def addCategory = Action.async { implicit request =>
    Ok(views.html.addproduct())
  }
*/

  def addCategory = Action.async { implicit request =>
    // Bind the form first, then fold the result, passing a function to handle errors, and a function to handle succes.
  /*    var a:Seq[Category] = Seq[Category]()
      val categories = categoriesRepo.list().onComplete{
        case Success(cat) => a= cat
      case Failure(_) => print("fail")
    } */

    categoryForm.bindFromRequest.fold(
      // The error function. We return the Product page with the error form, which will render the errors.
      // We also wrap the result in a successful future, since this action is synchronous, but we're required to return
      // a future because the category creation function returns a future.
      errorForm => {
        Future.successful(
            Ok(views.html.Category(errorForm))
          )
      },
      // There were no errors in the from, so create the category.
      category => {
        categoriesRepo.create(category.name).map { _ =>
          // If successful, we simply redirect to the Product page.
          Redirect(routes.CategoryController.getCategories).flashing("success" -> "category.created")
        }
      }
    )
  }


  /**
   * A REST endpoint that gets all the people as JSON.
   */
  def getCategories = Action.async { implicit request =>
    categoriesRepo.list().map { categories =>
      Ok(Json.toJson(categories))
    }
  }

  def handleCategoryPost = Action.async { implicit request =>
    val name = request.body.asJson.get("name").as[String]

    categoriesRepo.create(name).map { category =>
      Ok(Json.toJson(category))
    }
  }



}

/**
 * The create category form.
 *
 * Generally for forms, you should define separate objects to your models, since forms very often need to present data
 * in a different way to your models.  In this case, it doesn't make sense to have an id parameter in the form, since
 * that is generated once it's created.
 */
case class CreateCategoryForm(name: String)
