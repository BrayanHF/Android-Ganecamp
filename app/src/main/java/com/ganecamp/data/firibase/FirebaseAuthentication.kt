package com.ganecamp.data.firibase

import android.util.Log
import com.ganecamp.data.firibase.dao.FarmDao
import com.ganecamp.data.firibase.dao.GanecampUserDao
import com.ganecamp.data.firibase.model.GanecampUser
import com.ganecamp.utilities.enums.AuthRespond
import com.ganecamp.utilities.enums.FirestoreRespond
import com.ganecamp.utilities.functions.AuthErrorEvaluator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthentication @Inject constructor(
    private val auth: FirebaseAuth,
    private val ganecampUserDao: GanecampUserDao,
    private val farmDao: FarmDao,
    private val farmSessionManager: FarmSessionManager
) {

    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthRespond {
        return try {
            val firebaseUser = auth.signInWithEmailAndPassword(email, password).await()
            if (firebaseUser.user != null) {
                val userRespond = ganecampUserDao.getUserByEmail(firebaseUser.user!!.email!!)
                val user = userRespond.first ?: return AuthRespond.GETTING_USER_INFO

                val farmRespond = farmDao.getFarmByToken(user.farmToken)
                val farm = farmRespond.first ?: return AuthRespond.FARM_NOT_FOUND

                farmSessionManager.setFarm(farm)
                AuthRespond.OK
            } else {
                AuthRespond.USER_NOT_FOUND
            }
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in signInWithEmailAndPassword: ${e.message}")
            AuthErrorEvaluator.evaluateError(e)
        }
    }

    suspend fun signUpWithEmailAndPassword(
        email: String, password: String, name: String, phoneNumber: String, token: String
    ): AuthRespond {
        return try {
            val farmRespond = farmDao.getFarmByToken(token)
            val farm = farmRespond.first ?: return AuthRespond.TOKEN_NOT_FOUND

            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.email?.let { userEmail ->
                val user = GanecampUser(
                    email = userEmail,
                    name = name,
                    phoneNumber = phoneNumber,
                    farmToken = farm.token
                )
                val respond = ganecampUserDao.createUser(user)
                if (respond == FirestoreRespond.OK) {
                    farmSessionManager.setFarm(farm)
                    return AuthRespond.OK
                }
                AuthRespond.REGISTERING_USER
            } ?: AuthRespond.USER_NOT_CREATED
        } catch (e: Exception) {
            Log.e("GanecampErrors", "Error in signUpWithEmailAndPassword: ${e.message}")
            AuthErrorEvaluator.evaluateError(e)
        }
    }

    fun signOut() {
        auth.signOut()
        farmSessionManager.clearFarm()
    }

}