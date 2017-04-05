package com.anthonynsimon.urlshortener.api.domain.db


import com.anthonynsimon.urlshortener.api.domain.Url
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.JdbcProfile

import scala.concurrent.Future


trait UrlRepository {
	protected val db: Database
	protected val driver: JdbcProfile

	import driver.api._

	protected class UrlTable(tag: Tag) extends Table[UrlRow](tag, "urls") {
		def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)

		def url = column[String]("url")

		def * = (id, url) <> (UrlRow.tupled, UrlRow.unapply)
	}

	protected val urls = TableQuery[UrlTable]

	def createSchema(): Unit = {
		db.run(urls.schema.create)
	}

	def getUrlById(id: Int): Future[Option[Url]] = {
		db.run(urls.filter(_.id === id).result)
	}

	def createUrl(url: UrlRow): Future[Any] = {
		db.run(urls += url)
	}
}
