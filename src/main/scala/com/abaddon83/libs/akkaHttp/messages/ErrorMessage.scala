package com.abaddon83.libs.akkaHttp.messages

case class ErrorMessage(
                         instance: String,
                         message: String,
                         exceptionType: String,
                         errorCode: Int
                       )

object ErrorMessage{
  def apply(exception: Throwable, apiResource: String): ErrorMessage ={
    ErrorMessage(apiResource,exception.getMessage,exception.getClass().getCanonicalName(),0)
  }

  def apply(msg: String, apiResource: String): ErrorMessage ={
    ErrorMessage(apiResource,msg,"Missing",0)
  }
}