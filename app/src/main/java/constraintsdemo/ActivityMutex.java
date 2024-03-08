package constraintsdemo;

import gov.nasa.jpl.aerie.constraints.Constraint;
import gov.nasa.jpl.aerie.constraints.Violations;
import gov.nasa.jpl.aerie.timeline.CollectOptions;
import gov.nasa.jpl.aerie.timeline.plan.Plan;
import org.jetbrains.annotations.NotNull;

/**
 * A basic activity mutex
 */
class ActivityMutex implements Constraint {
    @NotNull
    @Override
    public Violations run(Plan plan, @NotNull CollectOptions options) {
        final var growActs = plan.instances("GrowBanana");
        final var bakeActs = plan.instances("BakeBananaBread");

        return Violations.mutex(growActs, bakeActs);
    }
}
