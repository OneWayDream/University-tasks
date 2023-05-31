package ru.itis.tsisa.blockchain.core.structures;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.core.blocks.Block;
import ru.itis.tsisa.blockchain.core.encryption.EncryptionUtils;
import ru.itis.tsisa.blockchain.core.encryption.HashUtils;
import ru.itis.tsisa.blockchain.exceptions.AuditorConnectionException;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Objects;

@Component
@Profile("remote")
public class RemoteAuditorService implements AuditorService {
    private final String SIGN_URL;
    private final String KEY_URL;
    private final OkHttpClient client;
    private final HashUtils hashUtils;

    private PublicKey auditorPublicKey;

    public RemoteAuditorService(
            @Value("${block-chain.auditor.sign-url}") String signUrl,
            @Value("${block-chain.auditor.key-url}") String keyUrl,
            HashUtils hashUtils
            ){
        this.client = new OkHttpClient();
        SIGN_URL = signUrl;
        KEY_URL = keyUrl;
        this.hashUtils = hashUtils;
    }
    @Override
    public synchronized void signBlock(Block block) {
        String url = getRequestUrlForBlock(block);
        Request request = new Request.Builder()
                .url(url)
                .build();
        JSONObject response = new JSONObject(executeRequest(request));
        handleSignResponse(response, block);
    }

    private String getRequestUrlForBlock(Block block){
        byte[] blockHash = hashUtils.getHash(block.getBytes());
        String hexString = hashUtils.bytesToHex(blockHash);
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(SIGN_URL)).newBuilder();
        urlBuilder.addQueryParameter("digest", hexString);
        return urlBuilder.build().toString();
    }

    private String executeRequest(Request request){
        try (Response response = client.newCall(request).execute()){
            if (response.code() != 200 || response.body() == null){
                throw new AuditorConnectionException();
            }
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException ex) {
            throw new AuditorConnectionException(ex);
        }
    }

    private void handleSignResponse(JSONObject response, Block block){
        handleResponseStatus(response);
        JSONObject timeStampToken = response.getJSONObject("timeStampToken");
        String dateTimeString = timeStampToken.getString("ts");
        String hexSignature = timeStampToken.getString("signature");
        byte[] auditorSign = hashUtils.hexToBytes(hexSignature);
        block.setAuditorSignDateTime(dateTimeString);
        block.setAuditorSign(auditorSign);
    }

    private void handleResponseStatus(JSONObject response){
        if (response.getInt("status") != 0){
            throw new AuditorConnectionException(response.getString("statusString"));
        }
    }

    @Override
    public PublicKey getPublicKey() {
        if (auditorPublicKey == null){
            initAuditorPublicKey();
        }
        return auditorPublicKey;
    }

    private void initAuditorPublicKey(){
        String url = getKeyUrl();
        Request request = new Request.Builder()
                .url(url)
                .build();
        String keyString = executeRequest(request);
        byte[] keyBytes = hashUtils.hexToBytes(keyString);
        auditorPublicKey = EncryptionUtils.recoverPublicKey(keyBytes);
    }

    private String getKeyUrl(){
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(KEY_URL)).newBuilder();
        return urlBuilder.build().toString();
    }

}
