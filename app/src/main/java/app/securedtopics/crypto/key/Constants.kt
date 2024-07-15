package app.securedtopics.crypto.key

import android.security.keystore.KeyProperties

const val ANDROID_KEYSTORE = "AndroidKeyStore"
const val KEY_SIZE_SYMMETRIC = 128
const val KEY_SIZE_ASYMMETRIC = 1024
const val KEY_ALGORITHM_SYMMETRIC = KeyProperties.KEY_ALGORITHM_AES
const val KEY_ALGORITHM_ASYMMETRIC = KeyProperties.KEY_ALGORITHM_RSA