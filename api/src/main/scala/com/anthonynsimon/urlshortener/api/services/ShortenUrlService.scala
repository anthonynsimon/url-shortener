package com.anthonynsimon.urlshortener.api.services

trait ShortenUrlService {

	def create(url: String): String

	def get(id: String): Option[String]

	def encodeId(id: Int, charset: String=EncodingCharset, radix: Int=EncodingRadix): String = {
		var num = id
		var digits: List[Int] = List()
		while (num > 0) {
			digits = (num % radix) +: digits
			num = num / radix
		}

		digits.map(charset.charAt(_)).mkString
	}

	def decodeId(id: String, charset: String=EncodingCharset, radix: Int=EncodingRadix): Int = {
		id.foldLeft[Int](0)((res, ch) => {
			(radix * res) + charset.indexOf(ch)
		})
	}
}
