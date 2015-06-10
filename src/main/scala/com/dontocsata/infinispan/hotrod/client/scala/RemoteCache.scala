package com.dontocsata.infinispan.hotrod.client.scala

import java.net.Socket
import java.io.ByteArrayOutputStream
import java.io.OutputStream

/**
 * @author ray
 */
class RemoteCache(hostname: String = "localhost", port: Int = 11222, cache: String = "") {

  val socket = new Socket(hostname, port)
  val out = socket.getOutputStream
  val in = socket.getInputStream
  var messageId = 0

  def put(key: String, value: String): String = {
    execute(Put(key, value), 0x01.toByte)
  }

  def get(key: String): String = {
    execute(Get(key), 0x03.toByte)
  }

  private def writeHeader(opCode: Byte) {
    out.write(Constants.REQUEST_MAGIC)
    out.write(VariableLength.fromInt(messageId))
    messageId += 1
    out.write(Constants.HOTROD_VERSION)
    out.write(opCode)
    out.write(VariableLength.fromInt(cache.length()))
    out.write(cache.getBytes)
    out.write(0x00) //flags
    out.write(0x01) //client intelligence
    out.write(0x00) //topology id
    out.write(0x00) //transaction type
  }

  private def readHeader() {
    var magic = in.read() //magic
    if (magic != 0xA1) {
      throw new IllegalArgumentException("Magic number is " + magic);
    }
    var msgId = VariableLength.toInt(in)
    var opCode = in.read()
    var status = in.read()
    in.read() //Topology Change Marker
  }

  private def execute(op: Operation[String, String], opCode: Byte): String = {
    writeHeader(opCode)
    op match {
      case Put(k, v) =>
        out.write(VariableLength.fromInt(k.length()))
        out.write(k.getBytes)
        out.write(0x00) //lifespan
        out.write(0x00) //idle
        out.write(VariableLength.fromInt(v.length()))
        out.write(v.getBytes)
        return read(false)
      case Get(k) =>
        out.write(VariableLength.fromInt(k.length()))
        out.write(k.getBytes)
        return read(true)
      case _ =>
        throw new UnsupportedOperationException
    }
  }

  private def read(readValue: Boolean): String = {
    readHeader()
    if (readValue) {
      var bytes = new Array[Byte](VariableLength.toInt(in))
      in.read(bytes)
      return new String(bytes)
    } else {
      return null
    }
  }
}