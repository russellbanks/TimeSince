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
    title = "TimeSince",
    width = 320,
    height = 240
) {
    vbox {
        val area = drawarea {
            draw {
                val originalDate = Instant.fromEpochSeconds(0)
                val timeSince = originalDate.periodUntil(Clock.System.now(), TimeZone.currentSystemDefault())
                val yearString = if (timeSince.years == 1) "year" else "years"
                val monthString = if (timeSince.months == 1) "month" else "months"
                val dayString = if (timeSince.days == 1) "day" else "days"
                val hourString = if(timeSince.hours == 1) "hour" else "hours"
                val minuteString = if (timeSince.minutes == 1) "minute" else "minutes"
                val secondString = if (timeSince.seconds == 1) "second" else "seconds"
                val dateString = string("It has been ${timeSince.years} $yearString, ${timeSince.months} $monthString, ${timeSince.days} $dayString, ${timeSince.hours} $hourString, ${timeSince.minutes} $minuteString and ${timeSince.seconds} $secondString since $originalDate")
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
