package com.anthonynsimon.urlshortener.api.controllers

import com.anthonynsimon.urlshortener.api.domain.http.{GetUrlResponse, RedirectRequest, ShortenUrlRequest, ShortenUrlResponse}
import com.anthonynsimon.urlshortener.api.domain.sql.UrlRow
import com.anthonynsimon.urlshortener.api.services.{ShortenIdService, UrlCacheService, UrlStoreService}
import com.google.inject.Inject
import com.twitter.finagle.mysql.{Client => MysqlClient}
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging
import com.twitter.util.{Await, Future}

class ShortenUrlController @Inject()(urlStore: UrlStoreService, cache: UrlCacheService, shortenService: ShortenIdService)
		extends Controller with Logging {

	post("/urls") { request: ShortenUrlRequest =>

		cacheLookupByValue(request.url)
				.map(x => ifNoneThen[String](x, () => Await.result(dbLookupByValue(request.url)
					.map(y => ifNoneThen[UrlRow](y, () => Await.result(createUrl(request.url))))
						.map(z => extractAndEncodeId(z)))))
				.map(result => {
					if (result.isDefined) {
						Await.result(putInCache(result.get, request.url))
					}
					result
				})
				.map(_ match {
					case Some(encodedId) => response.created(ShortenUrlResponse(encodedId))
					case None => response.internalServerError
				})
	}

	get("/urls/:id") { request: RedirectRequest =>
		val decodedId = shortenService.decodeId(request.id)
		urlStore
				.get(decodedId)
				.map(_ match {
					case Some(value) => response.ok(GetUrlResponse(value.url))
					case None => response.notFound
				})
	}

	private def cacheLookupByValue(url: String) = {debug("cache lookup"); cache.reverseLookup(url)}

	private def putInCache(id: String, url: String) = {debug("cache put"); cache.put(id, url)}

	private def createUrl(url: String) = {debug("create url");urlStore.create(url)}

	private def extractAndEncodeId(row: Option[UrlRow]) = {
		debug("extract and encode")
		row match {
			case Some(row) => Some(shortenService.encodeId(row.id))
			case None => None
		}
	}

	private def ifNoneThen[T](thing: Option[T], otherwise: () => Option[T]): Option[T] =
		if (thing.isDefined) thing else otherwise()

	private def dbLookupByValue(url: String) =	{debug("db lookup by value"); urlStore.getByUrl(url)}

	private def dbLookupById(id: Int) = urlStore.get(id)
}
