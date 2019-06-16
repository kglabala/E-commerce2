package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class UserRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import profile.api._

  /**
    * Here we define the table. It will have a name of people
    */
  class UserTable(tag: Tag) extends Table[User](tag, "user") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def surname = column[String]("surname")
    def email = column[String]("email")
    def address = column[String]("address")
	def token = column[String]("token")

    def * = (id, name, surname, email, address, token) <> ((User.apply _).tupled, User.unapply)
  }

  /**
    * The starting point for all queries on the people table.
    */
  val userTable = TableQuery[UserTable]

  /**
   * Create a person with the given name and age.
   *
   * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
   * id for that person.
   */
  def create(name: String, surname: String, email: String, address: String, token: String): Future[User] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (userTable.map(u => (u.name, u.surname, u.email, u.address, u.token))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning userTable.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into {case ((name, surname, email, address, token), id) => User(id, name, surname, email, address, token)}
    // And finally, insert the person into the database
    ) += (name, surname, email, address, token)
  }

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[User]] = db.run {
    userTable.result
  }

  def get(user_id: Int): Future[Seq[User]] = db.run {
    userTable.filter(_.id === user_id).result
  }
}
