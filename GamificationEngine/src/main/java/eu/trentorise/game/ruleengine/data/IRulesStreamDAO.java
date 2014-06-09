package eu.trentorise.game.ruleengine.data;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Luca Piras
 */
public interface IRulesStreamDAO {
    
    public InputStream getRulesStream() throws IOException;
}