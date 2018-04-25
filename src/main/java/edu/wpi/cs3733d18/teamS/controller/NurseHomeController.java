package edu.wpi.cs3733d18.teamS.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class NurseHomeController {


    // ---------------- DRAW BLOOD --------------- //

    @FXML
    private Rectangle syringe_rect;

    @FXML
    private ImageView syringe_image;

    @FXML
    public void onSyringeClick() {
        Main.switchScenes("Draw Blood", "/DrawBlood.fxml");
    }

    // ---------------- VITALS ------------------- //

    @FXML
    private Rectangle lungs_rect;

    @FXML
    private ImageView lungs_image;

    @FXML
    public void onVitalsClick() {
        Main.switchScenes("Check Vitals", "/CheckVitals.fxml");
    }

    // ---------------- ADMINISTER MEDICATION ------------------- //

    @FXML
    private Rectangle pill_rect;

    @FXML
    private ImageView pill_image;

    @FXML
    public void onPillClick() {
        Main.switchScenes("Administer Medication", "/AdministerMeds.fxml");
    }

    public void changeColor() {
        // empty for now
    }

    public void resetColor() {
        // empty for now
    }

    @FXML
    private JFXButton back_btn;

    @FXML
    public void onBackClick() {
        Main.switchScenes("Brigham and Women's", "/AdminPage.fxml");
    }
}
