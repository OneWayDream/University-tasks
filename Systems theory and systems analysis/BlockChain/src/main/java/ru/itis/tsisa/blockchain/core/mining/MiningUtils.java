package ru.itis.tsisa.blockchain.core.mining;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class MiningUtils {

    private final int MINERS_AMOUNT;

    @Getter
    private long startValue;
    @Getter
    private long finishValue;


    public MiningUtils(
            @Value("${block-chain.miners-amount}") int minersAmount
    ) {
        this.MINERS_AMOUNT = minersAmount;
    }

    public MiningUtils init(int minerId){
        MiningUtils miningUtils = new MiningUtils(MINERS_AMOUNT);
        initSearchBorders(miningUtils, minerId);
        return miningUtils;
    }

    private void initSearchBorders(MiningUtils miningUtils, int minerId){
        BigInteger miningPart =  BigInteger.valueOf(Long.MAX_VALUE)
                .subtract(BigInteger.valueOf
                        (Long.MIN_VALUE))
                .divide(BigInteger.valueOf(MINERS_AMOUNT));
        miningUtils.startValue = Long.MIN_VALUE + miningPart.multiply(BigInteger.valueOf(minerId))
                .longValue();
        miningUtils.finishValue = Long.MIN_VALUE + miningPart.multiply(BigInteger.valueOf(minerId + 1))
                .subtract(BigInteger.ONE)
                .longValue();
    }
}
