package com.msl.myserieslist.controllers.vaadin;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.msl.myserieslist.entities.Series;
import com.msl.myserieslist.services.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


@Tag("products-search")
@JsModule("./src/products-search.js")
public class SeriesSearch extends PolymerTemplate<SeriesSearch.SeriesSearchModel> {
    @Autowired
    private SeriesService seriesService;

    @Id("searchButton")
    private Button searchButton;
    @Id("seriesNameTextField")
    private TextField productNameTextField;
    @Id("searchResults")
    private Grid<Series> searchResults;


    public SeriesSearch() {
        // building search bar
        searchResults.addColumn(Series::getName).setHeader("Name");
        searchResults.addColumn(Series::getRating).setHeader("Barcode");
        searchButton.addClickListener(buttonClickEvent -> {
            String searchText = productNameTextField.getValue();
            List<Series> series = seriesService.showSeriesByName(searchText);
            searchResults.setItems(series);
        });
        // building result table
        searchResults.setSelectionMode(Grid.SelectionMode.SINGLE);
        searchResults.addItemClickListener(event -> {
            Dialog dialog = new Dialog();
            dialog.add(new H3("You have selected this series:"));
            dialog.add(new Label(searchResults.getSelectedItems().toArray()[0].toString()));
            dialog.setWidth("400px");
            dialog.setHeight("150px");
            dialog.open();
        });
    }

    public interface SeriesSearchModel extends TemplateModel {}
}
