package com.github.nhirakawa.config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Throwables;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class JipCoreModule extends AbstractModule {

  @Override
  protected void configure() {
    try {
      List<String> lines = Resources.readLines(Resources.getResource("jip-core.properties"), Charset.defaultCharset());
      for (String line : lines) {
        String[] property = line.split("=");
        System.out.println(Arrays.toString(property));
        binder().bind(Key.get(Integer.class, Names.named(property[0]))).toInstance(Integer.parseInt(property[1]));
      }
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

}
