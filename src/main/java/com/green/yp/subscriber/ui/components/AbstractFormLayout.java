package com.green.yp.subscriber.ui.components;

import com.green.yp.subscriber.service.AccountService;
import com.green.yp.subscriber.service.UspsAddressService;
import com.green.yp.subscriber.service.reference.ReferenceService;
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.WrappedHttpSession;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import lombok.NonNull;

public abstract class AbstractFormLayout<T> extends FormLayout {

    public abstract void initialize(@NotNull @NonNull ReferenceService referenceService,
                                    @NonNull AccountService accountService,
                                    @NonNull UspsAddressService uspsAddressService);

    public abstract Optional<T> getFormData();

    public boolean validateForm() {
        getChildren().filter(this::isRequiredField)
                .forEach(this::setRequiredText);
        return getChildren().filter(this::isFieldInvalid).findFirst().isEmpty();
    }

    private boolean isFieldInvalid(Component component) {
        if (component instanceof TextField field) {
            return field.isVisible() && field.isEnabled() && !field.isReadOnly()
                    && (field.isRequired() || field.isRequiredIndicatorVisible())
                    && field.isInvalid();
        }
        return false;
    }

    private void setRequiredText(Component component) {
        if (component instanceof TextField field) {
            field.setErrorMessage("Required Field");
        }
    }

    private boolean isRequiredField(Component component) {
        if (component instanceof TextField field) {
            return field.isVisible() && field.isEnabled() && !field.isReadOnly()
                    && (field.isRequired() || field.isRequiredIndicatorVisible());
        }
        if (component instanceof AbstractSinglePropertyField<?, ?> field ) {
            return field.isVisible() && field.isEnabled() && !field.isReadOnly()
                    && field.isRequiredIndicatorVisible();
        }
        return false;
    }

    public void resetForm() {
        getChildren().filter(AbstractSinglePropertyField.class::isInstance)
                .forEach(field -> ((AbstractSinglePropertyField<?, ?>) field).clear());
    }

    protected Optional<WrappedHttpSession> getSession() {
        if (VaadinService.getCurrentRequest() == null
                || VaadinService.getCurrentRequest().getWrappedSession() == null) {
            return Optional.empty();
        }
        return Optional.of((WrappedHttpSession) VaadinService.getCurrentRequest().getWrappedSession());
    }

    protected Boolean getBooleanAttribute(String attributeName) {
        return getSession()
                .map(session -> (Boolean) session.getAttribute(attributeName))
                .orElse(Boolean.FALSE);
    }

    protected Optional<?> getAttribute(String attributeName) {
        Optional<WrappedHttpSession> session = getSession();
        if (session.isEmpty()) {
            return Optional.empty();
        }
        return session.map(wrappedHttpSession -> wrappedHttpSession.getAttribute(attributeName));
    }

    protected void setAttribute(String attributeName, Object attributeValue) {
        getSession()
                .ifPresent(session -> session.setAttribute(attributeName, attributeValue));
    }
}
