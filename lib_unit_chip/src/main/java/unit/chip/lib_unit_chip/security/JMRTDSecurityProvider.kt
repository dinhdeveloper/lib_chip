package unit.chip.lib_unit_chip.security

import java.security.Provider
import java.security.Security
import java.util.ArrayList
import java.util.Arrays
import java.util.logging.Logger


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


class JMRTDSecurityProvider private constructor() :
    Provider("JMRTD", 0.1, "JMRTD Security Provider") {

    init {
        put("CertificateFactory.CVC", "org.jmrtd.cert.CVCertificateFactorySpi")
        put("CertStore.PKD", "org.jmrtd.cert.PKDCertStoreSpi")
        put("CertStore.JKS", "org.jmrtd.cert.KeyStoreCertStoreSpi")
        put("CertStore.BKS", "org.jmrtd.cert.KeyStoreCertStoreSpi")
        put("CertStore.PKCS12", "org.jmrtd.cert.KeyStoreCertStoreSpi")

        /* But these work fine. */
        replicateFromProvider("CertificateFactory", "X.509", bouncyCastleProvider!!)
        replicateFromProvider("CertStore", "Collection", bouncyCastleProvider!!)
        //			replicateFromProvider("KeyStore", "JKS", SUN_PROVIDER);
        replicateFromProvider("MessageDigest", "SHA1", bouncyCastleProvider!!)
        replicateFromProvider("Signature", "SHA1withRSA/ISO9796-2", bouncyCastleProvider!!)
        replicateFromProvider("Signature", "MD2withRSA", bouncyCastleProvider!!)
        replicateFromProvider("Signature", "MD4withRSA", bouncyCastleProvider!!)
        replicateFromProvider("Signature", "MD5withRSA", bouncyCastleProvider!!)
        replicateFromProvider("Signature", "SHA1withRSA", bouncyCastleProvider!!)
        replicateFromProvider("Signature", "SHA1withRSA/ISO9796-2", bouncyCastleProvider!!)
        replicateFromProvider("Signature", "SHA256withRSA", bouncyCastleProvider!!)
        replicateFromProvider("Signature", "SHA256withRSA/ISO9796-2", bouncyCastleProvider!!)
        replicateFromProvider("Signature", "SHA384withRSA", bouncyCastleProvider!!)
        replicateFromProvider("Signature", "SHA384withRSA/ISO9796-2", bouncyCastleProvider!!)
        replicateFromProvider("Signature", "SHA512withRSA", bouncyCastleProvider!!)
        replicateFromProvider("Signature", "SHA512withRSA/ISO9796-2", bouncyCastleProvider!!)
        replicateFromProvider("Signature", "SHA224withRSA", bouncyCastleProvider!!)
        replicateFromProvider("Signature", "SHA224withRSA/ISO9796-2", bouncyCastleProvider!!)

        replicateFromProvider("Signature", "SHA256withRSA/PSS", bouncyCastleProvider!!)
        put("Alg.Alias.Mac.ISO9797Alg3Mac", "ISO9797ALG3MAC")
        put("Alg.Alias.CertificateFactory.X509", "X.509")
    }

    private fun replicateFromProvider(
        serviceName: String,
        algorithmName: String,
        provider: Provider
    ) {
        val name = "$serviceName.$algorithmName"
        val service = provider[name]
        if (service != null) {
            put(name, service)
        }
    }

    companion object {

        private val serialVersionUID = -2881416441551680704L

        private val LOGGER = Logger.getLogger("org.jmrtd")

        //	private static final Provider SUN_PROVIDER = null; // getProviderOrNull(SUN_PROVIDER_CLASS_NAME);
        private val BC_PROVIDER = org.bouncycastle.jce.provider.BouncyCastleProvider()

        //			getProviderOrNull(BC_PROVIDER_CLASS_NAME);
        private val SC_PROVIDER = org.spongycastle.jce.provider.BouncyCastleProvider()

        //			getProviderOrNull(SC_PROVIDER_CLASS_NAME);
        private val instance: Provider = JMRTDSecurityProvider()

        init {
            Security.insertProviderAt(org.spongycastle.jce.provider.BouncyCastleProvider(), 1)
        }

        fun beginPreferBouncyCastleProvider(): Int {
            val bcProvider = bouncyCastleProvider ?: return -1
            val providers = Security.getProviders()
            for (i in providers.indices) {
                val provider = providers[i]
                if (bcProvider.javaClass.canonicalName == provider.javaClass.canonicalName) {
                    Security.removeProvider(provider.name)
                    Security.insertProviderAt(bcProvider, 1)
                    return i + 1
                }
            }
            return -1
        }
        fun endPreferBouncyCastleProvider(i: Int) {
            val bcProvider = bouncyCastleProvider
            Security.removeProvider(bcProvider!!.name)
            if (i > 0) {
                Security.insertProviderAt(bcProvider, i)
            }
        }
        val bouncyCastleProvider: Provider?
            get() {
                if (BC_PROVIDER != null) {
                    return BC_PROVIDER
                }
                if (SC_PROVIDER != null) {
                    return SC_PROVIDER
                }
                LOGGER.severe("No Bouncy or Spongy provider")
                return null
            }
        val spongyCastleProvider: Provider?
            get() {
                if (SC_PROVIDER != null) {
                    return SC_PROVIDER
                }
                if (BC_PROVIDER != null) {
                    return BC_PROVIDER
                }
                LOGGER.severe("No Bouncy or Spongy provider")
                return null
            }

        private fun getProvider(serviceName: String, algorithmName: String): Provider? {
            val providers = getProviders(serviceName, algorithmName)
            return if (providers != null && providers.size > 0) {
                providers[0]
            } else null
        }

        private fun getProviders(serviceName: String, algorithmName: String): List<Provider>? {
            if (Security.getAlgorithms(serviceName).contains(algorithmName)) {
                val providers = Security.getProviders("$serviceName.$algorithmName")
                return ArrayList(Arrays.asList(*providers))
            }
            if (BC_PROVIDER.getService(serviceName, algorithmName) != null) {
                return ArrayList(listOf<Provider>(BC_PROVIDER))
            }
            if (SC_PROVIDER.getService(serviceName, algorithmName) != null) {
                return ArrayList(listOf<Provider>(SC_PROVIDER))
            }
            return if (instance.getService(
                    serviceName,
                    algorithmName
                ) != null
            ) {
                ArrayList(listOf(instance))
            } else null
        }
    }
}