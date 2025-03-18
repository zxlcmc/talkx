package org.bigmouth.gpt.xiaozhi;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Allen Hu
 * @date 2025/2/27
 */
@Data
@Accessors(chain = true)
public class OtaBindInf {
    private OtaRequest request;
    private OtaResponse response;
}
