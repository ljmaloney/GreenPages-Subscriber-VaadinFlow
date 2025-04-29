package com.green.yp.subscriber.ui.account;

import com.green.yp.api.apitype.account.AccountResponse;
import com.green.yp.api.apitype.producer.BusinessDetailsRequest;
import com.green.yp.api.apitype.producer.ProducerResponse;
import com.green.yp.api.apitype.producer.enumeration.ProducerSubscriptionType;
import com.green.yp.api.apitype.reference.LineOfBusinessApi;
import com.green.yp.api.apitype.reference.SubscriptionApi;
import com.green.yp.subscriber.service.AccountService;
import com.green.yp.subscriber.service.UspsAddressService;
import com.green.yp.subscriber.service.reference.ReferenceService;
import com.green.yp.subscriber.ui.components.AbstractFormLayout;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationStatusChangeEvent;
import io.micrometer.common.util.StringUtils;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BusinessDetailsForm extends AbstractFormLayout<AccountResponse> {

    private ReferenceService referenceService;
    private final TextField businessNameField;
    private final TextField urlField;
    Select<LineOfBusinessApi> lobSelect;
    Select<SubscriptionApi> subscriptionSelect;
    private final TextArea businessNarrative;

    private Boolean formIsValid = Boolean.FALSE;
    private AccountService accountService;

    public BusinessDetailsForm() {
        log.trace("Initialize business details form");
        setResponsiveSteps(new ResponsiveStep("0", 1),
                new ResponsiveStep("20em", 2));

        businessNameField = new TextField("Business Name");
        businessNameField.setMaxLength(100);
        businessNameField.setRequired(true);
        businessNameField.addValidationStatusChangeListener(this::nameValidationStatus);
        add(businessNameField, 2);

        urlField = new TextField("Website Url");
        urlField.setMaxLength(200);
        urlField.setRequired(false);
        urlField.setTooltipText("The url to your website");
        add(urlField, 2);

        lobSelect = new Select<>("Select the line of business that best fits your business", this::lobValueChanged);
        lobSelect.setItemLabelGenerator(lob -> lob.lineOfBusiness());
        lobSelect.setPlaceholder("Please select the line of business");
        lobSelect.setRequiredIndicatorVisible(true);
        add(lobSelect, 2);

        subscriptionSelect = new Select<>("Select the subscription for your listing", this::subscriptionValueChanged);
        subscriptionSelect.setPlaceholder("Please select the subscription");
        subscriptionSelect.setRequiredIndicatorVisible(true);
        subscriptionSelect.setItemLabelGenerator(api ->
                String.format("%s - Monthly $%,.2f", api.getDisplayName(), api.getMonthlyAutopayAmount()));
        add(subscriptionSelect, 2);

        businessNarrative = new TextArea("Business Narrative - max 512 characters");
        businessNarrative.setRequired(true);
        businessNarrative.setRequiredIndicatorVisible(true);
        businessNarrative.setMinHeight(200, Unit.PIXELS);
        businessNarrative.addValidationStatusChangeListener(event -> formIsValid = event.getNewStatus());
        add(businessNarrative, 2);
        businessNameField.focus();
    }

    public void setVisible(Boolean visible){
        super.setVisible(visible);
        if ( Boolean.TRUE.equals(visible)){
            Optional<AccountResponse> optionalResponse = (Optional<AccountResponse>) getAttribute("AccountResponse");
            optionalResponse.ifPresent(accountResponse -> {
                businessNameField.setValue(accountResponse.producer().businessName());
                urlField.setValue(accountResponse.producer().websiteUrl());
                businessNarrative.setValue(accountResponse.producer().narrative());
                lobSelect.getListDataView().getItems()
                        .filter(lob -> lob.lineOfBusinessId().equals(accountResponse.producer().lineOfBusinessId()))
                        .findFirst()
                        .ifPresent(lob -> lobSelect.setValue(lob));
                UUID subscriptionId = accountResponse.producer().subscriptions().stream()
                        .filter(subs -> subs.subscriptionType().isPrimarySubscription())
                        .findFirst()
                        .get().subscriptionId();

                subscriptionSelect.getListDataView().getItems()
                        .filter(subs -> subs.getSubscriptionId().equals(subscriptionId))
                        .findFirst()
                        .ifPresent(sub -> subscriptionSelect.setValue(sub));
            });
        }
    }

    private void nameValidationStatus(ValidationStatusChangeEvent<String> event) {
        formIsValid = event.getNewStatus();
    }

    private void subscriptionValueChanged(AbstractField.ComponentValueChangeEvent<Select<SubscriptionApi>, SubscriptionApi>
                                                  event) {
    }

    private void lobValueChanged(AbstractField.ComponentValueChangeEvent<Select<LineOfBusinessApi>, LineOfBusinessApi>
                                         event) {
    }

    @Override
    public void initialize(@NonNull ReferenceService referenceService,
                           @NonNull AccountService accountService,
                           @NonNull UspsAddressService uspsAddressService) {
        this.referenceService = referenceService;
        this.accountService = accountService;
        lobSelect.setItems(referenceService.getLinesOfBusiness());
        subscriptionSelect.setItems(referenceService.getSubscriptions());

    }

    @Override
    public Optional<AccountResponse> getFormData() {
        BusinessDetailsRequest request = new BusinessDetailsRequest(null,
                businessNameField.getValue(),
                lobSelect.getValue().lineOfBusiness(),
                lobSelect.getValue().lineOfBusinessId(),
                subscriptionSelect.getValue().getSubscriptionId(),
                ProducerSubscriptionType.LIVE_UNPAID,
                urlField.getValue(),
                businessNarrative.getValue());

        Optional<AccountResponse> optionalResponse = (Optional<AccountResponse>) getAttribute("AccountResponse");
        if ( optionalResponse.isEmpty()){
            AccountResponse accountResponse = accountService.createNewAccount(request);
            return Optional.of(accountResponse);
        }
        ProducerResponse producerResponse = optionalResponse.get().producer();
        AccountResponse updatedResponse = accountService.updateBusinessDetails(producerResponse.producerId(), request);
        return Optional.of(updatedResponse);
     }

    @Override
    public boolean validateForm() {
        formIsValid = true;
        if (StringUtils.isBlank(businessNameField.getValue())) {
            businessNameField.setErrorMessage("Required field");
            formIsValid = false;
        }
        if (StringUtils.isBlank(businessNarrative.getValue())) {
            businessNarrative.setErrorMessage("Required field");
            formIsValid = false;
        }
        if (lobSelect.getOptionalValue().isEmpty()
                || subscriptionSelect.getOptionalValue().isEmpty()) {
            formIsValid = false;
        }
        return formIsValid;
    }
}
