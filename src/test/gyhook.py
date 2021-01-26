'''
Descripttion:
version:
Author: Leo
Date: 2021-01-26 17:57:49
LastEditor: Leo
LastEditTime: 2021-01-26 22:50:54
'''
import frida
from flask import Flask, request


# 定义消息处理
def on_message_handler(message, payload):
    print(f"payload: {payload}")
    print(f"message: {message}")


js_code = '''
rpc.exports = {
    hookSig: function(deviceKey) {
        var result;
        Java.perform(function () {
        var class_old = Java.use("com.tgc.sky.SystemIO_android");
        result = class_old.getInstance().SignWithDeviceKey(deviceKey);
    });
    return result;
    }
}
'''

# 连接安卓机上的frida-server
device = frida.get_usb_device()
pid = device.spawn(["com.netease.sky"])
device.resume(pid)
session = device.attach(pid)
script = session.create_script(js_code)
script.on("message", on_message_handler)  # 调用错误处理
script.load()


app = Flask(__name__)


@ app.route('/getsig', methods=['GET', 'POST'])
def hook_sig():
    device_key = request.args.get("device_key")
    sig_ts = request.args.get("sig_ts")
    print("===================================================================")
    print("param: " + device_key+sig_ts)
    res = script.exports.hook_sig(device_key+sig_ts)
    print("result: " + res)
    print("===================================================================")
    return res


if __name__ == '__main__':
    app.run()
