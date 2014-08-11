package eu.trentorise.game.action.response;

import eu.trentorise.game.action.model.ExternalAction;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public class ExternalActionCollectionResponse {
    
    Collection<ExternalAction> externalActions;

    public Collection<ExternalAction> getExternalActions() {
        return externalActions;
    }

    public void setExternalActions(Collection<ExternalAction> externalActions) {
        this.externalActions = externalActions;
    }

    @Override
    public String toString() {
        return "ExternalActionCollectionResponse{" + "externalActions=" + externalActions + '}';
    }
}