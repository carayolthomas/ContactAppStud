package utils;

import com.example.contactsappstud.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class is associated with the ItemRide one in order to display an icon on
 * each row of a list
 * 
 * @author Thomas
 * 
 */
public class ItemContactAdapter extends ArrayAdapter<String> {

	/** The context */
	private final Context mContext;

	/** Ids */
	private final String[] mIds;

	/** Row ressource id */
	private final int mRowResourceId;

	/**
	 * Default constructor
	 * 
	 * @param pContext
	 * @param pTextViewResourceId
	 * @param pObjects
	 */
	public ItemContactAdapter(Context pContext, int pTextViewResourceId,
			String[] pObjects) {

		super(pContext, pTextViewResourceId);

		this.mContext = pContext;
		this.mIds = pObjects;
		this.mRowResourceId = pTextViewResourceId;

	}

	@Override
	public View getView(int pPosition, View pConvertView, ViewGroup pParent) {

		LayoutInflater lInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = lInflater.inflate(mRowResourceId, pParent, false);
		ImageView lImageView = (ImageView) rowView
				.findViewById(R.id.imageViewContactRow);
		TextView lTextView = (TextView) rowView
				.findViewById(R.id.textViewContactRow);

		ItemContact lItem = new ItemContact(mIds[pPosition],
				R.drawable.icon_list_contact);

		lTextView.setText(lItem.getItemRideText());
		lTextView.setTextColor(Color.parseColor("#575451"));
		lImageView.setImageResource(lItem.getItemContactIconFile());
		return rowView;

	}

	@Override
	public int getCount() {
		return mIds.length;
	}

	@Override
	public String getItem(int pPosition) {
		return mIds[pPosition];
	}
}