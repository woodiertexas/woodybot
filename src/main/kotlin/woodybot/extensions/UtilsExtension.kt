package woodybot.extensions

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import dev.kord.common.annotation.KordPreview
import woodybot.TEST_SERVER_ID

@OptIn(KordPreview::class)
class UtilsExtension: Extension() {
    override val name = "utilities"

    override suspend fun setup() {
        ephemeralSlashCommand(::SlashApplyArgs) {
            name = "apply"
            description = "Get a link to the server staff application."
            guild(TEST_SERVER_ID)

            action {
                val kord = this@UtilsExtension.kord

                channel.createMessage("Apply for staff here: https://forms.gle/tvKeFjnZ5ZmVpKNq5")
            }
        }
    }

    inner class SlashApplyArgs : Arguments() {
        //
    }
}