package se.tattooink.powerfy.ui.components

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import se.tattooink.powerfy.BuildConfig

suspend fun requestGoogleIdToken(context: Context): Result<String> {
    return try {
        val option = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()

        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
        Result.success(googleIdTokenCredential.idToken)
    } catch (e: GetCredentialException) {
        Result.failure(e)
    }
}