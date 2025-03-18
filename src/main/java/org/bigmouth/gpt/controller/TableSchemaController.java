package org.bigmouth.gpt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bxm.warcar.utils.StringHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bigmouth.gpt.entity.TableSchema;
import org.bigmouth.gpt.entity.TableSchemaVo;
import org.bigmouth.gpt.entity.map.TableSchemaMapper;
import org.bigmouth.gpt.interceptor.ContextFactory;
import org.bigmouth.gpt.service.ITableSchemaService;
import org.hibernate.validator.constraints.Length;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author huxiao
 * @date 2024/4/15
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/table_schema")
public class TableSchemaController {

    private final ITableSchemaService tableSchemaService;

    public TableSchemaController(ITableSchemaService tableSchemaService) {
        this.tableSchemaService = tableSchemaService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<TableSchemaVo>> list() {
        Long userId = ContextFactory.getLoginUser().getId();
        this.createIfEmpty(userId);
        List<TableSchema> tableSchemaList = tableSchemaService.list(new QueryWrapper<>(new TableSchema().setUserId(userId)));
        return ResponseEntity.ok(TableSchemaMapper.INSTANCE.of(tableSchemaList));
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody @Validated DeleteReq req) {
        Long userId = ContextFactory.getLoginUser().getId();
        TableSchema tableSchema = tableSchemaService.getById(req.getId());
        if (tableSchema == null || !tableSchema.getUserId().equals(userId)) {
            return ResponseEntity.badRequest().build();
        }
        tableSchemaService.removeById(req.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save_or_update")
    public ResponseEntity<Object> save(@RequestBody @Validated SaveReq req) {
        Long userId = ContextFactory.getLoginUser().getId();
        if (Objects.isNull(req.getId())) {
            TableSchema tableSchema = new TableSchema();
            tableSchema.setUserId(userId);
            tableSchema.setName(req.getName());
            tableSchema.setSqlType(req.getSqlType());
            tableSchema.setSchema(req.getSchema());
            tableSchemaService.save(tableSchema);
            return ResponseEntity.ok(tableSchema);
        } else {
            TableSchema tableSchema = tableSchemaService.getById(req.getId());
            if (tableSchema == null || !tableSchema.getUserId().equals(userId)) {
                return ResponseEntity.badRequest().build();
            }
            tableSchema.setName(req.getName());
            tableSchema.setSqlType(req.getSqlType());
            tableSchema.setSchema(req.getSchema());
            tableSchema.setModifyTime(LocalDateTime.now());
            tableSchemaService.updateById(tableSchema);
            return ResponseEntity.ok(tableSchema);
        }
    }

    private void createIfEmpty(Long userId) {
        if (tableSchemaService.countWithoutDeleted(userId) > 0) {
            return;
        }
        byte[] bytes = readFully("table-schema-example.sql");
        if (ArrayUtils.isNotEmpty(bytes)) {
            String metadata = StringHelper.convert(bytes);
            TableSchema tableSchema = new TableSchema();
            tableSchema.setUserId(userId);
            tableSchema.setName("example-table-metadata");
            tableSchema.setSqlType("MySQL");
            tableSchema.setSchema(metadata);
            tableSchema.setConversactionStart("查询某个客户购买过的所有产品及数量,查询某个销售人员在特定日期范围内的销售总额,查询某个产品的供应商及供货价格,查询某个地理销售区域的销售额排名");
            tableSchemaService.save(tableSchema);
        }
    }

    public static byte[] readFully(String classpath) {
        ClassPathResource resource = new ClassPathResource(classpath);
        try {
            InputStream inputStream = resource.getInputStream();
            return IOUtils.readFully(inputStream, inputStream.available());
        } catch (IOException e) {
            log.warn("readFully: {}", e.getMessage());
            return null;
        }
    }

    @Data
    public static class SaveReq {

        private Long id;

        /**
         * 表结构名称
         */
        @NotBlank(message = "表结构名称不能为空")
        @Length(max = 32, message = "表结构名称长度不能超过32")
        private String name;

        /**
         * SQL语言类型
         */
        @NotBlank(message = "SQL语言类型不能为空")
        @Length(max = 32, message = "SQL语言类型长度不能超过32")
        private String sqlType;

        @NotBlank(message = "表结构信息不能为空")
        @Length(max = 8192, message = "表结构信息长度不能超过32")
        private String schema;
    }

    @Data
    public static class DeleteReq {
        @NotNull(message = "ID不能为空")
        private Long id;
    }
}
