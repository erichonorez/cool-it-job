package models

import play.api.data.Form
import play.api.data.Forms._

case class JobPreviewData(
  title: Option[String],
  location: Option[String],
  description: Option[String],
  targetSalary: Option[BigDecimal],
  maximumSalary: Option[BigDecimal],
  hasHomeWorking: Option[Boolean],
  hasHealthInsurance: Option[Boolean],
  hasCar: Option[Boolean],
  hasLunchVouncher: Option[Boolean],
  hasBonus: Option[Boolean],
  referer: String)

object JobPreviewForm {

  def form = {
    Form(
      mapping(
        "title" -> optional(text),
        "location" -> optional(text),
        "description" -> optional(text),
        "target-salary" -> optional(bigDecimal),
        "maximum-salary" -> optional(bigDecimal),
        "homeworking" -> optional(boolean),
        "health-insurance" -> optional(boolean),
        "car" -> optional(boolean),
        "lunch-voucher" -> optional(boolean),
        "bonus" -> optional(boolean),
        "referer" -> text
      )(JobPreviewData.apply)(JobPreviewData.unapply)
    )
  }

}