package com.example.administrator.myapplication.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;


/**
 * Created by ray on 16/9/17.
 */
public class ToastUtills {
    private static Toast toast;

    public static void imageTosat(Context context , int imageId , String content , int duration){
        //new一个toast传入要显示的activity的上下文
        Toast toast = new Toast(context);
        //显示的时间
        toast.setDuration(duration);
        //显示的位置
        toast.setGravity(Gravity.CENTER, 0, 0);
        //重新给toast进行布局
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toastLayout = inflater.inflate(R.layout.toast_qr, null);
        toastLayout.setBackgroundResource(R.drawable.bg_toast);
        ImageView imageView = (ImageView)toastLayout.findViewById(R.id.image_icon);
        imageView.setImageResource(imageId);
        //把imageView添加到toastLayout的布局当中

        TextView textView = (TextView)toastLayout.findViewById(R.id.txt_message);
        textView.setText(content);
//        textView.setBackgroundColor(Color.GRAY);
        //把textView添加到toastLayout的布局当中
        //把toastLayout添加到toast的布局当中
        toast.setView(toastLayout);
        toast.show();
    }

    public static void showToast(Context context,
                                 String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }


    /*public static AlertDialog createDialog(Context context, String titleContent, final View.OnClickListener onClickListener){
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.home_alertdialog,null);
        AlertDialog myDialog = new AlertDialog.Builder(context).create();
        myDialog.setView(layout);
        TextView textView = (TextView)layout.findViewById(R.id.title);
        final TextView leftBtn = (TextView)layout.findViewById(R.id.btn_left);
//        final Button rightBtn = (Button)layout.findViewById(R.id.btn_right);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(view);
            }
        });
//        rightBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                rightclick.onClick(view);
//            }
//        });
        textView.setText(titleContent);
        return myDialog;
    }*/

/*    public static AlertDialog createDeleteDialog(Context context, int layoutid, String lefttext, String righttext, String titleContent, final View.OnClickListener onClickListener, final View.OnClickListener rightclick){
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(layoutid,null);
        AlertDialog myDialog = new AlertDialog.Builder(context).create();
        myDialog.setView(layout);
        TextView textView = (TextView)layout.findViewById(R.id.title);
        final TextView leftBtn = (TextView)layout.findViewById(R.id.btn_left);
        final TextView rightBtn = (TextView)layout.findViewById(R.id.btn_right);
        leftBtn.setText(lefttext);
        rightBtn.setText(righttext);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(view);
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightclick.onClick(view);
            }
        });
        textView.setText(titleContent);
        return myDialog;
    }*/
}


