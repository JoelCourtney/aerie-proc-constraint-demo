package constraintsdemo;

import gov.nasa.jpl.aerie.constraints.Constraint;
import gov.nasa.jpl.aerie.constraints.Violations;
import gov.nasa.jpl.aerie.timeline.CollectOptions;
import gov.nasa.jpl.aerie.timeline.collections.profiles.Numbers;
import gov.nasa.jpl.aerie.timeline.plan.Plan;
import org.jetbrains.annotations.NotNull;

/**
 * Compares an activity argument to a resource.
 */
class ActivityAndResource implements Constraint {
    @NotNull
    @Override
    public Violations run(Plan plan, @NotNull CollectOptions options) {
        final var pickActStarts = plan
                .allActivityInstances()
                .filterByType("PickBanana")
                .starts();

        final var plant = plan.resource("/plant", Numbers::deserialize);


        /*
        Version 1: extracts the argument and flattens it into a profile.
        This only works because the PickBanana activity does not overlap with itself in the plan;
        it would break otherwise.
        It also does not record the activity id in the violation.
         */
        final var quantities = pickActStarts.<Long, Numbers<Long>>flattenIntoProfile(
                Numbers::new,
                (act) -> act.getInner().getArguments().get("quantity").asInt().get()
        );
        final var violations = Violations.violateOn(quantities.greaterThan(plant), true);


        /*
        Version 1.5: extracts argument as a profile like in Version 1. Then filters out activities that are not violating
        Again only works because PickBanana does not overlap with itself.
        This one does record the activity id; however for now it is very inefficient because by default timelines are not
        cached. So the activities are queried from the database, filtered, and transformed twice.
        I plan to change this behavior in the near future.
         */
//        final var quantities = pickActStarts.<Long, Numbers<Long>>flattenIntoProfile(
//                Numbers::new,
//                (act) -> act.getInner().getArguments().get("quantity").asInt().get()
//        );
//        final var violations = Violations.violations(
//                pickActStarts.filterByWindows(quantities.greaterThan(plant).highlightTrue(), false)
//        );



        /*
        Version 2: Directly compare the activities to the profile segments and create the violations yourself.
        Its ugly and hard to read, but it does it right:
        - will work even if PickBanana's overlap with each other
        - records activity ids
        - only processes the activities once
         */
//        final Violations violations = pickActStarts.unsafeMap2(
//                Violations::new,
//                plant,
//                (act, plantSegment, intersection) -> {
//                    final var quantityArg = act.getInner().getArguments().get("quantity").asInt().get();
//                    if (quantityArg > plantSegment.getValue().longValue()) {
//                        return new Violation(
//                                intersection,
//                                List.of(new ActivityId.InstanceId(act.getId()))
//                        );
//                    } else {
//                        return null;
//                    }
//                }
//        );

        return violations;
    }
}
