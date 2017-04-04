package com.anthonynsimon.urlshortener.api

import com.anthonynsimon.urlshortener.api.controllers.ShortenUrlController
import com.anthonynsimon.urlshortener.api.modules.ServicesModule
import com.google.inject.Module
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter

object APIServerMain extends APIServer

class APIServer extends HttpServer {

	override val disableAdminHttpServer = true

	override def modules: Seq[Module] = Seq(
		ServicesModule
	)

	override def configureHttp(router: HttpRouter): Unit = {
		router
				.filter[LoggingMDCFilter[Request, Response]]
				.filter[TraceIdMDCFilter[Request, Response]]
				.filter[CommonFilters]
				.add[ShortenUrlController]
	}
}