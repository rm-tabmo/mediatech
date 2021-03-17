package org.scalatra.mediatech.mediatechUtils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatra.mediatech.controllers.MovieBean
import org.scalatra.mediatech.fakeDatabase.MovieDB

object MediatechUtils {

  val dateTimeFormat = DateTimeFormat.forPattern("yyyy/MM/dd")
  val errorMessages = new StringBuilder()

  def movieBeanToMovieDB(movieBean:MovieBean) :Either[String, MovieDB] = {

      errorMessages.clear()
      val destMovieDB = new MovieDB()

    //title: String,//-- <= 250 char oui
    isOKTitle(movieBean.title) match {
      case Right(title) => destMovieDB.title = title
      case Left(error) => errorMessages.append(error)
    }


    //country: String,//-- Format ISO 3166-1 alpha-3 oui
    //-- TODO

    //year: Int, oui
    destMovieDB.year = movieBean.year

    //original_title: String, //-- Requis si le film est de nationalité étrangère (country != "FRA"), 250 caractères maximum
    isOKOriginalTitle(movieBean.original_title, movieBean.country) match {
      case Right(oriTitle) => destMovieDB.original_title = oriTitle
      case Left(error) => errorMessages.append(error)
    }

    //french_release: DateTime,//-- format YYYY/MM/DD (ex: 2016/08/23)
    isOKFrenchRelease(movieBean.french_release) match {
      case Right(date) => destMovieDB.french_release = date
      case Left(error) => errorMessages.append(error)
    }

    //synopsis: String
    destMovieDB.synopsis = movieBean.synopsis

    // -- Liste d'au moins un élément, chaque genre étant une chaine de caractères de 50 caractères max à stocker en minuscule
    //genre: List[String],
    isOKGenre(movieBean.genre) match {
      case Right(genre) => destMovieDB.genre = genre
      case Left(error) => errorMessages.append(error)
    }
    //ranking: String //-- 	note entre 0 et 10, pas de 0.1
    isOKRanking(movieBean.ranking) match {
      case Right(rank) => destMovieDB.ranking = rank
      case Left(error) => errorMessages.append(error)
    }

    if (errorMessages.isEmpty)
      Right(destMovieDB)
    else
      Left(errorMessages.insert(0, "Misformatted Movie datas : \n").toString())
  }

  def isOKTitle(title :String) :Either[String, String] = {
    if (title.isEmpty)
      Left("Title should not be empty.")
    else if (title.length > 250)
      Left("Title is too long.")
    else
      Right(title)
  }


  //  def isOKCountry(country :String) :Either[String, String] = {
  //    try {
  //      val frenchReleaseDate = DateTime.parse(dateStr, dateTimeFormat)
  //      Left(frenchReleaseDate)
  //    } catch {
  //      case _: Throwable => Right("Wrong Date format. Expected : yyyy/MM/dd")
  //    }
  //  }


  def isOKOriginalTitle(originalTitle :String, country:String) :Either[String, String] = {
    if (originalTitle.length > 250)
      Left("original_title : is too long.")
    else if (originalTitle.isEmpty && country != "FRA")
      Left("original_title : You should specify a original_title when the country is not FRA.")
    else
      Right(originalTitle)
  }


  def isOKFrenchRelease(dateStr :String) :Either[String, DateTime] = {
    if(dateStr.isEmpty)
      Right(null)//-- TODO option.NONE
    else
      try {
        val frenchReleaseDate = DateTime.parse(dateStr, dateTimeFormat)
        Right(frenchReleaseDate)
      } catch {
        case _: Throwable => Left("french_release : Wrong Date format. Expected : yyyy/MM/dd")
      }
  }


  def isOKGenre(genre :List[String]) :Either[String, List[String]] = {
    if (genre.isEmpty)
      Left("genre : You should specify at least one genre.")
    else if (!genre.filter(s => s.length > 50).isEmpty)
      Left("genre : Specified genre should have a length lesser than 50 characters.")
    else
      Right(genre.map(s => s.toLowerCase()))
  }

  def isOKRanking(ranking :Int) :Either[String, Int] = {
   if (ranking < 0 || ranking > 10)
     Left("ranking : Should be between [0; 10].")
    else
     Right(ranking)
  }

  def movieDBToMovieBean(movie: MovieDB):MovieBean = {

    val frenchReleaseBean: String = dateTimeFormat.print(movie.french_release)

    MovieBean(movie.title, movie.country,
      movie.year, movie.original_title,
      frenchReleaseBean, movie.synopsis,
      movie.genre, movie.ranking)
  }

}
