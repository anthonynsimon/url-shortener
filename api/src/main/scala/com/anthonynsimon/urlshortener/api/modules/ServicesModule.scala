package com.anthonynsimon.urlshortener.api.modules

import com.anthonynsimon.urlshortener.api.services.ShortenUrlService
import com.anthonynsimon.urlshortener.api.services.impl.RedisShortenUrlService
import com.twitter.inject.TwitterModule

object ServicesModule extends TwitterModule {

	override val modules = Seq(
		RedisClientModule,
		ShortenUrlModule
	)

	override def configure(): Unit = {
		bind[ShortenUrlService].to[RedisShortenUrlService]
	}
}
