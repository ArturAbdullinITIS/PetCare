package ru.tbank.petcare.domain.usecase.pets

import ru.tbank.petcare.domain.model.Activity
import ru.tbank.petcare.domain.model.ActivityDetails
import ru.tbank.petcare.domain.model.ActivityType
import ru.tbank.petcare.domain.model.AnalyticsChartEntry
import ru.tbank.petcare.domain.model.AnalyticsPeriod
import ru.tbank.petcare.domain.model.AnalyticsResult
import ru.tbank.petcare.domain.model.AnalyticsSummary
import ru.tbank.petcare.domain.model.GoalCompletionSummary
import ru.tbank.petcare.domain.model.ValidationResult
import ru.tbank.petcare.domain.repository.ActivityRepository
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class AnalyticsUseCase @Inject constructor(
    private val activityRepository: ActivityRepository
) {

    suspend operator fun invoke(
        petId: String,
        period: AnalyticsPeriod
    ): ValidationResult<AnalyticsResult> {
        val range = getPeriodRange(period)

        val activitiesResult = activityRepository.getActivitiesByPeriod(
            petId = petId,
            startDate = range.startMillis,
            endDate = range.endMillis
        )

        if (!activitiesResult.isSuccess || activitiesResult.data == null) {
            return ValidationResult(
                error = activitiesResult.error,
                isSuccess = false
            )
        }

        val activities = activitiesResult.data

        return ValidationResult(
            data = AnalyticsResult(
                distanceChart = buildDistanceChart(activities, period),
                expensesChart = buildExpensesChart(activities, period),
                summary = buildSummary(activities),
                goalCompletion = buildGoalCompletion(activities)
            ),
            isSuccess = true
        )
    }

    private fun buildSummary(activities: List<Activity>): AnalyticsSummary {
        val walks = activities.filter { it.activityType == ActivityType.WALK }

        val totalWalks = walks.size

        val totalKm = walks.sumOfFloat { activity ->
            (activity.details as? ActivityDetails.Walk)?.actualKm.toSafeFloat()
        }

        val totalExpenses = activities.sumOfFloat { activity ->
            when (val details = activity.details) {
                is ActivityDetails.Grooming -> details.groomingCost.toSafeFloat()
                is ActivityDetails.Vet -> details.vetCost.toSafeFloat()
                else -> 0f
            }
        }

        val avgKm = if (totalWalks > 0) {
            (totalKm / totalWalks)
        } else {
            0f
        }

        return AnalyticsSummary(
            totalWalks = totalWalks,
            totalExpenses = totalExpenses,
            avgKm = avgKm
        )
    }

    private fun buildDistanceChart(
        activities: List<Activity>,
        period: AnalyticsPeriod
    ): List<AnalyticsChartEntry> {
        val walks = activities.filter { it.activityType == ActivityType.WALK }

        return when (period) {
            AnalyticsPeriod.WEEK -> buildWeekChart(walks) { activity ->
                (activity.details as? ActivityDetails.Walk)?.actualKm.toSafeFloat()
            }

            AnalyticsPeriod.MONTH -> buildMonthChart(walks) { activity ->
                (activity.details as? ActivityDetails.Walk)?.actualKm.toSafeFloat()
            }

            AnalyticsPeriod.THREE_MONTHS -> buildThreeMonthsChart(walks) { activity ->
                (activity.details as? ActivityDetails.Walk)?.actualKm.toSafeFloat()
            }

            AnalyticsPeriod.YEAR -> buildYearChart(walks) { activity ->
                (activity.details as? ActivityDetails.Walk)?.actualKm.toSafeFloat()
            }
        }
    }

    @Suppress("CyclomaticComplexMethod")
    private fun buildExpensesChart(
        activities: List<Activity>,
        period: AnalyticsPeriod
    ): List<AnalyticsChartEntry> {
        val expenses = activities.filter {
            it.activityType == ActivityType.GROOMING || it.activityType == ActivityType.VET
        }

        return when (period) {
            AnalyticsPeriod.WEEK -> buildWeekChart(expenses) { activity ->
                when (val details = activity.details) {
                    is ActivityDetails.Grooming -> details.groomingCost.toSafeFloat()
                    is ActivityDetails.Vet -> details.vetCost.toSafeFloat()
                    else -> 0f
                }
            }

            AnalyticsPeriod.MONTH -> buildMonthChart(expenses) { activity ->
                when (val details = activity.details) {
                    is ActivityDetails.Grooming -> details.groomingCost.toSafeFloat()
                    is ActivityDetails.Vet -> details.vetCost.toSafeFloat()
                    else -> 0f
                }
            }

            AnalyticsPeriod.THREE_MONTHS -> buildThreeMonthsChart(expenses) { activity ->
                when (val details = activity.details) {
                    is ActivityDetails.Grooming -> details.groomingCost.toSafeFloat()
                    is ActivityDetails.Vet -> details.vetCost.toSafeFloat()
                    else -> 0f
                }
            }

            AnalyticsPeriod.YEAR -> buildYearChart(expenses) { activity ->
                when (val details = activity.details) {
                    is ActivityDetails.Grooming -> details.groomingCost.toSafeFloat()
                    is ActivityDetails.Vet -> details.vetCost.toSafeFloat()
                    else -> 0f
                }
            }
        }
    }

    private fun buildWeekChart(
        activities: List<Activity>,
        valueSelector: (Activity) -> Float
    ): List<AnalyticsChartEntry> {
        val today = LocalDate.now()
        val days = (6 downTo 0).map { today.minusDays(it.toLong()) }

        return days.map { day ->
            AnalyticsChartEntry(
                label = day.dayOfWeek.shortLabel(),
                value = activities
                    .filter { it.activityDate.toLocalDateOrNull() == day }
                    .sumOfFloat(valueSelector)
            )
        }
    }

    private fun buildMonthChart(
        activities: List<Activity>,
        valueSelector: (Activity) -> Float
    ): List<AnalyticsChartEntry> {
        val today = LocalDate.now()
        val currentWeekStart = today.startOfWeek()
        val weeks = (3 downTo 0).map { currentWeekStart.minusWeeks(it.toLong()) }

        return weeks.mapIndexed { index, weekStart ->
            val weekEnd = weekStart.plusDays(6)

            AnalyticsChartEntry(
                label = "W${index + 1}",
                value = activities
                    .filter { activity ->
                        val date = activity.activityDate.toLocalDateOrNull()
                        date != null && !date.isBefore(weekStart) && !date.isAfter(weekEnd)
                    }
                    .sumOfFloat(valueSelector)
            )
        }
    }

    private fun buildThreeMonthsChart(
        activities: List<Activity>,
        valueSelector: (Activity) -> Float
    ): List<AnalyticsChartEntry> {
        val currentMonth = LocalDate.now().withDayOfMonth(1)
        val months = (2 downTo 0).map { currentMonth.minusMonths(it.toLong()) }

        return months.map { monthStart ->
            AnalyticsChartEntry(
                label = monthStart.month.shortLabel(),
                value = activities
                    .filter { activity ->
                        val date = activity.activityDate.toLocalDateOrNull()
                        date != null &&
                            date.year == monthStart.year &&
                            date.month == monthStart.month
                    }
                    .sumOfFloat(valueSelector)
            )
        }
    }

    private fun buildYearChart(
        activities: List<Activity>,
        valueSelector: (Activity) -> Float
    ): List<AnalyticsChartEntry> {
        val currentMonth = LocalDate.now().withDayOfMonth(1)
        val months = (11 downTo 0).map { currentMonth.minusMonths(it.toLong()) }

        return months.map { monthStart ->
            AnalyticsChartEntry(
                label = monthStart.month.shortLabel(),
                value = activities
                    .filter { activity ->
                        val date = activity.activityDate.toLocalDateOrNull()
                        date != null &&
                            date.year == monthStart.year &&
                            date.month == monthStart.month
                    }
                    .sumOfFloat(valueSelector)
            )
        }
    }

    private fun getPeriodRange(period: AnalyticsPeriod): PeriodRange {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now()

        val startDate = when (period) {
            AnalyticsPeriod.WEEK -> today.minusDays(6)
            AnalyticsPeriod.MONTH -> today.startOfWeek().minusWeeks(3)
            AnalyticsPeriod.THREE_MONTHS -> today.withDayOfMonth(1).minusMonths(2)
            AnalyticsPeriod.YEAR -> today.withDayOfMonth(1).minusMonths(11)
        }

        val endDateExclusive = today.plusDays(1)

        val startMillis = startDate.atStartOfDay(zone).toInstant().toEpochMilli()
        val endMillis = endDateExclusive.atStartOfDay(zone).toInstant().toEpochMilli() - 1

        return PeriodRange(
            startMillis = startMillis,
            endMillis = endMillis
        )
    }

    private data class PeriodRange(
        val startMillis: Long,
        val endMillis: Long
    )

    private fun String?.toSafeFloat(): Float {
        return this?.replace(",", ".")?.toFloatOrNull() ?: 0f
    }

    private fun Date?.toLocalDateOrNull(): LocalDate? {
        if (this == null) return null
        return Instant.ofEpochMilli(time)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    private fun LocalDate.startOfWeek(): LocalDate {
        return with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    }

    private fun Month.shortLabel(): String {
        return getDisplayName(TextStyle.SHORT, Locale.getDefault())
            .first()
            .uppercase()
    }

    private fun DayOfWeek.shortLabel(): String {
        return getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }

    private inline fun <T> Iterable<T>.sumOfFloat(selector: (T) -> Float): Float {
        var sum = 0f
        for (item in this) {
            sum += selector(item)
        }
        return sum
    }
    private fun buildGoalCompletion(activities: List<Activity>): GoalCompletionSummary {
        val walks = activities.mapNotNull { activity ->
            activity.details as? ActivityDetails.Walk
        }

        val validWalks = walks.filter { walk ->
            walk.goalKm.toFloatOrNull() != null && walk.actualKm.toFloatOrNull() != null
        }

        val totalGoals = validWalks.size

        val completedGoals = validWalks.count { walk ->
            val goal = walk.goalKm.toFloatOrNull() ?: 0f
            val actual = walk.actualKm.toFloatOrNull() ?: 0f
            actual >= goal
        }

        val progress = if (totalGoals > 0) {
            completedGoals.toFloat() / totalGoals.toFloat()
        } else {
            0f
        }

        return GoalCompletionSummary(
            completedGoals = completedGoals,
            totalGoals = totalGoals,
            progress = progress
        )
    }
}
