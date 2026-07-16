# Android Master Class Sources Directory

คลังซอร์สโค้ดและเวิร์กชอปหลักสูตร **Android Master Class** โดยแบ่งโฟลเดอร์ตามหัวข้อหลักสูตรรายวัน ในการพัฒนาจะเน้นการใช้ตรรกะแบบ Kotlin-First, Compose-First, สถาปัตยกรรมระดับองค์กร (Clean Architecture) และการจัดการแบบ Multi-module ในอนาคต

---

## 📁 สารบัญโฟลเดอร์ซอร์สโค้ด (Workshops Index)

* **[Day2]**: เวิร์กชอปการสร้างเกมเป่ายิ้งฉุบและการปูพื้นฐานไวยากรณ์ภาษา Kotlin (Variables, Booleans, Strings, Chars, I/O readln, If-Else Statements, Exception Try-Catch, While Loops & Validation)

---

## 🛠️ โครงสร้างและการตั้งค่าร่วมกันของโครงการ (Shared Settings)

โปรเจกต์ต่าง ๆ ในไดเรกทอรีนี้จะถูกตั้งค่าโดยยึดหลักเกณฑ์ความทันสมัยและระเบียบการเขียนโค้ดที่ถูกต้อง:

1. **Gradle Kotlin DSL**: ไฟล์ตั้งค่าทั้งหมดใช้สคริปต์รูปแบบ `.gradle.kts` เพื่อช่วยตรวจสอบชนิดข้อมูลและความปลอดภัยในเวลาคอมไพล์ (Compile-time safety)
2. **Gradle Version Catalogs**: จัดการและรวมกลุ่ม Dependencies / Plugins ไว้ที่ส่วนกลางผ่านไฟล์ `gradle/libs.versions.toml` ของแต่ละโปรเจกต์ เพื่อป้องกันปัญหาความไม่เข้ากันของเวอร์ชันไลบรารี
3. **Jetpack Compose & Material 3**: เครื่องมือมาตรฐานในการจัดทำ UI ทั้งหมด โดยไม่มีการเรียกใช้งานระบบ View System (XML) เดิม ยกเว้นในกรณีประเมินการย้ายฐานระบบ (Migration)
4. **GitLab CI/CD Integration**: ในแต่ละโฟลเดอร์ย่อยถูกเตรียมความพร้อมสำหรับการทำระบบ Automated Build และ Test ผ่าน GitLab CI เท่านั้น

---

## 🚀 วิธีการทดสอบและการคอมไพล์ (Build & Compilation)

ในการทดสอบความถูกต้องของแต่ละวัน สามารถเปิด Terminal เข้าไปยังไดเรกทอรีของโปรเจกต์นั้น ๆ (เช่น `Day2/`) และใช้คำสั่ง Gradle ดังต่อไปนี้:

* **ตรวจสอบและคอมไพล์ไฟล์ Kotlin**:
  ```bash
  ./gradlew :app:compileDebugKotlin
  ```
* **ประกอบร่างไฟล์แอปพลิเคชันเวอร์ชันทดสอบ (Build APK)**:
  ```bash
  ./gradlew assembleDebug
  ```

---
**จัดทำโดย Android Developer Agent**  
*เน้นความถูกต้อง ปลอดภัย และมีหลักฐานอ้างอิงเป็นหลักในการเรียนรู้*
