package com.example.honeyz.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

enum class UserRole {
    ADMIN, CUSTOMER, NONE
}

data class User(
    val uid: String = "",
    val email: String = "",
    val role: String = ""
)

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Expose the current user to the UI
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    // Alternatively, expose just the email
    val email: MutableState<String?> = mutableStateOf(auth.currentUser?.email)

    // User role state
    var userRole: MutableState<UserRole> = mutableStateOf(UserRole.NONE)
        private set

    // States for managing UI behavior during login
    var isLoading: MutableState<Boolean> = mutableStateOf(false)
        private set

    var errorMessage: MutableState<String?> = mutableStateOf(null)
        private set

//    // Total number of users, admins, and customers
//    var totalUsers: MutableState<Int> = mutableStateOf(0)
//        private set
//    var totalAdmins: MutableState<Int> = mutableStateOf(0)
//        private set
//    var totalCustomers: MutableState<Int> = mutableStateOf(0)
//        private set

    // Fetch all users from Firestore and categorize them into admins and customers
//    fun fetchAllUsers() {
//        isLoading.value = true
//        viewModelScope.launch {
//            db.collection("users").get()
//                .addOnSuccessListener { result ->
//                    val users = result.map { document ->
//                        val user = document.toObject(User::class.java)
//                        user
//                    }
//
//                    // Count admins and customers
//                    totalAdmins.value = users.count { it.role == "admin" }
//                    totalCustomers.value = users.count { it.role == "customer" }
//                    totalUsers.value = users.size
//
//                    Log.d("Firestore", "Fetched ${users.size} users. Admins: ${totalAdmins.value}, Customers: ${totalCustomers.value}")
//                    isLoading.value = false
//                }
//                .addOnFailureListener { e ->
//                    Log.e("Firestore", "Error fetching users: ${e.message}")
//                    errorMessage.value = "Error fetching users: ${e.message}"
//                    isLoading.value = false
//                }
//        }
//    }

    fun isUserLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    // Login function
    fun login(email: String, password: String, onLoginResult: (Boolean, String?) -> Unit) {
        // Set loading state
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign-in success, check the user role
                        val user = auth.currentUser
                        user?.let {
                            this@LoginViewModel.email.value = it.email // Store the email
                        }
                        checkUserRole(user, onLoginResult)
                    } else {
                        // Handle login failure
                        errorMessage.value = getFriendlyErrorMessage(task.exception?.message)
                        isLoading.value = false
                        onLoginResult(false, errorMessage.value)
                    }
                }
        }
    }

    // Check the user role (Admin or Customer)
    private fun checkUserRole(user: FirebaseUser?, onLoginResult: (Boolean, String?) -> Unit) {
        user?.let {
            val email = it.email ?: ""
            when {
                email.endsWith("@admin.com") -> {
                    // Assign admin role
                    userRole.value = UserRole.ADMIN
                    onLoginResult(true, null)
                }
                else -> {
                    // Assign customer role
                    userRole.value = UserRole.CUSTOMER
                    onLoginResult(true, null)
                }
            }
        } ?: onLoginResult(false, "User is null")

        // Reset loading state
        isLoading.value = false
    }

    // Helper method to provide more user-friendly error messages
    private fun getFriendlyErrorMessage(firebaseError: String?): String {
        return when (firebaseError) {
            "There is no user record corresponding to this identifier." -> "User not found."
            "The password is invalid or the user does not have a password." -> "Incorrect password."
            "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> "Network error. Please try again."
            else -> "Authentication failed. Please try again."
        }
    }

    // Logout function
    fun logout() {
        auth.signOut()
        email.value = null
        userRole.value = UserRole.NONE
    }

    // Function to delete the user's account
    fun deleteAccount(onDeleteResult: (Boolean, String?) -> Unit) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            user.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Account deleted successfully
                    userRole.value = UserRole.NONE
                    onDeleteResult(true, null)
                } else {
                    // Account deletion failed
                    onDeleteResult(false, task.exception?.message)
                }
            }
        } ?: onDeleteResult(false, "No user is currently signed in")
    }
}
