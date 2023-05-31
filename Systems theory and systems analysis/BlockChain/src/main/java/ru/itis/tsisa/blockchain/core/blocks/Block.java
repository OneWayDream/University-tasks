package ru.itis.tsisa.blockchain.core.blocks;

public interface Block {

    byte[] getBytes();
    void setAuditorSign(byte[] auditorSign);
    void setMinerSign(byte[] minerSign);
    void setAuditorSignDateTime (String dataTimeString);

}
