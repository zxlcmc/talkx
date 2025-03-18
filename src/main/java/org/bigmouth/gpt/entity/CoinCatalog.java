package org.bigmouth.gpt.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author allen
 * @since 2023-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("coin_catalog")
public class CoinCatalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;


    public static final String ID = "id";

    public static final String COINS = "coins";

    public static final String PRICE = "price";

    public static final String MARKET_PRICE = "market_price";

    public static final String STOCKS = "stocks";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

}
