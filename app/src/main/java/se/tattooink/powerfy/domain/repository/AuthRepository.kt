package se.tattooink.powerfy.domain.repository

import se.tattooink.powerfy.domain.model.User

interface AuthRepository {
    suspend fun signUp(name: String, email: String, password: String): Result<User>
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signInAnonymously(): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun signOut()
    fun getCurrentUser(): User?
}