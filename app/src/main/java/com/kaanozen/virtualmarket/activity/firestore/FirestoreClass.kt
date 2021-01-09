package com.kaanozen.virtualmarket.activity.firestore

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kaanozen.virtualmarket.activity.LoginActivity
import com.kaanozen.virtualmarket.activity.RegisterActivity
import com.kaanozen.virtualmarket.activity.UserProfileActivity
import com.kaanozen.virtualmarket.activity.model.Order
import com.kaanozen.virtualmarket.activity.model.Product
import com.kaanozen.virtualmarket.activity.model.ProductCategory
import com.kaanozen.virtualmarket.activity.model.User
import com.kaanozen.virtualmarket.activity.utilies.Constants

//A custom class where we will add the operation performed for the FireStore database.

open class FirestoreClass {

    // Access a Cloud Firestore instance.
    private val mFireStore = FirebaseFirestore.getInstance()

    private val storage = Firebase.storage

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

    private fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!

                //pass the result to the Login Activity.
                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    else -> {
                        val intent = Intent(activity, UserProfileActivity::class.java)
                        intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
                        activity.startActivity(intent)
                        activity.finish()
                    }
                }

            }
    }

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

    fun addProduct(product: Product, context: Context) {
        mFireStore.collection(Constants.PRODUCTS)
            .add(product)
            .addOnSuccessListener { documentReference ->

                product.id = documentReference.id

                mFireStore.collection(Constants.PRODUCTS)
                    .document(documentReference.id)
                    .set(product, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(context,"Product Added",Toast.LENGTH_SHORT).show()
                    }
            }
    }

    fun addCategory(category: ProductCategory, context: Context) {
        mFireStore.collection(Constants.CATEGORIES)
            .add(category)
            .addOnSuccessListener { documentReference ->

                category.id = documentReference.id

                mFireStore.collection(Constants.CATEGORIES)
                    .document(documentReference.id)
                    .set(category, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(context,"Category Added",Toast.LENGTH_SHORT).show()
                    }
            }
    }

    fun getProducts(parentID: String, context: Context) : Task<QuerySnapshot> {
        var queryRes = mFireStore.collection(Constants.PRODUCTS)
            .whereEqualTo("parentID", parentID)
            .get()

        while (!queryRes.isComplete && !queryRes.isCanceled);

        return  queryRes
    }

    fun getCategories(depth: Int, context: Context) : Task<QuerySnapshot> {

        var queryRes = mFireStore.collection(Constants.CATEGORIES)
            .whereEqualTo("depth", depth)
            .get()

        while (!queryRes.isComplete && !queryRes.isCanceled);

        return  queryRes
    }

    fun getOrders() : Task<QuerySnapshot> {

        var queryRes = mFireStore.collection(Constants.ORDERS)
            .whereEqualTo("userID", getCurrentUserID())
            .get()

        while (!queryRes.isComplete && !queryRes.isCanceled);

        return  queryRes
    }

    fun addOrder(product : Product, quantity : Int, context: Context) {
        var order : Order = Order()
        order.cost = quantity * product.price
        order.quantitiy = quantity
        order.productID = product.id
        order.userID = getCurrentUserID()
        order.productName = product.name

        mFireStore.collection(Constants.ORDERS).add(order).addOnSuccessListener { orderDocument ->
            order.id = orderDocument.id

            mFireStore.collection(Constants.ORDERS)
                .document(orderDocument.id)
                .set(order, SetOptions.merge())
                .addOnSuccessListener {
                    mFireStore.collection(Constants.PRODUCTS)
                        .document(product.id)
                        .set(product, SetOptions.merge())
                        .addOnSuccessListener { Toast.makeText(context,"Sipariş Sepete Eklendi",Toast.LENGTH_SHORT).show() }
                }
        }
    }

    fun removeOrder(order: Order) : Boolean {

        var task1 = mFireStore.collection(Constants.ORDERS).document(order.id).delete()

        while (!task1.isComplete);

        if(task1.isSuccessful)
        {
            var task2 = mFireStore.collection(Constants.ORDERS).whereEqualTo("id", order.id).get()

            while (!task2.isComplete);

            if(task2.result != null)
            {
                mFireStore.collection(Constants.PRODUCTS).document(order.productID).get().addOnSuccessListener {x->
                    var product : Product = x.toObject(Product::class.java)!!
                    product.stock = product.stock + order.quantitiy
                    mFireStore.collection(Constants.PRODUCTS).document(product.id).set(product, SetOptions.merge())
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