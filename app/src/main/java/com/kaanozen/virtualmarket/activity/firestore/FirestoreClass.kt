package com.kaanozen.virtualmarket.activity.firestore

import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.kaanozen.virtualmarket.activity.LoginActivity
import com.kaanozen.virtualmarket.activity.OrderListActivity
import com.kaanozen.virtualmarket.activity.RegisterActivity
import com.kaanozen.virtualmarket.activity.model.*
import com.kaanozen.virtualmarket.activity.recycle.OrderRecycleAdapter
import com.kaanozen.virtualmarket.activity.utilies.Constants
import kotlinx.coroutines.delay
import java.lang.Thread.sleep

//A custom class where we will add the operation performed for the FireStore database.

open class FirestoreClass {

    // Access a Cloud Firestore instance.
    private val mFireStore = FirebaseFirestore.getInstance()

    private val storage = Firebase.storage

     //A function to make an entry of the registered user in the FireStore database.

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        mFireStore.collection("users")
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.id)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

     // A function to get the user id of current logged user.

    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    // A function to get the logged user details from from FireStore Database.

    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!

                //Create an instance of the Android SharedPreferences.
                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.SHOP_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                // Create an instance of the editor which is help us to edit the SharedPreference.
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                //pass the result to the Login Activity.
                when (activity) {
                    is LoginActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userLoggedInSuccess(user)
                    }
                }

            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is LoginActivity -> {
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }


     //A function to update the user profile data into the database.

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        // Collection Name
        mFireStore.collection(Constants.USERS)
                // Document ID against which the data to be updated. Here the document id is the current logged in user id.
                .document(getCurrentUserID())
                // A HashMap of fields which are to be updated.
                .update(userHashMap)
                .addOnSuccessListener {
                    
                }
                .addOnFailureListener { e ->
                    Log.e(
                            activity.javaClass.simpleName,
                            "Error while updating the user details.",
                            e
                    )
                }
    }

    fun getImageReference (item: MarketItem) : StorageReference {
        var itemFolderRef = this.storage.reference

        var id = ""

        when (item) {
            is Product -> {itemFolderRef = itemFolderRef.child("product")
                id = item.id
            }

            is ProductCategory -> {itemFolderRef = itemFolderRef.child("category")
                id = item.id
            }
        }

        val itemRef = itemFolderRef.child((id +".jpg"))

        return itemRef
    }

    fun addProduct(product: Product, context: Context) {
        mFireStore.collection("products")
                .add(product)
                .addOnSuccessListener { documentReference ->

                    product.id = documentReference.id

                    mFireStore.collection("products")
                            .document(documentReference.id)
                            .set(product, SetOptions.merge())
                            .addOnSuccessListener {
                                Toast.makeText(context,"Product Added",Toast.LENGTH_SHORT).show()
                            }
                }
    }

    fun addCategory(category: ProductCategory, context: Context) {
        mFireStore.collection("categories")
            .add(category)
            .addOnSuccessListener { documentReference ->

                category.id = documentReference.id

                mFireStore.collection("categories")
                    .document(documentReference.id)
                    .set(category, SetOptions.merge())
                        .addOnSuccessListener {
                            Toast.makeText(context,"Category Added",Toast.LENGTH_SHORT).show()
                        }
            }
    }

    fun getProducts(parentID: String, context: Context) : Task<QuerySnapshot> {
        var queryRes = mFireStore.collection("products")
                .whereEqualTo("parentID", parentID)
                .get()

        while (!queryRes.isComplete && !queryRes.isCanceled);

        return  queryRes
    }

    fun getCategories(depth: Int, context: Context) : Task<QuerySnapshot> {

        var queryRes = mFireStore.collection("categories")
                .whereEqualTo("depth", depth)
                .get()

        while (!queryRes.isComplete && !queryRes.isCanceled);

        return  queryRes
    }

    fun getOrders() : Task<QuerySnapshot> {

        var queryRes = mFireStore.collection("orders")
                .whereEqualTo("userID", getCurrentUserID())
                .get()

        while (!queryRes.isComplete && !queryRes.isCanceled);

        return  queryRes
    }

    fun addOrder(product : Product, quantitiy : Int, context: Context)
    {
        var order : Order = Order()
        order.cost = quantitiy * product.price
        order.quantitiy = quantitiy
        order.productID = product.id
        order.userID = getCurrentUserID()
        order.productName = product.name

        mFireStore.collection("orders").add(order).addOnSuccessListener { orderDocument ->
            order.id = orderDocument.id

            mFireStore.collection("orders")
                    .document(orderDocument.id)
                    .set(order, SetOptions.merge())
                    .addOnSuccessListener {
                        mFireStore.collection("products")
                                .document(product.id)
                                .set(product, SetOptions.merge())
                                .addOnSuccessListener { Toast.makeText(context,"Sipariş Sepete Eklendi",Toast.LENGTH_SHORT).show() }
                    }
        }
    }

    suspend fun removeOrder(order: Order) : Boolean {

        var task1 = mFireStore.collection("orders").document(order.id).delete()

        while (!task1.isComplete);

        if(task1.isSuccessful)
        {
            var task2 = mFireStore.collection("orders").whereEqualTo("id", order.id).get()

            while (!task2.isComplete);

            if(task2.result != null)
            {
                mFireStore.collection("products").document(order.productID).get().addOnSuccessListener {x->
                    var product : Product = x.toObject(Product::class.java)!!
                    product.stock = product.stock + order.quantitiy
                    mFireStore.collection("products").document(product.id).set(product, SetOptions.merge())
                }

                return true
            }
            else
            {
                return true
            }
        }
        else
            return false
    }
}