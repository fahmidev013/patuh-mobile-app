package app.patuhmobile.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import app.patuhmobile.App;
import app.patuhmobile.AppConfig;
import app.patuhmobile.R;
import app.patuhmobile.auth.AuthInfo;
import app.patuhmobile.model.Artikel;
import app.patuhmobile.model.Komentar;
import app.patuhmobile.utils.NetworkUtils;

/**
 * Created by Fahmi Hakim on 05/08/2018.
 * for SERA
 */

public class KomenAdapter extends RecyclerView.Adapter<KomenAdapter.SingleItemRowHolder> {

    public interface onClickLIstener{
        void onItemClicked(int position);
    }

    private ArrayList<Komentar> itemsList;
    private Context mContext;
    private App mApp;
    private AuthInfo session;
    private KomenAdapter.onClickLIstener mListener;

    public KomenAdapter(Context context, ArrayList<Komentar> itemsList, KomenAdapter.onClickLIstener callback) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.mListener = callback;
        mApp = App.get(context);
        session = mApp.getAuthInfo();
    }

    private View v;

    @Override
    public KomenAdapter.SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_list, viewGroup, false);
        KomenAdapter.SingleItemRowHolder mh = new KomenAdapter.SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(KomenAdapter.SingleItemRowHolder holder, int pos) {
        Komentar item = itemsList.get(pos);
        holder.title.setText(item.getUserId());
        holder.location.setText(item.getComment());
        if (session.getUserId() != null){
            if (NetworkUtils.isOnline(mApp.getBaseContext())){
                Glide.with(mContext)
                        .load(AppConfig.API_BASE_URL + "UserProfile/GetProfilePic?UserId=" + item.getUserId())
                        .animate(R.anim.abc_fade_in)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(holder.imageResource);
            }

        }
        //holder.imageResource.setImageDrawable(mContext.getDrawable(R.drawable.pic_galgadot));
        /*holder.btnContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClicked(pos);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView title, location;
        protected ImageView imageResource;
        //protected ImageView btnContent;
        public SingleItemRowHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.txt_title);
            this.location = (TextView) view.findViewById(R.id.txt_location);
            this.imageResource = (ImageView) view.findViewById(R.id.img_profile);
            //this.btnContent = (ImageView) view.findViewById(R.id.btn_content);

        }



    }
}
