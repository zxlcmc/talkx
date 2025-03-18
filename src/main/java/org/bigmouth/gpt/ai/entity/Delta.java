package org.bigmouth.gpt.ai.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delta {
   private String content;
   private String role;
   @JSONField(name = "function_call")
   private FunctionCall functionCall;
}
