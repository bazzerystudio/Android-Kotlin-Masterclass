package studio.bazzery.androidmasterclassday2.ui.screens

import androidx.compose.foundation.BorderStroke
import studio.bazzery.androidmasterclassday2.ui.components.CodeSnippet
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class SimulatorState {
    NOT_STARTED,
    AWAITING_PLAYER_CHOICE,
    AWAITING_PLAY_AGAIN,
    GAME_OVER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsoleSimulatorScreen() {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Console logs list
    var consoleLogs by remember { mutableStateOf(listOf<String>()) }
    var simState by remember { mutableStateOf(SimulatorState.NOT_STARTED) }
    var currentComputerChoice by remember { mutableStateOf("") }
    var inputVal by remember { mutableStateOf("") }

    val codeText = remember {
        """fun main() {
    var isGameOver = false
    println("=== ยินดีต้อนรับสู่เกมเป่ายิ้งฉุบ! ===")
    while (!isGameOver) {
        val randomNumber = (1..3).random()
        var computerChoice = ""
        if (randomNumber == 1) { computerChoice = "Rock" }
        else if (randomNumber == 2) { computerChoice = "Paper" }
        else { computerChoice = "Scissors" }

        var playerChoice = ""
        var isValidInput = false
        while (!isValidInput) {
            println("\nกรุณาพิมพ์ตัวเลือกของคุณ: Rock, Paper, หรือ Scissors")
            playerChoice = readln()
            if (playerChoice == "Rock" || playerChoice == "Paper" || playerChoice == "Scissors") {
                isValidInput = true
            } else {
                println("❌ พิมพ์ผิดครับ! โปรดเช็คตัวสะกดและตัวพิมพ์ใหญ่-เล็ก")
            }
        }

        var winner = ""
        if (playerChoice == computerChoice) { winner = "Tie" }
        else if (playerChoice == "Rock" && computerChoice == "Scissors") { winner = "Player" }
        else if (playerChoice == "Paper" && computerChoice == "Rock") { winner = "Player" }
        else if (playerChoice == "Scissors" && computerChoice == "Paper") { winner = "Player" }
        else { winner = "Computer" }

        println("-------------------")
        println("คุณเลือก: ${'$'}playerChoice")
        println("คอมพิวเตอร์เลือก: ${'$'}computerChoice")
        if (winner == "Tie") { println("ผลการแข่งขัน: เสมอกัน! 🤝") }
        else if (winner == "Player") { println("ผลการแข่งขัน: ยินดีด้วย! คุณชนะ 🎉") }
        else { println("ผลการแข่งขัน: คอมพิวเตอร์ชนะ 🤖") }
        println("-------------------")

        println("ต้องการเล่นต่อไหม? (พิมพ์ 'yes' เพื่อเล่นต่อ, พิมพ์คำอื่นเพื่อออก)")
        val playAgain = readln()
        if (playAgain != "yes") {
            isGameOver = true
            println("ขอบคุณที่ร่วมสนุกครับ บ๊ายบาย! 👋")
        }
    }
}"""
    }

    fun generateComputerChoice(): String {
        val randomNumber = (1..3).random()
        return when (randomNumber) {
            1 -> "Rock"
            2 -> "Paper"
            else -> "Scissors"
        }
    }

    fun startSimulator() {
        consoleLogs = listOf(
            "=== ยินดีต้อนรับสู่เกมเป่ายิ้งฉุบ! ==="
        )
        currentComputerChoice = generateComputerChoice()
        consoleLogs = consoleLogs + "\nกรุณาพิมพ์ตัวเลือกของคุณ: Rock, Paper, หรือ Scissors"
        simState = SimulatorState.AWAITING_PLAYER_CHOICE
        inputVal = ""
    }

    fun handleInput(input: String) {
        if (input.trim().isEmpty()) return
        val sanitizedInput = input.trim()
        val currentLogs = consoleLogs.toMutableList()
        currentLogs.add("> $sanitizedInput")

        when (simState) {
            SimulatorState.AWAITING_PLAYER_CHOICE -> {
                if (sanitizedInput == "Rock" || sanitizedInput == "Paper" || sanitizedInput == "Scissors") {
                    // Valid selection
                    val playerChoice = sanitizedInput
                    val computerChoice = currentComputerChoice

                    // Determine winner
                    val winner = if (playerChoice == computerChoice) {
                        "Tie"
                    } else if (playerChoice == "Rock" && computerChoice == "Scissors") {
                        "Player"
                    } else if (playerChoice == "Paper" && computerChoice == "Rock") {
                        "Player"
                    } else if (playerChoice == "Scissors" && computerChoice == "Paper") {
                        "Player"
                    } else {
                        "Computer"
                    }

                    // Print outcome
                    currentLogs.add("-------------------")
                    currentLogs.add("คุณเลือก: $playerChoice")
                    currentLogs.add("คอมพิวเตอร์เลือก: $computerChoice")

                    if (winner == "Tie") {
                        currentLogs.add("ผลการแข่งขัน: เสมอกัน! 🤝")
                    } else if (winner == "Player") {
                        currentLogs.add("ผลการแข่งขัน: ยินดีด้วย! คุณชนะ 🎉")
                    } else {
                        currentLogs.add("ผลการแข่งขัน: คอมพิวเตอร์ชนะ 🤖")
                    }
                    currentLogs.add("-------------------")

                    // Prompt play again
                    currentLogs.add("ต้องการเล่นต่อไหม? (พิมพ์ 'yes' เพื่อเล่นต่อ, พิมพ์คำอื่นเพื่อออก)")
                    simState = SimulatorState.AWAITING_PLAY_AGAIN
                } else {
                    // Invalid selection
                    currentLogs.add("❌ พิมพ์ผิดครับ! โปรดเช็คตัวสะกดและตัวพิมพ์ใหญ่-เล็ก")
                    currentLogs.add("\nกรุณาพิมพ์ตัวเลือกของคุณ: Rock, Paper, หรือ Scissors")
                }
            }
            SimulatorState.AWAITING_PLAY_AGAIN -> {
                if (sanitizedInput == "yes") {
                    currentComputerChoice = generateComputerChoice()
                    currentLogs.add("\nกรุณาพิมพ์ตัวเลือกของคุณ: Rock, Paper, หรือ Scissors")
                    simState = SimulatorState.AWAITING_PLAYER_CHOICE
                } else {
                    currentLogs.add("ขอบคุณที่ร่วมสนุกครับ บ๊ายบาย! 👋")
                    simState = SimulatorState.GAME_OVER
                }
            }
            else -> {}
        }

        consoleLogs = currentLogs
        inputVal = ""

        // Scroll to the bottom of the console logs
        coroutineScope.launch {
            delay(50)
            scrollState.animateScrollTo(scrollState.maxValue)
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
        // Gradient Header
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
                                Color(0xFF1E3C72),
                                Color(0xFF2A5298)
                            )
                        )
                    )
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Console Simulator",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "จำลองการรันโค้ดเป่ายิ้งฉุบแบบ Loop คอนโซลสมบูรณ์แบบ",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Code Snippet Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "โค้ดภาษา Kotlin ที่เปิดรันอยู่เบื้องหลัง (Full Source):", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                CodeSnippet(code = codeText)
            }
        }

        // Action controls
        if (simState == SimulatorState.NOT_STARTED || simState == SimulatorState.GAME_OVER) {
            Button(
                onClick = { startSimulator() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Run")
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (simState == SimulatorState.GAME_OVER) "เริ่มรันใหม่อีกครั้ง" else "สั่งเริ่มรันโปรแกรม (main)")
            }
        }

        // Simulated Console View
        if (simState != SimulatorState.NOT_STARTED) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "หน้าต่างจำลองระบบ Console Terminal:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Black terminal box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F0F0F))
                        .border(1.dp, Color(0xFF333333), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        consoleLogs.forEach { log ->
                            val textColor = if (log.startsWith(">")) {
                                Color(0xFF81D4FA) // Cyan user input
                            } else if (log.startsWith("❌")) {
                                Color(0xFFEF9A9A) // Light red warning
                            } else if (log.contains("ยินดีด้วย") || log.contains("ชนะ")) {
                                Color(0xFFA5D6A7) // Light green wins
                            } else {
                                Color(0xFF00FF00) // Classic green terminal output
                            }

                            Text(
                                text = log,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = textColor
                            )
                        }
                    }
                }

                // Keyboard input bar inside the simulator
                if (simState != SimulatorState.GAME_OVER) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inputVal,
                            onValueChange = { inputVal = it },
                            placeholder = {
                                Text(
                                    if (simState == SimulatorState.AWAITING_PLAYER_CHOICE)
                                        "พิมพ์ Rock, Paper หรือ Scissors"
                                    else
                                        "พิมพ์ yes เพื่อเล่นต่อ / พิมพ์คำอื่นเพื่อออก"
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Send
                            ),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    keyboardController?.hide()
                                    handleInput(inputVal)
                                }
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        Button(
                            onClick = {
                                keyboardController?.hide()
                                handleInput(inputVal)
                            },
                            contentPadding = PaddingValues(12.dp),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send Input")
                        }
                    }
                }
            }
        }
    }
}
