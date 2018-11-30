package data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * StackExchange user.
 */
// all fields are nullable because they can be null, e.g. the author of
// https://stackoverflow.com/questions/32033712/is-there-a-downloadable-corpus-dictionary-lexicon-for-informal-playful-words
// doesn't have anything except display_name.
class User(
        @JsonProperty("user_id")
        val id: Int?,
        @JsonProperty("display_name")
        val displayName: String?,
        val link: String?,
        @JsonProperty("profile_image")
        val profileImage: String?)
