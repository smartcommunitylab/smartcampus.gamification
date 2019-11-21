package it.smartcommunitylab.model.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PointConceptControllerUtils {

	ObjectMapper mapper = new ObjectMapper();

	public List<PointConcept> convertToSpecificType(Map[] objs)
			throws IllegalArgumentException, ClassNotFoundException {
		List<PointConcept> result = new ArrayList<>();
		for (Map pConcept : objs) {
			result.add((PointConcept) mapper.convertValue(pConcept, PointConcept.class));
		}
		return result;

	}

	public PointConcept convertToSpecificType(Map obj) throws IllegalArgumentException, ClassNotFoundException {

		return mapper.convertValue(obj, PointConcept.class);

	}

}
