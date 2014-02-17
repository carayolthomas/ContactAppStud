package com.example.contactsappstud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import utils.JSONRetriever;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class MainActivity extends Activity {
	
	/** JSON URL */
	private static final String URL = "http://www.appstud.me/test_recrutement/contacts.json";
	
	/** JSON Node names */
    private static final String TAG_CONTACTS = "contacts";
    private static final String TAG_NOM = "nom";
    private static final String TAG_PRENOM = "prenom";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PICTURE = "picture";
	
	/** The button in order to refresh the contact list */
	private Button mRefreshContactsButton;
	
	/** The list view for all the contacts */
	private ListView mListContactsView;
	
	/** The contacts list */
	public static ArrayList<Contact> mListContacts;
	
	/** The contacts list in String (for display) */
	private List<String> mListContactsInfosToString;
	
	/** View for loading screen */
	private ProgressDialog mDialogLoadingContacts;
	
	/** The Async Task to get all the contacts in background */
	private GetAllContactsTask mGetAllContactsTask;
	
	/** Picture's URL */
	private String[] mPicturesURL;
	
	/** Some options for the picture loader */
	DisplayImageOptions options;
	
	/** The image loader */
	ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) { 		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Configuration of the ImageLoader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.getBaseContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.build();
		// Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
		
		// Allow to run the getData method below 
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        
        // First retrieve & parse data
        getData();
        
        // Some options for put all the pictures in cache
        options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20))
				.build();
           
        // The ListView for all the contacts
        mListContactsView = (ListView) findViewById(R.id.listViewContacts);
        // Allow to load data in cache
        mListContactsView.setAdapter(new ItemAdapter());
        // Allow to click on a contact to send an email
        mListContactsView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("CONTACT", mListContacts.get(position).getEmail());
				String lEmail = mListContacts.get(position).getEmail();
				String[] mails = { lEmail };
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("plain/text");
				i.putExtra(Intent.EXTRA_EMAIL, mails);
				startActivity(Intent.createChooser(i,"Choix de l'application"));
			}
		});
        
        // The refresh button
        mRefreshContactsButton = (Button) findViewById(R.id.buttonRefresh);
        // Define the reload function
        mRefreshContactsButton.setOnClickListener(
    		new Button.OnClickListener() {
    			public void onClick(View v) {
    				/** Loading page */
    				createLoadingWaitDialog("Contacts are loading. Please wait...");
    				/** Refresh all the contacts */
    				mListContacts = new ArrayList<Contact>();
    				mGetAllContactsTask = new GetAllContactsTask();
    				mGetAllContactsTask.execute();     				
    			}
    		}
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
	 * Create a dialog view for waiting
	 * @param pMessage
	 */
	public void createLoadingWaitDialog(String pMessage) {
		mDialogLoadingContacts = ProgressDialog.show(this, "", 
				pMessage, true);
		mDialogLoadingContacts.setCancelable(false);
	}

	/**
	 * Dismiss the loading dialog view
	 */
	public void dismissLoadingWaitDialog() {
		mDialogLoadingContacts.dismiss();
	}
    
	/**
	 * Our data retriever & parser function
	 * @return true if loading is successfull !
	 */
	private boolean getData() {
		// Initialize the list of contacts
		mListContacts = new ArrayList<Contact>();
		// Retrieve the JSON as a String
		String lJsonStrContacts = JSONRetriever.getContactsAppStud(URL);
		Log.i("JSON RESPONSE", lJsonStrContacts);
		
		
        if(lJsonStrContacts != null) {
        	try {
        		// Parse the JSON & Create the list of associated class
				JSONObject lJsonObjContacts = new JSONObject(lJsonStrContacts);
				JSONArray contacts = lJsonObjContacts.getJSONArray(TAG_CONTACTS);
				for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                     
                    String nom = c.getString(TAG_NOM);
                    String prenom = c.getString(TAG_PRENOM);
                    String email = c.getString(TAG_EMAIL);
                    String picture = c.getString(TAG_PICTURE);
                    
                    mListContacts.add(new Contact(nom, prenom, picture, email));
                    // Sort the list by the name of contacts
                    Collections.sort(mListContacts);
                    
				}
				// A little trick to fill an array with all the picture URL
				mPicturesURL = new String[mListContacts.size()];
				for (int i = 0; i < mListContacts.size(); i++) {
					mPicturesURL[i] = mListContacts.get(i).getContactPhotoUrl();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
            
        	// Define return values
			if(mListContacts.size() != 0) {	
				return true;
			} else {
				return false;
			}
        }
        return false;
	}
    
    /**
	 * AsyncTask in order to get all the contacts to the following url's
	 */
	public class GetAllContactsTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return getData();
		}

		@Override
		protected void onPostExecute(final Boolean pSuccess) {
			if(!pSuccess) {
				Toast.makeText(MainActivity.this, "Problem while loading contacts !", Toast.LENGTH_LONG).show();
			} else {
				mListContactsView.setAdapter(new ItemAdapter());
				// Cancel the loading dialog 
				dismissLoadingWaitDialog();
				Toast.makeText(MainActivity.this, "New contacts loaded !", Toast.LENGTH_LONG).show();
			}
		}
	} 
	
	
	/**
	 * Class in order to define a row in the list of contacts with picture and text
	 * Just for information I reused it from https://github.com/nostra13/Android-Universal-Image-Loader
	 * @author Thomas
	 *
	 */
	class ItemAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = (ImageLoadingListener) new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView text;
			public ImageView image;
		}

		@Override
		public int getCount() {
			return mPicturesURL.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.item_list_image, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.text);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.text.setText(mListContacts.get(position).getContactFirstName() + " " + mListContacts.get(position).getContactLastName());

			imageLoader.displayImage(mPicturesURL[position], holder.image, options, animateFirstListener);

			return view;
		}
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
