package com.emv.qrcode.core.model;

import java.io.IOException;

@SuppressWarnings("java:S1214")
public interface BERTLV<T, V> extends TLV<T, V> {

  byte[] EMPTY_BYTES = new byte[0];

  byte[] getBytes() throws IOException;

  String toHex() throws IOException;

//  default String toHex() throws IOException {
//    return Hex.encodeHexString(getBytes(), false);
//  }

}
