package com.green.yp.subscriber.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class LocalErrorDialog extends Dialog {
    public LocalErrorDialog(String title, String errorMessage) {
        setHeaderTitle(title);

        Button closeButtonTop = new Button(new Icon("lumo", "cross"),
                (e) -> close());
        closeButtonTop.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        getHeader().add(closeButtonTop);

        Icon icon = new Icon(VaadinIcon.MINUS_CIRCLE);
        icon.setColor("#8B0000");
        add(icon, new Span(errorMessage));

        Button closeButton = new Button("Cancel", (e) -> close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        getFooter().add(closeButton);
    }
}
