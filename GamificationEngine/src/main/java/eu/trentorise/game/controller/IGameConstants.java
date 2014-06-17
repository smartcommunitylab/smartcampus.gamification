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
    public static final String SERVICE_GAME_AAA = SERVICE_PATH + "/AAA";
    /*views and services - services - profile*/
    public static final String SERVICE_GAME_PROFILE = SERVICE_PATH + "/profile";
    /*views and services - services - profile*/
    public static final String SERVICE_GAME_PROFILE_GAME = SERVICE_GAME_PROFILE + "/game";
    /*views and services - services - player*/
    public static final String SERVICE_GAME_PLAYER = SERVICE_PATH + "/player";
    /*views and services - services - plugins*/
    public static final String SERVICE_PLUGINS_PATH = SERVICE_PATH + "/plugins";
    public static final String SERVICE_PLUGINS_POINT = SERVICE_PLUGINS_PATH + "/point";
    /*views and services - services - events*/
    public static final String SERVICE_EVENTS_PATH = SERVICE_PATH + "/events";
    public static final String SERVICE_EVENTS_START_EVENT = SERVICE_EVENTS_PATH + "/startEvent";
    
    
    public static final String VIEW_PAGE_EXTENSION = "htm";
    public static final String SERVICE_EXTENSION = "service";
    public static final String VIEW_PAGE_EXTENSION_SEPARATOR = ".";
    public static final String SERVICE_EXTENSION_SEPARATOR = VIEW_PAGE_EXTENSION_SEPARATOR;
    public static final String SERVICE_SEPARATOR_PLUS_EXTENSION = SERVICE_EXTENSION_SEPARATOR + SERVICE_EXTENSION;
}