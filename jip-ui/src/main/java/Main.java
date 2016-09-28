import java.io.IOException;

import com.github.nhirakawa.config.JipCoreModule;
import com.github.nhirakawa.emulator.JipEmulator;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

  public static void main(String... args) throws IOException {
    Injector injector = Guice.createInjector(new JipCoreModule());
    JipEmulator emulator = injector.getInstance(JipEmulator.class);
  }
}
