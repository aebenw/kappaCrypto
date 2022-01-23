package com.kappacrypto.utils;

import com.kappacrypto.Models.Crypto;

import java.util.List;

public class TwitterUtils {

    public static String createRuleFromAssetName(List<Crypto> assets) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            Crypto curr = assets.get(i);
            sb.append(curr.assetId + " OR " + curr.name + " ");
        }

        return sb.toString();
    }
}
