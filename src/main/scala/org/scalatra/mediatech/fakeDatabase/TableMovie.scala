package org.scalatra.mediatech.fakeDatabase

import org.joda.time.{DateTime, DateTimeZone}

import scala.collection.mutable.ListBuffer

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Class which simulate Table Movie Crud repository.
 */
class TableMovie {

  private val movies: ListBuffer[MovieDB] = new ListBuffer[MovieDB]()

  def findAll():Future[List[MovieDB]] = Future{movies.toList.sortWith((m1, m2) => m1.title < m2.title)}

  def findByGenre(genre:String):Future[List[MovieDB]] = Future{

    if (genre == null)  movies.sortWith((m1, m2) => m1.title < m2.title).toList
    else
      movies.filter(m => m.genre.contains(genre)) //--filter by genre
        .groupBy(m => m.french_release) //-- group by date
        .toList.sortBy(_._1.getMillis) //-- sort by date
        .map(couple => (couple._1, couple._2.sortBy(m => m.title))) //-- sort by title
        .flatMap((_._2))
  }

  def findMovieNumberByYears(): Future[Map[Int, Int]] = Future {
    movies.groupBy(m => m.year).mapValues(_.size)
  }


  def insert(movie: MovieDB):Future[MovieDB] = Future{
    movies += movie
    movie
  }

  def clear():Unit = movies.clear()

  def feedMe() : Future[List[MovieDB]] = Future{
    val date = DateTime.now()

    movies += new MovieDB("filmZ","UK",1990,"uk uk uk",DateTime.now(),"vive Bourvil",List("comique","drame"),4)
    movies += new MovieDB("film45","FRA",1990,"originalfilm",date,"synapse",List("comique","burlesque"),5)
    movies += new MovieDB("film2","US",2090,"the real title",date,"c'est un vrai thriller",List("comique","thriller"),8)
    movies.toList
   }
}