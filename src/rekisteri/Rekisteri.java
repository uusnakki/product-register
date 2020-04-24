/**
 * 
 */
package rekisteri;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * - varmistaa Tuotteet ja Tuoteryhmät -luokkien yhteistoiminnasta   
 * - tallentaa rekisterin tiedostoon avustajien avulla
 * @author joonas uusnäkki & asla paakkinen
 * @version 23.4.2020
 *
 */
public class Rekisteri {
   
    Tuotteet tuotteet = new Tuotteet();
    Tuoteryhmat tuoteryhmat = new Tuoteryhmat();
    
    /**
     * ei tarvitse tehdä
     */
    public Rekisteri() {
        //muodostettu muualla
    }
    /**
     * Lisää rekisteriin uuden tuotteen
     * @param jees lisättävä tuote
     * @throws SailoException jos lisäystä ei voida tehdä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Rekisteri rekisteri = new Rekisteri();
     * Tuote a1 = new Tuote(), a2 = new Tuote();
     * a1.rekisteroi(); a2.rekisteroi();
     * rekisteri.getJasenia() === 0;
     * rekisteri.lisaa(a1); rekisteri.getTuotteita() === 1;
     * rekisteri.lisaa(a2); rekisteri.getTuotteita() === 2;
     * rekisteri.lisaa(a1); rekisteri.getTuotteita() === 3;
     * rekisteri.getTuotteita() === 3;
     * rekisteri.annaTuote(0) === a1;
     * rekisteri.annaTuote(1) === a2;
     * rekisteri.annaTuote(2) === a1;
     * rekisteri.annaTuote(3) === a1; #THROWS IndexOutOfBoundsException 
     * rekisteri.lisaa(a1); kerho.getTuotteita() === 4;
     * rekisteri.lisaa(a1); kerho.getTuotteita() === 5;
     * rekisteri.lisaa(a1);            #THROWS SailoException
     * </pre>
     */
    public void lisaaTuote(Tuote jees) throws SailoException {
        tuotteet.lisaa(jees);
    }
     /**
      * Poistetaan rekisteristä tietty tuote
     * @param tuote tuote, joka poistetaan
     * @return tuotteen tunnusnumero
     */
    public int poista(Tuote tuote) {
           if ( tuote == null ) return 0;
           int ret = tuotteet.poista(tuote.getTunnusNro()); 
           tuoteryhmat.poistaTuotteenTuoteryhmat(tuote.getTunnusNro()); 
           return ret; 
       }
    /**
     * Poistetaan tuoteryhmä rekisteristä
     * @param ryhma tuoteryhmä, joka poistetaan
     */
    public void poistaTuoteryhma(Tuoteryhma ryhma) { 
            tuoteryhmat.poista(ryhma); 
        } 
    /**
     * Lisätään uusi tuoteryhmä rekisteriin
     * @param tuo lisättävä tuoteryhmä 
     * @throws SailoException jos ei toimi
     */
    public void lisaaTuoteryhma(Tuoteryhma tuo) throws SailoException {
        tuoteryhmat.lisaa(tuo);
    }


    /**
     * Haetaan tuotteiden lukumäärä
     * @return tuotteidein lukumäärä
     */
    public int getTuotteita() {
        return tuotteet.getLkm();
    }

