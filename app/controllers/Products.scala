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
  val productForm: Form[Product] = Form(
    mapping(
      "ean" -> longNumber.verifying("validation.ean.duplicate", Product.findByEan(_).isEmpty),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText)((ean, name, desc) => Product(0, ean, name, desc))((prod: Product) => Some(prod.ean, prod.name, prod.description)))

  //Implicit conversion object for Product serialization to Json    
  implicit object ProductWrites extends Writes[Product] {
    def writes(p: Product) = Json.toJson(
      Map(
        "ean" -> Json.toJson(p.ean),
        "name" -> Json.toJson(p.name),
        "description" -> Json.toJson(p.description)))
  }

  implicit object ProductReads extends Reads[Product] {
    def reads(json: JsValue) = new Product(
      (json \ "ean").as[Long],
      (json \ "name").as[String],
      (json \ "description").as[String])
  }

  //product list  
  def list = Action { implicit request =>
    val products = Product.findAll
    val productCodes = products.map(_.ean)

    Ok(Json.toJson(productCodes))
  }

  //display an individual product by ean
  def show(ean: Long) = Action { implicit request =>
    Product.findByEan(ean).map { product =>
      Ok(Json.toJson(product))
    }.getOrElse(NotFound)
  }

  def save(ean:Long) = Action(parse.json) { implicit request =>
    val productJson = request.body
    val product = productJson.as[Product]

    Product.save(product)
    Ok("Saved")
  }

  

}