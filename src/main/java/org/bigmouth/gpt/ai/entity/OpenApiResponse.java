package org.bigmouth.gpt.ai.entity;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiResponse {

   private String id;
   private String object;
   private String created;
   private String model;
   private List<Choice> choices = Lists.newArrayList();
   private Usage usage;
}
