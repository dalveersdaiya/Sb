package in.ajm.sb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.ajm.sb.R;
import in.ajm.sb_library.chips.Chip;
import in.ajm.sb_library.chips.ChipDataSource;
import in.ajm.sb_library.chips.ContactChip;

/**
 * DSD
 *
 * @author DSD
 * @version 1.1
 */
public class ContactOnChipAdapter extends RecyclerView.Adapter<ContactOnChipAdapter.Holder> implements ChipDataSource.ChangeObserver {
    private final OnContactClickListener listener;
    private ChipDataSource chipDataSource;


    public ContactOnChipAdapter(OnContactClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return chipDataSource == null ? 0 : chipDataSource.getFilteredChips().size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.chip_view_filterable, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Chip chip = chipDataSource.getFilteredChips().get(position);
        holder.text.setText(chip.getTitle());
        holder.subtitle.setText(chip.getSubtitle());
        if (chip.getAvatarUri() != null) {
            holder.image.setImageURI(chip.getAvatarUri());
        } else if (chip.getAvatarDrawable() != null) {
            holder.image.setImageDrawable(chip.getAvatarDrawable());
        } else {
            holder.image.setImageDrawable(null);
        }
    }

    @Override
    public void onChipDataSourceChanged() {
        notifyDataSetChanged();
    }

    public void setChipDataSource(ChipDataSource chipDataSource) {
        this.chipDataSource = chipDataSource;
        notifyDataSetChanged();
    }

    public interface OnContactClickListener {
        void onContactClicked(ContactChip chip);
    }


    public  class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text, subtitle;
        ImageView image;

        Holder(View v) {
            super(v);
            this.text = v.findViewById(R.id.title);
            this.subtitle = v.findViewById(R.id.subtitle);
            this.image = v.findViewById(R.id.image);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Chip clickedChip = chipDataSource.getFilteredChip(getAdapterPosition());

        }
    }
}