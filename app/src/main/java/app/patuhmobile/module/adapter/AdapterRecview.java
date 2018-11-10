package app.patuhmobile.module.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.patuhmobile.R;
import app.patuhmobile.model.Kupon;

/**
 * Created by Fahmi Hakim on 17/09/2018.
 * for SERA
 */
public class AdapterRecview extends RecyclerView.Adapter<AdapterRecview.MyViewHolder>{

    public interface KuponAdapterCallback {
        void onClicked(Kupon kupon, int position);

    }


    private List<Kupon> kupons;
    private AdapterRecview.KuponAdapterCallback mListener;

    public AdapterRecview(List<Kupon> kuponList, AdapterRecview.KuponAdapterCallback callback) {
        this.kupons = kuponList;
        this.mListener = callback;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_kupon, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        String title = kupons.get(i).getTitle();
        String poinNeed = kupons.get(i).getPointNeeded();
        myViewHolder.poin.setText(poinNeed);
        myViewHolder.judulKupon.setText(title);
        myViewHolder.cv.setOnClickListener( view -> {

            mListener.onClicked(kupons.get(i), i);


        });
    }

    @Override
    public int getItemCount() {
        return kupons.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView poin, judulKupon;
        CardView cv;

        public MyViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cv_kupon);
            poin = (TextView) itemView.findViewById(R.id.tv_poin);
            judulKupon = (TextView) itemView.findViewById(R.id.tv_kupon_title);
        }
    }

}
