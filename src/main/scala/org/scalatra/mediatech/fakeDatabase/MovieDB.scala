package org.scalatra.mediatech.fakeDatabase

import org.joda.time.DateTime

class MovieDB (
  private var _title: String = "",
  private var _country: String = "",
  private var _year: Int = 0,
  private var _original_title: String = "",
  private var _french_release: DateTime = DateTime.now(),//-- change into option[DateTime]
  private var _synopsis: String = "",
  private var _genre: List[String] = List.empty,
  private var _ranking: Int  = 0
)
{

  def title = _title
  def title_= (newValue: String): Unit = {
    _title = newValue
  }

  def country = _country
  def country_= (newValue: String): Unit = {
    _country = newValue
  }

  def year = _year
  def year_= (newValue: Int): Unit = {
    _year = newValue
  }

  def original_title = _original_title
  def original_title_= (newValue: String): Unit = {
    _original_title = newValue
  }

  def french_release = _french_release
  def french_release_= (newValue: DateTime): Unit = {
    _french_release = newValue
  }

  def synopsis = _synopsis
  def synopsis_= (newValue: String): Unit = {
    _synopsis = newValue
  }

  def genre = _genre
  def genre_= (newValue: List[String]): Unit = {
    _genre = newValue
  }

  def ranking = _ranking
  def ranking_= (newValue: Int): Unit = {
    _ranking = newValue
  }
}


