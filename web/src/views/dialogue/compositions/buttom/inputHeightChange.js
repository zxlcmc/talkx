import { onMounted, onBeforeUnmount, watch } from 'vue'


/**
 * textarea 输入框高度自适应
 * @param {*} textareaRef 
 * @param {*} text  
 */
export default function inputHeightChange(textareaRef, text) {
    const getInputWrapper = () => {
        if (!textareaRef.value) return;
        const wrapper = textareaRef.value.$el.querySelector(".n-input-wrapper");
        const textEa = wrapper.querySelector(".n-input__textarea-el");
        return { wrapper, textEa };
    };

    const heightChange = () => {
        if (!textareaRef.value) return;
        const { wrapper, textEa } = getInputWrapper();
        let h = textEa.scrollHeight;
        if (text.value.length <= 0) {
            h = 0;
        }
        h = h - 18;
        h = h < 62 ? 62 : h;
        wrapper.style.height = h + "px";
        textEa.style.height = h + "px";
    };

    const watchTextareaHeight = () => {
        if (!textareaRef.value) return;
        const { wrapper, textEa } = getInputWrapper();
        textEa.addEventListener("input", heightChange);
    };

    const removeWatchTextareaHeight = () => {
        const { wrapper, textEa } = getInputWrapper();
        textEa.removeEventListener("input", heightChange);
    };

    watch(text, () => heightChange())

    onMounted(() => { watchTextareaHeight() })
    onBeforeUnmount(() => { removeWatchTextareaHeight() })
}