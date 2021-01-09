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
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerBut_register.setOnClickListener{
            registerUser()
        }
    }

    fun loginBut_registerClick(view : View) {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * A function to validate the entries of a new user.
     */
    private fun validateRegisterDetails(): Boolean {

        if(firstNameTxt.text.isEmpty())
        {
            showErrorSnackBar(resources.getString(R.string.err_msg_first_name), true)
            return false
        }

        if(lastNameTxt.text.isEmpty())
        {
            showErrorSnackBar(resources.getString(R.string.err_msg_last_name), true)
            return false
        }

        if(emailTxt.text.isEmpty())
        {
            showErrorSnackBar(resources.getString(R.string.err_msg_email), true)
            return false
        }

        if(passwordTxt.text.isEmpty())
        {
            showErrorSnackBar(resources.getString(R.string.err_msg_password), true)
            return false
        }

        if(confirmPasswordTxt.text.isEmpty())
        {
            showErrorSnackBar(resources.getString(R.string.err_msg_confirm_password),true)
            return false
        }

        return true
    }
    /**
     * A function to register the user with email and password using FirebaseAuth.
     */
    private fun registerUser() {

        // Check with validate function if the entries are valid or not.
        if (validateRegisterDetails()) {

            val email: String = emailTxt.text.toString().trim { it <= ' ' }
            val password: String = passwordTxt.text.toString().trim { it <= ' ' }

            // Create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = com.kaanozen.virtualmarket.activity.model.User(
                                firebaseUser.uid,
                                firstNameTxt.text.toString().trim{ it <= ' '},
                                lastNameTxt.text.toString().trim { it <= ' ' },
                                emailTxt.text.toString().trim { it <= ' ' }

                            )

                            //we store user register info to firestore database
                            FirestoreClass().registerUser(this@RegisterActivity, user)
                        } else {
                            // If the registering is not successful then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }

    fun userRegistrationSuccess(){
        Toast.makeText(this@RegisterActivity,resources.getString(R.string.register_success),Toast.LENGTH_LONG).show()
    }

}