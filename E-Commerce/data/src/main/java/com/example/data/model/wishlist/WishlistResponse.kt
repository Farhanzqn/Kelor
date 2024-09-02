package com.example.data.model.wishlist

import com.example.data.model.BaseResponse
import com.example.data.model.product.ProductDto
import com.google.gson.annotations.SerializedName

data class WishlistResponseDto(
	@field:SerializedName("status")
	val status: String? = null,
	@field:SerializedName("count")
	val count:Int
):BaseResponse<String?>()
