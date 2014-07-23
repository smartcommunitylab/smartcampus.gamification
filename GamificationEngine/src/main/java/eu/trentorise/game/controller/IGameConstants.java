package eu.trentorise.game.controller;

/**
 *
 * @author luca
 */
public interface IGameConstants {
    
    /*views and services*/
    public static final String VIEW_PATH = "/game/";
    public static final String VIEW_INTERNAL_NAMESPACE = "game.";
    
    
    /*parameters*/
    /*game configuration*/
    public static final String PARAM_GAME_ID = "gameId";
    public static final String PARAM_GAMIFIABLE_ACTIONS_FILE = "gamifiableActionsFile";
    /*badgeCollection - badge*/
    public static final String PARAM_BADGECOLLECTION_ID = "badgeCollectionId";
    public static final String PARAM_BADGECOLLECTION_PLUGIN_ID = "badgeCollectionPluginId";
    public static final String PARAM_BADGECOLLECTION_BADGE_FILE = "badgeCollectionBadgeFile";
    
    
    /*views and services - services -*/
    public static final String SERVICE_PATH = VIEW_PATH + "services";
    /*views and services - services - profile*/
    public static final String SERVICE_GAME_AAA_PATH = SERVICE_PATH + "/AAA";
    /*views and services - services - profile*/
    public static final String SERVICE_GAME_PROFILE_PATH = SERVICE_PATH + "/profile";
    /*views and services - services - profile*/
    public static final String SERVICE_GAME_PROFILE_GAME_PATH = SERVICE_GAME_PROFILE_PATH + "/game";
    /*views and services - services - player*/
    public static final String SERVICE_GAME_PLAYER = SERVICE_PATH + "/player";
    /*views and services - services - configuration*/
    public static final String SERVICE_GAME_CONFIGURATION = SERVICE_PATH + "/configuration";
    /*views and services - services - plugins*/
    public static final String SERVICE_PLUGINS_PATH = SERVICE_PATH + "/plugins";
    public static final String SERVICE_PLUGINS_POINT_PATH = SERVICE_PLUGINS_PATH + "/point";
    public static final String SERVICE_PLUGINS_BADGECOLLECTION_PATH = SERVICE_PLUGINS_PATH + "/badgeCollection";
    public static final String SERVICE_PLUGINS_BADGECOLLECTION_BADGE_PATH = SERVICE_PLUGINS_BADGECOLLECTION_PATH + "/badge";
    public static final String SERVICE_PLUGINS_LEADERBOARDPOINT_PATH = SERVICE_PLUGINS_PATH + "/leaderboardPoint";
    /*views and services - services - actions*/
    public static final String SERVICE_ACTION_PATH = SERVICE_PATH + "/action";
    public static final String SERVICE_ACTION_PARAM_PATH = SERVICE_ACTION_PATH + "/param";
    /*views and services - services - ruleengine*/
    public static final String SERVICE_RULEENGINE_PATH = SERVICE_PATH + "/ruleEngine";
    public static final String SERVICE_RULEENGINE_TEMPLATERULE_PATH = SERVICE_RULEENGINE_PATH + "/templateRule";
    /*views and services - services - events*/
    public static final String SERVICE_EVENTS_PATH = SERVICE_PATH + "/events";
    public static final String SERVICE_EVENTS_START_EVENT_PATH = SERVICE_EVENTS_PATH + "/startEvent";
    
    
    public static final String VIEW_PAGE_EXTENSION = "htm";
    public static final String SERVICE_EXTENSION = "service";
    public static final String VIEW_PAGE_EXTENSION_SEPARATOR = ".";
    public static final String SERVICE_EXTENSION_SEPARATOR = VIEW_PAGE_EXTENSION_SEPARATOR;
    public static final String SERVICE_SEPARATOR_PLUS_EXTENSION = SERVICE_EXTENSION_SEPARATOR + SERVICE_EXTENSION;
}