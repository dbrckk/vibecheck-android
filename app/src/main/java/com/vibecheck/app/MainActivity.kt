package com.vibecheck.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecheck.app.data.QuestionRepository
import com.vibecheck.app.domain.GameEngine
import com.vibecheck.app.domain.PlayerRules
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.Vote

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { VibeCheckApp() }
    }
}

private enum class Screen { HOME, PLAYERS, GAME, RESULT }

@Composable
fun VibeCheckApp() {
    val background = Brush.verticalGradient(
        listOf(Color(0xFF101014), Color(0xFF24153A), Color(0xFF101014))
    )

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                var screen by remember { mutableStateOf(Screen.HOME) }
                var selectedMode by remember { mutableStateOf(GameMode.WHO_OF_US) }
                var questionIndex by remember { mutableIntStateOf(0) }
                val votes = remember { mutableStateListOf<Vote>() }
                val players = remember { mutableStateListOf<String>() }

                when (screen) {
                    Screen.HOME -> HomeScreen { mode ->
                        selectedMode = mode
                        questionIndex = 0
                        votes.clear()
                        screen = Screen.PLAYERS
                    }

                    Screen.PLAYERS -> PlayerSetupScreen(
                        players = players,
                        onBack = { screen = Screen.HOME },
                        onAddPlayer = { players += it },
                        onRemovePlayer = { players.remove(it) },
                        onStart = {
                            votes.clear()
                            questionIndex = 0
                            screen = Screen.GAME
                        }
                    )

                    Screen.GAME -> {
                        val questions = QuestionRepository.forMode(selectedMode)
                        val question = questions[questionIndex]
                        GameScreen(
                            mode = selectedMode,
                            questionText = question.text,
                            progress = questionIndex + 1,
                            total = questions.size,
                            answers = if (selectedMode == GameMode.RED_GREEN) {
                                listOf("Green Flag", "Red Flag")
                            } else {
                                players.toList()
                            },
                            onAnswer = { answer ->
                                votes += Vote(question.id, answer)
                                if (questionIndex == questions.lastIndex) {
                                    screen = Screen.RESULT
                                } else {
                                    questionIndex += 1
                                }
                            }
                        )
                    }

                    Screen.RESULT -> ResultScreen(
                        mode = selectedMode,
                        votes = votes,
                        onReplay = {
                            votes.clear()
                            questionIndex = 0
                            screen = Screen.GAME
                        },
                        onHome = {
                            votes.clear()
                            screen = Screen.HOME
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(onMode: (GameMode) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("VibeCheck", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Black)
        Text("Qui connaît vraiment qui ?", color = Color(0xFFBEB7C9), fontSize = 18.sp)
        Spacer(Modifier.height(28.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(GameMode.entries) { mode ->
                Card(
                    onClick = { onMode(mode) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF211E29)),
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text(mode.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(mode.subtitle, color = Color(0xFFA9A3B3), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerSetupScreen(
    players: List<String>,
    onBack: () -> Unit,
    onAddPlayer: (String) -> Unit,
    onRemovePlayer: (String) -> Unit,
    onStart: () -> Unit
) {
    var input by remember { mutableStateOf("") }
    val normalized = PlayerRules.normalize(input)
    val canAdd = PlayerRules.canAdd(players, input)
    val canStart = PlayerRules.canStart(players)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Crée ton groupe", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black)
            Text(
                "Ajoute entre " + PlayerRules.MIN_PLAYERS + " et " + PlayerRules.MAX_PLAYERS + " joueurs.",
                color = Color(0xFFBEB7C9)
            )
            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = input,
                onValueChange = { input = it.take(PlayerRules.MAX_NAME_LENGTH + 4) },
                label = { Text("Prénom ou pseudo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = {
                    onAddPlayer(normalized)
                    input = ""
                },
                enabled = canAdd,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Ajouter le joueur")
            }

            Spacer(Modifier.height(18.dp))

            if (players.isEmpty()) {
                Text("Aucun joueur ajouté pour le moment.", color = Color(0xFF8B8494))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(players, key = { it }) { player ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF211E29)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(player, color = Color.White, fontWeight = FontWeight.Bold)
                                IconButton(onClick = { onRemovePlayer(player) }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Supprimer " + player,
                                        tint = Color(0xFFBEB7C9)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onStart,
                enabled = canStart,
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    if (canStart) "Lancer la partie" else "Ajoute au moins " + PlayerRules.MIN_PLAYERS + " joueurs",
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF302A39))
            ) {
                Text("Retour")
            }
        }
    }
}

@Composable
private fun GameScreen(
    mode: GameMode,
    questionText: String,
    progress: Int,
    total: Int,
    answers: List<String>,
    onAnswer: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(mode.title, color = Color(0xFFC9A7FF), fontWeight = FontWeight.Bold)
            Text(progress.toString() + " / " + total, color = Color(0xFF8B8494), fontSize = 14.sp)
        }

        Text(
            questionText,
            color = Color.White,
            fontSize = 30.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Black
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            answers.forEach { answer ->
                Button(
                    onClick = { onAnswer(answer) },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEEE6FF),
                        contentColor = Color(0xFF18121F)
                    )
                ) {
                    Text(answer, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ResultScreen(
    mode: GameMode,
    votes: List<Vote>,
    onReplay: () -> Unit,
    onHome: () -> Unit
) {
    val result = GameEngine.result(votes)
    val context = LocalContext.current
    val percent = if (result.total == 0) 0 else (result.score * 100 / result.total)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("VIBECHECK", color = Color(0xFFC9A7FF), fontWeight = FontWeight.Black)
            Spacer(Modifier.height(30.dp))
            Text(
                result.winner,
                color = Color.White,
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
            Text(
                percent.toString() + "%",
                color = Color(0xFFE8D8FF),
                fontSize = 72.sp,
                fontWeight = FontWeight.Black
            )
            Text("sur " + mode.title, color = Color(0xFFA9A3B3), textAlign = TextAlign.Center)
        }

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = {
                    val shareText = "Mon VibeCheck : " + result.winner + " arrive en tête avec " +
                        percent + "% — " + mode.title + "."
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(Intent.createChooser(intent, "Partager le VibeCheck"))
                },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Text("Partager le résultat", fontWeight = FontWeight.Bold)
                }
            }

            Button(
                onClick = onReplay,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Rejouer")
            }

            Button(
                onClick = onHome,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF302A39))
            ) {
                Text("Changer de mode")
            }
        }
    }
}
