package eu.trentorise.game.action.service;

import eu.trentorise.game.action.container.IImportExternalActionContainer;
import eu.trentorise.game.action.model.ExternalAction;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public interface IExternalActionImporter {
    
    public Collection<ExternalAction> importExternalActions(IImportExternalActionContainer container) throws Exception;
}