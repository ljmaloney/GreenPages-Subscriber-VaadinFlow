package com.green.yp.subscriber.ui.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CommonFooterDiv extends Div {
    public CommonFooterDiv() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new Div(new Paragraph("Copyright © 2023 Green YP.com - All Rights Reserved.")));
        add(layout);
    }
}
