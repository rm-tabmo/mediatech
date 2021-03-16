package org.scalatra.mediatech.controllers

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatra.mediatech.fakeDatabase.{FakeDatabase, MovieDB}
import org.scalatra.mediatech.util.Utils
import scala.concurrent.Await
import scala.concurrent.duration._

class MediatechController extends BaseController {

  post("/feedMe") {
    Await.result[List[MovieDB]](FakeDatabase.tableMovies().feedMe(), 5 seconds)
  }

  post("/addMovie") {

    val movieBean = parsedBody.extract[MovieBean]

//    val title           :String = params.getOrElse("title", halt(400,   "title attribute not found."))
//    val country         :String = params.getOrElse("country", halt(400,   "country attribute not found."))
//    val year            :Int    = params.getAsOrElse[Int]("year", halt(400,   "year attribute not found."))
//    val original_title  :String = params.getOrElse("original_title", halt(400,   "original_title attribute not found."))
//    val french_release  :String = params.getOrElse("french_release", halt(400,   "french release attribute not found."))
//    val synopsis        :String = params.getOrElse("synopsis", halt(400,   "synopsis attribute not found."))
//    val genre           :Seq[String] = multiParams.getOrElse("genre", halt(400,   "genre attribute not found."))
//    val ranking         :Int    = params.getAsOrElse[Int]("ranking", halt(400,   "ranking attribute not found."))




    Await.result[MovieDB](FakeDatabase.tableMovies().insert(Utils.checkParams(movieBean)), 5 seconds)
  }

  get("/findAll") {
    Await.result[List[MovieDB]](FakeDatabase.tableMovies().findAll(), 5 seconds)
  }

  get("/hello/:name") {
    // Matches "GET /hello/foo" and "GET /hello/bar"
    // params("name") is "foo" or "bar"
    val name:String = params.getOrElse("name", halt(400))
    Test("hello", name)
  }

  post("/testBody") {
    parsedBody.extract[Test]
  }
}

case class Test(titre: String, name: String )
