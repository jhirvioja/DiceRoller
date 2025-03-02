package fi.voicehaus.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fi.voicehaus.diceroller.ui.ViewResultsScreen
import fi.voicehaus.diceroller.ui.StartDiceRollScreen
import fi.voicehaus.diceroller.ui.theme.DiceRollerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceRollerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    DiceRollerApp()
                }
            }
        }
    }
}

data class DiceRoll(
    val timestamp: Long, val result: Int
)

enum class DiceRollScreen {
    Start, Results,
}

@Preview(showBackground = true)
@Composable
fun DiceRollerApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen =
        DiceRollScreen.valueOf(backStackEntry?.destination?.route ?: DiceRollScreen.Start.name)

    var isRolling by remember { mutableStateOf(false) }
    val result = remember { mutableIntStateOf(1) }
    val results = remember { mutableStateOf<List<DiceRoll>>(emptyList()) }

    Scaffold(topBar = {
        DiceRollerTopAppBar(
            currentScreen = currentScreen,
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.navigateUp() })
    }) { paddingValues ->
        NavHost(
            navController = navController, startDestination = DiceRollScreen.Start.name
        ) {
            composable(
                route = DiceRollScreen.Start.name
            ) {
                StartDiceRollScreen(
                    result = result,
                    onRollDice = { newResult ->
                        result.intValue = newResult
                        if (isRolling.not()) {
                            results.value += DiceRoll(System.currentTimeMillis(), newResult)
                        }
                    },
                    onViewResultsClicked = { navController.navigate(DiceRollScreen.Results.name) },
                    isRolling = isRolling,
                    setIsRolling = { newIsRolling -> isRolling = newIsRolling },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
            }
            composable(
                route = DiceRollScreen.Results.name
            ) {
                ViewResultsScreen(
                    rolls = results,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceRollerTopAppBar(
    currentScreen: DiceRollScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (currentScreen.name == DiceRollScreen.Results.name) {
                        currentScreen.name
                    } else {
                        stringResource(R.string.app_name)
                    }, style = MaterialTheme.typography.headlineSmall
                )
            }
        })
}
