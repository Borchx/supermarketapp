package com.borja.android.supermarketapp.ui.addproduct

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.borja.android.supermarketapp.R
import com.borja.android.supermarketapp.databinding.ActivityAddProductBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@AndroidEntryPoint
class AddProductActivity : AppCompatActivity() {

    companion object {
        fun create(context: Context): Intent {
            return Intent(context, AddProductActivity::class.java)
        }
    }

    private lateinit var uri: Uri

    private lateinit var binding: ActivityAddProductBinding
    private val addProductViewModel: AddProductViewModel by viewModels()

    private val intentCameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it && uri.path?.isNotEmpty() == true) {
                addProductViewModel.onImageSelected(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
        initUIState()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addProductViewModel.uiState.collect {
                    binding.pb.isVisible = it.isLoading
                    binding.btnAddProduct.isEnabled = it.isValidProduct()
                    showImage(it.imageUrl)
                    if(!it.error.isNullOrBlank()){

                    }
                }
            }
        }
    }

    private fun showImage(imageUrl: String) {
        val emptyImage = imageUrl.isEmpty()

        binding.apply {
            tvImage.isVisible = emptyImage
            etImage.isVisible = emptyImage
            cvImageProduct.isVisible = !emptyImage
            Glide.with(this@AddProductActivity).load(imageUrl).into(ivProduct)
        }

    }

    private fun initListeners() {
        binding.etName.doOnTextChanged { text, _, _, _ ->
            addProductViewModel.onNameChanged(text)
        }

        binding.etDescription.doOnTextChanged { text, _, _, _ ->
            addProductViewModel.onDescriptionChanged(text)
        }

        binding.etPrice.doOnTextChanged { text, _, _, _ ->
            addProductViewModel.onPriceChanged(text)
        }

        binding.etImage.setOnClickListener {
            takePhoto()
        }

        binding.btnAddProduct.setOnClickListener {
            addProductViewModel.onAddProductSelected {
                setResult(RESULT_OK)
                finish()
            }
        }
        
        binding.ivBack.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun takePhoto() {
        generateUri()
        intentCameraLauncher.launch(uri)
    }

    private fun generateUri() {
        uri = FileProvider.getUriForFile(
            Objects.requireNonNull(this),
            "com.borja.android.supermarketapp.provider",
            createFile()
        )
    }

    private fun createFile(): File {
        val name: String = SimpleDateFormat("yyyyMMdd_hhmmss").format(Date()) + "image"
        return File.createTempFile(name, ".jpg", externalCacheDir)
    }
}