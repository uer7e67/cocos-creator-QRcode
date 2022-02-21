const { ccclass, property } = cc._decorator;

// @ts-ignore
window.NativeHelper = {
    message: function(msg) {
        if(msg) {
            console.log(JSON.stringify(msg))
        }
    }
}

@ccclass
export default class NewClass extends cc.Component {

    @property(cc.Label)
    tip: cc.Label = null;

    private socketIO = null;

    onLoad() {
        this.tip.string = "onload";
    }

    // 测试
    scanQR() {
        let msg = "cocos msg ....";
        if (cc.sys.isNative && cc.sys.os == cc.sys.OS_ANDROID) {
            console.log(msg)
            jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AppActivity", "scan", "()V");
        }
    }

    // 原生功能桥 
    test() {
        let msg = "cocos msg ....";
        if (cc.sys.isNative && cc.sys.os == cc.sys.OS_ANDROID) {
            console.log(msg)
            jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AppActivity", "test", "()V");
        }
    }

    getDeviceId() {
        let msg = "get device id ....";
        if (cc.sys.isNative && cc.sys.os == cc.sys.OS_ANDROID) {
            console.log(msg)
            let device = jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AppActivity", "device", "()Ljava/lang/String;");
            this.tip.string = "设备id:" + device;
        }
    }


}
