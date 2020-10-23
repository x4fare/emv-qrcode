package com.emv.qrcode.decoder.mpm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.emv.qrcode.BiConsumer;
import com.emv.qrcode.core.model.TagLengthString;
import com.emv.qrcode.model.mpm.AdditionalDataFieldTemplate;
import com.emv.qrcode.model.mpm.MerchantAccountInformationTemplate;
import com.emv.qrcode.model.mpm.MerchantInformationLanguageTemplate;
import com.emv.qrcode.model.mpm.MerchantPresentedMode;
import com.emv.qrcode.model.mpm.UnreservedTemplate;
import com.emv.qrcode.model.mpm.constants.MerchantPresentedModeCodes;

// @formatter:off
public final class MerchantPresentedModeDecoder extends DecoderMpm<MerchantPresentedMode> {

  private static final Map<String, Entry<Class<?>, BiConsumer<MerchantPresentedMode, ?>>> mapConsumers = new HashMap<>();

  static {
    mapConsumers.put(MerchantPresentedModeCodes.ID_PAYLOAD_FORMAT_INDICATOR, consumerTagLengthValue(String.class, (merchantPresentedMode, s)-> merchantPresentedMode.setPayloadFormatIndicator(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_POINT_OF_INITIATION_METHOD, consumerTagLengthValue(String.class, (merchantPresentedMode, s)-> merchantPresentedMode.setPointOfInitiationMethod(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_MERCHANT_CATEGORY_CODE, consumerTagLengthValue(String.class, (merchantPresentedMode, s)-> merchantPresentedMode.setMerchantCategoryCode(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_TRANSACTION_CURRENCY, consumerTagLengthValue(String.class, (merchantPresentedMode, s)-> merchantPresentedMode.setTransactionCurrency(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_TRANSACTION_AMOUNT, consumerTagLengthValue(String.class, (merchantPresentedMode, s)-> merchantPresentedMode.setTransactionAmount(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_TIP_OR_CONVENIENCE_INDICATOR, consumerTagLengthValue(String.class, (merchantPresentedMode, s)-> merchantPresentedMode.setTipOrConvenienceIndicator(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_VALUE_OF_CONVENIENCE_FEE_FIXED, consumerTagLengthValue(String.class, (merchantPresentedMode, s)-> merchantPresentedMode.setValueOfConvenienceFeeFixed(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_VALUE_OF_CONVENIENCE_FEE_PERCENTAGE, consumerTagLengthValue(String.class, (merchantPresentedMode, s)-> merchantPresentedMode.setValueOfConvenienceFeePercentage(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_COUNTRY_CODE, consumerTagLengthValue(String.class, (merchantPresentedMode, s)-> merchantPresentedMode.setCountryCode(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_MERCHANT_NAME, consumerTagLengthValue(String.class, (merchantPresentedMode, s)-> merchantPresentedMode.setMerchantName(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_MERCHANT_CITY, consumerTagLengthValue(String.class, (merchantPresentedMode, s)-> merchantPresentedMode.setMerchantCity(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_POSTAL_CODE, consumerTagLengthValue(String.class, (merchantPresentedMode, s)-> merchantPresentedMode.setPostalCode(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_CRC, consumerTagLengthValue(String.class, (merchantPresentedMode, s)-> merchantPresentedMode.setCRC(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_ADDITIONAL_DATA_FIELD_TEMPLATE, consumerTagLengthValue(AdditionalDataFieldTemplate.class, (merchantPresentedMode, s)-> merchantPresentedMode.setAdditionalDataField(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_MERCHANT_INFORMATION_LANGUAGE_TEMPLATE, consumerTagLengthValue(MerchantInformationLanguageTemplate.class, (merchantPresentedMode, s)-> merchantPresentedMode.setMerchantInformationLanguage(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_MERCHANT_ACCOUNT_INFORMATION, consumerTagLengthValue(MerchantAccountInformationTemplate.class, (merchantPresentedMode, s)-> merchantPresentedMode.addMerchantAccountInformation(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_RFU_FOR_EMVCO, consumerTagLengthValue(TagLengthString.class, (merchantPresentedMode, s)-> merchantPresentedMode.addRFUforEMVCo(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_UNRESERVED_TEMPLATES, consumerTagLengthValue(UnreservedTemplate.class, (merchantPresentedMode, s)-> merchantPresentedMode.addUnreserved(s)));
    mapConsumers.put(MerchantPresentedModeCodes.ID_CRC, consumerTagLengthValue(String.class, (merchantPresentedMode, s)-> merchantPresentedMode.setCRC(s)));
  }

  public MerchantPresentedModeDecoder(final String source) {
    super(source);
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked", "java:S3740" })
  protected MerchantPresentedMode decode() {
    final MerchantPresentedMode result = new MerchantPresentedMode();

    result.setCRC("0000");

    while (iterator.hasNext()) {
      String value = iterator.next();

      final String tag = derivateId(value.substring(0, DecodeMpmIterator.ID_WORD_COUNT));

      final Entry<Class<?>, BiConsumer<MerchantPresentedMode, ?>> entry = mapConsumers.get(tag);

      final Class<?> clazz = entry.getKey();

      final BiConsumer consumer = entry.getValue();

      consumer.accept(result, DecoderMpm.decode(value, clazz));
      }

    return result;
  }

  private String derivateId(final String id) {

    if (betweenAccountInformationRange(id)) {
      return MerchantPresentedModeCodes.ID_MERCHANT_ACCOUNT_INFORMATION;
    }

    if (betweenRFUForEMVCORange(id)) {
      return MerchantPresentedModeCodes.ID_RFU_FOR_EMVCO;
    }

    if (betweenUnreservedTemplatesRange(id)) {
      return MerchantPresentedModeCodes.ID_UNRESERVED_TEMPLATES;
    }

    return id;
  }

  private boolean betweenAccountInformationRange(final String value) {
    return value.compareTo(MerchantPresentedModeCodes.ID_MERCHANT_ACCOUNT_INFORMATION_RANGE_START) >= 0
        && value.compareTo(MerchantPresentedModeCodes.ID_MERCHANT_ACCOUNT_INFORMATION_RANGE_END) <= 0;
  }

  private boolean betweenRFUForEMVCORange(final String value) {
    return value.compareTo(MerchantPresentedModeCodes.ID_RFU_FOR_EMVCO_RANGE_START) >= 0
        && value.compareTo(MerchantPresentedModeCodes.ID_RFU_FOR_EMVCO_RANGE_END) <= 0;
  }

  private boolean betweenUnreservedTemplatesRange(final String value) {
    return value.compareTo(MerchantPresentedModeCodes.ID_UNRESERVED_TEMPLATES_RANGE_START) >= 0
        && value.compareTo(MerchantPresentedModeCodes.ID_UNRESERVED_TEMPLATES_RANGE_END) <= 0;
  }

}
// @formatter:on
