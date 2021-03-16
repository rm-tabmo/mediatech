package org.scalatra.mediatech.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatra.mediatech.controllers.MovieBean
import org.scalatra.mediatech.fakeDatabase.MovieDB

object Utils {

  def checkParams(movieBean:MovieBean) :MovieDB = {

    val dateFormat = "yyyy/MM/dd"
    val frenchReleaseDate: DateTime = DateTime.parse(movieBean.french_release, DateTimeFormat.forPattern(dateFormat))

    MovieDB(movieBean.title, movieBean.country,
      movieBean.year, movieBean.original_title,
      frenchReleaseDate, movieBean.synopsis,
      movieBean.genre, movieBean.ranking)
  }


  /**
   * Convert {@}
   * @param movie
   * @return
   */
  def movieDBToMovieBean(movie: MovieDB):MovieBean = {

    val dateFormat = "yyyy/MM/dd"
    val frenchReleaseBean: String = DateTimeFormat.forPattern(dateFormat).print(movie.french_release)

    MovieBean(movie.title, movie.country,
              movie.year, movie.original_title,
              frenchReleaseBean, movie.synopsis,
              movie.genre, movie.ranking)
  }
}



//
//def asHumanDateFromUnixDate(dateString: String): String =
//Try(DateTimeFormat.forPattern(humanDateFormat).print(DateTime.parse(unixDate))) match {
//  case Success(v) => v
//  case Failure(e) => {
//  Logger.warn("Invalid date parse in DateTimeTools.asHumanDateFromUnixDate: " + e)
//  unixDate
//}
//}


