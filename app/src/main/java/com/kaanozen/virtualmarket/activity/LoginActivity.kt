package com.kaanozen.virtualmarket.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.User
import com.kaanozen.virtualmarket.activity.utilies.Constants
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginButClick(view:View) {
        loginRegisteredUser()
    }

    fun registerButClick(view:View) {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

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

    private fun loginRegisteredUser() {

        if (validateLoginDetails()) {

            val email = emailText.text.toString().trim { it <= ' ' }
            val password = passwordText.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        FirestoreClass().getUserDetails(this@LoginActivity)
                    }
                    else
                    {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userLoggedInSuccess(user : User){
        val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
        intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
        startActivity(intent)
        finish()
    }
}

