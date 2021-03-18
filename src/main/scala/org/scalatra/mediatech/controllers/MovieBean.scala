package org.scalatra.mediatech.controllers

case class MovieBean(
                  title: String,//-- <= 250 char
                  country: String,//-- Format ISO 3166-1 alpha-3
                  year: Int,
                  original_title: String, //-- Requis si le film est de nationalité étrangère (country != "FRA"), 250 caractères maximum
                  french_release: String,//-- format YYYY/MM/DD (ex: 2016/08/23)
                  synopsis: String,//-- Liste d'au moins un élément, chaque genre étant une chaine de caractères de 50 caractères max à stocker en minuscule
                  genre: List[String],
                  ranking: Int //-- 	note entre 0 et 10, pas de 0.1
                )


