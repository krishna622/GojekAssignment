package com.krishna.gojekassignment.ui.model

class TrendDataItem(
    var author: String?,
    var name: String?,
    var avatar: String?,
    var url: String?,
    var description: String?,
    var stars: Int?,
    var forks: Int?,
    var currentPeriodStars: Int?,
    var language : String?,
    var  languageColor : String?
){
    var isExpend : Boolean = false
}