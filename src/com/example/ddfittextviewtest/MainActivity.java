package com.example.ddfittextviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.dd.views.DDFitTextView;

public class MainActivity extends Activity {
	private LayoutInflater inflater;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inflater = LayoutInflater.from(this);
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new MyAdapter());
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 8;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = inflater.inflate(R.layout.item, null);
			DDFitTextView tv = (DDFitTextView) v.findViewById(R.id.tv);
			setIconAndTextByPosition(tv, position);
			return v;
		}
	}

	private void setIconAndTextByPosition(DDFitTextView tv, int position) {
		switch (position) {
		case 0:
			tv.setText("内容内容内容内容内容内容内容内容");
			break;
		case 1:
			tv.setText("内容内容内容内容内容内容内容内容内容内");
			tv.setIconAndText(new int[] { R.drawable.comment }, "999");
			break;
		case 2:
			tv.setText("内容内容内容内容内容内容内容内容内容内容");
			tv.setIconAndText(new int[] { R.drawable.comment }, "999");
			break;
		case 3:
			tv.setText("内容内容内容内容内容内容内容内容内容内容内容内容内容内容");
			tv.setIconAndText(new int[] { R.drawable.comment }, "999");
			break;
		case 4:
			tv.setText("内容内容内容内容内容");
			tv.setIconAndText(new int[] { R.drawable.comment, R.drawable.comment }, "999", "666");
			break;
		case 5:
			tv.setText("内容内容内容内容内容内容内容内容内容内容内容内容内容内容");
			tv.setIconAndText(new int[] { R.drawable.comment, R.drawable.comment }, "999", "666");
			break;
		case 6:
			tv.setText("内容内容内容内容内容内容内容内容内容内容内容内容内容内内容内容内容内容内容内");
			tv.setIconAndText(new int[] { R.drawable.comment, R.drawable.comment }, "999", "666");
			break;
		case 7:
			tv.setText("内容内容内容内容内容内容内容内容内容内容内容内容内容内内容内容内容内容内容内容内容内容内容内");
			tv.setIconAndText(new int[] { R.drawable.comment, R.drawable.comment }, "999", "666");
			break;
		default:
			break;
		}
	}

}
