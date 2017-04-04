package com.anthonynsimon.urlshortener.api.services

trait ShortenUrlService {

	def create(url: String): String

	def get(id: String): Option[String]

	protected def encodeId(id: Int, charset: String, radix: Int): String = {
		var num = id
		var digits: List[Int] = List()
		while (num > 0) {
			digits = (num % radix) +: digits
			num = num / radix
		}

		digits.map(charset.charAt(_)).mkString
	}

	protected def decodeId(id: String, charset: String, radix: Int): Int = {
		id.foldLeft[Int](0)((res, ch) => {
			(radix * res) + charset.indexOf(ch)
		})
	}
}
