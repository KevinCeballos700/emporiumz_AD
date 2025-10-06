package com.emporiumz.app.models
data class OrderRequest(val userId:Int, val address:String, val payment:String, val items:List<OrderItemRequest>)