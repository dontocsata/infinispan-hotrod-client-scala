package com.dontocsata.infinispan.hotrod.client.scala

/**
 * @author ray
 */
object Utils {
  val hexArray = "0123456789ABCDEF".toCharArray();
  def bytesToHex(bytes: Array[Byte]): String = {
    val hexChars = new Array[Char](bytes.length * 2);
    var j = 0
    for (j <- 0 until bytes.length) {
      var v = bytes(j) & 0xFF;
      hexChars(j * 2) = hexArray(v >>> 4);
      hexChars(j * 2 + 1) = hexArray(v & 0x0F);
    }
    return new String(hexChars);
  }

  def byteToHex(b: Byte): String = {
    return bytesToHex(Array[Byte](b));
  }
  
  def byteToHex(i:Int): String  = {
    return Integer.toHexString(i)
  }
}