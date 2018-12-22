package services.domain

import services.domain.Job.JobId
import services.domain.Company.CompanyId

import scala.concurrent.Future

trait JobRepository {
  def create(job: Job): JobId
  def update(job: Job): Unit
  def delete(job: Job): Unit

  // queries
  def jobOfId(tenantId: CompanyId, jobId: JobId): Option[Job]

  // specific views
  def jobsByCompanyView(id: CompanyId): Seq[Job.views.JobsByCompanyView.Job]
  def jobLocationsView(): Seq[Job.views.JobLocationsView.Job]
}
