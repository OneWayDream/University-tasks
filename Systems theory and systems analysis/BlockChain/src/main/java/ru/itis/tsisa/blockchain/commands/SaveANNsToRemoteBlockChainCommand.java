package ru.itis.tsisa.blockchain.commands;

import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.core.encryption.HashUtils;
import ru.itis.tsisa.blockchain.core.encryption.KeyManager;
import ru.itis.tsisa.blockchain.core.encryption.Subscriber;
import ru.itis.tsisa.blockchain.exceptions.AuditorConnectionException;
import ru.itis.tsisa.blockchain.utils.BytesUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Objects;

@Component
public class SaveANNsToRemoteBlockChainCommand implements DescribedCommand{

    private static final String BLOCK_CHAIN_URL = "http://89.108.115.118/nbc/chain";
    private static final String ARBITER_SIGN_URL = "http://89.108.115.118/nbc/newblock/";
    private static final String AUTHOR_SUBMIT_URL = "http://89.108.115.118/nbc/autor";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static final String FILE_NAME = "3_1_results.txt";
    private final HashUtils hashUtils;
    private final OkHttpClient client;

    private final Subscriber subscriber;

    private final PublicKey publicKey;

    public SaveANNsToRemoteBlockChainCommand(
            KeyManager keyManager,
            HashUtils hashUtils
    ){
        this.hashUtils = hashUtils;
        publicKey = keyManager.getPublicKey(-1);
        PrivateKey privateKey = keyManager.getPrivateKey(-1);
        subscriber = new Subscriber(privateKey);
        client = new OkHttpClient();
    }

    @Override
    public void execute() {
        try{
            String data = loadANNsData();
            data = addPublicKey(data);
            String previousBlockHash = getPreviousBlockHash();
            String signature = subscribeData(data);
            String block = formBlock(previousBlockHash, data, signature);
            sendBlockToArbiter(block);
            submitAuthor();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private String loadANNsData() throws Exception{
        File f = new File(FILE_NAME);
        if (f.exists()){
            InputStream is = new FileInputStream(FILE_NAME);
            String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
            jsonTxt = jsonTxt.replace(" ", "");
            jsonTxt = jsonTxt.substring(0, jsonTxt.length() - 1);
            return jsonTxt;
        }
        throw new RuntimeException();
    }

    private String addPublicKey(String data){
        String hexPublicKey = hashUtils.bytesToHex(publicKey.getEncoded());
        return data + ",\"publickey\":\"" + hexPublicKey + "\"}";
    }

    private String getPreviousBlockHash(){
        JSONObject lastBlock = getLastBlock();
        byte[] content = hashUtils.hexToBytes(lastBlock.getString("prevhash"));
        content = BytesUtils.concatenate(content, blockDataToBytes(lastBlock.getJSONObject("data")));
        byte[] signature = hashUtils.hexToBytes(lastBlock.getString("signature"));
        content = BytesUtils.concatenate(content, signature);
        byte[] lastBlockHash = hashUtils.getHash(content);
        return hashUtils.bytesToHex(lastBlockHash);
    }

    private JSONObject getLastBlock(){
        Request request = new Request.Builder()
                .url(BLOCK_CHAIN_URL)
                .build();
        JSONArray response = new JSONArray(executeRequest(request));
        return response.getJSONObject(response.length() - 1);
    }

    private byte[] blockDataToBytes(JSONObject jsonObject){
        return ("{" +
                "\"w11\":\"" + jsonObject.getString("w11") + "\"," +
                "\"w12\":\"" + jsonObject.getString("w12") + "\"," +
                "\"w21\":\"" + jsonObject.getString("w21") + "\"," +
                "\"w22\":\"" + jsonObject.getString("w22") + "\"," +
                "\"v11\":\"" + jsonObject.getString("v11") + "\"," +
                "\"v12\":\"" + jsonObject.getString("v12") + "\"," +
                "\"v13\":\"" + jsonObject.getString("v13") + "\"," +
                "\"v21\":\"" + jsonObject.getString("v21") + "\"," +
                "\"v22\":\"" + jsonObject.getString("v22") + "\"," +
                "\"v23\":\"" + jsonObject.getString("v23") + "\"," +
                "\"w1\":\"" + jsonObject.getString("w1") + "\"," +
                "\"w2\":\"" + jsonObject.getString("w2") + "\"," +
                "\"w3\":\"" + jsonObject.getString("w3") + "\"," +
                "\"e\":\"" + jsonObject.getString("e") + "\"," +
                "\"publickey\":\"" + jsonObject.getString("publickey") + "\"}").getBytes(StandardCharsets.UTF_8);
    }

    private String executeRequest(Request request){
        try (Response response = client.newCall(request).execute()){
            if (response.code() != 200 || response.body() == null){
                System.out.println(response.body().string());
                throw new AuditorConnectionException();
            }
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException ex) {
            throw new AuditorConnectionException(ex);
        }
    }

    private String formBlock(String previousHash, String data, String signature){
        return "{\"prevhash\":\"" + previousHash + "\",\"data\":" + data + ",\"signature\":\"" + signature + "\"}";
    }

    private String subscribeData(String data){
        byte[] signature = subscriber.subscribeMessage(data.getBytes());
        return hashUtils.bytesToHex(signature);
    }

    private void sendBlockToArbiter(String block){
        RequestBody requestBody = RequestBody.create(block, JSON);
        Request request = new Request.Builder()
                .url(ARBITER_SIGN_URL)
                .post(requestBody)
                .build();
        String response = executeRequest(request);
        System.out.println(response);
    }

    private void submitAuthor(){
        String authorString = "Свидиров Кирилл, 11-902";
        byte[] authorStringSign = subscriber.subscribeMessage(authorString.getBytes());
        String hexSign = hashUtils.bytesToHex(authorStringSign);
        String hexPublicKey = hashUtils.bytesToHex(publicKey.getEncoded());
        String content = makeAuthorVerificationContent(authorString, hexSign, hexPublicKey);
        sendAuthorVerification(content);
    }

    private String makeAuthorVerificationContent(String authorString, String hexSignature, String hexPublicKey){
        return "{\"autor\":\"" + authorString + "\",\"sign\":\"" + hexSignature + "\",\"publickey\":\""
                + hexPublicKey + "\"}";
    }

    private void sendAuthorVerification(String content){
        RequestBody requestBody = MultipartBody.create(content, JSON);
        Request request = new Request.Builder()
                .url(AUTHOR_SUBMIT_URL)
                .post(requestBody)
                .build();
        String response = executeRequest(request);
        System.out.println(response);
    }

    @Override
    public Command getCommandType() {
        return Command.SAVE_ANNS;
    }

    @Override
    public String getInfo() {
        return getCommandType().getValue() + " - Save ANNs results to the remote blockchain.";
    }
}
