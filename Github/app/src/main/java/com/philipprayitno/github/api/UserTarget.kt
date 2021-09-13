package com.philipprayitno.github.api

import com.philipprayitno.github.model.UserResponseWrapper
import com.philipprayitno.github.model.UserQueryResponseWrapper
import com.philipprayitno.github.model.UserDetailResponseWrapper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserTarget {
    @GET("search/users")
    @Headers("Authorization: token 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")
    suspend fun getUsersByQuery(@Query("q")username: String): UserQueryResponseWrapper

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    suspend fun getUserDetailByUsername(@Path("username")username: String): UserDetailResponseWrapper

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    suspend fun getUserFollowerList(@Path("username")username: String): ArrayList<UserResponseWrapper>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    suspend fun getUserFollowingList(@Path("username")username: String): ArrayList<UserResponseWrapper>
}

object RetrofitUserClient {
    private const val BASE_URL = "https://api.github.com/"

    val instance: UserTarget by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(UserTarget::class.java)
    }
}