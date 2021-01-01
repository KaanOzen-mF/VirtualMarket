package com.kaanozen.virtualmarket.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.User
import com.kaanozen.virtualmarket.activity.utilies.Constants
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Click event assigned to Login button.
        loginBut.setOnClickListener(this)
        // Click event assigned to Register text.
        registerBut_login.setOnClickListener(this)


    }

    override fun onClick(view: View) {
        if (view != null){
            when(view.id){

                R.id.loginBut -> {
                    loginRegisteredUser()
                }

                R.id.registerBut_login ->{
                    // with this intent if we click login button we will open Login Activity
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    //this function check our user email and password for empty string
    //if user do not write anything that part, user see error message
    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(emailText.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_email), true)
                false
            }
            TextUtils.isEmpty(passwordText.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun loginRegisteredUser() {

        if (validateLoginDetails()) {

            // Get the text from editText and trim the space
            val email = emailText.text.toString().trim { it <= ' ' }
            val password = passwordText.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                     if (task.isSuccessful) {

                         FirestoreClass().getUserDetails(this@LoginActivity)

                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userLoggedInSuccess(user : User){

        //Print the user details in the log as of now
        Log.i("First Name: " , user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: " , user.email)

        // If the user profile is incomplete then launch the UserProfileActivity.
        val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
        //Pass the user details to the user profile screen.

        intent.putExtra(Constants.EXTRA_USER_DETAILS, user)

        startActivity(intent)
        finish()
    }
}

