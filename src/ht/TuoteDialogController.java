package ht;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rekisteri.Tuote;

/**
 * Kontrolleri tuotteen ominaisuuksille
 * 
 * @author joonas uusnäkki & asla paakkinen
 * @version 22.4.2020
 *
 */
public class TuoteDialogController implements ModalControllerInterface<Tuote>,Initializable {
    
    @FXML private Label labelVirheMuokkaus;
    
    @FXML private TextField editNimi;

    @FXML private TextField editValmistaja;

    @FXML private TextField editVuosi;

    @FXML private TextField editHinta;

    @FXML private TextField editMaara;

    @FXML private TextField editKP;
    
    @FXML private void keyOKDialog() {
      if ( tuoteKohdalla != null && tuoteKohdalla.getNimi().trim().equals("")) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirheMuokkaus);
    }
    @FXML private void keyPeruuta() {
        tuoteKohdalla = null;
        ModalController.closeStage(labelVirheMuokkaus);
    }
    
    
    @FXML private void keyLisaaCancel() {
        tuoteKohdalla = null;
        ModalController.closeStage(labelVirheMuokkaus);
    }
    
    @FXML private void keyLisaaTuote() {
        ModalController.closeStage(labelVirheMuokkaus);
    }
    
     @Override
     public void initialize(URL arg0, ResourceBundle arg1) {
       alusta();
         
     }     
 //===========================================================================================================================    
 // Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia =============================================================
    private Tuote tuoteKohdalla;
    private TextField edits[];
    
    
   /**
   * Tekee tarvittavat alustukset
   */
   protected void alusta() {
       edits = new TextField[] {editNimi, editValmistaja, editVuosi, editHinta, editMaara, editKP };
       int i = 0;
       for (TextField edit : edits) {
           final int k = ++i;
           edit.setOnKeyReleased( e -> kasitteleMuutosTuotteeseen(k, (TextField)(e.getSource())));
       }
   }
   /**
    * Käsitellään tuotteeseen tullut muutos
    * @param edit muuttunut kenttä
    */
   private void kasitteleMuutosTuotteeseen(int k, TextField edit) {
       if (tuoteKohdalla == null) return;
       String s = edit.getText();
       String virhe = null;
       switch (k) {
          case 1 : virhe = tuoteKohdalla.setNimi(s); break;
          case 2 : virhe = tuoteKohdalla.setValmistaja(s); break;
          case 3 : virhe = tuoteKohdalla.setVuosi(s); break;
          case 4 : virhe = tuoteKohdalla.setHinta(s); break;
          case 5 : virhe = tuoteKohdalla.setMaara(s); break;
          case 6 : virhe = tuoteKohdalla.setKP(s); break;
          default:
       }
       if (virhe == null) {
           Dialogs.setToolTipText(edit,"");
           edit.getStyleClass().removeAll("virhe");
           naytaVirhe(virhe);
       } else {
           Dialogs.setToolTipText(edit,virhe);
           edit.getStyleClass().add("virhe");
           naytaVirhe(virhe);
       }
   }

   private void naytaVirhe(String virhe) {
       if ( virhe == null || virhe.isEmpty() ) {
           labelVirheMuokkaus.setText("");
           labelVirheMuokkaus.getStyleClass().removeAll("virhe");
           return;
       }
       labelVirheMuokkaus.setText(virhe);
       labelVirheMuokkaus.getStyleClass().add("virhe");
   }


    @Override
    public Tuote getResult() {
        return tuoteKohdalla;
    }

    @Override
    public void handleShown() {
        // if (tuoteKohdalla == null) return;
        edits[5].requestFocus();
        
    }
    @Override
    public void setDefault(Tuote oletus) {
        tuoteKohdalla = oletus;
        naytaTuote(tuoteKohdalla);
    }
    /**
     * Näytetään tuotteen tiedot TextField komponentteihin
     * @param edits taulukko, jossa tekstikenttiä 
     * @param tuote näytettävä tuote
     */
    public static void naytaTuote(TextField[] edits, Tuote tuote) {
        if (tuote == null) return;
        edits[0].setText(tuote.getNimi());
        edits[1].setText(tuote.getValmistaja());
        edits[2].setText(tuote.getVuosi());
        edits[3].setText(tuote.getHinta());
        edits[4].setText(tuote.getMaara());
        edits[5].setText(tuote.getKP());
    }
    /**
     * Näytetään tuotteen tiedot TextField komponentteihin
     * @param tuote näytettävä tuote
     */
    public void naytaTuote(Tuote tuote) {
        naytaTuote(edits, tuote);
       }

    /**
     * Luodaan tuotteen kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @return null jos painetaan Cancel, muuten täytetty tietue
     */

    public static Tuote kysyTuote(Stage modalityStage, Tuote oletus) {
        return ModalController.showModal(
                TuoteDialogController.class.getResource("tuotteenTiedot.fxml"),
                "Tuotteen tiedot",
                modalityStage, oletus, null 
            );

        
    }
}
