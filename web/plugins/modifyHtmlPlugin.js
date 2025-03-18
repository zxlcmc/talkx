
import fs from 'fs'
import path from 'path'
import jsdom from 'jsdom'

function modifyHtmlPlugin() {
    return {
        name: 'modify-html-plugin',
        apply: 'build',
        async closeBundle() {
            const { JSDOM } = jsdom
            const __VERSION__ = process.env.npm_package_version
            const htmlPath = path.resolve(__dirname, '../dist/index.html'); // HTML文件的路径
            const htmlContent = fs.readFileSync(htmlPath, 'utf-8'); // 读取HTML文件内容 
            const dom = new JSDOM(htmlContent)
            const head = dom.window.document.head;
            const v = Math.random()
            const bundleJSName = `bundle.${__VERSION__}.js`
            const script = head.querySelector(`script[src="./assets/${bundleJSName}"]`)
            script.setAttribute('src', `./assets/${bundleJSName}?v=${v}`)
            const link = head.querySelector(`link[href="./assets/index.${__VERSION__}.css"]`)
            link.setAttribute('href', `./assets/index.${__VERSION__}.css?v=${v}`)
            fs.writeFileSync(htmlPath, dom.serialize(), 'utf-8'); // 将修改后的内容写入HTML文件

            // 对应其他chunkjs中引用的 bundle.xx.js 加上v

            const assets = path.join(__dirname, '../dist/assets')
            const res = fs.readdirSync(assets)
            const jsArr = res.filter(name => /\.js$/.test(name) && name != bundleJSName)
            for (let i = 0; i < jsArr.length; i++) {
                const jsPath = path.join(assets, jsArr[i])
                const jsFile = fs.readFileSync(jsPath, 'utf-8');
                const str = jsFile.replaceAll(bundleJSName, `${bundleJSName}?v=${v}`)
                fs.writeFileSync(jsPath, str, 'utf-8'); // 将修改后的内容写入HTML文件
            }
        }
    };
}

export default modifyHtmlPlugin;