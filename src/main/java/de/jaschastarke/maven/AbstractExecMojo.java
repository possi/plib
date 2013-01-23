package de.jaschastarke.maven;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/*
 * 8[11:42] trygvis: Possible: the trick is to just get the MavenProject instance and loop over project.getCompileArtifacts()
 * 8[11:43] trygvis: those files will be resolved if you have @requiresDependencyResolution tese
 * 8[12:08] rfscholte: Possible: FYI, if you use MavenProject.getCompileClasspathElements() you'll get list a list of File paths
 */

/**
 * Builds up a classpath to get classloader for generated files working, requires
 * "@requiresDependencyResolution compile" in doc comments of mojo-class.
 * 
 * Source: https://svn.codehaus.org/mojo/trunk/mojo/exec-maven-plugin/src/main/java/org/codehaus/mojo/exec/ExecJavaMojo.java
 *  & https://svn.codehaus.org/mojo/trunk/mojo/exec-maven-plugin/src/main/java/org/codehaus/mojo/exec/AbstractExecMojo.java
 * @author Jascha
 *
 */
abstract public class AbstractExecMojo extends AbstractMojo {
    /**
     * Defines the scope of the classpath passed to the plugin. Set to compile,test,runtime or system depending on your
     * needs. Since 1.1.2, the default value is 'runtime' instead of 'compile'.
     * 
     * @parameter expression="${exec.classpathScope}" default-value="runtime"
     */
    protected String classpathScope;
    
    /**
     * The enclosing project.
     * 
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * Collects the project artifacts in the specified List and the project specific classpath (build output and build
     * test output) Files in the specified List, depending on the plugin classpathScope value.
     * 
     * @param artifacts the list where to collect the scope specific artifacts
     * @param theClasspathFiles the list where to collect the scope specific output directories
     */
    @SuppressWarnings( "unchecked" )
    protected void collectProjectArtifactsAndClasspath(List<Artifact> artifacts, List<File> theClasspathFiles) {
        if ("compile".equals(classpathScope)) {
            artifacts.addAll(project.getCompileArtifacts());
            theClasspathFiles.add(new File(project.getBuild().getOutputDirectory()));
        } else if ("test".equals( classpathScope)) {
            artifacts.addAll( project.getTestArtifacts() );
            theClasspathFiles.add(new File(project.getBuild().getTestOutputDirectory()));
            theClasspathFiles.add(new File(project.getBuild().getOutputDirectory()));
        } else if ("runtime".equals(classpathScope)) {
            artifacts.addAll( project.getRuntimeArtifacts() );
            theClasspathFiles.add(new File(project.getBuild().getOutputDirectory()));
        } else if ("system".equals(classpathScope)) {
            artifacts.addAll( project.getSystemArtifacts() );
        } else {
            throw new IllegalStateException("Invalid classpath scope: " + classpathScope);
        }
        
        getLog().debug("Collected project artifacts " + artifacts);
        getLog().debug("Collected project classpath " + theClasspathFiles);
    }


    /* *
     * Determine all plugin dependencies relevant to the executable.
     * Takes includePlugins, and the executableDependency into consideration.
     *
     * @return a set of Artifact objects.
     *         (Empty set is returned if there are no relevant plugin dependencies.)
     * @throws MojoExecutionException if a problem happens resolving the plufin dependencies
     * /
    private Set<Artifact> determineRelevantPluginDependencies()
        throws MojoExecutionException {
        Set<Artifact> relevantDependencies;
        //if ( this.includePluginDependencies ) {
            if ( this.executableDependency == null )
            {
                getLog().debug( "All Plugin Dependencies will be included." );
                relevantDependencies = new HashSet<Artifact>( this.pluginDependencies );
            }
            else
            {
                getLog().debug( "Selected plugin Dependencies will be included." );
                Artifact executableArtifact = this.findExecutableArtifact();
                Artifact executablePomArtifact = this.getExecutablePomArtifact( executableArtifact );
                relevantDependencies = this.resolveExecutableDependencies( executablePomArtifact );
            }
        /*}
        else
        {
            relevantDependencies = Collections.emptySet();
            getLog().debug( "Plugin Dependencies will be excluded." );
        }* /
        return relevantDependencies;
    }//*/

    
    /* *
     * Add any relevant project dependencies to the classpath.
     * Indirectly takes includePluginDependencies and ExecutableDependency into consideration.
     *
     * @param path classpath of {@link java.net.URL} objects
     * @throws MojoExecutionException if a problem happens
     * /
    private void addRelevantPluginDependenciesToClasspath( List<URL> path )
        throws MojoExecutionException {
        try {
            for ( Artifact classPathElement : this.determineRelevantPluginDependencies() )
            {
                getLog().debug(
                    "Adding plugin dependency artifact: " + classPathElement.getArtifactId() + " to classpath" );
                path.add( classPathElement.getFile().toURI().toURL() );
            }
        } catch (MalformedURLException e) {
            throw new MojoExecutionException( "Error during setting up classpath", e );
        }
    }//*/

    /**
     * Add any relevant project dependencies to the classpath.
     * Takes includeProjectDependencies into consideration.
     *
     * @param path classpath of {@link java.net.URL} objects
     * @throws MojoExecutionException if a problem happens
     */
    protected void addRelevantProjectDependenciesToClasspath(List<URL> path)
        throws MojoExecutionException {
        //if (this.includeProjectDependencies) {
            try {
                getLog().debug("Project Dependencies will be included.");

                List<Artifact> artifacts = new ArrayList<Artifact>();
                List<File> theClasspathFiles = new ArrayList<File>();
 
                collectProjectArtifactsAndClasspath(artifacts, theClasspathFiles);

                for (File classpathFile : theClasspathFiles) {
                     URL url = classpathFile.toURI().toURL();
                     getLog().debug("Adding to classpath : " + url);
                     path.add(url);
                }

                for (Artifact classPathElement : artifacts) {
                    getLog().debug("Adding project dependency artifact: " +
                                        classPathElement.getArtifactId() + " to classpath");
                    path.add(classPathElement.getFile().toURI().toURL());
                }
            } catch (MalformedURLException e) {
                throw new MojoExecutionException("Error during setting up classpath", e);
            }
        /*} else {
            getLog().debug("Project Dependencies will be excluded.");
        }*/
    }
}
