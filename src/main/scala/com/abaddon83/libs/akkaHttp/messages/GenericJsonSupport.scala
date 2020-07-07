package com.abaddon83.libs.akkaHttp.messages

import java.text.SimpleDateFormat
import java.util._

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import scala.util.Try

trait GenericJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object UUIDFormat extends JsonFormat[UUID] {
    def write(uuid: UUID): JsString = JsString(uuid.toString)

    def read(json: JsValue): UUID = json match {
      case JsString(rawDate) =>
        Try {
          UUID.fromString(rawDate)
        }.toOption
          .fold(deserializationError(s"Expected UUID format, got $rawDate"))(identity)
      case error => deserializationError(s"Expected JsString, got $error")
    }
  }

  implicit val ErrorMessageFormat = new RootJsonFormat[ErrorMessage] {
    override def write(msg: ErrorMessage): JsValue = {
      JsObject(
        "code" -> JsNumber(msg.errorCode),
        "exceptionType" -> JsString(msg.exceptionType),
        "instance" -> JsString(msg.instance),
        "message" -> JsString(msg.message)
      )
    }
    override def read(json: JsValue): ErrorMessage = ???
//    def read(json: JsValue): ErrorMessage = {
//      json.asJsObject.getFields("code", "exceptionType", "instance", "message") match {
//        case Seq(JsNumber(code), JsString(exceptionType), JsString(instance), JsString(message)) =>
//          new ErrorMessage(errorCode = code.intValue, exceptionType = exceptionType, instance = instance, message = message)
//        case _ => throw DeserializationException("ErrorMessage expected")
//      }
//    }

  }

  implicit object DateFormat extends JsonFormat[Date] {
    def write(date: Date): JsString = JsString(dateToIsoString(date))

    def read(json: JsValue): Date = json match {
      case JsString(rawDate) =>
        parseIsoDateString(rawDate)
          .fold(deserializationError(s"Expected ISO Date format, got $rawDate"))(identity)
      case error => deserializationError(s"Expected JsString, got $error")
    }
  }

  private val localIsoDateFormatter = new ThreadLocal[SimpleDateFormat] {
    override def initialValue() = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  }

  private def dateToIsoString(date: Date) =
    localIsoDateFormatter.get().format(date)

  private def parseIsoDateString(date: String): Option[Date] =
    Try {
      localIsoDateFormatter.get().parse(date)
    }.toOption
}
