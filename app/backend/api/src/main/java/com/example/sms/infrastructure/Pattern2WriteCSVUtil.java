package com.example.sms.infrastructure;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class Pattern2WriteCSVUtil<T>{
    @FunctionalInterface
    public interface CSVWriterFunction<T> {
        void accept(OutputStreamWriter streamWriter, List<T> list) throws Exception;
    }

    public static <T> CSVWriterFunction<T> writeCsv(Class<T> clazz) {
        return (streamWriter, csvList) -> {
            CustomMappingStrategy<T> mappingStrategy = new CustomMappingStrategy<>();
            mappingStrategy.setType(clazz);
            StatefulBeanToCsv<T> writer = new StatefulBeanToCsvBuilder<T>(streamWriter)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withMappingStrategy(mappingStrategy)
                    .withOrderedResults(false)
                    .build();
            writer.write(csvList);
            streamWriter.flush();
        };
    }
}

