package org.bigmouth.gpt.xiaozhi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OtaRequest {
    private Integer version;
    @JsonProperty("flash_size")
    private Long flashSize;
    @JsonProperty("minimum_free_heap_size")
    private Long minimumFreeHeapSize;
    @JsonProperty("mac_address")
    private String macAddress;
    private String uuid;
    @JsonProperty("chip_model_name")
    private String chipModelName;
    @JsonProperty("chip_info")
    private ChipInfo chipInfo;
    private Application application;
    @JsonProperty("partition_table")
    private List<PartitionTable> partitionTable;
    private Ota ota;
    private Board board;

    @Data
    public static class ChipInfo {
        private Integer model;
        private Integer cores;
        private Integer revision;
        private Integer features;
    }

    @Data
    public static class Application {
        private String name;
        private String version;
        @JsonProperty("compile_time")
        private String compileTime;
        @JsonProperty("idf_version")
        private String idfVersion;
        @JsonProperty("elf_sha256")
        private String elfSha256;
    }

    @Data
    public static class PartitionTable {
        private String label;
        private Integer type;
        private Integer subtype;
        private Long address;
        private Long size;
    }

    @Data
    public static class Ota {
        private String label;
    }

    @Data
    public static class Board {
        private String type;
        private String ssid;
        private Integer rssi;
        private Integer channel;
        private String ip;
        private String mac;
    }
}