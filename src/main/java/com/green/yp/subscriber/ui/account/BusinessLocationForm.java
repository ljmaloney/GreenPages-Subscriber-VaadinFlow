package com.green.yp.subscriber.ui.account;

import com.green.yp.api.apitype.account.AccountResponse;
import com.green.yp.api.apitype.common.StateType;
import com.green.yp.api.apitype.producer.LocationRequest;
import com.green.yp.api.apitype.producer.enumeration.LocationDisplayType;
import com.green.yp.api.apitype.producer.enumeration.ProducerLocationType;
import com.green.yp.subscriber.service.AccountService;
import com.green.yp.subscriber.service.UspsAddressService;
import com.green.yp.subscriber.service.reference.ReferenceService;
import com.green.yp.subscriber.ui.components.AbstractFormLayout;
import com.green.yp.subscriber.ui.components.ConfirmMessageDiv;
import com.green.yp.subscriber.ui.components.MessageDiv;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import java.util.Arrays;
import java.util.Optional;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import usps.enumeration.FootnoteType;
import usps.model.AddressResponse;

public class BusinessLocationForm extends AbstractFormLayout<AccountResponse> {

    private AccountService accountService;
    private UspsAddressService uspsAddressService;
    private final TextField addressLine1Field;
    private final TextField addressLine2Field;
    private final TextField addressLine3Field;
    private final TextField cityField;
    private final Select<StateType> stateSelect;
    private final TextField zipCodeField;
    private final TextField websiteUrlField;
    private final Select<ProducerLocationType> locationTypeSelect;
    private final Select<LocationDisplayType> locationDisplaySelect;
    private final MessageDiv informMessage;
    private final ConfirmMessageDiv addressConfirmMessage;

    public BusinessLocationForm() {

        setResponsiveSteps(new ResponsiveStep("0", 1),
                new ResponsiveStep("20em", 2));

        informMessage = new MessageDiv("Address Updated",
                "Your address has been updated to match US Postal Service records");
        add(informMessage, 2);

        addressLine1Field = new TextField("Address Line 1");
        addressLine1Field.setRequired(true);
        addressLine1Field.setMaxLength(100);
        add(addressLine1Field, 2);

        addressLine2Field = new TextField("Address Line 2");
        addressLine2Field.setRequired(false);
        addressLine2Field.setMaxLength(100);
        add(addressLine2Field, 2);

        addressLine3Field = new TextField("Address Line 3");
        addressLine3Field.setRequired(false);
        addressLine3Field.setMaxLength(100);
        add(addressLine3Field, 2);

        cityField = new TextField("City");
        cityField.setRequired(true);
        cityField.setMaxLength(100);
        cityField.setMinLength(2);
        add(cityField, 2);

        stateSelect = new Select<StateType>("State", this::stateSelected);
        stateSelect.setItems(Arrays.asList(StateType.values()));
        stateSelect.setRequiredIndicatorVisible(true);
        stateSelect.setItemLabelGenerator(StateType::getUnabbreviated);
        add(stateSelect);

        zipCodeField = new TextField("Zip Code");
        zipCodeField.setRequired(true);
        zipCodeField.setMaxLength(10);
        zipCodeField.setMinLength(4);
        zipCodeField.setPattern("^((\\d{5}-\\d{4})|(\\d{5})|(\\d{9})|([A-Z]\\d[A-Z]\\s\\d[A-Z]\\d))$");
        add(zipCodeField);

        websiteUrlField = new TextField("Location website Url:");
        websiteUrlField.setRequired(false);
        websiteUrlField.setMaxLength(100);
        add(websiteUrlField, 2);

        locationTypeSelect = new Select<>("Select the type of location", this::locationTypeSelect);
        locationTypeSelect.setItems(Arrays.asList(ProducerLocationType.values()));
        locationTypeSelect.setValue(ProducerLocationType.HOME_OFFICE_PRIMARY);
        locationTypeSelect.setRequiredIndicatorVisible(true);
        locationTypeSelect.setItemLabelGenerator(ProducerLocationType::getDisplayLabel);
        add(locationTypeSelect, 2);

        locationDisplaySelect = new Select<>("Select the display for the location", this::locationDisplaySelect);
        locationDisplaySelect.setItems(Arrays.asList(LocationDisplayType.values()));
        locationDisplaySelect.setValue(LocationDisplayType.CITY_STATE_ZIP);
        locationDisplaySelect.setRequiredIndicatorVisible(true);
        locationDisplaySelect.setItemLabelGenerator(LocationDisplayType::getDisplayLabel);
        add(locationDisplaySelect, 2);

        addressConfirmMessage = new ConfirmMessageDiv();
        add(addressConfirmMessage, 2);
        addressConfirmMessage.setVisible(false);

    }

