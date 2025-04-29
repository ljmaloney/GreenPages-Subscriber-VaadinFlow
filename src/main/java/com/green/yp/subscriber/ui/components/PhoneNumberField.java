package com.green.yp.subscriber.ui.components;

import com.vaadin.flow.component.KeyPressEvent;
import com.vaadin.flow.component.textfield.TextField;
import org.apache.commons.lang3.StringUtils;

public class PhoneNumberField extends TextField {

    private static final String PHONE_FORMAT_FULL = "^[+]?[(][0-9]{3}[)][-\\s.]?[0-9]{3}[-\\s.][0-9]{4,6}$";

    public PhoneNumberField(String fieldName) {
        super(fieldName);
        setPattern(PHONE_FORMAT_FULL);
        setAllowedCharPattern("[0-9()+-]");
        setMinLength(10);
        setMaxLength(18);
        addValueChangeListener(this::valueChanged);
        addKeyPressListener(this::keyPressed);
        setHelperText("Format: (123) 456-7890");
    }

    private void keyPressed(KeyPressEvent keyPressEvent) {
        if (StringUtils.isBlank(getValue())) {
            setValue("(" + keyPressEvent.getKey().toString());
            return;
        }
        if (getValue().length() == 4) {
            setValue(getValue() + ")" + keyPressEvent.getKey().toString());
        }
        if (getValue().length() == 8) {  //(123)123 has been entered
            setValue(getValue() + "-" + keyPressEvent.getKey().toString());
        }
    }

    private void valueChanged(ComponentValueChangeEvent<TextField, String> changeEvent) {
        if (StringUtils.isBlank(changeEvent.getValue())) {
            return;
        }
        String phoneNumber = changeEvent.getValue();
        if (phoneNumber.matches(PHONE_FORMAT_FULL)) {
            return;
        }
        if (phoneNumber.matches("\\d{10}")) {
            setValue(String.format("(%s) %s-%s",
                    phoneNumber.substring(0, 3),
                    phoneNumber.substring(3, 6),
                    phoneNumber.substring(6)));
        }
    }

}
