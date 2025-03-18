import confetti from 'canvas-confetti'

/**
 * 话题切换-移动放大缩小
 * @param {*} moveRef 移动元素ref
 * @param {*} targetRef 移动位置目标元素ref
 * @param {*} mtime 移动过程段时间
 * @returns 
 */
export const moveAnimate = async (moveRef, targetRef, mtime = 300) => {
    if (!(moveRef.value && targetRef.value)) return

    const mRect = moveRef.value.getBoundingClientRect()
    const tRect = targetRef.value.getBoundingClientRect()

    const mcenter = { x: mRect.left + mRect.width / 2, y: mRect.top + mRect.height / 2 }
    const tcenter = { x: tRect.left + tRect.width / 2, y: tRect.top + tRect.height / 2 }

    const mt = { x: tcenter.x - mcenter.x, y: tcenter.y - mcenter.y }

    // 先放大在缩小
    const animate = async () => {
        await new Promise(c => {
            moveRef.value.style.transition = `transform ${mtime}ms`
            moveRef.value.style.transform = `translate(0px, 0px) scale(1.05)`
            moveRef.value.addEventListener('transitionend', () => {
                c()
            })
        })

        await new Promise(c => {
            moveRef.value.style.transition = `transform ${mtime}ms`
            moveRef.value.style.transform = `translate(${mt.x}px, ${mt.y}px) scale(0.05)`
            moveRef.value.addEventListener('transitionend', () => {
                moveRef.value.style.transition = 'none'
                moveRef.value.style.opacity = '0'
                c()
            }, { once: true })
        })
    }

    await animate()


    // 复原
    const recover = () => {
        moveRef.value.style.transition = 'none'
        moveRef.value.style.transform = 'none'
        moveRef.value.style.opacity = '1'
    }

    return { recover }
}


let confettiObj
/**
 * 彩带播放
 * @param {*} moveRef 喷发位置元素ref， 取底部居中 
 * @returns 
 */
export const colorBarBottomCenter = (moveRef) => {
    if (!(moveRef.value)) return

    var triangle = confetti.shapeFromPath({ path: 'M0 10 L5 0 L10 10z' });

    const mRect = moveRef.value.getBoundingClientRect()
    const mbc = { x: mRect.left + mRect.width / 2, y: mRect.top + mRect.height }
    const origin = { x: mbc.x / window.innerWidth, y: mbc.y / window.innerHeight }

    confettiObj = confetti({
        shapes: [triangle],
        origin
    })

    return confettiObj
}


/**
 * 话题切换-彩带动画
 * @param {*} moveRef 
 * @returns 
 */
export const moveColorBar = async (moveRef) => {

    // colorBarBottomCenter(moveRef)

    // 隐藏 moveRef
    moveRef.value.style.opacity = '0'

    const recover = () => {
        moveRef.value.style.opacity = '1'
    }


    return { recover }
}