package models
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Product(id: Option[Long], name: String, description: String)

object Product {

  val productParser: RowParser[Product] = {
    get[Long]("id") ~      
      get[String]("name") ~
      get[String]("description") map {
        case id ~ name ~ description => Product(Option(id),  name, description)
      }
  }

  def findAll = DB.withConnection { implicit c =>
    SQL("select id, name, description from trials").as(productParser *)
  }

  def findByID(id: Long): Option[Product] = DB.withConnection { implicit c =>
    val selectId = SQL("select id, name, description from trials where id = {id}").on("id" -> id).as(productParser *)
    
    def listEan(xs: List[Product]) = xs match{
      case List() => None
      case x :: List() => Option(x)
      case x :: ys => Option(x)
    }
    
    listEan(selectId)
  }

  def add(product: Product): Boolean = {
    DB.withConnection { implicit connection =>
      SQL("""insert
    		  into trials (name, description)
    		  values ( {name}, {description})""").on(    				  
    				  "name" -> product.name,
    				  "description" -> product.description).executeUpdate() == 1
    }
  }
  
   def save(product: Product): Boolean = {
    DB.withConnection { implicit connection =>
      SQL("""update trials set    		 
              name= {name},
              description = {description}
              WHERE id = {id} """).on(
    				  "id" -> product.id,    				  
    				  "name" -> product.name,
    				  "description" -> product.description).executeUpdate() == 1
    }
  }

}