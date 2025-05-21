package ru.otus.jsonhandler.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jsonhandler.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private static final Logger logger = LoggerFactory.getLogger(ResourcesFileLoader.class);
    private final ObjectMapper mapper;
    private final File file;

    public ResourcesFileLoader(String fileName) throws RuntimeException {
        URI path;
        try {
            path = Objects.requireNonNull(ResourcesFileLoader.class.getResource(String.format("/%s", fileName)))
                    .toURI();
        } catch (Exception e) {
            logger.atError().setMessage(e.getMessage()).log();
            throw new FileProcessException(e);
        }
        this.file = new File(path);
        this.mapper = JsonMapper.builder().build();
    }

    @Override
    public List<Measurement> load() throws RuntimeException {
        List<Measurement> list;
        try {
            list = Arrays.asList(mapper.readValue(file, Measurement[].class));
        } catch (IOException ioe) {
            logger.atError().setMessage(ioe.getMessage()).log();
            throw new FileProcessException(ioe);
        }
        logger.atInfo()
                .setMessage("Read list of measurements: {}, from file: {}")
                .addArgument(list)
                .addArgument(file.getAbsolutePath())
                .log();
        return list;
    }
}
