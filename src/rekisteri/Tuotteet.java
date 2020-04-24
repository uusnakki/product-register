/**
 * 
 */
package rekisteri;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

/**
 * - pitää yllä varsinaista tuoterekisteriä eli        
 * osaa lisätä ja poistaa tuotteen                    
 * - lukee ja kirjoittaa tuotteen tiedostoon        
 * - osaa etsiä ja lajitella
 * - tietää monta tuotetta olemassa
 * @author joonas uusnäkki & asla paakkinen
 * @version 23.4.2020
 *
 */
public class Tuotteet implements Iterable<Tuote> {
    private static final int MAX_TUOTTEITA  = 5;
    private int lkm = 0;
    private Tuote[] alkiot = new Tuote[MAX_TUOTTEITA];
    private String kokoNimi = "";
    private String tiedostonPerusNimi = "boomshakalaka";
    
    
    /**
     * Oletusmuodostaja
     */
    public Tuotteet() {
        //attribuuttien oma alustuss riittää
    }
    /**
     * Lisää uuden tuotteen tietorakenteeseen.  Ottaa tuotteen omistukseensa.
     * @param tuote lisättäävän tuotteen viite.  
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * Tuotteet tuotteet = new Tuotteet();
     * Tuote t1 = new Tuote(), t2 = new Tuote();
     * tuotteet.getLkm() === 0;
     * tuotteet.lisaa(t1); tuotteet.getLkm() === 1;
     * tuotteet.lisaa(t2); tuotteet.getLkm() === 2;
     * tuotteet.lisaa(t1); tuotteet.getLkm() === 3;
     * Iterator<Tuote> it = tuotteet.iterator(); 
     * it.next() === t1;
     * it.next() === t2; 
     * it.next() === t1;  
     * tuotteet.lisaa(t1); tuotteet.getLkm() === 4;
     * tuotteet.lisaa(t1); tuotteet.getLkm() === 5;
     * </pre>
     */

    public void lisaa(Tuote tuote) throws SailoException {
        if (lkm >= alkiot.length) {
            alkiot = Arrays.copyOf(alkiot, alkiot.length + 10);
        }
        alkiot[lkm++] = tuote;
    }
    /**
     * @param i monennenko tuotteen viite on kyseessä
     * @return viite tuotteeseen, jonka indeksi on i
     */
    public Tuote anna(int i) {
        return alkiot[i];
    }
     /**
     * @return tuotteiden lukumäärä
     */
    public int getLkm() {
        return lkm;
     }
    /**
     * Tallennetaan tuotetiedosto
     * @throws SailoException jos jokin ei toimi
     */
    public void tallenna()throws SailoException  {
     
        File ftied = new File(getTiedostonNimi());
       

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(getKokoNimi());
            fo.println(alkiot.length);
            for (Tuote tuote : this) {
                fo.println(tuote.toString());
            }
           
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
    }
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }

    /**
     * Palauttaa rekisterin koko nimen
     * @return rekisterin koko nimi merkkijononna
     */
    public String getKokoNimi() {
        return kokoNimi;
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
        return getTiedostonPerusNimi() + ".dat";
    }

    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }

    /**
     * Lukee tiedostosta
     * @param tied tiedoston perusnimi
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            kokoNimi = fi.readLine();
            if ( kokoNimi == null ) throw new SailoException("Rekisterin nimi puuttuu");
            String rivi = fi.readLine();
            if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Tuote tuote = new Tuote();
                tuote.parse(rivi); 
                lisaa(tuote);
            }
            

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }

    }
    /**
     * Asettaa tiedoston perusnimen
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }

    /**
     * Korvaa tuotteen tietorakenteessa.  Ottaa tuotteen omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva tuote. Jos ei löydy,
     * niin lisätään uutena tuotteena.
     * @param tuote lisätäävän tuotteen viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Tuotteet tuotteet = new Tuotteet();
     * Tuote a = new Tuote(), a2 = new Tuote();
     * a1.rekisteroi(); a2.rekisteroi();
     * tuotteet.getLkm() === 0;
     * tuotteet.korvaaTaiLisaa(a1); tuotteet.getLkm() === 1;
     * tuotteet.korvaaTaiLisaa(a2); tuotteet.getLkm() === 2;
     * Tuote a3 = a1.clone();
     * a3.setValmistaja("Nike");
     * Iterator<Tuote> it = Tuotteet.iterator();
     * it.next() == a1 === true;
     * tuotteet.korvaaTaiLisaa(a3); tuotteet.getLkm() === 2;
     * it = tuotteet.iterator();
     * Tuote j0 = it.next();
     * j0 === a3;
     * j0 == a3 === true;
     * j0 == a1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Tuote tuote) throws SailoException {
        int id = tuote.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnusNro() == id ) {
                alkiot[i] = tuote;
                return;
            }
        }
        lisaa(tuote);
    }
   /**
 * @param id tunnusnro
 * @return 0 tai -1
 */
