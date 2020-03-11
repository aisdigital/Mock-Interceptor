package br.com.aisdigital.retrofit2.mock.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitTestAPI {
    @POST("retrofit/requestTest")
    fun requestPostEmailResponse(@Body request: RequestModel): Call<ResponseModel>

    @GET("retrofit/pathTest/{id}/otherpath")
    fun requestIDResponse(@Path("id") id: Int): Call<ResponseModel>

    @GET("retrofit/pathTest/{id}/otherpath")
    fun requestIDResponse(@Path("id") id: Int, @Query("cpf") cpf: String): Call<ResponseModel>

    @POST("retrofit/pathTest/{id}/otherpath")
    fun requestIDResponse(@Path("id") id: Int, @Body request: RequestModel): Call<ResponseModel>

    @FormUrlEncoded
    @POST("retrofit/fieldTest")
    fun requestFieldResponse(@Field("cpf") cpf: String, @Field("myMap") map: Int): Call<ResponseModel>

    @GET("retrofit/queryTest")
    fun requestQueryResponse(@Query("cpf") cpf: String, @Query("myFloat") myFloat: Float): Call<ResponseModel>

    @Multipart
    @POST("retrofit/multipart/files")
    fun requestMultipart(@Part file: MultipartBody.Part, @Part("MyFirstParam") category: RequestBody): Call<ResponseModel>

    @Multipart
    @POST("retrofit/multipart/files")
    fun requestMultipart(@Part file: MultipartBody.Part,
                         @Part("MyFirstParam") myParam: RequestBody,
                         @Part("MySecondParam") secondParam: RequestBody
    ): Call<ResponseModel>
}