    @Override
    public void initialize(@NonNull ReferenceService referenceService,
                           @NonNull AccountService accountService,
                           @NonNull UspsAddressService uspsAddressService) {
        this.accountService = accountService;
        this.uspsAddressService = uspsAddressService;
    }

    @Override
    public boolean validateForm() {
        Boolean formValid = super.validateForm();
        if (Boolean.FALSE.equals(formValid)) {
            return formValid;
        }
        if (isVisible()) {
            AddressResponse addressResponse = uspsAddressService.validateAddress(addressLine1Field.getValue(),
                    addressLine2Field.getValue(),
                    cityField.getValue(),
                    stateSelect.getValue().getANSIAbbreviation(),
                    zipCodeField.getValue());

            processValidationResponse(addressResponse);
        }
        return !addressConfirmMessage.isVisible() || addressConfirmMessage.isConfirmed();
    }

    private void processValidationResponse(AddressResponse addressResponse) {
        FootnoteType footnoteType = FootnoteType.findType(addressResponse.getFootNotes());
        switch (footnoteType) {
            case ZIP_CORRECTED:
                if (StringUtils.isNotBlank(addressResponse.getZip4())) {
                    zipCodeField.setValue(String.format("%s-%s", addressResponse.getZip(), addressResponse.getZip4()));
                } else {
                    zipCodeField.setValue(addressResponse.getZip());
                }
                informMessage.showMessage("Zip code updated",
                        "Your zip code has been updated to match United States Postal Service records");
                return;
            case CITY_STATE_SPELLING:
                cityField.setValue(addressResponse.getCity());
                stateSelect.setValue(StateType.parse(addressResponse.getState()));
                informMessage.showMessage("City or State updated",
                        "The spelling of your city and / or state has been updated to match United States Postal Service records");
                return;
            case ADDRESS_STANDARDIZED:
                informMessage.showMessage("Address Verified",
                        "Your address has been verified using United States Postal Service records");
                return;
            default:
                informMessage.setVisible(false);
                addressConfirmMessage.showConfirmMessage("Address not Validated",
                        "The United States Postal Service wasd not able to validate your address. " +
                                "Please review your address or click to confirm the address is correct");
        }
    }

    @Override
    public Optional<AccountResponse> getFormData() {
        Optional<AccountResponse> optionalResponse = (Optional<AccountResponse>) getAttribute("AccountResponse");
        AccountResponse accountResponse = optionalResponse.get();
        return Optional.of(accountService.updatePrimaryLocation(accountResponse.producer().producerId(),
                LocationRequest.builder()
                        .active(true)
                        .locationId(accountResponse.primaryLocation() != null ? accountResponse.primaryLocation().locationId() : null)
                        .addressLine1(addressLine1Field.getValue())
                        .addressLine2(addressLine2Field.getValue())
                        .addressLine3(addressLine3Field.getValue())
                        .city(cityField.getValue())
                        .state(stateSelect.getValue().getANSIAbbreviation())
                        .postalCode(zipCodeField.getValue())
                        .locationType(locationTypeSelect.getValue())
                        .locationDisplayType(locationDisplaySelect.getValue())
                        .websiteUrl(websiteUrlField.getValue())
                        .build()));
    }

    private void locationDisplaySelect(AbstractField.ComponentValueChangeEvent<Select<LocationDisplayType>,
            LocationDisplayType> selectLocationDisplayTypeComponentValueChangeEvent) {
    }

    private void locationTypeSelect(AbstractField.ComponentValueChangeEvent<Select<ProducerLocationType>,
            ProducerLocationType> selectProducerLocationTypeComponentValueChangeEvent) {
    }

    private void stateSelected(AbstractField.ComponentValueChangeEvent<Select<StateType>,
            StateType> selectStateTypeComponentValueChangeEvent) {
    }

}
