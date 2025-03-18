package org.bigmouth.gpt.xiaozhi.forest;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.DataFile;
import com.dtflys.forest.annotation.Post;
import org.bigmouth.gpt.xiaozhi.entity.asr.SenseVoiceFastApiResult;

@BaseRequest(baseURL = "${sensevoiceFastApiUrl}")
public interface SenseVoiceFastApi {

    @Post(url = "/api/v1/asr")
    SenseVoiceFastApiResult asr(
            @DataFile(value = "files", fileName = "0.wav") byte[] fileData,
            @Body("keys") String keys,
            @Body("language") String language
    );
}
