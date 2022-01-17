package woodybot.extensions

import com.kotlindiscord.kord.extensions.DISCORD_BLACK
import com.kotlindiscord.kord.extensions.DISCORD_BLURPLE
import com.kotlindiscord.kord.extensions.DISCORD_FUCHSIA
import com.kotlindiscord.kord.extensions.DISCORD_GREEN
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.coalescedString
import com.kotlindiscord.kord.extensions.commands.converters.impl.defaultingCoalescingString
import com.kotlindiscord.kord.extensions.commands.converters.impl.defaultingString
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.utils.addReaction
import dev.kord.rest.builder.message.create.embed
import dev.kord.core.behavior.channel.createMessage
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.channel.TextChannel
import woodybot.TEST_SERVER_ID

@OptIn(KordPreview::class)
class SuggestExtension : Extension() {
    override val name = "test"

    override suspend fun setup() {
        chatCommand(::SuggestArgs) {
            name = "suggest"
            description = "Make a suggestion."

            check { failIf(event.message.author == null) }

            action {
                val kord = this@SuggestExtension.kord
                val suggestionChannel = kord.getChannelOf<TextChannel>(Snowflake(914991174790574150))

                var embedTitle = ""
                var embedColor = DISCORD_BLACK
                if (arguments.type == "server") {
                    embedTitle = "Server suggestion from ${arguments.name}"
                    embedColor = DISCORD_BLURPLE
                } else if (arguments.type == "bot") {
                    embedTitle = "Bot suggestion from ${arguments.name}"
                    embedColor = DISCORD_FUCHSIA
                } else if (arguments.type == "mc") {
                    embedTitle = "Minecraft suggestion from ${arguments.name}"
                    embedColor = DISCORD_GREEN
                }

                val eee = suggestionChannel?.createMessage {
                    embed {
                        title = embedTitle
                        color = embedColor
                        description = arguments.suggestion

                        footer {
                            text = message.author?.id.toString()
                        }
                    }
                    return@createMessage
                }
                eee?.addReaction(":yes:914991294227566672")
                eee?.addReaction(":no:914991294219173888")
            }
        }

        ephemeralSlashCommand(::SlashSuggestArgs) {
            name = "suggest"
            description = "Make a suggestion."
            guild(TEST_SERVER_ID)

            action {
                val kord = this@SuggestExtension.kord
                val suggestionChannel = kord.getChannelOf<TextChannel>(Snowflake(914991174790574150))

                var embedTitle = ""
                var embedColor = DISCORD_BLACK
                if (arguments.type == "server") {
                    embedTitle = "Server suggestion from ${arguments.name}"
                    embedColor = DISCORD_BLURPLE
                } else if (arguments.type == "bot") {
                    embedTitle = "Bot suggestion from ${arguments.name}"
                    embedColor = DISCORD_FUCHSIA
                } else if (arguments.type == "mc") {
                    embedTitle = "Minecraft suggestion from ${arguments.name}"
                    embedColor = DISCORD_GREEN
                }

                val eee = suggestionChannel?.createMessage {
                    embed {
                        title = embedTitle
                        color = embedColor
                        description = arguments.suggestion

                        footer {
                            text = member?.id.toString()
                        }
                    }
                    return@createMessage
                }
                eee?.addReaction(":yes:914991294227566672")
                eee?.addReaction(":no:914991294219173888")
            }
        }
    }

    inner class SuggestArgs : Arguments() {
        val name: String by coalescedString(
            displayName = "name",
            description = "This could be your current username or if you're a part of a plural system; your headmate(s) name(s).\n",
        )

        val type: String by coalescedString(
            displayName = "type",
            description = "The suggestion type can be any of the following: `server` for a Discord server suggestion, `bot` for a bot suggestion, and `mc` for a Minecraft related suggestion.\n",
        )

        val suggestion: String by coalescedString(
            displayName = "suggestion",
            description = "This is where you write your suggestion."
        )
    }

    inner class SlashSuggestArgs : Arguments() {
        val name: String by coalescedString(
            displayName = "name",
            description = "Input current username or if you're plural; your headmate's name.",
        )

        val type: String by coalescedString(
            displayName = "type",
            description = "\"server\": Discord server suggestion, \"bot\": bot suggestion, \"mc\": MC related suggestion.",
        )

        val suggestion: String by coalescedString(
            displayName = "suggestion",
            description = "This is where you write your suggestion."
        )
    }
}
