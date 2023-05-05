package `in`.fiberstory.tfsplaytv.utility

import android.text.TextUtils
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.drm.UnsupportedDrmException
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import java.util.*

object DemoUtil {
    /**
     * Derives a DRM [UUID] from `drmScheme`.
     *
     * @param drmScheme A protection scheme UUID string; or `"widevine"`, `"playready"` or
     * `"clearkey"`.
     * @return The derived [UUID].
     * @throws UnsupportedDrmException If no [UUID] could be derived from `drmScheme`.
     */
    @Throws(UnsupportedDrmException::class)
    fun getDrmUuid(drmScheme: String?): UUID {
        return when (Util.toLowerInvariant(drmScheme!!)) {
            "widevine" -> C.WIDEVINE_UUID
            "playready" -> C.PLAYREADY_UUID
            "clearkey" -> C.CLEARKEY_UUID
            else -> try {
                UUID.fromString(drmScheme)
            } catch (e: RuntimeException) {
                throw UnsupportedDrmException(UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME)
            }
        }
    }

    /**
     * Builds a track name for display.
     *
     * @param format [Format] of the track.
     * @return a generated name specific to the track.
     */
    fun buildTrackName(format: Format): String {
        val trackName: String
        trackName = if (MimeTypes.isVideo(format.sampleMimeType)) {
            joinWithSeparator(
                joinWithSeparator(
                    joinWithSeparator(
                        buildResolutionString(format), buildBitrateString(format)
                    ), buildTrackIdString(format)
                ),
                buildSampleMimeTypeString(format)
            )
        } else if (MimeTypes.isAudio(format.sampleMimeType)) {
            joinWithSeparator(
                joinWithSeparator(
                    joinWithSeparator(
                        joinWithSeparator(
                            buildLanguageString(format), buildAudioPropertyString(format)
                        ),
                        buildBitrateString(format)
                    ), buildTrackIdString(format)
                ),
                buildSampleMimeTypeString(format)
            )
        } else {
            joinWithSeparator(
                joinWithSeparator(
                    joinWithSeparator(
                        buildLanguageString(format),
                        buildBitrateString(format)
                    ), buildTrackIdString(format)
                ),
                buildSampleMimeTypeString(format)
            )
        }
        return if (trackName.length == 0) "unknown" else trackName
    }

    private fun buildResolutionString(format: Format): String {
        return if (format.width == Format.NO_VALUE || format.height == Format.NO_VALUE) "" else format.width.toString() + "x" + format.height
    }

    private fun buildAudioPropertyString(format: Format): String {
        return if (format.channelCount == Format.NO_VALUE || format.sampleRate == Format.NO_VALUE) "" else format.channelCount.toString() + "ch, " + format.sampleRate + "Hz"
    }

    private fun buildLanguageString(format: Format): String {
        return if (TextUtils.isEmpty(format.language) || "und" == format.language) "" else format.language!!
    }

    private fun buildBitrateString(format: Format): String {
        return if (format.bitrate == Format.NO_VALUE) "" else String.format(
            Locale.US,
            "%.2fMbit",
            format.bitrate / 1000000f
        )
    }

    private fun joinWithSeparator(first: String, second: String): String {
        return if (first.length == 0) second else if (second.length == 0) first else "$first, $second"
    }

    private fun buildTrackIdString(format: Format): String {
        return if (format.id == null) "" else "id:" + format.id
    }

    private fun buildSampleMimeTypeString(format: Format): String {
        return if (format.sampleMimeType == null) "" else format.sampleMimeType!!
    }
}
