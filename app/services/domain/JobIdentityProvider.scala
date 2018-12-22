package services.domain

import services.domain.Job.JobId

trait JobIdentityProvider {

  def next: JobId

}
