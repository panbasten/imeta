package com.plywet.mm.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MMFileProperties implements MMPropertiesProvider {

	private Map props;

	private boolean hasFile = false;

	private String filePath;

	public MMFileProperties(String path) {
		this.filePath = path;
		try {
			File f = new File(this.filePath);
			if (!f.exists()) {
				f.createNewFile();
			}
			if (f.length() == 0L) {
				this.props = new HashMap();
				this.hasFile = false;
			} else {
				FileInputStream is = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(is);
				this.props = (Map) ois.readObject();
				ois.close();
				is.close();
				this.hasFile = true;
			}

		} catch (Exception e) {
			this.props = new HashMap();
			this.hasFile = false;
		}
	}

	@Override
	public Object get(int key) {
		return this.props.get(Integer.valueOf(key));
	}

	@Override
	public void put(int key, Object value) {
		this.props.put(Integer.valueOf(key), value);

		if (!this.hasFile) {
			try {
				FileOutputStream os = new FileOutputStream(this.filePath);
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(this.props);
				oos.close();
				os.flush();
				os.close();
			} catch (IOException e) {

			}
		}
	}
}
