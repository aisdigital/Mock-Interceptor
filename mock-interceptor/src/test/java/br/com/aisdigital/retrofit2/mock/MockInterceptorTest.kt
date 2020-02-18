package br.com.aisdigital.retrofit2.mock

import android.content.Context
import br.com.aisdigital.retrofit2.mock.testutil.SynchronousExecutorService
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.io.FileInputStream
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.TimeUnit

/**
 * @author Natan Guilherme
 * @since 02/28/19
 */
class RetrofitMockTest {
    lateinit var retrofit: Retrofit

    companion object {
        const val TIMEOUT: Long = 35

        private var timberInit: Boolean = false
        fun initTimber() {
            if(!timberInit) {
                timberInit = true
                Timber.plant(UnitTestTree())
            }
        }
    }

    private fun initClient(dispatcher: Dispatcher): OkHttpClient.Builder {

        val httpClient = OkHttpClient.Builder()
        httpClient.dispatcher(dispatcher)
        httpClient.connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        httpClient.readTimeout(TIMEOUT, TimeUnit.SECONDS)
        httpClient.writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        return httpClient
    }

    private fun initRetrofit(clientBuilder: OkHttpClient.Builder): Retrofit {
        val BASE_URL = "https://api.test"
        val httpClient = clientBuilder.build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory
                    .create(
                        GsonBuilder()
                            .serializeNulls()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            .create()
                    )
            )
            .client(httpClient)
            .build()
    }

    @Before
    fun setUp() {
        initTimber()
        val initClientBuilder = initClient(Dispatcher(SynchronousExecutorService()))
        val mockInterceptor = object: MockInterceptor(mock(Context::class.java)) {
            override fun getJson(filepath: String): String {
                val file = File("src/test/resources/$filepath")
                var json = ""
                try {
                    json = parseStream(FileInputStream(file))
                }
                catch (e: Exception) {}
                return json
            }
        }
        initClientBuilder.addInterceptor(mockInterceptor)
        retrofit = initRetrofit(initClientBuilder)
    }

    @Test
    fun testRequestPostEmailResponseSuccess() {
        val retrofitTestAPI = retrofit.create(RetrofitTestAPI::class.java)
        val requestCall = retrofitTestAPI.requestPostEmailResponse(
            RetrofitRequest(
                "test@gmail.com"
            )
        )

        var isSuccessful: Boolean = false
        var failed: Boolean = false
        var code: Int = 0
        var responseBody: RetrofitResponse? = null
        requestCall.enqueue(object : Callback<RetrofitResponse> {
            override fun onResponse(call: Call<RetrofitResponse>, response: Response<RetrofitResponse>) {
                isSuccessful = response.isSuccessful
                responseBody = response.body()
                code = response.code()
            }

            override fun onFailure(call: Call<RetrofitResponse>, t: Throwable) {
                failed = true
            }
        })

        Assert.assertEquals(true, isSuccessful)
        Assert.assertEquals(false, failed)
        Assert.assertEquals(200, code)
        Assert.assertNotNull(responseBody)
        Assert.assertEquals(999, responseBody!!.code)
    }

    @Test
    fun testRequestWithPath() {
        val retrofitTestAPI = retrofit.create(RetrofitTestAPI::class.java)
        val requestCall = retrofitTestAPI.requestIDResponse(102,
            RetrofitRequest("test@gmail.com")
        )

        var isSuccessful: Boolean = false
        var failed: Boolean = false
        var code: Int = 0
        var responseBody: RetrofitResponse? = null
        requestCall.enqueue(object : Callback<RetrofitResponse> {
            override fun onResponse(call: Call<RetrofitResponse>, response: Response<RetrofitResponse>) {
                isSuccessful = response.isSuccessful
                responseBody = response.body()
                code = response.code()
            }

            override fun onFailure(call: Call<RetrofitResponse>, t: Throwable) {
                failed = true
            }
        })

        Assert.assertEquals(true, isSuccessful)
        Assert.assertEquals(false, failed)
        Assert.assertEquals(200, code)
        Assert.assertNotNull(responseBody)
        Assert.assertEquals(212, responseBody!!.code)
    }

    @Test
    fun testRequestPostEmailResponseFail() {
        val retrofitTestAPI = retrofit.create(RetrofitTestAPI::class.java)
        val requestCall = retrofitTestAPI.requestPostEmailResponse(
            RetrofitRequest(
                "testFail@gmail.com"
            )
        )

        var isSuccessful: Boolean = false
        var failed: Boolean = false
        var code: Int = 0
        var responseBody: RetrofitResponse? = null
        requestCall.enqueue(object : Callback<RetrofitResponse> {
            override fun onResponse(call: Call<RetrofitResponse>, response: Response<RetrofitResponse>) {
                isSuccessful = response.isSuccessful
                responseBody = response.body()
                code = response.code()
            }

            override fun onFailure(call: Call<RetrofitResponse>, t: Throwable) {
                failed = true
            }
        })

        Assert.assertEquals(false, isSuccessful)
        Assert.assertEquals(false, failed)
        Assert.assertEquals(400, code)
        Assert.assertNull(responseBody)
    }

    @Test
    fun testRequestPostEmailServerError() {
        val retrofitTestAPI = retrofit.create(RetrofitTestAPI::class.java)
        val requestCall = retrofitTestAPI.requestPostEmailResponse(
            RetrofitRequest(
                "testServerError@gmail.com"
            )
        )

        var isSuccessful: Boolean = false
        var failed: Boolean = false
        var code: Int = 0
        var responseBody: RetrofitResponse? = null
        requestCall.enqueue(object : Callback<RetrofitResponse> {
            override fun onResponse(call: Call<RetrofitResponse>, response: Response<RetrofitResponse>) {
                isSuccessful = response.isSuccessful
                responseBody = response.body()
                code = response.code()
            }

            override fun onFailure(call: Call<RetrofitResponse>, t: Throwable) {
                failed = true
            }
        })

        Assert.assertEquals(false, isSuccessful)
        Assert.assertEquals(false, failed)
        Assert.assertEquals(500, code)
        Assert.assertNull(responseBody)
    }

    @Test
    fun testQueryParam() {
        val retrofitTestAPI = retrofit.create(RetrofitTestAPI::class.java)
        val requestCall = retrofitTestAPI.requestQueryResponse("012.345.678-90", 10.5f)

        var isSuccessful: Boolean = false
        var failed: Boolean = false
        var code: Int = 0
        var responseBody: RetrofitResponse? = null
        requestCall.enqueue(object : Callback<RetrofitResponse> {
            override fun onResponse(call: Call<RetrofitResponse>, response: Response<RetrofitResponse>) {
                isSuccessful = response.isSuccessful
                responseBody = response.body()
                code = response.code()
            }

            override fun onFailure(call: Call<RetrofitResponse>, t: Throwable) {
                failed = true
            }
        })

        Assert.assertEquals(true, isSuccessful)
        Assert.assertEquals(false, failed)
        Assert.assertEquals(200, code)
        Assert.assertNotNull(responseBody)
        Assert.assertEquals(870, responseBody!!.code)
    }

    @Test
    fun testFieldParam() {
        val retrofitTestAPI = retrofit.create(RetrofitTestAPI::class.java)
        val requestCall = retrofitTestAPI.requestFieldResponse("012.345.678-90", 10)

        var isSuccessful: Boolean = false
        var failed: Boolean = false
        var code: Int = 0
        var responseBody: RetrofitResponse? = null
        requestCall.enqueue(object : Callback<RetrofitResponse> {
            override fun onResponse(call: Call<RetrofitResponse>, response: Response<RetrofitResponse>) {
                isSuccessful = response.isSuccessful
                responseBody = response.body()
                code = response.code()
            }

            override fun onFailure(call: Call<RetrofitResponse>, t: Throwable) {
                failed = true
            }
        })

        Assert.assertEquals(true, isSuccessful)
        Assert.assertEquals(false, failed)
        Assert.assertEquals(201, code)
        Assert.assertNotNull(responseBody)
        Assert.assertEquals(470, responseBody!!.code)
    }

    @Test
    fun testIDPathParam() {
        val retrofitTestAPI = retrofit.create(RetrofitTestAPI::class.java)
        val requestCall = retrofitTestAPI.requestIDResponse(102)

        var isSuccessful: Boolean = false
        var failed: Boolean = false
        var code: Int = 0
        var responseBody: RetrofitResponse? = null
        requestCall.enqueue(object : Callback<RetrofitResponse> {
            override fun onResponse(call: Call<RetrofitResponse>, response: Response<RetrofitResponse>) {
                isSuccessful = response.isSuccessful
                responseBody = response.body()
                code = response.code()
            }

            override fun onFailure(call: Call<RetrofitResponse>, t: Throwable) {
                failed = true
            }
        })

        Assert.assertEquals(true, isSuccessful)
        Assert.assertEquals(false, failed)
        Assert.assertEquals(200, code)
        Assert.assertNotNull(responseBody)
        Assert.assertEquals(810, responseBody!!.code)
    }

    @Test
    fun testIDPathWithFieldParam() {
        val retrofitTestAPI = retrofit.create(RetrofitTestAPI::class.java)
        val requestCall = retrofitTestAPI.requestIDResponse(102, "012.345.678-90")

        var isSuccessful: Boolean = false
        var failed: Boolean = false
        var code: Int = 0
        var responseBody: RetrofitResponse? = null
        requestCall.enqueue(object : Callback<RetrofitResponse> {
            override fun onResponse(call: Call<RetrofitResponse>, response: Response<RetrofitResponse>) {
                isSuccessful = response.isSuccessful
                responseBody = response.body()
                code = response.code()
            }

            override fun onFailure(call: Call<RetrofitResponse>, t: Throwable) {
                failed = true
            }
        })

        Assert.assertEquals(true, isSuccessful)
        Assert.assertEquals(false, failed)
        Assert.assertEquals(200, code)
        Assert.assertNotNull(responseBody)
        Assert.assertEquals(210, responseBody!!.code)
    }

    @Test
    fun testIDPathNotFound() {
        val retrofitTestAPI = retrofit.create(RetrofitTestAPI::class.java)
        val requestCall = retrofitTestAPI.requestIDResponse(402)

        var isSuccessful: Boolean = false
        var failed: Boolean = false
        var code: Int = 0
        var responseBody: RetrofitResponse? = null
        requestCall.enqueue(object : Callback<RetrofitResponse> {
            override fun onResponse(call: Call<RetrofitResponse>, response: Response<RetrofitResponse>) {
                isSuccessful = response.isSuccessful
                responseBody = response.body()
                code = response.code()
            }

            override fun onFailure(call: Call<RetrofitResponse>, t: Throwable) {
                failed = true
            }
        })

        Assert.assertEquals(false, isSuccessful)
        Assert.assertEquals(false, failed)
        Assert.assertEquals(MockInterceptor.DEFAULT_ERROR, code)
        Assert.assertNull(responseBody)
    }

    @Test
    fun testMultiPartOneParam() {
        val contentType = "multipart/form-data"
        val resource = ClassLoader.getSystemResource("testImage.png").toURI()
        val file = File(resource)

        val retrofitTestAPI = retrofit.create(RetrofitTestAPI::class.java)


        val formData = MultipartBody.Part.createFormData(
            "file",
            file.absolutePath,
            RequestBody.create(MediaType.parse("multipart/form-data"), file.absoluteFile)
        )

        val caregoryBody = RequestBody.create(MediaType.parse(contentType), "MyFirstParamValue")

        val requestCall = retrofitTestAPI.requestMultipart(formData, caregoryBody)

        var isSuccessful: Boolean = false
        var failed: Boolean = false
        var code: Int = 0
        var responseBody: RetrofitResponse? = null
        requestCall.enqueue(object : Callback<RetrofitResponse> {
            override fun onResponse(call: Call<RetrofitResponse>, response: Response<RetrofitResponse>) {
                isSuccessful = response.isSuccessful
                responseBody = response.body()
                code = response.code()
            }

            override fun onFailure(call: Call<RetrofitResponse>, t: Throwable) {
                failed = true
            }
        })

        Assert.assertEquals(true, isSuccessful)
        Assert.assertEquals(false, failed)
        Assert.assertEquals(200, code)
        Assert.assertNotNull(responseBody)
        Assert.assertEquals(170, responseBody!!.code)
    }

    @Test
    fun testMultiPartTwoParam() {
        val contentType = "multipart/form-data"
        val resource = ClassLoader.getSystemResource("testImage.png").toURI()
        val file = File(resource)

        val retrofitTestAPI = retrofit.create(RetrofitTestAPI::class.java)


        val formData = MultipartBody.Part.createFormData(
            "file",
            file.absolutePath,
            RequestBody.create(MediaType.parse("multipart/form-data"), file.absoluteFile)
        )

        val param1 = RequestBody.create(MediaType.parse(contentType), "MyFirstParamValue")
        val param2 = RequestBody.create(MediaType.parse(contentType), "MySecondParamValue")

        val requestCall = retrofitTestAPI.requestMultipart(formData, param1, param2)

        var isSuccessful: Boolean = false
        var failed: Boolean = false
        var code: Int = 0
        var responseBody: RetrofitResponse? = null
        requestCall.enqueue(object : Callback<RetrofitResponse> {
            override fun onResponse(call: Call<RetrofitResponse>, response: Response<RetrofitResponse>) {
                isSuccessful = response.isSuccessful
                responseBody = response.body()
                code = response.code()
            }

            override fun onFailure(call: Call<RetrofitResponse>, t: Throwable) {
                failed = true
            }
        })

        Assert.assertEquals(true, isSuccessful)
        Assert.assertEquals(false, failed)
        Assert.assertEquals(201, code)
        Assert.assertNotNull(responseBody)
        Assert.assertEquals(112, responseBody!!.code)
    }

}

