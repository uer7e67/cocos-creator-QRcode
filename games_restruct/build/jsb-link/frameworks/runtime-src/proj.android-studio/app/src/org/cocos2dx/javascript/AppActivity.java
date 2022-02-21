/****************************************************************************
Copyright (c) 2015-2016 Chukong Technologies Inc.
Copyright (c) 2017-2018 Xiamen Yaji Software Co., Ltd.
 
http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/
package org.cocos2dx.javascript;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.cocos2dx.lib.Cocos2dxJavascriptJavaBridge;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.content.res.Configuration;
import android.provider.Settings;
import android.util.Log;

import com.huawei.hms.hmsscankit.ScanKitActivity;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;

public class AppActivity extends Cocos2dxActivity {

    private static Activity mContext;
    private static int REQUEST_CODE_SCAN_ONE = 111;
    private static String RCODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Workaround in
        // https://stackoverflow.com/questions/16283079/re-launch-of-activity-on-home-button-but-only-the-first-time/16447508
        if (!isTaskRoot()) {
            // Android launched another instance of the root activity into an existing task
            // so just quietly finish and go away, dropping the user back into the activity
            // at the top of the stack (ie: the last state of this task)
            // Don't need to finish it again since it's finished in super.onCreate .
            return;
        }
        // DO OTHER INITIALIZATION BELOW
        SDKWrapper.getInstance().init(this);
        mContext = this;
    }

    @Override
    public Cocos2dxGLSurfaceView onCreateView() {
        Cocos2dxGLSurfaceView glSurfaceView = new Cocos2dxGLSurfaceView(this);
        // TestCpp should create stencil buffer
        glSurfaceView.setEGLConfigChooser(5, 6, 5, 0, 16, 8);
        SDKWrapper.getInstance().setGLSurfaceView(glSurfaceView, this);

        return glSurfaceView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SDKWrapper.getInstance().onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        SDKWrapper.getInstance().onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Workaround in https://stackoverflow.com/questions/16283079/re-launch-of-activity-on-home-button-but-only-the-first-time/16447508
        if (!isTaskRoot()) {
            return;
        }

        SDKWrapper.getInstance().onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SDKWrapper.getInstance().onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_CODE_SCAN_ONE) && (resultCode == Activity.RESULT_OK)) {
            HmsScan obj = data.getParcelableExtra(ScanUtil.RESULT);
            if (obj != null && obj.getOriginalValue() != null) {
                String res = obj.getOriginalValue();
                Log.d("ddd", "onActivityResult: 扫描结果：" + res);
                RCODE = res;
                this.sendReward();
            }
        }
    }

    /**返回结果*/
    public void sendReward() {
        this.runOnGLThread(new Runnable() {
            @Override
            public void run() {
                String js = "NativeHelper.message({name:";
                js += "'" + "scan" + "',";
                js += "value:'" + RCODE + "'})";
                Log.d("ddd", "run: " + js);
                Cocos2dxJavascriptJavaBridge.evalString(js);
            }
        });
    }

    public static void test() {
        Log.d("tag", "this is android ...");
    }

    /**扫描二维码 */
    public static void scan() {
        // CAMERA_REQ_CODE为用户自定义，用于接收权限校验结果
        Intent intent = new Intent(mContext, ScanKitActivity.class);
        mContext.startActivityForResult(intent, REQUEST_CODE_SCAN_ONE);
        Log.d("tag", "scan: ...................");
    }

    /**获取设备id */
    public static String device() {
        String android_id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("tag", "设备id: ====" + android_id);
        return android_id;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SDKWrapper.getInstance().onNewIntent(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SDKWrapper.getInstance().onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SDKWrapper.getInstance().onStop();
    }

    @Override
    public void onBackPressed() {
        SDKWrapper.getInstance().onBackPressed();
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        SDKWrapper.getInstance().onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        SDKWrapper.getInstance().onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        SDKWrapper.getInstance().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        SDKWrapper.getInstance().onStart();
        super.onStart();
    }
}
