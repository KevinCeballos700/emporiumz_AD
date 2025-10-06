package com.emporiumz.app.models
data class RegisterRequest(val fullName:String, val contact:String, val password:String)
data class RegisterResponse(val user: UserDto?)