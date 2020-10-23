package com.emv.qrcode.decoder.mpm;

import com.emv.qrcode.core.model.TagLengthString;

// @formatter:off
public final class TagLengthStringDecoder extends DecoderMpm<TagLengthString> {

  public TagLengthStringDecoder(final String source) {
    super(source);
  }

  @Override
  protected TagLengthString decode() {
    final TagLengthString result = new TagLengthString();

    while (iterator.hasNext()) {
      String value = iterator.next();
      final String tag = value.substring(0, DecodeMpmIterator.ID_WORD_COUNT);
      final Integer length = Integer.valueOf(value.substring(DecodeMpmIterator.ID_WORD_COUNT, DecodeMpmIterator.ID_WORD_COUNT + DecodeMpmIterator.VALUE_LENGTH_WORD_COUNT));
      final String string = value.substring(DecodeMpmIterator.ID_WORD_COUNT + DecodeMpmIterator.VALUE_LENGTH_WORD_COUNT, DecodeMpmIterator.ID_WORD_COUNT + DecodeMpmIterator.VALUE_LENGTH_WORD_COUNT + length);
      result.setTag(tag);
      result.setValue(string);
    }

    return result;
  }

}

// @formatter:on
