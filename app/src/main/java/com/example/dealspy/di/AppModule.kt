// Update your AppModule.kt
package com.example.dealspy.di

import android.content.Context
import com.example.dealspy.BuildConfig
import com.example.dealspy.data.remote.AuthApi
import com.example.dealspy.data.remote.WatchlistApi
import com.example.dealspy.data.remote.SaveForLaterApi
import com.example.dealspy.data.remote.UserApi
import com.example.dealspy.data.repo.GeminiService
import com.example.dealspy.data.repo.SaveForLaterRepository
import com.example.dealspy.data.repo.ThemeRepository
import com.example.dealspy.data.repo.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesGeminiService(): GeminiService = GeminiService()

    @Provides
    @Singleton
    fun provideGoogleSignInClient(@ApplicationContext context: Context): GoogleSignInClient {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .requestProfile()
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, googleSignInOptions)
    }
    @Provides
    @Singleton
    fun provideSaveForLaterRepository(
        saveForLaterApi: SaveForLaterApi,
        auth: FirebaseAuth
    ): SaveForLaterRepository {
        return SaveForLaterRepository(saveForLaterApi, auth)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }



    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userApi: UserApi,
        auth: FirebaseAuth
    ): UserRepository {
        return UserRepository(userApi, auth)
    }

    @Provides
    @Singleton
    fun provideThemeRepository(@ApplicationContext context: Context): ThemeRepository =
        ThemeRepository(context)


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWatchlistApi(retrofit: Retrofit): WatchlistApi {
        return retrofit.create(WatchlistApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSaveForLaterApi(retrofit: Retrofit): SaveForLaterApi {
        return retrofit.create(SaveForLaterApi::class.java)
    }
}
