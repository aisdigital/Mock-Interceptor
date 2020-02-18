package br.com.aisdigital.retrofit2.mock

import android.content.Context
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import okhttp3.*
import okio.Buffer
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

/**
 * @author Natan Guilherme
 * @since 02/28/19
 */
open class MockInterceptor(private val context: Context) : Interceptor {

    companion object {
        // The server either does not recognize the request method, or it lacks the ability to fulfil the request.
        const val DEFAULT_ERROR = 501
        private const val LOG_TAG = "MockInterceptor"
        private val UTF8 = Charset.forName("UTF-8")
    }

    private val MEDIA_JSON = MediaType.parse("application/json")

    private val gson = Gson()

    override fun intercept(chain: Interceptor.Chain?): Response {

        val request = chain?.request()
        val mockFilePath = getMockFilePath(request?.url())
        // Find and return the json string
        val mockFileJSON = getJson(mockFilePath)
        // Convert the Json String to list
        val mockJsonList = gson.fromJson(mockFileJSON, Array<MockJSON>::class.java)
        val mockJSON: MockJSON? = getMockJSON(request, mockJsonList)

        if (mockFileJSON.isEmpty())
            Timber.tag(LOG_TAG).i("Mock file not found at: $mockFilePath")
        else
            Timber.tag(LOG_TAG).i("Mock file found at: $mockFilePath")

        if (mockJSON == null && mockFileJSON.isNotEmpty())
            Timber.tag(LOG_TAG).i("There wasn't any item in mock file that match with the current request configuration")

        // use default failure code mock object have not been found
        var code = DEFAULT_ERROR

        var responseJson = ""
        if (mockJSON != null) {
            code = mockJSON.code
            mockJSON.response?.let {
                responseJson = fixJSON(it)
            }
        }

        return Response.Builder()
            .body(ResponseBody.create(MEDIA_JSON, responseJson))
            .request(chain!!.request())
            .code(code)
            .protocol(Protocol.HTTP_2)
            .message("")
            .build()
    }

    private fun getMockFilePath(url: HttpUrl?): String {
        val path = url?.encodedPath()
        val updatePath = path?.replace("/".toRegex(), "_")
        return "mockData/mock$updatePath"
    }

    private fun getMockJSON(request: Request?, mockJsonList: Array<MockJSON>?): MockJSON? {
        val paramURL = getParamURL(request)
        val paramJSON = getParamJSON(list = paramURL)
        val requestBody = request?.body()
        var requestJSON = ""
        if (requestBody != null && mockJsonList != null) {
            requestJSON = getRequestBodyJSON(requestBody)
        }
        return getMockJSON(mockJsonList, requestJSON, paramJSON)
    }

    private fun getRequestBodyJSON(requestBody: RequestBody): String {
        var charset: Charset? = UTF8
        val contentType = requestBody.contentType()

        contentType?.let { mType ->
            val bodyCharset = mType.charset() ?: return ""
            charset = mType.charset(UTF8)
        }
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        return buffer.readString(charset)
    }

    private fun getMockJSON(list: Array<MockJSON>?, requestJson: String, paramJson: String): MockJSON? {
        list?.forEach { jsonObject ->
            val mockRequestJson = fixJSON(jsonObject.request)
            val mockFormJson = fixJSON(jsonObject.requestForm)
            if(checkIfJsonIsEqual(requestJson, paramJson, mockRequestJson, mockFormJson))
                return jsonObject
        }
        return null
    }

    private fun checkIfJsonIsEqual(requestJson: String, paramJson: String, mockRequestJson: String, mockFormJson: String): Boolean {
        var mockRequestJsonElement: JsonObject? = null
        var requestJsonElement: JsonObject? = null
        var mockFormJsonElement: JsonArray? = null
        var formJsonElement: JsonArray? = null
        var requestEqual = mockRequestJson == requestJson
        var formEqual = mockFormJson == paramJson


        //convert Json String to JsonObject
        if(mockRequestJson.isNotEmpty())
            mockRequestJsonElement = JsonParser().parse(mockRequestJson) as JsonObject
        if(requestJson.isNotEmpty())
            requestJsonElement = JsonParser().parse(requestJson) as JsonObject
        if(mockFormJson.isNotEmpty())
            mockFormJsonElement = JsonParser().parse(mockFormJson) as JsonArray
        if(paramJson.isNotEmpty())
            formJsonElement = JsonParser().parse(paramJson) as JsonArray

        // Check if all its content is equal. Even if the order is different
        if(mockRequestJsonElement != null && requestJsonElement != null)
            requestEqual = mockRequestJsonElement == requestJsonElement

        if(mockFormJsonElement != null && formJsonElement != null)
            formEqual = mockFormJsonElement == formJsonElement

        // return object if both requestType are equals.
        if (requestEqual && formEqual)
            return true
        if ((requestJson.isNotEmpty() && requestEqual) ||
            (paramJson.isNotEmpty() && formEqual)
        ) {
            // if one request type exist and its the same as the mock, return it
            return true
        }
        return false
    }

