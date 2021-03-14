package org.scalatra.mediatech.fakeDatabase


import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class TableMovieTest extends FunSuite {

  private val tableMovieMok = new TableMovie

  test("TableMovie.findAll") {

    tableMovieMok.clear()
    Await.result[List[Movie]](tableMovieMok.feedMe(), 5 seconds)
    val resultList = Await.result[List[Movie]](tableMovieMok.findAll(), 5 seconds)
    assert(resultList.length == 3)
  }
}

