package eu.trentorise.game.ruleengine.data;

import eu.trentorise.game.ruleengine.model.experimental.Badge;
import eu.trentorise.game.ruleengine.model.experimental.BadgeCollectionPlugin;
import eu.trentorise.game.ruleengine.model.experimental.BadgeCollectionPluginState;
import eu.trentorise.game.ruleengine.model.experimental.ExternalAction;
import eu.trentorise.game.ruleengine.model.experimental.Game;
import eu.trentorise.game.ruleengine.model.experimental.Player;
import eu.trentorise.game.ruleengine.model.experimental.PlayerState;
import eu.trentorise.game.ruleengine.model.experimental.PluginState;
import eu.trentorise.game.ruleengine.model.experimental.PointPlugin;
import eu.trentorise.game.ruleengine.model.experimental.PointPluginState;
import eu.trentorise.game.ruleengine.model.experimental.State;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;



/**
 * 
 * @author Luca Piras
 */
@Repository("rovGameFactsDAO")
public class RovGameFactsDAO implements IFactsDAO {

    @Override
    public Collection getFacts() {
        List elements = new ArrayList();
        
        String greenLeavesName = "greenLeaves";
        String rovGameName = "rovGame";
        String ecologicalBadgesName = "ecologicalBadges";
        String greenHeroFirstLevelName = "Green Hero Level 1";
        
        Badge greenHeroNovice = new Badge();
        greenHeroNovice.setName("Green Hero Novice");
        
        Badge greenHeroFirstLevel = new Badge();
        greenHeroFirstLevel.setName(greenHeroFirstLevelName);
        
        Badge greenHeroSecondLevel = new Badge();
        greenHeroSecondLevel.setName("Green Hero Level 2");
        
        //ExternalAction elements
        Map<String, Object> params = new HashMap<>();
        params.put("bikeKM", 20);
        params.put("busKM", 30);
        
        PointPluginState pointPluginState = new PointPluginState();
        pointPluginState.setTotalPoints(0);
        
        
        Collection<Badge> earnedBadges = new ArrayList<>();
        earnedBadges.add(greenHeroNovice);
        
        BadgeCollectionPluginState badgeCollectionPluginState = new BadgeCollectionPluginState();
        badgeCollectionPluginState.setEarnedBadges(earnedBadges);
        
        
        //Integer customizedPluginId
        Map<String, PluginState> rovGamePlayerCustomizedPointPluginStates = new HashMap<>();
        rovGamePlayerCustomizedPointPluginStates.put(greenLeavesName, pointPluginState);
        
        //Integer customizedPluginName
        Map<String, PluginState> rovGamePlayerCustomizedBadgeCollectionPluginStates = new HashMap<>();
        rovGamePlayerCustomizedBadgeCollectionPluginStates.put(ecologicalBadgesName, badgeCollectionPluginState);
        
        //Class pluginClass 
        //Integer pluginId
        Map<Class, Map<String, PluginState>> rovGamePlayerPluginStates = new HashMap<>();
        rovGamePlayerPluginStates.put(PointPlugin.class, rovGamePlayerCustomizedPointPluginStates);
        rovGamePlayerPluginStates.put(BadgeCollectionPlugin.class, rovGamePlayerCustomizedBadgeCollectionPluginStates);
        
        PlayerState rovGamePlayerState = new PlayerState();
        rovGamePlayerState.setPluginStates(rovGamePlayerPluginStates);
        
        
        
        Game game = new Game();
        game.setName(rovGameName);
        
        //Integer gameName
        Map<String, PlayerState> playerGameStates = new HashMap<>();
        playerGameStates.put(game.getName(), rovGamePlayerState);
        
        Player player = new Player();
        player.setId(1);
        player.setGameStates(playerGameStates);
        
        ExternalAction extAct = new ExternalAction();
        extAct.setName("saveItinerary");
        extAct.setParams(params);
        extAct.setPlayer(player);
        
        elements.add(extAct);
        //elements.add(game);
        
        
        //PointPluginState state = (PointPluginState) player.getGameStates().get(game.getId()).getPluginStates().get(PointPlugin.class).get(greenLeavesName);
        //state.setTotalPoints(state.getTotalPoints() + );
        
        
        //when greenleaves points of a player > 20
        /*PointPluginState ppState = (PointPluginState) player.getGameStates().get(game.getId()).getPluginStates().get(PointPlugin.class).get(greenLeavesName);
        ppState.getTotalPoints();*/
        //il player non ha il badge green hero level 1
        /*BadgeCollectionPluginState bcpState = (BadgeCollectionPluginState) player.getGameStates().get(game.getId()).getPluginStates().get(BadgeCollectionPlugin.class).get(ecologicalBadgesName);
        Badge badge = new Badge();
        badge.setName(greenHeroFirstLevelName);
        boolean contains = bcpState.getEarnedBadges().contains(badge);*/
        
        //then
        //aggiungi allo stato del player relativo al badgecollection ecological badges il badge green hero level 1
        /*bcpState.getEarnedBadges().add(badge);*/
        
        //Boolean b = ((PointPluginState) player.getGameStates().get(game.getId()).getPluginStates().get(PointPlugin.class).get("greenLeaves")).getTotalPoints() > 20;
        //b = ((BadgeCollectionPluginState) player.getGameStates().get(game.getId()).getPluginStates().get(BadgeCollectionPlugin.class).get("ecologicalBadges")).getEarnedBadges().contains(new Badge("Green Hero Level 1"));
        
        
        /*elements.add(new State("A"));
        elements.add(new State("B"));
        elements.add(new State("C"));
        elements.add(new State("D"));
        
        PointPluginState pointPluginState1 = new PointPluginState();
        pointPluginState1.setTotalPoints(0);
        elements.add(pointPluginState1);
        
        BadgeCollectionPluginState badgeCollectionPluginState1 = new BadgeCollectionPluginState();
        Collection<Badge> badges = new ArrayList<>();
        badges.add(greenHeroNovice);
        badgeCollectionPluginState1.setEarnedBadges(badges);
        elements.add(badgeCollectionPluginState1);*/
        
        return elements;
    }
}