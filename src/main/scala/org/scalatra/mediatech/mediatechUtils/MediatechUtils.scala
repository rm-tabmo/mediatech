package org.scalatra.mediatech.mediatechUtils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatra.mediatech.controllers.MovieBean
import org.scalatra.mediatech.fakeDatabase.MovieDB

object MediatechUtils {

  val dateTimeFormat = DateTimeFormat.forPattern("yyyy/MM/dd")
  val errorMessages = new StringBuilder("Bad given Movie datas : ")

  def movieBeanToMovieDB(movieBean:MovieBean) :Either[MovieDB, String] = {

      errorMessages.clear()

      val destMovieDB = new MovieDB()

      var frenchReleaseDate: DateTime = null

      //title: String,//-- <= 250 char oui

      //country: String,//-- Format ISO 3166-1 alpha-3 oui

      //year: Int, oui

      //original_title: String, //-- Requis si le film est de nationalité étrangère (country != "FRA"), 250 caractères maximum

      //french_release: DateTime,//-- format YYYY/MM/DD (ex: 2016/08/23)
      isOKDate(movieBean.french_release) match {
        case Left(date) => destMovieDB.french_release = date
        case Right(error) => errorMessages.append(error)
      }


      Left(destMovieDB)
  }

  def isOKTitle(title :String) :Either[String, String] = {
    if (title.isEmpty)
      Right("Title should not be empty.")
    else if (title.length > 250)
      Right("Title is too long.")
    else
      Left(title)
  }

//  def isOKCountry(country :String) :Either[String, String] = {
//    try {
//      val frenchReleaseDate = DateTime.parse(dateStr, dateTimeFormat)
//      Left(frenchReleaseDate)
//    } catch {
//      case _: Throwable => Right("Wrong Date format. Expected : yyyy/MM/dd")
//    }
//  }

  def isOKDate(dateStr :String) :Either[DateTime, String] = {
    try {
      val frenchReleaseDate = DateTime.parse(dateStr, dateTimeFormat)
      Left(frenchReleaseDate)
    } catch {
      case _: Throwable => Right("Wrong Date format. Expected : yyyy/MM/dd")
    }
  }






  //title: String,//-- <= 250 char oui
  //country: String,//-- Format ISO 3166-1 alpha-3 oui
  //year: Int, oui
  //original_title: String, //-- Requis si le film est de nationalité étrangère (country != "FRA"), 250 caractères maximum
  //french_release: DateTime,//-- format YYYY/MM/DD (ex: 2016/08/23)
  //synopsis: String,//-- Liste d'au moins un élément, chaque genre étant une chaine de caractères de 50 caractères max à stocker en minuscule
  //genre: List[String],
  //ranking: String //-- 	note entre 0 et 10, pas de 0.1
  //

  def movieDBToMovieBean(movie: MovieDB):MovieBean = {

    val frenchReleaseBean: String = dateTimeFormat.print(movie.french_release)

    MovieBean(movie.title, movie.country,
      movie.year, movie.original_title,
      frenchReleaseBean, movie.synopsis,
      movie.genre, movie.ranking)
  }

}
