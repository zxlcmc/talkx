package org.bigmouth.gpt.entity.response;

import lombok.Data;
import org.bigmouth.gpt.entity.CoinCatalog;

import java.math.BigDecimal;

/**
 * @author huxiao
 * @date 2023/11/3
 * @since 1.0.0
 */
@Data
public class CoinCatalogVo {

    private Long catalogId;

    private BigDecimal coins;

    /**
     * 实际售价
     */
    private BigDecimal price;

    /**
     * 市场售价
     */
    private BigDecimal marketPrice;

    /**
     * 库存
     */
    private Integer stocks;

    public CoinCatalogVo() {
    }

    public CoinCatalogVo(CoinCatalog e) {
        this.catalogId = e.getId();
        this.coins = e.getCoins();
        this.price = e.getPrice();
        this.marketPrice = e.getMarketPrice();
        this.stocks = e.getStocks();
    }
}
