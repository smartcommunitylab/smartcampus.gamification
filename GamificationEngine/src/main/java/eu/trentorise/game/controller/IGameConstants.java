package eu.trentorise.game.controller;

/**
 *
 * @author luca
 */
public interface IGameConstants {
    
    /*views and services*/
    public static final String VIEW_PATH = "/game/";
    public static final String VIEW_INTERNAL_NAMESPACE = "game.";
    /*views and services - services -*/
    public static final String SERVICE_PATH = VIEW_PATH + "services";
    /*views and services - services - profile*/
    public static final String SERVICE_GAME_AAA_PATH = SERVICE_PATH + "/aaa";
    /*views and services - services - profile*/
    public static final String SERVICE_GAME_PROFILE_PATH = SERVICE_PATH + "/profile";
    /*views and services - services - applications*/
    public static final String SERVICE_APPLICATIONS_PATH = SERVICE_PATH + "/applications";
    public static final String SERVICE_APPLICATIONS_SINGLE_PATH_PARAM = "appId";
    public static final String SERVICE_APPLICATIONS_SINGLE_PATH = SERVICE_APPLICATIONS_PATH + "/{" + SERVICE_APPLICATIONS_SINGLE_PATH_PARAM + "}";
    /*views and services - services - actions*/
    /*views and services - services - actions - externalActions*/
    public static final String SERVICE_EXTERNALACTIONS_PATH = SERVICE_APPLICATIONS_SINGLE_PATH + "/externalactions";
    public static final String SERVICE_EXTERNALACTIONS_SINGLE_PATH_PARAM = "extActId";
    public static final String SERVICE_EXTERNALACTIONS_SINGLE_PATH = SERVICE_EXTERNALACTIONS_PATH + "/{" + SERVICE_EXTERNALACTIONS_SINGLE_PATH_PARAM + "}";
    public static final String SERVICE_ACTION_PARAM_PATH = SERVICE_EXTERNALACTIONS_PATH + "/param";
    /*views and services - services - profile - games*/
    public static final String SERVICE_GAME_PROFILE_GAMES_PATH = SERVICE_GAME_PROFILE_PATH + "/games";
    public static final String SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM = "gameId";
    public static final String SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH = SERVICE_GAME_PROFILE_GAMES_PATH + "/{" + SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM + "}";
    /*views and services - services - plugins*/
    public static final String SERVICE_PLUGINS_PATH_RELATIVE = "/plugins";
    public static final String SERVICE_PLUGINS_PATH = SERVICE_PATH + SERVICE_PLUGINS_PATH_RELATIVE;
    public static final String SERVICE_PLUGINS_SINGLE_PATH_PARAM = "plugId";
    public static final String SERVICE_PLUGINS_SINGLE_PATH_RELATIVE = SERVICE_PLUGINS_PATH_RELATIVE + "/{" + SERVICE_PLUGINS_SINGLE_PATH_PARAM + "}";
    public static final String SERVICE_PLUGINS_SINGLE_PATH = SERVICE_PATH + SERVICE_PLUGINS_SINGLE_PATH_RELATIVE;
    /*views and services - services - profile - games - plugins - customizedPlugins */
    public static final String SERVICE_CUSTOMIZEDPLUGINS_PATH_RELATIVE = "/customizedplugins";
    public static final String SERVICE_CUSTOMIZEDPLUGINS_PATH = SERVICE_PLUGINS_SINGLE_PATH_RELATIVE + SERVICE_CUSTOMIZEDPLUGINS_PATH_RELATIVE;
    public static final String SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM = "cusPlugId";
    public static final String SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH = SERVICE_CUSTOMIZEDPLUGINS_PATH + "/{" + SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM + "}";
    /*views and services - services - profile - games - plugins - gameCustomizedPlugins */
    public static final String SERVICE_GAMECUSTOMIZEDPLUGINS_PATH = SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH + SERVICE_CUSTOMIZEDPLUGINS_PATH;
    public static final String SERVICE_GAMECUSTOMIZEDPLUGINS_SINGLE_PATH = SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH + SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH;
    /*views and services - services - profile - games - plugins - customizedPlugins - points */
    public static final String SERVICE_PLUGINS_POINT_PATH = SERVICE_PATH + "/point-plugins";
    public static final String SERVICE_PLUGINS_POINT_SINGLE_PATH_PARAM = SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM;
    public static final String SERVICE_PLUGINS_POINT_SINGLE_PATH = SERVICE_PLUGINS_POINT_PATH + "/{" + SERVICE_PLUGINS_POINT_SINGLE_PATH_PARAM + "}";
    /*views and services - services - profile - games - plugins - customizedPlugins - badgeCollections */
    public static final String SERVICE_PLUGINS_BADGECOLLECTION_PATH = SERVICE_PATH + "/badgecollection-plugins";
    public static final String SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH_PARAM = SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM;
    public static final String SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH = SERVICE_PLUGINS_BADGECOLLECTION_PATH + "/{" + SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH_PARAM + "}";
    /*views and services - services - profile - games - plugins - customizedPlugins - badgeCollections - badges*/
    public static final String SERVICE_PLUGINS_BADGECOLLECTION_BADGE_PATH = SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH + "/badges";
    public static final String SERVICE_PLUGINS_BADGECOLLECTION_BADGE_SINGLE_PATH_PARAM = "badgeId";
    public static final String SERVICE_PLUGINS_BADGECOLLECTION_BADGE_SINGLE_PATH = SERVICE_PLUGINS_BADGECOLLECTION_BADGE_PATH + "/{" + SERVICE_PLUGINS_BADGECOLLECTION_BADGE_SINGLE_PATH_PARAM + "}";
    /*views and services - services - profile - games - plugins - customizedPlugins - leaderboardspoint */
    public static final String SERVICE_PLUGINS_LEADERBOARDPOINT_PATH = SERVICE_PATH + "/leaderboardpoint-plugins";
    public static final String SERVICE_PLUGINS_LEADERBOARDPOINT_SINGLE_PATH_PARAM = SERVICE_CUSTOMIZEDPLUGINS_SINGLE_PATH_PARAM;
    public static final String SERVICE_PLUGINS_LEADERBOARDPOINT_SINGLE_PATH = SERVICE_PLUGINS_LEADERBOARDPOINT_PATH + "/{" + SERVICE_PLUGINS_LEADERBOARDPOINT_SINGLE_PATH_PARAM + "}";
    /*views and services - services - player*/
    public static final String SERVICE_GAME_PLAYER = SERVICE_PATH + "/player";
    /*views and services - services - ruleengine*/
    public static final String SERVICE_RULEENGINE_PATH = SERVICE_PATH + "/ruleengine";
     /*views and services - services - ruleengine - ruletemplates*/
    public static final String SERVICE_RULEENGINE_RULETEMPLATES_PATH = SERVICE_RULEENGINE_PATH + SERVICE_PLUGINS_SINGLE_PATH_RELATIVE + "/ruletemplates";
    public static final String SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM = "ruleTemplId";
    public static final String SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH = SERVICE_RULEENGINE_RULETEMPLATES_PATH + "/{" + SERVICE_RULEENGINE_RULETEMPLATES_SINGLE_PATH_PARAM + "}";
    /*views and services - services - events*/
    public static final String SERVICE_EVENTS_PATH = SERVICE_PATH + "/events";
    public static final String SERVICE_EVENTS_START_EVENT_PATH = SERVICE_EVENTS_PATH + "/start-event";
    
    
    public static final String VIEW_PAGE_EXTENSION = "htm";
    public static final String SERVICE_EXTENSION = "service";
    public static final String VIEW_PAGE_EXTENSION_SEPARATOR = ".";
    public static final String SERVICE_EXTENSION_SEPARATOR = VIEW_PAGE_EXTENSION_SEPARATOR;
    public static final String SERVICE_SEPARATOR_PLUS_EXTENSION = SERVICE_EXTENSION_SEPARATOR + SERVICE_EXTENSION;
}