package com.aegeanflow.core;

import com.aegeanflow.core.definition.BoxDefinition;
import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.core.spi.AnnotatedBox;
import com.aegeanflow.core.spi.Plugin;
import com.google.inject.Inject;
import com.gorkemgok.annoconf.guice.Initiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by gorkem on 18.01.2018.
 */
public class BoxRepository implements Initiable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoxRepository.class);

    private final List<BoxInfo> boxInfoList;

    private final Set<Plugin> plugins;

    @Inject
    public BoxRepository(Set<Plugin> plugins) {
        this.plugins = plugins;
        boxInfoList = new ArrayList<>();
    }

    public void register(Class<? extends AnnotatedBox> boxClass) throws IllegalNodeConfigurationException {
        BoxInfo boxInfo = CompilerUtil.compile(boxClass);
        boxInfoList.add(boxInfo);
    }

    public List<BoxDefinition> getBoxDefinitionList(){
        return boxInfoList.stream()
                .map(nodeInfo -> nodeInfo.getDefinition())
                .collect(Collectors.toList());
    }

    public List<BoxInfo> getBoxInfoList() {
        return Collections.unmodifiableList(boxInfoList);
    }

    @Override
    public void init() {
        plugins.stream().flatMap(plugin -> plugin.provideAnnotatedBoxes().stream())
                .forEach(boxClass -> {
                    try {
                        register(boxClass);
                        LOGGER.info("Registered {}", boxClass.getName());
                    } catch (IllegalNodeConfigurationException e) {
                        LOGGER.error("Cant register annotatedBox {}. {}", boxClass.getName(), e.getMessage());
                    }
                });
    }
}
