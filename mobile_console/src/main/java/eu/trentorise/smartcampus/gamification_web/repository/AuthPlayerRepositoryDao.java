package eu.trentorise.smartcampus.gamification_web.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthPlayerRepositoryDao extends CrudRepository<AuthPlayer, String>{
	
	public AuthPlayer findByPid(String id);
	
	public AuthPlayer findByMail(String mail);
	
	public AuthPlayer findByPidAndType(String id, String type);
	
	public AuthPlayer findByMailAndType(String mail, String type);

}
