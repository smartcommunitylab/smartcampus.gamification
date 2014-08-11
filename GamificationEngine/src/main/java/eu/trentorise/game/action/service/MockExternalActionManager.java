package eu.trentorise.game.action.service;

import eu.trentorise.game.action.comparator.ExternalActionKeyFkComparator;
import eu.trentorise.game.action.container.IImportExternalActionContainer;
import eu.trentorise.game.action.model.ExternalAction;
import eu.trentorise.game.application.comparator.ApplicationKeyComparator;
import java.util.Collection;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockExternalActionManager")
public class MockExternalActionManager implements IExternalActionImporter {

    public static MockExternalActionManager createInstance() {
        MockExternalActionManager mock = new MockExternalActionManager();
        mock.mockActionManager = MockActionManager.createInstance();
        
        mock.comparator = new ExternalActionKeyFkComparator();
        ((ExternalActionKeyFkComparator) mock.comparator).setActionKeyComparator(mock.mockActionManager.getComparator());
        ((ExternalActionKeyFkComparator) mock.comparator).setApplicationKeyComparator(new ApplicationKeyComparator());
        
        return mock;
    }
    
    
    @Override
    public Collection<ExternalAction> importExternalActions(IImportExternalActionContainer container) throws Exception {
        //TODO: return null or throw Exception if it is not possible to 
        //complete this activity
        
        return this.createElements(container);
    }
    
    
    public Collection<ExternalAction> createElements(IImportExternalActionContainer container) {
        return mockActionManager.createViaggiaRoveretoExternalActions();
    }

    
    public Comparator<ExternalAction> getComparator() {
        return comparator;
    }
    
    
    @Qualifier("mockActionManager")
    @Autowired
    protected MockActionManager mockActionManager;
    
    
    @Qualifier("externalActionKeyFkComparator")
    @Autowired
    protected Comparator<ExternalAction> comparator;
}