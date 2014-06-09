package eu.trentorise.utils.filter;

/**
 *
 * @author Luca Piras
 */
public interface IElementFilter<C, R, E> {

    /**
     * Effettua dei controlli su di un elemento e se riscontra che secondo
     * determinati criteri, è da filtrare (eliminare, disabilitare, dipende 
     * dalla classe implementanta) allora rende true, altrimenti false
     *
     * @param container contiene elementi che verranno utilizzati durante i
     * controlli da effettuare
     * @param result contiene elementi che verranno utilizzati durante i
     * controlli da effettuare
     * @param element l'elemento da controllare
     * @return rende true se l'elemento è da filtrare (eliminare, disabilitare,
     * dipende dalla classe implementanta), secondo determinati controlli e
     * criteri relativi, altrimenti rende false
     *
     * @throws Exception se qualcosa non va a buon fine
     */
    public Boolean filter(C container, R result, E element) throws Exception;
}