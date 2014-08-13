package eu.trentorise.game.action.response;

import eu.trentorise.game.action.model.Param;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public class ParamCollectionResponse {
    
    protected Collection<Param> params;

    public Collection<Param> getParams() {
        return params;
    }

    public void setParams(Collection<Param> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "ParamCollectionResponse{" + "params=" + params + '}';
    }
}