package eu.trentorise.game.application.service;

import eu.trentorise.game.action.model.Application;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public interface IApplicationManager {

    public Collection<Application> readApplications() throws Exception;

    public Application readApplicationById(Integer appId) throws Exception;
}