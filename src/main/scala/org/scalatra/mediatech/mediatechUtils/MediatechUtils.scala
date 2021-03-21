package org.scalatra.mediatech.mediatechUtils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatra.mediatech.controllers.MovieBean
import org.scalatra.mediatech.fakeDatabase.{FakeDatabase, MovieDB}
import com.vitorsvieira.iso._



import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util

object MediatechUtils {

  val dateTimeFormat = DateTimeFormat.forPattern("yyyy/MM/dd")
  val errorMessages = new StringBuilder()
  val lineSep = "\n"

  val iso3 = List("ABW","AFG", "AGO", "AIA", "ALA" , "ALB",  "AND",  "ARE",  "ARG", "ARM" , "ASM", "ATA", "ATF", "ATG",
    "AUS","AUT", "AZE", "BDI", "BEL", "BEN", "BES", "BFA", "BGD", "BGR", "BHR", "BHS", "BIH", "BLM", "BLR",  "BLZ", "BMU",
    "BOL", "BRA","BRB", "BRN", "BTN", "BVT", "BWA", "CAF", "CAN",  "CCK", "CHE", "CHL", "CHN", "CIV","CMR",  "COD", "COG",
    "COK",  "COL", "COM","CPV", "CRI", "CUB", "CUW ",  "CXR","CYM", "CYP","CZE", "DEU", "DJI","DMA", "DNK", "DOM", "DZA",
    "ECU", "EGY", "ERI", "ESH", "ESP", "EST", "ETH",  "FIN", "FJI", "FLK ", "FRA", "FRO ", "FSM  ", "GAB",  "GBR", "GEO",
    "GGY", "GHA", "GIB", "GIN", "GLP", "GMB", "GNB","GNQ", "GRC", "GRD",  "GRL",  "GTM",  "GUF", "GUM", "GUY",
    "HKG", "HMD", "HND", "HRV", "HTI ", "HUN", "IDN", "IMN", "IND", "IOT", "IRL", "IRN", "IRQ",
    "ISL", "ISR", "ITA ", "JAM",  "JEY", "JOR", "JPN", "KAZ", "KEN", "KGZ", "KHM", "KIR", "KNA",
    "KOR", "KWT", "LAO", "LBN", "LBR", "LBY", "LCA", "LIE", "LKA", "LSO", "LTU", "LUX", "LVA", "MAC",
    "MAF", "MAR", "MCO", "MDA", "MDG", "MDV", "MEX", "MHL", "MKD", "MLI", "MLT", "MMR", "MNE", "MNG","MNP",
    "MOZ", "MRT", "MSR", "MTQ", "MUS", "MWI", "MYS", "MYT","NAM", "NCL","NER", "NFK","NGA", "NIC","NIU",
    "NLD", "NOR", "NPL", "NRU","NZL", "OMN", "PAK", "PAN","PCN", "PER", "PHL", "PLW", "PNG", "POL", "PRI",
    "PRK", "PRT", "PRY", "PSE", "PYF", "QAT", "REU", "ROU",  "RUS", "RWA", "SAU", "SDN", "SEN", "SGP", "SGS", "SHN",
    "SJM", "SLB", "SLE", "SLV", "SMR", "SOM", "SPM", "SRB", "SSD", "STP", "SUR", "SVK", "SVN", "SWE", "SWZ","SXM",
    "SYC", "SYR", "TCA", "TCD", "TGO", "THA", "TJK",  "TKL", "TKM", "TLS","TON", "TTO", "TUN", "TUR", "TUV",
    "TWN","TZA", "UGA", "UKR", "UMI", "URY", "USA",  "UZB", "VAT","VCT", "VEN", "VGB", "VIR", "VNM", "VUT", "WLF",
    "WSM", "YEM", "ZAF", "ZMB", "ZWE")



