# EPIC 1 Mediatech Scalatra API 
## Prerequisite Install ##
```shell script
> sbt
> jdk8 or higher
```

## Build & Run ##

```shell script
$ cd ${whereYouPutTheProject}/mediatech
$ sbt
> jetty:start
```

For developing, use this command to refresh changes
```
~;jetty:stop;jetty:start
```

## Routes Examples ##
#### POST ####
US 1-1 : store Movie.
```
URL : http://localhost:8080/api/mediatech/US11addMovie
```

```
Body : 
{
  "title": "Tabmo",
  "country": "FRA",
  "year": 1990,
  "original_title": "Tabmo va à la plage",
  "french_release": "2015/11/12",
  "synopsis": "c'est un film",
  "genre": [
    "thriller",
    "comique"
  ],
  "ranking": 8
}
```

#### GET ####
US 1-2 : En tant qu'utilisateur, je veux lister les films préalablement enregistées, en filtrant par genre

```
URL : http://localhost:8080/api/mediatech/US12findByGenre/:genre (comique)
```

US 1-3 : En tant qu'utilisateur, je veux connaitre le nombre de films présents dans la médiathèque par année de production
```
URL : http://localhost:8080/api/mediatech/US13findByNumberYear
```

#### Custom routes ####
To store quickly some Movies to test
```
URL : http://localhost:8080/api/mediatech/feedMe
```

To get quickly all Movies
```
URL : http://localhost:8080/api/mediatech/findAll
```

