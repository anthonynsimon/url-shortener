package com.anthonynsimon.urlshortener.api.modules

import com.anthonynsimon.urlshortener.api.services.impl.{DefaultShortenIdService, MysqlUrlStoreService, RedisUrlCacheService}
import com.anthonynsimon.urlshortener.api.services.{ShortenIdService, UrlCacheService, UrlStoreService}
import com.twitter.inject.TwitterModule

object ServicesModule extends TwitterModule {

	override val modules = Seq(
		RedisClientModule,
		ShortenUrlModule,
		MySqlClientModule
	)

	override def configure(): Unit = {
		bind[UrlStoreService].to[MysqlUrlStoreService]
		bind[ShortenIdService].to[DefaultShortenIdService]
		bind[UrlCacheService].to[RedisUrlCacheService]
	}
}
