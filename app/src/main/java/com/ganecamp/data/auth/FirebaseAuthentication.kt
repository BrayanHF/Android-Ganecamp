package com.ganecamp.data.auth

import com.ganecamp.data.firestore.FarmDataSource
import com.ganecamp.data.firestore.GanecampUserDataSource
import com.ganecamp.data.model.FirestoreGanecampUser
import com.ganecamp.data.model.toFarm
import com.ganecamp.data.result.DataResult
import com.ganecamp.data.result.DataResult.Error
import com.ganecamp.data.result.DataResult.Success
import com.ganecamp.domain.session.FarmSessionManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthentication @Inject constructor(
    private val auth: FirebaseAuth,
    private val ganecampUserDao: GanecampUserDataSource,
    private val farmDao: FarmDataSource,
    private val farmSessionManager: FarmSessionManager
) {

    suspend fun signInWithEmailAndPassword(email: String, password: String): DataResult<Unit> {
        return try {
            val firebaseUser = auth.signInWithEmailAndPassword(email, password).await()
            val userRespond = ganecampUserDao.getUserByEmail(firebaseUser.user!!.email!!)
            val user = when (userRespond) {
                is Success -> userRespond.data
                is Error -> return Error(userRespond.exception)
            }

            val farmRespond = farmDao.getFarmByToken(user.farmToken)
            val farm = when (farmRespond) {
                is Success -> farmRespond.data
                is Error -> return Error(farmRespond.exception)
            }
            farmSessionManager.setFarm(farm.toFarm())
            Success(Unit)
        } catch (e: Exception) {
            Error(e)
        }
    }

    suspend fun signUpWithEmailAndPassword(
        email: String, password: String, name: String, phoneNumber: String, token: String
    ): DataResult<Unit> {
        return try {

            val farm = when (val farmRespond = farmDao.getFarmByToken(token)) {
                is Success -> farmRespond.data
                is Error -> return Error(farmRespond.exception)
            }

            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user = FirestoreGanecampUser(
                email = authResult.user?.email!!,
                name = name,
                phoneNumber = phoneNumber,
                farmToken = farm.token
            )

            when (val respond = ganecampUserDao.addUser(user)) {
                is Success -> Success(Unit)
                is Error -> Error(respond.exception)
            }
        } catch (e: Exception) {
            Error(e)
        }
    }

    fun signOut() {
        auth.signOut()
        farmSessionManager.clearFarm()
    }

}