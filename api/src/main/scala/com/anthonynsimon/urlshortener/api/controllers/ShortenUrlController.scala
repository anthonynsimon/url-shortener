package com.anthonynsimon.urlshortener.api.controllers

import com.anthonynsimon.urlshortener.api.domain.http.{PostUrlRequest, PostUrlResponse}
import com.twitter.finatra.http.Controller

class ShortenUrlController extends Controller {

	post("/urls") { request: PostUrlRequest =>
		logger.debug(s"Shortenning URL: '${request.url}'")
		PostUrlResponse("result here")
	}

}
