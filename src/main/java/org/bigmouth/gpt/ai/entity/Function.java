package org.bigmouth.gpt.ai.entity;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author huxiao
 * @date 2023/9/27
 * @since 1.0.0
 */
@Data
public class Function {

    /**
     * The name of the function to be called. Must be a-z, A-Z, 0-9, or contain underscores and dashes, with a maximum length of 64.
     */
    private String name;
    /**
     * A description of what the function does, used by the model to choose when and how to call the function.
     */
    private String description;

    /**
     * The parameters the functions accepts, described as a JSON Schema object. See the guide for examples, and the JSON Schema reference
     * for documentation about the format.
     * <br>
     * <br>
     * To describe a function that accepts no parameters, provide the value <code>{"type": "object", "properties": {}}</code>.
     */
    private Parameters parameters;
    /**
     * required parameters
     */
    private List<String> required;

    public static Function ofEcharts() {
        Function function = new Function();

        function.setName("create_javascript_echarts");
        function.setDescription("调用这个方法来创建一个echarts图表所需要的option参数");

        Parameters parameters1 = new Parameters();

        Map<String, Properties> properties = parameters1.getProperties();
        properties.put("option", new Properties().setType("object").setDescription("创建JavaScript语言的echarts图表所需要的option参数"));

        function.setParameters(parameters1);
        function.setRequired(Lists.newArrayList("option"));

        return function;
    }
}
