package com.green.yp.subscriber.integration.account;

import com.green.yp.api.apitype.account.AccountResponse;
import com.green.yp.api.apitype.account.CreateAccountRequest;
import com.green.yp.api.apitype.account.UpdateAccountRequest;
import com.green.yp.api.apitype.common.ResponseApi;
import com.green.yp.subscriber.integration.GreenPagesFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "SubscriberAccountClient",
        url = "${green.yp.account.url}",
        configuration = GreenPagesFeignConfiguration.class)
public interface AccountClient {
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseApi<AccountResponse> createAccount(@RequestBody CreateAccountRequest account);
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseApi<AccountResponse> updateAccount(@RequestBody UpdateAccountRequest updateRequest);
}
