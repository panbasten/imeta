package com.panet.imeta.shared;

import com.panet.imeta.core.exception.KettleException;

public interface SharedObjectInterface {
	public void setShared(boolean shared);

	public boolean isShared();

	public String getName();

	public String getXML() throws KettleException;
}
