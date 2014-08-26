package eu.trentorise.game.plugin.service;

import com.google.common.collect.Sets;
import eu.trentorise.game.annotation.TransactionalGame;
import eu.trentorise.game.annotation.TransactionalGameReadOnly;
import eu.trentorise.game.plugin.data.PluginRepository;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("pluginManager")
public class PluginManager implements IRestCrudManager<Plugin, Object, Plugin> {
    
    //CREATE
    @TransactionalGame
    @Override
    public Plugin createSingleElement(Plugin containerWithForeignIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //return repository.save(containerWithForeignIds);
    }
    
    
    //READ
    @TransactionalGameReadOnly
    @Override
    public Collection<Plugin> readCollection(Object container) throws Exception {
        return Sets.newConcurrentHashSet(repository.findAll());
    }

    @TransactionalGameReadOnly
    @Override
    public Plugin readSingleElement(Plugin containerWithIds) throws Exception {
        return repository.findOne(containerWithIds.getId());
    }
    
    
    //UPDATE
    @TransactionalGame
    @Override
    public Plugin updateSingleElement(Plugin containerWithForeignIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        /*Plugin result = repository.findOne(containerWithForeignIds.getId());
        
        result.setName(containerWithForeignIds.getName());
        
        return result;*/
    }
    
    
    //DELETE
    @TransactionalGame
    @Override
    public Plugin deleteSingleElement(Plugin containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        /*Plugin result = repository.findOne(containerWithIds.getId());
        
        repository.delete(containerWithIds.getId());
        
        return result;*/
    }
    
    
    @Autowired
    protected PluginRepository repository;
}