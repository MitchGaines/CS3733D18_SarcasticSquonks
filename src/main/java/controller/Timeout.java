package controller;

import internationalization.AllText;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.io.IOException;

/**
 * Timeout.java
 *
 * Timeout causes a screen to revert to home after a certain period of time
 *
 * @Authors: Matthew McMillan and Danny Sullivan
 * @Date: 4/10/18
 */


public class Timeout {

    private static Thread timeout_thread = new Thread() {
        public void run() {
            try {
                Thread.sleep(sleep_time);
                while(!kill) {
                    //System.out.println(DateTime.now().getMillis());
                    //System.out.println(last_action.getMillis());
                    if ((DateTime.now().getMillis() - last_action.getMillis()) > sleep_time) {
                        AllText.changeLanguage("en");
                        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"), AllText.getBundle());
                        Platform.runLater(() -> curr_stage.setTitle("Brigham and Women's"));
                        Scene primary_scene = new Scene(root, 1200, 800);
                        addListenersToScene(primary_scene);
                        Platform.runLater(() -> curr_stage.setScene(primary_scene));
                        //last_action = DateTime.now();
                        Thread.sleep(sleep_time);
                    } else {
                        long new_sleep_time = sleep_time - (DateTime.now().getMillis() - last_action.getMillis());
                        if(new_sleep_time <= 0) {
                            new_sleep_time = 10000;
                        }
                        //System.out.println("Sleeping for " + new_sleep_time);
                        Thread.sleep(new_sleep_time);
                    }
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException io_exception) {
                io_exception.printStackTrace();
            }
        }
    };

    static DateTime last_action = DateTime.now();

    private static boolean kill;

    static final int sleep_time = 60000; //timeout after 60 seconds of inactivity
    static Stage curr_stage;

    static EventHandler<MouseEvent> mousePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            last_action = DateTime.now();
            //System.out.println("Mouse press");
        }
    };

    static EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            last_action = DateTime.now();
            //System.out.println("Key press");
        }
    };

    public static void addListenersToScene(Scene scene) {
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressed);
    }

    public static void setCurrStage(Stage stage) {
        curr_stage = stage;
    }

    public static void stop() {
        kill = true;
        timeout_thread.interrupt();
    }

    public static void start() {
        timeout_thread.start();
    }
}
