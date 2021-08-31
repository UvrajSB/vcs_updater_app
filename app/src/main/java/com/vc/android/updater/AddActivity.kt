
package com.vc.android.updater

import android.R.attr
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.FirebaseStorageKtxRegistrar
import com.google.firebase.storage.ktx.storage
import com.vc.android.updater.DataClass.ProductInfo
import com.vc.android.updater.databinding.ActivityAddBinding
import java.io.File
import java.net.URL


@Suppress("Deprecation")
class AddActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var category : String = "Earrings"
    val REQUEST_CODE = 200
    val imageUrlArray = ArrayList<String>()
    var imageUriArray= ArrayList<Uri>()
    val db = Firebase.firestore
    val PICK_IMAGE_MULTIPLE = 1
    lateinit var binding: ActivityAddBinding
    val storage = Firebase.storage
    val storageRef = storage.reference
    lateinit var code: String
    lateinit var name : String
    lateinit var price: String
    lateinit var desc: String
    lateinit var stock : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add)

        binding.select.setOnClickListener {
            intent = Intent()
            // setting type to select to be image
            intent.setType("image/*")
            // allowing multiple image to be selected
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_MULTIPLE
            )
        }

        binding.saveBtn.setOnClickListener{
            code = binding.productCodeET.text.toString()
            name = binding.productNameET.text.toString()
            price = binding.productPriceET.text.toString()
            desc = binding.productDescET.text.toString()
            stock = binding.stock.text.toString()
            //converting imageUriArray to imageUrlArray
            Log.d("AddActivity","${imageUriArray.size}")
            imageUrlArray.clear()
            for(i in 0 until imageUriArray.size){
                uploadImage(imageUriArray[i])
            }
            Log.d("AddActivity","size of image Url Array ${imageUrlArray.size}")

        }

        val spinner: Spinner = findViewById(R.id.spinner)
        spinner.onItemSelectedListener = this

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.categories_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
    }
    fun uploadImage(uri: Uri){
        val ImageRef = storageRef.child("images/${uri.lastPathSegment}")


        val uploadTask = ImageRef.putFile(uri)
            .addOnSuccessListener { // Image uploaded successfully

               ImageRef.downloadUrl.addOnSuccessListener { uri ->
                    var imageUrl = uri.toString()

                   imageUrlArray.add(imageUrl)
                   Log.d("AddActivity","download uri is working $imageUrl")
                   Log.d("AddActivity","${imageUrlArray.size} and ${imageUriArray.size}")
                   if(imageUriArray.size == imageUrlArray.size){
                       var product = ProductInfo(category,code,name,price,desc,stock,imageUrlArray)
                       db.collection("$category").document("$code").set(product)
                           .addOnSuccessListener {
                               Log.d("Add","success working")
                           }.addOnFailureListener{
                               Log.d("Add","${it.message}")
                           }
                       Log.d("Add","add button working")


                       db.collection("Products").document("${product.Code}").set(product)
                       startActivity(Intent(this,MainActivity2::class.java))
                       finish()
                   }


                }.addOnFailureListener {
                   Log.d("AddActivity", "failure")
                }
            }
            .addOnFailureListener { e -> // Error, Image not uploaded
                Log.d("AddActivity","download uri is not working 2")
                Toast.makeText(this, "Failed " + e.message, Toast.LENGTH_SHORT).show()
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("Array", "OnActivityResult is called ")
            imageUriArray.clear()
        if (resultCode === RESULT_OK) {
            if (requestCode === PICK_IMAGE_MULTIPLE) {
                if (data?.getClipData() != null) {
                    val mClipData: ClipData = data.getClipData()!!
                    for (i in 0 until mClipData.itemCount) {
                        val item = mClipData.getItemAt(i)
                        val uri = item.uri
                        // display your images
                        imageUriArray.add(uri)
                        Log.d("Array", "${imageUriArray}")
                        Toast.makeText(this,"Image uploaded successfuly",Toast.LENGTH_SHORT)
                    }
                } else if (data?.getData() != null) {
                    val uri: Uri = data.getData()!!
                    // display your image
                    imageUriArray.add(uri)
                    Log.d("Array", "${imageUriArray}")
                    Toast.makeText(this,"Image uploaded successfuly",Toast.LENGTH_SHORT)
                }
            }
        }

        binding.carouselView.setImageListener { position, imageView ->
                imageView.setImageURI(imageUriArray[position])}
        binding.carouselView.pageCount = imageUriArray.size
        }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(position){
            0 -> category = "Earrings"
            1 -> category = "Necklaces and Sets"
            2 -> category = "Bracelets and Bangle"
            3 -> category = "Rings and Nosepins"
            4 -> category = "Anklets"
            5 -> category = "Scrunchies and Others"

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}



