package models

import play.api.data.{Form, Forms}
import play.api.data.Forms._
import play.api.data.format.Formats._
import services.domain.Job

case class JobFormData(
  title: String,
  location: String,
  description: String,
  targetSalary: BigDecimal,
  maximumSalary: BigDecimal,
  hasHomeWorking: Boolean,
  hasHealthInsurance: Boolean,
  hasCar: Boolean,
  hasLunchVouncher: Boolean,
  hasBonus: Boolean,
  referer: String,
  latitude: Float,
  longitude: Float)

object JobFormData {

  def form = {
    Form(
      mapping(
        "title" -> nonEmptyText(minLength = 1, maxLength = 255),
        "location" -> nonEmptyText,
        "description" -> nonEmptyText,
        "target-salary" -> bigDecimal,
        "maximum-salary" -> bigDecimal,
        "homeworking" -> boolean,
        "health-insurance" -> boolean,
        "car" -> boolean,
        "lunch-voucher" -> boolean,
        "bonus" -> boolean.verifying("cannot be checked", value => !value),
        "referer" -> text,
        "lat" -> optional(Forms.of[Float]),
        "lng" -> optional(Forms.of[Float])
      )(JobFormData.map)(JobFormData.unmap) verifying("Failed form constraints!", fields => fields match {
        case jobFormData => jobFormData.latitude != 0 && jobFormData.longitude != 0
      })
    )
  }

  def of(job: Job): Form[JobFormData] = {
    val data = JobFormData(
      job.title,
      job.location,
      job.description,
      job.targetSalary,
      job.maximumSalary,
      job.hasHomeWorking,
      job.hasHealthInsurance,
      job.hasCar,
      job.hasLunchVouncher,
      job.hasBonus,
      "",
      job.latitude,
      job.longitude
    )
    JobFormData.form.fill(data)
  }

  def map(title: String,
          location: String,
          description: String,
          targetSalary: BigDecimal,
          maximumSalary: BigDecimal,
          hasHomeWorking: Boolean,
          hasHealthInsurance: Boolean,
          hasCar: Boolean,
          hasLunchVouncher: Boolean,
          hasBonus: Boolean,
          referer: String,
          latitude: Option[Float],
          longitude: Option[Float]): JobFormData = {
    JobFormData(
      title,
      location,
      description,
      targetSalary,
      maximumSalary,
      hasHomeWorking,
      hasHealthInsurance,
      hasCar,
      hasLunchVouncher,
      hasBonus,
      referer,
      latitude.getOrElse(0),
      longitude.getOrElse(0)
    )
  }

  def unmap(jobFormData: JobFormData): Option[(String, String, String, BigDecimal, BigDecimal, Boolean, Boolean, Boolean, Boolean, Boolean, String, Option[Float], Option[Float])] = {
    Some((jobFormData.title, jobFormData.location, jobFormData.description, jobFormData.targetSalary, jobFormData.maximumSalary, jobFormData.hasHomeWorking, jobFormData.hasHealthInsurance, jobFormData.hasCar, jobFormData.hasLunchVouncher, jobFormData.hasBonus, jobFormData.referer, Some(jobFormData.longitude), Some(jobFormData.latitude)))
  }

}