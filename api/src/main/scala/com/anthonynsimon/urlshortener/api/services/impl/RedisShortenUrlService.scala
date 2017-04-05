package com.anthonynsimon.urlshortener.api.services.impl

import com.anthonynsimon.urlshortener.api.services.{EncodingCharset, EncodingRadix, ShortenUrlService}
import com.google.inject.Inject
import com.twitter.inject.Logging
import com.twitter.inject.annotations.Flag
import redis.clients.jedis.{Jedis => RedisClient}

class RedisShortenUrlService @Inject()(client: RedisClient,
									   @Flag("urls.keyprefix") urlKeyPrefix: String,
									   @Flag("urls.reverselookupprefix") urlRevLookupPrefix: String,
									   @Flag("urls.idcounterkey") counterKey: String)
		extends ShortenUrlService with Logging {

	configRedis()

	private def configRedis() = {
		// Sets the counter to the initial id if it does not exist
		client.setnx(counterKey, InitialId.toString)
	}

	override def create(url: String): String = {
		reverseLookup(url) match {
			case Some(id) => id
			case None => {
				val id = nextId()
				val encodedId = encodeId(id)
				client.set(getUrlKey(id), url)
				client.set(getReverseLookupKey(url), encodedId)
				encodedId
			}
		}

	}

	override def get(encodedId: String): Option[String] = {
		val id = decodeId(encodedId)
		Option(
			client.get(
				getUrlKey(id)))
	}

	private def reverseLookup(url: String): Option[String] = {
		Option(
			client.get(
				getReverseLookupKey(url)))
	}

	private def getUrlKey(id: Int): String = String.format("%s%s", urlKeyPrefix, id.toString)

	private def getReverseLookupKey(url: String): String = String.format("%s%s", urlRevLookupPrefix, url)

	private def nextId(): Int = {
		client.incr(counterKey).toInt
	}
}
