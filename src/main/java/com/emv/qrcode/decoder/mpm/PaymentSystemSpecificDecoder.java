package com.emv.qrcode.decoder.mpm;

import com.emv.qrcode.BiConsumer;
import com.emv.qrcode.core.model.TagLengthString;
import com.emv.qrcode.model.mpm.PaymentSystemSpecific;
import com.emv.qrcode.model.mpm.constants.MerchantAccountInformationFieldCodes;
import com.emv.qrcode.model.mpm.constants.PaymentSystemSpecificFieldCodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

// @formatter:off
public final class PaymentSystemSpecificDecoder extends DecoderMpm<PaymentSystemSpecific> {

  private static final Map<String, Entry<Class<?>, BiConsumer<PaymentSystemSpecific, ?>>> mapConsumers = new HashMap<>();

  static {
    mapConsumers.put(MerchantAccountInformationFieldCodes.ID_GLOBALLY_UNIQUE_IDENTIFIER, consumerTagLengthValue(String.class, (paymentSystemSpecific, s)-> paymentSystemSpecific.setGloballyUniqueIdentifier(s)));
    mapConsumers.put(MerchantAccountInformationFieldCodes.ID_PAYMENT_NETWORK_SPECIFIC, consumerTagLengthValue(TagLengthString.class, (paymentSystemSpecific, s)-> paymentSystemSpecific.addPaymentSystemSpecific(s)));
  }

  public PaymentSystemSpecificDecoder(final String source) {
    super(source);
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked", "java:S3740" })
  protected PaymentSystemSpecific decode() {
    final PaymentSystemSpecific result = new PaymentSystemSpecific();

    while (iterator.hasNext()) {
      String value = iterator.next();

      final String tag = derivateId(value.substring(0, DecodeMpmIterator.ID_WORD_COUNT));

      final Entry<Class<?>, BiConsumer<PaymentSystemSpecific, ?>> entry = mapConsumers.get(tag);

      final Class<?> clazz = entry.getKey();

      final BiConsumer consumer = entry.getValue();

      consumer.accept(result, DecoderMpm.decode(value, clazz));
    }

    return result;
  }

  private String derivateId(final String id) {

    if (betweenPaymentNetworkSpecificRange(id)) {
      return PaymentSystemSpecificFieldCodes.ID_PAYMENT_NETWORK_SPECIFIC;
    }

    return id;
  }

  private boolean betweenPaymentNetworkSpecificRange(final String value) {
    return value.compareTo(PaymentSystemSpecificFieldCodes.ID_PAYMENT_NETWORK_SPECIFIC_START) >= 0
        && value.compareTo(PaymentSystemSpecificFieldCodes.ID_PAYMENT_NETWORK_SPECIFIC_END) <= 0;
  }

}
// @formatter:on
