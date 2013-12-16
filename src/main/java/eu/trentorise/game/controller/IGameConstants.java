package eu.trentorise.game.controller;

/**
 *
 * @author luca
 */
public interface IGameConstants {
    
    /*views and services*/
    public static final String VIEW_PATH = "/game/";
    public static final String SERVICE_PATH = VIEW_PATH + "services";
    public static final String SERVICE_GAME_PROFILE = SERVICE_PATH + "/gameProfile";
    public static final String VIEW_INTERNAL_NAMESPACE = "game.";
    
    public static final String VIEW_PAGE_EXTENSION = "htm";
    public static final String SERVICE_EXTENSION = "service";
    public static final String VIEW_PAGE_EXTENSION_SEPARATOR = ".";
    public static final String SERVICE_EXTENSION_SEPARATOR = VIEW_PAGE_EXTENSION_SEPARATOR;
    public static final String SERVICE_SEPARATOR_PLUS_EXTENSION = SERVICE_EXTENSION_SEPARATOR + SERVICE_EXTENSION;
}