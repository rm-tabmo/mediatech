package org.scalatra.mediatech.controllers

import org.scalatra.mediatech.fakeDatabase.{FakeDatabase, MovieDB}
import org.scalatra.mediatech.mediatechUtils.MediatechUtils
import scala.concurrent.Await
import scala.concurrent.duration._

class MediatechController extends BaseController {

  post("/feedMe") {
    val listResult: Seq[MovieDB] = Await.result[List[MovieDB]](FakeDatabase.tableMovies().feedMe(), 5 seconds)
    listResult.map(x => MediatechUtils.movieDBToMovieBean(x))
  }

  post("/addMovie") {

    val movieBean = parsedBody.extract[MovieBean]

    MediatechUtils.movieBeanToMovieDB(movieBean) match {
      case Left(mdb) => MediatechUtils.movieDBToMovieBean(Await.result[MovieDB](FakeDatabase.tableMovies().insert(mdb), 5 second))
      case Right(errorMessage) =>  halt(400, errorMessage)
    }
  }

  get("/findAll") {
    val listResult: Seq[MovieDB] = Await.result[List[MovieDB]](FakeDatabase.tableMovies().findAll(), 5 seconds)
    listResult.map(x => MediatechUtils.movieDBToMovieBean(x))
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
