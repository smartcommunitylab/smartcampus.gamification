package eu.trentorise.game.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.game.model.TaskData;

@Repository
public interface TaskDataRepo extends CrudRepository<TaskData, String> {
}
