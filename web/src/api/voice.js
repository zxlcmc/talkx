import server from './require'


/**
 * 文本转语音
 * @returns 
 */
export const textToVoice = async (data) => {
    return server({
        url: '/voice/text_to_speech',
        method: "POST",
        RawData: true,
        responseType: 'blob', 
        data
    })
} 

/**
 * 语音转文本
 * @returns 
 */
export const voiceToText = async (data) => {
    return server({
        url: '/voice/speech_to_text',
        method: "POST",
        type: 'file',
        'Content-type': 'multipart/form-data;',
        // responseType: 'arraybuffer', 
        data
    })
}