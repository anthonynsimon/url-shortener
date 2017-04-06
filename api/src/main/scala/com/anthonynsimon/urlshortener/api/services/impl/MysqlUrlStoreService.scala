package com.anthonynsimon.urlshortener.api.services.impl

import com.anthonynsimon.urlshortener.api.domain.sql.UrlRow
import com.anthonynsimon.urlshortener.api.services.UrlStoreService
import com.google.inject.Inject
import com.twitter.finagle.mysql._
import com.twitter.util.{Await, Future}

class MysqlUrlStoreService @Inject()(client: Client) extends UrlStoreService {

	private val schema: String =
		"""
		CREATE TABLE IF NOT EXISTS urls (
			id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
			url VARCHAR(2048) UNIQUE NOT NULL
		);
		""".stripMargin

	init()

	protected def init() = {
		println(schema)
		Await.result(client.query(schema))
	}


	protected def rowToUrl(row: Row): Option[UrlRow] = {
		val id = row("id").flatMap {
			case IntValue(v) => Some(v)
			case _ => None
		}
		val url = row("url").flatMap {
			case StringValue(v) => Some(v)
			case _ => None
		}
		(id, url) match {
			case (Some(id), Some(url)) => Option(UrlRow(id, url))
			case _ => None
		}
	}

	override def create(url: String): Future[Option[UrlRow]] = {
		client
				.prepare("INSERT INTO urls (url) VALUES (?)")
				.apply(url)
				.flatMap[Option[UrlRow]](_ => client
				.select("SELECT id, url FROM urls WHERE id = last_insert_id()")(rowToUrl)
				.map(seq => if (seq.isEmpty) None else seq.head))
	}

	override def get(id: Int): Future[Option[UrlRow]] = {
		client
				.prepare("SELECT id, url FROM urls WHERE id = ?")
				.select[Option[UrlRow]](id)(rowToUrl)
				.map(seq => if (seq.isEmpty) None else seq.head)
	}

	override def getByUrl(url: String): Future[Option[UrlRow]] = {
		client
				.prepare("SELECT id, url FROM urls WHERE url = ?")
				.select[Option[UrlRow]](url)(rowToUrl)
				.map(seq => if (seq.isEmpty) None else seq.head)
	}
}
