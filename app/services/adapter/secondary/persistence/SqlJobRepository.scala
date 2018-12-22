package services.adapter.secondary.persistence

import java.util.UUID

import com.google.inject.Inject
import services.domain.Job.JobId
import services.domain.Company.CompanyId
import services.domain.{Job, JobIdentityProvider, JobRepository}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import slick.jdbc.GetResult

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * This implementation of [[JobRepository]] uses native SQL queries capabilities of Slick.
  */
class SqlJobRepository @Inject() (dbConfigProvider: DatabaseConfigProvider) extends JobRepository with JobIdentityProvider {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  override def jobsByCompanyView(id: CompanyId): Seq[Job.views.JobsByCompanyView.Job] = {
    Await.result(
      dbConfig.db.run(
        sql"""select job_id, title
                    from jobs
                    where company_id = ${id}
         """.as[Job.views.JobsByCompanyView.Job]
      ),
      3000 seconds
    )
  }

  override def create(job: Job): JobId = {
    Await.result(
      dbConfig.db.run(
        sqlu"""insert into jobs (
               company_id,
               job_id,
               title,
               location,
               description,
               target_salary,
               maximum_salary,
               has_homeworking,
               has_health_insurance,
               has_car,
               has_lunch_voucher,
               has_bonus,
               latitude,
               longitude
             ) values (
              ${job.companyId},
              ${job.jobId},
              ${job.title},
              ${job.location},
              ${job.description},
              ${job.targetSalary},
              ${job.maximumSalary},
              ${job.hasHomeWorking},
              ${job.hasHealthInsurance},
              ${job.hasCar},
              ${job.hasLunchVouncher},
              ${job.hasBonus},
              ${job.latitude},
              ${job.longitude}
            )"""
      ),
      3000 seconds
    )
    job.jobId
  }

  override def jobOfId(companyId: CompanyId, jobId: JobId): Option[Job] = {
    Await.result(
      dbConfig.db.run(
        sql"""
          SELECT `Job_ID`, `Job_EmployerID`, `Job_Title`, `Job_FunctionID`, `Job_DatePublished`, `Job_Status`, `Job_Description`, `Job_Location`, `Job_PensionInsurance`, `Job_HealthInsurance`, `Job_TargetIncome`, `Job_MaxIncome`, `Job_MealVoucher`, `Job_Car`, `Job_HomeWorking`, `Job_Bonus`
          FROM `jobs`
          where Job_EmployerID = ${companyId} and Job_ID = ${jobId}
      """.as[Job].headOption
      ),
      3000 seconds
    )
  }

  implicit val jobResult = GetResult(r => {
    Job(
      r.nextInt,
      r.nextInt,
      r.nextString(),
      r.nextInt,
      r.nextDate(),
      r.nextInt(),
      r.nextString(),
      r.nextString(),
      r.nextBoolean(),
      r.nextBoolean(),
      r.nextInt(),
      r.nextInt(),
      r.nextBoolean(),
      r.nextBoolean(),
      r.nextBoolean(),
      r.nextBoolean()
    )
  })

  override def update(job: Job): Unit = {
    Await.result(
      dbConfig.db.run(
        sqlu"""update jobs
                set title = ${job.title},
                  location = ${job.location},
                  description = ${job.description},
                  target_salary = ${job.targetSalary},
                  maximum_salary = ${job.maximumSalary},
                  has_homeworking = ${job.hasHomeWorking},
                  has_health_insurance = ${job.hasHealthInsurance},
                  has_car = ${job.hasCar},
                  has_lunch_voucher = ${job.hasLunchVouncher},
                  has_bonus = ${job.hasBonus},
                  latitude = ${job.latitude},
                  longitude = ${job.longitude}
               where job_id = ${job.jobId}
            """
      ),
      3000 seconds
    )
  }

  override def delete(job: Job): Unit = {
    Await.result(
      dbConfig.db.run(
        sqlu"delete from jobs where company_id = ${job.companyId} and job_id = ${job.jobId}"
      ),
      3000 seconds
    )
  }

  override def jobLocationsView(): Seq[Job.views.JobLocationsView.Job] = {
    Await.result(
      dbConfig.db.run(
        sql"""select company_id, job_id, title, latitude, longitude from jobs""".as[Job.views.JobLocationsView.Job]
      ),
      3000 seconds
    )
  }

  implicit val JjobsByCompanyViewJob = GetResult(r => {
    Job.views.JobsByCompanyView.Job(r.nextString(), r.nextString())
  })



  implicit val jobLocationsViewJob = GetResult(r => {
    Job.views.JobLocationsView.Job(
      r.nextString(),
      r.nextString(),
      r.nextString(),
      r.nextFloat(),
      r.nextFloat()
    )
  })

  override def next: JobId = UUID.randomUUID().toString
}
