package com.github.nhirakawa.jip.config;

import com.github.nhirakawa.config.JipCoreModule;
import com.google.inject.AbstractModule;

public class JipUiModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new JipCoreModule());
  }
}
