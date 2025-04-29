package com.green.yp.subscriber.ui;

import com.green.yp.api.apitype.reference.LineOfBusinessApi;
import com.green.yp.subscriber.service.reference.ReferenceService;
import com.green.yp.subscriber.ui.components.LineOfBusinessDiv;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Route("lineOfBusiness")
@Slf4j
@AnonymousAllowed
@PermitAll
public class LineOfBusinessView extends AbstractVerticalLayout {

    private static final String LOB_LOADED = "lobLoaded";

    @NonNull
    private final ReferenceService referenceService;

    private final Div lineOfBusinessDiv = new Div();

    public LineOfBusinessView(@NonNull @NotNull @Autowired ReferenceService referenceService) {
        this.referenceService = referenceService;
    }

    @Override
    protected String getHeaderText() {
        return "Lines of Business";
    }

    @Override
    public void initializePage(Div pageContentDiv) {
        Paragraph paragraph = new Paragraph();
        add(paragraph);

        add(lineOfBusinessDiv);

        log.debug("Created home page view");

    }

    public void onAttach(AttachEvent attachEvent) {
        log.debug("LineOfBusinessView:OnAttach called ... show Lines of Business from DB");

        Optional<List<LineOfBusinessApi>> lobOptional =
                (Optional<List<LineOfBusinessApi>>) getAttribute("linesOfBusiness");

        List<LineOfBusinessApi> lineOfBusinessList = lobOptional.orElseGet(() -> {
            List<LineOfBusinessApi> linesOfBusiness = referenceService.getLinesOfBusiness();
            setAttribute("linesOfBusiness", linesOfBusiness);
            return linesOfBusiness;
        });

        lineOfBusinessDiv.removeAll();
        lineOfBusinessList.forEach(lob -> {
            lineOfBusinessDiv.add(new LineOfBusinessDiv(lob));
        });
        setAttribute(LOB_LOADED, true);
    }
}
