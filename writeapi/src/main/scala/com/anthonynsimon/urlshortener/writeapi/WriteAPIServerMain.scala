package com.anthonynsimon.urlshortener.writeapi

import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.routing.HttpRouter

object WriteAPIServerMain extends WriteAPIServer

class WriteAPIServer extends HttpServer {

	override val disableAdminHttpServer = true

	override def configureHttp(router: HttpRouter): Unit = {

	}
}