  /**
   * Convert a MovieBean object case class use to get params and JSon parsing
   * into Case class Movie Database object adapted for fake crud repository.
   *
   * Moreover check all params from MovieBean and return Right(MovieDB) if
   * all params are efficient else return Left(errorMessage).
   */
  def movieBeanToMovieDB(movieBean:MovieBean) :Either[String, MovieDB] = {

      errorMessages.clear()
      val destMovieDB = new MovieDB()

    //title: String,//-- <= 250 char oui
    isOKTitle(movieBean.title) match {
      case Right(title) => destMovieDB.title = title
      case Left(error) => errorMessages.append(error+lineSep)
    }
    //country: String,//-- Format ISO 3166-1 alpha-3 oui
    isOKCountry(movieBean.country) match {
      case Right(country) => destMovieDB.country = country
      case Left(error) => errorMessages.append(error+lineSep)
    }

    //year: Int, oui
    destMovieDB.year = movieBean.year

    //original_title: String, //-- Requis si le film est de nationalité étrangère (country != "FRA"), 250 caractères maximum
    isOKOriginalTitle(movieBean.original_title, movieBean.country) match {
      case Right(oriTitle) => destMovieDB.original_title = oriTitle
      case Left(error) => errorMessages.append(error+lineSep)
    }

    //french_release: DateTime,//-- format YYYY/MM/DD (ex: 2016/08/23)
    isOKFrenchRelease(movieBean.french_release) match {
      case Right(date) => destMovieDB.french_release = date
      case Left(error) => errorMessages.append(error+lineSep)
    }

    //synopsis: String
    destMovieDB.synopsis = movieBean.synopsis

    // -- Liste d'au moins un élément, chaque genre étant une chaine de caractères de 50 caractères max à stocker en minuscule
    //genre: List[String],
    isOKGenre(movieBean.genre) match {
      case Right(genre) => destMovieDB.genre = genre
      case Left(error) => errorMessages.append(error+lineSep)
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

  /**
   * Returns Either where Right contain title in accordance with specifications
   * or Left which contains error message.
   */
  def isOKTitle(title :String) :Either[String, String] = {
    if (title.isEmpty)
      Left("Title should not be empty.")
    else if (title.length > 250)
      Left("Title is too long.")
    else
      Right(title)
  }

  /**
   * Returns Either where Right contain country in accordance with specifications
   * or Left which contains error message.
   */
  def isOKCountry(country :String) :Either[String, String] = {
    if (iso3.contains(country))
      Right(country)
    else
      Left("country : misformatted country it should be ISO 3166-1 alpha-3.")
//    ISOCountry.from(country) match {
//      case Some(iso) => Right(iso.alpha3Code)
//      case None => Left("country : misformatted country it should be ISO 3166-1 alpha-3.")
//    }
  }

  /**
   * Returns Either where Right contain original_title in accordance with specifications
   * or Left which contains error message.
   */
  def isOKOriginalTitle(originalTitle :String, country:String) :Either[String, String] = {
    if (originalTitle.length > 250)
      Left("original_title : is too long.")
    else if (originalTitle.isEmpty && country != "FRA")
      Left("original_title : You should specify a original_title when the country is not FRA.")
    else
      Right(originalTitle)
  }

  /**
   * Returns Either where Right french_release title in accordance with specifications
   * or Left which contains error message.
   */
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

  /**
   * Returns Either where Right contain genre in accordance with specifications
   * or Left which contains error message.
   */
  def isOKGenre(genre :List[String]) :Either[String, List[String]] = {
    if (genre.isEmpty)
      Left("genre : You should specify at least one genre.")
    else if (!genre.filter(s => s.length > 50).isEmpty)
      Left("genre : Specified genre should have a length lesser than 50 characters.")
    else
      Right(genre.map(s => s.toLowerCase()))
  }

  /**
   * Returns Either where Right contain ranking in accordance with specifications
   * or Left which contains error message.
   */
  def isOKRanking(ranking :Int) :Either[String, Int] = {
   if (ranking < 0 || ranking > 10)
     Left("ranking : Should be between [0; 10].")
    else
     Right(ranking)
  }

  /**
   * Filter in database from column Name and given value.
   */
  @throws(classOf[IllegalArgumentException])
  @throws(classOf[NumberFormatException])
  def findByPredicate(columName: String, value :String) :List[MovieBean] = {
    val filterPredic = getPredicate(columName, value)
    val movies = Await.result[List[MovieDB]](FakeDatabase.tableMovies().findByPredicate(filterPredic), 5 seconds)
    movies.map(m => movieDBToMovieBean(m))
  }


  /**
   * Return a predicate to filter Movie in db in function of column name
   * and given value.
   */
    @throws(classOf[IllegalArgumentException])
    @throws(classOf[NumberFormatException])
  def getPredicate(columnName: String, value: String): (MovieDB => Boolean) = {

    columnName match {
      case "title" => {
        movie => movie.title.equals(value)
      }
      case "country" => {
        movie => movie.country.equals(value)
      }
      case "year" => {
        val yearVal :Int = Integer.parseInt(value)
        p => p.year.equals(yearVal)
      }
      case "original_title" => {
        p => p.original_title.equals(value)
      }
      case "french_release" => {
        p => p.french_release.equals(DateTime.parse(value, dateTimeFormat))
      }
      case "synopsis" => {
        p => p.synopsis.equals(value)
      }
      case "genre" => {
        p => p.genre.contains(value.toLowerCase)
      }
      case "ranking" => {
        p => p.ranking.equals(Integer.parseInt(value))
      }
      case _ => throw new IllegalArgumentException(s"Unknow columnName named : $columnName. " +
        s"Expected values [title, country, year, original_title, synopsis, genre or ranking]")
    }
  }

  /**
   * Convert MovieDB database model into MovieBean case class object
   * use for JSon parsing
   */
  def movieDBToMovieBean(movie: MovieDB):MovieBean = {

    val frenchReleaseBean: String = dateTimeFormat.print(movie.french_release)

    MovieBean(movie.title, movie.country,
      movie.year, movie.original_title,
      frenchReleaseBean, movie.synopsis,
      movie.genre, movie.ranking)
  }
}

