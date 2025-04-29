package com.green.yp.subscriber.ui.account;

import com.green.yp.subscriber.service.AccountService;
import com.green.yp.subscriber.service.UspsAddressService;
import com.green.yp.subscriber.service.reference.ReferenceService;
import com.green.yp.subscriber.ui.components.AbstractFormLayout;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.TextArea;
import java.util.Optional;
import lombok.NonNull;

public class TermsAndConditionsForm extends AbstractFormLayout<String> {

    private final TextArea termsAndConditions;

    private final Checkbox acceptTermsCheckbox;

    public TermsAndConditionsForm() {
        setResponsiveSteps(new ResponsiveStep("0", 1),
                new ResponsiveStep("20em", 2));

        termsAndConditions = new TextArea("Please review the following terms and conditions of service");
        add(termsAndConditions, 2);
        termsAndConditions.setMinHeight(400, Unit.PIXELS);
        termsAndConditions.setEnabled(false);

        acceptTermsCheckbox = new Checkbox();
        acceptTermsCheckbox.setLabel("I accept the terms and conditions as outlined above");
        add(acceptTermsCheckbox, 2);
    }

    @Override
    public void initialize(@NonNull ReferenceService referenceService,
                           @NonNull AccountService accountService, @NonNull UspsAddressService uspsAddressService) {

    }

    @Override
    public Optional<String> getFormData() {
        return Optional.empty();
    }

    @Override
    public boolean validateForm() {
        return acceptTermsCheckbox.getValue();
    }
}
