package eu.trentorise.game.action.service;

import eu.trentorise.game.action.model.GameInternalAction;
import eu.trentorise.game.action.model.InternalAction;
import eu.trentorise.game.action.model.Param;
import eu.trentorise.game.action.model.ParamType;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import eu.trentorise.utils.rest.crud.IRestCrudTestManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockInternalActionParamManager")
public class MockInternalActionParamManager implements IRestCrudManager<Param, InternalAction, Param>,
                                                       IRestCrudTestManager<Param, InternalAction, Param>{

    public static MockInternalActionParamManager createInstance() {
        MockInternalActionParamManager mock = new MockInternalActionParamManager();
        
        mock.mockActionManager = MockActionManager.createInstance();
        mock.mockGameInternalActionManager = MockGameInternalActionManager.createInstance();
        mock.mockExternalActionParamManager = MockExternalActionParamManager.createInstance();
        
        mock.comparator = mock.mockExternalActionParamManager.getComparator();
        
        return mock;
    }
    
    
    @Override
    public Param createSingleElement(Param containerWithForeignIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Collection<Param> readCollection(InternalAction containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        //TODO: vai nella tabella InternalAction e recupera tutti i
        //gli elementi per l'app indicata
        return this.createElements(containerWithIds);
    }

    @Override
    public Param readSingleElement(Param containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        Param returnValue = null;
        
        Param expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }
    
    @Override
    public Param updateSingleElement(Param containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Param deleteSingleElement(Param containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public Param createElement(Param containerWithIds) throws Exception {
        return this.createGreenLeavesPointsUpdatingParam();
    }
    
    @Override
    public List<Param> createElements(InternalAction containerWithIds) throws Exception {
        List<Param> list = new ArrayList<>();
        
        list.add(this.createGreenLeavesPointsUpdatingParam());
        
        return list;
    }
    
    protected Param createGreenLeavesPointsUpdatingParam() throws Exception {
        GameInternalAction gameInternalAction = mockGameInternalActionManager.createGameGreenLeavesUpdatingInternalAction();
        InternalAction internalAction = gameInternalAction.getInternalAction();
        
        return mockExternalActionParamManager.createBasicParam(internalAction, "greenLeaves", ParamType.INTEGER);
    }

    
    public Comparator<Param> getComparator() {
        return comparator;
    }
    
    
    @Qualifier("mockActionManager")
    @Autowired
    protected MockActionManager mockActionManager;
    
    @Qualifier("mockGameInternalActionManager")
    @Autowired
    protected MockGameInternalActionManager mockGameInternalActionManager;
    
    @Qualifier("mockExternalActionParamManager")
    @Autowired
    protected MockExternalActionParamManager mockExternalActionParamManager;
    
    
    @Qualifier("paramKeyComparator")
    @Autowired
    protected Comparator<Param> comparator;
}