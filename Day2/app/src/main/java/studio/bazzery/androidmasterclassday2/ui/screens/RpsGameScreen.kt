package studio.bazzery.androidmasterclassday2.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class RpsChoice(val text: String, val emoji: String) {
    ROCK("Rock", "🪨"),
    PAPER("Paper", "📄"),
    SCISSORS("Scissors", "✂️")
}

enum class GameResult {
    WIN, LOSS, TIE, NONE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RpsGameScreen() {
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Game stats
    var wins by remember { mutableIntStateOf(0) }
    var losses by remember { mutableIntStateOf(0) }
    var ties by remember { mutableIntStateOf(0) }

    // Game states
    var playerChoice by remember { mutableStateOf<RpsChoice?>(null) }
    var computerChoice by remember { mutableStateOf<RpsChoice?>(null) }
    var gameResult by remember { mutableStateOf(GameResult.NONE) }
    var isThinking by remember { mutableStateOf(false) }
    var thinkingEmoji by remember { mutableStateOf("❓") }

    // Input mode: true = Visual Selection, false = Text Input Validation
    var isVisualMode by remember { mutableStateOf(true) }
    var textInputVal by remember { mutableStateOf("") }
    var textInputError by remember { mutableStateOf<String?>(null) }

    // Loop game simulation state
    var playedRoundsLog by remember { mutableStateOf(listOf<String>()) }

    val resultBgColor by animateColorAsState(
        targetValue = when (gameResult) {
            GameResult.WIN -> Color(0xFFE8F5E9)
            GameResult.LOSS -> Color(0xFFFFEBEE)
            GameResult.TIE -> Color(0xFFFFFDE7)
            GameResult.NONE -> MaterialTheme.colorScheme.surfaceVariant
        }, label = "resultBgColor"
    )

    val resultBorderColor by animateColorAsState(
        targetValue = when (gameResult) {
            GameResult.WIN -> Color(0xFF4CAF50)
            GameResult.LOSS -> Color(0xFFE57373)
            GameResult.TIE -> Color(0xFFFFD54F)
            GameResult.NONE -> Color.Transparent
        }, label = "resultBorderColor"
    )

    fun playRound(playerSelected: RpsChoice) {
        coroutineScope.launch {
            isThinking = true
            playerChoice = playerSelected
            gameResult = GameResult.NONE
            computerChoice = null
            textInputError = null

            // Animate computer choice "thinking"
            val emojis = listOf("🪨", "📄", "✂️")
            repeat(6) { index ->
                thinkingEmoji = emojis[index % emojis.size]
                delay(100)
            }

            // Get random computer choice
            val randomNumber = (1..3).random()
            val compChoice = when (randomNumber) {
                1 -> RpsChoice.ROCK
                2 -> RpsChoice.PAPER
                else -> RpsChoice.SCISSORS
            }
            computerChoice = compChoice
            isThinking = false

            // Determine winner (based on PDF Section 5)
            val result = if (playerSelected == compChoice) {
                GameResult.TIE
            } else if (
                (playerSelected == RpsChoice.ROCK && compChoice == RpsChoice.SCISSORS) ||
                (playerSelected == RpsChoice.PAPER && compChoice == RpsChoice.ROCK) ||
                (playerSelected == RpsChoice.SCISSORS && compChoice == RpsChoice.PAPER)
            ) {
                GameResult.WIN
            } else {
                GameResult.LOSS
            }

            gameResult = result

            // Update stats
            when (result) {
                GameResult.WIN -> wins++
                GameResult.LOSS -> losses++
                GameResult.TIE -> ties++
                GameResult.NONE -> {}
            }

            // Log the round
            val roundLog = "คุณ: ${playerSelected.emoji} vs คอม: ${compChoice.emoji} -> " + when (result) {
                GameResult.WIN -> "คุณชนะ! 🎉"
                GameResult.LOSS -> "คอมชนะ! 🤖"
                GameResult.TIE -> "เสมอกัน! 🤝"
                GameResult.NONE -> ""
            }
            playedRoundsLog = listOf(roundLog) + playedRoundsLog
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sleek Gradient Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        )
                    )
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Rock Paper Scissors",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "เวิร์กชอปประกอบร่างเกมเป่ายิ้งฉุบ (Day 2)",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Scoreboard Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScoreItem(label = "ชนะ (Wins)", count = wins, color = Color(0xFF2E7D32))
                VerticalDivider(modifier = Modifier.height(40.dp))
                ScoreItem(label = "เสมอ (Ties)", count = ties, color = Color(0xFFF57F17))
                VerticalDivider(modifier = Modifier.height(40.dp))
                ScoreItem(label = "แพ้ (Losses)", count = losses, color = Color(0xFFC62828))
            }
        }

        // Mode Selector (Visual vs Text Validation)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val visualBg = if (isVisualMode) MaterialTheme.colorScheme.primary else Color.Transparent
            val visualColor = if (isVisualMode) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            val textBg = if (!isVisualMode) MaterialTheme.colorScheme.primary else Color.Transparent
            val textColor = if (!isVisualMode) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(visualBg)
                    .clickable { isVisualMode = true }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Visual Mode 🎨", color = visualColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(textBg)
                    .clickable { isVisualMode = false }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Text Validation ⌨️", color = textColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        // Main Board
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, resultBorderColor, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = resultBgColor),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Battle State UI
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Player selection
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "คุณ (Player)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = playerChoice?.emoji ?: "❓",
                            fontSize = 60.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = playerChoice?.text ?: "รอเลือก...",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Text(text = "VS", fontSize = 24.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))

                    // Computer selection
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "บอท (Computer)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isThinking) thinkingEmoji else (computerChoice?.emoji ?: "❓"),
                            fontSize = 60.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isThinking) "กำลังสุ่ม..." else (computerChoice?.text ?: "รอสุ่ม..."),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                // Battle status text
                if (gameResult != GameResult.NONE) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = when (gameResult) {
                            GameResult.WIN -> "ยินดีด้วย! คุณชนะ 🎉"
                            GameResult.LOSS -> "น่าเสียดาย! บอทชนะ 🤖"
                            GameResult.TIE -> "ผลการแข่งขัน: เสมอกัน! 🤝"
                            GameResult.NONE -> ""
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (gameResult) {
                            GameResult.WIN -> Color(0xFF1B5E20)
                            GameResult.LOSS -> Color(0xFFB71C1C)
                            GameResult.TIE -> Color(0xFFF57F17)
                            GameResult.NONE -> MaterialTheme.colorScheme.onSurface
                        },
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Interaction Area
        if (isVisualMode) {
            // Visual selections
            Text(
                text = "เลือกออกเป่ายิ้งฉุบของคุณ:",
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RpsChoice.values().forEach { choice ->
                    val isSelected = playerChoice == choice
                    val cardBg = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    val border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(enabled = !isThinking) { playRound(choice) },
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = border,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = choice.emoji, fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = choice.text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        } else {
            // Text Input validation mode
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "กรอกตัวเลือกของคุณ (ต้องพิมพ์ตัวใหญ่ตัวเล็กให้ตรงเป๊ะ):",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    OutlinedTextField(
                        value = textInputVal,
                        onValueChange = {
                            textInputVal = it
                            textInputError = null
                        },
                        placeholder = { Text("พิมพ์ Rock, Paper หรือ Scissors") },
                        isError = textInputError != null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                val parsed = RpsChoice.values().firstOrNull { it.text == textInputVal.trim() }
                                if (parsed != null) {
                                    playRound(parsed)
                                } else {
                                    textInputError = "❌ พิมพ์ผิดครับ! โปรดเช็คตัวสะกดและตัวพิมพ์ใหญ่-เล็ก"
                                }
                            }
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (textInputError != null) {
                                Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    )

                    if (textInputError != null) {
                        Text(
                            text = textInputError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            keyboardController?.hide()
                            val parsed = RpsChoice.values().firstOrNull { it.text == textInputVal.trim() }
                            if (parsed != null) {
                                playRound(parsed)
                            } else {
                                textInputError = "❌ พิมพ์ผิดครับ! โปรดเช็คตัวสะกดและตัวพิมพ์ใหญ่-เล็ก"
                            }
                        },
                        enabled = !isThinking,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ส่งคำตอบเพื่อเป่ายิ้งฉุบ")
                    }
                }
            }
        }

        // Reset Scoreboard button
        if (wins > 0 || losses > 0 || ties > 0) {
            OutlinedButton(
                onClick = {
                    wins = 0
                    losses = 0
                    ties = 0
                    playerChoice = null
                    computerChoice = null
                    gameResult = GameResult.NONE
                    playedRoundsLog = emptyList()
                    textInputVal = ""
                    textInputError = null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset")
                Spacer(modifier = Modifier.width(8.dp))
                Text("รีเซ็ตคะแนนและสถิติทั้งหมด")
            }
        }

        // Played Log (simulated loop/history)
        if (playedRoundsLog.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "ประวัติการเล่น (Game Loop History):",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        playedRoundsLog.take(5).forEachIndexed { index, log ->
                            Text(
                                text = "รอบที่ ${playedRoundsLog.size - index}: $log",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (playedRoundsLog.size > 5) {
                            Text(
                                text = "... และอีก ${playedRoundsLog.size - 5} รอบก่อนหน้า",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreItem(label: String, count: Int, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = count.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = color
        )
    }
}
