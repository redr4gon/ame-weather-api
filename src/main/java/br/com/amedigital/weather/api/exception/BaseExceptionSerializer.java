package br.com.amedigital.weather.api.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class BaseExceptionSerializer extends StdSerializer<BaseRuntimeException> {


    public BaseExceptionSerializer() {
        this(null);
    }

    protected BaseExceptionSerializer(Class<BaseRuntimeException> t) {
        super(t);
    }

    @Override
    public void serialize(BaseRuntimeException value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();

        jgen.writeStringField("error", value.getErrorCode());
        jgen.writeStringField("error_description", value.getErrorDescription());

        if (value.getFields() != null && !value.getFields().isEmpty())
            jgen.writeObjectField("fields", value.getFields());

        jgen.writeEndObject();
    }
}
