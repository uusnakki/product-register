package ht;

import java.io.PrintStream;




import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import rekisteri.Rekisteri;
import rekisteri.SailoException;
import rekisteri.Tuote;
import rekisteri.Tuoteryhma;

/**
 * Pääkontrolleriluokka käyttöliittymän tapahtumiin
 * 
 * @author joonas uusnäkki & asla paakkinen
 * @version 22.4.2020
 * 
 */
public class TuoteGUIController implements Initializable, ModalControllerInterface<Tuoteryhma>  {
    @FXML private ListChooser<Tuote> chooserLista;
    @FXML private ScrollPane panelTuote;
    @FXML private TextField hakuehto;
    @FXML private ComboBoxChooser<Tuote> cbKentat;
    @FXML private TextField editNimi;
    @FXML private TextField editValmistaja;
    @FXML private TextField editVuosi;
    @FXML private TextField editHinta;
    @FXML private TextField editMaara;
    @FXML private TextField editKP;
    @FXML private Label labelVirhe;
    @FXML private StringGrid<Tuoteryhma> tableTuoteryhmat;
    @FXML private ComboBoxChooser<Tuoteryhma> cbRyhmat;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
        
    }
   
    @FXML private void handleHakuehto() {
        hae(0); 
    }

    @FXML private void keyLisaa() {
        uusiTuote();
        uusiTuoteryhma(); 
    }
    @FXML void keyPoistaTuote() {
        poistaTuote();
        poistaTuoteryhma();
    }
    @FXML void keyMuokkaa() {
     muokkaa();     
    }
    @FXML void keyTallenna() {
        tallenna();
        Dialogs.showMessageDialog("Tallennettu!");
    }
   
     @FXML void keyLopeta() {
       tallenna();
       Dialogs.showMessageDialog("Olet jumissa ohjelmassamme :)");
       Platform.exit();
   }

  //====================================================================================================================================   
 // Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia =======================================================================   
    private Rekisteri rekisteri = new Rekisteri();
    private Tuote tuoteKohdalla;
    private TextField[] edits;
    /**
     * 
     */
    public String rekisterinNimi = "urheilukauppa";
    
    private static Tuote aputuote = new Tuote(); 
    
    /**
     * Tekee tarvittavat muut alustukset. Luodaan tekstikenttä,
     * johon voidaan tulostaa tuotteiden tiedot.
     * Alustetaan myös tuotelistan kuuntelija 
     */
    private void alusta() {
        panelTuote.setFitToHeight(true);
        
        chooserLista.clear();
        chooserLista.addSelectionListener(e -> naytaTuote());
        
        edits = new TextField[] {editNimi, editValmistaja, editVuosi, editHinta, editMaara, editKP };
        
    }
    /**
     * Näyttää listalta valitun tuotteen tiedot tekstikenttään.
     */
    private void naytaTuote() {
         tuoteKohdalla = chooserLista.getSelectedObject();

        if (tuoteKohdalla == null) return;
        
        TuoteDialogController.naytaTuote(edits, tuoteKohdalla);
        naytaTuoteryhma(tuoteKohdalla);
        
       }
   
    private void naytaTuoteryhma(Tuote tuote)   {
        tableTuoteryhmat.clear();
        if ( tuote == null ) return;
        
        List<Tuoteryhma> ryhmat = rekisteri.annaTuoteryhmat(tuote);
        if ( ryhmat.size() == 0 ) return;
        for (Tuoteryhma tuo: ryhmat)
            tableTuoteryhmat.add(tuo,tuo.getRyhmaNimi());

    }
    private void poistaTuote() {
              Tuote tuote = tuoteKohdalla;
              if ( tuote == null ) return;
              if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko tuote: " + tuote.getNimi(), "Kyllä", "Ei") )
                  return;
              rekisteri.poista(tuote);
              int index = chooserLista.getSelectedIndex();
              hae(0);
              chooserLista.setSelectedIndex(index);
          }
   private void poistaTuoteryhma() {
           int rivi = tableTuoteryhmat.getRowNr();
           if ( rivi < 0 ) return;
           Tuoteryhma ryhma = tableTuoteryhmat.getObject();
           if ( ryhma == null ) return;
           rekisteri.poistaTuoteryhma(ryhma);
           naytaTuoteryhma(tuoteKohdalla);
           int ryhmia = tableTuoteryhmat.getItems().size(); 
           if ( rivi >= ryhmia ) rivi = ryhmia -1;
           tableTuoteryhmat.getFocusModel().focus(rivi);
           tableTuoteryhmat.getSelectionModel().select(rivi);
       }
   
    @SuppressWarnings("unused")
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }

    /**
     * Tulostaa tuotteen tiedot
     * @param os tietovirta johon tulostetaan
     * @param t tulostettava tuote
     */
    public void tulosta(PrintStream os, final Tuote t) {
        os.println("----------------------------------------------");
        t.tulosta(os);
        os.println("----------------------------------------------");
        List<Tuoteryhma> ryhmat = rekisteri.annaTuoteryhmat(t);   
        
        for (Tuoteryhma har:ryhmat)
            har.tulosta(os);  
    }

    /**
     * Tarkistaa onko tallennus tehty
     * @return true jos voidaan sulkea, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }
    /**
     * Tietojen tallennustoiminto
     */
    private void tallenna() {
        try {
            rekisteri.tallenna();
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Tallennukessa ilmeni virhe: " + e.getMessage());
        }
    }
    
    private void muokkaa() {
        if ( tuoteKohdalla == null ) return; 
        try { 
            Tuote tuote; 
            tuote = TuoteDialogController.kysyTuote(null, tuoteKohdalla.clone()); 
            if ( tuote == null ) return; 
            rekisteri.korvaaTaiLisaa(tuote); 
            hae(tuote.getTunnusNro()); 
        } catch (CloneNotSupportedException e) { 
            // 
        } catch (SailoException e) { 
            Dialogs.showMessageDialog(e.getMessage()); 
        } 

    }
    /**
     * Luo uuden tuotteen
     */
    private void uusiTuote() {
        Tuote uusi = new Tuote();
        
        uusi = TuoteDialogController.kysyTuote(null, uusi);
        if(uusi == null) return;
        uusi.rekisteroi();
      
        try {
            rekisteri.lisaaTuote(uusi);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
        hae(uusi.getTunnusNro());
    }
    /** 
     * Luo uuden tuoteryhmän 
     */ 
    public void uusiTuoteryhma() { 
        tuoteKohdalla = chooserLista.getSelectedObject();
        
        if ( tuoteKohdalla == null ) return;
        Tuoteryhma tuo = new Tuoteryhma();  
        tuo.rekisteroi();
        tuo.setRyhmaNimi(cbRyhmat.getSelectedText());
       // tuo.setRyhmaNimi(cbRyhmat.getRivit().toString());
        tuo.taytaRyhma(tuoteKohdalla.getTunnusNro());  
        try {
            rekisteri.lisaaTuoteryhma(tuo);  
            hae(tuoteKohdalla.getTunnusNro());          
           
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Tuoteryhmän luomisessa ongelmia! " + e.getMessage());
        }
      } 

    /**
     * Hakee tuotteiden tiedot listaan
     * @param jnro tuotteen numero, joka aktivoidaan haun jälkeen
     */
    private void hae(int jnr) {
        int jnro = jnr; // jnro jäsenen numero, joka aktivoidaan haun jälkeen 
        if ( jnro <= 0 ) { 
            Tuote kohdalla = tuoteKohdalla; 
            if ( kohdalla != null ) jnro = kohdalla.getTunnusNro(); 
        }
        int k = cbKentat.getSelectionModel().getSelectedIndex() + aputuote.ekaKentta(); 
        String ehto = hakuehto.getText(); 
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 

        chooserLista.clear();

        int index = 0;
        
        Collection<Tuote> tuotteet;
        try {
            tuotteet = rekisteri.etsi(ehto, k);
            int i = 0;
            for (Tuote tuo:tuotteet) {
                if (tuo.getTunnusNro() == jnro) index = i;
                chooserLista.add(tuo.getNimi(), tuo);
                i++;
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tuotteen hakemisessa ongelmia! " + ex.getMessage());
        }
        chooserLista.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää tuotteen
    }

    /**
     * Alustaa rekisterin ja menee aliohjelmaan, jossa näytetään tuote
     * @param r Rekisteri jota käytetään tässä käyttöliittymässä
     */
    public void setRekisteri(Rekisteri r) {
        this.rekisteri = r;
        naytaTuote(); 
        
    }
    private void setTitle(String title) {
        ModalController.getStage(hakuehto).setTitle(title);
    }
    /**
     * Alustaa rekisterin lukemalla sen valitun nimisestä tiedostosta
     * @param nimi tiedosto josta kerhon tiedot luetaan
     * @return null jos onnistuu, muuten virhe tekstinä
     */

    protected String lueTiedosto(String nimi) {
        rekisterinNimi = nimi;
        setTitle("Rekisteri - " + rekisterinNimi);
        try {
            rekisteri.lueTiedostosta(nimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }

    }
        /**
         * Kysytään tiedoston nimi ja luetaan se
         * @return true jos onnistui, false jos ei
         */
        public boolean avaa() {
            String uusinimi = EtuGUIController.kysyNimi(null, rekisterinNimi);
            if (uusinimi == null) return false;
            lueTiedosto(uusinimi);
            return true;
        }

        

        @Override
        public void handleShown() {
            //
        }

       
        @Override
        public Tuoteryhma getResult() {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public void setDefault(Tuoteryhma arg0) {
            // TODO Auto-generated method stub
            
        }
  
}