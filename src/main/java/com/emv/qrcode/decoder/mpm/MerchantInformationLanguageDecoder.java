package com.emv.qrcode.decoder.mpm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.emv.qrcode.BiConsumer;
import com.emv.qrcode.core.model.TagLengthString;
import com.emv.qrcode.model.mpm.MerchantInformationLanguage;
import com.emv.qrcode.model.mpm.constants.MerchantInformationLanguageFieldCodes;

// @formatter:off
public final class MerchantInformationLanguageDecoder extends DecoderMpm<MerchantInformationLanguage> {

  private static final Map<String, Entry<Class<?>, BiConsumer<MerchantInformationLanguage, ?>>> mapConsumers = new HashMap<>();

  static {
    mapConsumers.put(MerchantInformationLanguageFieldCodes.ID_LANGUAGE_PREFERENCE, consumerTagLengthValue(String.class, (merchantInformationLanguage, s)-> merchantInformationLanguage.setLanguagePreference(s)));
    mapConsumers.put(MerchantInformationLanguageFieldCodes.ID_MERCHANT_NAME, consumerTagLengthValue(String.class, (merchantInformationLanguage, s)-> merchantInformationLanguage.setMerchantName(s)));
    mapConsumers.put(MerchantInformationLanguageFieldCodes.ID_MERCHANT_CITY, consumerTagLengthValue(String.class, (merchantInformationLanguage, s)-> merchantInformationLanguage.setMerchantCity(s)));
    mapConsumers.put(MerchantInformationLanguageFieldCodes.ID_RFU_FOR_EMVCO, consumerTagLengthValue(TagLengthString.class, (merchantInformationLanguage, s)-> merchantInformationLanguage.addRFUforEMVCo(s)));
  }

  public MerchantInformationLanguageDecoder(final String source) {
    super(source);
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked", "java:S3740" })
  protected MerchantInformationLanguage decode() {
    final MerchantInformationLanguage result = new MerchantInformationLanguage();

    while (iterator.hasNext()) {
      String value = iterator.next();

      final String tag = derivateId(value.substring(0, DecodeMpmIterator.ID_WORD_COUNT));

      final Entry<Class<?>, BiConsumer<MerchantInformationLanguage, ?>> entry = mapConsumers.get(tag);

      final Class<?> clazz = entry.getKey();

      final BiConsumer consumer = entry.getValue();

      consumer.accept(result, DecoderMpm.decode(value, clazz));
    }

    return result;
  }

  private String derivateId(final String id) {

    if (betweenRFUForEMVCORange(id)) {
      return MerchantInformationLanguageFieldCodes.ID_RFU_FOR_EMVCO;
    }

    return id;
  }

  private boolean betweenRFUForEMVCORange(final String value) {
    return value.compareTo(MerchantInformationLanguageFieldCodes.ID_RFU_FOR_EMVCO_RANGE_START) >= 0
        && value.compareTo(MerchantInformationLanguageFieldCodes.ID_RFU_FOR_EMVCO_RANGE_END) <= 0;
  }

}
// @formatter:on
