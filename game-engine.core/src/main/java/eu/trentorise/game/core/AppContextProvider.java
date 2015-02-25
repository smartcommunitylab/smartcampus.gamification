package eu.trentorise.game.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppContextProvider implements ApplicationContextAware {

	private static ApplicationContext ctx;

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {

		ctx = arg0;
	}

	public ApplicationContext getApplicationContext() {
		return ctx;
	}

}
