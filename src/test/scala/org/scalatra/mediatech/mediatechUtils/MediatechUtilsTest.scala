package org.scalatra.mediatech.mediatechUtils

import org.scalatest.FunSuite

class MediatechUtilsTest extends FunSuite {
  
  test("MediatechUtils.isOKTitle") {
    assert(MediatechUtils.isOKTitle("").isLeft)
    assert(MediatechUtils.isOKTitle("une longue chaine de caractere de plus de 250 caracteres." +
      "une longue chaine de caractere de plus de 250 caracteres." +
      "une longue chaine de caractere de plus de 250 caracteres." +
      "une longue chaine de caractere de plus de 250 caracteres." +
      "une longue chaine de caractere de plus de 250 caracteres.").isLeft)
    assert(MediatechUtils.isOKTitle("normalTitle").isRight)
  }

  test("MediatechUtils.isOKCountry") {
    assert(MediatechUtils.isOKCountry("").isLeft)
    assert(MediatechUtils.isOKCountry("toto").isLeft)
    assert(MediatechUtils.isOKCountry("FRA").isRight)
  }

  test("MediatechUtils.isOKOriginalTitle") {
    //-- not required if country is FRA
    assert(MediatechUtils.isOKOriginalTitle("", "FRA").isRight)
    //-- required if country != FRA
    assert(MediatechUtils.isOKOriginalTitle("", "US").isLeft)

    assert(MediatechUtils.isOKOriginalTitle("une longue chaine de caractere de plus de 250 caracteres." +
      "une longue chaine de caractere de plus de 250 caracteres." +
      "une longue chaine de caractere de plus de 250 caracteres." +
      "une longue chaine de caractere de plus de 250 caracteres." +
      "une longue chaine de caractere de plus de 250 caracteres.", "US").isLeft)
    assert(MediatechUtils.isOKOriginalTitle("normalTitle", "FRA").isRight)
    assert(MediatechUtils.isOKOriginalTitle("normalTitle", "US").isRight)
  }

  test("MediatechUtils.isOKFrenchRelease") {
    assert(MediatechUtils.isOKFrenchRelease("").isRight)
    assert(MediatechUtils.isOKFrenchRelease("bad format").isLeft)
    assert(MediatechUtils.isOKFrenchRelease("2012-12-12").isLeft)
    assert(MediatechUtils.isOKFrenchRelease("2012/12/12").isRight)
  }

  test("MediatechUtils.isOKGenre") {
    assert(MediatechUtils.isOKGenre(List.empty).isLeft)
    assert(MediatechUtils.isOKGenre(List("","une longue chaine de caractere de plus de 50 caracteres.")).isLeft)
    assert(MediatechUtils.isOKGenre(List("TOTO","TATA")).isRight)
    assert(MediatechUtils.isOKGenre(List("toto","tata")).isRight)
  }

  test("MediatechUtils.isOKRanking") {
    assert(MediatechUtils.isOKRanking(-1).isLeft)
    assert(MediatechUtils.isOKRanking(11).isLeft)
    assert(MediatechUtils.isOKRanking(6).isRight)
  }
}

