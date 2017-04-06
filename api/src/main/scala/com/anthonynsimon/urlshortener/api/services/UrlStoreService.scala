package com.anthonynsimon.urlshortener.api.services

import com.anthonynsimon.urlshortener.api.domain.sql.UrlRow
import com.twitter.util.Future

trait UrlStoreService {

	def create(url: String): Future[Option[UrlRow]]

	def get(id: Int): Future[Option[UrlRow]]

	def getByUrl(url: String): Future[Option[UrlRow]]
}
