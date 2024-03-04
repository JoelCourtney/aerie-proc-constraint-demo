package constraintsdemo

import gov.nasa.jpl.aerie.constraints.ActivityId.InstanceId
import gov.nasa.jpl.aerie.constraints.Constraint
import gov.nasa.jpl.aerie.constraints.Violation
import gov.nasa.jpl.aerie.constraints.Violations
import gov.nasa.jpl.aerie.timeline.CollectOptions
import gov.nasa.jpl.aerie.timeline.plan.Plan

/**
 * Requires a ThrowBanana in between each DownloadBanana.
 */
class ActivityOrdering: Constraint {
    override fun run(plan: Plan, options: CollectOptions): Violations {
        val allActivities = plan.allActivityInstances()

        val downloads = allActivities.filterByType("DownloadBanana")

        return downloads.connectTo(downloads, false).unsafeOperate(::Violations) {
            opts ->
            val throws = allActivities.filterByType("ThrowBanana").collect(opts)
            collect(opts).mapNotNull { connection ->
                val satisfied = throws.any { t -> t.interval in connection.interval }
                if (!satisfied) Violation(
                    connection.interval,
                    listOfNotNull(
                        connection.from?.let { InstanceId(it.id) },
                        connection.to?.let { InstanceId(it.id) },
                    )
                ) else null
            }
        }
    }
}
