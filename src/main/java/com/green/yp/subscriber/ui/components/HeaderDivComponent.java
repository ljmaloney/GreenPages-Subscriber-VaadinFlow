package com.green.yp.subscriber.ui.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;

public class HeaderDivComponent extends Header {

    //#106478 - background
    //#F7F7D8 -- text
    //complement -- #C18D71

    //
    public HeaderDivComponent() {
        Div div = new Div(new Image("images/subscriber_banner_green.png",
                "GreenPages: Explore the green marketplace"));
        add(div);
        div.getStyle().setBackground("#106478");
        add(new MenuBarDiv());
//        div.getStyle().set("background", "images/subscriber_banner_green.png");
//        add(new LoginStatusDiv());
    }
}
