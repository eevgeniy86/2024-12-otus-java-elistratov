package ru.otus.jsonhandler.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jsonhandler.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private static final Logger logger = LoggerFactory.getLogger(ResourcesFileLoader.class);
    private final ObjectMapper mapper;
    private final File file;

    public ResourcesFileLoader(String fileName) {
        this.file = new File(fileName);
        this.mapper = JsonMapper.builder().build();
    }

    @Override
    public List<Measurement> load() {
        List<Measurement> list = null;
        try {
            list = Arrays.asList(mapper.readValue(file, Measurement[].class));
        } catch (IOException ioe) {
            logger.atError().setMessage(ioe.getMessage()).log();
        }
        logger.atInfo()
                .setMessage("Read list of measurements: {}")
                .addArgument(list)
                .log();
        return list;
    }
}
