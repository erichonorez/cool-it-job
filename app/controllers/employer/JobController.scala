package controllers.employer

import com.google.inject.{Inject, Singleton}
import models.{JobFormData, JobPreviewForm, JobView}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import services.domain.{Job, JobIdentityProvider, JobRepository}


/**
  * Created by Eric on 10/19/2016.
  */
@Singleton
class JobController @Inject()(
                               jobRepository: JobRepository,
                               jobIdentityProvider: JobIdentityProvider,
                               val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index = Action { implicit request =>
    val jobs = jobRepository.jobsByCompanyView("123")

    Ok(views.html.employer.jobs.index(jobs))
  }

  def show(id: String) = Action { implicit request =>
    jobRepository.jobOfId("123", id) match {
      case None => NotFound(s"Job with id ${id} not found")
      case Some(job) => {
        val jobView: JobView = JobView(
          title = job.title,
          location = job.location,
          description = job.description,
          targetSalary = job.targetSalary,
          maximumSalary = job.maximumSalary,
          hasHomeWorking = job.hasHomeWorking,
          hasHealthInsurance = job.hasHealthInsurance,
          hasCar = job.hasCar,
          hasLunchVouncher = job.hasLunchVouncher,
          hasBonus = job.hasBonus,
          latitude = job.latitude,
          longitude = job.longitude
        )
        Ok(views.html.employer.jobs.show(jobView))
      }
    }
  }

  def edit(id: String) = Action { implicit request =>
    jobRepository.jobOfId("123", id) match {
      case None => NotFound(s"Job with id ${id} not found")
      case Some(job) => {
        val referer = request.headers.get("referer") match {
          case Some(r) => r
          case None => routes.JobController.index().absoluteURL()
        }

        Ok(views.html.employer.jobs.edit(id, JobFormData.of(job), referer))
      }
    }

  }

  def update(id: String) = Action { implicit request =>
    JobFormData.form.bindFromRequest fold(
      formWithErrors => {
        BadRequest(views.html.employer.jobs.create(formWithErrors, formWithErrors.data("referer")))
      },
      jobForm => {
        jobRepository.jobOfId("123", id) match {
          case None => NotFound(s"Job with id ${id} not found")
          case Some(job) => {
            jobRepository.update(Job(
              companyId = "123",
              jobId = id,
              title = jobForm.title,
              location = jobForm.location,
              description = jobForm.description,
              targetSalary = jobForm.targetSalary,
              maximumSalary = jobForm.maximumSalary,
              hasHomeWorking = jobForm.hasHomeWorking,
              hasHealthInsurance = jobForm.hasHealthInsurance,
              hasCar = jobForm.hasCar,
              hasLunchVouncher = jobForm.hasLunchVouncher,
              hasBonus = jobForm.hasBonus,
              latitude = jobForm.latitude,
              longitude = jobForm.longitude
            ))
          }
        }

        Redirect(controllers.employer.routes.JobController.index).flashing(
          "success" -> "updated"
        )
      }
    )
  }

  def remove(id: String) = Action { implicit request =>
    jobRepository.jobOfId("123", id) match {
      case None => NotFound(s"Job with id ${id} not found")
      case Some(job) => {
        val referer = request.headers.get("referer") match {
          case Some(r) => r
          case None => routes.JobController.index().absoluteURL()
        }

        Ok(views.html.employer.jobs.remove(id, referer))
      }
    }


  }

  def delete(id: String) = Action { implicit request =>
    jobRepository.jobOfId("123", id) match {
      case None => NotFound(s"Job with id ${id} not found")
      case Some(job) => {
        jobRepository.delete(job)

        Redirect(controllers.employer.routes.JobController.index).flashing(
          "success" -> "removed"
        )
      }
    }
  }

  def add = Action { implicit request =>
    val referer = request.headers.get("referer") match {
      case Some(r) => r
      case None => routes.JobController.index().absoluteURL()
    }

    Ok(views.html.employer.jobs.create(JobFormData.form, referer))
  }

  def create = Action { implicit request =>
    JobFormData.form.bindFromRequest().fold(
      formWithErros => {
        BadRequest(views.html.employer.jobs.create(formWithErros, formWithErros.data("referer")))
      },
      jobForm => {
        jobRepository.create(Job(
          companyId = "123",
          jobId = jobIdentityProvider.next,
          title = jobForm.title,
          location = jobForm.location,
          description = jobForm.description,
          targetSalary = jobForm.targetSalary,
          maximumSalary = jobForm.maximumSalary,
          hasHomeWorking = jobForm.hasHomeWorking,
          hasHealthInsurance = jobForm.hasHealthInsurance,
          hasCar = jobForm.hasCar,
          hasLunchVouncher = jobForm.hasLunchVouncher,
          hasBonus = jobForm.hasBonus,
          latitude = jobForm.latitude,
          longitude = jobForm.longitude
        ))

        Redirect(controllers.employer.routes.JobController.index).flashing(
          "success" -> "added"
        )
      }
    )
  }

  def preview = Action { implicit request =>
    JobPreviewForm.form.bindFromRequest().fold(
      formWithErrors => {
        InternalServerError
      },
      form => {
        val view: JobView = JobView(
          form.title.getOrElse(""),
          form.location.getOrElse(""),
          form.description.getOrElse(""),
          form.targetSalary.getOrElse(0.0),
          form.maximumSalary.getOrElse(0.0),
          form.hasHomeWorking.getOrElse(false),
          form.hasHealthInsurance.getOrElse(false),
          form.hasLunchVouncher.getOrElse(false),
          form.hasCar.getOrElse(false),
          form.hasBonus.getOrElse(false),
          0.0F,
          0.0F
        )
        Ok(views.html.employer.jobs.show(view))
      }
    )

  }

}
