package com.shinho.android.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shinho.android.utils.OnPickAvatarListener;
import com.shinho.android.utils.OnPickImageListener;
import com.shinho.android.utils.StringUtils;

import java.util.Arrays;


/**
 * 对话框工具类, 提供常用对话框显示, 使用support.v7包内的AlertDialog样式
 */
public class DialogUtils {

    public static Dialog createProgressDialog(Context context) {
        return createProgressDialog(context, false);
    }

    public static Dialog createProgressDialog(Context context, boolean needCancel) {
        LoadingDialog dialog = new LoadingDialog(context);
        dialog.setCancelable(needCancel);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     *退出页面填写丢失提示框
     */
    public static Dialog showAbandonInputDialog(Activity activity) {
        return show2BtnDialog(activity, "提示",
                "本页面内容尚未提交，离开页面将不保存本次填写内容",
                true, "确定离开", "继续填写",
                v -> activity.finish(), null);
    }

//    /** sfa:用于更新
//     * 更新对话框
//     */
//    public static void checkShowUpdateDialog(Context context) {
//        UpdateInfo info = UpdateHelper.appUpdateInfo;
//        if (info != null && (info.isMustUpdate() || !UpdateHelper.appUpdateDialogTempSilence)) {
//            // 有更新信息，且是强制更新 or 非强更但尚未点击过下次提醒，此时提示对话框
//            AppUpdateDialogActivity.start(context);
//        }
//    }

//    public static void checkShowUpdateDialog(Context context) {
//        UpdateInfo info = UpdateHelper.appUpdateInfo;
//        if (info != null) {
//            // 直接跳转到下载网页，不在客户端下载
//            // File apkFile = UpdateHelper.getDownloadApkFile(info);
//            // if (!apkFile.exists()) return;
//
//            Log.i("DDD", "checkUpdate: show update dialog");
//
//            boolean forceUpdate = info.getMustUpdate() == 1;
//            String title = "有新版本 " + info.getVersionName();
//            String content = info.getNote();
//            Dialog dialog = DialogUtils.show2BtnDialog(context, title, content, !forceUpdate,
//                    forceUpdate ? "退出" : "下次提醒", "立即更新",
//                    v -> {
//                        // 强制更新的话，直接退出app
//                        if (forceUpdate) {
//                            ExitActivity.exit(context);
//                        } else {
//                            UpdateHelper.appUpdateInfo = null;
//                        }
//                    },
//                    v -> {
//                        String downloadUrl = info.getLinkUrl();
//                        AppUtils.openBrowser(context,
//                                downloadUrl,
//                                "请选择浏览器软件打开应用更新下载页面",
//                                "手机未安装浏览器软件，无法打开应用更新下载页面");
//                    });
//            dialog.setCancelable(false);
//        }
//    }

//    /**
//     *sfa:是否保存草稿对话框
//     */
//    public static Dialog showSaveDraftDialog(Activity activity, InputContainer container, String draftKey) {
//        return show2BtnDialog(activity, "保存草稿",
//                "请问是否保存草稿，放弃编辑将不保存本次填写内容",
//                true, "放弃编辑", "保存草稿",
//                v -> {
//                    // 放弃编辑时清空草稿
//                    container.clearDraft(draftKey);
//                    activity.finish();
//                },
//                v -> {
//                    // 关闭页面判断时已经先保存了草稿，所以这里不做操作
//                    activity.finish();
//                });
//    }

    /**
     * 是否保存草稿对话框
     */
    public static Dialog showSaveDraftDialog(Activity activity, View.OnClickListener abandonListener, View.OnClickListener saveListener) {
        return show2BtnDialog(activity, "保存草稿",
                "请问是否保存草稿，放弃编辑将不保存本次填写内容",
                true, "放弃编辑", "保存草稿",
                abandonListener, saveListener);
    }


    /**
     * 选择拍照还是相册对话框
     */
    public static Dialog showImagePickDialog(Context context, OnPickImageListener listener) {
        BottomSelectDialog dialog = new BottomSelectDialog(context,
                Arrays.asList(R.drawable.ic_camera, R.drawable.ic_albums),
                Arrays.asList("拍照", "相册"),
                (parent, view, position, id) -> {
                    if (listener == null) return;

                    if (position == 0) {
                        listener.onCamera();
                    } else if (position == 1) {
                        listener.onAlbum();
                    }
                });
        dialog.show();
        return dialog;
    }




    public static Dialog showMessageControlDialog(Context context, AdapterView.OnItemClickListener listener) {
        View view = View.inflate(context, R.layout.dialog_message_control, null);
        FBottomDialog dialog = new FBottomDialog(context);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        view.findViewById(R.id.ll_read_message).setOnClickListener(v -> {
            dialog.dismiss();
            if (listener != null) listener.onItemClick(null, null, 0, -1);
        });
        view.findViewById(R.id.ll_delete_message).setOnClickListener(v -> {
            dialog.dismiss();
            if (listener != null) listener.onItemClick(null, null, 1, -1);
        });
        view.findViewById(R.id.message_control_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();

        return dialog;
    }

    /**
     * 选择头像对话框
     */
    public static Dialog showAvatarPickDialog(Context context, OnPickAvatarListener listener) {
        BottomSelectDialog dialog = new BottomSelectDialog(context,
                Arrays.asList(R.drawable.ic_camera, R.drawable.ic_albums, R.drawable.ic_local_image),
                Arrays.asList("拍照", "相册", "预设"),
                (parent, view, position, id) -> {
                    if (listener == null) return;

                    if (position == 0) {
                        listener.onCamera();
                    } else if (position == 1) {
                        listener.onAlbum();
                    } else if(position == 2) {
                        listener.onLocalAlbum();
                    }
                });
        dialog.show();
        return dialog;
    }


    /**
     * 自定义确定按钮/取消对话框
     */
    public static Dialog show2BtnDialog(@NonNull Context context, @NonNull String title, String content, String ok, View.OnClickListener okListener) {
        return show2BtnDialog(context, title, content, true, "取消", ok, null, okListener);
    }

    /**
     * 确定/取消对话框
     */
    public static Dialog show2BtnDialog(@NonNull Context context, @NonNull String title, String content, View.OnClickListener okListener) {
        return show2BtnDialog(context, title, content, true, "取消", "确定", null, okListener);
    }

    /**
     * 俩按钮对话框
     */
    public static Dialog show2BtnDialog(@NonNull Context context,
                                        @NonNull String title,
                                        String content,
                                        boolean autoClose,
                                        String cancel,
                                        String ok,
                                        View.OnClickListener cancelListener,
                                        View.OnClickListener okListener) {
        View view = View.inflate(context, R.layout.dialog_common, null);
        DialogViewHolder holder = new DialogViewHolder(view);

        Dialog dialog = new Dialog(context, R.style.FDialog);
        dialog.setContentView(view);
        holder.tv_title.setText(title);
        if (StringUtils.isEmpty(content)) {
            holder.tv_content.setVisibility(View.GONE);
        } else {
            holder.tv_content.setVisibility(View.VISIBLE);
            holder.tv_content.setText(content);
        }
        if (cancel != null) holder.tv_cancel.setText(cancel);
        holder.tv_cancel.setOnClickListener(v -> {
            if(autoClose) dialog.dismiss();
            if (cancelListener != null) cancelListener.onClick(v);
        });
        if (ok != null) holder.tv_ok.setText(ok);
        holder.tv_ok.setOnClickListener(v -> {
            if(autoClose) dialog.dismiss();
            if (okListener != null) okListener.onClick(v);
        });
        dialog.show();

        return dialog;
    }

    /**
     * 单按钮对话框
     */
    public static Dialog show1BtnDialog(Context context, String title, String content) {
        return show1BtnDialog(context, title, content, "确定", null);
    }

    /**
     * 单按钮对话框
     */
    public static Dialog show1BtnDialog(Context context, String title, String content,
                                        String ok, View.OnClickListener okListener) {
        View view = View.inflate(context, R.layout.dialog_common, null);
        DialogViewHolder holder = new DialogViewHolder(view);
        holder.btnDivider.setVisibility(View.GONE);
        holder.tv_cancel.setVisibility(View.GONE);

        Dialog dialog = new Dialog(context, R.style.FDialog);
        dialog.setContentView(view);
        holder.tv_title.setText(title);
        if (StringUtils.isEmpty(content)) {
            holder.tv_content.setVisibility(View.GONE);
        } else {
            holder.tv_content.setVisibility(View.VISIBLE);
            holder.tv_content.setText(content);
        }
        if (ok != null) holder.tv_ok.setText(ok);
        holder.tv_ok.setOnClickListener(v -> {
            dialog.dismiss();
            if (okListener != null) okListener.onClick(v);
        });
        dialog.show();

        return dialog;
    }

    /**
     * 默认成功Alert对话框
     */
    public static Dialog showSuccessAlertDialog(Context context, String title) {
        return showSuccessAlertDialog(context, title, "完成", null);
    }

    /**
     * 成功Alert对话框
     */
    public static Dialog showSuccessAlertDialog(Context context, String title, String ok, View.OnClickListener okListener) {
        View view = View.inflate(context, R.layout.dialog_success, null);
        DialogViewHolder holder = new DialogViewHolder(view);

        Dialog dialog = new Dialog(context, R.style.FDialog);
        dialog.setContentView(view);
        holder.tv_title.setText(title);
        if (ok != null) holder.tv_ok.setText(ok);
        holder.tv_ok.setOnClickListener(v -> {
            dialog.dismiss();
            if (okListener != null) okListener.onClick(v);
        });
        dialog.show();

        return dialog;
    }
//    /**
//     * sfa引导对话框
//     */
//    public static @Nullable
//    Dialog showGuideAlertDialog(Context context, String label, int image, String title, String ok, View.OnClickListener okListener) {
//        if (GuideHelper.getLabelCount(context, label) > 0) {
//            return null;
//        }
//        GuideHelper.addLabelCount(context,label);
//        View view = View.inflate(context, R.layout.dialog_guide, null);
//        DialogViewHolder holder = new DialogViewHolder(view);
//        Dialog dialog = new Dialog(context, R.style.FDialog);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setContentView(view);
//        holder.iv_guide.setImageResource(image);
//        holder.tv_title.setText(title);
//        if (ok != null) holder.tv_ok.setText(ok);
//        holder.tv_ok.setOnClickListener(v -> {
//            dialog.dismiss();
//            if (okListener != null) okListener.onClick(v);
//        });
//        dialog.show();
//
//        return dialog;
//    }
    /**
     * 默认失败Alert对话框
     */
    public static Dialog showErrorAlertDialog(Context context, String title, String content) {
        return showErrorAlertDialog(context, title, content, "完成", null);
    }

    /**
     * 失败Alert对话框
     */
    public static Dialog showErrorAlertDialog(Context context, String title, String content, String ok, View.OnClickListener okListener) {
        View view = View.inflate(context, R.layout.dialog_error, null);
        DialogViewHolder holder = new DialogViewHolder(view);

        Dialog dialog = new Dialog(context, R.style.FDialog);
        dialog.setContentView(view);
        holder.tv_title.setText(title);
        if (StringUtils.isEmpty(content)) {
            holder.tv_content.setVisibility(View.GONE);
        } else {
            holder.tv_content.setVisibility(View.VISIBLE);
            holder.tv_content.setText(content);
        }
        if (ok != null) holder.tv_ok.setText(ok);
        holder.tv_ok.setOnClickListener(v -> {
            dialog.dismiss();
            if (okListener != null) okListener.onClick(v);
        });
        dialog.show();

        return dialog;
    }

    static class DialogViewHolder {

        public View btnDivider;
        public TextView tv_title;
        public TextView tv_content;
        public TextView tv_cancel;
        public TextView tv_ok;
        public DialogViewHolder(View view) {
            btnDivider = view.findViewById(R.id.v_btn_divider);
            tv_title = view.findViewById(R.id.alert_tv_title);
            tv_content = view.findViewById(R.id.alert_tv_content);
            tv_cancel = view.findViewById(R.id.alert_tv_cancel);
            tv_ok = view.findViewById(R.id.alert_tv_ok);
        }

    }
}
