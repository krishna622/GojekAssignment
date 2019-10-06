package com.krishna.gojekassignment.repository.remote
data class TrendDataModel(var author : String, var name : String, var avatar : String, var url : String,
                          var description : String, var language: String, var languageColor : String, var stars : Int, var forks : Int,
                          var currentPeriodStars : Int, var builtBy : List<BuildBy>)
data class BuildBy(var href : String, var avatar : String, var username : String)