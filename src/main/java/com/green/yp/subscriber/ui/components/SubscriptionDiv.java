package com.green.yp.subscriber.ui.components;

import com.green.yp.api.apitype.reference.SubscriptionApi;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class SubscriptionDiv extends Div {

    public SubscriptionDiv(SubscriptionApi subscription) {
        VerticalLayout layout = new VerticalLayout();
        add(layout);
        layout.setWidth(95, Unit.PERCENTAGE);

        H5 title = new H5(subscription.getDisplayName());
        layout.add(title);
        title.getStyle().setColor("#C18D71");

        layout.add(new Paragraph(subscription.getHtmlDescription()));

        H6 cost = new H6("Subscription Cost:");
        layout.add(cost);
        UnorderedList ul = new UnorderedList();
        ul.add(new ListItem(String.format("Monthly:   $%,.2f", subscription.getMonthlyAutopayAmount())));
        ul.add(new ListItem(String.format("Quarterly: $%,.2f", subscription.getQuarterlyAutopayAmount())));
        ul.add(new ListItem(String.format("Annual:    $%,.2f", subscription.getAnnualBillAmount())));
        layout.add(ul);

        getStyle().set("border-radius", "20px");
        getStyle().setBackground("#106478");
        getStyle().setColor("#F7F7D8");
    }
}