public int poista(int id) { 
           int ind = etsiId(id); 
           if (ind < 0) return 0; 
           lkm--; 
           for (int i = ind; i < lkm; i++) 
               alkiot[i] = alkiot[i + 1]; 
           alkiot[lkm] = null; 
           return 1; 
       } 

    /**
     * @param id id, jota etitään
     * @return tuotteen tunnusnumero tai -1
     */
    public int etsiId(int id) { 
               for (int i = 0; i < lkm; i++) 
                   if (id == alkiot[i].getTunnusNro()) return i; 
               return -1; 
           } 
    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien tuotteen viitteet 
     * @param hakuehto hakuehto 
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä tuotteista 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     *   Tuotteet tuotteet = new Tuotteet(); 
     *   Tuote j1 = new Tuote(); j1.parse("1|Helly Hansen GRX|2019|150€|20|S|"); 
     *   Tuote j2 = new Tuote(); j2.parse("2|Nike Air Max2||50€|3|39|"); 
     *   Tuote j3 = new Tuote(); j3.parse("3|Adidas Go|1997||119€|60|S|"); 
     *   Tuote j4 = new Tuote(); j4.parse("4|Umbro Pro|2006|100€|40|M|"); 
     *   Tuote j5 = new Tuote(); j5.parse("5|Kappa Yo|2020|12€|30|XS|"); 
     *   tuotteet.lisaa(j1); tuotteet.lisaa(j2); tuotteet.lisaa(j3); tuotteet.lisaa(j4); tuotteet.lisaa(j5);
     *   List<Tuote> loytyneet;  
     *   loytyneet = (List<Tuote>)tuotteet.etsi("*s*",1);  
     *   loytyneet.size() === 2;  
     *   loytyneet.get(0) == j3 === true;  
     *   loytyneet.get(1) == j4 === true;  
     *     
     *   loytyneet = (List<Tuote>)tuotteet.etsi("*7-*",2);  
     *   loytyneet.size() === 2;  
     *   loytyneet.get(0) == j3 === true;  
     *   loytyneet.get(1) == j5 === true; 
     *     
     *   loytyneet = (List<Tuote>)tuotteet.etsi(null,-1);  
     *   loytyneet.size() === 5;  
     * </pre> 
     */ 
    public Collection<Tuote> etsi(String hakuehto, int k) { 
        String ehto = "*"; 
        if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto; 
        int hk = k; 
        if ( hk < 0 ) hk = 1;
        List<Tuote> loytyneet = new ArrayList<Tuote>(); 
        for (Tuote tuo : this) { 
            if (WildChars.onkoSamat(tuo.anna(hk), ehto)) loytyneet.add(tuo);   
        } 
        Collections.sort(loytyneet, new Tuote.Vertailija(hk));
        return loytyneet; 
    }

    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
    Tuotteet tuotteet = new Tuotteet();
    Tuote pallo = new Tuote(), hattu = new Tuote();
    pallo.rekisteroi();
    pallo.taytaTiedoilla();
    
    hattu.rekisteroi();
    hattu.taytaTiedoilla();
    
  try {
      tuotteet.lisaa(pallo);
      tuotteet.lisaa(hattu);
    
          System.out.println("==== Tuotteet testi ====");
    
              for( int i = 0; i < tuotteet.getLkm(); i++) {
                      Tuote tuote = tuotteet.anna(i);
                          System.out.println("Tuote nro: "+i);
                          tuote.tulosta(System.out);
        
              }
       }catch (SailoException ex) {
              System.err.println(ex.getMessage());
       }
    
    }
    @Override
    public Iterator<Tuote> iterator() {
        return new TuotteetIterator();
    } 
    /**
     * Luokka tuotteiden iteroimiseksi.
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Tuotteet tuotteet = new Tuotteet();
     * Tuote t1 = new Tuote(), t2 = new Tuote();
     * t1.rekisteroi(); t2.rekisteroi();
     *
     * tuotteet.lisaa(t1); 
     * tuotteet.lisaa(t2); 
     * tuotteet.lisaa(t1); 
     * 
     * StringBuffer ids = new StringBuffer(30);
     * for (Tuote tuote:tuotteet)   // Kokeillaan for-silmukan toimintaa
     *   ids.append(" "+tuote.getTunnusNro());           
     * 
     * String tulos = " " + t1.getTunnusNro() + " " + t2.getTunnusNro() + " " + t1.getTunnusNro();
     * 
     * ids.toString() === tulos; 
     * 
     * ids = new StringBuffer(30);
     * for (Iterator<Tuote>  i=tuotteet.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
     *   Tuote tuote = i.next();
     *   ids.append(" "+tuote.getTunnusNro());           
     * }
     * 
     * ids.toString() === tulos;
     * 
     * Iterator<Tuote>  i=tuotteet.iterator();
     * i.next() == t1  === true;
     * i.next() == t2  === true;
     * i.next() == t1  === true;
     * 
     * i.next();  #THROWS NoSuchElementException
     *  
     * </pre>
     */
    public class TuotteetIterator implements Iterator<Tuote> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa tuotetta
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä tuotteita
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava tuote
         * @return seuraava tuote
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Tuote next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei ole");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }

}
