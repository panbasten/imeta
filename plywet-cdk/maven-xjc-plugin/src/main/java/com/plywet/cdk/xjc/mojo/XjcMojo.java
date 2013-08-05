package com.plywet.cdk.xjc.mojo;

/**
 * 
 */
import java.io.File;
import java.io.StringReader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Mkdir;
import org.apache.tools.ant.types.FileSet;
import com.plywet.cdk.xjc.mant.MantGoal;

/**
 * xjc compiler plugin. This plugin generates JAXB source files from an XML
 * schema. The source files are generated in a directory under the project build
 * directory derived from the configured properties.
 * 
 * <p>
 * A word of caution: Your schema may be rejected if your text editor places
 * spurious whitespace characters at the start of the file - if you think your
 * schema is otherwise well formed then open it up in a hex editor and correct
 * as necessary.
 * </p>
 * 
 * @goal xjc
 * @phase generate-sources
 * @requiresDependencyResolution test
 */
public class XjcMojo extends AbstractMojo {
	/**
	 * The Maven project.
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * The xjc ant task, for example: &lt;xjc
	 * schema="src/main/resources/com/acme/services.xsd" package="com.acme"/&gt;
	 * Place inside a CDATA section - see README.txt for more information.
	 * 
	 * @parameter
	 * @required
	 */
	private String task;

	public void execute() throws MojoExecutionException, MojoFailureException {
		
		String[] mappings = new String[] { "@target", MantGoal.JAVA_GEN };
		MantGoal goal = new MantGoal(this, project,
				"com.sun.tools.xjc.XJCTask", task, mappings);
		createJavaGenDir(goal);
		goal.execute();
		//copyResources(goal);
	}

	/**
	 * The xjc task won't create the target directory so we create it here.
	 * 
	 * @param goal
	 */
	private void createJavaGenDir(MantGoal goal) {
		Mkdir mkdir = new Mkdir();
		Project project = new Project();
		project.init();
		mkdir.setProject(project);
		mkdir.setTaskName("mkdir");
		mkdir.setDir(new File(goal.getJavaGen()));
		mkdir.execute();
	}

	/**
	 * Copies the generated java to the generated resources. Easiest way to get
	 * the Configuration.xml and jaxb.properties files in the right place,
	 * although unfortunately all the java files are copied too.
	 * 
	 * @param goal
	 */
	private void copyResources(MantGoal goal) {
		Copy copy = new Copy();
		Project project = new Project();
		project.init();
		copy.setProject(project);
		copy.setTaskName("copy");
		copy.setTodir(new File(goal.getResGen()));
		FileSet fileSet = new FileSet();
		fileSet.setDir(new File(goal.getJavaGen()));
		copy.addFileset(fileSet);
		copy.execute();
	}
}