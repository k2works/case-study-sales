package com.example.sms.domain.model.master.partner.vendor;

import com.example.sms.domain.model.master.partner.PartnerName;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 仕入先名
 */
@Value
@NoArgsConstructor(force = true)
public class VendorName {
    PartnerName value;

    public VendorName(String vendorName, String vendorNameKana) {
        this.value = PartnerName.of(vendorName, vendorNameKana);
    }

    public static VendorName of(
            String vendorName,
            String vendorNameKana
    ) {
        return new VendorName(
                vendorName,
                vendorNameKana
        );
    }
}
