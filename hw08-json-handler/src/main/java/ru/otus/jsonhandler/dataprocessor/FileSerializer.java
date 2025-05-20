package ru.otus.jsonhandler.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSerializer implements Serializer {
    private static final Logger logger = LoggerFactory.getLogger(FileSerializer.class);
    private final File file;
    private final ObjectMapper mapper;

    public FileSerializer(String fileName) {
        this.file = new File(fileName);
        this.mapper = JsonMapper.builder().build();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        SortedMap<String, Double> dataSorted = new TreeMap<>(data);
        try {
            mapper.writeValue(file, dataSorted);
        } catch (IOException ioe) {
            logger.atError().setMessage(ioe.getMessage()).log();
        }
        logger.atInfo().setMessage("Serialized data {}").addArgument(dataSorted).log();
    }
}
