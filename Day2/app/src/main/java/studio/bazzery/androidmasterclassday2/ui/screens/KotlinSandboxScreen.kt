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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KotlinSandboxScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
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
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.primary
                            )
                        )
                    )
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Kotlin Basics Playground",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ทดลองตรรกะและเรียนรู้โครงสร้างพื้นฐานภาษา Kotlin (Day 2)",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Module 1: Hello World
        Module1HelloWorldCard()

        // Module 2: Variables (val vs var)
        Module2VariablesCard()

        // Module 3: Booleans, Chars & Strings
        Module3DataTypesCard()

        // Module 4: If-Else & Input Validation (Age Checker)
        Module4IfElseAgeCard()
    }
}

@Composable
fun Module1HelloWorldCard() {
    var consoleOutput by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "ส่วนที่ 1: ปฐมนิเทศและชัยชนะแรก (Hello World)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "เรียนรู้การทำงานของฟังก์ชันหลัก `main()` และคำสั่งแสดงผล `println()` ในภาษา Kotlin", fontSize = 14.sp)

            CodeSnippet(
                code = "fun main() {\n" +
                        "    println(\"Hello World\")\n" +
                        "}"
            )

            Button(onClick = {
                consoleOutput = "Hello World"
            }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Run")
                Spacer(modifier = Modifier.width(8.dp))
                Text("รันโค้ด (Quick Win!)")
            }

            if (consoleOutput != null) {
                ConsoleOutputBox(text = consoleOutput!!)
            }
        }
    }
}

@Composable
fun Module2VariablesCard() {
    var enemyScore by remember { mutableIntStateOf(5) }
    var consoleLogs by remember { mutableStateOf(listOf<String>()) }
    var compileErrorMsg by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "ส่วนที่ 2: กล่องเก็บของและตัวเลข (Variables)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "เปรียบเทียบกฎเหล็ก:\n• `val` (Value) = กล่องปิดตาย แก้ไขไม่ได้ (Read-only)\n• `var` (Variable) = กล่องเปิดแก้ไขค่าได้ตลอดเวลา (Mutable)", fontSize = 14.sp)

            CodeSnippet(
                code = "val myScore = 10\n" +
                        "var enemyScore = 5\n\n" +
                        "// ทดลองเปลี่ยนค่า\n" +
                        "enemyScore = 8  // ทำได้\n" +
                        "myScore = 11     // ERROR! Val cannot be reassigned"
            )

            Text(
                text = "ค่าปัจจุบัน: myScore = 10 (val) | enemyScore = $enemyScore (var)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        enemyScore++
                        compileErrorMsg = null
                        consoleLogs = consoleLogs + "เพิ่ม enemyScore สำเร็จ! -> enemyScore = $enemyScore"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("เพิ่ม enemyScore (var)")
                }

                FilledTonalButton(
                    onClick = {
                        compileErrorMsg = "compiler error: Val cannot be reassigned\n(เนื่องจาก myScore ถูกประกาศเป็น val จึงไม่สามารถเขียนทับค่าได้)"
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("พยายามเพิ่ม myScore (val)")
                }
            }

            if (compileErrorMsg != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    border = BorderStroke(1.dp, Color(0xFFE57373)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = "Compile Error", tint = Color(0xFFC62828))
                        Text(text = compileErrorMsg!!, fontSize = 12.sp, color = Color(0xFFC62828), fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            if (consoleLogs.isNotEmpty()) {
                ConsoleOutputBox(text = consoleLogs.joinToString("\n"))
            }
        }
    }
}

@Composable
fun Module3DataTypesCard() {
    var isSunnyDay by remember { mutableStateOf(true) }
    val isRaining = false // Immutable system check
    var playerName by remember { mutableStateOf("Android Dev") }
    val firstLetter: Char = 'R'

    val isNotSunny = !isSunnyDay

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "ส่วนที่ 3: ความจริง, ตัวอักษร, และข้อความ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "ทำความเข้าใจชนิดข้อมูลเบื้องต้น `Boolean` (สวิตช์), `Char` (ตัวอักษรเดี่ยว), และ `String` (ข้อความต่อกันด้วย String Templates)", fontSize = 14.sp)

            CodeSnippet(
                code = "val isSunnyDay: Boolean = $isSunnyDay\n" +
                        "val isNotSunny = !isSunnyDay // ได้ผลลัพธ์ $isNotSunny\n" +
                        "val firstLetter: Char = '$firstLetter'\n" +
                        "val msg = \"สวัสดีคุณ \$playerName! ตัวแรกคือ \$firstLetter\""
            )

            // Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "แดดออกวันนี้ (isSunnyDay):", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Switch(checked = isSunnyDay, onCheckedChange = { isSunnyDay = it })
            }

            Text(
                text = "ผลของการสลับค่า (!isSunnyDay): isNotSunny = $isNotSunny",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isNotSunny) Color(0xFFC62828) else Color(0xFF2E7D32)
            )

            // String Input
            OutlinedTextField(
                value = playerName,
                onValueChange = { playerName = it },
                label = { Text("กรอกชื่อผู้เล่น (playerName: String)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Display template
            ConsoleOutputBox(
                text = "ผลลัพธ์ String Template:\n" +
                        "สวัสดีคุณ $playerName! ตัวอักษรแรกของคำตอบคือ $firstLetter\n" +
                        "วันนี้แดดออกใช่ไหม? ตอบ: $isSunnyDay | ฝนตกใช่ไหม? ตอบ: $isRaining\n" +
                        "แดดไม่ออกใช่ไหม? ตอบ: $isNotSunny"
            )
        }
    }
}