    /**
     * This fix json that have character ' to "
     * It also Double parse it to remove whitespaces, lines, etc
     */
    private fun fixJSON(json: String?): String {
        var jsonFixed = ""
        json?.let {
            val replaced = json.replace("\'".toRegex(), "\"") // fix json ' to ""
            try {
                jsonFixed = gson.toJson(JsonParser().parse(replaced)) // fix formatting
            }
            catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }
        }
        return jsonFixed
    }

    protected open fun getJson(filepath: String): String {
        var json = ""
        try {
            val stream = context.applicationContext.assets.open(filepath)
            json = parseStream(stream)
        } catch (e: IOException) {
        }
        return json
    }

    fun parseStream(stream: InputStream): String {
        val builder = StringBuilder()
        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))

        for (line in reader.readLines()) {
            builder.append(line)
        }

        reader.close()
        return builder.toString()
    }


    private fun getParamURL(request: Request?): ArrayList<ParamData> {
        val queryList = ArrayList<ParamData>()

        request?.url()?.query()?.let { query ->
            val split = query.split("&")
            split.forEach { param ->
                val paramSplit = param.split("=")
                if (paramSplit.size == 2)
                    queryList.add(
                        ParamData(
                            paramSplit[0],
                            paramSplit[1]
                        )
                    )
            }
        }
        request?.body()?.let { body ->
            if (body is FormBody) {
                for (i in 0 until body.size())
                    queryList.add(
                        ParamData(
                            body.name(
                                i
                            ), body.value(i)
                        )
                    )
            }
            else if (body is MultipartBody) {
                addMultiPartParam(queryList, body)
            }
        }
        return queryList
    }

    /**
     *  This method add multipart request parameters to a list. So it search each item to get
     *  parameter and parameter value
     *
     *  Header Ex:
     *  [0] "Content-Disposition"
     *  [1] "form-data; name="MyFirstParam""
     *  [2] "Content-Transfer-Encoding"
     *  [3] "binary"
     *
     *  To get the parameter this method check each String item and tries to find "name=" tag
     */
    private fun addMultiPartParam(queryList: ArrayList<ParamData>, body: MultipartBody) {
        for (bodyIndex in 0 until body.size()) {
            val part = body.part(bodyIndex)
            val partBody = part.body()
            val partHeaders = part.headers()

            // this method finds the multiPart value
            val multiparkParamValue = getRequestBodyJSON(partBody)

            if(multiparkParamValue.isNotEmpty()) {
                partHeaders?.let { headers ->
                    for (headerIndex in 0 until partHeaders.size())  {
                        val value = headers.value(headerIndex)
                        val split = value.split(";")

                        for (element in split) {
                            val keyValueSplit = element.split("=")
                            if(keyValueSplit.size == 2) {
                                val key = keyValueSplit[0].trim()
                                if(key == "name") {
                                    val multiparkParamKey = keyValueSplit[1].trim().replace("\"".toRegex(), "")
                                    queryList.add(
                                        ParamData(
                                            multiparkParamKey,
                                            multiparkParamValue
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getParamJSON(list: ArrayList<ParamData>?): String {
        var json = ""

        if (!list.isNullOrEmpty()) {
            val jsonArray = JsonArray()
            list.forEach { paramData ->
                val jsonObject = JsonObject()
                jsonObject.addProperty(paramData.key, paramData.value)
                jsonArray.add(jsonObject)
            }
            json = jsonArray.toString()
        }
        return json
    }

    data class ParamData(
        @SerializedName("key") val key: String,
        @SerializedName("value") val value: String
    )

    data class MockJSON(
        @SerializedName("type") val type: String,
        @SerializedName("format") val format: String,
        @SerializedName("code") val code: Int,
        @SerializedName("request") var request: String?,
        @SerializedName("response") var response: String?,
        @SerializedName("requestForm") var requestForm: String?
    ) {
        companion object {
            const val TYPE_POST = "POST"
            const val TYPE_GET = "GET"
            const val TYPE_PUT = "PUT"
            const val TYPE_DELETE = "DELETE"

            const val FORMAT_JSON = "JSON"
        }
    }
}