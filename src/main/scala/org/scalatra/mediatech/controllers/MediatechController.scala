package org.scalatra.mediatech.controllers

import org.scalatra.mediatech.fakeDatabase.{FakeDatabase, Movie}

import scala.concurrent.Await
import scala.concurrent.duration._

class MediatechController extends BaseController {

  post("/feedMe") {
    Await.result[List[Movie]](FakeDatabase.tableMovies().feedMe(), 5 seconds)
  }

  get("/findAll") {
    Await.result[List[Movie]](FakeDatabase.tableMovies().findAll(), 5 seconds)
  }

  get("/hello/:name") {
    // Matches "GET /hello/foo" and "GET /hello/bar"
    // params("name") is "foo" or "bar"
    val name:String = params.getOrElse("name", halt(400))
    Test("hello", name)
  }

}

case class Test(titre: String, name: String )