interface RetrofitTestAPI {
    @POST("retrofit_requestTest")
    fun requestPostEmailResponse(@Body request: RetrofitRequest): Call<RetrofitResponse>

    @GET("retrofit/pathTest/{id}/otherpath")
    fun requestIDResponse(@Path("id") id: Int): Call<RetrofitResponse>

    @GET("retrofit/pathTest/{id}/otherpath")
    fun requestIDResponse(@Path("id") id: Int, @Query("cpf") cpf: String): Call<RetrofitResponse>

    @POST("retrofit/pathTest/{id}/otherpath")
    fun requestIDResponse(@Path("id") id: Int, @Body request: RetrofitRequest): Call<RetrofitResponse>

    @FormUrlEncoded
    @POST("retrofit/fieldTest")
    fun requestFieldResponse(@Field("cpf") cpf: String, @Field("myMap") map: Int): Call<RetrofitResponse>

    @GET("retrofit/queryTest")
    fun requestQueryResponse(@Query("cpf") cpf: String, @Query("myFloat") myFloat: Float): Call<RetrofitResponse>

    @Multipart
    @POST("retrofit/multipart/files")
    fun requestMultipart(@Part file: MultipartBody.Part, @Part("MyFirstParam") category: RequestBody): Call<RetrofitResponse>

    @Multipart
    @POST("retrofit/multipart/files")
    fun requestMultipart(@Part file: MultipartBody.Part,
                         @Part("MyFirstParam") myParam: RequestBody,
                         @Part("MySecondParam") secondParam: RequestBody): Call<RetrofitResponse>
}

data class RetrofitRequest(
    @SerializedName("email") val email: String
)

data class RetrofitResponse(
    @SerializedName("message") val message: String,
    @SerializedName("code") val code: Int
)

class UnitTestTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        System.out.println("$tag: $message")
    }
}