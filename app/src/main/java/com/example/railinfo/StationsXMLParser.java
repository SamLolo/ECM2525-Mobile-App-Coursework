package com.example.railinfo;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;

public class StationsXMLParser {
    private static final String namespace = null;

    public StationData parse(BufferedInputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readStation(parser);
        } finally {
            in.close();
        }
    }

    private StationData readStation(XmlPullParser parser) throws XmlPullParserException, IOException {
        StationData station = new StationData();

        parser.require(XmlPullParser.START_TAG, namespace, "StationV4.0");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "CrsCode":
                    station.setCrs(readText(parser, name));
                    break;
                case "Name":
                    station.setName(readText(parser, name));
                    break;
                case "Address":
                    station.setAddress(readAddress(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return station;
    }

    private String readText(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespace, tag);
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, namespace, tag);
        return result;
    }

    private String readAddress(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespace, "Address");
        StringBuilder result = new StringBuilder();

        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, namespace, "com:PostalAddress");
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, namespace, "add:A_5LineAddress");

        while (parser.nextTag() == XmlPullParser.START_TAG) {
            String tag = parser.getName();
            if (parser.next() == XmlPullParser.TEXT) {
                result.append(parser.getText());
                if (Objects.equals(tag, "add:Line")) {
                    result.append(", ");
                }
            }
            parser.nextTag();
        }

        parser.require(XmlPullParser.END_TAG, namespace, "add:A_5LineAddress");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, namespace, "com:PostalAddress");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, namespace, "Address");
        return result.toString();
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
