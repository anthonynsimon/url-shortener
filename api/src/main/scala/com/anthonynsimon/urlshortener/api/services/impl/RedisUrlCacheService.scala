package com.anthonynsimon.urlshortener.api.services.impl

import com.anthonynsimon.urlshortener.api.services.UrlCacheService
import com.google.inject.Inject
import com.twitter.inject.Logging
import com.twitter.inject.annotations.Flag
import com.twitter.util.Future
import redis.clients.jedis.{Jedis => RedisClient}

class RedisUrlCacheService @Inject()(client: RedisClient,
									 @Flag("urls.keyprefix") urlKeyPrefix: String,
									 @Flag("urls.reverselookupprefix") urlRevLookupPrefix: String,
									 @Flag("urls.idcounterkey") counterKey: String)
		extends UrlCacheService with Logging {


	override def put(id: String, url: String): Future[String] = {
		Future {
			client.set(getUrlKey(id), url)
			client.set(getReverseLookupKey(url), id)
			id
		}
	}

	override def get(id: String): Future[Option[String]] = {
		Future {
			Option(client.get(getUrlKey(id)))
		}
	}

	override def reverseLookup(url: String): Future[Option[String]] = {
		Future {
			Option(client.get(getReverseLookupKey(url)))
		}
	}

	private def getUrlKey(id: String): String = String.format("%s%s", urlKeyPrefix, id.toString)

	private def getReverseLookupKey(url: String): String = String.format("%s%s", urlRevLookupPrefix, url)
}
