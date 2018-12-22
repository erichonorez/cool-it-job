package services.domain

import services.domain.Company.CompanyId

case class Company(id: CompanyId)

object Company {

  type CompanyId = String

}
