package sundeep.yahoo.com.gridsearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import sundeep.yahoo.com.gridsearch.R;
import sundeep.yahoo.com.gridsearch.models.ImageResult;

/**
 * Created by josephr on 9/11/14.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context, android.R.layout.simple_list_item_1, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imgInfo = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }

        ImageView ivImg = (ImageView) convertView.findViewById(R.id.iv_resultImage);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
        //clear image
        ivImg.setImageResource(0);
        tvTitle.setText(Html.fromHtml(imgInfo.title));
        Picasso.with(getContext()).load(imgInfo.thumbUrl).into(ivImg);
        return convertView;

    }
}
