package android.luispouso.recipes.features.recipes.commons.repository.di

import android.content.Context
import android.luispouso.recipes.features.recipes.commons.repository.data.ImageApiDataSource
import android.luispouso.recipes.features.recipes.commons.repository.data.RecipeApiDataSource
import android.net.ConnectivityManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRecipeApiDataSource(db: FirebaseFirestore): RecipeApiDataSource {
        return RecipeApiDataSource(db)
    }

    @Provides
    @Singleton
    fun provideImageApiDataSource(storageRef: StorageReference): ImageApiDataSource {
        return ImageApiDataSource(storageRef)
    }

    @Provides
    @Singleton
    fun provideFirebaseDataBase() : FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideStorageReference() : StorageReference {
        return Firebase.storage.reference
    }

    @Singleton
    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}