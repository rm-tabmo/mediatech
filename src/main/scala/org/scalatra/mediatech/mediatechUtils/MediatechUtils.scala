package org.scalatra.mediatech.mediatechUtils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatra.mediatech.controllers.MovieBean
import org.scalatra.mediatech.fakeDatabase.MovieDB

object MediatechUtils {

  def checkParams(movieBean:MovieBean) :MovieDB = {

    val dateFormat = "yyyy/MM/dd"
    val frenchReleaseDate: DateTime = DateTime.parse(movieBean.french_release, DateTimeFormat.forPattern(dateFormat))

    MovieDB(movieBean.title, movieBean.country,
      movieBean.year, movieBean.original_title,
      frenchReleaseDate, movieBean.synopsis,
      movieBean.genre, movieBean.ranking)
  }
    def movieBeanToMovieDB(movieBean:MovieBean) :Either[MovieDB, String] = {

    val dateFormat = "yyyy/MM/dd"
    val frenchReleaseDate: DateTime = DateTime.parse(movieBean.french_release, DateTimeFormat.forPattern(dateFormat))


    Left(MovieDB(movieBean.title, movieBean.country,
      movieBean.year, movieBean.original_title,
      frenchReleaseDate, movieBean.synopsis,
      movieBean.genre, movieBean.ranking))
  }

  def movieDBToMovieBean(movie: MovieDB):MovieBean = {

    val dateFormat = "yyyy/MM/dd"
    val frenchReleaseBean: String = DateTimeFormat.forPattern(dateFormat).print(movie.french_release)

    MovieBean(movie.title, movie.country,
      movie.year, movie.original_title,
      frenchReleaseBean, movie.synopsis,
      movie.genre, movie.ranking)
  }

}
