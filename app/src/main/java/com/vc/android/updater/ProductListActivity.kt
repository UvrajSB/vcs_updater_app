package com.vc.android.updater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vc.android.updater.Adapter.ProductsAdapter
import com.vc.android.updater.DataClass.ProductInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

class ProductListActivity : AppCompatActivity() {
    var productListArray = ArrayList<ProductInfo>()
    val adapter = ProductsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        addData()
        val rv = findViewById<RecyclerView>(R.id.productListrv)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rv.adapter = adapter
    }

    private fun addData() {
        GlobalScope.launch(Dispatchers.Main) {
            val firestoreRV = Firebase.firestore.collection("Products")
            var NewProduct : ProductInfo
            firestoreRV
                .get()
                .addOnSuccessListener { products ->
                    productListArray.clear()
                    for(product in products){
                        NewProduct = ProductInfo( product.getString("category"),product.getString("code"),product.getString("name"),product.getString("price"),product.getString("desc"),product.getString("stock"),
                            product.get("imageArray") as ArrayList<String>?
                        )
                        productListArray.add(NewProduct)
                    }
                    adapter.updateUsers(productListArray)


                }

        }
    }
}