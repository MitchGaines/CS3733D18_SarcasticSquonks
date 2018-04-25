package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.data.Node;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * FuzzyComboBox creates a combobox of Node objects that can sort out Nodes that do not match the text typed in the
 * associated JavaFX combobox's editor. The FuzzyComboBox checks the editor for keywords pertaining to a floor or
 * building in BWH in order to sort out all nodes not on that floor or in that building. Then it uses any outstanding
 * text to filter through the remaining nodes that have that text as a substring.
 *
 * @author Will Lucca
 * @version 1.4, 4/25/18
 */
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
     * Called when the user changes the text in the ComboBox's field to update the options listed in the
     * ComboBox to include only those that contain a substring of the user input.
     */
    private void updateVisibleItems() {
        String user_text = combo_box.getEditor().getText().toLowerCase();
        int old_vis_items_size = visible_items.size();
        combo_box.show();
        visible_items = FXCollections.observableArrayList();
        visible_items.addAll(item_names);

        // Filter by floor
        // 1, 2, 3, L1, L2
        user_text = filterFloor(user_text, new String[]{"floor 1", "floor one", "1st floor", "first floor"}, "1");
        user_text = filterFloor(user_text, new String[]{"floor 2", "floor two", "2nd floor", "second floor"}, "2");
        user_text = filterFloor(user_text, new String[]{"floor 3", "floor three", "3rd floor", "third floor"}, "3");
        user_text = filterFloor(user_text, new String[]{"floor l1", "l1", "basement 1", "basement one"}, "L1");
        user_text = filterFloor(user_text, new String[]{"floor l2", "l2", "basement 2", "basement two"}, "L2");


        // Filter by building
        // 15 Francis, 45 Francis, BTM, Shapiro, Tower
        user_text = filterBuilding(user_text, new String[]{"15 francis", "francis 15", "fifteen francis", "francis fifteen"}, "15 Francis");
        user_text = filterBuilding(user_text, new String[]{"45 francis", "francis 45", "forty-five francis", "francis forty-five", "forty five francis", "francis forty five"}, "45 Francis");
        user_text = filterBuilding(user_text, new String[]{"btm", "building for transformative medicine", "building transformative medicine", "transformative medicine"}, "BTM");
        user_text = filterBuilding(user_text, new String[]{"shapiro"}, "Shapiro");
        user_text = filterBuilding(user_text, new String[]{"tower"}, "Tower");

        if (user_text.length() != 0) {
            ArrayList<String> toRemove = new ArrayList<>();
            Stream<String> visible_items_stream = visible_items.stream();
            String user_searchable_text = user_text;
            visible_items_stream.filter(el -> !el.toLowerCase().contains(user_searchable_text)).forEach(toRemove::add);
            visible_items.removeAll(toRemove);
        }
        combo_box.setItems(visible_items);

        if (visible_items.size() != old_vis_items_size) {
            combo_box.hide();
            combo_box.show();
        }
    }

    /**
     * Returns the JavaFX ComboBox used by this FuzzyComboBox.
     *
     * @return the JavaFX ComboBox used by this FuzzyComboBox.
     */
    public ComboBox<String> getComboBox() {
        return combo_box;
    }

    /**
     * Sets the JavaFX ComboBox used by this FuzzyComboBox.
     *
     * @param combo_box the JavaFX ComboBox to be assigned.
     */
    public void setComboBox(ComboBox<String> combo_box) {
        this.combo_box = combo_box;
    }

    /**
     * Returns the list of items that the FuzzyComboBox can search through.
     *
     * @return the list of items that the FuzzyComboBox can search through.
     */
    public ObservableList<edu.wpi.cs3733d18.teamS.data.Node> getItems() {
        return items;
    }

    /**
     * Sets the list of items that the FuzzyComboBox will search through.
     *
     * @param items a list of items for the FuzzyComboBox to search through.
     */
    public void setItems(ObservableList<edu.wpi.cs3733d18.teamS.data.Node> items) {
        this.items = items;
    }

    /**
     * Removes all elements of visible_items on the specified floor if one of the keywords is a substring of str.
     *
     * @param str a String to look for keywords in.
     * @param keywords an array of Strings that are keywords pertaining to the given floor.
     * @param floor a String representing the floor being filtered for.
     * @return str if no matching keyword was found in it, and otherwise returns str with the matched keyword removed and any
     * leading or trailing whitespace trimmed off.
     */
    private String filterFloor(String str, String[] keywords, String floor) {
        for (String keyword : keywords) {
            if (str.contains(keyword)) {
                for (Node item : items) {
                    if (!item.getNodeFloor().equals(floor)) {
                        visible_items.remove(item.getLongName());
                    }
                }
                return str.replace(keyword, "").trim();
            }
        }
        return str;
    }

    /**
     * Removes all elements of visible_items in the specified building if one of the keywords is a substring of str.
     *
     * @param str a String to look for keywords in.
     * @param keywords an array of Strings that are keywords pertaining to the given building.
     * @param building a String representing the building being filtered for.
     * @return str if no matching keyword was found in it, and otherwise returns str with the matched keyword removed and any
     * leading or trailing whitespace trimmed off.
     */
    private String filterBuilding(String str, String[] keywords, String building) {
        for (String keyword : keywords) {
            if (str.contains(keyword)) {
                for (Node item : items) {
                    if (!item.getNodeBuilding().equals(building)) {
                        visible_items.remove(item.getLongName());
                    }
                }
                return str.replace(keyword, "").trim();
            }
        }
        return str;
    }
}
