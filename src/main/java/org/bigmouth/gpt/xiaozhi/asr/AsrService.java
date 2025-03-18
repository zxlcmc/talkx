package org.bigmouth.gpt.xiaozhi.asr;

public interface AsrService {

    AsrType of();

    String asr(String key, byte[] bytes) throws Exception;
}
