package app.patuhmobile.module.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.patuhmobile.R;
import app.patuhmobile.model.Kupon;
import app.patuhmobile.module.Home.Panutan.PanutanFragment;

/**
 * Created by Fahmi Hakim on 01/06/2018.
 * for SERA
 */

public class RecViewAdapter extends RecyclerView.Adapter<RecViewAdapter.MahasiswaViewHolder> {


    private ArrayList<PanutanFragment.Mahasiswa> dataList;

    public RecViewAdapter(ArrayList<PanutanFragment.Mahasiswa> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MahasiswaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MahasiswaViewHolder holder, int position) {
        holder.txtTitle.setText(dataList.get(position).getJudul());
        holder.imgProfile.setImageResource(R.drawable.patuhcontent);
        holder.txtLocation.setText(dataList.get(position).getLokasi());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class MahasiswaViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitle, txtLocation;
        private ImageView imgProfile;

        public MahasiswaViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            imgProfile = (ImageView) itemView.findViewById(R.id.img_profile);
            txtLocation = (TextView) itemView.findViewById(R.id.txt_location);
        }
    }
}
