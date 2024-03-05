package com.example.railinfo.data.xml;

import android.util.Xml;

import com.example.railinfo.data.objects.StationData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * A custom XMLParser designed to read the Station information returned from the National Rail API
 *
 * @author Sam Townley
 * @version 1.0
 */
public class StationsXMLParser {

    /** The namespace of the XML. In this case, it is null throughout. */
    private static final String namespace = null;

    /**
     * Parses XML data from a given input stream.
     *
     * @param in The input stream obtained from the HTTPS connection.
     * @return A StationData object containing the relevant station information.
     * @throws XmlPullParserException An exception occurred whilst parsing the XML.
     * @throws IOException An exception occurred whilst reading from the input.
     */
    public StationData parse(BufferedInputStream in) throws XmlPullParserException, IOException {
        try {
            // Create a new parser
            XmlPullParser parser = Xml.newPullParser();

            // Turn off parsing namespaces
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            // Set input and get next tag (Station tag)
            parser.setInput(in, null);
            parser.nextTag();

            // Read station data
            return readStation(parser);

        } finally {
            // Close the input stream once it's been finished with
            in.close();
        }
    }

    /**
     * Reads the necessary fields from the XML and adds the data to a new StationData object.
     *
     * @param parser The XML parser instance to use.
     * @return A StationData object containing the relevant station information.
     * @throws XmlPullParserException An exception occurred whilst parsing the XML.
     * @throws IOException An exception occurred whilst reading from the input.
     */
    private StationData readStation(XmlPullParser parser) throws XmlPullParserException, IOException {
        // Construct a new empty StationData object.
        StationData station = new StationData();

        // Require that the feed has the correct starting tag before parsing the data.
        parser.require(XmlPullParser.START_TAG, namespace, "StationV4.0");

        // Keep going through the XML until you reach the end of the file
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            // Get the current tag name and call the appropriate function to read the data
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

    /**
     * Reads the text from inside of the specified tag.
     *
     * @param parser The XML parser instance to use.
     * @param tag The name of the tag to read.
     * @return The text inside the XML tag.
     * @throws IOException An exception occurred whilst reading from the input.
     * @throws XmlPullParserException An exception occurred whilst parsing the XML.
     */
    private String readText(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        // Require the name of tag passed in before reading the text.
        parser.require(XmlPullParser.START_TAG, namespace, tag);

        // Read the text into a string
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        // Require the end name tag to make sure the text has been read correctly.
        parser.require(XmlPullParser.END_TAG, namespace, tag);
        return result;
    }

    /**
     * Constructs a full, human-readable address string from the address tag.
     *
     * @param parser The XML parser instance to use.
     * @return The combined, full address string from inside the XML tags.
     * @throws IOException An exception occurred whilst reading from the input.
     * @throws XmlPullParserException An exception occurred whilst parsing the XML.
     */
    private String readAddress(XmlPullParser parser) throws IOException, XmlPullParserException {

        // Require the address tag before reading in the address and construct a new StringBuilder.
        parser.require(XmlPullParser.START_TAG, namespace, "Address");
        StringBuilder result = new StringBuilder();

        // Navigate through the tags until reaching the actual address lines.
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, namespace, "com:PostalAddress");
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, namespace, "add:A_5LineAddress");

        // Read each line one by one, adding it to the string as it goes.
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

        // Require the end tags on the way out to make sure the parser exits the address tag.
        parser.require(XmlPullParser.END_TAG, namespace, "add:A_5LineAddress");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, namespace, "com:PostalAddress");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, namespace, "Address");
        return result.toString();
    }

    /**
     * Skips the current tag if it doesn't contain any important data.
     *
     * @param parser The XML parser instance to use.
     * @throws XmlPullParserException An exception occurred whilst parsing the XML.
     * @throws IOException An exception occurred whilst reading from the input.
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        // Throw an exception if trying to skip a tag that isn't the start tag.
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        // Skip through to the end of the current tag if it has multiple depths to it.
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
