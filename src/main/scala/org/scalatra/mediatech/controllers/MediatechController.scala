package org.scalatra.mediatech.controllers

import org.scalatra.BadRequest
import org.scalatra.mediatech.fakeDatabase.{FakeDatabase, MovieDB}
import org.scalatra.mediatech.mediatechUtils.MediatechUtils

import scala.concurrent.Await
import scala.concurrent.duration._

class MediatechController extends BaseController {

  post("/feedMe") {
    val listResult: Seq[MovieDB] = Await.result[List[MovieDB]](FakeDatabase.tableMovies().feedMe(), 5 seconds)
    listResult.map(x => MediatechUtils.movieDBToMovieBean(x))
  }

  post("/US11addMovie") {

    parsedBody.extractOpt[MovieBean] match {
      case Some(movieBean) => MediatechUtils.movieBeanToMovieDB(movieBean) match {
                                case Right(mdb)  => MediatechUtils.movieDBToMovieBean(Await.result[MovieDB](FakeDatabase.tableMovies().insert(mdb), 5 second))
                                case Left(errorMessage) => halt(400, errorMessage)
                              }

      case None => BadRequest("Given json is unvalid.")
    }
  }

  get("/findAll") {
    val listResult: Seq[MovieDB] = Await.result[List[MovieDB]](FakeDatabase.tableMovies().findAll(), 5 seconds)
    listResult.map(x => MediatechUtils.movieDBToMovieBean(x))
  }

  get("/findByPredicate/:columnName/:value") {
    MediatechUtils.findByPredicate(params("columnName"), params("value"))
  }

  get("/US12findByGenre/:genre") {
    val listResult: Seq[MovieDB] = Await.result[List[MovieDB]](FakeDatabase.tableMovies().findByGenre(params("genre").toLowerCase()), 5 seconds)
    listResult.map(x => MediatechUtils.movieDBToMovieBean(x))
  }


  get("/US13findByNumberYear") {
   Await.result[Map[Int, Int]](FakeDatabase.tableMovies().findMovieNumberByYears(), 5 seconds)
  }
}

