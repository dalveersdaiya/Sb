package in.ajm.sb_library.chips;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Example of custom chip image renderer that uses Glide
 * to load images.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class GlideRenderer implements ChipImageRenderer {
    @Override
    public void renderAvatar(ImageView imageView, Chip chip) {
        if (chip.getAvatarUri() != null) {
            Glide.with(imageView.getContext())
                    .load(chip.getAvatarUri())
                    .into(imageView);
        } else {
            imageView.setImageBitmap(LetterTileProvider
                    .getInstance(imageView.getContext())
                    .getLetterTile(chip.getTitle()));
        }
    }
}