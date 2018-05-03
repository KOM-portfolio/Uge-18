package dk.sdu.mmmi.cbse.asteroidnbmodules;

import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.io.IOException;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import junit.framework.Test;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;
import org.openide.util.Lookup;

public class ApplicationTest extends NbTestCase {

    // TODO: Put your own paths here...
    private static final String ADD_ENEMY_UPDATES_FILE = "C:/Users/chris/Desktop/Development/KOM-Labs/Uge-18/AsteroidNBModules/application/src/test/java/resources/enemy/updates.xml";
    private static final String REM_ENEMY_UPDATES_FILE = "C:/Users/chris/Desktop/Development/KOM-Labs/Uge-18/AsteroidNBModules/application/src/test/java/resources/delenemy/updates.xml";
    private static final String UPDATES_FILE = "C:/Users/chris/Desktop/Development/KOM-Labs/Uge-18/netbeans_site/updates.xml";

    public static Test suite() {
        return NbModuleSuite.createConfiguration(ApplicationTest.class).
                gui(false).
                failOnMessage(Level.WARNING). // works at least in RELEASE71
                failOnException(Level.INFO).
                enableClasspathModules(false).
                clusters(".*").
                suite(); // RELEASE71+, else use NbModuleSuite.create(NbModuleSuite.createConfiguration(...))
    }

    public ApplicationTest(String n) {
        super(n);
    }

    public void testApplication() throws InterruptedException, IOException {

        // SETUP
        List<IEntityProcessingService> processors = new CopyOnWriteArrayList<>();
        List<IGamePluginService> plugins = new CopyOnWriteArrayList<>();

        waitForUpdate(processors, plugins, 10000);

        // PRE ASSERTS
        //Size should be 0 because no modules are installed.
        assertEquals("No plugins", 0, processors.size());
        assertEquals("No processors", 0, plugins.size());

        // TEST: Load Enemy via UC
        copy(get(ADD_ENEMY_UPDATES_FILE), get(UPDATES_FILE), REPLACE_EXISTING);
        System.out.println("Copied " + ADD_ENEMY_UPDATES_FILE + " - Waiting for update!!!");

        waitForUpdate(processors, plugins, 10000);

        // ASSERTS: Enemy loaded
        assertEquals("One plugins", 1, plugins.size());
        assertEquals("One processors", 1, processors.size());

        // TEST: Unload Enemy via UC
        copy(get(REM_ENEMY_UPDATES_FILE), get(UPDATES_FILE), REPLACE_EXISTING);

        waitForUpdate(processors, plugins, 10000);

        // ASSERTS: Enemy unloaded
        assertEquals("No plugins", 0, plugins.size());
        assertEquals("No processors", 0, processors.size());
    }

    private void waitForUpdate(List<IEntityProcessingService> processors, List<IGamePluginService> plugins, long millis) throws InterruptedException {
        // Needs time for silentUpdater to install all modules
        Thread.sleep(millis);

        processors.clear();
        processors.addAll(Lookup.getDefault().lookupAll(IEntityProcessingService.class));

        plugins.clear();
        plugins.addAll(Lookup.getDefault().lookupAll(IGamePluginService.class));
    }

}