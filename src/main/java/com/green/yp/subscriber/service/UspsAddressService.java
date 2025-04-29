package com.green.yp.subscriber.service;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import usps.client.UspsAddressClient;
import usps.model.AddressResponse;

@Slf4j
@Service
public class UspsAddressService {

    private UspsAddressClient uspsAddressClient;
    @Value("${greem.yp.usps.address.api.userId}")
    private String uspsUserId;
    @Value("${green.yp.usps.address.api.url}")
    private String uspsUrl;

    public UspsAddressService() {
    }

    @PostConstruct
    public void init() {
        uspsAddressClient = new UspsAddressClient(uspsUserId, uspsUrl);
    }


    public AddressResponse validateAddress(@NotNull @NonNull String addressLine1,
                                           String addressLine2,
                                           @NotNull @NonNull String city,
                                           @NotNull @NonNull String state,
                                           @NotNull @NonNull String zipCode) {

        return uspsAddressClient.validateAddress(addressLine1, addressLine2, city, state, zipCode);
    }
}
