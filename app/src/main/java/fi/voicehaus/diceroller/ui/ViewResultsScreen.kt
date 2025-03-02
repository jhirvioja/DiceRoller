package fi.voicehaus.diceroller.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fi.voicehaus.diceroller.DiceRoll
import fi.voicehaus.diceroller.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ViewResultsScreen(
    modifier: Modifier = Modifier,
    rolls: MutableState<List<DiceRoll>>
) {
    val casts = rolls.value.size
    val odds = rolls.value.count { it.result % 2 != 0 }
    val evens = casts - odds

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CounterItem(label = "Rolls", value = casts)
            CounterItem(label = "Odds", value = odds)
            CounterItem(label = "Evens", value = evens)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.result),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.timestamp),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            val sortedRolls = rolls.value.sortedByDescending { it.timestamp }

            items(sortedRolls) { roll ->
                DiceRollItem(roll)
            }
        }
    }
}

@Composable
fun CounterItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = value.toString(), style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun DiceRollItem(roll: DiceRoll) {
    val formatter = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    val timeString = formatter.format(Date(roll.timestamp))
    val diceIcons = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")
    val diceIcon = diceIcons[roll.result - 1]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "$diceIcon  ${roll.result}", style = MaterialTheme.typography.bodyLarge)
            }
            Column {
                Text(text = timeString, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewViewResultsScreen() {
    val sampleRolls = listOf(
        DiceRoll(timestamp = System.currentTimeMillis(), result = 3),
        DiceRoll(timestamp = System.currentTimeMillis() - 60000, result = 6),
        DiceRoll(timestamp = System.currentTimeMillis() - 120000, result = 1)
    )

    val results = remember { mutableStateOf(sampleRolls) }

    ViewResultsScreen(rolls = results)
}
