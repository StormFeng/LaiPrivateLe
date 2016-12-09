package com.lailem.app.adapter.datasource;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;

import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.bean.Contact;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.ui.me.ContactsActivity;
import com.lailem.app.utils.CharacterParserUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ContacsDataSource extends BaseListDataSource<Object> {

	private static final String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
	private static final int PHONES_NUMBER_INDEX = 1;
	private static final int PHONES_PHOTO_ID_INDEX = 2;
	private static final int PHONES_CONTACT_ID_INDEX = 3;
	private ArrayList<Contact> localContacts = new ArrayList<Contact>();

	public ContacsDataSource(Context context) {
		super(context);
		buildPhoneContacts();
	}

	@Override
	protected ArrayList<Object> load(int page) throws Exception {
		ArrayList<Object> models = new ArrayList<Object>();

		for (int i = 0; i < localContacts.size(); i++) {
			String imageUri = localContacts.get(i).getImageUri();
			String name = localContacts.get(i).getName();
			String beforLetter = "";
			if (i > 0) {
				beforLetter = localContacts.get(i - 1).getLetter();
			}
			String curletter = localContacts.get(i).getLetter();
			String nickName = curletter;// temp

			if (i == 0 || beforLetter.charAt(0) != curletter.charAt(0)) {
				models.add(new ObjectWrapper((curletter.charAt(0) + "").toUpperCase(), BaseMultiTypeListAdapter.TPL_SECTION));
			}

			models.add(new Contact(imageUri, name, nickName, curletter, ContactsActivity.TPL_CONTACT));
		}

		hasMore = false;
		return models;
	}

	private void buildPhoneContacts() {
		ContentResolver resolver = _activity.getContentResolver();
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
				Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
				String photoUri = "";
				if (photoid > 0) {
					Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
					photoUri = uri.toString();
				}
				Contact contact = new Contact();
				contact.setImageUri(photoUri);
				contact.setPhone(phoneNumber);
				contact.setName(contactName);
				contact.setLetter(CharacterParserUtils.getInstance().getSelling(contactName).toUpperCase());
				localContacts.add(contact);
			}
			phoneCursor.close();
		}
		Collections.sort(localContacts, new Comparator<Contact>() {

			@Override
			public int compare(Contact lhs, Contact rhs) {
				return lhs.getLetter().charAt(0) - rhs.getLetter().charAt(0);
			}
		});
	}

}
