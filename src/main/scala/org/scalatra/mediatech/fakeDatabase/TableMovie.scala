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

    movies += MovieDB("filmZ","UK",1990,"uk uk uk",DateTime.now(),"vive Bourvil",List("comique","drame"),4)
    movies += MovieDB("film45","FRA",1990,"originalfilm",date,"synapse",List("comique","burlesque"),5)
    movies += MovieDB("film2","US",2090,"the real title",date,"c'est un vrai thriller",List("comique","thriller"),8)
    movies.toList
   }
}
//title: String,//-- <= 250 char
//country: String,//-- Format ISO 3166-1 alpha-3
//year: Int,
//original_title: String, //-- Requis si le film est de nationalité étrangère (country != "FRA"), 250 caractères maximum
//french_release: DateTime,//-- format YYYY/MM/DD (ex: 2016/08/23)
//synopsis: String,//-- Liste d'au moins un élément, chaque genre étant une chaine de caractères de 50 caractères max à stocker en minuscule
//genre: List[String],
//ranking: String //-- 	note entre 0 et 10, pas de 0.1
//
//object DateTimeTools extends CurrentTaxYear {
//
//  //Timezone causing problem on dev server
//  val defaultTZ = DateTimeZone.forID("Europe/London")
//  val unixDateFormat = "yyyy-MM-dd"
//  val unixDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss"
//  val humanDateFormat = "dd MMMMM yyyy"
//
//  //Returns for example 1516 in March 2016
//  def previousAndCurrentTaxYear = previousAndCurrentTaxYearFromGivenYear(current.currentYear)
//
//  def previousAndCurrentTaxYearFromGivenYear(year: Int) = {
//    def y = year
//
//    (y - 1).toString.takeRight(2) + (y).toString.takeRight(2)
//  }
//
//  private def formatter(pattern: String): DateTimeFormatter = DateTimeFormat.forPattern(pattern).withZone(defaultTZ)
//
//  def short(dateTime: LocalDate) = formatter("dd/MM/yyy").print(dateTime)
//
//  def asHumanDateFromUnixDate(unixDate: String): String =
//    Try(DateTimeFormat.forPattern(humanDateFormat).print(DateTime.parse(unixDate))) match {
//      case Success(v) => v
//      case Failure(e) => {
//        Logger.warn("Invalid date parse in DateTimeTools.asHumanDateFromUnixDate: " + e)
//        unixDate
//      }
//    }
//
//  def toPaymentDate(dateTime: JavaLDT): LocalDate =
//    new LocalDate(dateTime.getYear, dateTime.getMonthValue, dateTime.getDayOfMonth)
//
//  override def now: () => DateTime = DateTime.now
//}
//
//@Singleton
//class DateTimeTools @Inject()() {
//
//  def showSendTaxReturnByPost = {
//
//    val start = new DateTime(s"${DateTime.now().getYear}-11-01T00:00:00Z")
//    val end = new DateTime(s"${DateTime.now().getYear + 1}-01-31T23:59:59Z")
//    !DateTime.now().isAfter(start) && DateTime.now().isBefore(end)
//  }
//}

