package fi.voicehaus.diceroller.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fi.voicehaus.diceroller.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StartDiceRollScreen(
    onShowResultsClicked: () -> Unit, modifier: Modifier = Modifier
) {
    var result by remember { mutableIntStateOf(1) }
    var isRolling by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(imageResource), contentDescription = result.toString()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.height(42.dp), onClick = {
                    if (!isRolling) {
                        isRolling = true
                        coroutineScope.launch {
                            val diceRolls = listOf(1, 2, 3, 4, 5, 6)
                            val delayTimes = listOf(50L, 50L, 100L, 100L, 350L, 400L)

                            for (i in diceRolls.indices) {
                                result = diceRolls[i]
                                delay(delayTimes[i])
                            }

                            result = (1..6).random()
                            isRolling = false
                        }
                    }
                }, enabled = !isRolling
            ) {
                if (isRolling) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(24.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    Text(stringResource(R.string.roll))
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = onShowResultsClicked) {
                Text(stringResource(R.string.view_results))
            }
        }
    }
}

@Preview
@Composable
fun StartDiceRollScreenPreview() {
    StartDiceRollScreen(onShowResultsClicked = {})
}
