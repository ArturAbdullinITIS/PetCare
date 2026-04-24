package ru.tbank.petcare.presentation.screen.analytics

import android.R.attr.data
import android.R.attr.text
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.himanshoe.charty.bar.BarChart
import com.himanshoe.charty.bar.config.BarChartConfig
import com.himanshoe.charty.bar.data.BarData
import com.himanshoe.charty.color.ChartyColor
import com.himanshoe.charty.common.config.Animation
import com.himanshoe.charty.common.config.ChartScaffoldConfig
import com.himanshoe.charty.common.config.CornerRadius
import ru.tbank.petcare.R
import ru.tbank.petcare.domain.model.AnalyticsChartEntry
import ru.tbank.petcare.domain.model.AnalyticsPeriod
import ru.tbank.petcare.presentation.mapper.toUiText
import ru.tbank.petcare.presentation.model.ActivityHistoryModel
import ru.tbank.petcare.presentation.model.AnalyticsChartType
import ru.tbank.petcare.presentation.model.PetCardUIModel
import ru.tbank.petcare.utils.DateFormatter

@Composable
fun ActivityHistoryCard(
    modifier: Modifier = Modifier,
    model: ActivityHistoryModel,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(48.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActivityHistoryIcon(
                model = model
            )
            Column {
                Text(
                    text = model.activityType.value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = DateFormatter.formatDob(model.activityDate),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Text(
                text = model.trailingText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ActivityHistoryIcon(
    modifier: Modifier = Modifier,
    model: ActivityHistoryModel
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(color = model.bgColor.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = model.iconVector,
            contentDescription = stringResource(R.string.activity_history_icon),
            tint = model.iconTint
        )
    }
}

@Composable
fun PetShortInfo(
    modifier: Modifier = Modifier,
    pet: PetCardUIModel
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = pet.name,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 36.sp
            )
            Text(
                text = pet.subtitle,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Box(modifier = Modifier.padding(3.dp)) {
                AsyncImage(
                    model = pet.photoUrl,
                    contentDescription = stringResource(R.string.pets_photo),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    placeholder = painterResource(R.drawable.photo_placeholder),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.photo_placeholder)
                )
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: @Composable () -> Unit,
    value: String,
    bgColor: Color
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(48.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color = bgColor.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = value,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun AnalyticsPeriodSegmentedControl(
    currentPeriod: AnalyticsPeriod,
    onSelected: (AnalyticsPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    val trackHeight = 48.dp
    val shape = RoundedCornerShape(48.dp)

    val periods = AnalyticsPeriod.entries

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(trackHeight),
        shape = shape,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            periods.forEach { period ->
                val isSelected = period == currentPeriod

                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    onClick = { onSelected(period) },
                    shape = shape,
                    contentPadding = ButtonDefaults.ContentPadding,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            Color.Transparent
                        },
                        contentColor = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        }
                    )
                ) {
                    Text(
                        text = period.toUiText(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Clip
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityHistoryTitle(
    modifier: Modifier = Modifier,
    onViewAllClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.activity_history),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.view_all),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.clickable(
                onClick = onViewAllClick
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnalyticsChartsPager(
    modifier: Modifier = Modifier,
    distanceChart: List<AnalyticsChartEntry>,
    expensesChart: List<AnalyticsChartEntry>
) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> AnalyticsChartCard(
                    title = stringResource(R.string.distance),
                    subtitle = stringResource(R.string.walked_distance),
                    entries = distanceChart,
                    chartType = AnalyticsChartType.DISTANCE
                )

                1 -> AnalyticsChartCard(
                    title = stringResource(R.string.expenses),
                    subtitle = stringResource(R.string.service_costs),
                    entries = expensesChart,
                    chartType = AnalyticsChartType.EXPENSES
                )
            }
        }

        PagerIndicator(
            pagerState = pagerState,
            pageCount = 2
        )
    }
}

@Composable
private fun AnalyticsChartCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    entries: List<AnalyticsChartEntry>,
    chartType: AnalyticsChartType
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                )
            }

            if (entries.isEmpty() || entries.all { it.value.toInt() == 0 }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_data_yet),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            } else {
                AnalyticsBarChart(
                    entries = entries,
                    chartType = chartType
                )
            }
        }
    }
}

@Composable
private fun AnalyticsBarChart(
    entries: List<AnalyticsChartEntry>,
    chartType: AnalyticsChartType,
) {
    if (entries.isEmpty()) return

    val chartColor = when (chartType) {
        AnalyticsChartType.DISTANCE -> ChartyColor.Solid(MaterialTheme.colorScheme.onPrimaryContainer)
        AnalyticsChartType.EXPENSES -> ChartyColor.Solid(MaterialTheme.colorScheme.onPrimaryContainer)
    }

    BarChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(horizontal = 12.dp),
        data = {
            entries.map { entry ->
                BarData(
                    label = entry.label,
                    value = entry.value
                )
            }
        },
        color = chartColor,
        barConfig = BarChartConfig(
            barWidthFraction = when (entries.size) {
                4 -> 0.55f
                7 -> 0.5f
                12 -> 0.38f
                else -> 0.45f
            },
            cornerRadius = CornerRadius.Custom(radius = 50f),
            animation = Animation.Enabled()
        ),
        scaffoldConfig = ChartScaffoldConfig(
            labelTextStyle = TextStyle.Default.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PagerIndicator(
    pagerState: PagerState,
    pageCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            val isSelected = pagerState.currentPage == index

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(if (isSelected) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f)
                        }
                    )
            )
        }
    }
}

@Composable
fun GoalCompletionCard(
    modifier: Modifier = Modifier,
    petName: String,
    completedGoals: Int,
    totalGoals: Int,
    progress: Float
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = stringResource(R.string.goal_completion),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.size(180.dp),
                    strokeWidth = 18.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$completedGoals/$totalGoals",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.goals).uppercase(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            val percent = (progress * 100).toInt()

            Text(
                text = stringResource(R.string.completed_of_the_distance_goals, petName, percent),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
