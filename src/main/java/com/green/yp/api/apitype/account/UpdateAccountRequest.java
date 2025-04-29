package com.green.yp.api.apitype.account;

import com.green.yp.api.apitype.producer.LocationRequest;
import com.green.yp.api.apitype.producer.ProducerContactRequest;
import com.green.yp.api.apitype.producer.ProducerRequest;
import com.green.yp.api.apitype.producer.UserCredentialsRequest;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {

  @NotNull @NonNull UUID producerId;

  private ProducerRequest producerRequest;

  private ProducerContactRequest primaryContact;

  private LocationRequest primaryLocation;

  private UserCredentialsRequest masterUserCredentials;
}
