/**
 * 
 */
package rekisteri;

import java.io.PrintStream;
import java.util.Comparator;

import fi.jyu.mit.ohj2.Mjonot;

/**
 *  tietää tuotteen kentät( valmistaja, hinta ...)   
 * - osaa tarkistaa tietyn kentän oikeellisuuden      
 * - osaa muuttaa annettavat merkkijonot tiedoiksi   
 * @author joonas uusnäkki & asla paakkinen
 * @version 23.4.2020
 *
 */
public class Tuote implements Cloneable {
    private int tunnusNro;
    
    private String nimi;
    private String valmistaja;
    private String valmistusvuosi;
    private String hinta;
    private String maara;
    private String kokoPituus;
    
    private static int seuraavaNro = 1;
    
    /** 
     * Tuotteiden vertailija 
     */ 
    public static class Vertailija implements Comparator<Tuote> { 
        private int k;  
         
         
        /**
         * @param k minkä kentän mukaan vertaillaan
         */
        public Vertailija(int k) { 
            this.k = k; 
        } 
         
        @Override 
        public int compare(Tuote t1, Tuote t2) { 
            return t1.getAvain(k).compareToIgnoreCase(t2.getAvain(k)); 
        }
    } 
     
    /** 
     * Antaa k:n kentän sisällön merkkijonona 
     * @param k monenenko kentän sisältö palautetaan 
     * @return kentän sisältö merkkijonona 
     */ 
    public String getAvain(int k) {
        switch ( k ) {
        case 0: return "" + tunnusNro;
        case 1: return "" + nimi.toUpperCase();
        case 2: return "" + valmistaja.toUpperCase();
        default: return "Urpo!";
        }
    }
    /**
     * Metodi jolla saadaan täytettyä testiarvot tuotteelle
     */
    public void taytaTiedoilla() {
        nimi = "Helly Hansen GRX 2019";
        valmistaja = "Helly Hansen";
        valmistusvuosi = "2019";
        hinta = "149.99€";
        maara = "25";
        kokoPituus = "S/M/XL";
    }
    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monenenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + tunnusNro;
        case 1: return "" + nimi;
        case 2: return "" + valmistaja;
        case 3: return "" + valmistusvuosi;
        case 4: return "" + hinta;
        case 5: return "" + maara;
        case 6: return "" + kokoPituus;
        default: return "Urpo!";
        }
    }

    /**
     * Tulostetaan tuotteen tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", tunnusNro) + " " + nimi + "  "
                + "\n" + "Valmistaja:" + " " + valmistaja + "\n" +
        "Valmistusvuosi:" + " " + valmistusvuosi + "\n" 
                +"Hinta:" + " " + hinta + "\n" +
        "Määrä:" + " " + maara + "\n" 
        + "Koko/Pituus: " + kokoPituus);
    }
    /**
     * Antaa tuotteelle seuraavan rekisterinumeron.
     * @return tuotteen uusi tunnusNro
     * @example
     * <pre name="test">
     *   Tuote pallo = new Tuote();
     *   pallo.getTunnusNro() === 0;
     *   pallo.rekisteroi();
     *   Tuote pallo2 = new Tuote();
     *   pallo2.rekisteroi();
     *   int n1 = pallo.getTunnusNro();
     *   int n2 = pallo2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        this.tunnusNro = seuraavaNro;
        Tuote.seuraavaNro++;
        return tunnusNro;
    }
    /**
     * Palauttaa tuotteen tunnusnumeron.
     * @return tuotteen tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    /**
     * @return palautetaan tietyn tuotteen nimi
     */
    public String getNimi() {
        return this.nimi;
    }
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }
    /**
     * Palauttaa tuotteen kenttien lukumäärän
     * @return kenttien lukumäärä
     */
    public int getKenttia() {
        return 7;
    }
    /**
     * Eka kenttä joka on mielekäs kysyttäväksi
     * @return eknn kentän indeksi
     */
    public int ekaKentta() {
        return 1;
    }

    /**
     * @return valmistaja
     */
    public String getValmistaja() {
        return valmistaja;
    }

    /**
     * @return vuosi
     */
    public String getVuosi() {
        return valmistusvuosi;
    }

    /**
     * @return hinta
     */
    public String getHinta() {
        return hinta;
    }

    /**
     * @return määrä
     */
    public String getMaara() {
        return maara;
    }

    /**
     * @return koko/pituus
     */
    public String getKP() {
        return kokoPituus;
    }
    /**
     * Selvitää tuotteen tiedot | erotellusta merkkijonosta
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusNro.
     * @param rivi josta tuotteen tiedot otetaan
     * @example
     * <pre name="test">
     *   Tuote tuote = new Tuote();
     *   tuote.parse("   3  | Nike Airmax 2000 | Nike | 2000 | 120€ | 20 | 42 |");
     *   tuote.getTunnusNro() === 3;
     *   tuote.toString().startsWith("3|Nike Airmax 2000|Nike|2000|120€|20|42|") === true; 
     *
     *   tuote.rekisteroi();
     *   int n = tuote.getTunnusNro();
     *   tuote.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
     *   tuote.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
     *   tuote.getTunnusNro() === n+20+1;
     *     
     * </pre>
     */

    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        nimi = Mjonot.erota(sb, '|', nimi);
        valmistaja = Mjonot.erota(sb, '|', valmistaja);
        valmistusvuosi = Mjonot.erota(sb, '|', valmistusvuosi);
        hinta= Mjonot.erota(sb, '|', hinta);
        maara = Mjonot.erota(sb, '|',maara);
        kokoPituus = Mjonot.erota(sb, '|', kokoPituus);
    }
    /**
     * Palauttaa tuotteen tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return tuote tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   tuote.parse("   3  | Nike Airmax 2000 | Nike | 2000 | 120€ | 20 | 42 |");
     *   tuote.getTunnusNro() === 3;
     *   tuote.toString().startsWith("3|Nike Airmax 2000|Nike|2000|120€|20|42|") === true; 
     *
     * </pre>  
     */
    @Override
    public String toString() {
        return "" +
                getTunnusNro() + "|" +
                nimi + "|" +
                valmistaja + "|" +
                valmistusvuosi + "|" +
                hinta + "|" +
                maara + "|" +
                kokoPituus; 
    }


    /**
     * Tehdään identtinen klooni tuotteesta
     * @return Object kloonattu tuote
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Tuote tuote = new Tuote();
     *   tuote.parse("   3  |  Helly Hansen GRX   | 123");
     *   Tuote kopio = tuote.clone();
     *   kopio.toString() === tuote.toString();
     *   tuote.parse("   4  |  Helly Hansen GRX   | 123");
     *   tuote.toString().equals(tuote.toString()) === false;
     * </pre>
     */
    @Override
    public Tuote clone() throws CloneNotSupportedException {
        Tuote uusi = (Tuote) super.clone();
        return uusi;
    }

    /**
     * @param s tuotteelle laitettava nimi
     * @return virheilmoitus, null jos ok
     */
    public String setNimi(String s) {
        if (s.equals("")) return "Nimi ei voi olla tyhjä!";
        nimi = s;
        return null;
    }
    /**
     * @param s tuotteelle laitettava valmistaja
     * @return virheilmoitus, null jos ok
     */
    public String setValmistaja(String s) {
        if (s.equals("")) return "Valmistaja täytyy olla!";
        valmistaja = s;
        return null;
    }
    /**
     * @param s tuotteelle laitettava valmistusvuosi
     * @return virheilmoitus, null jos ok
     */
    public String setVuosi(String s) {
        if (s.trim().equals("")) return "Tuotteella on oltava valmistusvuosi!";
        if ( !s.matches("[0-9]*") || s.length() > 4 ) return "Vuosiluvun oltava numeerinen";
        valmistusvuosi = s;
        return null;

    }
    /**
     * @param s tuotteelle laitettava hinta
     * @return virheilmoitus, null jos ok
     */
    public String setHinta(String s) {
        if (s.trim().equals("")) return "Tuote ei voi olla ilmainen :) mutta tuote voi maksaa 0€ :D";
        char vika = s.charAt(s.length()-1);
        if (!s.matches("[0-9,€]*") || vika != '€' ) return "Tuotteella on oltava eurohinta!";
        hinta = s;
        return null;
    }
    /**
     * @param s tuotteelle laitettava varastomäärä
     * @return virheilmoitus, null jos ok
     */
    public String setMaara(String s) {
        if (!s.matches("[0-9,€]*") || s.equals("")) return "Määrän täytyy olla määritelty!";
        maara = s;
        return null;
    }
    /**
     * @param s tuotteelle laitettava koko/pituus
     * @return virheilmoitus, null jos ok
     */
    public String setKP(String s) {
        if (s.equals("")) return "Aseta koko/pituus!";
        kokoPituus = s;
        return null;
    }


    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
    Tuote takki = new Tuote();
    Tuote takki2 = new Tuote();
    
    takki.rekisteroi();
    takki.taytaTiedoilla();
    takki.tulosta(System.out);
   
    
    takki2.rekisteroi();
    takki2.tulosta(System.out);
    takki2.taytaTiedoilla();
    
    
    
    }
}
