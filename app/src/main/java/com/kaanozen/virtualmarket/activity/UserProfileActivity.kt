package com.kaanozen.virtualmarket.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.User
import com.kaanozen.virtualmarket.activity.utilies.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.home_bottom_image_view
import kotlinx.android.synthetic.main.activity_user_profile.shopping_cart_image_view
import kotlinx.android.synthetic.main.activity_user_profile.user_bottom_logo_image_view

class UserProfileActivity : BaseActivity(),View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        var userDetails: User = User() //Create a user

        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)) { //Get user information
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        //Set view values to user information
        editFirstName.setText(userDetails.firstName)
        editLastName.setText(userDetails.lastName)
        editPhoneNumber.setText(userDetails.mobile.toString())
        editEmail.isEnabled = false
        editEmail.setText(userDetails.email)

        //Set event listeners of the bottom application navigation views
        //Set event listeners of buttons
        saveBut.setOnClickListener(this)
        home_bottom_image_view.setOnClickListener(this)
        user_bottom_logo_image_view.setOnClickListener(this)
        shopping_cart_image_view.setOnClickListener(this)
        logoutBut.setOnClickListener(this)
    }

    //When something is clicked
    override fun onClick(view: View?) {

        if (view != null) {
            //If navigation bar views are clicked call BaseActivity navigation bar handler
            if((view.id == home_bottom_image_view.id) || (view.id == user_bottom_logo_image_view.id) || (view.id == shopping_cart_image_view.id)) {
                super.onClick(view)
                return
            }
            when (view.id) {
                R.id.saveBut ->{ //If save button is clicked update user information
                    if(validateUserProfileDetails()){

                        val userHashMap = HashMap<String, Any>() //Create HashMap to update user information

                        //Fill the hash map
                        val mobileNum = editPhoneNumber.text.toString().trim { it <= ' ' }
                        val firstName : String = editFirstName.text.toString().trim { it <= ' ' }
                        val lastName : String = editLastName.text.toString().trim { it <= ' ' }

                        if (mobileNum.isNotEmpty()){
                            userHashMap["mobile"] = mobileNum.toLong()
                        }

                        if(firstName.isNotEmpty()) {
                            userHashMap["firstName"] = firstName
                        }

                        if(lastName.isNotEmpty()) {
                            userHashMap["lastName"] = lastName
                        }

                        //Update the user information using the hash map
                        FirestoreClass().updateUserProfileData(
                            this@UserProfileActivity,
                            userHashMap
                        )
                    }
                }
                R.id.logoutBut ->{ //Logout Button is clicked, logout and go to login screen
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    //Function to check input values
    private fun validateUserProfileDetails(): Boolean {

        return when {
            TextUtils.isEmpty(editFirstName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("İsim Kısmı Boş Kalamaz", true)
                false
            }
            TextUtils.isEmpty(editLastName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Soyisim Kısmı Boş Kalamaz", true)
                false
            }
            TextUtils.isEmpty(editPhoneNumber.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Telefon Numarası Kısmı Boş Kalamaz", true)
                false
            }
            else -> {
                true
            }
        }
    }
}