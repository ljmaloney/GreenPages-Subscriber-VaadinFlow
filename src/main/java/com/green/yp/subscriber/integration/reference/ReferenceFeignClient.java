package com.green.yp.subscriber.integration.reference;

import com.green.yp.api.apitype.common.ResponseApi;
import com.green.yp.api.apitype.reference.LineOfBusinessApi;
import com.green.yp.api.apitype.reference.SubscriptionApi;
import com.green.yp.subscriber.integration.GreenPagesFeignConfiguration;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ReferenceDataClient",
        url = "${green.yp.reference.url}",
        configuration = GreenPagesFeignConfiguration.class)
public interface ReferenceFeignClient {
    @GetMapping(value = "/lob", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseApi<List<LineOfBusinessApi>> getLineOfBusiness();

    @GetMapping(value = "/subscription", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseApi<List<SubscriptionApi>> getSubscriptions();
}
