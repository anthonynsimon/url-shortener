package com.anthonynsimon.urlshortener.api.services

trait ShortenUrlService {

	def create(url: String): String

	def get(id: String): Option[String]
}
