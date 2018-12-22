package services.domain

import java.util.Date

import services.domain.Job.JobId
import services.domain.Company.CompanyId

case class Job(
              jobId: Int,
              employerId : Int ,
              title : String,
              functionId : Int,
              datePublished : Date,
              status : Int,
              descritpion : String,
              location : String,
              hasPensionInsurance : Boolean,
              hasHealthInsurance : Boolean,
              targetIncome : Int,
              maxIncome : Int,
              hasMealVouchers : Boolean,
              hasCar : Boolean,
              hasHomeWorking : Boolean,
              hasBonus : Boolean
              )

object Job {
  type JobId = String

  def tupled = (Job.apply _).tupled

  object views {

    object JobLocationsView {
      case class Job(
                      companyId: CompanyId,
                      jobId: JobId,
                      title: String,
                      latitude: Float,
                      longitude: Float
      )
    }

    object JobsByCompanyView {
      case class Job(id: String, title: String)
    }

  }
}