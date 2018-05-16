
import org.ocpsoft.prettytime.PrettyTime
import java.io.File
import java.io.FileFilter
import java.util.*

private object TimeFormatter : PrettyTime()

val File.isFolder: Boolean get() = this.isDirectory

val File.size: Long
    get() {
        return if (this.isFolder) {
            var sizeInBytes = 0L
            //percorre todos os arquivos e pastas.
            this.listFiles()?.forEach {
                //Soma só com o tamanho dos arquivos.
                sizeInBytes += if (it.isFile) {
                    it.length()
                } else {
                    //Recursivo
                    //it.size
                    //Não recursivo
                    0
                }
            }
            sizeInBytes
        } else {
            this.length()
        }
    }

internal fun Long.toSizeString(): String {
    return when {
        this >= 1024 * 1024 * 1024 -> String.format("%.1f", this / (1024 * 1024 * 1024).toFloat()) + " GB"
        this >= 1024 * 1024 -> String.format("%.1f", this / (1024 * 1024).toFloat()) + " MB"
        this >= 1024 -> String.format("%.1f", this / 1024f) + " KB"
        else -> this.toString() + " B"
    }
}

val File.sizeAsString: String get() = this.size.toSizeString()

val File.lastModified: String get() = TimeFormatter.format(Date(this.lastModified()))

val File.count: Int get() = if (this.isFolder) this.listFiles()?.size ?: 0 else 0

fun File.count(filter: FileFilter): Int = if (this.isFolder) this.listFiles(filter)?.size
        ?: 0 else 0

val File.isProtected: Boolean get() = !this.canRead() || !this.canWrite()