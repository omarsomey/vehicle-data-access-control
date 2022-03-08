package tf.cyber.authzforce.time.functions;

import org.ow2.authzforce.core.pdp.api.IndeterminateEvaluationException;
import org.ow2.authzforce.core.pdp.api.expression.Expression;
import org.ow2.authzforce.core.pdp.api.func.BaseFirstOrderFunctionCall;
import org.ow2.authzforce.core.pdp.api.func.FirstOrderFunctionCall;
import org.ow2.authzforce.core.pdp.api.func.MultiParameterTypedFirstOrderFunction;
import org.ow2.authzforce.core.pdp.api.value.*;
import tf.cyber.authzforce.time.datatypes.DayOfWeekValue;
import tf.cyber.authzforce.time.datatypes.java.DayOfWeekType;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.util.Deque;
import java.util.List;

public class DateTimeInDayOfWeekRange extends MultiParameterTypedFirstOrderFunction<BooleanValue> {
    public static final String FUNCTION_NAME = "urn:oasis:names:tc:xacml:3.0:function:dateTime-in-dayOfWeek-range";

    public DateTimeInDayOfWeekRange() {
        super(FUNCTION_NAME, StandardDatatypes.BOOLEAN, false, List.of(StandardDatatypes.DATETIME,
                                                                       DayOfWeekValue.TYPE,
                                                                       DayOfWeekValue.TYPE));
    }

    @Override
    public FirstOrderFunctionCall<BooleanValue> newCall(List<Expression<?>> argExpressions,
                                                        Datatype<?>... remainingArgTypes) throws IllegalArgumentException {
        return new BaseFirstOrderFunctionCall.EagerMultiPrimitiveTypeEval<>(functionSignature, argExpressions, remainingArgTypes) {
            @Override
            protected BooleanValue evaluate(Deque<AttributeValue> args) throws IndeterminateEvaluationException {
                XMLGregorianCalendar dateTimeValue = ((DateTimeValue) args.poll()).getUnderlyingValue();
                DayOfWeekType dayOfWeekValueStart = ((DayOfWeekValue) args.poll()).getUnderlyingValue();
                DayOfWeekType dayOfWeekValueEnd = ((DayOfWeekValue) args.poll()).getUnderlyingValue();

                dateTimeValue = dateTimeValue.normalize();

                LocalDate currentDayOfWeekUTC = LocalDate.of(
                        dateTimeValue.getYear(),
                        dateTimeValue.getMonth(),
                        dateTimeValue.getDay());

                boolean overlap = dayOfWeekValueStart.getDayOfWeek().getValue() > dayOfWeekValueEnd.getDayOfWeek().getValue();

                return new BooleanValue(true);
            }
        };
    }
}
