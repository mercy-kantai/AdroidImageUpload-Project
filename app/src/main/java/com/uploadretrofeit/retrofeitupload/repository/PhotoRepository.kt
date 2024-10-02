package com.uploadretrofeit.retrofeitupload.repository

import android.content.ContentResolver
import android.net.DnsResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.customview.widget.Openable
import com.uploadretrofeit.retrofeitupload.MyApp
import com.uploadretrofeit.retrofeitupload.api.ApiClient
import com.uploadretrofeit.retrofeitupload.api.ApiInterface
import com.uploadretrofeit.retrofeitupload.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class PhotoRepository {
    val apiInterface = ApiClient.buildClient(ApiInterface::class.java)
    suspend fun uploadPhoto(uri: Uri, caption: String): Response<Photo>{
        return withContext(Dispatchers.IO){
           val imageFile = getFileFromUri(uri)
           val imageRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
           val imageRequest = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)
           val captionRequest = MultipartBody.Part.createFormData("caption", caption)
            apiInterface.uploadPhoto(captionRequest,imageRequest)

        }
    }
   fun getFileFromUri(uri: Uri): File{
       val context = MyApp.appContext
       val inputStream = context.contentResolver.openInputStream(uri)
       val file = File(context.filesDir, getFileNameFromUri(context.contentResolver, uri))
       val outputStream = FileOutputStream(file)
       inputStream!!.copyTo(outputStream)
       inputStream.close()
       outputStream.close()
       return file
   }

    fun getFileNameFromUri(resolver: ContentResolver, uri: Uri): String{
        val cursor = resolver.query(uri, null, null, null,null )!!
        val nameIndex= cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        val name = cursor.getString(nameIndex)
        cursor.close()
        return name
    }

}