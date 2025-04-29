package com.green.yp.subscriber.ui.account;

import com.green.yp.api.apitype.account.AccountResponse;
import com.green.yp.subscriber.service.AccountService;
import com.green.yp.subscriber.service.UspsAddressService;
import com.green.yp.subscriber.service.reference.ReferenceService;
import com.green.yp.subscriber.ui.AbstractVerticalLayout;
import com.green.yp.subscriber.ui.components.AbstractFormLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/register")
@Slf4j
@AnonymousAllowed
@PermitAll
public class RegisterAccountView extends AbstractVerticalLayout {

    @NonNull
    private final ReferenceService referenceService;
    @NonNull
    final AccountService accountService;
    @NonNull
    final UspsAddressService uspsAddressService;

    private enum AccountCreateTabs {
        BUSINESS_DETAILS(BusinessDetailsForm.class, "Business Details", true),
        PRIMARY_LOCATION(BusinessLocationForm.class, "Primary Business Location", false),
        PRIMARY_CONTACT(AccountContactForm.class, "Primary Business Contact", false),
        ACCOUNT_ADMIN_CREDS(AccountAdminCredentialsForm.class, "Account Administrator", false),
        TERMS_AND_CONDITIONS(TermsAndConditionsForm.class, "Terms and Conditions", false);
        private final Class formLayoutClass;
        private final String tabLabel;
        private final Boolean firstTab;

        AccountCreateTabs(Class clazz, String tabLabel, Boolean firstTab) {
            this.formLayoutClass = clazz;
            this.tabLabel = tabLabel;
            this.firstTab = firstTab;
        }
    }

    private TabSheet tabSheet;
    private final List<AbstractFormLayout<?>> accountForms = new ArrayList<>();

    public RegisterAccountView(@NonNull @Autowired ReferenceService referenceService,
                               @NonNull @Autowired AccountService accountService,
                               @NonNull @Autowired UspsAddressService uspsAddressService) {
        this.referenceService = referenceService;
        this.accountService = accountService;
        this.uspsAddressService = uspsAddressService;
    }

    public @NonNull AccountService getAccountService() {
        return accountService;
    }

    @Override
    public void addPageHeader(Div pageHeaderDiv) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidthFull();
        verticalLayout.setAlignItems(Alignment.START);

        H3 headline = new H3(getHeaderText());
        headline.getStyle().setTextAlign(Style.TextAlign.LEFT);
        headline.getStyle().setColor("#106478");
        verticalLayout.add(headline);
        verticalLayout.setWidth(90, Unit.PERCENTAGE);

        Paragraph paragraph = new Paragraph();
        verticalLayout.add(paragraph);

        paragraph.setWidth(90, Unit.PERCENTAGE);
        paragraph.setText("""
                    We are excited to have you join the premier marketplace for the green industry.  
                """);
        verticalLayout.add(new Paragraph("""
                To join the marketplace, please complete the form below. Once your account has been created, 
                you will gain access to your dashboard. 
                 """));

