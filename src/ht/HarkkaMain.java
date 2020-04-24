package ht;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import rekisteri.Rekisteri;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 *Pääohjelma ohjelman käynnistämiseksi
 * 
 * @author joonas uusnäkki & asla paakkinen
 * @version 22.4.2020
 *
 */
public class HarkkaMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("tuote.fxml"));
            final Pane root = ldr.load();
            final TuoteGUIController harkkaCtrl = (TuoteGUIController) ldr.getController();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("harkka.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Harkka");
            
            primaryStage.setOnCloseRequest((event) -> {
                if ( ! harkkaCtrl.voikoSulkea() ) event.consume();
            });
            
            Rekisteri rekisteri = new Rekisteri();
            harkkaCtrl.setRekisteri(rekisteri);
            
           
            if ( !harkkaCtrl.avaa() ) Platform.exit();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        primaryStage.show();
    }

    /**
     * Käynnistetään käyttöliittymä
     * @param args komentorivin parametrit
     */
    public static void main(String[] args) {
        launch(args);
    }
}