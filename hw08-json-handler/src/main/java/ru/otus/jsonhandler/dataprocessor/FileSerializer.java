package ru.otus.jsonhandler.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSerializer implements Serializer {
    private static final Logger logger = LoggerFactory.getLogger(FileSerializer.class);
    private final File file;
    private final ObjectMapper mapper;

    public FileSerializer(String fileName) {

        file = new File(fileName);
        this.mapper = JsonMapper.builder().build();
    }

    @Override
    public void serialize(Map<String, Double> data) throws FileProcessException {
        try {
            FileOutputStream output = new FileOutputStream(file);
            mapper.writeValue(output, data);
            output.close();
        } catch (IOException ioe) {
            logger.atError().setMessage(ioe.getMessage()).log();
            throw new FileProcessException(ioe);
        }
        logger.atInfo()
                .setMessage("Serialized data {}, to file: {}")
                .addArgument(data)
                .addArgument(file.getAbsolutePath())
                .log();
    }
}
