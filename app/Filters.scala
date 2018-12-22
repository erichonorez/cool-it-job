import javax.inject._

import play.api.http.HttpFilters
import play.filters.csrf.CSRFFilter

@Singleton
class Filters @Inject() (csrfFilter: CSRFFilter) extends HttpFilters {
  def filters = Seq(csrfFilter)
}