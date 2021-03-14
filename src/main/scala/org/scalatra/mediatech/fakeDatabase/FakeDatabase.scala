package org.scalatra.mediatech.fakeDatabase

object FakeDatabase {

  private val tableMovie = new TableMovie()
  def tableMovies():TableMovie = tableMovie
}



