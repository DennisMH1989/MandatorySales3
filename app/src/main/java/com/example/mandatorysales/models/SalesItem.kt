package com.example.mandatorysales.models

import java.text.DateFormat
import java.io.Serializable

data class SalesItem(val id: Int, val description: String, val price: Int, val sellerEmail: String, val sellerPhone: String, val time: Long, val pictureUrl: String)  :
    Serializable {

    constructor(description: String, price: Int, sellerEmail: String, sellerPhone: String, time: Long, pictureUrl: String) : this(-1, description, price, sellerEmail, sellerPhone, time, pictureUrl)

    override fun toString(): String {
        return "$id  $description,  $price, $sellerEmail, $sellerPhone,  ${humanDate(time)}, $pictureUrl"
    }
    fun humanDate(timeInSeconds: Long): String {
        val formatter = DateFormat.getDateInstance()
        return formatter.format(timeInSeconds * 1000L)
    }
}