package com.kaanozen.virtualmarket.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.kaanozen.virtualmarket.R
import com.kaanozen.virtualmarket.activity.firestore.FirestoreClass
import com.kaanozen.virtualmarket.activity.model.User
import com.kaanozen.virtualmarket.activity.utilies.Constants
import kotlinx.android.synthetic.main.activity_user_profile.*


class UserProfileActivity : BaseActivity(),View.OnClickListener{


    //This function is auto created by Android when the Activity Class is created.

    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_user_profile)

        // Create a instance of the User model class.
        var userDetails: User = User()
        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra.
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        // Here, the some of the edit text components are disabled because it is added at a time of Registration.
        editFirstName.isEnabled = false
        editFirstName.setText(userDetails.firstName)

        editLastName.isEnabled = false
        editLastName.setText(userDetails.lastName)

        editEmail.isEnabled = false
        editEmail.setText(userDetails.email)

        saveBut.setOnClickListener(this)
        home_bottom_image_view.setOnClickListener(this)
        user_bottom_logo_image_view.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.saveBut ->{

                    if(validateUserProfileDetails()){

                        val userHashMap = HashMap<String, Any>()

                        val mobileNum = editPhoneNumber.text.toString().trim { it <= ' ' }

                        if (mobileNum.isNotEmpty()){
                            userHashMap[Constants.MOBILE] = mobileNum.toLong()

                        }

                        /*showErrorSnackBar("Your details are valid. You can update them.", false)*/

                        // call the registerUser function of FireStore class to make an entry in the database.
                        FirestoreClass().updateUserProfileData(
                                this@UserProfileActivity,
                                userHashMap
                        )


                    }
                }

                R.id.home_bottom_image_view ->{
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.user_bottom_logo_image_view ->{
                    val intent = Intent(this,UserProfileActivity::class.java)
                    startActivity(intent)
                }
            }

        }
    }

     //A function to validate the input entries for profile details.

    private fun validateUserProfileDetails(): Boolean {
        return when {
            // The FirstName, LastName, and Email Id are not editable when they come from the login screen.

            // Check if the mobile number is not empty as it is mandatory to enter.
            TextUtils.isEmpty(editPhoneNumber.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }


}