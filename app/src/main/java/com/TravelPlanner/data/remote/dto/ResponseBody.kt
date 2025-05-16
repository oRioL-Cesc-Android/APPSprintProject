package com.TravelPlanner.data.remote.dto


data class ResponseBody(
    val loc: Array<Any>,
    val message: String,
    val type: String?
)