package constraintsdemo;

import gov.nasa.jpl.aerie.constraints.Constraint;
import gov.nasa.jpl.aerie.constraints.Violations;
import gov.nasa.jpl.aerie.timeline.collections.profiles.Numbers;
import gov.nasa.jpl.aerie.timeline.collections.profiles.Real;
import gov.nasa.jpl.aerie.timeline.plan.Plan;
import gov.nasa.jpl.aerie.timeline.CollectOptions;
import org.jetbrains.annotations.NotNull;

/**
 * A basic resource constraint
 */
class ResourceBasics implements Constraint {
    @NotNull
    @Override
    public Violations run(Plan plan, @NotNull CollectOptions options) {

        final var fruit = plan.resource("/fruit", Real::deserialize);
        final var peel = plan.resource("/peel", Numbers::deserialize);

        return Violations.violateOn(
                fruit.lessThan(2.5).or(peel.greaterThan(3.5)),
                false
        );
    }
}
