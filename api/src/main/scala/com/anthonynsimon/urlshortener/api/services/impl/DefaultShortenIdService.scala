package com.anthonynsimon.urlshortener.api.services.impl

import com.anthonynsimon.urlshortener.api.services.ShortenIdService


class DefaultShortenIdService extends ShortenIdService {

	override def encodeId(id: Int, charset: String, radix: Int): String = {
		var num = id
		var digits: List[Int] = List()
		while (num > 0) {
			digits = (num % radix) +: digits
			num = num / radix
		}

		digits.map(charset.charAt(_)).mkString
	}

	override def decodeId(id: String, charset: String, radix: Int): Int = {
		id.foldLeft[Int](0)((res, ch) => {
			(radix * res) + charset.indexOf(ch)
		})
	}

}
