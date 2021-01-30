package bg.sofia.uni.fmi.ai.id3;

import java.util.Map;

public class OccurrencesData {
	Map<Feature, Map<Category, Integer>> occurrences;
	int noRecurrenceEventsNumber = 0;
	int recurrenceEventsNumber = 0;

	public OccurrencesData(Map<Feature, Map<Category, Integer>> occurrences, int noRecurrenceEventsNumber,
			int recurrenceEventsNumber) {
		this.occurrences = occurrences;
		this.noRecurrenceEventsNumber = noRecurrenceEventsNumber;
		this.recurrenceEventsNumber = recurrenceEventsNumber;
	}
}
