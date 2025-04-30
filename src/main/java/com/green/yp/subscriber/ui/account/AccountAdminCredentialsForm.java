package com.green.yp.subscriber.ui.account;

import com.green.yp.api.apitype.account.AccountResponse;
import com.green.yp.api.apitype.producer.UserCredentialsRequest;
import com.green.yp.subscriber.service.AccountService;
import com.green.yp.subscriber.service.UspsAddressService;
import com.green.yp.subscriber.service.reference.ReferenceService;
import com.green.yp.subscriber.ui.components.AbstractFormLayout;
import com.green.yp.subscriber.ui.components.PhoneNumberField;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import java.util.Optional;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

public class AccountAdminCredentialsForm extends AbstractFormLayout<UserCredentialsRequest> {

    private AccountService accountService;

    private final TextField firstNameField;
    private final TextField lastNameField;
    private final PhoneNumberField businessPhoneField;
    private final PhoneNumberField cellPhoneField;
    private final EmailField emailAddressField;
    private final TextField userNameField;
    private final PasswordField passwordField;

    public AccountAdminCredentialsForm() {
        setResponsiveSteps(new ResponsiveStep("0", 1),
                new ResponsiveStep("20em", 2));

        firstNameField = new TextField("First Name");
        add(firstNameField);
        firstNameField.setRequired(true);
        firstNameField.setRequiredIndicatorVisible(true);

        lastNameField = new TextField("Last Name");
        add(lastNameField);
        lastNameField.setRequired(true);
        lastNameField.setRequiredIndicatorVisible(true);

        businessPhoneField = new PhoneNumberField("Business/Office phone number");
        businessPhoneField.setRequired(true);
        businessPhoneField.setRequiredIndicatorVisible(true);
        add(businessPhoneField);

        cellPhoneField = new PhoneNumberField("Cellphone number");
        add(cellPhoneField);

        emailAddressField = new EmailField("Contact Email Address");
        add(emailAddressField, 2);
        emailAddressField.setRequired(true);
        emailAddressField.setRequiredIndicatorVisible(true);

        userNameField = new TextField("Username");
        add(userNameField, 2);
        userNameField.setRequired(true);
        userNameField.setRequiredIndicatorVisible(true);

        passwordField = new PasswordField("Password");
        initializePasswordField(passwordField);

        emailAddressField.addValueChangeListener(event -> {
            if (StringUtils.isNotBlank(event.getValue())) {
                userNameField.setValue(event.getValue());
            }
        });
    }

    private void initializePasswordField(PasswordField passwordField) {

        add(passwordField);
        passwordField.setRequired(true);
        passwordField.setRequiredIndicatorVisible(true);
        passwordField.setMinLength(8);
        passwordField.setMaxLength(32);
        passwordField.setAllowedCharPattern("[A-Za-z0-9@!$%]");

        Icon checkIcon = VaadinIcon.CHECK.create();
        checkIcon.setVisible(false);
        checkIcon.getStyle().set("color", "var(--lumo-success-color)");
        passwordField.setSuffixComponent(checkIcon);

        Div passwordStrength = new Div();
        passwordStrength.add(new Text("Password strength: "),
                new Span());
        passwordField.setHelperComponent(passwordStrength);
    }

    @Override
    public void initialize(@NonNull ReferenceService referenceService,
                           @NonNull AccountService accountService,
                           @NonNull UspsAddressService uspsAddressService) {
        this.accountService = accountService;
    }

    @Override
    public Optional<UserCredentialsRequest> getFormData() {
        Optional<AccountResponse> optionalResponse = (Optional<AccountResponse>) getAttribute("AccountResponse");
        AccountResponse accountResponse = optionalResponse.get();
        UserCredentialsRequest credentialsRequest = UserCredentialsRequest.builder()
                .firstName(firstNameField.getValue())
                .lastName(lastNameField.getValue())
                .businessPhone(businessPhoneField.getValue())
                .cellPhone(cellPhoneField.getValue())
                .emailAddress(emailAddressField.getValue())
                .userName(userNameField.getValue())
                .credentials(passwordField.getValue())
                .build();
        accountService.updateUserCredentials(accountResponse.producer().producerId(), credentialsRequest);
        return Optional.of(credentialsRequest);
    }
}
