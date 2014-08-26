package eu.trentorise.game.profile.game.service;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.profile.game.comparator.GameKeyComparator;
import eu.trentorise.game.profile.game.model.Game;
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
@Service("mockGameProfileManager")
public class MockGameProfileManager implements IRestCrudManager<Game, Object, Game>,
                                               IRestCrudTestManager<Game, Object, Game> {

    public static final int MOCK_GAME_ID = IGameConstants.SEQUENCE_INITIAL_VALUE;
    
    public static MockGameProfileManager createInstance() {
        MockGameProfileManager mock = new MockGameProfileManager();
        
        mock.comparator = new GameKeyComparator();
        
        return mock;
    }
    
    
    @Override
    public Game createSingleElement(Game containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to create a
        //new one
        containerWithForeignIds.setId(MOCK_GAME_ID);
        return containerWithForeignIds;
    }

    @Override
    public Collection<Game> readCollection(Object containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        return this.createElements(null);
    }

    @Override
    public Game readSingleElement(Game containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        Game returnValue = null;
        
        Game expectedElement = this.createElement(null);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }

    @Override
    public Game updateSingleElement(Game containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to update a
        //that one
        
        Game returnValue = null;
        
        Game expectedElement = this.createElement(null);
        if (0 == comparator.compare(containerWithForeignIds, expectedElement)) {
            returnValue = containerWithForeignIds;
        }
        
        return returnValue;
    }

    @Override
    public Game deleteSingleElement(Game containerWithIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to delete
        //that one or if it is not present
        
        Game returnValue = null;
        
        Game expectedElement = this.createElement(null);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }
    
    
    @Override
    public Game createElement(Game containerWithIds) throws Exception {
        Game element = new Game();
        element.setId(MockGameProfileManager.MOCK_GAME_ID);
        element.setName("RoveretoGame");
        return element;
    }

    @Override
    public Collection<Game> createElements(Object containerWithIds) throws Exception {
        List<Game> elements = new ArrayList<>();
        elements.add(this.createElement(null));
        return elements;
    }

    
    public Comparator<Game> getComparator() {
        return comparator;
    }
    
    
    @Qualifier("gameKeyComparator")
    @Autowired
    protected Comparator<Game> comparator;
}