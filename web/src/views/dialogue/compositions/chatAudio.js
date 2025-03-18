import { computed } from "vue";
import useAudioList from "./AudioPlayList";
const audioList = useAudioList()

function getMsgAudio(id, change) {
    const control = audioList.getControl(id, change) 
    return { control }
}

export default { getMsgAudio }