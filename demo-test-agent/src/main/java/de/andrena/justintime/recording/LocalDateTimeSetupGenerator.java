package de.andrena.justintime.recording;

import static net.amygdalum.testrecorder.deserializers.Templates.assignLocalVariableStatement;
import static net.amygdalum.testrecorder.deserializers.Templates.callMethod;
import static net.amygdalum.testrecorder.util.Types.mostSpecialOf;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.amygdalum.testrecorder.deserializers.Adaptor;
import net.amygdalum.testrecorder.deserializers.Deserializer;
import net.amygdalum.testrecorder.deserializers.builder.DefaultSetupGenerator;
import net.amygdalum.testrecorder.types.Computation;
import net.amygdalum.testrecorder.types.DeserializerContext;
import net.amygdalum.testrecorder.types.TypeManager;
import net.amygdalum.testrecorder.util.Literals;
import net.amygdalum.testrecorder.values.SerializedImmutable;

public class LocalDateTimeSetupGenerator extends DefaultSetupGenerator<SerializedImmutable<LocalDateTime>> implements Adaptor<SerializedImmutable<LocalDateTime>> {

	@SuppressWarnings("rawtypes")
	@Override
	public Class<SerializedImmutable> getAdaptedClass() {
		return SerializedImmutable.class;
	}

	@Override
	public boolean matches(Type type) {
		return type.equals(LocalDateTime.class);
	}

	@Override
	public Computation tryDeserialize(SerializedImmutable<LocalDateTime> value, Deserializer generator) {
		DeserializerContext context = generator.getContext();
		TypeManager types = context.getTypes();
		types.registerTypes(LocalDateTime.class);

		LocalDateTime date = value.getValue();

		List<String> statements = new ArrayList<>();
		String dateexpression = context.newLocal("date");
		String year = Literals.asLiteral(date.getYear());
		String month = Literals.asLiteral(date.getMonthValue());
		String day = Literals.asLiteral(date.getDayOfMonth());
		String hour = Literals.asLiteral(date.getHour());
		String min = Literals.asLiteral(date.getMinute());
		String sec = Literals.asLiteral(date.getSecond());
		String nano = Literals.asLiteral(date.getNano());
		statements.add(assignLocalVariableStatement(types.getVariableTypeName(LocalDateTime.class), dateexpression, callMethod(types.getRawTypeName(LocalDateTime.class), "of", year, month, day, hour, min, sec, nano)));

		return Computation.expression(dateexpression, mostSpecialOf(value.getUsedTypes()).orElse(Object.class), statements);
	}

}
