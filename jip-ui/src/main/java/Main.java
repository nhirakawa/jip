import java.io.IOException;
import java.net.URISyntaxException;

import com.github.nhirakawa.config.JipCoreModule;
import com.github.nhirakawa.emulator.JipEmulator;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

  public static void main(String... args) throws IOException, URISyntaxException {
    Injector injector = Guice.createInjector(new JipCoreModule());
    JipEmulator emulator = injector.getInstance(JipEmulator.class);
    emulator.loadRom(Resources.getResource("MAZE.rom").toURI());
    emulator.step();
    emulator.step();
  }
}
