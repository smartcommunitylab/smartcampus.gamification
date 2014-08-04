package eu.trentorise.game.profile.game.service;

import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.utils.rest.ICrudManager;
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
public class MockGameProfileManager implements ICrudManager<Game, Object, Game> {

    public static MockGameProfileManager createInstance() {
        return new MockGameProfileManager();
    }
    
    
    @Override
    public Game createSingleElement(Game containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to create a
        //new one
        containerWithForeignIds.setId(135);
        return containerWithForeignIds;
    }

    @Override
    public Collection<Game> readCollection(Object containerWithIds) throws Exception {
        return this.createElements();
    }

    @Override
    public Game readSingleElement(Game containerWithIds) throws Exception {
        Game returnValue = null;
        
        Game expectedElement = this.createRoveretoGame();
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
        
        Game expectedElement = this.createRoveretoGame();
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
        
        Game expectedElement = this.createRoveretoGame();
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }
    

    public Collection<Game> createElements() {
        List<Game> elements = new ArrayList<>();
        elements.add(this.createRoveretoGame());
        return elements;
    }
    
    public Game createRoveretoGame() {
        Game element = new Game();
        element.setId(135);
        element.setName("RoveretoGame");
        return element;
    }
    
    @Qualifier("gameKeyComparator")
    @Autowired
    protected Comparator<Game> comparator;
}