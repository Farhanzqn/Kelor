package com.zidan.skripsikelor.data.response

data class LoginRequest(
	val username: String,
	val password: String
)

data class LoginResponse(
	val auth: Boolean,
	val token: String
)
