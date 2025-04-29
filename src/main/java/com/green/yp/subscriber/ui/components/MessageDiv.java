package com.green.yp.subscriber.ui.components;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MessageDiv extends Div {

    private final H5 h5title = new H5("");
    private final Paragraph paragraphMessage = new Paragraph();

    public MessageDiv() {
        VerticalLayout layout = new VerticalLayout();
        add(layout);
        layout.setWidth(95, Unit.PERCENTAGE);

        layout.add(h5title);
        h5title.getStyle().setColor("#C18D71");
        layout.add(paragraphMessage);

        getStyle().set("border-radius", "20px");
        getStyle().setBackground("#106478");
        getStyle().setColor("#F7F7D8");
        setVisible(false);
    }

    public MessageDiv(String title, String message) {
        this();
        h5title.setText(title);
        paragraphMessage.setText(message);
    }

    public void showMessage(String title, String message) {
        super.setVisible(true);
        h5title.setText(title);
        paragraphMessage.setText(message);
    }

  @Override
  public void setVisible(boolean visible) {
        if (Boolean.FALSE.booleanValue() == visible) {
            h5title.setText("");
            paragraphMessage.setText("");
        }
        super.setVisible(visible);
    }
}
