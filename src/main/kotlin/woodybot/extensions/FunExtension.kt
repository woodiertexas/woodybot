package woodybot.extensions

import com.kotlindiscord.kord.extensions.DISCORD_FUCHSIA
import com.kotlindiscord.kord.extensions.DISCORD_RED
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.defaultingCoalescingString
import com.kotlindiscord.kord.extensions.commands.converters.impl.user
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.event
import com.kotlindiscord.kord.extensions.utils.respond
import dev.kord.rest.builder.message.create.embed
import dev.kord.core.behavior.channel.createMessage
import dev.kord.common.annotation.KordPreview

@OptIn(KordPreview::class)
class FunExtension : Extension() {
    override val name = "fun"

    override suspend fun setup() {
        chatCommand(::BamArgs) {
            name = "bam"
            description = "fakeban a user"

            check { failIf(event.message.author == null) }

            action {
                val kord = this@FunExtension.kord


                channel.createMessage("${message.author?.username} banned ${arguments.target.username} for: **${arguments.reason}**")
            }
        }

        chatCommand(::KillArgs) {
            name = "kill"
            description = "kill a target"

            check { failIf(event.message.author == null) }

            action {
                val kord = this@FunExtension.kord

                val realTarget = if (arguments.target.id == kord.selfId) {
                    //channel.createMessage("woodybot bops ${message.author} on the head.")
                    message.author!!
                } else if (arguments.target.id == message.author?.id) {
                    message.author!!
                } else {
                    arguments.target
                }

                val killListMut: MutableList<String> = mutableListOf(
                    "**${message.author?.username}** drops sand blocks on **${realTarget.username}**",
                    "**${message.author?.username}** drops an anvil on **${realTarget.username}**",
                    "**${message.author?.username}** knocks **${realTarget.username}** out of the arena in Bedwars."
                )
                val killList: List<String> = killListMut

                channel.createMessage(killList.random())
            }
        }

        chatCommand(::EightBallArgs) {
            name = "8ball"
            description = "Ask the magic 8ball."

            check { failIf(event.message.author == null) }

            action {
                val eightBallListMut: MutableList<String> = mutableListOf(
                    "It is certain.", "It is decidedly so.",
                    "Without a doubt.", "Yes - definitely.",
                    "You may rely on it.", "As I see it, yes.",
                    "Most likely.", "Outlook good.", "Yes.",
                    "Signs point to yes.", "Reply hazy, try again.",
                    "Ask again later.", "Better not tell you now.",
                    "Cannot predict now.", "Concentrate and ask again.",
                    "Don't count on it.", "My reply is no.",
                    "My sources say no.", "Outlook not so good.",
                    "Very doubtful."
                )
                val eightBallList: List<String> = eightBallListMut

                channel.createMessage {
                    embed {
                        title = ""
                        color = DISCORD_FUCHSIA

                        field {
                            name = "Question Asked by ${message.author?.username}."
                            value = arguments.question

                        }

                        field {
                            name = "Response"
                            value = eightBallList.random()
                        }
                    }
                }
            }
        }

        chatCommand(::SlapArgs) {
            name = "slap"
            description = "Ask the bot to slap another user"

            check { failIf(event.message.author == null) }

            action {
                // Because of the DslMarker annotation KordEx uses, we need to grab Kord explicitly
                val kord = this@FunExtension.kord

                // Don't slap ourselves on request, slap the requester!
                val realTarget = if (arguments.target.id == kord.selfId) {
                    message.author!!
                } else {
                    arguments.target
                }

                message.respond("*slaps ${realTarget.mention} with their ${arguments.weapon}*")
            }
        }

        chatCommand(::SadTromboneArgs) {
            name = "sadtrombone"
            description = "Post a sad trombone."

            check { failIf(event.message.author == null) }

            action {
                val kord = this@FunExtension.kord

                val realTarget = if (arguments.target.id == kord.selfId) {
                    message.author!!
                } else {
                    arguments.target
                }

                channel.createMessage {
                    embed {
                        title = ""
                        description = "Uh oh! Looks like ${realTarget.username} did a big fail."

                        field {
                            value = "https://www.youtube.com/watch?v=CQeezCdF4mk"
                        }
                    }
                }
            }
        }
    }

    inner class BamArgs: Arguments() {
        val target by user(displayName = "target user", description = "user to fakeban")
        val reason: String by defaultingCoalescingString(
                displayName = "reason",
                defaultValue = "aaaaaaaaaaaaaaaaaaaaaa",
                description = "Give a reason"
        )
    }

    inner class KillArgs: Arguments() {
        val target by user(displayName = "target user", description = "user to kill lol")
    }

    inner class SadTromboneArgs: Arguments() {
        val target by user(displayName = "target", description = "Person you want to give a sad trombone to.")
    }

    inner class EightBallArgs: Arguments() {
        val question: String by defaultingCoalescingString(
            displayName = "question",
            defaultValue = "are kotlin and python the same programming language",
            description = "Ask a question."
        )
    }

    inner class SlapArgs : Arguments() {
        val target by user("target", description = "Person you want to slap")

        val weapon by defaultingCoalescingString(
            "weapon",

            defaultValue = "large, smelly trout",
            description = "What you want to slap with"
        )
    }
}
