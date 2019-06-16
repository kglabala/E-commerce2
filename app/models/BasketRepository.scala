package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }
import models.ProductRepository
import models.UserRepository

/**
 * A repository for baskets.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class BasketRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, productRepository: ProductRepository, userRepository: UserRepository)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import profile.api._
  import productRepository.ProductTable
  import userRepository.UserTable

  /**
   * Here we define the table. It will have a name of baskets
   */
  class BasketTable(tag: Tag) extends Table[Basket](tag, "basket") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def user = column[Int]("user")
    def product = column[Int]("product")
    def quantity = column[Int]("quantity")
    def status = column[Int]("status")

    //val productTable = TableQuery[ProductTable]
    //val userTable = TableQuery[UserTable]

    //def user_fk = foreignKey("#1", user, userTable)(_.id)
    //def product_fk = foreignKey("#2", product, productTable)(_.id)

    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Person object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Person case classes
     * apply and unapply methods.
     */
    def * = (id, user, product, quantity, status) <> ((Basket.apply _).tupled, Basket.unapply)
    //def * = (id, name) <> ((Category.apply _).tupled, Category.unapply)
  }

  /**
    * The starting point for all queries on the people table.
    */
  val basketTable = TableQuery[BasketTable]

  /**
   * Create a person with the given name and age.
   *
   * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
   * id for that person.
   */
  def create(user: Int, product: Int, quantity: Int, status: Int): Future[Basket] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (basketTable.map(b => (b.user, b.product, b.quantity, b.status))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning basketTable.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into {case ((user, product, quantity, status), id) => Basket(id, user, product, quantity, status)}
    // And finally, insert the person into the database
    ) += (user, product, quantity, status)
  }

  /**
   * List all the basketTables in the database.
   */
  def list(): Future[Seq[Basket]] = db.run {
    basketTable.result
  }

  def getByUser(user_id: Int): Future[Seq[Basket]] = db.run {
    basketTable.filter(_.user === user_id).result
  }

  def getByProduct(product_id: Int): Future[Seq[Basket]] = db.run {
    basketTable.filter(_.product === product_id).result
  }
}
