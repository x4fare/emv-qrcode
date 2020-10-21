package com.emv.qrcode.model.mpm;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.emv.qrcode.core.model.TLV;

public class PaymentSystemSpecificTemplate implements TLV<String, PaymentSystemSpecific> {

  private static final long serialVersionUID = -1445641777082739037L;

  private String tag;

  private PaymentSystemSpecific value;

  @Override
  public String getTag() {
    return tag;
  }

  @Override
  public PaymentSystemSpecific getValue() {
    return value;
  }

  @Override
  public Integer getLength() {
    return Optional.ofNullable(getValue()).map(PaymentSystemSpecific::toString).map(String::length).orElse(0);
  }

  @Override
  public String toString() {
    if (Objects.isNull(value)) {
      return StringUtils.EMPTY;
    }

    final String string = value.toString();

    if (StringUtils.isBlank(string)) {
      return StringUtils.EMPTY;
    }

    return String.format("%s%02d%s", tag, string.length(), string);
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setValue(PaymentSystemSpecific value) {
    this.value = value;
  }
}
