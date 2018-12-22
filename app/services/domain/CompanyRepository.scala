package services.domain

import services.domain.Job.JobId
import services.domain.Company.CompanyId

trait CompanyRepository {
  def tenantOfId(id: CompanyId): Option[Company]
}
