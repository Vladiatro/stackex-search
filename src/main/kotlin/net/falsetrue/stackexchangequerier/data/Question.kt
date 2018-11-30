package data

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import net.falsetrue.stackexchangequerier.util.UnixTimestampDeserializer
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val WORDS_TO_KEEP = 30

/**
 * StackExchange question.
 */
class Question(
        @JsonProperty("question_id")
        val id: Int,
        val title: String,
        val body: String,
        val link: String,
        @JsonProperty("creation_date")
        @JsonDeserialize(using = UnixTimestampDeserializer::class)
        val creationDate: LocalDateTime,
        @JsonProperty("answer_count")
        val answerCount: Int,
        val owner: User) {

    /**
     * Removes HTML, line endings, and double spaces from the body;
     * keeps just the first 30 words (simple algorithm which is based on spaces).
     */
    fun getBodyPreview(): String {
        val text = Jsoup.clean(body, Whitelist.none())
                .replace("\\n", " ")
                .replace("  ", " ")
        var currentIndex = 0
        var spacesCount = 0
        while (currentIndex < text.length - 1 && spacesCount < WORDS_TO_KEEP) {
            currentIndex++
            if (text[currentIndex] == ' ') {
                spacesCount++;
            }
        }
        return text.substring(0, currentIndex) + if (currentIndex < text.length) "…" else ""
    }

    /**
     * Returns date in the format of 2018-02-13
     */
    fun getDateFormatted(): String {
        return creationDate.format(DateTimeFormatter.ISO_DATE)
    }
}
