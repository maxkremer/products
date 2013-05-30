package controllers

import play.api.mvc.{ Action, Controller }
import models.Product
import play.api.data.Form
import play.api.data.Forms.{ mapping, longNumber, nonEmptyText }
import play.api.i18n.Messages
import play.api.mvc.Flash
import play.api.libs.json._

object Products extends Controller {

  //The product update/create form
  
  //Implicit conversion object for Product serialization to Json    
  implicit object ProductWrites extends Writes[Product] {
    def writes(p: Product) = Json.toJson(
      Map(
        "id" -> Json.toJson(p.id),
        "name" -> Json.toJson(p.name),
        "description" -> Json.toJson(p.description)))
  }

  implicit object ProductReads extends Reads[Product] {
    def reads(json: JsValue) = {
      
      new Product(      
      (json \ "id").as[Option[Long]],
      (json \ "name").as[String],
      (json \ "description").as[String])
      
    }
  }

  //product list  
  def list = Action { implicit request =>
    val products = Product.findAll
    val productCodes = products.map(_.id)

    Ok(Json.toJson(productCodes))
  }

  //display an individual product by id
  def show(id: Long) = Action { implicit request =>
    Product.findByID(id).map { product =>
      Ok(Json.toJson(product))
    }.getOrElse(NotFound)
  }

  def save(ean:Long) = Action(parse.json) { implicit request =>
    val productJson = request.body
    val product = productJson.as[Product]

    Product.save(product)
    Ok("Saved")
  }

  def add() = Action(parse.json) { implicit request =>
    val productJson = request.body
    val product = productJson.as[Product]

    Product.add(product)
    Ok("Saved")
  }


}