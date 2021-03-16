package org.scalatra.mediatech.fakeDatabase

import org.joda.time.DateTime
import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent._

case class MovieDB(
                  title: String,//-- <= 250 char
                  country: String,//-- Format ISO 3166-1 alpha-3
                  year: Int,
                  original_title: String, //-- Requis si le film est de nationalité étrangère (country != "FRA"), 250 caractères maximum
                  french_release: DateTime,//-- format YYYY/MM/DD (ex: 2016/08/23)
                  synopsis: String,//-- Liste d'au moins un élément, chaque genre étant une chaine de caractères de 50 caractères max à stocker en minuscule
                  genre: List[String],
                  ranking: Int //-- 	note entre 0 et 10, pas de 0.1
                ){
//                    url: Option[String] = None,
//                    createdAt: DateTime,
//                    deletedAt: Option[DateTime] = None) extends ShortenedNames {
//
//  def save()(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Movie] = Company.save(this)(session, cxt)
//  def destroy()(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = Company.destroy(id)(session, cxt)
}


