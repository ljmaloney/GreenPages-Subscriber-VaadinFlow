package com.green.yp.subscriber.ui.account;

import com.green.yp.api.apitype.producer.ProducerContactRequest;
import com.green.yp.api.apitype.producer.enumeration.ProducerContactType;
import com.green.yp.api.apitype.producer.enumeration.ProducerDisplayContactType;
import com.green.yp.subscriber.service.AccountService;
import com.green.yp.subscriber.service.UspsAddressService;
import com.green.yp.subscriber.service.reference.ReferenceService;
import com.green.yp.subscriber.ui.components.AbstractFormLayout;
import com.green.yp.subscriber.ui.components.Divider;
import com.green.yp.subscriber.ui.components.PhoneNumberField;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import java.util.Optional;
import lombok.NonNull;

public class AccountContactForm extends AbstractFormLayout<ProducerContactRequest> {

    private ReferenceService referenceService;

    private final TextField genericContactField;
    private final TextField firstNameField;
    private final TextField lastNameField;
    private final PhoneNumberField businessPhoneField;
    private final PhoneNumberField cellPhoneField;
    private final EmailField emailAddressField;
    private final Select<ProducerContactType> contactTypeSelect;
    private final Select<ProducerDisplayContactType> displayContactSelect;


    public AccountContactForm() {

        setResponsiveSteps(new ResponsiveStep("0", 1),
                new ResponsiveStep("20em", 2));

        genericContactField = new TextField("Generic Contact Name : example Front Desk");
        add(genericContactField, 2);

        add(new HorizontalLayout(new Divider(), new Span("OR"), new Divider()), 2);

        firstNameField = new TextField("First Name");
        add(firstNameField);

        lastNameField = new TextField("Last Name");
        add(lastNameField);

        businessPhoneField = new PhoneNumberField("Business/Office phone number");
        add(businessPhoneField);

        cellPhoneField = new PhoneNumberField("Cellphone number");
        add(cellPhoneField);

        emailAddressField = new EmailField("Contact Email Address");
        add(emailAddressField, 2);

        displayContactSelect = new Select<>("Choose the the display for the contact information",
                this::displayContactValueChanged);
        displayContactSelect.setItems(ProducerDisplayContactType.getValuesAsList());
        displayContactSelect.setItemLabelGenerator(type -> type.getDisplayLabel());
        displayContactSelect.setValue(ProducerDisplayContactType.PHONE_EMAIL_ONLY);

        contactTypeSelect = new Select<>("Select the type of contact", this::contactTypeValueChanged);
        contactTypeSelect.setItems(ProducerContactType.getAccountCreationTypes());
        contactTypeSelect.setValue(ProducerContactType.PRIMARY);
        contactTypeSelect.setItemLabelGenerator(type -> type.getDisplayLabel());

        add(contactTypeSelect, 2);
        add(displayContactSelect, 2);

    }

    private void displayContactValueChanged(AbstractField.ComponentValueChangeEvent<Select<ProducerDisplayContactType>,
            ProducerDisplayContactType> valueChangeEvent) {
    }

    private void contactTypeValueChanged(AbstractField.ComponentValueChangeEvent<Select<ProducerContactType>,
            ProducerContactType> valueChangeEvent) {
        if (valueChangeEvent.getValue() != null
                && valueChangeEvent.getValue().isDisplayOnPage()) {
            displayContactSelect.setReadOnly(false);
        } else {
            displayContactSelect.setReadOnly(true);
            displayContactSelect.setValue(ProducerDisplayContactType.NO_DISPLAY);
        }
    }

    @Override
    public void initialize(@NonNull ReferenceService referenceService,
                           @NonNull AccountService accountService,
                           @NonNull UspsAddressService uspsAddressService) {
        this.referenceService = referenceService;
    }

    @Override
    public Optional<ProducerContactRequest> getFormData() {
        return Optional.empty();
    }
}
