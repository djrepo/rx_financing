package lu.crx.financing.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class CsvReader {

    private static ObjectReader createReader(Class def) {
        CsvSchema schema = createSchema(def, true);
        CsvMapper mapper = createBasicMapper();
        ObjectReader reader = mapper.readerFor(def).with(schema);
        reader = reader.without(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        return reader;
    }

    public static <T> List<T> load(File dataFile, Class<T> clazz) throws IOException {
        ObjectReader reader = createReader(clazz);
        MappingIterator<T> readValues = reader.readValues(dataFile);
        List<T> rows = readValues.readAll();
        return rows;
    }

    private static CsvMapper createBasicMapper() {
        CsvMapper mapper = new CsvMapper();
        mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        ObjectMapper objectMapper = mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    private static CsvSchema createSchema(Class def, boolean header) {
        Field[] declaredFields = def.getDeclaredFields();
        int index = 0;
        List<CsvSchema.Column> columns = new ArrayList<>();
        for (Field fieldInfo : declaredFields) {
            CsvSchema.ColumnType columnType = convertToCsvType(fieldInfo.getType());
            columns.add(new CsvSchema.Column(index++, fieldInfo.getName(), columnType));
        }
        CsvSchema schema = CsvSchema.builder().setUseHeader(header)
                .setColumnSeparator('|')
                .disableQuoteChar()
                .addColumns(columns)
                .build();
        return schema;
    }

    private static CsvSchema.ColumnType convertToCsvType(Class<?> type) {
        if (type.isAssignableFrom(long.class)) {
            return CsvSchema.ColumnType.NUMBER;
        }
        if (type.isAssignableFrom(int.class)) {
            return CsvSchema.ColumnType.NUMBER;
        }
        if (type.isAssignableFrom(double.class)) {
            return CsvSchema.ColumnType.NUMBER;
        }
        if (type.isAssignableFrom(String.class)) {
            return CsvSchema.ColumnType.STRING;
        }
        if (type.isAssignableFrom(boolean.class)) {
            return CsvSchema.ColumnType.BOOLEAN;
        }
        if (type.isAssignableFrom(long.class)) {
            return CsvSchema.ColumnType.NUMBER;
        }
        if (type.isAssignableFrom(LocalDate.class)) {
            return CsvSchema.ColumnType.STRING;
        }
        return CsvSchema.ColumnType.NUMBER_OR_STRING;
    }


}

