package edu.hkust.leap.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.thoughtworks.xstream.XStream;

//import edu.hkust.leap.replay.utils.*;

public class Serializer {
	
	
	
	

	
	
	// Use XStream to serialize the stream
	static XStream xstream = new XStream();

	static public Object loadObject(Reader reader) {
		return xstream.fromXML(reader);
	}


	public static void storeObject(Object object, Writer out) {
		xstream.toXML(object, out);
	}

	public static String toXMLString(Object object) {
		if (object == null) {
			return null;
		}
		return xstream.toXML(object);//xstream.toXML(object,Parameters.CLONE_DEPTH);
	}

	public static byte[] toXMLZippedByteArray(Object object) throws IOException {
		if (object == null) {
			return null;
		}

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		GZIPOutputStream gzOut = new GZIPOutputStream(bout);

		xstream.toXML(object, gzOut);
		gzOut.close();
		bout.close();
		return bout.toByteArray();
	}

	public static Object loadObject(byte[] gzippedByteArray) throws IOException {
		if (gzippedByteArray == null) {
			return null;
		}
		ByteArrayInputStream bin = new ByteArrayInputStream(gzippedByteArray);
		GZIPInputStream gzIn = new GZIPInputStream(bin);

		Object toRet = xstream.fromXML(gzIn);
		
		gzIn.close();
		bin.close();
		
		return toRet;
	}
	
	
	static public Object loadObject(String xmlString) {
		return xstream.fromXML(xmlString);
	}
}
