package ru.otus.jsonhandler.dataprocessor;

import java.util.List;
import java.util.Map;
import ru.otus.jsonhandler.model.Measurement;

public interface Processor {

    Map<String, Double> process(List<Measurement> data);
}
