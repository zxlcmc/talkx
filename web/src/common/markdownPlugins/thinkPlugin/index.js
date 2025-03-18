import './index.scss'
const thinkPlugin = (md, options) => {
    const math_think = (state, startLine) => {
        // 获取当前行的内容
        const pos = state.bMarks[startLine] + state.tShift[startLine];
        const max = state.eMarks[startLine];
        const line = state.src.slice(pos, max).trim();

        if (line !== '<think>') return false;

        let end = startLine;
        while (++end < state.lineMax) {
            const endPos = state.bMarks[end] + state.tShift[end];
            const endMax = state.eMarks[end];
            const endLineContent = state.src.slice(endPos, endMax).trim();
            if (endLineContent === '</think>') break;
        }

        let content = [];
        for (let i = startLine + 1; i < end; i++) {
            const contentPos = state.bMarks[i] + state.tShift[i];
            const contentMax = state.eMarks[i];
            content.push(state.src.slice(contentPos, contentMax).trim());
        }
        const token = state.push('math_think', '', 0);
        token.content = content.join('\n');
        token.map = [startLine, end];

        state.line = end + 1;
        return true;
    }

    // 支持嵌套
    const math_think2 = (state, startLine, endLine, silent) => {
        // 获取起始行内容 
        const lineStart = state.bMarks[startLine] + state.tShift[startLine];
        const lineEnd = state.eMarks[startLine];
        const lineContent = state.src.slice(lineStart, lineEnd).trim();

        if (lineContent !== '<think>') return false;

        // 初始化嵌套层级计数器
        let nestLevel = 1;
        let endTagLine = startLine;

        // 逐行扫描直到找到匹配的闭合标签
        while (++endTagLine < state.lineMax) {
            const currentLineStart = state.bMarks[endTagLine] + state.tShift[endTagLine];
            const currentLineEnd = state.eMarks[endTagLine];
            const currentLineContent = state.src.slice(currentLineStart, currentLineEnd).trim();
            if (currentLineContent === '<think>') {
                console.log('lv', nestLevel);
                nestLevel++; // 遇到嵌套标签，层级+1 
            } else if (currentLineContent === '</think>') {
                nestLevel--; // 遇到闭合标签，层级-1
                if (nestLevel === 0) break; // 层级归零时找到匹配的闭合标签
            }
        }

        // 提取标签间内容（包含嵌套标签）
        let content = [];
        for (let i = startLine + 1; i < endTagLine; i++) {
            const contentLineStart = state.bMarks[i] + state.tShift[i];
            const contentLineEnd = state.eMarks[i];
            content.push(state.src.slice(contentLineStart, contentLineEnd));
        }

        // 创建 token 并传递内容
        const token = state.push('math_think', '', 0);
        token.content = content.join('\n');
        token.map = [startLine, endTagLine];

        // 跳转到闭合标签下一行继续解析
        state.line = endTagLine + 1;
        return true;
    };

    const thinkRenderer = function (tokens, idx) {
        const token = tokens[idx];
        // 不允许嵌套, 把<think>实体化
        let content = token.content.replace(/<think>/g, '&lt;think&gt;').replace(/<\/think>/g, '&lt;/think&gt;')
        content = md.render(content)
        return `<div class="custom-think active">
            <div class="think-header">
                <div class="text">已深度思考</div>
                <div class="icon">
                    <svg viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M3.14645 5.64645C3.34171 5.45118 3.65829 5.45118 3.85355 5.64645L8 9.79289L12.1464 5.64645C12.3417 5.45118 12.6583 5.45118 12.8536 5.64645C13.0488 5.84171 13.0488 6.15829 12.8536 6.35355L8.35355 10.8536C8.15829 11.0488 7.84171 11.0488 7.64645 10.8536L3.14645 6.35355C2.95118 6.15829 2.95118 5.84171 3.14645 5.64645Z" fill="currentColor"></path></svg>
                </div>
            </div>
            <div class="think-wrapper"><blockquote>${content}</blockquote></div>  
        </div>`;
    }
    md.block.ruler.after('blockquote', 'math_think', math_think2, {
        alt: ['paragraph', 'reference', 'blockquote', 'list']
    });

    md.renderer.rules.math_think = thinkRenderer;
}

export function thinkHeaderClick(e) {
    const btn = e.target;
    const parent = btn.parentElement
    parent.classList.toggle('active')
}

export default thinkPlugin