package com.emv.qrcode.decoder.mpm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.emv.qrcode.BiConsumer;
import com.emv.qrcode.core.model.TagLengthString;
import com.emv.qrcode.model.mpm.AdditionalDataField;
import com.emv.qrcode.model.mpm.PaymentSystemSpecificTemplate;
import com.emv.qrcode.model.mpm.constants.AdditionalDataFieldCodes;

// @formatter:off
public final class AdditionalDataFieldDecoder extends DecoderMpm<AdditionalDataField> {

  private static final Map<String, Entry<Class<?>, BiConsumer<AdditionalDataField, ?>>> mapConsumers = new HashMap<>();

  static {
    mapConsumers.put(AdditionalDataFieldCodes.ID_BILL_NUMBER, consumerTagLengthValue(String.class, (additionalDataField, s) -> additionalDataField.setBillNumber(s)));
    mapConsumers.put(AdditionalDataFieldCodes.ID_MOBILE_NUMBER, consumerTagLengthValue(String.class, (additionalDataField, s) -> additionalDataField.setMobileNumber(s)));
    mapConsumers.put(AdditionalDataFieldCodes.ID_STORE_LABEL, consumerTagLengthValue(String.class, (additionalDataField, s) -> additionalDataField.setStoreLabel(s)));
    mapConsumers.put(AdditionalDataFieldCodes.ID_LOYALTY_NUMBER, consumerTagLengthValue(String.class, (additionalDataField, s) -> additionalDataField.setLoyaltyNumber(s)));
    mapConsumers.put(AdditionalDataFieldCodes.ID_REFERENCE_LABEL, consumerTagLengthValue(String.class, (additionalDataField, s) -> additionalDataField.setReferenceLabel(s)));
    mapConsumers.put(AdditionalDataFieldCodes.ID_CUSTOMER_LABEL, consumerTagLengthValue(String.class, (additionalDataField, s) -> additionalDataField.setCustomerLabel(s)));
    mapConsumers.put(AdditionalDataFieldCodes.ID_TERMINAL_LABEL, consumerTagLengthValue(String.class, (additionalDataField, s) -> additionalDataField.setTerminalLabel(s)));
    mapConsumers.put(AdditionalDataFieldCodes.ID_PURPOSE_TRANSACTION, consumerTagLengthValue(String.class, (additionalDataField, s) -> additionalDataField.setPurposeTransaction(s)));
    mapConsumers.put(AdditionalDataFieldCodes.ID_RFU_FOR_EMVCO, consumerTagLengthValue(TagLengthString.class, (additionalDataField, s) -> additionalDataField.addRFUforEMVCo(s)));
    mapConsumers.put(AdditionalDataFieldCodes.ID_PAYMENT_SYSTEM_SPECIFIC, consumerTagLengthValue(PaymentSystemSpecificTemplate.class, (additionalDataField, s) -> additionalDataField.addPaymentSystemSpecific(s)));
    mapConsumers.put(AdditionalDataFieldCodes.ID_ADDITIONAL_CONSUMER_DATA_REQUEST, consumerTagLengthValue(String.class, (additionalDataField, s) -> additionalDataField.setAdditionalConsumerDataRequest(s)));
  }

  public AdditionalDataFieldDecoder(final String source) {
    super(source);
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked", "java:S3740" })
  protected AdditionalDataField decode() {
    final AdditionalDataField result = new AdditionalDataField();

    while (iterator.hasNext()) {
      String value = iterator.next();

      final String tag = derivateId(value.substring(0, DecodeMpmIterator.ID_WORD_COUNT));

      final Entry<Class<?>, BiConsumer<AdditionalDataField, ?>> entry = mapConsumers.get(tag);

      final Class<?> clazz = entry.getKey();

      final BiConsumer consumer = entry.getValue();

      consumer.accept(result, DecoderMpm.decode(value, clazz));
    }

    return result;
  }

  private String derivateId(final String id) {

    if (betweenPaymentSystemSpecificRange(id)) {
      return AdditionalDataFieldCodes.ID_PAYMENT_SYSTEM_SPECIFIC;
    }

    if (betweenRFUForEMVCORange(id)) {
      return AdditionalDataFieldCodes.ID_RFU_FOR_EMVCO;
    }

    return id;
  }

  private boolean betweenRFUForEMVCORange(final String value) {
    return value.compareTo(AdditionalDataFieldCodes.ID_RFU_FOR_EMVCO_RANGE_START) >= 0
        && value.compareTo(AdditionalDataFieldCodes.ID_RFU_FOR_EMVCO_RANGE_END) <= 0;
  }

  private boolean betweenPaymentSystemSpecificRange(final String value) {
    return value.compareTo(AdditionalDataFieldCodes.ID_PAYMENT_SYSTEM_SPECIFIC_TEMPLATES_RANGE_START) >= 0
        && value.compareTo(AdditionalDataFieldCodes.ID_PAYMENT_SYSTEM_SPECIFIC_TEMPLATES_RANGE_END) <= 0;
  }

}
// @formatter:on
