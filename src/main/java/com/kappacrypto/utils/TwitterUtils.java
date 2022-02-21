package com.kappacrypto.utils;

import com.kappacrypto.Models.Crypto;

import java.util.List;

public class TwitterUtils {

    public static String createRuleFromAssetName(List<Crypto> assets) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < assets.size(); i++) {
            Crypto curr = assets.get(i);

            sb.append("\"$" + curr.assetId + "\"");
            if (i < assets.size() - 1) sb.append(" OR ");
        }

        sb.append(") -giveaway -win");

        return sb.toString();
    }
}
