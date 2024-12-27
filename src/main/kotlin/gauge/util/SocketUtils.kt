package gauge.util

import com.intellij.openapi.diagnostic.Logger
import java.io.IOException
import java.net.ServerSocket

object SocketUtils {
    private val LOG = Logger.getInstance(SocketUtils::class.java)

    fun findFreePortForApi(): Int {
        var socket: ServerSocket? = null
        return try {
            socket = ServerSocket(0)
            socket.localPort
        } catch (e: IOException) {
            LOG.debug(e)
            -1
        } finally {
            try {
                socket?.close()
            } catch (e: IOException) {
                LOG.debug(e)
            }
        }
    }
}
