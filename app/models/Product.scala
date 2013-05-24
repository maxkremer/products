package models
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Product(id: Long, ean: Long, name: String, description: String){
  
  def this( ean: Long, name: String, description: String) = this(0, ean, name, description)
  
}

object Product {

  val productParser: RowParser[Product] = {
    get[Long]("id") ~
      get[Long]("ean") ~
      get[String]("name") ~
      get[String]("description") map {
        case id ~ ean ~ name ~ description => Product(id, ean, name, description)
      }
  }

  def findAll = DB.withConnection { implicit c =>
    SQL("select id, ean, name, description from products").as(productParser *)
  }

  def findByEan(ean: Long): Option[Product] = DB.withConnection { implicit c =>
    val selectEan = SQL("select id, ean, name, description from products where ean = {ean}").on("ean" -> ean).as(productParser *)
    
    def listEan(xs: List[Product]) = xs match{
      case List() => None
      case x :: List() => Option(x)
      case x :: ys => Option(x)
    }
    
    listEan(selectEan)
  }

  def add(product: Product): Boolean = {
    DB.withConnection { implicit connection =>
      SQL("""insert
    		  into products
    		  values ({id}, {ean}, {name}, {description})""").on(
    				  "id" -> product.id,
    				  "ean" -> product.ean,
    				  "name" -> product.name,
    				  "description" -> product.description).executeUpdate() == 1
    }
  }
  
   def save(product: Product): Boolean = {
    DB.withConnection { implicit connection =>
      SQL("""update products
    		  set ean = {ean},
              name= {name},
              description = {description}
              WHERE id = {id} """).on(
    				  "id" -> product.id,
    				  "ean" -> product.ean,
    				  "name" -> product.name,
    				  "description" -> product.description).executeUpdate() == 1
    }
  }

}