    /**
     * @param i paikan indeksi
     * @return tuotteen paikka
     * @throws IndexOutOfBoundsException jos indeksi on taulukon ulkopuolella
     */
    public Tuote annaTuote(int i) throws IndexOutOfBoundsException {
        return tuotteet.anna(i);
    }
    /**
     * Antaa listasta tietyn tuotteen tuoteryhmät
     * @param tuote tuote, jonka tuoteryhmät
     * @return lista tuoteryhmistä
     */
    public List<Tuoteryhma> annaTuoteryhmat(Tuote tuote) {
        return tuoteryhmat.annaTuoteryhmat(tuote.getTunnusNro());
    }
    /**
     * Tallentaa kaikki tiedostot
     * @throws SailoException jos jokin ei toimi
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            tuotteet.tallenna();
        } catch (SailoException e) {
            virhe += e.getMessage();
        }
        try {
            tuoteryhmat.tallenna();
        } catch (SailoException e) {
            virhe += e.getMessage();
            }
        if( virhe.length() > 0)
            throw new SailoException(virhe);
      }
    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "rekisteri";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        tuotteet.setTiedostonPerusNimi(hakemistonNimi + "tuotteet");
        tuoteryhmat.setTiedostonPerusNimi(hakemistonNimi + "tuoteryhmät");
    }

    /**
     * Luetaan tiedostot
     * @param nimi jota käytetään lukemisessa
     * @throws SailoException jos menee päin tuuletinta
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        tuotteet = new Tuotteet(); 
        tuoteryhmat = new Tuoteryhmat();

        setTiedosto(nimi);
        tuotteet.lueTiedostosta();
        tuoteryhmat.lueTiedostosta();
    }

    /** 
     * Korvaa tuotteen tietorakenteessa.  Ottaa tuotteen omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva tuote.  Jos ei löydy, 
     * niin lisätään uutena tuotteena. 
     * @param tuote lisätäävän tuotteen viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo täynnä 
     */ 
    public void korvaaTaiLisaa(Tuote tuote) throws SailoException { 
        tuotteet.korvaaTaiLisaa(tuote); 
    } 
    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien tuotteiden viitteet 
     * @param hakuehto hakuehto  
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä tuotteista 
     * @throws SailoException Jos jotakin menee väärin
     * @example 
     * <pre name="test">
     *   #THROWS CloneNotSupportedException, SailoException
     *   alusta();
     *   Tuote t3 = new Tuote(); t3.rekisteroi();
     *   t3.aseta(1,"Helly Hansen GRX");
     *   rekisteri.lisaa(t3);
     *   Collection<Tuote> loytyneet = rekisteri.etsi("*Helly*",1);
     *   loytyneet.size() === 1;
     *   Iterator<Tuote> it = loytyneet.iterator();
     *   it.next() == t3 === true; 
     * </pre>
     */ 
    public Collection<Tuote> etsi(String hakuehto, int k) throws SailoException { 
        return tuotteet.etsi(hakuehto, k); 
    } 

    /**
     * @param args ei käytössä
     * @throws SailoException jos liikaa tuotteita
     */
    public static void main(String[] args) throws SailoException {
        var rekisteri = new Rekisteri();
        
        
        try { 
        var tuote1 = new Tuote();
        var tuote2 = new Tuote();
        
       
           tuote1.rekisteroi();
           tuote1.taytaTiedoilla();
       
           tuote2.rekisteroi();
           tuote2.taytaTiedoilla();
           
           rekisteri.lisaaTuote(tuote1);
           rekisteri.lisaaTuote(tuote2);
           
           int id1 = tuote1.getTunnusNro();
           int id2 = tuote2.getTunnusNro();
           
           Tuoteryhma p1 = new Tuoteryhma(id1); 
           p1.taytaRyhma(id1); 
           rekisteri.lisaaTuoteryhma(p1);
           
           
           
           Tuoteryhma p2 = new Tuoteryhma(id2); p2.taytaRyhma(id2); rekisteri.lisaaTuoteryhma(p2);
           
       } catch (SailoException e) {
           System.err.println(e.getMessage());
           System.err.flush();
       }
           System.out.println("==== Rekisteri testi ====");
        
        for( int i = 0; i < rekisteri.getTuotteita(); i++) {
            Tuote tuote = rekisteri.annaTuote(i);
                System.out.println("Tuote paikassa "+i);
                tuote.tulosta(System.out);
              List<Tuoteryhma> l = rekisteri.annaTuoteryhmat(tuote);
              for (Tuoteryhma h : l)
                  h.tulosta(System.out);

            }
    }
  
  
}
    