@Composable
fun Module4IfElseAgeCard() {
    val keyboardController = LocalSoftwareKeyboardController.current
    var ageInputVal by remember { mutableStateOf("") }
    var ageResultMsg by remember { mutableStateOf<String?>(null) }
    var ageResultError by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "ส่วนที่ 4: การรับค่าและการตัดสินใจ (If-Else & Crash Prevention)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "การตรวจสอบเงื่อนไขเชิงตรรกะแบบหลายทางแยก (`if`, `else if`, `else`) ร่วมกับการตรวจช่วงตัวเลขด้วยคีย์เวิร์ด `in` พร้อมระบบดักจับการแปลงข้อมูล (Try-Catch) เพื่อป้องกันแอปพังตามคำเตือนของ Mentor", fontSize = 14.sp)

            CodeSnippet(
                code = "val ageInt = ageString.toInt() // เสี่ยงแอปพังหากไม่ใช่ตัวเลข!\n" +
                        "if (ageInt >= 60) \"ผู้สูงอายุที่น่าเคารพ\"\n" +
                        "else if (ageInt in 13..19) \"วัยรุ่นวัยมันส์!\"\n" +
                        "else if (ageInt < 13) \"เด็กน้อย\"\n" +
                        "else \"ผู้ใหญ่วัยทำงาน\""
            )

            OutlinedTextField(
                value = ageInputVal,
                onValueChange = {
                    ageInputVal = it
                    ageResultMsg = null
                    ageResultError = null
                },
                label = { Text("โปรดกรอกอายุของคุณ:") },
                placeholder = { Text("ใส่ตัวเลข เช่น 25 (หรือลองกรอกตัวอักษร)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                isError = ageResultError != null,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    keyboardController?.hide()
                    try {
                        // Attempt to parse string to integer (Danger zone)
                        val ageInt = ageInputVal.trim().toInt()
                        val birthYear = 2026 - ageInt
                        
                        // Decision logic matching page 11
                        val category = if (ageInt >= 60) {
                            "คุณคือผู้สูงอายุที่น่าเคารพ 👴👵"
                        } else if (ageInt in 13..19) {
                            "คุณอยู่ในวัยรุ่นวัยมันส์! ⚡"
                        } else if (ageInt < 13) {
                            "คุณยังเป็นเด็กน้อยอยู่เลย 👶"
                        } else {
                            "คุณคือผู้ใหญ่วัยทำงาน! 💼"
                        }

                        ageResultError = null
                        ageResultMsg = "ปีเกิดของคุณคือ ค.ศ. $birthYear (เมื่อปีนี้คือ 2026)\nผลประเมินช่วงอายุ: $category"
                    } catch (e: NumberFormatException) {
                        // Gracefully catch validation failure (Mentor's Warning)
                        ageResultMsg = null
                        ageResultError = "⚠️ ตรวจพบบั๊ก: เกิดข้อผิดพลาด NumberFormatException ในคำสั่ง .toInt() เนื่องจากคุณกรอกค่าที่ไม่ใช่จำนวนเต็ม!\n\nดีที่แอปมีระบบ Try-Catch ครอบไว้ ทำให้ไม่แครชพัง แต่สามารถแสดงคำเตือนนี้ได้"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("คำนวณและวิเคราะห์ผล")
            }

            if (ageResultMsg != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    border = BorderStroke(1.dp, Color(0xFF4CAF50)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = "Result", tint = Color(0xFF2E7D32))
                        Text(text = ageResultMsg!!, fontSize = 14.sp, color = Color(0xFF1B5E20), fontWeight = FontWeight.Medium)
                    }
                }
            }

            if (ageResultError != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                    border = BorderStroke(1.dp, Color(0xFFFFB74D)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = "Warning", tint = Color(0xFFE65100))
                        Text(text = ageResultError!!, fontSize = 13.sp, color = Color(0xFFE65100), fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}



@Composable
fun ConsoleOutputBox(text: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = "จำลอง Console Output:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF1E1E1E))
                .padding(12.dp)
        ) {
            Text(
                text = text,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                color = Color(0xFF00FF00) // Green console output
            )
        }
    }
}
