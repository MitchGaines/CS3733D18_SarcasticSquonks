package edu.wpi.cs3733d18.teamS.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;

import java.util.ArrayList;
import java.util.stream.Stream;

public class AutocompleteComboBox<T> {

    /**
     * Stores JavaFX ComboBox which is used in scenes.
     */
    private ComboBox<String> combo_box;

    /**
     * Stores items contained in the ComboBox.
     */
    private ObservableList<T> items;

    /**
     * Stores String versions of items in the SAME ORDER as the items list.
     */
    private ObservableList<String> item_names = FXCollections.observableArrayList();

    /**
     * Stores currently visible items based upon matching text
     */
    private ObservableList<String> visible_items = FXCollections.observableArrayList();

    private boolean list_updatable = true;

    /**
     * Constructs an AutocompleteComboBox with the given JavaFX ComboBox and list of items.
     *
     * @param combo_box JavaFX ComboBox with generic type String which will be used in the scene.
     * @param items list of items to be searched through.
     */
    public AutocompleteComboBox(ComboBox<String> combo_box, ObservableList<T> items) {
        this.combo_box = combo_box;
        this.items = items;

        // Add listener to ComboBox text field
        combo_box.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String old_value, String new_value) {
                updateVisibleItems(old_value, new_value);
            }
        });

        combo_box.selectionModelProperty().addListener(new ChangeListener<SingleSelectionModel<String>>() {
            @Override
            public void changed(ObservableValue<? extends SingleSelectionModel<String>> observable, SingleSelectionModel<String> oldValue, SingleSelectionModel<String> newValue) {
                if (!newValue.isEmpty()) {
                    list_updatable = false;
                } else {
                    list_updatable = true;
                }
            }
        });

        // Populate item_names and visible_items with strings based on items
        item_names = FXCollections.observableArrayList();
        visible_items = FXCollections.observableArrayList();
        for (T item : items) {
            item_names.add(item.toString());
            visible_items.add(item.toString());
        }

        items.addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(Change<? extends T> c) {
                // Re-populate item_names and visible_items with strings based on items if items is modified
                item_names = FXCollections.observableArrayList();
                visible_items = FXCollections.observableArrayList();
                for (T item : items) {
                    item_names.add(item.toString());
                    visible_items.add(item.toString());
                }
            }
        });
    }

    /**
     * Returns the current object of type T in items represented by the string in the ComboBox's text editor OR null if the
     * string in the editor does not correspond to any object T in items.
     *
     * @return the current object of type T in items represented by the string in the ComboBox's text editor OR null if the
     * string in the editor does not correspond to any object T in items.
     */
    public T getValue() {
        int item_name_index = item_names.indexOf(combo_box.getValue());
        if (item_name_index != -1) {
            return items.get(item_name_index);
        } else {
            return null;
        }
    }

    /**
     * Called when the user changes the text in the ComboBox's field. Updates the options listed in the
     * ComboBox to include only those that contain a substring of the user input.
     *
     * @param old_value the String that was in the text field before the user modified it.
     * @param new_value the new String in the text field after modification.
     */
    private void updateVisibleItems(String old_value, String new_value) {
        if (list_updatable) {
            combo_box.show();
            System.out.println("Updating...");
            if (old_value == null || new_value == null) {
                return;
            }
            if (new_value.length() == 0) {
                visible_items = FXCollections.observableArrayList();
                visible_items.addAll(item_names);
            } else {
                ArrayList<String> toRemove = new ArrayList<>();
                String user_text = combo_box.getEditor().getText().toLowerCase();
                if (old_value.length() < new_value.length()) {
                    Stream<String> visible_items_stream = visible_items.stream();
                    visible_items_stream.filter(el -> !el.toLowerCase().contains(user_text)).forEach(toRemove::add);
                } else {
                    visible_items = FXCollections.observableArrayList();
                    visible_items.addAll(item_names);
                    Stream<String> item_names_stream = item_names.stream();
                    item_names_stream.filter(el -> !el.toLowerCase().contains(user_text)).forEach(toRemove::add);
                }
                System.out.println(toRemove);
                visible_items.removeAll(toRemove);
            }
            combo_box.setItems(visible_items);

            // Resize drop down
            int new_visible_size = visible_items.size() > 10 ? 10 : visible_items.size();
            if (new_visible_size != combo_box.getVisibleRowCount()) {
                combo_box.hide();
                combo_box.setVisibleRowCount(new_visible_size);
                combo_box.show();
            }
        }
    }

    /**
     * Returns the JavaFX ComboBox used by this AutocompleteComboBox.
     *
     * @return the JavaFX ComboBox used by this AutocompleteComboBox.
     */
    public ComboBox<String> getComboBox() {
        return combo_box;
    }

    /**
     * Sets the JavaFX ComboBox used by this AutocompleteComboBox.
     *
     * @param combo_box the JavaFX ComboBox to be assigned.
     */
    public void setComboBox(ComboBox<String> combo_box) {
        this.combo_box = combo_box;
    }

    /**
     * Returns the list of items that the AutocompleteComboBox can search through.
     *
     * @return the list of items that the AutocompleteComboBox can search through.
     */
    public ObservableList<T> getItems() {
        return items;
    }

    /**
     * Sets the list of items that the AutocompleteComboBox will search through.
     *
     * @param items a list of items for the AutocompleteComboBox to search through.
     */
    public void setItems(ObservableList<T> items) {
        this.items = items;
        item_names = FXCollections.observableArrayList();
        // Populate item_names
        for (T item : items) {
            item_names.add(item.toString());
        }
    }
}
