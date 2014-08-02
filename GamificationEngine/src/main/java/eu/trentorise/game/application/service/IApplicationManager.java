package eu.trentorise.game.application.service;

import eu.trentorise.game.action.model.Application;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public interface IApplicationManager {

    public Collection<Application> findApplications() throws Exception;

    public Application findApplicationById(Integer appId) throws Exception;
}