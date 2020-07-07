package com.abaddon83.libs

import java.util.Date

case object DateUtils{
  def buildDateFromString(date: String, format: Option[String]): Date ={
    val dateFormat = new java.text.SimpleDateFormat(format.getOrElse("yyyy-MM-dd"))
    dateFormat.parse(date)
  }
}
