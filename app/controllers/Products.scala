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

  implicit object ProductWrites extends Writes[Product] {
    def writes(p: Product) = Json.toJson(
      Map(
        "ean" -> Json.toJson(p.ean),
        "name" -> Json.toJson(p.name),
        "description" -> Json.toJson(p.description)))
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

  def save = Action { implicit request =>
    val newProductForm = this.productForm.bindFromRequest()
    newProductForm.fold(
      hasErrors = {
        form => Redirect(routes.Products.newProduct()).flashing(Flash(form.data) + ("error" -> Messages("validation.errors")))
      },
      success = {
        newProduct =>
          Product.add(newProduct)
          val message = Messages("products.new.success", newProduct.name)
          Redirect(routes.Products.show(newProduct.ean)).flashing("success" -> message)
      })
  }

  def newProduct = Action { implicit request =>
    val form = if (flash.get("error").isDefined)
      this.productForm.bind(flash.data)
    else
      this.productForm
    Ok(views.html.products.editProduct(form))
  }

}