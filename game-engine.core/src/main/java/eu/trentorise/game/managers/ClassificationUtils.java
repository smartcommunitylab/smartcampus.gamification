package eu.trentorise.game.managers;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.PointConcept.PeriodInstance;
import eu.trentorise.game.model.core.GameConcept;

public class ClassificationUtils {

	static DateTimeFormatter dtf = DateTimeFormat
			.forPattern("yyyy-MM-dd'T'HH:mm:ss");

	public static PeriodInstance retrieveWindow(Game g, String periodIdentifier, String pointClassificationName, long moment, int instanceIndex) {

		PeriodInstance periodInstance = null;
		if (g != null) {
			for (GameConcept gc : g.getConcepts()) {
				if (gc instanceof PointConcept) {
					PointConcept pc = (PointConcept) gc;
					if (pc.getName().equals(pointClassificationName)) {

						// there is one period instance per classification name.
						if (moment > -1) {
							periodInstance = pc.getPeriodInstance(
									periodIdentifier, moment);
							break;
						} else if (instanceIndex > -1) {
							periodInstance = pc.getPeriodInstance(
									periodIdentifier, instanceIndex);
							break;
						}
					}
				}
			}
		}
		return periodInstance;

	}

	public static String generateKey(PeriodInstance pi) {
		String key = null;
		LocalDateTime d = new LocalDateTime(pi.getStart());
		key = dtf.print(d);
		return key;
	}

    public static double calculateVariance(List<Double> numArray) {
		{
			double sum = 0.0, variance = 0.0;
            int length = numArray.size();

			for (double num : numArray) {
				sum += num;
			}

			double mean = sum / length;

			for (double num : numArray) {
				variance += Math.pow(num - mean, 2);
			}

			return (variance / length);
		}
	}

}
