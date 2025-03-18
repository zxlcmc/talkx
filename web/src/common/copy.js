// import Clipboard from "clipboard"
// import { v4 as uuidv4 } from 'uuid'

// export function copyToClip(text) {
//   return new Promise((resolve, reject) => {
//     try {

//       var input = document.createElement("textarea");
//       input.setAttribute("readonly", "readonly"); // 防止手机上弹出软键盘
//       input.setAttribute("value", text);
//       document.body.appendChild(input);
//       // input.setSelectionRange(0, 9999);
//       input.select();
//       var res = document.execCommand("copy");
//       document.body.removeChild(input);
//       resolve(true)

//       // const select = "copy" + uuidv4()
//       // const btn = document.createElement('div')
//       // btn.className = select
//       // console.log('text', text);
//       // btn.setAttribute('data-clipboard-text', text)
//       // btn.onclick = () => {
//       //   const clipboard = new Clipboard(`.${select}`)
//       //   clipboard.on('success', e => {
//       //     e.clearSelection(); //清除选中
//       //     clipboard.destroy() //释放内存 
//       //     btn.remove()
//       //     resolve(true)
//       //   })
//       //   clipboard.on('error', e => {
//       //     console.log('复制失败', e);
//       //     reject(e)
//       //   })
//       // }
//       // document.body.appendChild(btn)
//       // btn.click() 
//     }
//     catch (error) {
//       reject(error)
//     }
//   })
// }


export function copyToClip(text) {
  return new Promise((resolve, reject) => {
    try {
      const input = document.createElement('textarea')
      input.setAttribute('readonly', 'readonly')
      input.value = text
      document.body.appendChild(input)
      input.select()
      if (document.execCommand('copy'))
        document.execCommand('copy')
      document.body.removeChild(input)
      resolve(true)
    }
    catch (error) {
      reject(error)
    }
  })
}