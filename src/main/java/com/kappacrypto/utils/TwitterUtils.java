package com.kappacrypto.utils;

import com.kappacrypto.Models.Crypto;

import java.util.List;

public class TwitterUtils {

    public static String createRuleFromAssetName(List<Crypto> assets) {
        StringBuilder sb = new StringBuilder();
        int len = 5;
        for (int i = 0; i < len; i++) {
            Crypto curr = assets.get(i);

            sb.append(curr.assetId + " OR ");
            if (curr.name.indexOf(" ") != -1) {
                sb.append("\"" + curr.name + "\"");
            } else {
                sb.append(curr.name);
            }
            if (i < len - 1) sb.append(" OR ");
        }

        return sb.toString();
    }
}
