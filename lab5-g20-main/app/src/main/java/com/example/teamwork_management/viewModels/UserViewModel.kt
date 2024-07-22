package com.example.teamwork_management.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PropertyName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


data class UserInformations(
    @get:PropertyName("about")
    @set:PropertyName("about")
    var about: String = "",

    @get:PropertyName("city")
    @set:PropertyName("city")
    var city: String = "",

    @get:PropertyName("email")
    @set:PropertyName("email")
    var email: String = "",

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String = "",

    @get:PropertyName("profilePicture")
    @set:PropertyName("profilePicture")
    var profilePicture: String = "",

    @get:PropertyName("role")
    @set:PropertyName("role")
    var role: String = "",

    @get:PropertyName("surname")
    @set:PropertyName("surname")
    var surname: String = "",

    @get:PropertyName("userId")
    @set:PropertyName("userId")
    var userId: String = "",

    @get:PropertyName("userProjects")
    @set:PropertyName("userProjects")
    var userProjects: Int = 0,

    @get:PropertyName("userTasks")
    @set:PropertyName("userTasks")
    var userTasks: Int = 0
) {
    // Required no-argument constructor for Firestore deserialization
    constructor() : this("", "", "", "", "", "", "", "", 0, 0)
}

object  UserViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    //Google signed-in user
    private val _user = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    // Information retrieved from database
    private val _userInformation = MutableStateFlow<UserInformations?>(null)
    val userInformation: StateFlow<UserInformations?> = _userInformation

    init {
        auth.addAuthStateListener { auth ->
            _user.value = auth.currentUser
            auth.currentUser?.let { currentUser ->
                // Fetch user data from Firestore based on userId
                fetchUserData(currentUser.uid)
            }
        }
    }

    private fun fetchUserData(userId: String) {
        viewModelScope.launch {
            try {
                // Query Firestore for documents where userId field equals userId variable
                val querySnapshot = firestore.collection("users")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                // Check if query returned any documents
                if (!querySnapshot.isEmpty) {
                    // There should be exactly one document since userId is unique
                    val userDoc = querySnapshot.documents[0]
                    // Map Firestore document to UserInformations object
                    val u = userDoc.toObject(UserInformations::class.java)
                    _userInformation.value = u
                } else {
                    // User data not found, create a new UserInformations object with default values
                    val newUserInformation = UserInformations(
                        "", "", user.value?.email ?: "", user.value?.displayName ?: "", /*user.value?.photoUrl.toString() ?: */"", "", "", userId, 0, 0
                    )

                    // Add the new document to Firestore
                    firestore.collection("users")
                        .add(newUserInformation)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                            _userInformation.value = newUserInformation
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error adding document", e)
                            _userInformation.value = null
                        }
                }

            } catch (e: Exception) {
                // Handle exceptions (e.g., Firestore exceptions)
                Log.e("UserExcp", e.message!!)
                _userInformation.value = null
            }
        }
    }

    fun saveUserData(userInfo: UserInformations) {
        viewModelScope.launch {
            try {
                // Update Firestore document with new user information
                val userDoc = firestore.collection("users")
                    .whereEqualTo("userId", userInfo.userId)
                    .get()
                    .await()
                    .documents
                    .firstOrNull()

                if (userDoc != null) {
                    firestore.collection("users")
                        .document(userDoc.id)
                        .set(userInfo)
                        .addOnSuccessListener {
                            Log.d(TAG, "User information updated successfully")
                            _userInformation.value = userInfo
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error updating user information", e)
                        }
                } else {
                    Log.e(TAG, "User document not found")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error saving user information", e)
            }
        }
    }
}