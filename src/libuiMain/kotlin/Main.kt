import kotlinx.cinterop.memScoped
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import libui.ktx.appWindow
import libui.ktx.draw.font
import libui.ktx.draw.string
import libui.ktx.draw.text
import libui.ktx.drawarea
import libui.ktx.onTimer
import libui.ktx.vbox
import libui.uiDrawTextAlignCenter

fun main() = appWindow(
    title = "Title️",
    width = 320,
    height = 240
) {
    vbox {
        val area = drawarea {
            draw {
                val date = Instant.fromEpochSeconds(1609818594).periodUntil(Clock.System.now(), TimeZone.currentSystemDefault())
                val yearString = if (date.years == 1) "year" else "years"
                val monthString = if (date.months == 1) "month" else "months"
                val dayString = if (date.days == 1) "day" else "days"
                val hourString = if(date.hours == 1) "hour" else "hours"
                val minuteString = if (date.minutes == 1) "minute" else "minutes"
                val secondString = if (date.seconds == 1) "second" else "seconds"
                val dateString = string("${date.years} $yearString, ${date.months} $monthString, ${date.days} $dayString, ${date.hours} $hourString, ${date.minutes} $minuteString and ${date.seconds} $secondString.️")
                text(dateString, font("Helvetica", 16.0), it.AreaWidth, uiDrawTextAlignCenter, 0.0, 0.0)
            }
            stretchy = true
        }
        onTimer(1000) {
            memScoped {
                area.redraw()
            }
            true
        }
    }
}
