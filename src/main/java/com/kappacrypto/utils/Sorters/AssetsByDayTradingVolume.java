package com.kappacrypto.utils.Sorters;

import com.kappacrypto.Models.Crypto;

import java.util.Comparator;

public class AssetsByDayTradingVolume implements Comparator<Crypto> {

    @Override
    public int compare(Crypto o1, Crypto o2) {
        Long diff = o2.volume1DayUsd - o1.volume1DayUsd;
        if (diff == 0) return 0;

        return diff < 0 ? -1 : 1;
    }
}
