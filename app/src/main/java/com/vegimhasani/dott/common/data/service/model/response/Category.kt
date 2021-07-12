package com.vegimhasani.dott.common.data.service.model.response

data class Category(
    val icon: Icon,
    val id: String,
    val name: String,
    val pluralName: String,
    val primary: Boolean,
    val shortName: String
)