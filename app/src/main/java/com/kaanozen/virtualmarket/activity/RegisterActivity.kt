package com.kaanozen.virtualmarket.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.User
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Set register buttons onClick method
        registerBut_register.setOnClickListener{
            registerUser()
        }
    }

    //When login is pressed, go the login screen
    fun loginBut_registerClick(view : View) {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    //Function to control user input values
    private fun validateRegisterDetails(): Boolean {

        if(firstNameTxt.text.isEmpty()) {
            showErrorSnackBar(resources.getString(R.string.err_msg_first_name), true)
            return false
        }

        if(lastNameTxt.text.isEmpty()) {
            showErrorSnackBar(resources.getString(R.string.err_msg_last_name), true)
            return false
        }

        if(emailTxt.text.isEmpty()) {
            showErrorSnackBar(resources.getString(R.string.err_msg_email), true)
            return false
        }

        if(passwordTxt.text.isEmpty()) {
            showErrorSnackBar(resources.getString(R.string.err_msg_password), true)
            return false
        }

        if(confirmPasswordTxt.text.isEmpty()) {
            showErrorSnackBar(resources.getString(R.string.err_msg_confirm_password),true)
            return false
        }

        return true
    }

    //Function to register user
    private fun registerUser() {

        // Check  if the entries are valid or not.
        if (validateRegisterDetails()) {

            //Get input values
            val email: String = emailTxt.text.toString().trim { it <= ' ' }
            val password: String = passwordTxt.text.toString().trim { it <= ' ' }

            // Create an authentication on the database with email and password in FireBase Authentication System
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task -> //If task is completed

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            //Create a user to store in user table
                            val user = User(
                                firebaseUser.uid,
                                firstNameTxt.text.toString().trim{ it <= ' '},
                                lastNameTxt.text.toString().trim { it <= ' ' },
                                emailTxt.text.toString().trim { it <= ' ' }
                            )

                            //we store user info to firestore database
                            FirestoreClass().registerUser(this@RegisterActivity, user)
                        } else {
                            // If the registering is not successful then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }
}