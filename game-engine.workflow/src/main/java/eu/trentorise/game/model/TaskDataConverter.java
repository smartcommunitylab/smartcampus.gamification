package eu.trentorise.game.model;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaskDataConverter {

	private ObjectMapper mapper;

	public TaskDataConverter() {
		mapper = new ObjectMapper();
	}

	public <T> T convert(Object o, Class<T> classtype) {
		return mapper.convertValue(o, classtype);
	}

	public <T> List<T> convert(List<Object> o, Class<T> classtype) {
		return mapper.convertValue(o, new TypeReference<List<T>>() {
		});
	}

}
