package tf.cyber.authzforce.time.functions;

import org.ow2.authzforce.core.pdp.api.IndeterminateEvaluationException;
import org.ow2.authzforce.core.pdp.api.expression.Expression;
import org.ow2.authzforce.core.pdp.api.func.BaseFirstOrderFunctionCall;
import org.ow2.authzforce.core.pdp.api.func.FirstOrderFunctionCall;
import org.ow2.authzforce.core.pdp.api.func.MultiParameterTypedFirstOrderFunction;
import org.ow2.authzforce.core.pdp.api.value.AttributeValue;
import org.ow2.authzforce.core.pdp.api.value.BooleanValue;
import org.ow2.authzforce.core.pdp.api.value.Datatype;
import org.ow2.authzforce.core.pdp.api.value.StandardDatatypes;
import tf.cyber.authzforce.time.datatypes.DayOfWeekValue;

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
                return new BooleanValue(true);
            }
        };
    }
}
