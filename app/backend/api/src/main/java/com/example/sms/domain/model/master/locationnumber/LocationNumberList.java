package com.example.sms.domain.model.master.locationnumber;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LocationNumberList {
    private final List<LocationNumber> asList;

    public List<LocationNumber> asList() {
        return asList;
    }
}