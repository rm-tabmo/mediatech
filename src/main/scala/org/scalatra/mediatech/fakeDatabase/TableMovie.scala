package org.scalatra.mediatech.fakeDatabase

import org.joda.time.{DateTime, DateTimeZone}

import scala.collection.mutable.ListBuffer

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class TableMovie {

  private val movies: ListBuffer[Movie] = new ListBuffer[Movie]()

  def findAll():Future[List[Movie]] = Future{movies.toList}

  def insert(movie: Movie):Future[Movie] = Future{
    movies += movie
    movie
  }

  def clear():Unit = movies.clear()

  def feedMe() : Future[List[Movie]] = Future{
    movies += Movie("film1","FRA",1990,"originalfilm",DateTime.now(),"synapse",List("comique","burlesque"),5)
    movies += Movie("film2","US",2090,"the real title",DateTime.now(),"c'est un vrai thriller",List("suspense","thriller"),8)
    movies += Movie("film3","UK",1950,"uk uk uk",DateTime.now(),"vive Bourvil",List("a leau de rose","drame"),4)
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

