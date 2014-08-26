package eu.trentorise.game.profile.game.service;

import com.google.common.collect.Sets;
import eu.trentorise.game.annotation.TransactionalGame;
import eu.trentorise.game.annotation.TransactionalGameReadOnly;
import eu.trentorise.game.profile.game.data.GameRepository;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("gameProfileManager")
public class GameProfileManager implements IRestCrudManager<Game, Object, Game> {
    
    //CREATE
    @TransactionalGame
    @Override
    public Game createSingleElement(Game containerWithForeignIds) throws Exception {
        return repository.save(containerWithForeignIds);
    }
    
    
    //READ
    @TransactionalGameReadOnly
    @Override
    public Collection<Game> readCollection(Object container) throws Exception {
        return Sets.newConcurrentHashSet(repository.findAll());
    }

    @TransactionalGameReadOnly
    @Override
    public Game readSingleElement(Game containerWithIds) throws Exception {
        return repository.findOne(containerWithIds.getId());
    }
    
    
    //UPDATE
    @TransactionalGame
    @Override
    public Game updateSingleElement(Game containerWithForeignIds) throws Exception {
        Game result = repository.findOne(containerWithForeignIds.getId());
        
        result.setName(containerWithForeignIds.getName());
        
        return result;
    }
    
    
    //DELETE
    @TransactionalGame
    @Override
    public Game deleteSingleElement(Game containerWithIds) throws Exception {
        Game result = repository.findOne(containerWithIds.getId());
        
        repository.delete(containerWithIds.getId());
        
        return result;
    }
    
    
    @Autowired
    protected GameRepository repository;
}