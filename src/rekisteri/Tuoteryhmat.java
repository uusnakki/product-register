package rekisteri;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * - Osaa lukea tuoteryhmät tiedostoon
 *  - Osaa etsiä ja lajitella tuoteryhmiä
 * @author joonas uusnäkki & asla paakkinen
 * @version 23.4.2020
 *
 */
public class Tuoteryhmat implements Iterable<Tuoteryhma> {
    
   
    private String tiedostonPerusNimi = "generalStore";
    
    
    /** Taulukko tuoteryhmistä */
    private final Collection<Tuoteryhma> alkiot = new ArrayList<Tuoteryhma>();

    /**
     * Oletusmuodostaja
     */
    public Tuoteryhmat() {
        //muodostuu muualla
    }
    /**
     * Lisää uuden tuoteryhnmän tietorakenteeseen.  Ottaa tuoteryhmän omistukseensa.
     * @param tuo lisättävä tuoteryhmä
     */
    public void lisaa(Tuoteryhma tuo) {
        alkiot.add(tuo);
    }
    /**
     * Palauttaa rekisterin tuoteryhmiem lukumäärän
     * @return tuoteryhmien lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }

    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }

    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    /**
     * Haetaan kaikki tuotteiden tuoteryhmät
     * @param tunnusnro tuotteen tunnusnumero jolle tuoteryhmiä haetaan
     * @return tietorakenne jossa viiteet löydetteyihin tuoteryhmiin
     * @example
     */
    public List<Tuoteryhma> annaTuoteryhmat(int tunnusnro) {
        List<Tuoteryhma> loydetyt = new ArrayList<Tuoteryhma>();
        for (Tuoteryhma tue : alkiot)
            if (tue.getTuoteNro() == tunnusnro) loydetyt.add(tue);
        return loydetyt;
    }

    /**
     * Tallentaa tuoteryhmät tiedostoon.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
       
        File ftied = new File(getTiedostonNimi());
      

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Tuoteryhma tuo : this) {
                fo.println(tuo.toString());
            }

        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

    }
    /**
     * Lukee tuoteryhmat tiedostosta
     * @param tied tiedoston nimen alkuosa
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Tuoteryhma ryhma = new Tuoteryhma();
                lisaa(ryhma);
            }
            

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }

     /**
     * @param ryhma tuoteryhmä, joka poistetaan
     * @return jos onnistui, true
     */
    public boolean poista(Tuoteryhma ryhma) {
          boolean ret = alkiot.remove(ryhma);
          
          return ret;
      }
    /**
     * Poistetaan tietyn tuotteen tuoteryhmät
     * @param tunnusNro tuotteen tunnusnumero
     * @return n
     */
public int poistaTuotteenTuoteryhmat(int tunnusNro) {
          int n = 0;
          for (Iterator<Tuoteryhma> it = alkiot.iterator(); it.hasNext();) {
              Tuoteryhma har = it.next();
              if ( har.getTuoteNro() == tunnusNro ) {
                  it.remove();
                  n++;
              }
          }
           return n;
      }

    /**
     * Testiohjelma tuoteryhmille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Tuoteryhmat ryhmat = new Tuoteryhmat();
        
        Tuoteryhma p1 = new Tuoteryhma();
        p1.taytaRyhma(2);
        
        Tuoteryhma p2 = new Tuoteryhma();
        p2.taytaRyhma(1);
        Tuoteryhma p3 = new Tuoteryhma();
        p3.taytaRyhma(2);
        Tuoteryhma p4 = new Tuoteryhma();
        p4.taytaRyhma(2);

        ryhmat.lisaa(p1);
        ryhmat.lisaa(p2);
        ryhmat.lisaa(p3);
        ryhmat.lisaa(p2);
        ryhmat.lisaa(p4);

        System.out.println("============= Tuoteryhmät testi =================");

        List<Tuoteryhma> ryhmat2 = ryhmat.annaTuoteryhmat(2);

        for (Tuoteryhma har : ryhmat2) {
            System.out.print(har.getTuoteNro() + " ");
            har.tulosta(System.out);
        }

    }
    /**
     * Iteraattori kaikkien tuoteryhmien läpikäymiseen
     * @return tuoteryhmäiteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     *  Tuoteryhmat ryhmat = new Tuoteryhmat();
     *  Tuoteryhma talvi21 = new Tuoteryhma(2); ryhmat.lisaa(talvi21);
     *  Tuoteryhma talvi11 = new Tuoteryhma(1); ryhmat.lisaa(talvi11);
     *  Tuoteryhma talvi22 = new Tuoteryhma(2); ryhmat.lisaa(talvi22);
     *  Tuoteryhma talvi12 = new Tuoteryhma(1); ryhmat.lisaa(talvi12);
     *  Tuoteryhma talvi23 = new Tuoteryhma(2); ryhmat.lisaa(talvi23);
     * 
     *  Iterator<Tuoteryhma> i2=ryhmat.iterator();
     *  i2.next() === talvi21;
     *  i2.next() === talvi11;
     *  i2.next() === talvi22;
     *  i2.next() === talvi12;
     *  i2.next() === talvi23;
     *  i2.next() === talvi12;  #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  int tnrot[] = {2,1,2,1,2};
     *  
     *  for ( Tuoteryhma tuo:ryhmat ) { 
     *    tuo.getTuoteNro() === tnrot[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */
    @Override
    public Iterator<Tuoteryhma> iterator() {
        return alkiot.iterator();
    }

}
