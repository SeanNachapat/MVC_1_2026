# แบบฟอร์มสรุปการส่งคำตอบ
**Exit Exam MVC 1/2569 – อาทิตย์เช้า**

---

### 1) วิธีเปิดโปรแกรมและจุดเริ่มต้นของโปรแกรม (ไม่เกิน 3 บรรทัด)
- **จุดเริ่มต้นโปรแกรม:** `MainMVC.java` (เมธอด `main`)
- **วิธีรันโปรแกรม:**
  ```bash
  javac -encoding UTF-8 -cp "lib/*;." -d bin (Get-ChildItem -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName)
  java -cp "bin;lib/*;." MainMVC
  ```

---

### 2) ตารางเชื่อมโยง Requirements

| Requirement | Model / Domain | Controller / Action | View / Screen |
| :--- | :--- | :--- | :--- |
| **R1 (Data Loading & Initialization)** | `ElectionData`, `Election`, `Candidate`, `Voter`, `Ballot`, `ElectionService.loadFromData()` | `MainMVC.main()` เรียก `JsonService.loadElectionData()` | `ElectionView.success()`, `ElectionView.title()` |
| **R2 (Candidate Viewing & Voting)** | `Candidate`, `Voter`, `Ballot`, `ElectionService.castVote()` | `ElectionController.showCandidates()`, `processVoteInput()`, `handleCastVote()` | `ElectionView.candidates()`, `ElectionView.votingTitle()` |
| **R3 (Close Polls & Duplicate Pattern Detection)** | `BallotGroup`, `BallotStatus.PENDING_INSPECTION`, `ElectionService.closePollsAndDetectPatterns()` | `ElectionController.handleClosePolls()` | `ElectionView.summary()`, `ElectionView.ballotGroupsSummary()` |
| **R4 (Group Inspection, Audit & Conclusion)** | `ElectionService.decidePatternGroup()`, `calculateScores()`, `BallotStatus`, `ElectionStatus.CONCLUDED` | `ElectionController.processGroupDecision()`, `handleDecideGroup()`, `showBallotsAudit()` | `ElectionView.votingAudit()`, `ElectionView.votingAuditMenu()`, `ElectionView.ballotsAudit()` |
| **R5 (Status Summary & Error Handling)** | `ElectionService.getTotalBallotCount()`, `getCertifiedBallotCount()`, `getPendingBallotCount()`, `getVoidBallotCount()` | `ElectionController.showStatus()`, การดักจับ Exception และส่ง Error ไปยัง View | `ElectionView.summary()`, `ElectionView.error()`, `ElectionView.success()` |

---

### 3) ผลการทดสอบ

| กรณี | ผ่าน/ไม่ผ่าน | หมายเหตุ (เฉพาะที่จำเป็น) |
| :---: | :---: | :--- |
| **T1** | ผ่าน | แสดงรหัสและชื่อผู้สมัครครบถ้วน |
| **T2** | ผ่าน | ผู้มีสิทธิ์ที่ Active ลงคะแนนจัดอันดับ 3 คนที่ต่างกันได้ถูกต้อง |
| **T3** | ผ่าน | ปฏิเสธการลงคะแนนซ้ำ, เลือกไม่ครบ, เลือกคนซ้ำในบัตร หรือลงคะแนนหลังปิดหีบ โดยไม่ทำให้เสียสิทธิ์ |
| **T4** | ผ่าน | ปิดรับคะแนนแล้วหยุดรับบัตรใหม่ ตรวจจับรูปแบบการเลือกที่ซ้ำ $\ge 3$ ใบ เข้าสถานะ `PENDING_INSPECTION` และไม่นับคะแนนชั่วคราว |
| **T5** | ผ่าน | เจ้าหน้าที่ตรวจและตัดสินกลุ่มบัตรเป็น `CERTIFIED` หรือ `VOID` ได้ถูกต้อง |
| **T6** | ผ่าน | สรุปผล `CONCLUDED` คำนวณคะแนนตามน้ำหนัก (3, 2, 1) และตรวจสอบย้อนกลับ (Traceability) ของบัตรได้ครบถ้วน |

---

### 4) ความแตกต่างระหว่างแบบที่ออกแบบกับโปรแกรมจริง (ถ้ามี ไม่เกิน 3 ข้อ)
1. ไม่มีข้อแตกต่างอย่างมีนัยสำคัญ โครงสร้างแยกส่วน Model-View-Controller ตามหลักสถาปัตยกรรม MVC 

---

### 5) บันทึกการใช้ Generative AI (AI Coaching Level 1)

| เวลาโดยประมาณ | เครื่องมือ | ใช้เพื่ออะไร | นำคำแนะนำไปใช้อย่างไร |
| :---: | :---: | :--- | :--- |
| 10:30 - 11:55 | Antigravity (Gemini) | สอบถามแนวคิดการแยกสถาปัตยกรรม MVC, การตรวจสอบเงื่อนไข Exception และการจัดรูปแบบ Traceability | นำคำแนะนำด้านโครงสร้างมาตัดสินใจออกแบบคลาส `BallotGroup` และพัฒนาโค้ดส่วน Business Logic ด้วยตนเอง |
