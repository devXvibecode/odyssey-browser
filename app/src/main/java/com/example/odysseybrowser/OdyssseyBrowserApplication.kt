package com.example.odysseybrowser

import android.app.Application
import com.example.odysseybrowser.data.local.ChatDatabase

class OdyssseyBrowserApplication : Application() {

    val chatDatabase by lazy { ChatDatabase.getDatabase(this) }

    // We can also initialize Retrofit here if we want a single instance
}