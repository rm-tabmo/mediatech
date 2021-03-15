package org.scalatra.mediatech.fakeDatabase


import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration._


class TableMovieTest extends FunSuite {

  private val tableMovieMok = new TableMovie

  test("TableMovie.findAll") {

    tableMovieMok.clear()
    Await.result[List[Movie]](tableMovieMok.feedMe(), 5 seconds)
    val resultList = Await.result[List[Movie]](tableMovieMok.findAll(), 5 seconds)
    assert(resultList.length == 3)
  }

  /**
   * Test US 1-2 filter by genre date and title.
   */
  test("TableMovie.findByGenre") {

    tableMovieMok.clear()
    Await.result[List[Movie]](tableMovieMok.feedMe(), 5 seconds)
    val resultList = Await.result[List[Movie]](tableMovieMok.findByGenre("comique"), 5 seconds)

    val resultArray = resultList.toArray
    //-- verify returned Movie order
    assert(resultArray(0).title == "film2")
    assert(resultArray(1).title == "film45")
    assert(resultArray(2).title == "filmZ")
  }

  /**
   * Test US 1-3 get film number by year
   */
  test("TableMovie.findMovieNumberByYears") {

    tableMovieMok.clear()
    Await.result[List[Movie]](tableMovieMok.feedMe(), 5 seconds)
    val resultMap = Await.result[Map[Int, Int]](tableMovieMok.findMovieNumberByYears(), 5 seconds)

    //-- verify returned number by date
    assert(resultMap.equals(Map((1990 -> 2),(2090 -> 1))))
  }
}

