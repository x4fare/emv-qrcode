package com.emv.qrcode.core.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class TagLengthString implements TLV<String, String> {

  private static final long serialVersionUID = -6482977134879939277L;

  private String tag;

  private String value;

  public TagLengthString() {
    super();
  }

  public TagLengthString(final String tag, final String value) {
    this.tag = tag;
    this.value = value;
  }

  @Override
  public String getTag() {
    return tag;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public Integer getLength() {
    return Optional.ofNullable(getValue()).map(String::toString).map(String::length).orElse(0);
  }

  @Override
  public String toString() {
    if (StringUtils.isBlank(value)) {
      return StringUtils.EMPTY;
    }

    return String.format("%s%02d%s", tag, value.length(), value);
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
