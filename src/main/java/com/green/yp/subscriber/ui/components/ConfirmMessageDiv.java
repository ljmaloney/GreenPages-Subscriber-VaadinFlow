package com.green.yp.subscriber.ui.components;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConfirmMessageDiv extends Div {
    private final H5 h5title = new H5("");
    private final Paragraph paragraphMessage = new Paragraph();
    private final Checkbox confirmCheckBox;

    public ConfirmMessageDiv() {

        VerticalLayout layout = new VerticalLayout();
        add(layout);
        layout.setWidth(95, Unit.PERCENTAGE);

        layout.add(h5title);
        h5title.getStyle().setColor("#C18D71");
        layout.add(paragraphMessage);

        confirmCheckBox = new Checkbox("I confirm the address entered is valid");
        layout.add(confirmCheckBox);

        getStyle().set("border-radius", "20px");
        getStyle().setBackground("#106478");
        getStyle().setColor("#F7F7D8");
        setVisible(false);
    }

    public void showConfirmMessage(String title, String message) {
        h5title.setText(title);
        paragraphMessage.setText(message);
        setVisible(true);
    }

    public Boolean isConfirmed() {
        return isVisible() && confirmCheckBox.getValue();
    }
}
