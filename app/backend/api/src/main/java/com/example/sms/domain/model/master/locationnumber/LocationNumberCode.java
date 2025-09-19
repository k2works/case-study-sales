package com.example.sms.domain.model.master.locationnumber;

import lombok.Value;

/**
 * 棚番コード
 */
@Value(staticConstructor = "of")
public class LocationNumberCode {
    String value;
}