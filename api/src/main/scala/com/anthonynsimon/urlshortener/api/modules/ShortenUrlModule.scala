package com.anthonynsimon.urlshortener.api.modules

import com.twitter.inject.TwitterModule

object ShortenUrlModule extends TwitterModule {

	flag[String]("urls.keyprefix", "url:", "Default URL key prefix")
	flag[String]("urls.reverselookupprefix", "revurl:", "Default URL reverse lookup key prefix")
	flag("urls.idcounterkey", "idcounter", "Default sequential ID counter database key")

}
