package ru.otus.jsonhandler.dataprocessor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jsonhandler.model.Measurement;

public class ProcessorAggregator implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ProcessorAggregator.class);

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        Map<String, Double> processed = data.stream()
                .collect(Collectors.groupingBy(Measurement::name, Collectors.summingDouble(Measurement::value)));
        logger.atInfo()
                .setMessage("Aggregated measurements with result: {}")
                .addArgument(processed)
                .log();
        return processed;
    }
}
