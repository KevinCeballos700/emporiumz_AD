package com.emporiumz.app.models
data class LoginRequest(val contact:String, val password:String)
data class LoginResponse(val user: UserDto?)