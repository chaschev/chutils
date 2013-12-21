package chaschev.util;

import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * To use you will need to:
 *
 * 1. Copy build.properties found into filtered resources root to your modules.
 * 2. Add maven buildNumber plugin.
 * 3. Define property <timestamp>${maven.build.timestamp}</timestamp>
 *
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class RevisionInfo {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(RevisionInfo.class);

    private String artifactId;
    private String revision;
    private String version;

    private String buildTimestamp;

    private volatile static RevisionInfo instance;

    private final Class aClass;

    public RevisionInfo() {
        aClass = null;
    }

    private RevisionInfo(Class aClass)

    {
        this.aClass = aClass;
    }

    public RevisionInfo init() {
        final String moduleName = "build";

        InputStream is = null;
        try {
            final Properties props = new Properties();

            is = aClass.getResourceAsStream("/" + moduleName + ".properties");
            props.load(is);

            revision = props.getProperty("project.revision");
            version = props.getProperty("project.version");
            buildTimestamp = props.getProperty("project.buildTimestamp");
            artifactId = props.getProperty("project.artifactId");
        } catch (Exception e) {
            logger.warn("Failed to read " + moduleName + ".properties", e);
            revision = "<No revision info available>";
            throw Exceptions.runtime(e);
        }

        return this;
    }

    public String getRevision() {
        return revision;
    }

    public String getVersion() {
        return version;
    }

    public String getBuildTimestamp() {
        return buildTimestamp;
    }

    public String getArtifactId() {
        return artifactId;
    }

    @Override
    public String toString() {
        return String.format("%s v%s r%s %s", artifactId, version, revision, getBuildDate());
    }

    public String toShortString() {
        return String.format("%s v%s %s", artifactId, version, getBuildDate());
    }

    public String getBuildDate() {
        if (buildTimestamp.matches("\\d+")) {
            return new SimpleDateFormat().format(new Date(Long.parseLong(buildTimestamp)));
        } else {
            return buildTimestamp;
        }
    }

    public static RevisionInfo get(Class aClass) {
        if (instance == null) {
            synchronized (RevisionInfo.class){
                if(instance == null){
                    instance = new RevisionInfo(aClass).init();
                }
            }
        }

        return instance;
    }

    public static void main(String[] args) {
        System.out.println(RevisionInfo.get(RevisionInfo.class).toString());
    }
}