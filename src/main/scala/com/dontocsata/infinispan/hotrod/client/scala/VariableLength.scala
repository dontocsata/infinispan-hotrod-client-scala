package com.dontocsata.infinispan.hotrod.client.scala

import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream
import java.io.InputStream

/**
 * @author ray
 */
object VariableLength {

  def fromInt(i: Int): Array[Byte] = {
    val baos = new ByteArrayOutputStream
    fromInt(i, baos)
    return baos.toByteArray()
  }

  private def fromInt(i: Int, baos: ByteArrayOutputStream) {
    if ((i & ~0x7F) == 0) { baos.write(i.toByte) }
    else {
      baos.write(((i & 0x7f) | 0x80).toByte)
      fromInt(i >>> 7, baos)
    }
  }

  def toInt(in: InputStream): Int = {
    val b = in.read.toByte
    read(in, b, 7, b & 0x7F, 1)
  }

  private def read(in: InputStream, b: Byte, shift: Int, i: Int, count: Int): Int = {
    if ((b & 0x80) == 0) i
    else {
      if (count > 5)
        throw new IllegalStateException(
          "Stream corrupted.  A variable length integer cannot be longer than 5 bytes.")

      val bb = in.read.toByte
      read(in, bb, shift + 7, i | ((bb & 0x7FL) << shift).toInt, count + 1)
    }
  }
}