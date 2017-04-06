package com.anthonynsimon.urlshortener.api.modules

import com.google.inject.{Provides, Singleton}
import com.twitter.finagle.Mysql
import com.twitter.finagle.mysql.{Client => MysqlClient}
import com.twitter.inject.TwitterModule

object MySqlClientModule extends TwitterModule {

	val mysqlUser = flag("mysql.user", "root", "Default mysql user")
	val mysqlPassword = flag("mysql.password", "password", "Default mysql password")
	val mysqlAddress = flag("mysql.address", "localhost:3306", "Default mysql address")
	val mysqlDatabase = flag("mysql.database", "urlshortener", "Default mysql database")

	@Singleton
	@Provides
	def providesMySqlClient(): MysqlClient = {
		Mysql.client
				.withCredentials(mysqlUser(), mysqlPassword())
				.withDatabase(mysqlDatabase())
				.newRichClient(mysqlAddress())
	}

}
