package com.anthonynsimon.urlshortener.api.controllers

import com.anthonynsimon.urlshortener.api.domain.http.{GetUrlResponse, RedirectRequest, ShortenUrlRequest, ShortenUrlResponse}
import com.anthonynsimon.urlshortener.api.services.ShortenUrlService
import com.google.inject.Inject
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging

class ShortenUrlController @Inject()(shortenService: ShortenUrlService)
		extends Controller with Logging {

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
