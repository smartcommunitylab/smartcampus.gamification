package eu.trentorise.smartcampus.gamification_web.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthPlayerProdRepositoryDao extends CrudRepository<AuthPlayerProd, String>{
	
	public AuthPlayerProd findByPid(String id);
	
	public AuthPlayerProd findByMail(String mail);

}
