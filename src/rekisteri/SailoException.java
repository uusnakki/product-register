package rekisteri;

/**
 * Luokka poikkeusilmoitukselle
 * 
 * @author joonas uusnäkki & asla paakkinen
 * @version 23.4.2020
 *
 */
public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * Poikkeuksen muodostajia, jolle tuodaan poikkeuksessa
     * käytettävä viesti
     * @param viesti Poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
}
