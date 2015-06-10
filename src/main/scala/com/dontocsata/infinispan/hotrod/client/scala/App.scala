package com.dontocsata.infinispan.hotrod.client.scala

import java.util.Arrays

/**
 * Hello world!
 *
 */
object App extends App {
	val cache = new RemoteCache()
	cache.put("test_key", "test_value2")
	println(cache.get("test_key"))
}
