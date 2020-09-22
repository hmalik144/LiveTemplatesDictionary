// Dependencies to be added to app/build.gradle:
//def scarlet_version = '0.1.9'
//implementation "com.tinder.scarlet:scarlet:${scarlet_version}"
//implementation "com.tinder.scarlet:websocket-okhttp:${scarlet_version}"
//implementation "com.tinder.scarlet:message-adapter-gson:${scarlet_version}"
//implementation "com.tinder.scarlet:stream-adapter-coroutines:${scarlet_version}"
//implementation "com.tinder.scarlet:lifecycle-android:${scarlet_version}"
//def okhttp3_version = '4.9.0'
//implementation "com.squareup.okhttp3:okhttp:${okhttp3_version}"
//implementation "com.squareup.okhttp3:logging-interceptor:${okhttp3_version}"
//implementation "com.google.code.gson:gson:2.8.6"

interface MessengerApi {

    // Receive websocket messages in the form of string
    @com.tinder.scarlet.ws.Receive
    fun observerMessage(): kotlinx.coroutines.channels.ReceiveChannel<MessageItem>

    @com.tinder.scarlet.ws.Receive
    fun observerEvent(): kotlinx.coroutines.channels.ReceiveChannel<com.tinder.scarlet.WebSocket.Event>

    // Send message to websocket and return pass/fail boolean result
    @com.tinder.scarlet.ws.Send
    fun send(message: MessageItem): Boolean


    // invoke method creating an invocation of the api call
    companion object{
        operator fun invoke(
            webSocketUrl: String = "ws://echo.websocket.org/",
            networkInterceptor: NetworkConnectionInterceptor
        ) : MessengerApi {

            val okHttpClient = okhttp3.OkHttpClient.Builder()
                    .addNetworkInterceptor(networkInterceptor)
                    .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .build()


            // creation of Api class for websocket
            return com.tinder.scarlet.Scarlet.Builder()
                .webSocketFactory(okHttpClient.newWebSocketFactory(webSocketUrl))
                .addMessageAdapterFactory(com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter.Factory())
                .addStreamAdapterFactory(com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory())
                .build()
                .create()

        }
    }
}

/**
 * Network interceptor to verify valid network connection
 * Throws an [IOException] if there no valid internet connection
 */
class NetworkConnectionInterceptor(
    context: android.content.Context
): okhttp3.Interceptor {
    private val applicationContext = context.applicationContext

    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        if (!isInternetAvailable()) {
            throw java.io.IOException("Make sure you have an active data connection")
        }

        return chain.proceed(chain.request())
    }

    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            applicationContext.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager?
        connectivityManager?.let {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                result = when {
                    hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
        }
        return result
    }
}

data class MessageItem(
    @com.google.gson.annotations.SerializedName("")
    @com.google.gson.annotations.Expose
    var body: HashMap<String, Any>? = null
)
