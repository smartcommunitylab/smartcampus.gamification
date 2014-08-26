package eu.trentorise.game.application.service;

import com.google.common.collect.Sets;
import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.annotation.TransactionalGame;
import eu.trentorise.game.annotation.TransactionalGameReadOnly;
import eu.trentorise.game.application.data.ApplicationRepository;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("applicationManager")
public class ApplicationManager implements IRestCrudManager<Application, Object, Application> {
    
    //CREATE
    @TransactionalGame
    @Override
    public Application createSingleElement(Application containerWithForeignIds) throws Exception {
        return repository.save(containerWithForeignIds);
    }
    
    
    //READ
    @TransactionalGameReadOnly
    @Override
    public Collection<Application> readCollection(Object container) throws Exception {
        return Sets.newConcurrentHashSet(repository.findAll());
    }

    @TransactionalGameReadOnly
    @Override
    public Application readSingleElement(Application containerWithIds) throws Exception {
        return repository.findOne(containerWithIds.getId());
    }
    
    
    //UPDATE
    @TransactionalGame
    @Override
    public Application updateSingleElement(Application containerWithForeignIds) throws Exception {
        Application result = repository.findOne(containerWithForeignIds.getId());
        
        result.setName(containerWithForeignIds.getName());
        
        return result;
    }
    
    
    //DELETE
    @TransactionalGame
    @Override
    public Application deleteSingleElement(Application containerWithIds) throws Exception {
        Application result = repository.findOne(containerWithIds.getId());
        
        repository.delete(containerWithIds.getId());
        
        return result;
    }
    
    
    @Autowired
    protected ApplicationRepository repository;
}