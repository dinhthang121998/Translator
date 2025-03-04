package com.example.cipher

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class CryptoManager
    @Inject
    constructor() {
        companion object {
            private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
            private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
            private const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
            private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"

            private const val ANDROID_KEYSTORE = "AndroidKeyStore"
            private const val KEY_ALIAS = "MyKeyAlias"
        }

        private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        private val cipher = Cipher.getInstance(TRANSFORMATION)

        init {
            keyStore.load(null)
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                createSecretKey()
            }
        }

        private fun createSecretKey() {
            val keyGenParams =
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                ).apply {
                    setBlockModes(BLOCK_MODE)
                    setEncryptionPaddings(PADDING)
                    setUserAuthenticationRequired(false)
                    // TODO: set expired and refresh keystore
//            setKeyValidityStart()
//            setKeyValidityStart()
                }.build()

            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            keyGenerator.init(keyGenParams)
            keyGenerator.generateKey()
        }

        fun getSecretKey(): SecretKey {
            return keyStore.getKey(KEY_ALIAS, null) as SecretKey
        }

        fun encrypt(bytes: ByteArray): String {
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
            val iv = cipher.iv
            val encryptedBytes = cipher.doFinal(bytes)
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT) + ":" +
                Base64.encodeToString(iv, Base64.DEFAULT)
        }

        fun decrypt(encryptedText: String): ByteArray {
            val parts = encryptedText.split(":")
            val encryptedData = Base64.decode(parts[0], Base64.DEFAULT)
            val iv = Base64.decode(parts[1], Base64.DEFAULT)

            val spec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
            return cipher.doFinal(encryptedData)
        }
    }
