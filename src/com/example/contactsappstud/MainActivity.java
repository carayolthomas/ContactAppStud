package com.example.contactsappstud;

import java.util.ArrayList;
import java.util.List;

import utils.ItemContactAdapter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	/** The button in order to refresh the contact list */
	private Button mRefreshContactsButton;
	
	/** The list view for all the contacts */
	private ListView mListContactsView;
	
	/** The contacts list */
	private List<Contact> mListContacts;
	
	/** The contacts list in String */
	private List<String> mListContactsInfosToString;
	
	/** The contacts controller in order to retrieve the JSON file */
	private ContactsController mContactsController;
	
	/** View for loading screen */
	private ProgressDialog mDialogLoadingContacts;
	
	/** The Async Task to get all the contacts in background */
	private GetAllContactsTask mGetAllContactsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mContactsController = new ContactsController();
        
        mListContactsView = (ListView) findViewById(R.id.listViewContacts);
        mRefreshContactsButton = (Button) findViewById(R.id.buttonRefresh);
        
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
	 * AsyncTask in order to get all the contacts to the following url's
	 */
	public class GetAllContactsTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			mContactsController.init();
			mListContacts = mContactsController.findAll();
			if(mListContacts.size() != 0) {	
				mListContactsInfosToString = new ArrayList<String>();
				for (Contact lC : mListContacts) {
					mListContactsInfosToString.add(lC.toString());
				}
				return true;
			} else {
				return false;
			}
			
		}

		@Override
		protected void onPostExecute(final Boolean pSuccess) {
			if(!pSuccess) {
				Toast.makeText(MainActivity.this, "Problem while loading contacts !", Toast.LENGTH_LONG).show();
			} else {
				/* Fill the listview */
				ItemContactAdapter lAdapter = new ItemContactAdapter(
						MainActivity.this,
						R.layout.item_contact_row,
						mListContactsInfosToString
								.toArray(new String[mListContactsInfosToString.size()]));
				mListContactsView.setAdapter(lAdapter);
				/** Cancel the loading dialog */
				dismissLoadingWaitDialog();
				Toast.makeText(MainActivity.this, "New contacts loaded !", Toast.LENGTH_LONG).show();
			}
		}
	}
    
}
