package se.tattooink.powerfy.di

import se.tattooink.powerfy.BuildConfig
import com.stripe.android.PaymentConfiguration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.Context

@Module
@InstallIn(SingletonComponent::class)
object StripeModule {

    @Provides
    @Singleton
    fun providePaymentConfiguration(@ApplicationContext context: Context): PaymentConfiguration {
        PaymentConfiguration.init(context, BuildConfig.STRIPE_PUBLISHABLE_KEY)
        return PaymentConfiguration.getInstance(context)
    }
}
