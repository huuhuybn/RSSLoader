package com.poly.rssloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // lay du lieu tu rss vnexpress

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                getDataFromRSS();
                return null;
            }
        };
        asyncTask.execute();


    }

    public void getDataFromRSS() {
        String diaChi = "https://vnexpress.net/rss/tin-moi-nhat.rss";
        try {
            URL url = new URL(diaChi);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            pullParser.setInput(inputStream, "utf-8");

            int eventType = pullParser.getEventType();
            String text = "";
            TinTuv tinTuv = null;
            List<TinTuv> list = new ArrayList<>();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = pullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (name.equalsIgnoreCase("item")) {
                            tinTuv = new TinTuv();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = pullParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tinTuv != null) {
                            if (name.equalsIgnoreCase("title")) {
                                Log.e("title", text);
                                tinTuv.title = text;
                            } else if (name.equalsIgnoreCase("description")) {
                                Log.e("description", text);
                                tinTuv.des = text;
                            } else if (name.equalsIgnoreCase("pubDate")) {
                                Log.e("pubDate", text);
                                tinTuv.pubDate = text;
                            } else if (name.equalsIgnoreCase("link")) {
                                Log.e("link", text);
                                tinTuv.link = text;
                            } else if (name.equalsIgnoreCase("item")) {
                                list.add(tinTuv);
                            }
                        }
                        break;
                }

                eventType = pullParser.next();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Link sai format!!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "ko co ket noi mang!!!", Toast.LENGTH_SHORT).show();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


    }

}