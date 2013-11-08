/**
 *
 * @author luca
 */

package eu.trentorise.game.controller;


import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.springframework.ui.Model;
       
/**
 *
 * @author Luca Piras
 * @param <C>
 */
public abstract class AbstractController<C> {
    
    protected String viewAbsolute;
    protected String viewInternal;
    
    protected String coName;
    
    protected Logger logger;
    
    public AbstractController(String viewAbsolute, String viewInternal, 
                              String coName, Logger logger) {
        
        this.viewAbsolute = viewAbsolute;
        this.viewInternal = viewInternal;
        
        this.coName = coName;
        
        this.logger = logger;
    }
    
    public void addModelMap(Model model) {}
    
    protected C manageGet(Model model, HttpSession session) {
        C co = (C) session.getAttribute(this.coName);
        if (null == co) {
            co = initializeNotExistentCommandObject();
        } else {
            initializeExistentCommandObject(co);
        }
        
        model.addAttribute(this.coName, co);
        
        return co;
    }
    
    protected abstract C initializeNotExistentCommandObject();
    
    protected abstract void initializeExistentCommandObject(C co);
}