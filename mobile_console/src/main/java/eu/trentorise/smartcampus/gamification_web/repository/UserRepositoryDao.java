package eu.trentorise.smartcampus.gamification_web.repository;

import org.springframework.data.repository.CrudRepository;

import eu.trentorise.smartcampus.gamification_web.repository.User;

public interface UserRepositoryDao extends CrudRepository<User, String>{
	
	public User findByUsername(String username);
	
}
