package com.anthonynsimon.urlshortener.api.services

trait ShortenIdService {

	def encodeId(id: Int, charset: String = EncodingCharset, radix: Int = EncodingRadix): String

	def decodeId(id: String, charset: String = EncodingCharset, radix: Int = EncodingRadix): Int
}