        pageHeaderDiv.add(verticalLayout);
    }

    @Override
    protected String getHeaderText() {
        return "Account Registration";
    }

    @Override
    public void initializePage(Div pageContentsDiv) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth(1000, Unit.PIXELS);
        layout.setAlignItems(Alignment.CENTER);
        layout.setDefaultVerticalComponentAlignment(Alignment.START);
        VerticalLayout verticalLayout = new VerticalLayout();
        layout.add(verticalLayout);
        pageContentsDiv.add(layout);

        tabSheet = new TabSheet();
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_BORDERED);

        Arrays.stream(AccountCreateTabs.values())
                .forEach(tab -> {
                    try {
                        AbstractFormLayout<?> form = (AbstractFormLayout<?>) tab.formLayoutClass
                                .getDeclaredConstructor().newInstance();
                        form.initialize(referenceService, accountService, uspsAddressService);
                        Span tabSpan = new Span(tab.tabLabel);
                        tabSpan.getStyle().setTextDecoration("bold");
                        tabSheet.add(tabSpan, form);
//                        tabSheet.getTab(form).setVisible(tab.firstTab);
                        accountForms.add(form);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        verticalLayout.add(tabSheet);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button resetButton = new Button("Reset Form", this::resetClickListener);
        resetButton.getStyle().setColor("#106478");
        buttonLayout.add(resetButton);

        Button registerButton = new Button("Register Account", this::registerAccount);
        registerButton.getStyle().setColor("#106478");
        registerButton.setVisible(false);
        buttonLayout.add(registerButton);

        Button nextButton = new Button("Next >>", event -> nextClickListener(event, registerButton));
        nextButton.getStyle().setColor("#106478");
        nextButton.setVisible(true);
        buttonLayout.add(nextButton);

        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setAlignItems(Alignment.END);
        buttonLayout.setWidthFull();

        verticalLayout.add(buttonLayout);
    }

    private void nextClickListener(ClickEvent<Button> clickEvent, Button registerButton) {
        AbstractFormLayout component = (AbstractFormLayout) tabSheet.getComponent(tabSheet.getSelectedTab());
        if (component.validateForm()) {
            int currentIndex = accountForms.indexOf(component);
            if (component.validateForm()) {
                Optional<AccountResponse> optionalResponse = component.getFormData();
                optionalResponse
                        .ifPresent(accountResponse -> setAttribute("AccountResponse", accountResponse));
            }
            if (currentIndex + 1 == accountForms.size()) {
                clickEvent.getSource().setVisible(false);
                registerButton.setVisible(true);
            } else {
                AbstractFormLayout nextComponent = accountForms.get(currentIndex + 1);
                nextTab(nextComponent);
//                tabSheet.getTab(component).setSelected(false);
//                nextComponent.setVisible(true);
//                component.setVisible(false);
                if (nextComponent == accountForms.get(accountForms.size() - 1)) {
                    clickEvent.getSource().setVisible(false);
                    registerButton.setVisible(true);
                }
            }
        }
    }

    private void nextTab(FormLayout tabForm) {
//        Tab tab = tabSheet.getTab(tabForm);
//        tabForm.setVisible(true);
//        tabSheet.setSelectedTab(tab);
        tabSheet.setSelectedIndex(tabSheet.getSelectedIndex() + 1);
    }

    private void resetClickListener(ClickEvent<Button> buttonClickEvent) {
        AbstractFormLayout component = (AbstractFormLayout) tabSheet.getComponent(tabSheet.getSelectedTab());
        component.resetForm();
    }

    private void registerAccount(ClickEvent<Button> registerEvent) {
//        boolean isInvalidForm = accountForms.stream()
//                .anyMatch(acct -> Boolean.FALSE.booleanValue() == acct.validateForm());
//
//        if (isInvalidForm) {
//            LocalErrorDialog errorDialog = new LocalErrorDialog("Account Registration"
//                    , "One or more required fields is missing or invalid");
//            errorDialog.setVisible(true);
//            return;
//        }
//
//        try {
//            CreateAccountResponse createResponse = accountService.createNewAccount(
//                    (BusinessDetailsRequest) accountForms.get(0).getFormData(),
//                    (ProducerContactRequest) accountForms.get(1).getFormData(),
//                    (CreateLocationRequest) accountForms.get(2).getFormData(),
//                    (CreateUserCredentialsRequest) accountForms.get(3).getFormData(),
//                    OffsetDateTime.now());
//
//            setAttribute("CreateAccountResponse", createResponse);
//
//            registerEvent.getSource()
//                    .getUI()
//                    .ifPresent(ui -> ui.navigate(RegistrationConfirmationView.class));
//
//        } catch (Exception e) {
//            log.error("An error occurred when attempting to create the account");
//            LocalErrorDialog errorDialog = new LocalErrorDialog("Registration Error",
//                    "An unexpected error has occurred when attempting to create your account.");
//            errorDialog.setVisible(true);
//        }
    }
}
