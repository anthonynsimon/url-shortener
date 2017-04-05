package com.anthonynsimon.urlshortener.api.controllers

import com.anthonynsimon.urlshortener.api.domain.db.{UrlRow, UrlRepository}
import com.anthonynsimon.urlshortener.api.domain.http.{GetUrlResponse, RedirectRequest, ShortenUrlRequest, ShortenUrlResponse}
import com.anthonynsimon.urlshortener.api.services.ShortenUrlService
import com.google.inject.Inject
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging

class ShortenUrlController @Inject()(shortenService: ShortenUrlService, urlRepo: UrlRepository)
		extends Controller with Logging {


	get("/postgres/:id") { request: RedirectRequest =>
		val someUrl = urlRepo.getUrlById(shortenService.decodeId(request.id))
		response.ok(
			GetUrlResponse(someUrl.map(_.)))
	}

	post("/postgres") { request: ShortenUrlRequest =>
		val someUrl = urlRepo.createUrl(UrlRow(None, request.url))
		response.created(
			ShortenUrlResponse(s"$protocol://$host/$result"))
	}

	post("/urls") { request: ShortenUrlRequest =>
		val result = shortenService.create(request.url)

		debug(s"Shortened URL: '${request.url}' to ID ${result}")

		// TODO: make this configurable / inject from flags?
		val protocol = "http"
		val host = "localhost:8080"

		response.created(
			ShortenUrlResponse(s"$protocol://$host/$result"))
	}

	get("/urls/:id") { request: RedirectRequest =>
		shortenService.get(request.id) match {
			case Some(url) => {
				debug(s"Mapped ID: '${request.id}' to URL: '${url}'")
				response.ok(
					GetUrlResponse(url)
				)
			}
			case None => response.notFound
		}
	}
}
