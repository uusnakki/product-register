package ht;

import fi.jyu.mit.fxgui.Dialogs;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Kontrolleriluokka etusivulle.
 * @author joonas uusnäkki & asla paakkinen
 * @version 22.4.2020
 */
public class EtuGUIController implements ModalControllerInterface<String>{
    @FXML private TextField textVastaus;
   
    private String vastaus = null;

    @FXML private void keyUusi() {
        if (textVastaus.getText().isEmpty()) {
            Dialogs.showMessageDialog("Rekisterillä tulee olla nimi!");
        }
        else {
            vastaus = textVastaus.getText();
            ModalController.closeStage(textVastaus);
        }
     }
   //================================================================================================================================== 
   // Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia ================================================================
     /**
      * Luodaan nimenkysymisdialogi ja palautetaan siihen kirjoitettu nimi tai null
      * @param modalityStage mille ollaan modaalisia, null = sovellukselle
      * @param oletus mitä nimeä näytetään oletuksena
      * @return null jos painetaan Cancel, muuten kirjoitettu nimi
      */
     public static String kysyNimi(Stage modalityStage, String oletus) {
         return ModalController.showModal(
                 EtuGUIController.class.getResource("etusivu.fxml"),
                 "Etusivu",
                 modalityStage, oletus);
     }
    
    @Override
    public String getResult() {
        return vastaus;  
    }
    @Override
    public void handleShown() {
        textVastaus.requestFocus();
    }
    @Override
    public void setDefault(String oletus) {
        textVastaus.setText(oletus);
    }

}
