package de.andrena.justintime.recording;

import static net.amygdalum.testrecorder.deserializers.Templates.assignLocalVariableStatement;
import static net.amygdalum.testrecorder.deserializers.Templates.callMethod;
import static net.amygdalum.testrecorder.deserializers.Templates.callMethodStatement;
import static net.amygdalum.testrecorder.deserializers.Templates.equalToMatcher;
import static net.amygdalum.testrecorder.types.Computation.expression;
import static net.amygdalum.testrecorder.util.Literals.asLiteral;
import static net.amygdalum.testrecorder.util.Types.parameterized;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import net.amygdalum.testrecorder.deserializers.Adaptor;
import net.amygdalum.testrecorder.deserializers.Deserializer;
import net.amygdalum.testrecorder.deserializers.matcher.DefaultMatcherGenerator;
import net.amygdalum.testrecorder.types.Computation;
import net.amygdalum.testrecorder.types.DeserializerContext;
import net.amygdalum.testrecorder.types.TypeManager;
import net.amygdalum.testrecorder.util.Types;
import net.amygdalum.testrecorder.values.SerializedImmutable;

public class CalendarMatcherGenerator extends DefaultMatcherGenerator<SerializedImmutable<Calendar>> implements Adaptor<SerializedImmutable<Calendar>> {

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
        
		types.registerImport(BigDecimal.class);
		types.staticImport(CoreMatchers.class, "equalTo");

		Calendar cal = value.getValue();
        
        List<String> statements = new ArrayList<>();
        String calexpression = context.newLocal("expected");
        statements.add(assignLocalVariableStatement(types.getVariableTypeName(Calendar.class), calexpression, callMethod(types.getRawTypeName(Calendar.class), "getInstance")));
        statements.add(callMethodStatement(calexpression, "setTimeInMillis", asLiteral(cal.getTimeInMillis())));

		String equalToMatcher = equalToMatcher(calexpression);
		return expression(equalToMatcher, parameterized(Matcher.class, null, value.getType()), statements);
    }
}
