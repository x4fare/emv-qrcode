package com.emv.qrcode.core.model;

import java.io.Serializable;

public interface TLV<T, V> extends Serializable {

  T getTag();

  V getValue();

  Integer getLength();

//  default Integer getLength() {
//    return Optional.ofNullable(getValue()).map(V::toString).map(String::length).orElse(0);
//  }

}
