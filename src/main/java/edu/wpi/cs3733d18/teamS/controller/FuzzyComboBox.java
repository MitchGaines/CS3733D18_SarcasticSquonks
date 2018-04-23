package edu.wpi.cs3733d18.teamS.controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import java.util.stream.Stream;

public class FuzzyComboBox {

    /**
     * Stores JavaFX ComboBox which is used in scenes.
     */
    private ComboBox<String> combo_box;

    /**
     * Stores Node items contained in the ComboBox.
     */
    private ObservableList<edu.wpi.cs3733d18.teamS.data.Node> items;

    /**
     * Stores String versions of Node items in the SAME ORDER as the items list.
     */
    private ObservableList<String> item_names;

    /**
     * Stores currently visible items based upon matching text.
     */
    private ObservableList<String> visible_items;

    /**
     * Constructs a FuzzyComboBox with the given JavaFX ComboBox and list of Node items.
     *
     * @param combo_box JavaFX ComboBox with generic type String which will be used in the scene.
     * @param items list of Node items to be searched through.
     */
    public FuzzyComboBox(ComboBox<String> combo_box, ObservableList<edu.wpi.cs3733d18.teamS.data.Node> items) {
        this.combo_box = combo_box;
        this.items = items;

        combo_box.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                combo_box.getEditor().end();
            }
            else if (!event.getCode().isArrowKey()) {
                updateVisibleItems();
            }
        });

        combo_box.getEditor().textProperty().addListener((ObservableValue<? extends String> observable, String old_value, String new_value) -> {
            if (new_value.equals("")) {
                updateVisibleItems();
            }
        });

        // Populate item_names and visible_items with strings based on items
        item_names = FXCollections.observableArrayList();
        visible_items = FXCollections.observableArrayList();
        for (edu.wpi.cs3733d18.teamS.data.Node item : items) {
            item_names.add(item.toString());
            visible_items.add(item.toString());
        }

        items.addListener((ListChangeListener.Change<? extends edu.wpi.cs3733d18.teamS.data.Node> change) -> {
            // Re-populate item_names and visible_items should items be modified
            item_names = FXCollections.observableArrayList();
            visible_items = FXCollections.observableArrayList();
            for (edu.wpi.cs3733d18.teamS.data.Node item : change.getList()) {
                item_names.add(item.toString());
                visible_items.add(item.toString());
            }
        });

        Label placeholder = new Label("No results found.");
        combo_box.setPlaceholder(placeholder);
        for (edu.wpi.cs3733d18.teamS.data.Node item : items) {
            combo_box.getItems().add(item.toString());
        }
    }

    /**
     * getValue
     * Returns the current Node object in items represented by the string in the ComboBox's text editor OR null if the
     * string in the editor does not correspond to any Node object in items.
     *
     * @return the current Node object in items represented by the string in the ComboBox's text editor OR null if the
     * string in the editor does not correspond to any Node object in items.
     */
    public edu.wpi.cs3733d18.teamS.data.Node getValue() {
        int item_name_index = item_names.indexOf(combo_box.getValue());
        if (item_name_index != -1) {
            return items.get(item_name_index);
        } else {
            return null;
        }
    }

    /**
     * updateVisibleItems
     * Called when the user changes the text in the ComboBox's field to update the options listed in the
     * ComboBox to include only those that contain a substring of the user input.
     */
    private void updateVisibleItems() {
        String user_text = combo_box.getEditor().getText().toLowerCase();
        int old_vis_items_size = visible_items.size();
        combo_box.show();
        visible_items = FXCollections.observableArrayList();
        visible_items.addAll(item_names);
        if (user_text.length() != 0) {
            Stream<String> item_names_stream = item_names.stream();
            item_names_stream.filter(el -> !el.toLowerCase().contains(user_text)).forEach(visible_items::remove);
        }
        combo_box.setItems(visible_items);

        if (visible_items.size() != old_vis_items_size) {
            combo_box.hide();
            combo_box.show();
        }
    }

    /**
     * getComboBox
     * Returns the JavaFX ComboBox used by this FuzzyComboBox.
     *
     * @return the JavaFX ComboBox used by this FuzzyComboBox.
     */
    public ComboBox<String> getComboBox() {
        return combo_box;
    }

    /**
     * setComboBox
     * Sets the JavaFX ComboBox used by this FuzzyComboBox.
     *
     * @param combo_box the JavaFX ComboBox to be assigned.
     */
    public void setComboBox(ComboBox<String> combo_box) {
        this.combo_box = combo_box;
    }

    /**
     * getItems
     * Returns the list of items that the FuzzyComboBox can search through.
     *
     * @return the list of items that the FuzzyComboBox can search through.
     */
    public ObservableList<edu.wpi.cs3733d18.teamS.data.Node> getItems() {
        return items;
    }

    /**
     * setItems
     * Sets the list of items that the FuzzyComboBox will search through.
     *
     * @param items a list of items for the FuzzyComboBox to search through.
     */
    public void setItems(ObservableList<edu.wpi.cs3733d18.teamS.data.Node> items) {
        this.items = items;
        item_names = FXCollections.observableArrayList();
        // Populate item_names
        for (edu.wpi.cs3733d18.teamS.data.Node item : items) {
            item_names.add(item.toString());
        }
    }
}
