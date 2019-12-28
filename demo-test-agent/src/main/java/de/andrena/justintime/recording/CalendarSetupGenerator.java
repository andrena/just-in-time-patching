package de.andrena.justintime.recording;

import static net.amygdalum.testrecorder.deserializers.Templates.assignLocalVariableStatement;
import static net.amygdalum.testrecorder.deserializers.Templates.callMethod;
import static net.amygdalum.testrecorder.deserializers.Templates.callMethodStatement;
import static net.amygdalum.testrecorder.util.Literals.asLiteral;
import static net.amygdalum.testrecorder.util.Types.mostSpecialOf;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.amygdalum.testrecorder.deserializers.Adaptor;
import net.amygdalum.testrecorder.deserializers.Deserializer;
import net.amygdalum.testrecorder.deserializers.builder.DefaultSetupGenerator;
import net.amygdalum.testrecorder.types.Computation;
import net.amygdalum.testrecorder.types.DeserializerContext;
import net.amygdalum.testrecorder.types.TypeManager;
import net.amygdalum.testrecorder.util.Types;
import net.amygdalum.testrecorder.values.SerializedImmutable;

public class CalendarSetupGenerator extends DefaultSetupGenerator<SerializedImmutable<Calendar>> implements Adaptor<SerializedImmutable<Calendar>> {

	@SuppressWarnings("rawtypes")
    @Override
	public Class<SerializedImmutable> getAdaptedClass() {
		return SerializedImmutable.class;
	}

	@Override
	public boolean matches(Type type) {
		return Calendar.class.isAssignableFrom(Types.baseType(type));
	}

	@Override
	public Computation tryDeserialize(SerializedImmutable<Calendar> value, Deserializer generator) {
		DeserializerContext context = generator.getContext();
        TypeManager types = context.getTypes();
        types.registerTypes(Calendar.class);

        Calendar cal = value.getValue();

        List<String> statements = new ArrayList<>();
        String calexpression = context.newLocal("cal");
        statements.add(assignLocalVariableStatement(types.getVariableTypeName(Calendar.class), calexpression, callMethod(types.getRawTypeName(Calendar.class), "getInstance")));
        statements.add(callMethodStatement(calexpression, "setTimeInMillis", asLiteral(cal.getTimeInMillis())));

        return Computation.expression(calexpression, mostSpecialOf(value.getUsedTypes()).orElse(Object.class), statements);
	}

}
