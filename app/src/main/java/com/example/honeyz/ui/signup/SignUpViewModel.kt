package com.example.honeyz.ui.signup

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // States to manage UI behavior
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var successMessage = mutableStateOf<String?>(null)

    // Total number of users, admins, and customers
    var totalUsers: MutableState<Int> = mutableStateOf(0)
        private set
    var totalAdmins: MutableState<Int> = mutableStateOf(0)
        private set
    var totalCustomers: MutableState<Int> = mutableStateOf(0)
        private set

    // Function to sign up a new user
    fun signUp(email: String, password: String, onSignUpResult: (Boolean, String?) -> Unit) {
        isLoading.value = true
        errorMessage.value = null
        successMessage.value = null

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign-up success, create user document and cart
                        val userId = auth.currentUser?.uid
                        val role = if (email.endsWith("@admin.com")) "admin" else "customer"
                        if (userId != null) {
                            createUserInFirestore(userId, email, role, onSignUpResult)
                        }
                    } else {
                        // Sign-up failed
                        errorMessage.value = task.exception?.message
                        isLoading.value = false
                        onSignUpResult(false, task.exception?.message)
                    }
                }
        }
    }

    // Create a user document and an empty cart in Firestore
    private fun createUserInFirestore(userId: String, email: String, role: String, onSignUpResult: (Boolean, String?) -> Unit) {
        val userDocument = db.collection("users").document(userId)
        val user = mapOf(
            "email" to email,
            "role" to role,
        )

        // Create user document
        userDocument.set(user)
            .addOnSuccessListener {
                // Once user is created, create the user's cart
                createCartForUser(userId, onSignUpResult)
            }
            .addOnFailureListener { e ->
                // Failed to create user document
                errorMessage.value = e.message
                isLoading.value = false
                onSignUpResult(false, e.message)
            }
    }

    // Create an empty cart for the user in Firestore
    private fun createCartForUser(userId: String, onSignUpResult: (Boolean, String?) -> Unit) {
        val cartDocument = db.collection("users").document(userId).collection("cart").document()

        // Create an empty cart structure
        val cart = mapOf(
            "cart_id" to cartDocument.id,
            "items" to emptyList<Map<String, Any>>() // Start with an empty list of items
        )

        // Set the cart document
        cartDocument.set(cart)
            .addOnSuccessListener {
                // Cart created successfully
                successMessage.value = "Signup successful, cart created"
                isLoading.value = false
                onSignUpResult(true, null)
            }
            .addOnFailureListener { e ->
                // Failed to create cart
                errorMessage.value = e.message
                isLoading.value = false
                onSignUpResult(false, e.message)
            }
    }
}
