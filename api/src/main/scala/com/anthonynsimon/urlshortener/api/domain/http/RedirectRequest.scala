package com.anthonynsimon.urlshortener.api.domain.http

import com.twitter.finagle.http.Request
import com.twitter.finatra.request.RouteParam

case class RedirectRequest(@RouteParam id: String, request: Request)
