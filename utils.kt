package furhatos.app.openaiskill.flow.utils
import java.io.File

object Utils {
    fun getLastWord(sentence: String): String {
        return sentence.trim().split(" ").last()
    }
}

// Path to the log file
val logFile = File("conversation_log.txt")

// Function to log utterances to a text file
fun logUtterance(speaker: String, message: String) {
    if (!logFile.exists()) {
        logFile.createNewFile() // Create the file if it doesn't exist
    }
    logFile.appendText("$speaker: $message\n")
}

