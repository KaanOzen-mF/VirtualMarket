package com.kaanozen.virtualmarket

import android.os.Bundle
import android.text.TextUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        loginBut_register.setOnClickListener {

            onBackPressed()

        // with this intent if we click login button we will open Login Activity
            /*
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
             */
        }

        registerBut.setOnClickListener{
            registerUser()
        }
    }

    /**
     * A function to validate the entries of a new user.
     */
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(firstNameTxt.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_first_name), true)
                false
            }

            TextUtils.isEmpty(lastNameTxt.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_last_name), true)
                false
            }

            TextUtils.isEmpty(emailTxt.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_email), true)
                false
            }

            TextUtils.isEmpty(passwordTxt.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password), true)
                false
            }

            TextUtils.isEmpty(confirmPasswordTxt.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                        resources.getString(R.string.err_msg_confirm_password),
                        true
                )
                false
            }

            passwordTxt.text.toString().trim { it <= ' ' } != confirmPasswordTxt.text.toString()
                    .trim { it <= ' ' } -> {
                showErrorSnackBar(
                        resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),
                        true
                )
                false
            }
            else -> {
                true
            }
        }
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

                                    showErrorSnackBar(
                                            "Welcome "+ firstNameTxt.text.toString() + "\nYou are registered successfully. Your user id is ${firebaseUser.uid}" ,
                                            false
                                    )

                                    //with these lines we turn back login activity for login app
                                    FirebaseAuth.getInstance().signOut()
                                    finish()
                                } else {
                                    // If the registering is not successful then show error message.
                                    showErrorSnackBar(task.exception!!.message.toString(), true)
                                }
                            })
        }
    }

}