package com.green.yp.subscriber.ui.components;

import com.green.yp.api.apitype.reference.LineOfBusinessApi;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;

public class LineOfBusinessDiv extends Div {
    public LineOfBusinessDiv(LineOfBusinessApi lineOfBusiness) {
        VerticalLayout layout = new VerticalLayout();
        H5 lobHeader = new H5(lineOfBusiness.lineOfBusiness());
        lobHeader.getStyle().setTextAlign(Style.TextAlign.JUSTIFY);
        lobHeader.getStyle().setColor("#106478");
        layout.add(lobHeader);

        Paragraph paragraph = new Paragraph();
        layout.add(paragraph);
        paragraph.add(lineOfBusiness.description());
        add(layout);
    }
}
