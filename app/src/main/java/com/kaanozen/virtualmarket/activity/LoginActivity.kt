package com.kaanozen.virtualmarket.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.User
import com.kaanozen.virtualmarket.activity.utilies.Constants
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    //Login button method for logging in

    fun loginButClick(view:View) {
        loginRegisteredUser()
    }

    //Register button method for opening registration page

    fun registerButClick(view:View) {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    //Function to check if inputs are valid

    private fun validateLoginDetails(): Boolean {
        if(emailText.text.isEmpty())
        {
            showErrorSnackBar(resources.getString(R.string.err_msg_email), true)
            return false
        }
        if(passwordText.text.isEmpty())
        {
            showErrorSnackBar(resources.getString(R.string.err_msg_password), true)
            return false
        }
        return true
    }

    //Login function

    private fun loginRegisteredUser() {

        if (validateLoginDetails()) { //If inputs are valid

            //Take information of the user

            val email = emailText.text.toString().trim { it <= ' ' }
            val password = passwordText.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task -> //If successful
                    if (task.isSuccessful)
                    {
                        FirestoreClass().getUserDetails(this@LoginActivity) //Get user details and open profile activity
                    }
                    else
                    {
                        showErrorSnackBar(task.exception!!.message.toString(), true) //Give error
                    }
                }
        }
    }
}

