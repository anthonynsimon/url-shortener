package com.anthonynsimon.urlshortener.api.domain.http

import com.twitter.finagle.http.Request

case class ShortenUrlRequest(url: String, request: Request)
