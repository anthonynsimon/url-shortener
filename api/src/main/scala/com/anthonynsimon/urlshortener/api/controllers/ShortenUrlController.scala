package com.anthonynsimon.urlshortener.api.controllers

import com.anthonynsimon.urlshortener.api.domain.http.{ShortenUrlRequest, ShortenUrlResponse}
import com.twitter.finatra.http.Controller

class ShortenUrlController extends Controller {

	post("/urls") { request: ShortenUrlRequest =>
		logger.debug(s"Shortenning URL: '${request.url}'")
		ShortenUrlResponse("result here")
	}

}
