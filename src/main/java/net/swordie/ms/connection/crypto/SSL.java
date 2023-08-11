package net.swordie.ms.connection.crypto;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.ssl.OptionalSslHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import net.swordie.ms.ServerConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public final class SSL {
    private static final Logger log = LogManager.getLogger(SSL.class);
    private static SslContext sslContext;

    public static void initialize() {
        try {
            KeyStore keyStore;
            KeyManagerFactory keyManagerFactory;
            TrustManagerFactory trustManagerFactory;
            try {
                keyStore = KeyStore.getInstance("JKS");
                InputStream in = new FileInputStream(ServerConstants.SSL_KEYSTORE_PATH);
                keyStore.load(in, ServerConstants.SSL_STORE_PASSWORD.toCharArray());
            } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
                log.error("Failed to initialise keyStore from path : " + ServerConstants.SSL_KEYSTORE_PATH);
                throw new RuntimeException(e);
            }
            try {
                keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, ServerConstants.SSL_KEY_PASSWORD.toCharArray());
            } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
                log.error("Failed to initialise keyManagerFactory");
                throw new RuntimeException(e);
            }
            try {
                trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
            } catch (NoSuchAlgorithmException | KeyStoreException e) {
                log.error("Failed to initialise trustManagerFactory");
                throw new RuntimeException(e);
            }
            sslContext = SslContextBuilder.forServer(keyManagerFactory).build();
        } catch (SSLException e) {
            log.error("Failed to initialise sslContext");
            throw new RuntimeException(e);
        }
    }

    public static ByteToMessageDecoder createHandler() {
        if (sslContext == null) {
            log.error("SSL Context not initialized");
            throw new RuntimeException();
        }
        return new OptionalSslHandler(sslContext);
    }
}
