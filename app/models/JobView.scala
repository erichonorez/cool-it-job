package models

/**
  * Created by Eric on 11/1/2016.
  */
case class JobView(
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
  latitude: Float,
  longitude: Float
)
