package com.anthonynsimon.urlshortener.api

package object services {
	val EncodingCharset: String = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
	val EncodingRadix: Int = EncodingCharset.length
}
