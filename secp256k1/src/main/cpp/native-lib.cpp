#include <jni.h>
#include <string>
#include <vector>
#include "encoding.h"
#include "library.h"

std::vector<uint8_t> ConvertToVector(JNIEnv* env, jbyteArray b) {
    jsize len = env->GetArrayLength(b);
    std::vector<uint8_t> v(len);
    env->GetByteArrayRegion(b, 0, len, reinterpret_cast<jbyte *>(v.data()));
    return v;
}

jbyteArray ConvertToByteArray(JNIEnv* env, const std::vector<uint8_t>& v) {
    size_t len = v.size();
    jbyteArray b = env->NewByteArray(len);
    env->SetByteArrayRegion(b, 0, len, reinterpret_cast<const jbyte *>(v.data()));
    return b;
}

std::string ConvertToString(JNIEnv* env, jstring js) {
    jsize len = env->GetStringUTFLength(js);
    std::string s(len, 0);
    env->GetStringUTFRegion(js, 0, len, &s[0]);
    return s;
}

jstring ConvertToJString(JNIEnv* env, const std::string& s) {
    return env->NewStringUTF(s.c_str());
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_peerbridge_secp256k1_Libsecp256k1_createKeyPair(JNIEnv* env, jclass clazz) {
    const auto& [privateKey, publicKey] = secp256k1::Encryption::getInstance().createKeyPair();

    jclass cls = env->FindClass("kotlin/Pair");
    jmethodID constructor = env->GetMethodID(cls,  "<init>","(Ljava/lang/Object;Ljava/lang/Object;)V");
    return env->NewObject(cls, constructor, ConvertToByteArray(env, *privateKey), ConvertToByteArray(env, *publicKey));
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_peerbridge_secp256k1_Libsecp256k1_createKeyPairHex(JNIEnv* env, jclass clazz) {
    const auto& [privateKey, publicKey] = secp256k1::Encryption::getInstance().createKeyPair();

    std::string privateKeyHex = encoding::hex::EncodeToString(*privateKey);
    std::string publicKeyHex = encoding::hex::EncodeToString(*publicKey);

    jclass cls = env->FindClass("kotlin/Pair");
    jmethodID constructor = env->GetMethodID(cls,  "<init>","(Ljava/lang/Object;Ljava/lang/Object;)V");
    return env->NewObject(cls, constructor, ConvertToJString(env, privateKeyHex), ConvertToJString(env, publicKeyHex));
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_peerbridge_secp256k1_Libsecp256k1_computePublicKey(JNIEnv* env, jclass clazz, jbyteArray private_key) {
    const secp256k1::ByteArray key = ConvertToVector(env, private_key);

    const std::unique_ptr<secp256k1::ByteArray> publicKey = secp256k1::Encryption::getInstance().computePublicKey(key);
    if (!publicKey) return nullptr;

    return ConvertToByteArray(env, *publicKey);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_peerbridge_secp256k1_Libsecp256k1_computePublicKeyHex(JNIEnv* env, jclass clazz, jstring private_key) {
    const std::string keyHex = ConvertToString(env, private_key);
    const secp256k1::ByteArray key = encoding::hex::DecodeString(keyHex);

    const std::unique_ptr<secp256k1::ByteArray> publicKey = secp256k1::Encryption::getInstance().computePublicKey(key);
    if (!publicKey) return nullptr;

    const std::string publicKeyHex = encoding::hex::EncodeToString(*publicKey);
    return ConvertToJString(env, publicKeyHex);
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_peerbridge_secp256k1_Libsecp256k1_computeSecret(JNIEnv* env, jclass clazz, jbyteArray private_key, jbyteArray public_key) {
    const secp256k1::ByteArray privateKey = ConvertToVector(env, private_key);
    const secp256k1::ByteArray publicKey = ConvertToVector(env, public_key);

    const std::unique_ptr<secp256k1::ByteArray> secret = secp256k1::Encryption::getInstance().computeSecret(privateKey, publicKey);
    if (!secret) return nullptr;

    return ConvertToByteArray(env, *secret);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_peerbridge_secp256k1_Libsecp256k1_computeSecretHex(JNIEnv* env, jclass clazz, jstring private_key, jstring public_key) {
    const std::string privateKeyHex = ConvertToString(env, private_key);
    const secp256k1::ByteArray privateKey = encoding::hex::DecodeString(privateKeyHex);

    const std::string publicKeyHex = ConvertToString(env, public_key);
    const secp256k1::ByteArray publicKey = encoding::hex::DecodeString(publicKeyHex);

    const std::unique_ptr<secp256k1::ByteArray> secret = secp256k1::Encryption::getInstance().computeSecret(privateKey, publicKey);
    if (!secret) return nullptr;

    const std::string secretHex = encoding::hex::EncodeToString(*secret);
    return ConvertToJString(env, secretHex);
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_peerbridge_secp256k1_Libsecp256k1_sign(JNIEnv* env, jclass clazz, jbyteArray private_key, jbyteArray message) {
    const secp256k1::ByteArray key = ConvertToVector(env, private_key);
    const secp256k1::ByteArray msg = ConvertToVector(env, message);

    const std::unique_ptr<secp256k1::ByteArray> signature = secp256k1::Encryption::getInstance().sign(msg, key);
    if (!signature) return nullptr;

    return ConvertToByteArray(env, *signature);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_peerbridge_secp256k1_Libsecp256k1_signHex(JNIEnv* env, jclass clazz, jstring private_key, jstring message) {
    const std::string keyHex = ConvertToString(env, private_key);
    const secp256k1::ByteArray key = encoding::hex::DecodeString(keyHex);

    const std::string msgHex = ConvertToString(env, message);
    const secp256k1::ByteArray msg = encoding::hex::DecodeString(msgHex);

    const std::unique_ptr<secp256k1::ByteArray> signature = secp256k1::Encryption::getInstance().sign(msg, key);
    if (!signature) return nullptr;

    const std::string sigHex = encoding::hex::EncodeToString(*signature);
    return ConvertToJString(env, sigHex);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_peerbridge_secp256k1_Libsecp256k1_verifySignature(JNIEnv* env, jclass clazz, jbyteArray public_key, jbyteArray message, jbyteArray signature) {
    const secp256k1::ByteArray key = ConvertToVector(env, public_key);
    const secp256k1::ByteArray msg = ConvertToVector(env, message);
    const secp256k1::ByteArray sig = ConvertToVector(env, signature);

    return secp256k1::Encryption::getInstance().verifySignature(msg, key, sig);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_peerbridge_secp256k1_Libsecp256k1_verifySignatureHex(JNIEnv* env, jclass clazz, jstring public_key, jstring message, jstring signature) {
    const std::string keyHex = ConvertToString(env, public_key);
    const secp256k1::ByteArray key = encoding::hex::DecodeString(keyHex);

    const std::string msgHex = ConvertToString(env, message);
    const secp256k1::ByteArray msg = encoding::hex::DecodeString(msgHex);

    const std::string sigHex = ConvertToString(env, signature);
    const secp256k1::ByteArray sig = encoding::hex::DecodeString(sigHex);

    return secp256k1::Encryption::getInstance().verifySignature(msg, key, sig);
}
