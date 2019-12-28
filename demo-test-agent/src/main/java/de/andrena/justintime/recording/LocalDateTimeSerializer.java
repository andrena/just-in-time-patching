package de.andrena.justintime.recording;

import static java.util.Arrays.asList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import net.amygdalum.testrecorder.types.Serializer;
import net.amygdalum.testrecorder.types.SerializerSession;
import net.amygdalum.testrecorder.values.SerializedImmutable;

public class LocalDateTimeSerializer implements Serializer<SerializedImmutable<LocalDateTime>> {

	public LocalDateTimeSerializer() {
    }

    @Override
    public List<Class<?>> getMatchingClasses() {
        return asList(LocalDateTime.class);
    }
    
    @Override
    public Stream<?> components(Object object, SerializerSession session) {
    	return Stream.empty();
    }

    @Override
    public SerializedImmutable<LocalDateTime> generate(Class<?> type, SerializerSession session) {
        return new SerializedImmutable<>(type);
    }

    @Override
    public void populate(SerializedImmutable<LocalDateTime> serializedObject, Object object, SerializerSession session) {
        serializedObject.setValue(((LocalDateTime) object));
    }

}
