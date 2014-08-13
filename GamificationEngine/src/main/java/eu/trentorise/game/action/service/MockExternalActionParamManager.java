package eu.trentorise.game.action.service;

import eu.trentorise.game.action.comparator.ParamKeyComparator;
import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.BasicParam;
import eu.trentorise.game.action.model.ExternalAction;
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
@Service("mockExternalActionParamManager")
public class MockExternalActionParamManager implements IRestCrudManager<Param, ExternalAction, Param>,
                                                       IRestCrudTestManager<Param, ExternalAction, Param>{

    public static MockExternalActionParamManager createInstance() {
        MockExternalActionParamManager mock = new MockExternalActionParamManager();
        
        mock.mockActionManager = MockActionManager.createInstance();
        
        mock.comparator = new ParamKeyComparator();
        ((ParamKeyComparator) mock.comparator).setComparator(mock.mockActionManager.getComparator());
        
        return mock;
    }
    
    
    @Override
    public Param createSingleElement(Param containerWithForeignIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Collection<Param> readCollection(ExternalAction containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        //TODO: vai nella tabella ExternalAction e recupera tutti i
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
        return this.createBikeKmParam();
    }
    
    @Override
    public List<Param> createElements(ExternalAction containerWithIds) {
        List<Param> list = new ArrayList<>();
        
        ExternalAction action = mockActionManager.createExternalAction();
        
        list.add(this.createBikeKmParam());
        list.add(this.createBasicParam(action, "carKM", ParamType.INTEGER));
        list.add(this.createBasicParam(action, "busKM", ParamType.INTEGER));
        list.add(this.createBasicParam(action, "means", ParamType.INTEGER));
        
        return list;
    }
    
    
    protected BasicParam createBasicParam(Action action, String name, ParamType type) {
        BasicParam element = new BasicParam();
        element.setAction(action);
        element.setName(name);
        element.setType(ParamType.INTEGER);
        
        return element;
    }
    
    public BasicParam createBikeKmParam() {
        return this.createBasicParam(mockActionManager.createExternalAction(), "bikeKM", ParamType.INTEGER);
    }

    
    public Comparator<Param> getComparator() {
        return comparator;
    }
    
    
    @Qualifier("mockActionManager")
    @Autowired
    protected MockActionManager mockActionManager;
    
    
    @Qualifier("paramKeyComparator")
    @Autowired
    protected Comparator<Param> comparator;
}