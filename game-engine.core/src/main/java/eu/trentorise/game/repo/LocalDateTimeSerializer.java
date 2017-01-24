package eu.trentorise.game.repo;

import java.io.IOException;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import eu.trentorise.game.model.PointConcept;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

	@Override
	public void serialize(LocalDateTime value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeFieldName(value.toString(PointConcept.PERIOD_KEY_FORMAT));
	}

}
