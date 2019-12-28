package de.andrena.justintime.recording;

import static java.util.Arrays.asList;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Stream;

import net.amygdalum.testrecorder.types.Serializer;
import net.amygdalum.testrecorder.types.SerializerSession;
import net.amygdalum.testrecorder.values.SerializedImmutable;

public class CalendarSerializer implements Serializer<SerializedImmutable<Calendar>> {

	public CalendarSerializer() {
    }

    @Override
    public List<Class<?>> getMatchingClasses() {
        return asList(Calendar.class, GregorianCalendar.class);
    }
    
    @Override
    public Stream<?> components(Object object, SerializerSession session) {
    	return Stream.empty();
    }

    @Override
    public SerializedImmutable<Calendar> generate(Class<?> type, SerializerSession session) {
        return new SerializedImmutable<>(type);
    }

    @Override
    public void populate(SerializedImmutable<Calendar> serializedObject, Object object, SerializerSession session) {
    	Calendar date = (Calendar) ((Calendar) object).clone();
        serializedObject.setValue(date);
    }

}
