package app.patuhmobile.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import app.patuhmobile.R;

/**
 * Created by Fahmi Hakim on 03/06/2018.
 * for SERA
 */

public class DialogUtils {


    public static void showDialog(Activity activity, String msg, int view, int textView, int imglayout, String urlimage, int btnConfirm){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(view);

        TextView text = (TextView) dialog.findViewById(textView);
        text.setText(msg);

        ImageView pp = (ImageView) dialog.findViewById(imglayout);
        Glide.with(activity)
                .load(urlimage)
                .animate(R.anim.abc_fade_in)
                .centerCrop()
                .into(pp);

        Button dialogButton = (Button) dialog.findViewById(btnConfirm);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public static void showDialog(ProgressDialog dialog, String message, boolean isExist){
        if (isExist){
            dialog.dismiss();
        } else {
            dialog.setMessage(message);
            dialog.setCancelable(false);
            dialog.show();
        }
    }




    public static void showToast(String msg, Context ctx){
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
}
