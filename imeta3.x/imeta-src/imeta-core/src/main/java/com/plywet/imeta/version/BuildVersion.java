package com.plywet.imeta.version;

import java.net.JarURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import com.plywet.imeta.utils.Const;
import com.plywet.imeta.utils.DateUtils;

/**
 * Singleton class to allow us to see on which date & time the kettle3.jar was
 * built.
 * 
 * @author Matt
 * @since 2006-aug-12
 */
public class BuildVersion {
	public static final String REFERENCE_FILE = "/kettle-steps.xml";

	private static BuildVersion buildVersion;

	/**
	 * @return the instance of the BuildVersion singleton
	 */
	public static final BuildVersion getInstance() {
		if (buildVersion != null)
			return buildVersion;

		buildVersion = new BuildVersion();

		return buildVersion;
	}

	private String version;
	private String revision;
	private String buildDate;
	private String buildUser;

	private BuildVersion() {
		try {
			URL url = this.getClass().getResource(REFERENCE_FILE);
			JarURLConnection jarConnection = (JarURLConnection) url
					.openConnection();
			Manifest manifest = jarConnection.getManifest();

			version = manifest.getMainAttributes().getValue(
					Attributes.Name.IMPLEMENTATION_VERSION);
			revision = manifest.getMainAttributes().getValue(
					Attributes.Name.SPECIFICATION_VERSION);
			buildDate = manifest.getMainAttributes().getValue(
					"Compile-Timestamp");
			buildUser = manifest.getMainAttributes().getValue("Compile-User");
		} catch (Exception e) {
			// System.out.println("Unable to read version information from manifest : not running from jar files (Igored)");

			version = Const.VERSION;
			revision = "";
			buildDate = DateUtils.dat2str(new Date());
			buildUser = "";
		}
	}

	/**
	 * @return the buildDate
	 */
	public String getBuildDate() {
		return buildDate;
	}

	/**
	 * @param buildDate
	 *            the buildDate to set
	 */
	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param revision
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the revision
	 */
	public String getRevision() {
		return revision;
	}

	/**
	 * @param revision
	 *            the revision to set
	 */
	public void setRevision(String revision) {
		this.revision = revision;
	}

	/**
	 * @return the buildUser
	 */
	public String getBuildUser() {
		return buildUser;
	}

	/**
	 * @param buildUser
	 *            the buildUser to set
	 */
	public void setBuildUser(String buildUser) {
		this.buildUser = buildUser;
	}
}
