package com.kappacrypto.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Crypto {
    public String name;
    @JsonProperty ("asset_id")
    public String assetId;

    @JsonProperty ("type_is_crypto")
    public int typeIsCrypto;

    // TODO: Fix object mapper date formatter
//    @JsonProperty ("data_quote_start")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
//    public Date dataQuoteStart;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
//    @JsonProperty ("data_quote_end")
//    public Date dataQuoteEnd;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
//    @JsonProperty ("data_orderbook_start")
//    public Date dataOrderbookStart;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
//    @JsonProperty ("data_orderbook_end")
//    public Date dataOrderbookEnd;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
//    @JsonProperty ("data_trade_start")
//    public Date dataTradeStart;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
//    @JsonProperty ("data_trade_end")
//    public Date dataTradeEnd;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
//    @JsonProperty ("data_symbols_count")
//    public int datasSmbolsCount;
//

    @JsonProperty ("volume_1day_usd")
    public Long volume1DayUsd;

    @JsonProperty ("volume_1mth_usd")
    public Long volume1MthUsd;

    @JsonProperty ("volume_1hrs_usd")
    public Long volume1HrUsd;

    @JsonProperty ("price_usd")
    public int priceUsd;

    @JsonProperty ("id_icon")
    public String idIcon;

    @JsonProperty ("data_start")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date dataStart;

    @JsonProperty ("data_end")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date dataEnd;

    public String iconUrl;

    public void setAssetIcon(String iconUrl) {
        // TODO: Set Icon Url
        this.iconUrl = iconUrl;
    }

    @Override
    public String toString() {
        return "Name: " + this.name
                + "\nAsset Id: " + this.assetId
                + "\n1 day vol: " + this.volume1DayUsd;
    }
}
