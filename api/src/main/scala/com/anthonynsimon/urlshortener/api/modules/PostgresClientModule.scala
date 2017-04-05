package com.anthonynsimon.urlshortener.api.modules

import com.anthonynsimon.urlshortener.api.domain.db.UrlRepository
import com.google.inject.{Provides, Singleton}
import com.twitter.inject.TwitterModule
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile

object PostgresClientModule extends TwitterModule {

	val postgresUrl = flag("postgres.url", "postgres://postgres:password@localhost:5432/urlshortener", "Default postgres URL")

	private class PostgresUrlRepository(url: String) extends UrlRepository {
		protected override val db = Database.forConfig("postgresdb")
		//		protected override val db = Database.forURL(url = url, driver = "slick.jdbc.PostgresProfile")
		protected override val driver = PostgresProfile
	}

	@Singleton
	@Provides
	def providePostgresClient(): UrlRepository = {
		val client = new PostgresUrlRepository(postgresUrl())
		client.createSchema()
		client
	}

}
