package eu.trentorise.game.repo;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PlayerRepoCustom {

	public void customMethod(List<String> projectionFields);
}
