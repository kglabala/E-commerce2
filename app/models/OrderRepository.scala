package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }
import models.BasketRepository
import models.UserRepository

/**
 * A repository for orders.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class OrderRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, basketRepository: BasketRepository, userRepository: UserRepository)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import profile.api._
  import basketRepository.BasketTable
  import userRepository.UserTable

  /**
   * Here we define the table. It will have a name of orders
   */
  class OrderTable(tag: Tag) extends Table[Order](tag, "order") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def user = column[Int]("user")
    def basket = column[Int]("basket")
    def address = column[String]("address")
    def status = column[Int]("status")

    //val basketTable = TableQuery[BasketTable]
    //val userTable = TableQuery[UserTable]

    //def user_fk = foreignKey("#1", user, userTable)(_.id)
    //def basket_fk = foreignKey("#2", basket, basketTable)(_.id)

    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Person object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Person case classes
     * apply and unapply methods.
     */
    def * = (id, user, basket, address, status) <> ((Order.apply _).tupled, Order.unapply)
    //def * = (id, name) <> ((Category.apply _).tupled, Category.unapply)
  }

  /**
    * The starting point for all queries on the people table.
    */
  val orderTable = TableQuery[OrderTable]

  /**
   * Create a person with the given name and age.
   *
   * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
   * id for that person.
   */
  def create(user: Int, basket: Int, address: String, status: Int): Future[Order] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (orderTable.map(b => (b.user, b.basket, b.address, b.status))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning orderTable.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into {case ((user, basket, address, status), id) => Order(id, user, basket, address, status)}
    // And finally, insert the person into the database
    ) += (user, basket, address, status)
  }

  /**
   * List all the orderTables in the database.
   */
  def list(): Future[Seq[Order]] = db.run {
    orderTable.result
  }

  def get(order_id: Int): Future[Seq[Order]] = db.run {
    orderTable.filter(_.id === order_id).result
  }

  def getByBasket(basket_id: Int): Future[Seq[Order]] = db.run {
    orderTable.filter(_.basket === basket_id).result
  }

  def getByUser(user_id: Int): Future[Seq[Order]] = db.run {
    orderTable.filter(_.user === user_id).result
  }
}
