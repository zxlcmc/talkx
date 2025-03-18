package org.bigmouth.gpt.xiaozhi.forest;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/3/17
 */
@Configuration
@ForestScan({
        "org.bigmouth.gpt.xiaozhi.forest"
})
public class ForestScanAutoConfigurer {
}
