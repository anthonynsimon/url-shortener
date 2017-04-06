package com.anthonynsimon.urlshortener.api.services

import com.twitter.util.Future

trait UrlCacheService {

	def put(key: String, value: String): Future[String]

	def get(key: String): Future[Option[String]]

	def reverseLookup(value: String): Future[Option[String]]
}
