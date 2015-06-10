package com.dontocsata.infinispan.hotrod.client.scala

/**
 * @author ray
 */
abstract class Operation[K,V] {
  
}

case class Put(key:String, value:String) extends Operation[String, String]
case class Get(key:String) extends Operation[String,String]