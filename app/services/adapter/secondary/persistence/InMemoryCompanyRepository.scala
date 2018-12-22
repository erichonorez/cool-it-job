package services.adapter.secondary.persistence

import services.domain.Company.CompanyId
import services.domain.{Company, CompanyRepository}

/**
  * In memory implementation of [[CompanyRepository]] that always return a `Some(Tenant(id)`
  */
class InMemoryCompanyRepository extends CompanyRepository {
  override def tenantOfId(id: CompanyId): Option[Company] = Some(Company(id))
}
