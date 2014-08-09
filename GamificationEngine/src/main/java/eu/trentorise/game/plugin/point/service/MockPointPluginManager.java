package eu.trentorise.game.plugin.point.service;

import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("mockPointPluginManager")
public class MockPointPluginManager implements IRestCrudManager<PointPlugin, Object, PointPlugin>{

    public static MockPointPluginManager createInstance() {
        return new MockPointPluginManager();
    }
    
    @Override
    public PointPlugin createSingleElement(PointPlugin containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to create a
        //new one
        
        return manager.createGreenLeavesPointPlugin();
    }

    @Override
    public Collection<PointPlugin> readCollection(Object containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PointPlugin readSingleElement(PointPlugin containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PointPlugin updateSingleElement(PointPlugin containerWithForeignIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PointPlugin deleteSingleElement(PointPlugin containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Qualifier("mockPluginManager")
    @Autowired
    protected MockPluginManager manager;
}