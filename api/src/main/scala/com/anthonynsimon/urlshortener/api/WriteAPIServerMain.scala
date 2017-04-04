package com.anthonynsimon.urlshortener.api

import com.anthonynsimon.urlshortener.api.controllers.ShortenUrlController
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter

object WriteAPIServerMain extends WriteAPIServer

class WriteAPIServer extends HttpServer {

	override val disableAdminHttpServer = true

	override def configureHttp(router: HttpRouter): Unit = {
		router
				.filter[LoggingMDCFilter[Request, Response]]
				.filter[TraceIdMDCFilter[Request, Response]]
				.filter[CommonFilters]
				.add[ShortenUrlController]
	}
}