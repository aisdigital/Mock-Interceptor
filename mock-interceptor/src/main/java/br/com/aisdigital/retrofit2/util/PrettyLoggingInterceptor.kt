package br.com.aisdigital.retrofit2.util

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class PrettyLoggingInterceptor {
    companion object {
        private const val LOG_TAG: String = "Retrofit"
        fun getInterceptor(): Interceptor {
            return HttpLoggingInterceptor( HttpLoggingInterceptor.Logger { message ->
                if (!message.isNullOrEmpty()) {
                    var msg = message
                    try {
                        msg = GsonBuilder().setPrettyPrinting().serializeNulls().create()
                            .toJson(JsonParser().parse(message))
                        msg = " \n$msg"
                    } catch (m: JsonSyntaxException) {
                    }
                    largeLog(msg)
                }
            })
        }

        private fun largeLog(content: String) {
            var totalString = content
            var totalLength = totalString.length

            while (totalLength > 3000) {
                val log = totalString.substring(0, 3000)
                Timber.tag(LOG_TAG).d(" \n$log")
                totalString = totalString.removeRange(0, 3000)
                totalLength = totalString.length
            }
            if (totalLength > 0)
                Timber.tag(LOG_TAG).d(" \n$totalString")
        }
    }
}