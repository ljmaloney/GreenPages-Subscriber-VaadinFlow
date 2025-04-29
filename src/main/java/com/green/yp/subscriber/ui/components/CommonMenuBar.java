package com.green.yp.subscriber.ui.components;

import com.green.yp.subscriber.ui.AboutUsPageView;
import com.green.yp.subscriber.ui.LineOfBusinessView;
import com.green.yp.subscriber.ui.SubscriberHomePageView;
import com.green.yp.subscriber.ui.SubscriptionView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.server.VaadinService;
import java.util.Arrays;

public class CommonMenuBar extends MenuBar {

    private final static String CURRENT_PAGE = "SelectedMenuItem";

    public CommonMenuBar() {
        super();
        Arrays.stream(CommonMenuItem.values()).forEach(item -> {
            MenuItem menuItem = addItem(item.pageName, this::clickListener);
            menuItem.getStyle().setColor("#106478");
        });

    }

    private void clickListener(ClickEvent<MenuItem> clickEvent) {
        MenuItem menuItem = clickEvent.getSource();
        CommonMenuItem item = CommonMenuItem.getMenuItem(menuItem.getText());
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute("SelectedMenuItem", item);
        menuItem.getStyle().setTextDecoration("underline");
        menuItem.getUI()
                .ifPresent(ui -> {
                    ui.navigate(item.topLevelPageClass);
                });
    }

    public enum CommonMenuItem {
        HOME("Home", SubscriberHomePageView.class),
        LINES_OF_BUSINESS("Lines of Business", LineOfBusinessView.class),
        SUBSCRIPTIONS("Subscriptions", SubscriptionView.class),
        ABOUT_US("About Us", AboutUsPageView.class);

        String pageName;
        Class topLevelPageClass;

        CommonMenuItem(String pageName, Class clazz) {
            this.pageName = pageName;
            this.topLevelPageClass = clazz;
        }

        public static CommonMenuItem getMenuItem(String pageName) {
            return Arrays.stream(CommonMenuItem.values())
                    .filter(item -> item.pageName.equals(pageName))
                    .findFirst()
                    .get();
        }

    }
}
