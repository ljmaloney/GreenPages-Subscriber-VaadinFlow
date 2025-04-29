package com.green.yp.subscriber.service;

import com.green.yp.api.apitype.account.AccountResponse;
import com.green.yp.api.apitype.account.CreateAccountRequest;
import com.green.yp.api.apitype.account.UpdateAccountRequest;
import com.green.yp.api.apitype.common.ResponseApi;
import com.green.yp.api.apitype.producer.BusinessDetailsRequest;
import com.green.yp.api.apitype.producer.LocationRequest;
import com.green.yp.api.apitype.producer.ProducerRequest;
import com.green.yp.api.apitype.producer.enumeration.InvoiceCycleType;
import com.green.yp.subscriber.integration.account.AccountClient;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountService {

    private final AccountClient accountClient;

    public AccountService(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    public AccountResponse createNewAccount(BusinessDetailsRequest businessDetails) {

        CreateAccountRequest createRequest = CreateAccountRequest.builder()
                .businessName(businessDetails.businessName())
                .lineOfBusinessId(businessDetails.lineOfBusinessId())
                .subscriptionId(businessDetails.subscriptionId())
                .subscriptionType(businessDetails.subscriptionType())
                .narrative(businessDetails.narrative())
                .websiteUrl(businessDetails.websiteUrl())
                .build();

        ResponseApi<AccountResponse> response = accountClient.createAccount(createRequest);

        return response.getResponse();
    }

    public AccountResponse updateBusinessDetails(UUID producerId, BusinessDetailsRequest businessDetails) {
        UpdateAccountRequest updateRequest = UpdateAccountRequest.builder()
                .producerId(producerId)
                .producerRequest(new ProducerRequest(producerId,
                        businessDetails.businessName(),
                        businessDetails.lineOfBusinessId(),
                        businessDetails.subscriptionId(),
                        businessDetails.subscriptionType(),
                        InvoiceCycleType.MONTHLY,
                        businessDetails.narrative(),
                        businessDetails.websiteUrl())).build();

        ResponseApi<AccountResponse> response = accountClient.updateAccount(updateRequest);

        return response.getResponse();
    }

    public AccountResponse updatePrimaryLocation(UUID producerId, LocationRequest locationRequest){
        UpdateAccountRequest updateRequest = UpdateAccountRequest.builder()
                .producerId(producerId)
                .primaryLocation(locationRequest)
                .build();

        ResponseApi<AccountResponse> response = accountClient.updateAccount(updateRequest);

        return response.getResponse();
    }
}
