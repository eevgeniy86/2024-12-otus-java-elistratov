package ru.otus.jsonhandler.dataprocessor;

import java.util.List;
import ru.otus.jsonhandler.model.Measurement;

public interface Loader {

    List<Measurement> load();
}
