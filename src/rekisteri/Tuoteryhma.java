package rekisteri;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * - Tietää tunnusnumeronsa ja nimensä
 * @author joonas uusnäkki & asla paakkinen
 * @version 23.4.2020
 *
 */
public class Tuoteryhma {
    
    private int tunnusNro;
    private int tuoteNro;
    private String nimi = "Basic";
    
    private static int seuraavaNro = 1;
    
    /**
     * Oletusmuodostaja
     */
    public Tuoteryhma() {
        //muodostuu muualla
    }
    
    /**
     * @param tuoteNro tuotteen viitenumero
     */
    public Tuoteryhma(int tuoteNro) {
        this.tuoteNro = tuoteNro;
    }
    
    /**
     * Täytetään ryhmä tiedoilla
     * @param nro tuotenro
     */
    public void taytaRyhma(int nro ) {
        tuoteNro = nro;
        nimi = getRyhmaNimi();
    }
    /**
     * @return palautetaan tuoteryhmän nimi
     */
    public String getRyhmaNimi() {
        return nimi;
    }
    /**
     * Asetetaan tuoteryhmälle nimi
     * @param nimi tuoteryhmän nimi, joka valittu listasta
     */
    public void setRyhmaNimi(String nimi) {
        this.nimi = nimi;
    }
    /**
     * Tulostetaan tuoteryhmän tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println("Tuoteryhmä: " + nimi);
    }
    /**
     * Tulostetaan tuoteryhmän tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    /**
     * Antaa tuoteryhmälle seuraavan rekisterinumeron.
     * @return tuoteryhmän uusi tunnus_nro
     * @example
     * <pre name="test">
     *   Tuoteryhma p1 = new Tuoteryhma();
     *   p1.getTunnusNro() === 0;
     *   p1.rekisteroi();
     *   Tuoteryhma p2 = new Tuoteryhma();
     *   p2.rekisteroi();
     *   int n1 = p1.getTunnusNro();
     *   int n2 = p2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }

    /**
     * Palautetaan tuoteryhmän oma id
     * @return tuoteryhmän id
     */
    public int getTunnusNro() {
        return tunnusNro;
    }

    /**
     * Palautetaan mille tuotteelle tuoteryhmän kuuluu
     * @return jäsenen id
     */
    public int getTuoteNro() {
        return tuoteNro;
    }
    /**
     * Palauttaa tuoteryhmän tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return tuoteryhmä tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Tuoteryhma tuo = new Tuoteryhma();
     *   tuo.parse("   2   |  10  |   Talvitakki  |");
     *   tuo.toString()    === "2|10|Talvitakki|";
     * </pre>
     */
    @Override
    public String toString() {
        return "" + getTunnusNro() + "|" + nimi;
    }


    /**
     * Testiohjelma Harrastukselle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Tuoteryhma har = new Tuoteryhma();
        har.taytaRyhma(2);
        har.tulosta(System.out);
    }




}
