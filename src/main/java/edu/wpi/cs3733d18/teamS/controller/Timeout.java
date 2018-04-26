package edu.wpi.cs3733d18.teamS.controller;

import edu.wpi.cs3733d18.teamS.internationalization.AllText;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * Timeout causes a screen to revert to home after a certain period of time.
 * @author Matthew McMillan
 * @author Danny Sullivan
 * @version %I%, %G%
 *
 */


public class Timeout {

    static int sleep_time = 60000; //timeout after 60 seconds of inactivity
    static DateTime last_action = DateTime.now();
    static EventHandler<MouseEvent> mousePressed = event -> {
        last_action = DateTime.now();
        //System.out.println("Mouse press");
    };
    static EventHandler<KeyEvent> keyPressed = event -> {
        last_action = DateTime.now();
        //System.out.println("Key press");
    };
    private static boolean kill;
    private static Thread timeout_thread;

    public static void addListenersToScene(Scene scene) {
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressed);
    }

    public static void stop() {
        kill = true;
        timeout_thread.interrupt();
    }

    public static void start() {
        kill = false;
        timeout_thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(sleep_time);
                    while (!kill) {
                        if ((DateTime.now().getMillis() - last_action.getMillis()) > sleep_time) {
                            AllText.changeLanguage("en");
                            Platform.runLater(() -> Main.switchScenes("Home Page", "/HomePage.fxml"));
                            Thread.sleep(sleep_time);
                        } else {
                            long new_sleep_time = sleep_time - (DateTime.now().getMillis() - last_action.getMillis());
                            if (new_sleep_time <= 0) {
                                new_sleep_time = 10000;
                            }
                            Thread.sleep(new_sleep_time);
                        }
                    }
                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    Thread.currentThread().interrupt();
                }
            }
        };
        timeout_thread.start();
    }
}
