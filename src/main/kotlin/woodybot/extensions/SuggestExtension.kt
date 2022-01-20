package woodybot.extensions

import com.kotlindiscord.kord.extensions.DISCORD_BLURPLE
import com.kotlindiscord.kord.extensions.DISCORD_FUCHSIA
import com.kotlindiscord.kord.extensions.DISCORD_GREEN
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.converters.impl.stringChoice
import com.kotlindiscord.kord.extensions.commands.converters.impl.coalescedString
import com.kotlindiscord.kord.extensions.commands.converters.impl.defaultingCoalescingString
import com.kotlindiscord.kord.extensions.commands.converters.impl.defaultingString
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.utils.addReaction
import com.kotlindiscord.kord.extensions.types.respondEphemeral
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.builder.message.create.embed
import woodybot.TEST_SERVER_ID

@OptIn(KordPreview::class)
class SuggestExtension : Extension() {
    override val name = "test"

    override suspend fun setup() {
        ephemeralSlashCommand(::SlashSuggestArgs) {
            name = "suggest"
            description = "Make a suggestion."
            guild(TEST_SERVER_ID)

            action {
                val kord = this@SuggestExtension.kord
                val suggestionChannel = kord.getChannelOf<TextChannel>(Snowflake(914991174790574150))

                respond {
                    content = "Head to <#914991174790574150> to see your suggestion."
                }
                val eee = suggestionChannel?.createMessage {
                    embed {
                        when(arguments.type) {
                            "server" -> {title = "Server suggestion from ${arguments.name}"; color = DISCORD_BLURPLE}
                            "bot" -> {title = "Bot suggestion from ${arguments.name}"; color = DISCORD_FUCHSIA}
                            "mc" -> {title = "Minecraft suggestion from ${arguments.name}"; color = DISCORD_GREEN}
                        }

                        description = arguments.suggestion

                        footer {
                            text = member?.id.toString()
                        }
                    }
                }
                eee?.addReaction(":yes:914991294227566672")
                eee?.addReaction(":no:914991294219173888")
            }
        }
    }

    inner class SlashSuggestArgs : Arguments() {
        val name: String by coalescedString(
            displayName = "name",
            description = "Input current username or if you're plural; your headmate's name.",
        )

        val type: String by stringChoice(
            displayName = "type",
            description = "Input a suggestion type.",
            choices = mapOf("Discord server suggestion" to "server", "Discord bot suggestion" to "bot", "Minecraft suggestion" to "mc")
        )

        val suggestion: String by coalescedString(
            displayName = "suggestion",
            description = "This is where you write your suggestion."
        )
    }
}
