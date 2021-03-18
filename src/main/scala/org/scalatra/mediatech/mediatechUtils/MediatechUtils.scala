package org.scalatra.mediatech.mediatechUtils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatra.mediatech.controllers.MovieBean
import org.scalatra.mediatech.fakeDatabase.MovieDB
import com.vitorsvieira.iso._

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

  def movieDBToMovieBean(movie: MovieDB):MovieBean = {

    val frenchReleaseBean: String = dateTimeFormat.print(movie.french_release)

    MovieBean(movie.title, movie.country,
      movie.year, movie.original_title,
      frenchReleaseBean, movie.synopsis,
      movie.genre, movie.ranking)
  }
}

