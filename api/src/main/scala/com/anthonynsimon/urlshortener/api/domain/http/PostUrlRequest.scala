package com.anthonynsimon.urlshortener.api.domain.http

import com.twitter.finagle.http.Request

case class PostUrlRequest(url: String, request: Request)
