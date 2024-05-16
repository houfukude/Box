package com.github.tvbox.osc.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.github.tvbox.osc.BuildConfig;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.bean.ApiGithubData;
import com.github.tvbox.osc.ui.activity.HomeActivity;
import com.github.tvbox.osc.util.DefaultConfig;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AboutDialog extends BaseDialog {

    public static final String APK_TYPE = "application/vnd.android.package-archive";

    public static class DownloadItem {
        String downloadURL;
        String fileName;
        /**
         * GitHub Release 页面上的 name 即为版本号 也可以替换成 tag_name
         * 或者<br/>
         * 编译输出目录下的 output-metadata.json 文件作为判断依据
         */
        String version;
        Boolean isNewVersion;

        public DownloadItem(ApiGithubData data) {
            // GitHub Release 页面上的 name 即为版本号 也可以替换成 tag_name
            // this.version = info.tagName;
            this.version = data.tagName;
            try {
                Date current = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.CHINA)
                        .parse(BuildConfig.VERSION_NAME.replace("1.0.", ""));
                Date newVersion = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.CHINA)
                        .parse(this.version);
                if (current != null && newVersion != null) {
                    // 目前 通过对比当前系统的 VERSION_NAME 和 github release 页面上标记的 版本号对比 判断是否为新包
                    this.isNewVersion = current.before(newVersion);
                }
                // 默认第一个发现的 apk 为更新用的apk
                // TODO: 如有根本不同的CPU架构进行分包 则需要细化判断
                for (ApiGithubData.AssetsItem asset : data.assets) {
                    if (APK_TYPE.equals(asset.contentType)) {
                        this.downloadURL = asset.browserDownloadUrl;
                        this.fileName = asset.name;
                        break;
                    }
                }
            } catch (ParseException e) {
                this.isNewVersion = false;
            }

        }
    }

    private DownloadItem downloadItem;

    private final Button aboutUpdateButton;


    public AboutDialog(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.dialog_about);
        aboutUpdateButton = findViewById(R.id.aboutUpdateButton);
        aboutUpdateButton.setEnabled(false);
        aboutUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadItem != null && downloadItem.isNewVersion) {
                    GrantedStoragePermission();
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
        CheckUpdate();
    }

    /**
     * 通过检测 Github release API 来获取更新信息从而实现在线更新
     */
    private void CheckUpdate() {
        aboutUpdateButton.setText(String.format("当前版本：%s", BuildConfig.BUILD_TIME));
        OkGo.<String>get(BuildConfig.SERVER)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        ApiGithubData apiGithubData = new Gson().fromJson(response.body(), ApiGithubData.class);
                        downloadItem = new DownloadItem(apiGithubData);

                        if (downloadItem.isNewVersion) {
                            aboutUpdateButton.setText(String.format("发现新版本：%s 点击更新", downloadItem.version));
                            aboutUpdateButton.setEnabled(true);
                        } else {
                            aboutUpdateButton.setText(String.format("当前版本：%s 已是最新！", BuildConfig.BUILD_TIME));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Toast.makeText(getContext(), "检测更新出错！:" + response.body(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void GrantedStoragePermission() {
        if (XXPermissions.isGranted(getContext(), DefaultConfig.StoragePermissionGroup())) {
            Toast.makeText(getContext(), HomeActivity.getRes().getString(R.string.set_permission_ok), Toast.LENGTH_SHORT).show();
            downloadAndInstall();
        } else {

            XXPermissions.with(getContext())
                    .permission(DefaultConfig.StoragePermissionGroup())
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(@NonNull List<String> permissions, boolean all) {
                            if (all) {
                                Toast.makeText(getContext(), HomeActivity.getRes().getString(R.string.set_permission_ok), Toast.LENGTH_SHORT).show();
                                downloadAndInstall();
                            }
                        }

                        @Override
                        public void onDenied(@NonNull List<String> permissions, boolean never) {
                            if (never) {
                                Toast.makeText(getContext(), HomeActivity.getRes().getString(R.string.set_permission_fail2), Toast.LENGTH_SHORT).show();
                                XXPermissions.startPermissionActivity((Activity) getContext(), permissions);
                            } else {
                                Toast.makeText(getContext(), HomeActivity.getRes().getString(R.string.set_permission_fail1), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    private void downloadAndInstall() {
        aboutUpdateButton.setText(String.format("正在下载：%s", downloadItem.fileName));
        if (downloadItem.downloadURL == null || downloadItem.fileName == null) {
            Toast.makeText(getContext(), "下载错误： 下载地址或文件名为空", Toast.LENGTH_LONG).show();
            aboutUpdateButton.setText(String.format("当前版本：%s", BuildConfig.BUILD_TIME));
            return;
        }
        // /storage/emulated/0/Download/${applicationId}
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + BuildConfig.APPLICATION_ID;
        OkGo.<File>get(downloadItem.downloadURL).tag(downloadItem.version)
                .execute(new FileCallback(root, downloadItem.fileName) {

                    @Override
                    public void onSuccess(Response<File> response) {
                        File file = response.body();
                        if (file != null && file.exists()) {
                            aboutUpdateButton.setText(String.format("正在安装：%s", downloadItem.fileName));
                            InstallApk(file);
                        }
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        aboutUpdateButton.setText(String.format(Locale.getDefault(), "正在下载：%s (%.2f%%)", downloadItem.fileName, progress.fraction * 100));
                    }
                });
    }

    private void InstallApk(File file) {
        // https://developer.android.com/reference/android/content/Intent.html#ACTION_VIEW
        // https://developer.android.com/reference/android/content/Intent.html#setDataAndType(android.net.Uri,%20java.lang.String)
        // https://developer.android.com/reference/android/content/Intent.html#FLAG_ACTIVITY_NEW_TASK
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, APK_TYPE);
        } else {
            intent.setDataAndType(Uri.fromFile(file), APK_TYPE);
        }
        getContext().startActivity(intent);
    }
}