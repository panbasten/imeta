/* Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.*/

/**
 *   Kettle was (re-)started in March 2003
 */

package com.panet.imeta.pan;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;

import com.panet.imeta.core.Const;
import com.panet.imeta.core.database.Database;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.parameters.NamedParams;
import com.panet.imeta.core.parameters.NamedParamsDefault;
import com.panet.imeta.core.util.EnvUtil;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.job.JobEntryLoader;
import com.panet.imeta.repository.RepositoriesMeta;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryDirectory;
import com.panet.imeta.repository.RepositoryMeta;
import com.panet.imeta.repository.UserInfo;
import com.panet.imeta.trans.StepLoader;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.version.BuildVersion;

public class Pan {
	public static void main(String[] a) throws KettleException {
		EnvUtil.environmentInit();

		List<String> args = new ArrayList<String>();
		for (int i = 0; i < a.length; i++) {
			if (a[i].length() > 0) {
				args.add(a[i]);
			}
		}

		RepositoryMeta repinfo = null;
		UserInfo userinfo = null;
		Trans trans = null;

		// The options:
		StringBuffer optionRepname, optionUsername, optionPassword, optionTransname, optionDirname, optionFilename, optionLoglevel;
		StringBuffer optionLogfile, optionLogfileOld, optionListdir, optionListtrans, optionListrep, optionExprep, optionNorep, optionSafemode, optionVersion, optionJarFilename;
		NamedParams optionParams = new NamedParamsDefault();

		CommandLineOption options[] = new CommandLineOption[] {
				new CommandLineOption("rep", Messages
						.getString("Pan.ComdLine.RepName"),
						optionRepname = new StringBuffer()),
				new CommandLineOption("user", Messages
						.getString("Pan.ComdLine.RepUsername"),
						optionUsername = new StringBuffer()),
				new CommandLineOption("pass", Messages
						.getString("Pan.ComdLine.RepPassword"),
						optionPassword = new StringBuffer()),
				new CommandLineOption("trans", Messages
						.getString("Pan.ComdLine.TransName"),
						optionTransname = new StringBuffer()),
				new CommandLineOption("dir", Messages
						.getString("Pan.ComdLine.RepDir"),
						optionDirname = new StringBuffer()),
				new CommandLineOption("file", Messages
						.getString("Pan.ComdLine.XMLTransFile"),
						optionFilename = new StringBuffer()),
				new CommandLineOption("level", Messages
						.getString("Pan.ComdLine.LogLevel"),
						optionLoglevel = new StringBuffer()),
				new CommandLineOption("logfile", Messages
						.getString("Pan.ComdLine.LogFile"),
						optionLogfile = new StringBuffer()),
				new CommandLineOption("log", Messages
						.getString("Pan.ComdLine.LogOldFile"),
						optionLogfileOld = new StringBuffer(), false, true),
				new CommandLineOption("listdir", Messages
						.getString("Pan.ComdLine.ListDirRep"),
						optionListdir = new StringBuffer(), true, false),
				new CommandLineOption("listtrans", Messages
						.getString("Pan.ComdLine.ListTransDir"),
						optionListtrans = new StringBuffer(), true, false),
				new CommandLineOption("listrep", Messages
						.getString("Pan.ComdLine.ListReps"),
						optionListrep = new StringBuffer(), true, false),
				new CommandLineOption("exprep", Messages
						.getString("Pan.ComdLine.ExpObjectsXML"),
						optionExprep = new StringBuffer(), true, false),
				new CommandLineOption("norep", Messages
						.getString("Pan.ComdLine.NoRep"),
						optionNorep = new StringBuffer(), true, false),
				new CommandLineOption("safemode", Messages
						.getString("Pan.ComdLine.SafeMode"),
						optionSafemode = new StringBuffer(), true, false),
				new CommandLineOption("version", Messages
						.getString("Pan.ComdLine.Version"),
						optionVersion = new StringBuffer(), true, false),
				new CommandLineOption("jarfile", Messages
						.getString("Pan.ComdLine.JarFile"),
						optionJarFilename = new StringBuffer(), false, true),
				new CommandLineOption("param", Messages
						.getString("Pan.ComdLine.Param"), optionParams, true), };

		if (args.size() == 0) {
			CommandLineOption.printUsage(options);
			exitJVM(9);
		}

		LogWriter log;
		LogWriter.setConsoleAppenderDebug();
		// start with the default logger until we find out otherwise
		log = LogWriter.getInstance(LogWriter.LOG_LEVEL_BASIC);

		// Parse the options...
		if (!CommandLineOption.parseArguments(args, options, log)) {
			log.logError("Pan", Messages
					.getString("Pan.Error.CommandLineError"));

			exitJVM(8);
		}

		String kettleRepname = Const.getEnvironmentVariable(
				"KETTLE_REPOSITORY", null);
		String kettleUsername = Const.getEnvironmentVariable("KETTLE_USER",
				null);
		String kettlePassword = Const.getEnvironmentVariable("KETTLE_PASSWORD",
				null);

		if (kettleRepname != null && kettleRepname.length() > 0)
			optionRepname = new StringBuffer(kettleRepname);
		if (kettleUsername != null && kettleUsername.length() > 0)
			optionUsername = new StringBuffer(kettleUsername);
		if (kettlePassword != null && kettlePassword.length() > 0)
			optionPassword = new StringBuffer(kettlePassword);

		if (Const.isEmpty(optionLogfile) && !Const.isEmpty(optionLogfileOld)) {
			// if the old style of logging name is filled in, and the new one is
			// not
			// overwrite the new by the old
			optionLogfile = optionLogfileOld;
		}

		if (!Const.isEmpty(optionLogfile)) {
			log = LogWriter.getInstance(optionLogfile.toString(), true,
					LogWriter.LOG_LEVEL_BASIC);
		}

		if (!Const.isEmpty(optionLoglevel)) {
			log.setLogLevel(optionLoglevel.toString());
			log.logMinimal("Pan", Messages.getString("Pan.Log.Loglevel", log
					.getLogLevelLongDesc()));

		}

		if (!Const.isEmpty(optionVersion)) {
			BuildVersion buildVersion = BuildVersion.getInstance();
			if (log.isBasic())
				log.logBasic("Pan", Messages.getString("Pan.Log.KettleVersion",
						buildVersion.getVersion(), buildVersion.getRevision(),
						buildVersion.getBuildDate()));

			if (a.length == 1)
				exitJVM(6);
		}

		// ///////////////////////////////////////////////////////////////////////////////////////////////////
		// This is where the action starts.
		// Print the options before we start processing when running in Debug or
		// Rowlevel
		//
		// TODO: include the named parameters
		if (log.isDebug()) {
			System.out.println("Arguments:");
			for (int i = 0; i < options.length; i++) {
				/* if (!options[i].isHiddenOption()) */
				System.out.println(Const.rightPad(options[i].getOption(), 12)
						+ " : " + options[i].getArgument());
			}
			System.out.println("");
		}
		// ///////////////////////////////////////////////////////////////////////////////////////////////////

		log.logMinimal("Pan", Messages.getString("Pan.Log.StartingToRun"));

		/* Load the plugins etc. */
		try {
			StepLoader.init();
		} catch (KettleException e) {
			log.logError("Pan", Messages
					.getString("Pan.Error.LoadingStepsHaltPan"));

			exitJVM(8);
		}

		/* Load the plugins etc. */
		try {
			JobEntryLoader.init();
		} catch (KettleException e) {
			log.logError("Pan", Messages
					.getString("Pan.Error.LoadingJobEntriesHaltPan"), e);

			exitJVM(8);
		}

		Date start, stop;
		Calendar cal;
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		cal = Calendar.getInstance();
		start = cal.getTime();

		if (log.isDebug())
			log
					.logDebug("Pan", Messages
							.getString("Pan.Log.AllocatteNewTrans"));

		TransMeta transMeta = new TransMeta();

		try {
			if (log.isDebug())
				log.logDebug("Pan", Messages
						.getString("Pan.Log.StartingToLookOptions"));

			// Read kettle transformation specified on command-line?
			if (!Const.isEmpty(optionRepname) || !Const.isEmpty(optionFilename)
					|| !Const.isEmpty(optionJarFilename)) {
				if (log.isDebug())
					log.logDebug("Pan", Messages
							.getString("Pan.Log.ParsingCommandline"));

				if (!Const.isEmpty(optionRepname)
						&& !"Y".equalsIgnoreCase(optionNorep.toString())) {
					if (log.isDebug())
						log.logDebug("Pan", Messages
								.getString("Pan.Log.LoadingAvailableRep"));

					RepositoriesMeta repsinfo = new RepositoriesMeta(log);
					if (repsinfo.readData()) {
						if (log.isDebug())
							log.logDebug("Pan", Messages.getString(
									"Pan.Log.FindingRep", "" + optionRepname));

						repinfo = repsinfo.findRepository(optionRepname
								.toString());
						if (repinfo != null) {
							// Define and connect to the repository...
							if (log.isDebug())
								log
										.logDebug(
												"Pan",
												Messages
														.getString("Pan.Log.Allocate&ConnectRep"));

							Repository rep = new Repository(log, repinfo);
							if (rep.connect("Pan commandline")) {
								RepositoryDirectory directory = rep
										.getDirectoryTree(); // Default =
								// root

								// Find the directory name if one is
								// specified...
								if (!Const.isEmpty(optionDirname)) {
									directory = rep.getDirectoryTree()
											.findDirectory(
													optionDirname.toString());
								}

								if (directory != null) {
									// Check username, password
									if (log.isDebug())
										log
												.logDebug(
														"Pan",
														Messages
																.getString("Pan.Log.CheckSuppliedUserPass"));

									userinfo = new UserInfo(rep, optionUsername
											.toString(), optionPassword
											.toString());
									if (userinfo.getID() > 0) {
										// Load a transformation
										if (!Const.isEmpty(optionTransname)) {
											if (log.isDebug())
												log
														.logDebug(
																"Pan",
																Messages
																		.getString("Pan.Log.LoadTransInfo"));

											transMeta = new TransMeta(rep,
													optionTransname.toString(),
													directory);
											if (log.isDebug())
												log
														.logDebug(
																"Pan",
																Messages
																		.getString("Pan.Log.AllocateTrans"));

											trans = new Trans(transMeta);
											trans.setRepository(rep);
										} else
										// List the transformations in the
										// repository
										if ("Y"
												.equalsIgnoreCase(optionListtrans
														.toString())) {
											if (log.isDebug())
												log
														.logDebug(
																"Pan",
																Messages
																		.getString(
																				"Pan.Log.GettingListTransDirectory",
																				""
																						+ directory));

											String transnames[] = rep
													.getTransformationNames(directory
															.getID());
											for (int i = 0; i < transnames.length; i++) {
												System.out
														.println(transnames[i]);
											}
										} else
										// List the directories in the
										// repository
										if ("Y".equalsIgnoreCase(optionListdir
												.toString())) {
											String dirnames[] = rep
													.getDirectoryNames(directory
															.getID());
											for (int i = 0; i < dirnames.length; i++) {
												System.out.println(dirnames[i]);
											}
										} else
										// Export the repository
										if (!Const.isEmpty(optionExprep)) {
											System.out
													.println(Messages
															.getString(
																	"Pan.Log.ExportingObjectsRepToFile",
																	""
																			+ optionExprep));

											rep.exportAllObjects(null,
													optionExprep.toString(),
													directory, "all");
											System.out
													.println(Messages
															.getString(
																	"Pan.Log.FinishedExportObjectsRepToFile",
																	""
																			+ optionExprep));
										} else {
											System.out
													.println(Messages
															.getString("Pan.Error.NoTransNameSupplied"));
										}
									} else {
										System.out
												.println(Messages
														.getString("Pan.Error.CanNotVerifyUserPass"));
										userinfo = null;
										repinfo = null;
									}
								} else {
									System.out
											.println(Messages
													.getString(
															"Pan.Error.CanNotFindSpecifiedDirectory",
															"" + optionDirname));
									userinfo = null;
									repinfo = null;
								}
								rep.disconnect();
							} else {
								System.out
										.println(Messages
												.getString("Pan.Error.CanNotConnectRep"));
							}
						} else {
							System.out.println(Messages
									.getString("Pan.Error.NoRepProvided"));
						}
					} else {
						System.out.println(Messages
								.getString("Pan.Error.NoRepsDefined"));
					}
				}

				// Try to load the transformation from file, even if it failed
				// to load from the repository
				// You could implement some fail-over mechanism this way.
				//
				if (trans == null && !Const.isEmpty(optionFilename)) {
					if (log.isDetailed())
						log
								.logDetailed("Pan", Messages.getString(
										"Pan.Log.LoadingTransXML", ""
												+ optionFilename));

					transMeta = new TransMeta(optionFilename.toString());
					trans = new Trans(transMeta);
				}

				// Try to load the transformation from a jar file
				// 
				if (trans == null && !Const.isEmpty(optionJarFilename)) {
					try {
						if (log.isDetailed())
							log.logDetailed("Pan", Messages.getString(
									"Pan.Log.LoadingTransJar", ""
											+ optionJarFilename));

						InputStream inputStream = Pan.class
								.getResourceAsStream(optionJarFilename
										.toString());
						StringBuffer xml = new StringBuffer();
						int c;
						while ((c = inputStream.read()) != -1)
							xml.append((char) c);
						inputStream.close();
						Document document = XMLHandler.loadXMLString(xml
								.toString());
						transMeta = new TransMeta(XMLHandler.getSubNode(
								document, "transformation"), null);
						trans = new Trans(transMeta);
					} catch (Exception e) {
						System.out.println(Messages.getString(
								"Pan.Error.ReadingJar", e.toString()));

						System.out.println(Const.getStackTracker(e));
						throw e;
					}
				}
			}

			if ("Y".equalsIgnoreCase(optionListrep.toString())) {
				if (log.isDebug())
					log.logDebug("Pan", Messages
							.getString("Pan.Log.GettingListReps"));

				RepositoriesMeta ri = new RepositoriesMeta(log);
				if (ri.readData()) {
					System.out.println(Messages.getString("Pan.Log.ListReps"));

					for (int i = 0; i < ri.nrRepositories(); i++) {
						RepositoryMeta rinfo = ri.getRepository(i);
						System.out.println(Messages.getString(
								"Pan.Log.RepNameDesc", "" + (i + 1), rinfo
										.getName(), rinfo.getDescription()));
					}
				} else {
					System.out.println(Messages
							.getString("Pan.Error.UnableReadXML"));
				}
			}
		} catch (Exception e) {
			trans = null;
			transMeta = null;
			System.out.println(Messages.getString("Pan.Error.ProcessStopError",
					e.getMessage()));

			e.printStackTrace();
			exitJVM(1);
		}

		if (trans == null) {
			if (!"Y".equalsIgnoreCase(optionListtrans.toString())
					&& !"Y".equalsIgnoreCase(optionListdir.toString())
					&& !"Y".equalsIgnoreCase(optionListrep.toString())
					&& Const.isEmpty(optionExprep)) {
				System.out.println(Messages
						.getString("Pan.Error.CanNotLoadTrans"));

				exitJVM(7);
			} else {
				exitJVM(0);
			}

		}

		try {
			trans.initializeVariablesFrom(null);
			trans.getTransMeta().setInternalKettleVariables(trans);

			// Map the command line named parameters to the actual named
			// parameters. Skip for
			// the moment any extra command line parameter not known in the
			// transformation.
			String[] transParams = trans.listParameters();
			for (String param : transParams) {
				String value = optionParams.getParameterValue(param);
				if (value != null) {
					trans.setParameterValue(param, value);
				}
			}
			// Put the parameters over the already defined variable space.
			// Parameters
			// get priority.
			trans.activateParameters();

			// See if we want to run in safe mode:
			if ("Y".equalsIgnoreCase(optionSafemode.toString())) {
				trans.setSafeModeEnabled(true);
			}

			// allocate & run the required sub-threads
			try {
				trans.execute((String[]) args.toArray(new String[args.size()]));
			} catch (KettleException e) {
				System.out.println(Messages
						.getString("Pan.Error.UnablePrepareInitTrans"));

				exitJVM(3);
			}

			trans.waitUntilFinished();
			trans.endProcessing(Database.LOG_STATUS_END);

			log.logMinimal("Pan", Messages.getString("Pan.Log.Finished"));

			cal = Calendar.getInstance();
			stop = cal.getTime();
			String begin = df.format(start).toString();
			String end = df.format(stop).toString();

			log.logMinimal("Pan", Messages.getString("Pan.Log.StartStop",
					begin, end));

			long millis = stop.getTime() - start.getTime();
			long seconds = millis / 1000;
			if (seconds <= 60) {
				log.logMinimal("Pan", Messages.getString(
						"Pan.Log.ProcessingEndAfter", String.valueOf(seconds)));
			} else if (seconds <= 60 * 60) {
				int min = (int) (seconds / 60);
				int rem = (int) (seconds % 60);
				log.logMinimal("Pan", Messages.getString(
						"Pan.Log.ProcessingEndAfterLong", String.valueOf(min),
						String.valueOf(rem), String.valueOf(seconds)));
			} else if (seconds <= 60 * 60 * 24) {
				int rem;
				int hour = (int) (seconds / (60 * 60));
				rem = (int) (seconds % (60 * 60));
				int min = rem / 60;
				rem = rem % 60;
				log.logMinimal("Pan", Messages.getString(
						"Pan.Log.ProcessingEndAfterLonger", String
								.valueOf(hour), String.valueOf(min), String
								.valueOf(rem), String.valueOf(seconds)));
			} else {
				int rem;
				int days = (int) (seconds / (60 * 60 * 24));
				rem = (int) (seconds % (60 * 60 * 24));
				int hour = rem / (60 * 60);
				rem = rem % (60 * 60);
				int min = rem / 60;
				rem = rem % 60;
				log.logMinimal("Pan", Messages.getString(
						"Pan.Log.ProcessingEndAfterLongest", String
								.valueOf(days), String.valueOf(hour), String
								.valueOf(min), String.valueOf(rem), String
								.valueOf(seconds)));
			}

			if (trans.getResult().getNrErrors() == 0) {
				trans.printStats((int) millis / 1000);
				exitJVM(0);
			} else {
				exitJVM(1);
			}
		} catch (KettleException ke) {
			System.out.println(Messages.getString("Pan.Log.ErrorOccurred", ""
					+ ke.getMessage()));

			log.logError("Pan", Messages.getString(
					"Pan.Log.UnexpectedErrorOccurred", "" + ke.getMessage()));

			// Close the file appender if any...
			//
			LogWriter.closeAndRemoveFileAppender();

			exitJVM(2);
		}

	}

	private static final void exitJVM(int status) {
		// Close the open appenders...
		//
		LogWriter.getInstance().close();

		System.exit(status);
	}
}
